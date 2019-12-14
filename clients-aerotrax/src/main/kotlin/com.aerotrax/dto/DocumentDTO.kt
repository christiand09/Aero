package com.aerotrax.dto

import com.aerotrax.states.DocumentState
import com.aerotrax.states.ProductState
import java.time.Instant

data class AddDocumentFlowDTO(
        val companyId: String,
        val serialNumber: String,
        val productId: String,
        val name: String,
        val size: String,
        val url: String,
        val hash: String,
        val type: String,
        val uploadedBy: String
)

data class MainDocumentDTO(
        val companyId: String,
        val serialNumber: String?,
        val productId: String?,
        val name: String,
        val size: String,
        val url: String,
        val hash: String,
        val type: String,
        val uploadedBy: String,
        val uploadedAt: Instant,
        val updatedAt: Instant?,
        val linearId: String
)

fun mapToMainDocumentDTO(state: DocumentState) : MainDocumentDTO{
    return MainDocumentDTO(
            companyId = state.companyId,
            serialNumber = state.serialNumber,
            productId = state.productId,
            name = state.name,
            size = state.size,
            url = state.url,
            hash = state.hash,
            type = state.type,
            uploadedBy = state.uploadedBy,
            uploadedAt = state.uploadedAt,
            updatedAt = state.updatedAt,
            linearId = state.linearId.toString()
    )
}