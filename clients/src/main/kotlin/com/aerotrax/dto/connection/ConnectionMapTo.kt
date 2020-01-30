package com.aerotrax.dto.connection

import com.aerotrax.dto.participant.MainParticipantDTO
import com.aerotrax.states.ConnectionState
import com.aerotrax.states.ParticipantState

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
            companyDetails = MainParticipantDTO(
                    participantState.name,
                    participantState.email,
                    participantState.type,
                    participantState.node,
                    participantState.logoImage,
                    participantState.contactNumber,
                    participantState.rate,
                    participantState.website,
                    participantState.linkedIn,
                    participantState.about,
                    participantState.location,
                    participantState.addressLine1,
                    participantState.addressLine2,
                    participantState.city,
                    participantState.state,
                    participantState.country,
                    participantState.zipCode,
                    participantState.review,
                    participantState.linearId.toString(),
                    participantState.participants.map { it.toString() }
            )
    )
}