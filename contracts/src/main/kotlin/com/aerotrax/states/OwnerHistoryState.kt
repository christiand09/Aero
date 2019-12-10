package com.ipn.platforms.dezrez.states

import com.aerotrax.contracts.OwnerHistoryContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import java.time.Instant

@BelongsToContract(OwnerHistoryContract::class)
data class OwnerHistoryState(val serialNumber: String,
                             val productId: String,
                             val previousOwner: String?,
                             val newOwner: String,
                             val createdBy: String,
                             val createdAt: Instant,
                             val updatedAt: Instant?,
                             val transactionId: String,
                             override val linearId: UniqueIdentifier,
                             override val participants: List<AbstractParty>): LinearState