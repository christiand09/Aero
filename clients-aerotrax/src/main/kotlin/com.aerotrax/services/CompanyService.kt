package com.aerotrax.services

import com.aerotrax.AppConstants.Companion.DEFAULT_USER_ID
import com.aerotrax.dto.*
import com.aerotrax.flows.company.AddNewParticipantFlow
import com.aerotrax.flows.company.ApproveRejectConnectionFlow
import com.aerotrax.flows.company.RegisterCompanyFlow
import com.aerotrax.flows.company.RequestConnectionFlow
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
class CompanyService (private val rpc: NodeRPCConnection, private val fhc: FlowHandlerCompletion): ICompany
{
    override var currentUserId: String = DEFAULT_USER_ID

    override fun setCurrentUserDetails(userId: String?)
    {
        currentUserId = userId ?: currentUserId
    }

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

    override fun createCompany(request: RegisterCompanyDTO): MainCompanyDTO {
        val registerCompanyFlowReturn = rpc.proxy.startFlowDynamic(
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
        fhc.flowHandlerCompletion(registerCompanyFlowReturn)
        val companyState = registerCompanyFlowReturn.returnValue.get().coreTransaction.outputStates.first() as CompanyState

        val flowReturn = rpc.proxy.startFlowDynamic(
                AddNewParticipantFlow::class.java,
                request.name,
                request.email,
                request.type,
                companyState.node,
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
                companyState.linearId.toString()
        )
        fhc.flowHandlerCompletion(flowReturn)

        return mapToMainCompanyDTO(companyState)
    }

    override fun requestConnection(id: String, request: RequestConnectionDTO): MainConnectionDTO
    {
        val flowReturn = rpc.proxy.startFlowDynamic(
                RequestConnectionFlow::class.java,
                id,
                request.requestCompanyId,
                request.requestMessage,
                request.createdBy
        )
        fhc.flowHandlerCompletion(flowReturn)
        val flowResult = flowReturn.returnValue.get().coreTransaction.outputStates.first() as ConnectionState
        return mapToMainConnectionDTO(flowResult)
    }

    override fun approveRejectConnection(id: String, request: ApproveRejectConnectionDTO): MainConnectionDTO
    {
//        val answer = request.approveReject
//        val reason = request.reason
//
//        if (answer == true){
//            requireThat { "Reason should be empty" using (reason == null) }
//        }
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

    override fun searchAllAvailableConnections(searchText: String?): List<MainParticipantDTO>
    {
        val participantState = rpc.proxy.vaultQuery(ParticipantState::class.java).states.map { it }
        if(searchText == null || searchText.isEmpty())
            return participantState.map { mapToMainParticipantDTO(it.state.data) }
//        val normalizedSearchText = searchText.toLowerCase().trim()
            return participantState.map { mapToMainParticipantDTO(it.state.data) }.filter { it.name.contains(searchText, true)}
    }

    override fun getAllParticipants(): List<MainParticipantDTO>
    {
        val participantState = rpc.proxy.vaultQuery(ParticipantState::class.java).states.map { it }
        return participantState.map { mapToMainParticipantDTO(it.state.data)}
    }

    override fun getAllConnections(id:String): MutableList<MainConnectionWithCompanyDTO>
    {
        val connectionState = rpc.proxy.vaultQuery(ConnectionState::class.java).states
        val myConnection =  connectionState.map { it }.filter { (it.state.data.companyId == id || it.state.data.requestCompanyId == id) && it.state.data.acceptedAt != null  }
                if (myConnection.isEmpty()) throw CordaException("You do not have any connections yet")

        val list = mutableListOf<MainConnectionWithCompanyDTO>()

        val participantState = rpc.proxy.vaultQuery(ParticipantState::class.java).states


        myConnection.map { connect ->
            if (connect.state.data.companyId == id){
                val myParticipants = participantState.find { it.state.data.linearId.toString() == connect.state.data.requestCompanyId }
                        ?:throw NotFoundException("Participant not found.")
                list.add(mapToMainConnectionWithCompanyDTO(connect.state.data, myParticipants.state.data))
            }
            else{
            val myParticipants = participantState.find { it.state.data.linearId.toString() == connect.state.data.companyId }
                    ?:throw NotFoundException("Participant not found.")
            list.add(mapToMainConnectionWithCompanyDTO(connect.state.data, myParticipants.state.data))}
        }
        return list
    }

    override fun getAllRequestConnectionsFromOthers(id:String): MutableList<MainConnectionWithCompanyDTO>
    {
        val connectionState = rpc.proxy.vaultQuery(ConnectionState::class.java).states
        val myConnection =  connectionState.map { it }.filter { (it.state.data.requestCompanyId == id) && it.state.data.acceptedAt == null  }
        if (myConnection.isEmpty()) throw CordaException("You do not have any request connections yet")

        val list = mutableListOf<MainConnectionWithCompanyDTO>()

        val participantState = rpc.proxy.vaultQuery(ParticipantState::class.java).states

        myConnection.map { connect ->
            val myParticipants = participantState.find { it.state.data.linearId.toString() == connect.state.data.companyId }
                    ?:throw NotFoundException("Participant not found.")
            list.add(mapToMainConnectionWithCompanyDTO(connect.state.data, myParticipants.state.data))
        }
        return list
    }

    override fun getAllMyConnectionRequests(id:String): MutableList<MainConnectionWithCompanyDTO>
    {
        val connectionState = rpc.proxy.vaultQuery(ConnectionState::class.java).states
        val myConnection =  connectionState.map { it }.filter { (it.state.data.companyId == id) && it.state.data.acceptedAt == null  }
        if (myConnection.isEmpty()) throw CordaException("You do not have any request connections yet")

        val list = mutableListOf<MainConnectionWithCompanyDTO>()

        val participantState = rpc.proxy.vaultQuery(ParticipantState::class.java).states

        myConnection.map { connect ->
            val myParticipants = participantState.find { it.state.data.linearId.toString() == connect.state.data.requestCompanyId }
                    ?:throw NotFoundException("Participant not found.")
            list.add(mapToMainConnectionWithCompanyDTO(connect.state.data, myParticipants.state.data))
        }
        return list
    }

    override fun getNumberCurrentConnections(id:String): String
    {
        val connectionState = rpc.proxy.vaultQuery(ConnectionState::class.java).states
        val myConnection =  connectionState.map { it }.filter { (it.state.data.companyId == id || it.state.data.requestCompanyId == id) && it.state.data.acceptedAt != null  }
        if (myConnection.isEmpty()) throw CordaException("You do not have any connections yet")
        val number = myConnection.size.toString()
        return number
    }

    override fun getNumberRequestConnections(id:String): String
    {
        val connectionState = rpc.proxy.vaultQuery(ConnectionState::class.java).states
        val myConnection =  connectionState.map { it }.filter { (it.state.data.requestCompanyId == id) && it.state.data.acceptedAt == null  }
        if (myConnection.isEmpty()) throw CordaException("You do not have any request connections yet")
        val number = myConnection.size.toString()
        return number
    }

    override fun sortCurrentConnection(id: String, sortType: String): List<MainConnectionWithCompanyDTO>
    {
        val connectionState = rpc.proxy.vaultQuery(ConnectionState::class.java).states
        val myConnection =  connectionState.map { it }.filter { (it.state.data.companyId == id || it.state.data.requestCompanyId == id) && it.state.data.acceptedAt != null  }
        if (myConnection.isEmpty()) throw CordaException("You do not have any connections yet")

        val list = mutableListOf<MainConnectionWithCompanyDTO>()

        val participantState = rpc.proxy.vaultQuery(ParticipantState::class.java).states

        myConnection.map { connect ->
            if (connect.state.data.companyId == id){
                val myParticipants = participantState.find { it.state.data.linearId.toString() == connect.state.data.requestCompanyId }
                        ?:throw NotFoundException("Participant not found.")
                list.add(mapToMainConnectionWithCompanyDTO(connect.state.data, myParticipants.state.data))
            }
            else{
                val myParticipants = participantState.find { it.state.data.linearId.toString() == connect.state.data.companyId }
                        ?:throw NotFoundException("Participant not found.")
                list.add(mapToMainConnectionWithCompanyDTO(connect.state.data, myParticipants.state.data))}
        }
        println(sortType)
        return when {
            sortType.contains("AZ", true) -> list.sortedWith(compareBy { it.companyDetails.name })
            sortType.contains("ZA", true) -> list.sortedByDescending { it.companyDetails.name }
            sortType.contains("date", true) -> list.sortedWith(compareBy { it.acceptedAt })
            else -> list
        }
    }

    override fun sortAllRequestConnectionsFromOthers(id: String, sortType: String):  List<MainConnectionWithCompanyDTO>
    {
        val connectionState = rpc.proxy.vaultQuery(ConnectionState::class.java).states
        val myConnection =  connectionState.map { it }.filter { (it.state.data.requestCompanyId == id) && it.state.data.acceptedAt == null  }
        if (myConnection.isEmpty()) throw CordaException("You do not have any connections yet")

        val list = mutableListOf<MainConnectionWithCompanyDTO>()

        val participantState = rpc.proxy.vaultQuery(ParticipantState::class.java).states

        myConnection.map { connect ->
            if (connect.state.data.companyId == id){
                val myParticipants = participantState.find { it.state.data.linearId.toString() == connect.state.data.requestCompanyId }
                        ?:throw NotFoundException("Participant not found.")
                list.add(mapToMainConnectionWithCompanyDTO(connect.state.data, myParticipants.state.data))
            }
            else{
                val myParticipants = participantState.find { it.state.data.linearId.toString() == connect.state.data.companyId }
                        ?:throw NotFoundException("Participant not found.")
                list.add(mapToMainConnectionWithCompanyDTO(connect.state.data, myParticipants.state.data))}
        }
        println(sortType)
        return when {
            sortType.contains("AZ", true) -> list.sortedWith(compareBy { it.companyDetails.name })
            sortType.contains("ZA", true) -> list.sortedByDescending { it.companyDetails.name }
            sortType.contains("date", true) -> list.sortedWith(compareBy { it.acceptedAt })
            else -> list
        }
    }

    override fun sortAllMyConnectionRequests(id: String, sortType: String):  List<MainConnectionWithCompanyDTO>
    {
        val connectionState = rpc.proxy.vaultQuery(ConnectionState::class.java).states
        val myConnection =  connectionState.map { it }.filter { (it.state.data.companyId == id) && it.state.data.acceptedAt == null  }
        if (myConnection.isEmpty()) throw CordaException("You do not have any connections yet")

        val list = mutableListOf<MainConnectionWithCompanyDTO>()

        val participantState = rpc.proxy.vaultQuery(ParticipantState::class.java).states

        myConnection.map { connect ->
            if (connect.state.data.companyId == id){
                val myParticipants = participantState.find { it.state.data.linearId.toString() == connect.state.data.requestCompanyId }
                        ?:throw NotFoundException("Participant not found.")
                list.add(mapToMainConnectionWithCompanyDTO(connect.state.data, myParticipants.state.data))
            }
            else{
                val myParticipants = participantState.find { it.state.data.linearId.toString() == connect.state.data.companyId }
                        ?:throw NotFoundException("Participant not found.")
                list.add(mapToMainConnectionWithCompanyDTO(connect.state.data, myParticipants.state.data))}
        }
        println(sortType)
        return when {
            sortType.contains("AZ", true) -> list.sortedWith(compareBy { it.companyDetails.name })
            sortType.contains("ZA", true) -> list.sortedByDescending { it.companyDetails.name }
            sortType.contains("date", true) -> list.sortedWith(compareBy { it.acceptedAt })
            else -> list
        }
    }

    override fun getAConnection(id: String, idOfOther: String): MainParticipantDTO {
        val participantState = rpc.proxy.vaultQuery(ParticipantState::class.java).states
        val myParticipant =  participantState.map { it }.find { (it.state.data.linearId.toString() == idOfOther) }
                ?: throw CordaException("Company Not Found")
        return mapToMainParticipantDTO(myParticipant.state.data)
    }
}