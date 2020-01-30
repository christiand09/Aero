package com.aerotrax.flows.product

import co.paralleluniverse.fibers.Suspendable
import com.aerotrax.contracts.ARCContract
import com.aerotrax.functions.FlowFunctions
import com.aerotrax.states.ARCState
import net.corda.core.contracts.Command
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import java.time.Instant

@StartableByRPC
class GenerateARCFlow (
        private val companyId: String,
        private val productId: String
): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction {
        val transaction = transaction()
        val signedTransaction = verifyAndSign(transaction)
        return recordTransactionWithoutCounterParty(signedTransaction)
    }

    private fun outputARCState(): ARCState {
        val productState = getProductStateById(productId).state.data
        return ARCState(
                companyId = companyId,
                serialNumber = productState.serialNumber,
                productId = productState.linearId.toString(),
                status = "registering",
                contractNumber = UniqueIdentifier().toString(),
                trackNumber = UniqueIdentifier().toString(),
                remarks = null,
                approved = null,
                disapproved = null,
                createdBy = null,
                createdAt = null,
                updatedAt = null,
                linearId = UniqueIdentifier(),
                participants = listOf(ourIdentity)
        )
    }

    private fun transaction(): TransactionBuilder {
        val builder = TransactionBuilder(notary())
        val arcCmd = Command(ARCContract.Commands.Create(), ourIdentity.owningKey)
        builder.addOutputState(outputARCState(), ARCContract.ID)
        builder.addCommand(arcCmd)
        return builder
    }
}