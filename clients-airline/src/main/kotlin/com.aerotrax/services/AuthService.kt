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
class AuthService (private val rpc: NodeRPCConnection, private val fhc: FlowHandlerCompletion): IAuth
{
    override fun seedCompany(): MainCompanyDTO {
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