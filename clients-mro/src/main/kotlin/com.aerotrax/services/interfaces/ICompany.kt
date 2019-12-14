package com.aerotrax.services.interfaces

import com.aerotrax.dto.*

interface ICompany
{
    fun getAllCompany(): List<MainCompanyDTO>
    fun getACompany(id: String): MainCompanyDTO
    fun createCompany(request: RegisterCompanyDTO): MainCompanyDTO
    fun requestConnection(id: String, request: RequestConnectionDTO): MainConnectionDTO
    fun approveRejectConnection(id: String, request: ApproveRejectConnectionDTO): MainConnectionDTO
    fun searchAllConnections(searchText: String?): List<MainCompanyDTO>
    fun getAllParticipants(): List<MainParticipantDTO>
    fun getAllConnections(id:String): MutableList<MainConnectionWithCompanyDTO>
    fun getAllRequestConnectionsFromOthers(id:String): MutableList<MainConnectionWithCompanyDTO>  // list of all the connections requested by other party
    fun getAllMyConnectionRequests(id:String): MutableList<MainConnectionWithCompanyDTO> // list of all my request of connection to other party
//    fun getAllRequestingConnections(id:String): List<MainCompanyDTO>
//    fun getCurrentByFaveConnections(): List<MainCompanyDTO>
}