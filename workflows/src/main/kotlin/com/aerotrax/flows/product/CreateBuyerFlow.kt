package com.aerotrax.flows.product

import co.paralleluniverse.fibers.Suspendable
import com.aerotrax.contracts.BuyerDetailsContract
import com.aerotrax.functions.FlowFunctions
import com.aerotrax.states.BuyerDetailsState
import net.corda.core.contracts.Command
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.contracts.requireThat
import net.corda.core.flows.*
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import java.time.Instant

@StartableByRPC
@InitiatingFlow
class CreateBuyerFlow (
        private val productId: String,
        private val indicatePrice: String,
        private val currencyPrice: String,
        private val method: List<String>,
        private val status: String?,
        private val comment: String?,
        private val createdBy: String,
        private val parties: List<String>
): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction {
        val signedTransaction = verifyAndSign(transaction())
        val sessions = (outputBuyerDetailsState().participants - ourIdentity).map { initiateFlow(it) }
        val transactionSignedByAllParties = collectSignature(signedTransaction, sessions)
        return recordTransactionWithCounterParty(transactionSignedByAllParties, sessions)
    }

    private fun outputBuyerDetailsState(): BuyerDetailsState {
        val productState = getProductStateById(productId).state.data
        val serialNumber = productState.serialNumber
        return BuyerDetailsState(
                serialNumber = serialNumber,
                productId = productState.linearId.toString(),
                indicatePrice = indicatePrice,
                currencyPrice = currencyPrice,
                method = method,
                status = status,
                comment = comment,
                createdBy = createdBy,
                createdAt = Instant.now(),
                updatedAt = null,
                linearId = UniqueIdentifier(),
                participants = parties.map { stringToParty(it) }
        )
    }

    private fun transaction(): TransactionBuilder {
        val output = outputBuyerDetailsState()
        val builder = TransactionBuilder(notary())
        val participantCmd = Command(BuyerDetailsContract.Commands.Create(), output.participants.map { it.owningKey })
        builder.addOutputState(output, BuyerDetailsContract.ID)
        builder.addCommand(participantCmd)
        return builder
    }
}

@InitiatedBy(CreateBuyerFlow::class)
class CreateBuyerFlowResponder(val flowSession: FlowSession) : FlowLogic<SignedTransaction>() {
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