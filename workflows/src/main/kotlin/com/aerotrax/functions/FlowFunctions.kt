package com.aerotrax.functions

import co.paralleluniverse.fibers.Suspendable
import com.aerotrax.constants.NodeConstants.Companion.AEROTRAX_NODE
import com.aerotrax.constants.NodeConstants.Companion.AIRLINE1_NODE
import com.aerotrax.constants.NodeConstants.Companion.AIRLINE2_NODE
import com.aerotrax.constants.NodeConstants.Companion.MRO_NODE
import com.aerotrax.constants.NodeConstants.Companion.OEM_NODE
import com.aerotrax.states.*
import javassist.NotFoundException
import net.corda.core.contracts.StateAndRef
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.CollectSignaturesFlow
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.FlowSession
import net.corda.core.identity.CordaX500Name
import net.corda.core.identity.Party
import net.corda.core.node.ServiceHub
import net.corda.core.node.services.queryBy
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import java.time.Instant

abstract class FlowFunctions : FlowLogic<SignedTransaction>() {

    /**
     * Transaction functions
     */
    fun notary(): Party = serviceHub.networkMapCache.notaryIdentities.first()

    fun verifyAndSign(transaction: TransactionBuilder): SignedTransaction {
        transaction.verify(serviceHub)
        return serviceHub.signInitialTransaction(transaction)
    }

    @Suspendable
    fun collectSignature(
            transaction: SignedTransaction,
            sessions: List<FlowSession>
    ): SignedTransaction = subFlow(CollectSignaturesFlow(transaction, sessions))

    @Suspendable
    fun recordTransactionWithCounterParty(transaction: SignedTransaction, sessions: List<FlowSession>): SignedTransaction {
        return subFlow(FinalityFlow(transaction, sessions))
    }

    @Suspendable
    fun recordTransactionWithoutCounterParty(transaction: SignedTransaction) : SignedTransaction {
        return subFlow(FinalityFlow(transaction, emptyList()))
    }


    /**
     * Convert functions
     */

    fun stringToLinearID(id: String): UniqueIdentifier {
        return UniqueIdentifier.fromString(id)
    }

    fun stringToParty(name: String): Party {
        return serviceHub.identityService.wellKnownPartyFromX500Name(CordaX500Name.parse(name))
                ?: throw Exception("No match found for $name")
    }

    /**
     * Nodes
     */

    fun getAerotraxNode() = stringToParty(AEROTRAX_NODE)

    fun getMRONode() = stringToParty(MRO_NODE)

    fun getOEMNode() = stringToParty(OEM_NODE)

    fun getAirline1Node() = stringToParty(AIRLINE1_NODE)

    fun getAirline2Node() = stringToParty(AIRLINE2_NODE)

    fun parties(): List<Party>{
        return listOf(
                stringToParty(AEROTRAX_NODE),
                stringToParty(MRO_NODE),
                stringToParty(OEM_NODE),
                stringToParty(AIRLINE1_NODE)
//                stringToParty(AIRLINE2_NODE)
        )
    }

    /**
     * Get State functions
     */

    fun getCompanyStateById(id: String): StateAndRef<CompanyState> {
        return serviceHub.vaultService.queryBy<CompanyState>().states.find { it.state.data.linearId.toString() == id }
                ?: throw NotFoundException("Company Id Not Found.")
    }

    fun getConnectionStateById(id: String): StateAndRef<ConnectionState> {
        return serviceHub.vaultService.queryBy<ConnectionState>().states.find { it.state.data.linearId.toString() == id }
                ?: throw NotFoundException("Connection Id Not Found.")
    }

    fun getParticipantStateById(id: String): StateAndRef<ParticipantState> {
        return serviceHub.vaultService.queryBy<ParticipantState>().states.find { it.state.data.linearId.toString() == id }
                ?: throw NotFoundException("Participant Id Not Found.")
    }

    fun getProductStateById(id: String): StateAndRef<ProductState> {
        return serviceHub.vaultService.queryBy<ProductState>().states.find { it.state.data.linearId.toString() == id }
                ?: throw NotFoundException("Product Id Not Found.")
    }

}