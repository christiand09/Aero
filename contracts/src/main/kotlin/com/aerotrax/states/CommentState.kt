package com.aerotrax.states;

import com.aerotrax.contracts.CommentContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import java.time.Instant

@BelongsToContract(CommentContract::class)
data class CommentState(val companyId: String,
                        val serialNumber: String,
                        val productId: String,
                        val message: String,
                        val review: Int,
                        val commentedBy: String,
                        val createdAt: Instant,
                        val updatedAt: Instant?,
                        val transactionId: String,
                        override val linearId: UniqueIdentifier,
                        override val participants: List<Party>): LinearState