package com.aerotrax.states;

import com.aerotrax.contracts.ConnectionContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import java.time.Instant

@BelongsToContract(ConnectionContract::class)
data class ConnectionState(val companyId: String,
                           val requestCompanyId: String,
                           val node: String,
                           val status: String,
                           val createdBy: String,
                           val createdAt: Instant,
                           val updatedAt: Instant?,
                           val transactionId: String,
                           override val linearId: UniqueIdentifier,
                           override val participants: List<AbstractParty>): LinearState