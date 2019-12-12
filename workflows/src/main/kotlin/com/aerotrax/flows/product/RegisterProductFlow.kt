package com.aerotrax.flows.product

import co.paralleluniverse.fibers.Suspendable
import com.aerotrax.contracts.ProductContract
import com.aerotrax.functions.FlowFunctions
import com.aerotrax.states.CompanyState
import com.aerotrax.states.ProductState
import javassist.NotFoundException
import net.corda.core.contracts.Command
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.StartableByRPC
import net.corda.core.node.services.queryBy
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import java.time.Instant

@StartableByRPC
class RegisterProductFlow (
        private val companyId: String,
        private val partName: String,
        private val partImage: String,
        private val partNumber: String,
        private val serialNumber: String,
        private val status: String,
        private val category: String,
        private val manufacturer: String,
        private val CMMTitle: String,
//        private val CMMStatus: String?,
        private val createdBy: String,
        private val transactionId: String
): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction {
        val transaction = transaction()
        val signedTransaction = verifyAndSign(transaction)
        return recordTransactionWithoutCounterParty(signedTransaction)
    }

    private fun outputProductState(): ProductState {

        val companyState = serviceHub.vaultService.queryBy<CompanyState>().states.find {
            it.state.data.linearId == stringToLinearID(companyId)
        }?: throw NotFoundException("Company Id not found")

        val companyName = companyState.state.data.name

        return ProductState(
                companyId = companyId,
                partName = partName,
                partImage = partImage,
                partNumber = partNumber,
                serialNumber = serialNumber,
                status = status,
                category = category,
                manufacturer = manufacturer,
                registeredOwner = companyName,
                newOwner = null,
                partAvailability = "Registering",
                views = null,
                rate = null,
                dealBit = null,
                createdBy = createdBy,
                updatedBy = null,
                createdAt = Instant.now(),
                updatedAt = null,
                transactionId = transactionId,
                linearId = UniqueIdentifier(),
                participants = listOf(ourIdentity)
        )
    }



    private fun transaction(): TransactionBuilder {
        val builder = TransactionBuilder(notary())
        val productCmd = Command(ProductContract.Commands.Create(), ourIdentity.owningKey)
        builder.addOutputState(outputProductState(), ProductContract.ID)
        builder.addCommand(productCmd)
        return builder
    }
}