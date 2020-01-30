package com.aerotrax.flows.company

import co.paralleluniverse.fibers.Suspendable
import com.aerotrax.contracts.CompanyContract
import com.aerotrax.contracts.ConnectionContract
import com.aerotrax.functions.FlowFunctions
import com.aerotrax.states.CompanyState
import com.aerotrax.states.ConnectionState
import javassist.NotFoundException
import net.corda.core.CordaException
import net.corda.core.contracts.Command
import net.corda.core.contracts.StateAndRef
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.contracts.requireThat
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.node.services.queryBy
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import java.time.Instant

@StartableByRPC
@InitiatingFlow
class ApproveRejectConnectionFlow (private val connectionId: String,
                                   private val approveReject: Boolean,
                                   private val reasonReject: String?,
                                   private val createdBy: String): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction {
        val signedTransaction = verifyAndSign(transaction())
        val sessions = (outputConnectionState().participants - ourIdentity).map { initiateFlow(it) }
        val transactionSignedByAllParties = collectSignature(signedTransaction, sessions)
        return recordTransactionWithCounterParty(transactionSignedByAllParties, sessions)
    }

    private fun inputConnectionState(): StateAndRef<ConnectionState>{
        return getConnectionStateById(connectionId)
    }

    private fun outputConnectionState(): ConnectionState {
        val inputState = inputConnectionState().state.data
        val getCompanyName = inputState.requestCompanyId
        val getOtherCompanyName = inputState.companyId
        val acceptedAtEntry = inputState.acceptedAt
        val declinedAtEntry = inputState.declinedAt

        if (acceptedAtEntry != null || declinedAtEntry != null){
            if (acceptedAtEntry != null){ throw CordaException("This connection has been approved.") }
            if (declinedAtEntry != null){ throw CordaException("This connection has been declined.") }
        }

        println(getCompanyName)
        val companyName = getParticipantStateById(getCompanyName).state.data.name
        val otherCompanyName = getParticipantStateById(getOtherCompanyName).state.data.name
        println(companyName)
        var acceptedAt: Instant?
        var declinedAt: Instant?
        var reason: String?
        var status: String?
        if (approveReject){
            acceptedAt = Instant.now()
            declinedAt = null
            reason = null
            status = "$companyName approved connection with $otherCompanyName"
        }else{
            acceptedAt = null
            declinedAt = Instant.now()
            reason = reasonReject
            status = "$companyName rejected connection with $otherCompanyName"
        }
        return ConnectionState(
                companyId = inputState.companyId,
                requestCompanyId = inputState.requestCompanyId,
                requestNode = inputState.requestNode,
                requestMessage = inputState.requestMessage,
                acceptedAt = acceptedAt,
                declinedAt = declinedAt,
                reason = reason,
                status = status,
                createdBy = createdBy,
                createdAt = inputState.createdAt,
                updatedAt = Instant.now(),
                linearId = inputState.linearId,
                participants = inputState.participants
        )
    }
    private fun transaction(): TransactionBuilder {
        val output = outputConnectionState()
        val builder = TransactionBuilder(notary())
        val productCmd = Command(ConnectionContract.Commands.Update(), output.participants.map { it.owningKey })
        builder.addInputState(inputConnectionState())
        builder.addOutputState(output, ConnectionContract.ID)
        builder.addCommand(productCmd)
        return builder
    }
}

@InitiatedBy(ApproveRejectConnectionFlow::class)
class ApproveRejectConnectionFlowResponder(val flowSession: FlowSession) : FlowLogic<SignedTransaction>() {
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