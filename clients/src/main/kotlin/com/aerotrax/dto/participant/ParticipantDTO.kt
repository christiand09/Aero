package com.aerotrax.dto.participant

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
        val participants: List<String>
)