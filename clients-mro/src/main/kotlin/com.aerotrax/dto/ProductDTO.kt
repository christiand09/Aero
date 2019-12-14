package com.aerotrax.dto

import com.aerotrax.states.ProductState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import java.time.Instant

data class RegisterProductDTO(
         val companyId: String,
         val partName: String,
         val partImage: String,
         val partNumber: String,
         val serialNumber: String,
         val status: String,
         val category: String,
         val manufacturer: String,
         val CMMTitle: String,
//        private val CMMStatus: String?,
         val createdBy: String,
         val transactionId: String,
         val linearId: String
)

data class MainProductDTO(val companyId: String,
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
                          val linearId: UniqueIdentifier,
                          val participants: List<AbstractParty>)

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

         linearId = state.linearId,
         participants =  state.participants
    )
}