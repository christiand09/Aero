package com.aerotrax.services

import com.aerotrax.dto.MainCompanyDTO
import com.aerotrax.dto.mapToMainCompanyDTO
import com.aerotrax.flows.company.AddNewParticipantFlow
import com.aerotrax.flows.company.RegisterCompanyFlow
import com.aerotrax.services.interfaces.IAuth
import com.aerotrax.states.CompanyState
import com.aerotrax.util.FlowHandlerCompletion
import com.aerotrax.webserver.NodeRPCConnection
import org.springframework.stereotype.Service


@Service
class AuthService (private val rpc: NodeRPCConnection, private val fhc: FlowHandlerCompletion): IAuth {

    override fun seedCompany(): MainCompanyDTO {
         val name = "Aerotrax"
        val email = "admin@aerotrax"
        val password = "qwerty"
        val type = "company"
        val node = "O=Aerotrax, L=London, C=GB"
        val logoImage = "image"
        val contactNumber = "212-201-2377"
        val rate = null
        val website = "https://aerotrax.org/"
        val linkedIn = "https://www.linkedin.com/company/aerotrax-technologies"
        val about = "The Connective Tissue for Aviation Stakeholders\n\nEnd-to-End Supply Chain Integration:\nEach physical aircraft part is represented as a digital asset via the custom-engineered Aerotrax Identifier. This enables manufacturers, service providers, and airlines to easily capture the historical life cycle of aircraft parts by significantly streamlining the data coordination process.\n\nOnline-Permissioned Marketplace for Parts & Data:\nSuppliers can securely share information associated with their listed parts to selected connections, while procurement agents are able to proactively find, assess, and purchase parts from approved vendors. This module maintains the private nature of the industry while facilitating the exchange of parts and data.\n\nBusiness Operations & Communications Network:\nAn enhanced digital infrastructure that connects communication channels across siloed departments. This empowers participants to effectively manage supply chain networks while facilitating aftermarket exchanges."
        val location = ""
        val addressLine1 = "11 West 42nd Street, 8th Floor"
        val addressLine2 = "New York, NY 10036"
        val city = "NY"
        val state = "New York"
        val country = "United States"
        val zipCode = "10036"
        val review = null
        val createdBy = "Aerotrax Seed"

        val flowReturn = rpc.proxy.startFlowDynamic(
                RegisterCompanyFlow::class.java,
                name,
                email,
                password,
                type,
                logoImage,
                contactNumber,
                rate,
                website,
                linkedIn,
                about,
                location,
                addressLine1,
                addressLine2,
                city,
                state,
                country,
                zipCode,
                review,
                createdBy
        )
        fhc.flowHandlerCompletion(flowReturn)
        val flowResult = flowReturn.returnValue.get().coreTransaction.outputStates.first() as CompanyState

        val flowReturnParticipant = rpc.proxy.startFlowDynamic(
                AddNewParticipantFlow::class.java,
                name,
                email,
                type,
                node,
                logoImage,
                contactNumber,
                rate,
                website,
                linkedIn,
                about,
                location,
                addressLine1,
                addressLine2,
                city,
                state,
                country,
                zipCode,
                review,
                flowResult.linearId.toString()
        )
        fhc.flowHandlerCompletion(flowReturnParticipant)

        return mapToMainCompanyDTO(flowResult)
    }

}