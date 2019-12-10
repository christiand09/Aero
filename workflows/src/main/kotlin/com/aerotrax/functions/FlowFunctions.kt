package com.aerotrax.functions

import co.paralleluniverse.fibers.Suspendable
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.CollectSignaturesFlow
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.FlowSession
import net.corda.core.identity.Party
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker

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

}