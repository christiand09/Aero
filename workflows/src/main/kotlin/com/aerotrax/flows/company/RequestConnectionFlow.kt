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
class RequestConnectionFlow (private val ownCompanyId: String,
                             private val requestCompanyId: String,
                             private val createdBy: String): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction
    {
        val transaction = transaction()
        val sessions = (outputConnectionState().participants - ourIdentity).map { initiateFlow(it) }
        val signedTransaction = verifyAndSign(transaction)
        val transactionSignedByAllParties = collectSignature(signedTransaction, sessions)
        return recordTransactionWithCounterParty(transactionSignedByAllParties, sessions)
    }

    private fun outputConnectionState(): ConnectionState {
        val requestCompany = getCompanyStateByString(requestCompanyId)
        val requestCompanyNode = requestCompany.state.data.node

        println("******requestCompanyNode******")
        println(requestCompanyNode.toString())
        println("******requestCompanyNode******")

        val ownCompany = getCompanyStateByString(ownCompanyId)
        val ownCompanyNode = ownCompany.state.data.node

        val self = ourIdentity.toString()

        println("******ourIdentity******")
        println(self)
        println("******ourIdentity******")

        return ConnectionState(
                companyId = ownCompanyId, // company that is inviting
                requestCompanyId = requestCompanyId, // company that is being invited
                acceptedAt = null,
                declinedAt = null,
                reason = null,
                status = "Request connection sent.",
                createdBy = createdBy,
                createdAt = Instant.now(),
                updatedAt = null,
                transactionId = null,
                linearId = UniqueIdentifier(),
                participants = listOf(ourIdentity, requestCompanyNode)
        )

        }

//    private fun outputRequestCompanyState(): CompanyState {
//
//        val myCompany = getCompanyStateByString(requestCompanyId).state.data
//        var request = myCompany.requests
//        println(request)
//        request?.add(requestCompanyId)
//        println(request)
//        return myCompany.copy(
//                requests = request
//        )
//    }
    
    private fun transaction(): TransactionBuilder {
        val builder = TransactionBuilder(notary())
        println(outputConnectionState().participants)
        val connectionCmd = Command(ConnectionContract.Commands.Create(), outputConnectionState().participants.map { it.owningKey })
//        val companyCmd = Command(CompanyContract.Commands.Create(), outputRequestCompanyState().participants.map { it.owningKey })

        builder.addInputState(getCompanyStateByString(requestCompanyId))

        builder.addOutputState(outputConnectionState(), ConnectionContract.ID)
//        builder.addOutputState(outputRequestCompanyState(), CompanyContract.ID)

        builder.addCommand(connectionCmd)
//        builder.addCommand(companyCmd)
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