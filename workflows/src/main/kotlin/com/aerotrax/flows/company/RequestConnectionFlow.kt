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
class RequestConnectionFlow (
        private val ownCompanyId: String,
        private val requestCompanyId: String,
        private val requestMessage: String,
        private val createdBy: String
): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction {
        val signedTransaction = verifyAndSign(transaction())
        val sessions = (outputConnectionState().participants - ourIdentity).map { initiateFlow(it) }
        val transactionSignedByAllParties = collectSignature(signedTransaction, sessions)
        return recordTransactionWithCounterParty(transactionSignedByAllParties, sessions)
    }

    private fun outputConnectionState(): ConnectionState {

        val getRequestNode = getParticipantStateById(requestCompanyId).state.data.node
        val requestNode = stringToParty(getRequestNode)

        return ConnectionState(
                companyId = ownCompanyId, // company that is inviting
                requestCompanyId = requestCompanyId, // company that is being invited
                requestNode = getRequestNode, // company that is being invited node name
                requestMessage = requestMessage,
                acceptedAt = null,
                declinedAt = null,
                reason = null,
                status = "Request connection sent.",
                createdBy = createdBy,
                createdAt = Instant.now(),
                updatedAt = null,
                linearId = UniqueIdentifier(),
                participants = listOf(ourIdentity, requestNode)
        )
    }

    private fun transaction(): TransactionBuilder {
        val output = outputConnectionState()
        val builder = TransactionBuilder(notary())
        val connectionCmd = Command(ConnectionContract.Commands.Create(), output.participants.map { it.owningKey })
        builder.addOutputState(output, ConnectionContract.ID)
        builder.addCommand(connectionCmd)
        return builder
    }
}

@InitiatedBy(RequestConnectionFlow::class)
class RequestConnectionFlowResponder(val flowSession: FlowSession) : FlowLogic<SignedTransaction>() {
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