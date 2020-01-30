package com.aerotrax.service

import com.aerotrax.dto.ResponseDTO
import com.aerotrax.dto.company.RegisterCompanyDTO
import com.aerotrax.dto.connection.ApproveRejectConnectionDTO
import com.aerotrax.dto.connection.RequestConnectionDTO
import com.aerotrax.dto.login.LoginDTO
import org.springframework.http.ResponseEntity

interface CompanyService {

    /** login **/
    fun login(request: LoginDTO): ResponseEntity<ResponseDTO>

    /** get all registered company per node **/
    fun getAllCompany(): ResponseEntity<ResponseDTO>

    /** get a company per node **/
    fun getACompany(id: String): ResponseEntity<ResponseDTO>

    /** register a company on a node **/
    fun createCompany(request: RegisterCompanyDTO): ResponseEntity<ResponseDTO>

    /** request a connection to a company **/
    fun requestConnection(id: String, request: RequestConnectionDTO): ResponseEntity<ResponseDTO>

    /** approve/reject a pending connection request of a company **/
    fun approveRejectConnection(id: String, request: ApproveRejectConnectionDTO): ResponseEntity<ResponseDTO>

    /** search all connections **/
    fun searchAllAvailableConnections(searchText: String?): ResponseEntity<ResponseDTO>

    /** get all registered companies **/
    fun getAllParticipants(): ResponseEntity<ResponseDTO>

    /** get all my current connections **/
    fun getAllConnections(id:String): ResponseEntity<ResponseDTO>

    /** get all pending connection request made by other company **/
    fun getAllRequestConnectionsFromOthers(id:String): ResponseEntity<ResponseDTO>

    /** get all my connection request to other company **/
    fun getAllMyConnectionRequests(id:String): ResponseEntity<ResponseDTO>

    /** get number of my current connection **/
    fun getNumberCurrentConnections(id:String): ResponseEntity<ResponseDTO>

    /** get number of all pending connection request made by other company **/
    fun getNumberRequestConnections(id:String): ResponseEntity<ResponseDTO>

    /** sort all my current connection by sortType **/
    fun sortCurrentConnection(id: String, sortType: String):  ResponseEntity<ResponseDTO>

    /** sort all pending connection request made by other company by sortType **/
    fun sortAllRequestConnectionsFromOthers(id: String, sortType: String):  ResponseEntity<ResponseDTO>

    /** sort all my connection request to other company by sortType **/
    fun sortAllMyConnectionRequests(id: String, sortType: String): ResponseEntity<ResponseDTO>

    /** get a company's details thru Participant **/
    fun getAConnection(id: String, idOfOther: String): ResponseEntity<ResponseDTO>

}