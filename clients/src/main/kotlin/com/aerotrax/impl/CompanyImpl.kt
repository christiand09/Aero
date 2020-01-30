package com.aerotrax.impl

import com.aerotrax.dto.ResponseDTO
import com.aerotrax.dto.company.RegisterCompanyDTO
import com.aerotrax.dto.company.mapToMainCompanyDTO
import com.aerotrax.dto.connection.ApproveRejectConnectionDTO
import com.aerotrax.dto.connection.RequestConnectionDTO
import com.aerotrax.dto.connection.mapToMainConnectionDTO
import com.aerotrax.dto.login.LoginDTO
import com.aerotrax.dto.participant.MainParticipantDTO
import com.aerotrax.dto.participant.mapToMainParticipantDTO
import com.aerotrax.flows.company.AddNewParticipantFlow
import com.aerotrax.flows.company.ApproveRejectConnectionFlow
import com.aerotrax.flows.company.RegisterCompanyFlow
import com.aerotrax.flows.company.RequestConnectionFlow
import com.aerotrax.service.CompanyService
import com.aerotrax.util.*
import com.aerotrax.webserver.NodeRPCConnection
import javassist.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController

@Service
class CompanyImpl (private val rpc: NodeRPCConnection,
                   private val fhc: HandlerCompletion,
                   private val function: Functions,
                   private val response: Response): CompanyService {

    companion object {
        val logger = LoggerFactory.getLogger(RestController::class.java)!!
    }

    override fun login(request: LoginDTO): ResponseEntity<ResponseDTO> {

        return try {

            val companyState = function.companyState()
            val myCompany = companyState.find {
                it.state.data.email == request.userName &&
                        it.state.data.password == request.password
            } ?: throw NotFoundException("Wrong credentials.")

            response.successfulResponse(
                    response = mapToMainCompanyDTO(myCompany.state.data),
                    message = "login"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "login"
            )
        }
    }

    override fun getAllCompany(): ResponseEntity<ResponseDTO> {

        return try {

            val companyState = function.companyState()
            val listOfCompany = companyState.map { mapToMainCompanyDTO(it.state.data) }

            response.successfulResponse(
                    response = listOfCompany,
                    message = "get the list of companies"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "get the list of companies"
            )
        }
    }

    override fun getACompany(id: String): ResponseEntity<ResponseDTO> {

        return try {

            val companyState = function.companyState()
            val companyData = companyState.find {
                it.state.data.linearId.toString().equals(id, true)
            } ?: throw NotFoundException("Wrong id: $id.")

            response.successfulResponse(
                    response = mapToMainCompanyDTO(companyData.state.data),
                    message = "get the company data"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "get the company data"
            )
        }
    }

    override fun createCompany(request: RegisterCompanyDTO): ResponseEntity<ResponseDTO> {

        return try {

            val flowCompanyReturn = rpc.proxy.startFlowDynamic(
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
            fhc.handlerCompletion(flowCompanyReturn)
            val flowCompanyResult = function.returnCompanyState(flowCompanyReturn)

            val flowParticipantReturn = rpc.proxy.startFlowDynamic(
                    AddNewParticipantFlow::class.java,
                    request.name,
                    request.email,
                    request.type,
                    flowCompanyResult.node,
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
                    flowCompanyResult.linearId.toString()
            )
            fhc.handlerCompletion(flowParticipantReturn)

            response.successfulResponse(
                    response = mapToMainCompanyDTO(flowCompanyResult),
                    message = "get the company data"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "get the company data"
            )
        }
    }

    override fun requestConnection(id: String, request: RequestConnectionDTO): ResponseEntity<ResponseDTO> {

        return try {

            val flowConnectionReturn = rpc.proxy.startFlowDynamic(
                    RequestConnectionFlow::class.java,
                    id,
                    request.requestCompanyId,
                    request.requestMessage,
                    request.createdBy
            )
            fhc.handlerCompletion(flowConnectionReturn)
            val flowConnectionResult = function.returnConnectionState(flowConnectionReturn)

            response.successfulResponse(
                    response = mapToMainConnectionDTO(flowConnectionResult),
                    message = "request connection"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "request connection"
            )
        }
    }

    override fun approveRejectConnection(id: String, request: ApproveRejectConnectionDTO): ResponseEntity<ResponseDTO> {

        return try {

            val flowConnectionReturn = rpc.proxy.startFlowDynamic(
                    ApproveRejectConnectionFlow::class.java,
                    id,
                    request.approveReject,
                    request.reason,
                    request.createdBy
            )
            fhc.handlerCompletion(flowConnectionReturn)
            val flowConnectionResult = function.returnConnectionState(flowConnectionReturn)

            response.successfulResponse(
                    response = mapToMainConnectionDTO(flowConnectionResult),
                    message = "request connection"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "request connection"
            )
        }
    }

    override fun searchAllAvailableConnections(searchText: String?): ResponseEntity<ResponseDTO> {

        return try {

            val participantState = function.participantState()
            lateinit var listOfParticipants: List<MainParticipantDTO>
            if(searchText == null || searchText.isEmpty()){
                listOfParticipants = participantState.map { mapToMainParticipantDTO(it.state.data) }
                        .filter { it.name.contains(searchText.toString(), true)}
            }

            response.successfulResponse(
                    response = listOfParticipants,
                    message = "filter the available connections"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "filter the available connections"
            )
        }
    }

    override fun getAllParticipants(): ResponseEntity<ResponseDTO> {

        return try {

            val participantStates = function.participantState().map {
                mapToMainParticipantDTO(it.state.data)
            }

            response.successfulResponse(
                    response = participantStates,
                    message = "get the company data"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "get the company data"
            )
        }
    }

    override fun getAllConnections(id: String): ResponseEntity<ResponseDTO> {

        return try {

            val connectionState = function.connectionState()

            connectionState.find {
                it.state.data.linearId.toString().equals(id, true)
            } ?: throw NotFoundException("Wrong id: $id.")

            val listOfConnectionById = connectionState.filter {
                it.state.data.linearId.toString().equals(id, true)
            }.map { mapToMainConnectionDTO(it.state.data) }

            response.successfulResponse(
                    response = listOfConnectionById,
                    message = "get all the company by Id: $id"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "get all the company by Id: $id"
            )
        }
    }

    override fun getAllRequestConnectionsFromOthers(id: String): ResponseEntity<ResponseDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllMyConnectionRequests(id: String): ResponseEntity<ResponseDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNumberCurrentConnections(id: String): ResponseEntity<ResponseDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNumberRequestConnections(id: String): ResponseEntity<ResponseDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sortCurrentConnection(id: String, sortType: String): ResponseEntity<ResponseDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sortAllRequestConnectionsFromOthers(id: String, sortType: String): ResponseEntity<ResponseDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sortAllMyConnectionRequests(id: String, sortType: String): ResponseEntity<ResponseDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAConnection(id: String, idOfOther: String): ResponseEntity<ResponseDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}