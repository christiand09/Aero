package com.aerotrax.dto.arc

import com.aerotrax.states.ARCState

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
            linearId = state.linearId.id.toString()
    )
}