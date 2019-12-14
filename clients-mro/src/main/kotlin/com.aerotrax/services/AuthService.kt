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
import org.springframework.stereotype.Service


@Service
class AuthService (private val rpc: NodeRPCConnection, private val fhc: FlowHandlerCompletion): IAuth {

    override fun seedCompany(): MainCompanyDTO {
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