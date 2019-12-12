package com.aerotrax.flows.product

import co.paralleluniverse.fibers.Suspendable
import com.aerotrax.contracts.PartDetailContract
import com.aerotrax.functions.FlowFunctions
import com.aerotrax.states.PartDetailState
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
class RegisterPartDetailFlow (
      private val productId: String,
      private val createdBy: String,
      private val title: String,
      private val value: String,
      private val unit: String
): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction {
        val transaction = transaction()
        val signedTransaction = verifyAndSign(transaction)
        return recordTransactionWithoutCounterParty(signedTransaction)
    }

    private fun outputPartDetail(): PartDetailState {

        val productState = serviceHub.vaultService.queryBy<ProductState>().states.find {
            it.state.data.linearId == stringToLinearID(productId)
        }?: throw NotFoundException("Product Id not found")

        val serialNumber = productState.state.data.serialNumber

        return PartDetailState(
                serialNumber = serialNumber,
                productId = productId,
                title = title,
                value = value,
                unit = unit,
                createdBy = createdBy,
                createdAt = Instant.now(),
                updatedAt = null,
                linearId = UniqueIdentifier(),
                participants = listOf(ourIdentity)
        )
    }



    private fun transaction(): TransactionBuilder {
        val builder = TransactionBuilder(notary())
        val productCmd = Command(PartDetailContract.Commands.Create(), ourIdentity.owningKey)
        builder.addOutputState(outputPartDetail(), PartDetailContract.ID)
        builder.addCommand(productCmd)
        return builder
    }
}