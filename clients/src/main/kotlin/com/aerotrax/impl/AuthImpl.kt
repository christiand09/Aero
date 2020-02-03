package com.aerotrax.impl

import com.aerotrax.constant.AppConstants.Companion.LOCAL_NODE_AEROTRAX
import com.aerotrax.constant.AppConstants.Companion.LOCAL_NODE_AIRLINE1
import com.aerotrax.constant.AppConstants.Companion.LOCAL_NODE_MRO
import com.aerotrax.constant.AppConstants.Companion.LOCAL_NODE_OEM
import com.aerotrax.constant.AppConstants.Companion.SERVER_NODE_AEROTRAX
import com.aerotrax.constant.AppConstants.Companion.SERVER_NODE_AIRLINE1
import com.aerotrax.constant.AppConstants.Companion.SERVER_NODE_MRO
import com.aerotrax.constant.AppConstants.Companion.SERVER_NODE_OEM
import com.aerotrax.dto.ResponseDTO
import com.aerotrax.dto.company.mapToMainCompanyDTO
import com.aerotrax.flows.company.AddNewParticipantFlow
import com.aerotrax.flows.company.RegisterCompanyFlow
import com.aerotrax.service.AuthService
import com.aerotrax.states.CompanyState
import com.aerotrax.util.*
import com.aerotrax.webserver.NodeRPCConnection
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException

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
    override fun seedCompany(name: String): ResponseEntity<ResponseDTO> {

        return try {

            val myIdentity = rpc.proxy.nodeInfo().legalIdentities.single().name.toString()
            lateinit var state: CompanyState

            when (name){
                "aerotraxlocal" -> {
                    if (myIdentity == LOCAL_NODE_AEROTRAX) state = aerotraxInfo("aerotraxlocal") else throw IllegalArgumentException("Incorrect Node")
                }
                "airline1local" -> {
                    if (myIdentity == LOCAL_NODE_AIRLINE1) state = americanAirlineInfo("airline1local") else throw IllegalArgumentException("Incorrect Node")
                }
                "mrolocal" -> {
                    if (myIdentity == LOCAL_NODE_MRO) state = mroInfo("mrolocal") else throw IllegalArgumentException("Incorrect Node")
                }
                "oemlocal" -> {
                    if (myIdentity == LOCAL_NODE_OEM) state = oemInfo("oemlocal") else throw IllegalArgumentException("Incorrect Node")
                }
                "aerotrax" -> {
                    if (myIdentity == SERVER_NODE_AEROTRAX) state = aerotraxInfo("aerotrax") else throw IllegalArgumentException("Incorrect Node")
                }
                "airline1" -> {
                    if (myIdentity == SERVER_NODE_AIRLINE1) state = americanAirlineInfo("airline1") else throw IllegalArgumentException("Incorrect Node")
                }
                "mro" -> {
                    if (myIdentity == SERVER_NODE_MRO) state = mroInfo("mro") else throw IllegalArgumentException("Incorrect Node")
                }
                "oem" -> {
                    if (myIdentity == SERVER_NODE_OEM) state = oemInfo("oem") else throw IllegalArgumentException("Incorrect Node")
                }
                else -> {
                    throw IllegalArgumentException("Incorrect name: $name")
                }
            }

            response.successfulResponse(
                    response = mapToMainCompanyDTO(state),
                    message = "seed company data"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "seed company data"
            )
        }
    }

    private fun aerotraxInfo(status: String): CompanyState{

        val node = if (status.equals("aerotraxlocal", true)) LOCAL_NODE_AEROTRAX else SERVER_NODE_AEROTRAX
        val name = "Aerotrax"
        val email = "admin@aerotrax"
        val password = "qwerty"
        val type = "company"
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
        return flowResult
    }

    private fun americanAirlineInfo(status: String): CompanyState{

        val node = if (status.equals("airline1local", true)) LOCAL_NODE_AIRLINE1 else SERVER_NODE_AIRLINE1
        val name = "American Airlines"
        val email = "admin@american_airlines"
        val password = "qwerty"
        val type = "company"
        val logoImage = "image"
        val contactNumber = "800-433-7300"
        val rate = null
        val website = "https://www.americanairlines.ie/intl/ie/index.jsp?locale=en_IE"
        val linkedIn = "https://ph.linkedin.com/company/american-airlines"
        val about = "\"We welcome people to our company who want to be part of restoring American to the greatest airlines in the world.\"\n- Doug Parker Chairman & CEO\n\nOn the morning of April 15, 1926, a young aviator named Charles A. Lindbergh stowed a bag of mail in his little DH-4 biplane and took off from Chicago for St. Louis. Later that day, he and two other pilots flew three plane loads of mail from St. Louis to Chicago. Lindbergh was chief pilot of Robertson Aircraft Corporation, the second aviation company to hold a U.S. airmail contract, and one of scores of companies that eventually consolidated to form the modern-day American Airlines.\n\nToday, American Airlines is one of the largest airlines in the world. We've also created more than 900,000 jobs worldwide and we welcome people like you who are excited to join our #AATeam. Explore the possibilities and discover why our employees are #ProudToBeAA!\n\nInterested in joining the new American? Our premier global team, more than 100,000 people strong, is working hard every day to restore our airline as the greatest in the world. We owe our employees the best company culture in the business – one that’s built on respect. Respect for the product we’re providing our customers, respect for our company heritage and for our collective future. And most importantly, respect for each other."
        val location = ""
        val addressLine1 = "4333 Amon Carter Blvd."
        val addressLine2 = "Fort Worth, TX 76155, US"
        val city = "Fort Worth"
        val state = "Texas"
        val country = "United States"
        val zipCode = "76155"
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
        return flowResult
    }

    private fun mroInfo(status: String): CompanyState{

        val node = if (status.equals("mrolocal", true)) LOCAL_NODE_MRO else SERVER_NODE_MRO
        val name = "GA Telesis"
        val email = "admin@ga_telesis"
        val password = "qwerty"
        val type = "company"
        val logoImage = "image"
        val contactNumber = "+1-954-348-3535"
        val rate = null
        val website = "https://www.gatelesis.com/"
        val linkedIn = "https://www.linkedin.com/company/gatelesis"
        val about = "Headquartered in Fort Lauderdale, Florida, GA Telesis LLC is one of the world’s largest commercial aerospace firms with over \$1.0 billion in assets under management.\n\nGA Telesis operates sales, leasing, distribution, and maintenance facilities in the United States, Canada, United Kingdom, Finland and China. Over the past fifteen years, GA Telesis has emerged as a global leader in the aerospace industry.\n\nGA Telesis is uniquely positioned to deliver complete global solutions to companies seeking to gain competitive advantages in the industry. Our company is comprised of multiple integrated business units designed to maximize the value of commercial aircraft and engines throughout their entire useful life cycle. Our sales, distribution and maintenance facilities are strategically positioned around the globe and our dedicated professional team extensive relationships and industry experience enables us to execute our strategies.\n\nWith the support of our independent shareholders, Bank of America Merrill Lynch and Century Tokyo Leasing Corporation, GA Telesis has the unique ability to acquire, invest, co-invest and maximize the value of aerospace related assets and companies.\n\nGA Telesis offers the highest quality aircraft, engines, components, maintenance and solution-based services available. We have achieved the highest organizational accreditations, including ISO-9001, AS-9120, AS9110, EASO, FAA, EASA and CAAC certifications."
        val location = ""
        val addressLine1 = "1850 NW 49th Street"
        val addressLine2 = "Fort Lauderdale, FL 33309, US"
        val city = "Fort Lauderdale"
        val state = "Florida"
        val country = "United States"
        val zipCode = "33309"
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
        return flowResult
    }


    private fun oemInfo(status: String): CompanyState{

        val node = if (status.equals("oemlocal", true)) LOCAL_NODE_OEM else SERVER_NODE_OEM
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
        return flowResult
    }
}