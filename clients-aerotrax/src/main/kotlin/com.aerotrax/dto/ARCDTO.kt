package com.aerotrax.dto

import com.aerotrax.states.ARCState
import java.time.Instant

data class RegisterARCFlowDTO(
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

fun mapToMainARCDTO(state: ARCState) : MainARCDTO{
    return MainARCDTO(
            companyId = state.companyId,
            serialNumber = state.serialNumber,
            productId = state.productId,
            status = state.status,
            contractNumber = state.contractNumber,
            trackNumber = state.trackNumber,
            remarks = state.remarks,
            approved = state.approved,
            disapproved = state.disapproved,
            createdBy = state.createdBy,
            createdAt = state.createdAt,
            updatedAt = state.updatedAt,
            linearId = state.linearId.toString()
    )
}