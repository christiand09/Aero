package com.aerotrax.dto

import com.aerotrax.states.CompanyState
import com.aerotrax.states.ConnectionState
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
        val acceptedAt: Instant?,
        val declinedAt: Instant?,
        val reason: String?,
        val status: String,
        val createdBy: String,
        val createdAt: Instant,
        val updatedAt: Instant?,
        val linearId: String,
        val participants: List<Party>
)

fun mapToMainConnectionDTO(state: ConnectionState): MainConnectionDTO
{
    return MainConnectionDTO(
            companyId = state.companyId,
            requestCompanyId = state.requestCompanyId,
            requestNode = state.requestNode,
            acceptedAt = state.acceptedAt,
            declinedAt = state.declinedAt,
            reason = state.reason,
            status = state.status,
            createdBy = state.createdBy,
            createdAt = state.createdAt,
            updatedAt = state.updatedAt,
            linearId = state.linearId.toString(),
            participants = state.participants
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
            participants = state.participants?.map { it.toString() }
    )
}