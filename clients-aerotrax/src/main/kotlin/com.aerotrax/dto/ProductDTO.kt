package com.aerotrax.dto

import com.aerotrax.states.ProductState
import com.fasterxml.jackson.annotation.JsonCreator
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.Party
import java.time.Instant

data class RegisterProductFlowDTO(
        val companyId: String,
        val partName: String,
        val partImage: String?,
        val partNumber: String,
        val serialNumber: String,
        val status: String,
        val category: String,
        val manufacturer: String,
        val createdBy: String,
        val partDetails: List<PartDetail>,
        val CMMTitle: List<String>
)

data class PartDetail (
        val title: String,
        val value: String?,
        val unit: String
)

data class MainProductDTO(
        val companyId: String,
        val partName: String,
        val partImage: String?,
        val partNumber: String,
        val serialNumber: String,
        val status: String,
        val category: String,
        val manufacturer: String,
        val registeredOwner: String,
        val newOwner: String?,
        val partAvailability: String,
        val views: Int?,
        val rate: Int?,
        val dealBit: Boolean?,
        val createdBy: String,
        val updatedBy: String?,
        val createdAt: Instant,
        val updatedAt: Instant?,
        val linearId: String,
        val participants: List<String>
)

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
         views= state.views,
         rate= state.rate,
         dealBit= state.dealBit,
         createdBy= state.createdBy,
         updatedBy= state.updatedBy,
         createdAt= state.createdAt,
         updatedAt= state.updatedAt,
         linearId = state.linearId.toString(),
         participants =  state.participants.map { it.toString() }
    )
}