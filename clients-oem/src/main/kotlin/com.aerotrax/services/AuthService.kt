package com.aerotrax.services

import com.aerotrax.dto.*
import com.aerotrax.flows.company.ApproveRejectConnectionFlow
import com.aerotrax.flows.company.RegisterCompanyFlow
import com.aerotrax.flows.company.RequestConnectionFlow
import com.aerotrax.services.interfaces.IAuth
import com.aerotrax.services.interfaces.ICompany
import com.aerotrax.states.CompanyState
import com.aerotrax.states.ConnectionState
import com.aerotrax.states.ParticipantState
import com.aerotrax.util.FlowHandlerCompletion
import com.aerotrax.webserver.NodeRPCConnection
import javassist.NotFoundException
import net.corda.core.CordaException
import net.corda.core.contracts.requireThat
import org.springframework.stereotype.Service


@Service
class AuthService (private val rpc: NodeRPCConnection, private val fhc: FlowHandlerCompletion): IAuth {

    override fun seedCompany(): MainCompanyDTO {
        val name = "Boeing"
        val email = "admin@boeing"
        val password = "qwerty"
        val type = "company"
        val logoImage = "image"
        val contactNumber = "972-705-8100"
        val rate = null
        val website = "https://www.boeing.com/"
        val linkedIn = "https://www.linkedin.com/company/boeing"
        val about = "The Connective Tissue for Aviation Stakeholders\n\nEnd-to-End Supply Chain Integration:\nEach physical aircraft part is represented as a digital asset via the custom-engineered Aerotrax Identifier. This enables manufacturers, service providers, and airlines to easily capture the historical life cycle of aircraft parts by significantly streamlining the data coordination process.\n\nOnline-Permissioned Marketplace for Parts & Data:\nSuppliers can securely share information associated with their listed parts to selected connections, while procurement agents are able to proactively find, assess, and purchase parts from approved vendors. This module maintains the private nature of the industry while facilitating the exchange of parts and data.\n\nBusiness Operations & Communications Network:\nAn enhanced digital infrastructure that connects communication channels across siloed departments. This empowers participants to effectively manage supply chain networks while facilitating aftermarket exchanges."
        val location = ""
        val addressLine1 = "100 N. Riverside"
        val addressLine2 = "Chicago, IL 60606-1596, US"
        val city = "Chicago"
        val state = "Illinois"
        val country = "United States"
        val zipCode = "75024"
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
        return mapToMainCompanyDTO(flowResult)
    }

}