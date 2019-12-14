package com.aerotrax.flows.company

import co.paralleluniverse.fibers.Suspendable
import com.aerotrax.contracts.ParticipantContract
import com.aerotrax.functions.FlowFunctions
import com.aerotrax.states.ParticipantState
import net.corda.core.contracts.Command
import net.corda.core.contracts.requireThat
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.node.services.queryBy
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder

@StartableByRPC
@InitiatingFlow
class AddNewParticipantFlow (
        private val name: String,
        private val email: String,
        private val type: String,
        private val node: String,
        private val logoImage: String?,
        private val contactNumber: String?,
        private val rate: String?,
        private val website: String?,
        private val linkedIn: String?,
        private val about: String?,
        private val location: String?,
        private val addressLine1: String?,
        private val addressLine2: String?,
        private val city: String?,
        private val state: String?,
        private val country: String?,
        private val zipCode: String?,
        private val review: String?,
        private val linearId: String
): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction {
        val signedTransaction = verifyAndSign(transaction())
        val sessions = (outputParticipantState().participants - ourIdentity).map { initiateFlow(it) }
        val transactionSignedByAllParties = collectSignature(signedTransaction, sessions)
        return recordTransactionWithCounterParty(transactionSignedByAllParties, sessions)
    }

    private fun outputParticipantState(): ParticipantState {
        val participantStateIsEmpty = serviceHub.vaultService.queryBy<ParticipantState>().states.isEmpty()
        val participantState = serviceHub.vaultService.queryBy<ParticipantState>().states.map {
            it.state.data.node
        }
        println("####################################")
        println(participantState)
        println("####################################")
        val listOfParticipant = participantState.toMutableList()

        var participants: List<Party>
        participants = if (participantStateIsEmpty){
            listOf(stringToParty(node))
        }else{
            listOfParticipant.add(node)
            listOfParticipant.map { stringToParty(it) }
        }

        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
        println(participants)
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")

        return ParticipantState(
                name = name,
                email = email,
                type = type,
                node = node,
                logoImage = logoImage,
                contactNumber = contactNumber,
                rate = rate,
                website = website,
                linkedIn = linkedIn,
                about = about,
                location = location,
                addressLine1 = addressLine1,
                addressLine2 = addressLine2,
                city = city,
                state = state,
                country = country,
                zipCode = zipCode,
                review = review,
                linearId = stringToLinearID(linearId),
                participants = parties()
        )
    }

    private fun transaction(): TransactionBuilder {
        val output = outputParticipantState()
        val builder = TransactionBuilder(notary())
        val participantCmd = Command(ParticipantContract.Commands.Create(), output.participants.map { it.owningKey })
        builder.addOutputState(output, ParticipantContract.ID)
        builder.addCommand(participantCmd)
        return builder
    }
}

@InitiatedBy(AddNewParticipantFlow::class)
class AddNewParticipantFlowResponder(val flowSession: FlowSession) : FlowLogic<SignedTransaction>() {
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