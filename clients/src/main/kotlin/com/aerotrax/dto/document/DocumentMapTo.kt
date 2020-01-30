package com.aerotrax.dto.document

import com.aerotrax.states.DocumentState

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
            linearId = state.linearId.id.toString()
    )
}