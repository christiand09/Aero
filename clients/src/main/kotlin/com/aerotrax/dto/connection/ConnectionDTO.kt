package com.aerotrax.dto.connection

import com.aerotrax.dto.participant.MainParticipantDTO
import com.fasterxml.jackson.annotation.JsonCreator
import java.time.Instant

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

data class RequestConnectionDTO @JsonCreator constructor(
        val requestCompanyId: String,
        val requestMessage: String,
        val createdBy: String
)

data class ApproveRejectConnectionDTO @JsonCreator constructor(
        val approveReject: Boolean,
        val reason: String?,
        val createdBy: String
)