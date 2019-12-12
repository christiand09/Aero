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
class ApproveRejectConnectionFlow (private val connectionId: UniqueIdentifier,
                                   private val approveReject: Boolean,
                                   private val reason: String?,
                                   private val createdBy: String): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction
    {
        val transaction = transaction()
        val sessions = (outputConnectionState().participants - ourIdentity).map { initiateFlow(it as Party) }
        val signedTransaction = verifyAndSign(transaction)
        val transactionSignedByAllParties = collectSignature(signedTransaction, sessions)
        return recordTransactionWithCounterParty(transactionSignedByAllParties, sessions)
    }

    private fun outputConnectionState(): ConnectionState {
        val connection = getConnectionStateByUId(connectionId)
        val connectionCompanyId = connection.state.data.companyId
        val connectionRequestId = connection.state.data.requestCompanyId

        val requestingCompanyNode = getCompanyStateByString(connectionCompanyId).state.data.node

            if (approveReject) {
                if (connection.state.data.acceptedAt == null || connection.state.data.declinedAt == null) {
                    throw CordaException ("Connection has been declined/approved before")
                }

                return ConnectionState(
                        companyId = connectionCompanyId,
                        requestCompanyId = connectionRequestId,
                        acceptedAt = Instant.now(),
                        declinedAt = null,
                        reason = null,
                        status = "Request connection sent.",
                        createdBy = createdBy,
                        createdAt = Instant.now(),
                        updatedAt = Instant.now(),
                        transactionId = null,
                        linearId = connectionId,
                        participants = listOf(ourIdentity, requestingCompanyNode)
                )

            } else {
                if (connection.state.data.acceptedAt == null || connection.state.data.declinedAt == null) {
                    throw CordaException ("Connection has been declined/approved before")
                }

                return ConnectionState(
                        companyId = connectionCompanyId,
                        requestCompanyId = connectionRequestId,
                        acceptedAt = null,
                        declinedAt = Instant.now(),
                        reason = reason,
                        status = "Request connection sent.",
                        createdBy = createdBy,
                        createdAt = Instant.now(),
                        updatedAt = Instant.now(),
                        transactionId = null,
                        linearId = connectionId,
                        participants = listOf(ourIdentity, requestingCompanyNode)
                )
            }


        }



    private fun transaction(): TransactionBuilder {
        val builder = TransactionBuilder(notary())
        val productCmd = Command(ConnectionContract.Commands.Create(), outputConnectionState().participants.map { it.owningKey })
        builder.addInputState(getConnectionStateByUId(connectionId))
        builder.addOutputState(outputConnectionState(), ConnectionContract.ID)
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