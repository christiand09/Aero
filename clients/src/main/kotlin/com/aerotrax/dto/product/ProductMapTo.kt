package com.aerotrax.dto.product

import com.aerotrax.states.ProductState

fun mapToMainProductDTO(state: ProductState) : MainProductDTO {
    return MainProductDTO(
            companyId = state.companyId,
            partName =  state.partName,
            partImage= state.partImage,
            partNumber= state.partNumber,
            serialNumber= state.serialNumber,
            status= state.status,
            category= state.category,
            manufacturer= state.manufacturer,
            registeredOwner= state.registeredOwner,
            newOwner= state.newOwner,
            partAvailability= state.partAvailability,
            views= state.views.toString(),
            rate= state.rate.toString(),
            dealBit= state.dealBit.toString(),
            createdBy= state.createdBy,
            updatedBy= state.updatedBy,
            createdAt= state.createdAt,
            updatedAt= state.updatedAt,
            linearId = state.linearId.toString(),
            participants =  state.participants.map { it.toString() }
    )
}