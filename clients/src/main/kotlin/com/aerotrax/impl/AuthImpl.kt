package com.aerotrax.impl

import com.aerotrax.dto.ResponseDTO
import com.aerotrax.dto.company.mapToMainCompanyDTO
import com.aerotrax.flows.company.AddNewParticipantFlow
import com.aerotrax.flows.company.RegisterCompanyFlow
import com.aerotrax.service.AuthService
import com.aerotrax.util.*
import com.aerotrax.webserver.NodeRPCConnection
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController

@Service
class AuthImpl (private val rpc: NodeRPCConnection,
                private val fhc: HandlerCompletion,
                private val function: Functions,
                private val response: Response): AuthService {

    companion object {
        private val logger = LoggerFactory.getLogger(RestController::class.java)!!
    }

    /**
     * Seed Company Data
     */
    override fun seedCompany(): ResponseEntity<ResponseDTO> {

        return try {

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

            val flowCompanyReturn = rpc.proxy.startFlowDynamic(
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
            fhc.handlerCompletion(flowCompanyReturn)
            val flowResult = function.returnCompanyState(flowCompanyReturn)

            val flowParticipantReturn = rpc.proxy.startFlowDynamic(
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
            fhc.handlerCompletion(flowParticipantReturn)

            response.successfulResponse(
                    response = mapToMainCompanyDTO(flowResult),
                    message = "seed company data"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "seed company data"
            )
        }
    }
}