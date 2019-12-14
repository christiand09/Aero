package com.aerotrax.dto

import com.aerotrax.states.CompanyState
import com.aerotrax.states.ConnectionState
import com.aerotrax.states.ParticipantState
import net.corda.core.identity.Party
import java.time.Instant

data class RegisterCompanyDTO(
         val name: String,
         val email: String,
         val password: String,
         val type: String,
         val logoImage: String?,
         val contactNumber: String?,
         val rate: String?,
         val website: String?,
         val linkedIn: String?,
         val about: String?,
         val location: String?,
         val addressLine1: String?,
         val addressLine2: String?,
         val city: String?,
         val state: String?,
         val country: String?,
         val zipCode: String?,
         val review: String?,
         val createdBy: String
)

data class RequestConnectionDTO(
        val requestCompanyId: String,
        val requestMessage: String,
        val createdBy: String
)

data class ApproveRejectConnectionDTO(

        val approveReject: Boolean,
        val reason: String?,
        val createdBy: String
)

data class MainConnectionDTO(
        val companyId: String,
        val requestCompanyId: String,
        val requestNode: String,
        val requestMessage: String?,
        val acceptedAt: Instant?,
        val declinedAt: Instant?,
        val reason: String?,
        val status: String,
        val createdBy: String,
        val createdAt: Instant,
        val updatedAt: Instant?,
        val linearId: String,
        val participants: List<String>
)

data class MainConnectionWithCompanyDTO(
        val companyId: String,
        val requestCompanyId: String,
        val requestNode: String,
        val acceptedAt: Instant?,
        val declinedAt: Instant?,
        val reason: String?,
        val status: String,
        val createdBy: String,
        val createdAt: Instant,
        val updatedAt: Instant?,
        val linearId: String,
        val participants: List<String>,
        val companyDetails: MainParticipantDTO
)

fun mapToMainConnectionWithCompanyDTO(connectionState: ConnectionState, participantState: ParticipantState): MainConnectionWithCompanyDTO {
    return MainConnectionWithCompanyDTO(
            companyId = connectionState.companyId,
            requestCompanyId = connectionState.requestCompanyId,
            requestNode = connectionState.requestNode,
            acceptedAt = connectionState.acceptedAt,
            declinedAt = connectionState.declinedAt,
            reason = connectionState.reason,
            status = connectionState.status,
            createdBy = connectionState.createdBy,
            createdAt = connectionState.createdAt,
            updatedAt = connectionState.updatedAt,
            linearId = connectionState.linearId.toString(),
            participants = connectionState.participants.map { it.toString() },
            companyDetails = MainParticipantDTO(participantState.name, participantState.email,participantState.type,participantState.node,participantState.logoImage,participantState.contactNumber,participantState.rate,participantState.website,participantState.linkedIn,participantState.about,participantState.location,participantState.addressLine1,participantState.addressLine2,participantState.city,participantState.state,participantState.country,participantState.zipCode,participantState.review,participantState.linearId.toString(),participantState.participants.map { it.toString() })
)
}

fun mapToMainConnectionDTO(state: ConnectionState): MainConnectionDTO
{
    return MainConnectionDTO(
            companyId = state.companyId,
            requestCompanyId = state.requestCompanyId,
            requestNode = state.requestNode,
            requestMessage = state.requestMessage,
            acceptedAt = state.acceptedAt,
            declinedAt = state.declinedAt,
            reason = state.reason,
            status = state.status,
            createdBy = state.createdBy,
            createdAt = state.createdAt,
            updatedAt = state.updatedAt,
            linearId = state.linearId.toString(),
            participants = state.participants.map { it.toString() }
        )
}
data class MainCompanyDTO(
        val name: String,
        val email: String,
        val password: String,
//      val passwordHash: String,
//       val passwordSalt: String,
        val type: String,
        val node: String,
        val logoImage: String?,
        val contactNumber: String?,
        val rate: String?,
        val website: String?,
        val linkedIn: String?,
        val about: String?,
        val location: String?,
        val addressLine1: String?,
        val addressLine2: String?,
        val city: String?,
        val state: String?,
        val country: String?,
        val zipCode: String?,
        val review: String?,
        val createdBy: String,
        val createdAt: Instant,
        val updatedAt: Instant?,
        val linearId: String,
        val participants: List<String>)

fun mapToMainCompanyDTO(state: CompanyState) : MainCompanyDTO {
    return MainCompanyDTO(
            name = state.name,
            email =  state.email,
            password= state.password,
            type= state.type,
            node= state.node.toString(),
            logoImage= state.logoImage,
            contactNumber= state.contactNumber,
            rate= state.rate,
            website= state.website,
            linkedIn= state.linkedIn,
            about= state.about,
            location= state.location,
            addressLine1= state.addressLine1,
            addressLine2= state.addressLine2,
            city= state.city,
            state= state.state,
            country= state.country,
            zipCode= state.zipCode,
            review =  state.review,
            createdBy = state.createdBy,
            createdAt =  state.createdAt,
            updatedAt = state.updatedAt,
            linearId = state.linearId.toString(),
            participants = state.participants.map { it.toString() }
    )
}

data class MainParticipantDTO(
        val name: String,
        val email: String,
        val type: String,
        val node: String,
        val logoImage: String?,
        val contactNumber: String?,
        val rate: String?,
        val website: String?,
        val linkedIn: String?,
        val about: String?,
        val location: String?,
        val addressLine1: String?,
        val addressLine2: String?,
        val city: String?,
        val state: String?,
        val country: String?,
        val zipCode: String?,
        val review: String?,
        val linearId: String,
        val participants: List<String>)

fun mapToMainParticipantDTO(state: ParticipantState) : MainParticipantDTO {
    return MainParticipantDTO(
            name = state.name,
            email =  state.email,
            type= state.type,
            node= state.node,
            logoImage= state.logoImage,
            contactNumber= state.contactNumber,
            rate= state.rate,
            website= state.website,
            linkedIn= state.linkedIn,
            about= state.about,
            location= state.location,
            addressLine1= state.addressLine1,
            addressLine2= state.addressLine2,
            city= state.city,
            state= state.state,
            country= state.country,
            zipCode= state.zipCode,
            review =  state.review,
            linearId = state.linearId.toString(),
            participants = state.participants.map { it.toString() }
    )
}