package com.aerotrax.dto.arc

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.Instant

data class RegisterARCFlowDTO @JsonCreator constructor(
        val arcId: String,
        val remarks: String,
        val approvedReject: String,
        val createdBy: String
)

data class MainARCDTO(
        val companyId: String,
        val serialNumber: String,
        val productId: String,
        val status: String,
        val contractNumber: String?,
        val trackNumber: String?,
        val remarks: String?,
        val approved: Instant?,
        val disapproved: Instant?,
        val createdBy: String?,
        val createdAt: Instant?,
        val updatedAt: Instant?,
        val linearId: String
)