package com.aerotrax.flows.document

import co.paralleluniverse.fibers.Suspendable
import com.aerotrax.contracts.DocumentContract
import com.aerotrax.functions.FlowFunctions
import com.aerotrax.states.DocumentState
import net.corda.core.contracts.Command
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import java.time.Instant

@StartableByRPC
class AddNewDocumentFlow (
        private val companyId: String,
        private val serialNumber: String?,
        private val productId: String?,
        private val name: String,
        private val size: String,
        private val url: String,
        private val hash: String,
        private val type: String,
        private val uploadedBy: String
): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction {
        val signedTransaction = verifyAndSign(transaction())
        return recordTransactionWithoutCounterParty(signedTransaction)
    }

    private fun outputDocumentState(): DocumentState {
        return DocumentState(
                companyId = companyId,
                serialNumber = serialNumber,
                productId = productId,
                name = name,
                size = size,
                url = url,
                hash = hash,
                type = type,
                uploadedBy = uploadedBy,
                uploadedAt = Instant.now(),
                updatedAt = null,
                linearId = UniqueIdentifier(),
                participants = listOf(ourIdentity)
        )
    }

    private fun transaction(): TransactionBuilder {
        val builder = TransactionBuilder(notary())
        val companyCmd = Command(DocumentContract.Commands.Create(), ourIdentity.owningKey)
        builder.addOutputState(outputDocumentState(), DocumentContract.ID)
        builder.addCommand(companyCmd)
        return builder
    }
}