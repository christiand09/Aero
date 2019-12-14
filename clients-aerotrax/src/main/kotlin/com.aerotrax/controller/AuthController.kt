package com.aerotrax.controller

import com.aerotrax.dto.RegisterProductFlowDTO
import com.aerotrax.dto.ResponseDTO
import com.aerotrax.services.AuthService
import com.aerotrax.services.ProductService
import com.aerotrax.webserver.NodeRPCConnection
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private const val CONTROLLER_NAME = "api/v1/auth"

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(CONTROLLER_NAME) // The paths for HTTP requests are relative to this base path.
class AuthController(private val authService: AuthService, private val rpcConnection: NodeRPCConnection) : BaseController() {


    @PostMapping(value = [], produces = ["application/json"])
    private fun seedCompany(): ResponseEntity<ResponseDTO> {
        return try {
            val response = authService.seedCompany()
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "authService.seedCompany()"
            return this.handleException(e, identity, function)
        }
    }


}