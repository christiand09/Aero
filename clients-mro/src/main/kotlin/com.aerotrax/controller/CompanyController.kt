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
     * Get all requesting companies
     */

    @GetMapping(value = ["/requests"], produces = ["application/json"])
    private fun getAllRequestingConnections(@RequestHeader id: String): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response = companyService.getAllRequestingConnections(id)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "companyService.getAllRequestingConnections()"
            return this.handleException(e, identity, function)
        }
    }

    /**
     * Get all connections
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
            val function = "companyService.getAllConnections()"
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

    @PatchMapping(value = ["/connect"], produces = ["application/json"])
    private fun approveRejectConnection(@RequestHeader id: String, @RequestBody request: ApproveRejectConnectionDTO): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response =  companyService.approveRejectConnection(id, request)
            when {
                response != null -> ResponseEntity.ok(ResponseDTO(
                        message = "Success",
                        result = response
                ))
                else -> throw NotFoundException("Connection not found")
            }
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
    private fun requestConnection(@RequestHeader ownCompanyId: String, @RequestBody request: RequestConnectionDTO): ResponseEntity<ResponseDTO>
    {
        return try
        {
            val response =  companyService.requestConnection(ownCompanyId, request)
            when {
                response != null -> ResponseEntity.ok(ResponseDTO(
                        message = "Success",
                        result = response
                ))
                else -> throw NotFoundException("Connection not found")
            }
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "companyService.requestConnection()"
            return this.handleException(e, identity, function)
        }
    }

}