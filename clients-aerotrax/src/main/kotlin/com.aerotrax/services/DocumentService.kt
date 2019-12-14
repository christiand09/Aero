package com.aerotrax.services

import com.aerotrax.dto.AddDocumentFlowDTO
import com.aerotrax.dto.MainDocumentDTO
import com.aerotrax.dto.mapToMainCompanyDTO
import com.aerotrax.dto.mapToMainDocumentDTO
import com.aerotrax.flows.document.AddNewDocumentFlow
import com.aerotrax.flows.product.RegisterPartDetailFlow
import com.aerotrax.services.interfaces.IDocument
import com.aerotrax.states.CompanyState
import com.aerotrax.states.DocumentState
import com.aerotrax.util.AppConstants
import com.aerotrax.util.FlowHandlerCompletion
import com.aerotrax.util.Functions
import com.aerotrax.webserver.NodeRPCConnection
import liquibase.util.file.FilenameUtils
import net.corda.core.CordaException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.security.MessageDigest

@Service
class DocumentService (private val rpc: NodeRPCConnection, private val fhc: FlowHandlerCompletion, private val state: Functions): IDocument {
    override var currentCompanyId: String = AppConstants.DEFAULT_COMPANY_ID

    override fun setCurrentCompanyDetails(userId: String?) {
        currentCompanyId = userId ?: currentCompanyId
    }

    override fun uploadFile(file: MultipartFile, folderType: String, linearId: String): MainDocumentDTO {
        val mainFolder = Paths.get(AppConstants.FILE_STORAGE_PATH_LOCAL)

        val dir = File("${mainFolder}/$folderType")
        // have the object build the directory structure, if needed.
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val rootLocation = Paths.get("${AppConstants.FILE_STORAGE_PATH_LOCAL}/$folderType")

        val originalFileName = file.originalFilename!!
//        val fileExtension = FilenameUtils.getExtension(originalFileName)
//        val idRegex = linearId.replace("\\s".toRegex(),"_")
//        val filename = originalFileName.replace(originalFileName, "$idRegex.$fileExtension", true)
        Files.copy(file.inputStream, rootLocation.resolve(originalFileName), StandardCopyOption.REPLACE_EXISTING)

        //get hash
        val getUploadBytes = file.bytes
        val hash256 = MessageDigest.getInstance("SHA-256")
        val  digest = hash256.digest(getUploadBytes)
        val hash = BigInteger(1, digest).toString(16)

        val documentState: DocumentState = if (folderType == "image"){

            val companyState = state.companyState().find {
                it.state.data.linearId.toString() == linearId
            }?: throw CordaException("Id not found")

            val addNewDocumentFlowReturn = rpc.proxy.startFlowDynamic(
                    AddNewDocumentFlow::class.java,
                    linearId,
                    null,
                    null,
                    originalFileName,
                    file.inputStream.available().toString(),
                    "$rootLocation",
                    hash,
                    folderType,
                    companyState.state.data.name
                    )
            fhc.flowHandlerCompletion(addNewDocumentFlowReturn)
            state.returnDocumentState(addNewDocumentFlowReturn)
        }else{

            val productState = state.productState().find {
                it.state.data.linearId.toString() == linearId
            }?: throw CordaException("Id not found")

            val companyName = state.companyState().find {
                it.state.data.linearId.toString() == productState.state.data.companyId
            }?.state?.data?.name

            val addNewDocumentFlowReturn = rpc.proxy.startFlowDynamic(
                    AddNewDocumentFlow::class.java,
                    productState.state.data.companyId,
                    productState.state.data.serialNumber,
                    productState.state.data.linearId.toString(),
                    originalFileName,
                    file.inputStream.available().toString(),
                    "$rootLocation",
                    hash,
                    folderType,
                    companyName
            )
            fhc.flowHandlerCompletion(addNewDocumentFlowReturn)
            state.returnDocumentState(addNewDocumentFlowReturn)
        }
        return mapToMainDocumentDTO(documentState)
    }

    override fun getDocumentImage(linearId: String): String? {
        val com = get(linearId) as MainDocumentDTO
        if(com.name.isNullOrEmpty())
            throw CordaException("Company image not found")
        return com.name
    }

    override fun get(linearId: String): Any {
        val documentStateRef = state.documentState()
        val documentState = documentStateRef.find { stateAndRef ->
            stateAndRef.state.data.companyId == linearId
        } ?: throw CordaException("Company not found")
        return mapToMainDocumentDTO(documentState.state.data)
    }

}