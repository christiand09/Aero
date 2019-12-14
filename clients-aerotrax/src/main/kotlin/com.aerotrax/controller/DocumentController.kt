package com.aerotrax.controller

import com.aerotrax.dto.ResponseDTO
import com.aerotrax.services.DocumentService
import com.aerotrax.webserver.NodeRPCConnection
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

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
}