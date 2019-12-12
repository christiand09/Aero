package com.aerotrax.flows.product

import co.paralleluniverse.fibers.Suspendable
import com.aerotrax.contracts.ARCContract
import com.aerotrax.functions.FlowFunctions
import com.aerotrax.states.ARCState
import net.corda.core.contracts.Command
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import java.time.Instant

@StartableByRPC
class RegisterARCFlow (
        private val companyId: String,
        private val productId: String,
        private val remarks: String,
        private val approvedReject: Boolean,
        private val createdBy: String
): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction {
        val transaction = transaction()
        val signedTransaction = verifyAndSign(transaction)
        return recordTransactionWithoutCounterParty(signedTransaction)
    }

    private fun outputARCState(): ARCState {
        val productState = getProductStateById(productId).state.data
        val serialNumber = productState.serialNumber
        val approved: Instant?
        val disapproved: Instant?
        if (approvedReject){
            approved = Instant.now()
            disapproved = null
        }else{
            approved = null
            disapproved = Instant.now()
        }

        return ARCState(
                companyId = companyId,
                serialNumber = serialNumber,
                productId = productState.linearId.toString(),
                contractNumber = UniqueIdentifier().toString(),
                trackNumber = UniqueIdentifier().toString(),
                remarks = remarks,
                approved = approved,
                disapproved = disapproved,
                createdBy = createdBy,
                createdAt = Instant.now(),
                updatedAt = null,
                linearId = UniqueIdentifier(),
                participants = listOf(ourIdentity)
        )
    }

    private fun transaction(): TransactionBuilder {
        val builder = TransactionBuilder(notary())
        val arcCmd = Command(ARCContract.Commands.Create(), ourIdentity.owningKey)
        builder.addOutputState(outputARCState(), ARCContract.ID)
        builder.addCommand(arcCmd)
        return builder
    }
}