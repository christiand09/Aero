package com.aerotrax.states;

import com.aerotrax.contracts.DocumentContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import java.time.Instant

@BelongsToContract(DocumentContract::class)
data class DocumentState(val companyId: String,
                         val serialNumber: String?,
                         val productId: String?,
                         val name: String,
                         val size: String,
                         val url: String,
                         val hash: String,
                         val type: String,
                         val uploadedBy: String,
                         val uploadedAt: Instant,
                         val updatedAt: Instant?,
                         override val linearId: UniqueIdentifier,
                         override val participants: List<Party>): LinearState