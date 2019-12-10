package com.ipn.platforms.dezrez.states

import com.aerotrax.contracts.BuyerDetailsContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import java.time.Instant

@BelongsToContract(BuyerDetailsContract::class)
data class BuyerDetailsState(val buyerId: String,
                             val serialNumber: String,
                             val productId: String,
                             val indicatePrice: String,
                             val currencyPrice: String,
                             val method: List<String>,
                             val status: String?,
                             val comment: String?,
                             val createdBy: String,
                             val createdAt: Instant,
                             val updatedAt: Instant?,
                             val transactionId: String,
                             override val linearId: UniqueIdentifier,
                             override val participants: List<AbstractParty>): LinearState