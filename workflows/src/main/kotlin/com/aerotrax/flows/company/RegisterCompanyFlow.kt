package com.aerotrax.flows.company

import co.paralleluniverse.fibers.Suspendable
import com.aerotrax.contracts.CompanyContract
import com.aerotrax.functions.FlowFunctions
import com.aerotrax.states.CompanyState
import net.corda.core.CordaException
import net.corda.core.contracts.Command
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.contracts.requireThat
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import java.time.Instant

@StartableByRPC
@InitiatingFlow
class RegisterCompanyFlow (private val name: String,
                           private val email: String,
                           private val password: String,
                           private val type: String,
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
                           private val createdBy: String): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction
    {
        val transaction = transaction()

//        val self = ourIdentity.toString()
//        println(self)
//
//        if (self == "O=Aerotrax,L=London,C=GB"){
//            val spy1 = serviceHub.identityService.partiesFromName("O=MRO,L=New York,C=US", false).first()
//            val spy2 = serviceHub.identityService.partiesFromName("O=OEM,L=New York,C=US", false).first()
//            val spy3 = serviceHub.identityService.partiesFromName("O=Airline1,L=New York,C=US", false).first()
//
//        }


        val sessions = (outputCompanyState().participants - ourIdentity).map { initiateFlow(it) }
        val signedTransaction = verifyAndSign(transaction)
        val transactionSignedByAllParties = collectSignature(signedTransaction, sessions)
        return recordTransactionWithCounterParty(transactionSignedByAllParties, sessions)
    }

    private fun outputCompanyState(): CompanyState {
        println("******ourIdentity******")
        val self = ourIdentity.toString()
        println(self)
        println("******ourIdentity******")
        val nodes : List<Party>
        nodes = when (self){

            "O=Aerotrax, L=London, C=GB" -> listOf(ourIdentity, getMRONode(), getOEMNode(), getAirline1Node())
            "O=MRO, L=New York, C=US" -> listOf(ourIdentity, getAerotraxNode(), getOEMNode(), getAirline1Node())
            "O=OEM, L=New York, C=US" -> listOf(ourIdentity, getAerotraxNode(), getMRONode(), getAirline1Node())
            "O=Airline1, L=New York, C=US" -> listOf(ourIdentity, getAerotraxNode(), getMRONode(), getOEMNode())
            "O=Airline2, L=New York, C=US" -> listOf(ourIdentity, getAerotraxNode(), getMRONode(), getOEMNode(), getAirline1Node())
            else -> throw CordaException("Identity cannot be detected.")
        }

        return CompanyState(
                name = name,
                email = email,
                password = password,
                type = type,
                node = ourIdentity,
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
                createdBy = createdBy,
                createdAt = Instant.now(),
                updatedAt = null,
                connections = null,
                requests = null,
                linearId = UniqueIdentifier(),
                participants = nodes
        )
        }

    private fun transaction(): TransactionBuilder {
        val builder = TransactionBuilder(notary())
        val productCmd = Command(CompanyContract.Commands.Create(), outputCompanyState().participants.map { it.owningKey })
        builder.addOutputState(outputCompanyState(), CompanyContract.ID)
        builder.addCommand(productCmd)
        return builder
    }
}

@InitiatedBy(RegisterCompanyFlow::class)
class RegisterCompanyFlowResponder(val flowSession: FlowSession) : FlowLogic<SignedTransaction>() {

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