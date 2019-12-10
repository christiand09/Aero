package com.aerotrax.flows.Product

import co.paralleluniverse.fibers.Suspendable
import com.aerotrax.contracts.ProductContract
import com.aerotrax.functions.FlowFunctions
import com.aerotrax.states.CMMState
import com.aerotrax.states.CompanyState
import com.aerotrax.states.ProductState
import javassist.NotFoundException
import net.corda.core.contracts.Command
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.StartableByRPC
import net.corda.core.node.services.queryBy
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import java.time.Instant

@StartableByRPC
class RegisterCMMFlow (
        private val productId: String,
        private val serialNumber: String,
        private val CMMTitle: String,
        private val createdBy: String
): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction {
        val transaction = transaction()
        val signedTransaction = verifyAndSign(transaction)
        return recordTransactionWithoutCounterParty(signedTransaction)
    }


    private fun outputCMMState(): CMMState {

        return CMMState(
                serialNumber = serialNumber,
                productId = productId,
                title = CMMTitle,
                status = "completed",
                createdBy = createdBy,
                createdAt = Instant.now(),
                updatedAt = null,
                linearId = UniqueIdentifier(),
                participants = listOf(ourIdentity)
        )
    }

    private fun transaction(): TransactionBuilder {
        val builder = TransactionBuilder(notary())
        val productCmd = Command(ProductContract.Commands.Create(), ourIdentity.owningKey)
        builder.addOutputState(outputCMMState(), ProductContract.ID)
        builder.addCommand(productCmd)
        return builder
    }
}