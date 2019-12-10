package com.aerotrax.flows

import co.paralleluniverse.fibers.Suspendable
import com.aerotrax.contracts.ProductContract
import com.aerotrax.functions.FlowFunctions
import com.aerotrax.states.ProductState
import com.ipn.platforms.dezrez.states.CompanyState
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
        private val name: String,
        private val partNumber: String,
        private val serialNumber: String,
        private val manufacturer: String,
        private val partType: String,
        private val status: String,
        private val type: String,
        private val category: String,
        private val createdBy: String,
        private val transactionId: String
): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction {
        val transaction = transaction()
        val signedTransaction = verifyAndSign(transaction)
        return recordTransactionWithoutCounterParty(signedTransaction)
    }

    private fun output(): ProductState {

        val companyState = serviceHub.vaultService.queryBy<CompanyState>().states.find {
            it.state.data.linearId == stringToLinearID(companyId)
        }?: throw NotFoundException("Company Id not found")

        val companyName = companyState.state.data.name

        return ProductState(
                companyId = companyId,
                name = name,
                partNumber = partNumber,
                serialNumber = serialNumber,
                manufacturer = manufacturer,
                registeredOwner = companyName,
                newOwner = null,
                partType = partType,
                status = status,
                saleStatus = null,
                type = type,
                category = category,
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
        builder.addOutputState(output(), ProductContract.ID)
        builder.addCommand(productCmd)
        return builder
    }
}