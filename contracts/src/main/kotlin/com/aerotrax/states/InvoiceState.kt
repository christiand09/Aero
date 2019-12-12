package com.aerotrax.states;

import com.aerotrax.contracts.InvoiceContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import java.time.Instant

@BelongsToContract(InvoiceContract::class)
data class InvoiceState(val contractId: String,
                        val contractNumber: String,
                        val signer: String,
                        val statusSeller: String?,
                        val statusBuyer: String?,
                        val totalPrice: String,
                        val dateDue: String?,
                        val sellerAcceptAt: Instant?,
                        val sellerDenyAt: Instant?,
                        val sellerReason: String?,
                        val buyerAcceptAt: Instant?,
                        val buyerDenyAt: Instant?,
                        val buyerReason: String?,
                        val createdBy: String,
                        val createdAt: Instant,
                        val updatedAt: Instant?,
                        val transactionId: String,
                        override val linearId: UniqueIdentifier,
                        override val participants: List<Party>): LinearState