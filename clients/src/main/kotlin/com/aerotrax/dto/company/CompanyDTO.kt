package com.aerotrax.dto.company

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.Instant

data class RegisterCompanyDTO @JsonCreator constructor(
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

data class MainCompanyDTO(
        val name: String,
        val email: String,
        val password: String,
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
        val participants: List<String>
)