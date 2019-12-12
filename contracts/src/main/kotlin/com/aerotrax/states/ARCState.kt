package com.aerotrax.states;

import com.aerotrax.contracts.ARCContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import java.time.Instant

@BelongsToContract(ARCContract::class)
data class ARCState(val companyId: String,
                    val serialNumber: String,
                    val productId: String,
                    val contractNumber: String,
                    val trackNumber: String?,
                    val remarks: String?,
                    val approved: Instant?,
                    val disapproved: Instant?,
                    val createdBy: String,
                    val createdAt: Instant,
                    val updatedAt: Instant?,
                    override val linearId: UniqueIdentifier,
                    override val participants: List<Party>): LinearState