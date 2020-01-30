package com.aerotrax.flows.product

import co.paralleluniverse.fibers.Suspendable
import com.aerotrax.contracts.BuyerContract
import com.aerotrax.contracts.BuyerDetailsContract
import com.aerotrax.functions.FlowFunctions
import com.aerotrax.states.BuyerState
import net.corda.core.contracts.Command
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.contracts.requireThat
import net.corda.core.flows.*
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import java.time.Instant

@StartableByRPC
@InitiatingFlow
class AddBuyerFlow (
        private val buyerDetailsId: String,
        private val companyId: String,
        private val productId: String,
        private val groupNumber: String,
        private val status: String?,
        private val createdBy: String
): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction {
        val signedTransaction = verifyAndSign(transaction())
        val sessions = (outputBuyerState().participants - ourIdentity).map { initiateFlow(it) }
        val transactionSignedByAllParties = collectSignature(signedTransaction, sessions)
        return recordTransactionWithCounterParty(transactionSignedByAllParties, sessions)
    }

    private fun outputBuyerState(): BuyerState {
        val productState = getProductStateById(productId).state.data
        val serialNumber = productState.serialNumber
        val companyState = getCompanyStateById(companyId).state.data
        return BuyerState(
                buyerDetailsId = buyerDetailsId,
                companyId = companyState.linearId.toString(),
                serialNumber = serialNumber,
                productId = productState.linearId.toString(),
                groupNumber = groupNumber,
                node = companyState.node,
                status = status,
                createdBy = createdBy,
                createdAt = Instant.now(),
                updatedAt = null,
                linearId = UniqueIdentifier(),
                participants = listOf(ourIdentity, stringToParty(companyState.node))
        )
    }

    private fun transaction(): TransactionBuilder {
        val output = outputBuyerState()
        val builder = TransactionBuilder(notary())
        val buyerCmd = Command(BuyerContract.Commands.Create(), output.participants.map { it.owningKey })
        builder.addOutputState(output, BuyerContract.ID)
        builder.addCommand(buyerCmd)
        return builder
    }
}

@InitiatedBy(AddBuyerFlow::class)
class AddBuyerFlowResponder(val flowSession: FlowSession) : FlowLogic<SignedTransaction>() {
    @Suspendable
    override fun call(): SignedTransaction {
        val signTransactionFlow = object : SignTransactionFlow(flowSession) {
            override fun checkTransaction(stx: SignedTransaction) = requireThat {
            }
        }
        val signedTransaction = subFlow(signTransactionFlow)
        return subFlow(ReceiveFinalityFlow(otherSideSession = flowSession, expectedTxId = signedTransaction.id))
    }
}