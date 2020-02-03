package com.aerotrax.controller

import com.aerotrax.dto.ResponseDTO
import com.aerotrax.service.DocumentService
import io.swagger.annotations.ApiOperation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

private const val CONTROLLER_NAME = "/document"

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(CONTROLLER_NAME) /** The paths for HTTP requests are relative to this base path. **/
class DocumentController (private val documentService: DocumentService) {

    /************************************/
    /*****     Upload A File      *******/
    /************************************/
    @PostMapping(value = ["/upload/{path}/{linearId}"], produces = ["application/json"])
    @ApiOperation(value = "Upload A File")
    private fun uploadFile(@RequestParam file: MultipartFile, @PathVariable path: String, @PathVariable linearId: String): ResponseEntity<ResponseDTO> {
        return documentService.uploadFile(file, path, linearId)
    }

    /***********************************/
    /*****     Render Image      *******/
    /***********************************/
    @PostMapping(value = ["/render/company/{linearId}"], produces = [MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE])
    @ApiOperation(value = "Upload A File")
    private fun getDocumentImage(@PathVariable linearId: String): ResponseEntity<ResponseDTO> {
        return documentService.getDocumentImage(linearId)
    }

}