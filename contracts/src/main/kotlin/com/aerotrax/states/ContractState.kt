package com.aerotrax.states;

import com.aerotrax.contracts.ContractStateContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import java.time.Instant

@BelongsToContract(ContractStateContract::class)
data class ContractState(val buyerDetailsId: String,
                         val contactNumber: String,
                         val statusBuyer: String,
                         val statusSeller: String,
                         val buyerCompanyId: String,
                         val sellerCompanyId: String,
                         val createdBy: String,
                         val createdAt: Instant,
                         val updatedAt: Instant?,
                         val transactionId: String,
                         override val linearId: UniqueIdentifier,
                         override val participants: List<Party>): LinearState