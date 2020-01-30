package com.aerotrax.dto.document

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.Instant

data class AddDocumentFlowDTO @JsonCreator constructor(
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