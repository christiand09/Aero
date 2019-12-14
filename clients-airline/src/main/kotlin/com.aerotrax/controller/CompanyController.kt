package com.aerotrax.controller

import com.aerotrax.dto.ResponseDTO
import com.aerotrax.dto.ApproveRejectConnectionDTO
import com.aerotrax.dto.RegisterCompanyDTO
import com.aerotrax.dto.RequestConnectionDTO
import com.aerotrax.services.CompanyService
import com.aerotrax.webserver.NodeRPCConnection
import javassist.NotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private const val CONTROLLER_NAME = "api/v1/company"

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(CONTROLLER_NAME) // The paths for HTTP requests are relative to this base path.
class CompanyController(private val companyService: CompanyService, private val rpcConnection: NodeRPCConnection) : BaseController()
{
    /**
     * Get all company
     */

    @GetMapping(value = ["/all"], produces = ["application/json"])
    private fun getAllCompany(): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response = companyService.getAllCompany()
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "CompanyService.getAllCompany()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Get a specific company
     */

    @GetMapping(value = ["/{id}"], produces = ["application/json"])
    private fun getACompany(@RequestBody id: String): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response = companyService.getACompany(id)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "companyService.getACompany()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Search all available connections
     */

    @GetMapping(value = ["/search"], produces = ["application/json"])
    private fun searchAllAvailableConnections(@RequestParam searchText: String?): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response = companyService.searchAllAvailableConnections(searchText)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "companyService.searchAllAvailableConnections()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Create Company
     */

    @PostMapping(value = ["/create"], produces = ["application/json"])
    private fun createCompany(@RequestBody request: RegisterCompanyDTO): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response =  companyService.createCompany(request)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "companyService.createCompany()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Approve/Reject Connection
     */

    @PatchMapping(value = ["/connect/{id}"], produces = ["application/json"])
    private fun approveRejectConnection(@PathVariable id: String, @RequestBody request: ApproveRejectConnectionDTO): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response =  companyService.approveRejectConnection(id, request)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "companyService.approveRejectConnection()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Request Connection
     */

    @PostMapping(value = ["/request"], produces = ["application/json"])
    private fun requestConnection(@RequestHeader id: String, @RequestBody request: RequestConnectionDTO): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response =  companyService.requestConnection(id, request)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "companyService.requestConnection()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Get all participants
     */

    @GetMapping(value = ["/participants/all"], produces = ["application/json"])
    private fun getAllParticipants(): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response = companyService.getAllParticipants()
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "CompanyService.getAllParticipants()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Get all my connections
     */

    @GetMapping(value = ["/connections"], produces = ["application/json"])
    private fun getAllConnections(@RequestHeader id: String): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response = companyService.getAllConnections(id)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "CompanyService.getAllConnections()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Get all the connections requested by other party
     */

    @GetMapping(value = ["/connections/requests"], produces = ["application/json"])
    private fun getAllRequestConnectionsFromOthers(@RequestHeader id: String): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response = companyService.getAllRequestConnectionsFromOthers(id)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "CompanyService.getAllRequestConnectionsFromOthers()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Get all my request of connection to other party
     */

    @GetMapping(value = ["/connections/myRequests"], produces = ["application/json"])
    private fun getAllMyConnectionRequests(@RequestHeader id: String): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response = companyService.getAllMyConnectionRequests(id)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "CompanyService.getAllMyConnectionRequests()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Number of my Connections
     */

    @GetMapping(value = ["/connections/number/all"], produces = ["application/json"])
    private fun getNumberCurrentConnections(@RequestHeader id: String): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response = companyService.getNumberCurrentConnections(id)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "companyService.getNumberCurrentConnections()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Number of request of connection made by other party
     */

    @GetMapping(value = ["/connections/number/request"], produces = ["application/json"])
    private fun getNumberRequestConnections(@RequestHeader id: String): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response = companyService.getNumberRequestConnections(id)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "companyService.getNumberRequestConnections()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Sort all my current connection by sortType
     */

    @GetMapping(value = ["/connections/sorted/{id}"], produces = ["application/json"])
    private fun sortCurrentConnection(@RequestHeader id: String, @RequestBody sortType: String): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response = companyService.sortCurrentConnection(id, sortType)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "companyService.sortCurrentConnection()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Sort all pending connection request made by other company by sortType
     */

    @GetMapping(value = ["/connections/requests/sorted/{id}"], produces = ["application/json"])
    private fun sortAllRequestConnectionsFromOthers(@RequestHeader id: String, @RequestBody sortType: String): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response = companyService.sortAllRequestConnectionsFromOthers(id, sortType)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "companyService.sortAllRequestConnectionsFromOthers()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Sort all my connection request to other company by sortType
     */

    @GetMapping(value = ["/connections/myRequests/sorted/{id}"], produces = ["application/json"])
    private fun sortAllMyConnectionRequests(@RequestHeader id: String, @RequestBody sortType: String): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response = companyService.sortAllMyConnectionRequests(id, sortType)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "companyService.sortAllMyConnectionRequests()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Sort all my connection request to other company by sortType
     */

    @GetMapping(value = ["/connections/{idOfOther}"], produces = ["application/json"])
    private fun getAConnection(@RequestHeader id: String, @RequestBody idOfOther: String): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response = companyService.getAConnection(id, idOfOther)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "companyService.getAConnection()"
            return this.handleException(e, identity, function)
        }
    }
}