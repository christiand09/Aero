package com.aerotrax.flows.company

import co.paralleluniverse.fibers.Suspendable
import com.aerotrax.contracts.CompanyContract
import com.aerotrax.functions.FlowFunctions
import com.aerotrax.states.CompanyState
import net.corda.core.contracts.Command
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import java.time.Instant

@StartableByRPC
class RegisterCompanyFlow (
        private val name: String,
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
        private val createdBy: String
): FlowFunctions() {

    @Suspendable
    override fun call(): SignedTransaction {
        val output = outputCompanyState()
        val signedTransaction = verifyAndSign(transaction())
        return recordTransactionWithoutCounterParty(signedTransaction).also {
            subFlow(AddNewParticipantFlow(
                    name = output.name,
                    email = output.email,
                    type = output.type,
                    node = output.node,
                    logoImage = output.logoImage,
                    contactNumber = output.contactNumber,
                    rate = output.rate,
                    website = output.website,
                    linkedIn = output.linkedIn,
                    about = output.about,
                    location = output.location,
                    addressLine1 = output.addressLine1,
                    addressLine2 = output.addressLine2,
                    city = output.city,
                    state = output.state,
                    country = output.country,
                    zipCode = output.zipCode,
                    review = output.review,
                    linearId = output.linearId.toString()
            )
            )
        }
    }

    private fun outputCompanyState(): CompanyState {
        return CompanyState(
                name = name,
                email = email,
                password = password,
                type = type,
                node = ourIdentity.toString(),
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
                linearId = UniqueIdentifier(),
                participants = listOf(ourIdentity)
        )
    }

    private fun transaction(): TransactionBuilder {
        val builder = TransactionBuilder(notary())
        val companyCmd = Command(CompanyContract.Commands.Create(), ourIdentity.owningKey)
        builder.addOutputState(outputCompanyState(), CompanyContract.ID)
        builder.addCommand(companyCmd)
        return builder
    }
}