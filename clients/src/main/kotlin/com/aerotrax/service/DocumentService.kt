package com.aerotrax.service

import com.aerotrax.dto.ResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

interface DocumentService {

    /** upload a file using spring boot **/
    fun uploadFile(file: MultipartFile, folderPath: String, linearId: String): ResponseEntity<ResponseDTO>

    /** get document image **/
    fun getDocumentImage(linearId: String): ResponseEntity<ResponseDTO>

}