package com.aerotrax.states

import com.aerotrax.contracts.ProductContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import java.time.Instant

@BelongsToContract(ProductContract::class)
data class ProductState(val companyId: String,
                        val partName: String,
                        val partImage: String,
                        val partNumber: String,
                        val serialNumber: String,
                        val status: String, // new or existing
                        val category: String,
                        val manufacturer: String,
                        val registeredOwner: String,
                        val newOwner: String?,
                        val partAvailability: String, // available, registering
                        val views: Int?,
                        val rate: Int?,
                        val dealBit: Boolean?,
                        val createdBy: String,
                        val updatedBy: String?,
                        val createdAt: Instant,
                        val updatedAt: Instant?,
                        val transactionId: String,
                        override val linearId: UniqueIdentifier,
                        override val participants: List<AbstractParty>): LinearState