package com.aerotrax.services.interfaces

import com.aerotrax.dto.*
import com.aerotrax.states.ParticipantState

interface ICompany
{
//    var currentUserId: String
//    fun setCurrentUserDetails(userId: String?)

    fun getAllCompany(): List<MainCompanyDTO> // get all registered company per node
    fun getACompany(id: String): MainCompanyDTO // get a company per node
    fun createCompany(request: RegisterCompanyDTO): MainCompanyDTO // register a company on a node
    fun requestConnection(id: String, request: RequestConnectionDTO): MainConnectionDTO // request a connection to a company
    fun approveRejectConnection(id: String, request: ApproveRejectConnectionDTO): MainConnectionDTO // approve/reject a pending connection request of a company
    fun searchAllAvailableConnections(searchText: String?): List<MainParticipantDTO> // search all connections
    fun getAllParticipants(): List<MainParticipantDTO> // get all registered companies
    fun getAllConnections(id:String): MutableList<MainConnectionWithCompanyDTO> // get all my current connections
    fun getAllRequestConnectionsFromOthers(id:String): MutableList<MainConnectionWithCompanyDTO>  // get all pending connection request made by other company
    fun getAllMyConnectionRequests(id:String): MutableList<MainConnectionWithCompanyDTO> // get all my connection request to other company
    fun getNumberCurrentConnections(id:String): String // get number of my current connection
    fun getNumberRequestConnections(id:String): String // get number of all pending connection request made by other company
    fun sortCurrentConnection(id: String, sortType: String):  List<MainConnectionWithCompanyDTO> // sort all my current connection by sortType
    fun sortAllRequestConnectionsFromOthers(id: String, sortType: String):  List<MainConnectionWithCompanyDTO> // sort all pending connection request made by other company by sortType
    fun sortAllMyConnectionRequests(id: String, sortType: String):  List<MainConnectionWithCompanyDTO> // sort all my connection request to other company by sortType
    fun getAConnection(id: String, idOfOther: String): MainParticipantDTO // get a company's details thru Participant
//    fun sortCurrentConnection(id: String, sortType: String): List<MainParticipantDTO>
//    fun sortCurrentConnection(id: String, sortType: String): List<MainParticipantDTO>

//    fun getAllRequestingConnections(id:String): List<MainCompanyDTO>
//    fun getCurrentByFaveConnections(): List<MainCompanyDTO>
}