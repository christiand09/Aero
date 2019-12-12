package com.aerotrax.states

import com.aerotrax.contracts.ParticipantContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.Party
import java.time.Instant

@BelongsToContract(ParticipantContract::class)
data class ParticipantState(val name: String,
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
                            val createdBy: String,
                            val createdAt: Instant,
                            val updatedAt: Instant?,
                            override val linearId: UniqueIdentifier,
                            override val participants: List<Party>): LinearState