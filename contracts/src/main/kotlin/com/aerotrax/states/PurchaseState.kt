package com.aerotrax.states;

import com.aerotrax.contracts.PurchaseContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import java.time.Instant

@BelongsToContract(PurchaseContract::class)
data class PurchaseState(val contractId: String,
                         val contractNumber: String,
                         val signer: String,
                         val statusSeller: String?,
                         val statusBuyer: String?,
                         val sellerAcceptAt: Instant?,
                         val sellerDenyAt: Instant?,
                         val sellerReason: String?,
                         val buyerAcceptAt: Instant?,
                         val buyerDenyAt: Instant?,
                         val buyerReason: String?,
                         val transactionId: String,
                         override val linearId: UniqueIdentifier,
                         override val participants: List<Party>): LinearState