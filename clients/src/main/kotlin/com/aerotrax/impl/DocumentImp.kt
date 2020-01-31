package com.aerotrax.impl

import com.aerotrax.constant.AppConstants
import com.aerotrax.dto.ResponseDTO
import com.aerotrax.dto.document.mapToMainDocumentDTO
import com.aerotrax.flows.document.AddNewDocumentFlow
import com.aerotrax.service.DocumentService
import com.aerotrax.states.DocumentState
import com.aerotrax.util.Functions
import com.aerotrax.util.HandlerCompletion
import com.aerotrax.util.Response
import com.aerotrax.webserver.NodeRPCConnection
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.lang.IllegalArgumentException
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.security.MessageDigest

@Service
class DocumentImp (private val rpc: NodeRPCConnection,
                private val fhc: HandlerCompletion,
                private val function: Functions,
                private val response: Response): DocumentService {

    companion object {
        private val logger = LoggerFactory.getLogger(RestController::class.java)!!
    }

    override fun uploadFile(file: MultipartFile, folderType: String, linearId: String): ResponseEntity<ResponseDTO> {

        return try {

            val mainFolder = Paths.get(AppConstants.FILE_STORAGE_PATH_LOCAL)

            val dir = File("${mainFolder}/$folderType")
            // have the object build the directory structure, if needed.
            if (!dir.exists()) {
                dir.mkdirs()
            }

            val rootLocation = Paths.get("${AppConstants.FILE_STORAGE_PATH_LOCAL}/$folderType")

            val originalFileName = file.originalFilename!!
            Files.copy(file.inputStream, rootLocation.resolve(originalFileName), StandardCopyOption.REPLACE_EXISTING)

            //get hash
            val getUploadBytes = file.bytes
            val hash256 = MessageDigest.getInstance("SHA-256")
            val  digest = hash256.digest(getUploadBytes)
            val hash = BigInteger(1, digest).toString(16)

            val documentState: DocumentState = if (folderType == "image"){

                val companyState = function.companyState().find {
                    it.state.data.linearId.toString() == linearId
                }?: throw IllegalArgumentException("Id not found")

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
                fhc.handlerCompletion(addNewDocumentFlowReturn)
                function.returnDocumentState(addNewDocumentFlowReturn)
            }else{

                val productState = function.productState().find {
                    it.state.data.linearId.toString() == linearId
                }?: throw IllegalArgumentException("Id not found")

                val companyName = function.companyState().find {
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
                fhc.handlerCompletion(addNewDocumentFlowReturn)
                function.returnDocumentState(addNewDocumentFlowReturn)
            }

            response.successfulResponse(
                    response = mapToMainDocumentDTO(documentState),
                    message = "upload file"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "upload file"
            )
        }
    }

    override fun getDocumentImage(linearId: String): ResponseEntity<ResponseDTO> {

        return try {

            val documentState = function.documentState()
            val myDocuments =  documentState.find {
                it.state.data.companyId == linearId
            } ?: throw IllegalArgumentException("Company not found")

            response.successfulResponse(
                    response = mapToMainDocumentDTO(myDocuments.state.data),
                    message = "get document image"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "get document image"
            )
        }
    }
}