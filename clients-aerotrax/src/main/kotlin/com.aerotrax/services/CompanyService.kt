package com.aerotrax.services

import com.aerotrax.dto.*
import com.aerotrax.flows.company.ApproveRejectConnectionFlow
import com.aerotrax.flows.company.RegisterCompanyFlow
import com.aerotrax.flows.company.RequestConnectionFlow
import com.aerotrax.services.interfaces.ICompany
import com.aerotrax.states.CompanyState
import com.aerotrax.states.ConnectionState
import com.aerotrax.util.FlowHandlerCompletion
import com.aerotrax.webserver.NodeRPCConnection
import javassist.NotFoundException
import org.springframework.stereotype.Service


@Service
class CompanyService (private val rpc: NodeRPCConnection, private val fhc: FlowHandlerCompletion): ICompany
{
    override fun getAllCompany(): List<MainCompanyDTO>
    {
        val companyState = rpc.proxy.vaultQuery(CompanyState::class.java).states
        return companyState.map { mapToMainCompanyDTO(it.state.data) }
    }

    override fun getACompany(id: String): MainCompanyDTO
    {
        val companyState = rpc.proxy.vaultQuery(CompanyState::class.java).states
        val company = companyState.find { it.state.data.linearId.toString() == id }?.state?.data ?: throw NotFoundException("Company Not Found")
        return mapToMainCompanyDTO(company)
    }

    override fun getAllRequestingConnections(id:String): List<MainCompanyDTO>
    {
        val companyState = rpc.proxy.vaultQuery(CompanyState::class.java).states
        val company = companyState.find { it.state.data.linearId.toString() == id }?.state?.data ?: throw NotFoundException("Company Not Found")

        val requests = company.requests
        val myRequests = mutableListOf<CompanyState>()

        requests?.map { company ->
            val otherCompany = companyState.find { it.state.data.linearId.toString() == company }
            myRequests.add(otherCompany!!.state.data)
        }
        return myRequests.map { mapToMainCompanyDTO(it) }
    }

    override fun getAllConnections(id:String): List<MainCompanyDTO>
    {
        val companyState = rpc.proxy.vaultQuery(CompanyState::class.java).states
        val company = companyState.find { it.state.data.linearId.toString() == id }?.state?.data ?: throw NotFoundException("Company Not Found")

        val connections = company.connections
        val myConnections = mutableListOf<CompanyState>()

        connections?.map { company ->
        val otherCompany = companyState.find { it.state.data.linearId.toString() == company }
        myConnections.add(otherCompany!!.state.data)
        }
        return myConnections.map { mapToMainCompanyDTO(it) }
    }

    override fun createCompany(request: RegisterCompanyDTO): MainCompanyDTO
    {
        val flowReturn = rpc.proxy.startFlowDynamic(
                RegisterCompanyFlow::class.java,
                request.name,
                request.email,
                request.password,
                request.type,
                request.logoImage,
                request.contactNumber,
                request.rate,
                request.website,
                request.linkedIn,
                request.about,
                request.location,
                request.addressLine1,
                request.addressLine2,
                request.city,
                request.state,
                request.country,
                request.zipCode,
                request.review,
                request.createdBy
        )
        fhc.flowHandlerCompletion(flowReturn)
        val flowResult = flowReturn.returnValue.get().coreTransaction.outputStates.first() as CompanyState
        return mapToMainCompanyDTO(flowResult)
    }

    override fun requestConnection(id: String, request: RequestConnectionDTO): MainConnectionDTO
    {
        val flowReturn = rpc.proxy.startFlowDynamic(
                RequestConnectionFlow::class.java,
                id,
                request.requestCompanyId,
                request.createdBy
        )
        fhc.flowHandlerCompletion(flowReturn)
        val flowResult = flowReturn.returnValue.get().coreTransaction.outputStates.first() as ConnectionState
        return mapToMainConnectionDTO(flowResult)
    }

    override fun approveRejectConnection(id: String, request: ApproveRejectConnectionDTO): MainConnectionDTO
    {
        val flowReturn = rpc.proxy.startFlowDynamic(
                ApproveRejectConnectionFlow::class.java,
                id,
                request.approveReject,
                request.reason,
                request.createdBy
        )
        fhc.flowHandlerCompletion(flowReturn)
        val flowResult = flowReturn.returnValue.get().coreTransaction.outputStates.first() as ConnectionState
        return mapToMainConnectionDTO(flowResult)
    }

}