package com.aerotrax.controller

import com.aerotrax.dto.ResponseDTO
import com.aerotrax.services.DocumentService
import com.aerotrax.util.AppConstants
import com.aerotrax.webserver.NodeRPCConnection
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.StreamUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File

private const val CONTROLLER_NAME = "api/v1/document"

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(CONTROLLER_NAME) // The paths for HTTP requests are relative to this base path.
class DocumentController(private val documentService: DocumentService, private val rpcConnection: NodeRPCConnection) : BaseController() {

    @PostMapping(value = ["/upload/{path}/{linearId}"], produces = ["application/json"])
    private fun upload(@RequestParam file: MultipartFile, @PathVariable path: String, @PathVariable linearId: String): ResponseEntity<ResponseDTO> {
        return try {
            val response = documentService.uploadFile(file, path, linearId)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "upload"
            return this.handleException(e, identity, function)
        }
    }

    @GetMapping(value = ["/render/company/{linearId}"], produces = [MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE])
    private fun renderImage(@PathVariable linearId: String) : ResponseEntity<Any> {
        return try {
            val documentImage = this.documentService.getDocumentImage(linearId)
            val imgFile = File("${AppConstants.FILE_STORAGE_PATH_LOCAL}/image/$documentImage")
            val response = StreamUtils.copyToByteArray(imgFile.inputStream())
            ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(response)
        } catch (e: Exception) {
            return this.handleExceptionRender(e)
        }
    }
}