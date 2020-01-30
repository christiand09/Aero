package com.aerotrax.states;

import com.aerotrax.contracts.OwnerHistoryContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import java.time.Instant

@BelongsToContract(OwnerHistoryContract::class)
data class OwnerState(val name: String,
                      val ownershipType: String,
                      val timeOfOwnershipStart: Instant,
                      val timeOfOwnershipEnd: Instant,
                      val recordOfTransferTitle: String,
                      val recordOfTransferDate: Instant,
                      val document: String,
                      val createdBy: String,
                      val createdAt: Instant,
                      val updatedAt: Instant?,
                      val productID: String,
                      override val linearId: UniqueIdentifier,
                      override val participants: List<Party>): LinearState