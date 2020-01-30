package com.aerotrax.controller

import com.aerotrax.dto.ResponseDTO
import com.aerotrax.service.AuthService
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private const val CONTROLLER_NAME = "/auth"

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(CONTROLLER_NAME) /** The paths for HTTP requests are relative to this base path. **/
class AuthController (private val authService: AuthService) {

    companion object {
        private val logger = LoggerFactory.getLogger(RestController::class.java)
    }

    /*****************************************/
    /*****     Seed Company Data      *******/
    /****************************************/
    @PostMapping(value = ["/seed"], produces = ["application/json"])
    @ApiOperation(value = "Seed Company Data")
    private fun seedCompany(): ResponseEntity<ResponseDTO> {
        return authService.seedCompany()
    }

}
