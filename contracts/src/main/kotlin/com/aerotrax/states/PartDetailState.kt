package com.aerotrax.states;

import com.aerotrax.contracts.PartDetailContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import java.time.Instant

@BelongsToContract(PartDetailContract::class)
data class PartDetailState(val serialNumber: String,
                           val productId: String,
                           val title: String,
                           val value: String?,
                           val unit: String?,
                           val createdBy: String?,
                           val createdAt: Instant,
                           val updatedAt: Instant?,
                           override val linearId: UniqueIdentifier,
                           override val participants: List<AbstractParty>): LinearState