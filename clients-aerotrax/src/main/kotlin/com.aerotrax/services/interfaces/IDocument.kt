package com.aerotrax.services.interfaces

import com.aerotrax.dto.MainDocumentDTO
import org.springframework.web.multipart.MultipartFile

interface IDocument: IService {
    fun uploadFile(file: MultipartFile, folderPath: String, linearId: String):MainDocumentDTO
    fun getDocumentImage(linearId: String): String?
}