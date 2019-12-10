package com.aerotrax.states;

import com.aerotrax.contracts.UserContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import java.time.Instant

@BelongsToContract(UserContract::class)
data class UserState(val companyId: String,
                     val name: String,
                     val type: String,
                     val createdBy: String,
                     val createdAt: Instant,
                     val updatedAt: Instant?,
                     val transactionId: String,
                     override val linearId: UniqueIdentifier,
                     override val participants: List<AbstractParty>): LinearState