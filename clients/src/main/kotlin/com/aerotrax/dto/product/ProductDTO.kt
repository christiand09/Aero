package com.aerotrax.dto.product

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.Instant

data class RegisterProductFlowDTO @JsonCreator constructor(
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