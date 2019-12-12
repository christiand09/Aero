package com.aerotrax.states;

import com.aerotrax.contracts.BuyerContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import java.time.Instant

@BelongsToContract(BuyerContract::class)
data class BuyerState(val buyerDetailsId: String,
                      val companyId: String,
                      val serialNumber: String,
                      val productId: String,
                      val groupNumber: String,
                      val node: String,
                      val status: String?,
                      val createdBy: String,
                      val createdAt: Instant,
                      val updatedAt: Instant?,
                      override val linearId: UniqueIdentifier,
                      override val participants: List<Party>): LinearState