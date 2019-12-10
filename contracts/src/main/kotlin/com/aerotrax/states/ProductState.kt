package com.ipn.platforms.dezrez.states

import com.aerotrax.contracts.ARCContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import java.time.Instant

@BelongsToContract(ARCContract::class)
data class ProductState(val companyId: String,
                        val name: String,
                        val partNumber: String,
                        val serialNumber: String,
                        val manufacturer: String,
                        val registeredOwner: String,
                        val newOwner: String?,
                        val partType: String,
                        val status: String,
                        val saleStatus: String?,
                        val type: String,
                        val category: String,
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