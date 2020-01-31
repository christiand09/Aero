package com.aerotrax.controller

import com.aerotrax.dto.ResponseDTO
import com.aerotrax.dto.company.RegisterCompanyDTO
import com.aerotrax.dto.connection.ApproveRejectConnectionDTO
import com.aerotrax.dto.connection.RequestConnectionDTO
import com.aerotrax.dto.login.LoginDTO
import com.aerotrax.service.CompanyService
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private const val CONTROLLER_NAME = "/company"

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(CONTROLLER_NAME) /** The paths for HTTP requests are relative to this base path. **/
class CompanyController (private val companyService: CompanyService) {

    companion object {
        private val logger = LoggerFactory.getLogger(RestController::class.java)
    }

    /*************************************/
    /*****     Company Login      *******/
    /************************************/
    @PostMapping(value = ["/login"], produces = ["application/json"])
    @ApiOperation(value = "Company login")
    private fun login(@RequestBody request: LoginDTO): ResponseEntity<ResponseDTO> {
        return companyService.login(request)
    }

    /******************************************/
    /*****     Get All The Company      *******/
    /******************************************/
    @GetMapping(value = ["/all"], produces = ["application/json"])
    @ApiOperation(value = "Get All The Company")
    private fun getAllCompany(): ResponseEntity<ResponseDTO> {
        return companyService.getAllCompany()
    }

    /************************************/
    /*****     Get A Company      *******/
    /************************************/
    @GetMapping(value = [""], produces = ["application/json"])
    @ApiOperation(value = "Get A Company")
    private fun getACompany(@RequestParam id: String): ResponseEntity<ResponseDTO> {
        return companyService.getACompany(id)
    }

    /**************************************/
    /*****     Create A Company      ******/
    /**************************************/
    @PostMapping(value = ["/create"], produces = ["application/json"])
    @ApiOperation(value = "Create A Company")
    private fun createCompany(@RequestBody request: RegisterCompanyDTO): ResponseEntity<ResponseDTO> {
        return companyService.createCompany(request)
    }

    /******************************************/
    /*****     Request A Connection      ******/
    /******************************************/
    @PostMapping(value = ["/connection/request"], produces = ["application/json"])
    @ApiOperation(value = "Request A Connection")
    private fun requestConnection(@RequestParam id: String, @RequestBody request: RequestConnectionDTO): ResponseEntity<ResponseDTO> {
        return companyService.requestConnection(id, request)
    }

    /*****************************************/
    /*****     Response Connection      ******/
    /*****************************************/
    @PostMapping(value = ["/connection/response"], produces = ["application/json"])
    @ApiOperation(value = "Response Connection")
    private fun approveRejectConnection(@RequestParam id: String, @RequestBody request: ApproveRejectConnectionDTO): ResponseEntity<ResponseDTO> {
        return companyService.approveRejectConnection(id, request)
    }

    /*****************************************/
    /*****     Response Connection      ******/
    /*****************************************/
    @PostMapping(value = ["/connection/all"], produces = ["application/json"])
    @ApiOperation(value = "Response Connection")
    private fun searchAllAvailableConnections(@RequestParam searchText: String?): ResponseEntity<ResponseDTO> {
        return companyService.searchAllAvailableConnections(searchText)
    }

    /**********************************************/
    /*****     Get All The Participants      ******/
    /**********************************************/
    @GetMapping(value = ["/participant/all"], produces = ["application/json"])
    @ApiOperation(value = "Get All The Participants")
    private fun getAllParticipants(): ResponseEntity<ResponseDTO> {
        return companyService.getAllParticipants()
    }

    /*********************************************/
    /*****     Get All The Connections      ******/
    /*********************************************/
    @GetMapping(value = ["/connection/all"], produces = ["application/json"])
    @ApiOperation(value = "Get All The Connections")
    private fun getAllConnections(@RequestParam id: String): ResponseEntity<ResponseDTO> {
        return companyService.getAllConnections(id)
    }

    /*****************************************************/
    /*****     Get All The Request Connections      ******/
    /*****************************************************/
    @GetMapping(value = ["/connection/request"], produces = ["application/json"])
    @ApiOperation(value = "Get All The Request Connections")
    private fun getAllRequestConnectionsFromOthers(@RequestParam id: String): ResponseEntity<ResponseDTO> {
        return companyService.getAllRequestConnectionsFromOthers(id)
    }

    /****************************************************/
    /*****     Get All My Request Connections      ******/
    /****************************************************/
    @GetMapping(value = ["/connection/request/all"], produces = ["application/json"])
    @ApiOperation(value = "Get All My Request Connections")
    private fun getAllMyConnectionRequests(@RequestParam id: String): ResponseEntity<ResponseDTO> {
        return companyService.getAllMyConnectionRequests(id)
    }

    /**********************************************************/
    /*****     Get Number Of My Current Connections      ******/
    /**********************************************************/
    @GetMapping(value = ["/connection/number"], produces = ["application/json"])
    @ApiOperation(value = "Get Number Of My Current Connections")
    private fun getNumberCurrentConnections(@RequestParam id: String): ResponseEntity<ResponseDTO> {
        return companyService.getNumberCurrentConnections(id)
    }

    /**********************************************************/
    /*****     Get Number Of My Request Connections      ******/
    /**********************************************************/
    @GetMapping(value = ["/connection/number/request"], produces = ["application/json"])
    @ApiOperation(value = "Get Number Of My Request Connections")
    private fun getNumberRequestConnections(@RequestParam id: String): ResponseEntity<ResponseDTO> {
        return companyService.getNumberRequestConnections(id)
    }

    /**********************************************/
    /*****     Sort Current Connections      ******/
    /**********************************************/
    @GetMapping(value = ["/connection/sort/current"], produces = ["application/json"])
    @ApiOperation(value = "Sort Current Connections")
    private fun sortCurrentConnection(@RequestParam id: String, @RequestParam sortType: String): ResponseEntity<ResponseDTO> {
        return companyService.sortCurrentConnection(id, sortType)
    }

    /**************************************************/
    /*****     Sort All Request Connections      ******/
    /****************************************8*********/
    @GetMapping(value = ["/connection/sort/request"], produces = ["application/json"])
    @ApiOperation(value = "Sort All Request Connections")
    private fun sortAllRequestConnectionsFromOthers(@RequestParam id: String, @RequestParam sortType: String): ResponseEntity<ResponseDTO> {
        return companyService.sortAllRequestConnectionsFromOthers(id, sortType)
    }

    /*****************************************************/
    /*****     Sort All My Request Connections      ******/
    /*****************************************************/
    @GetMapping(value = ["/connection/sort/myRequest"], produces = ["application/json"])
    @ApiOperation(value = "Sort All My Request Connections")
    private fun sortAllMyConnectionRequests(@RequestParam id: String, @RequestParam sortType: String): ResponseEntity<ResponseDTO> {
        return companyService.sortAllMyConnectionRequests(id, sortType)
    }

    /************************************************************/
    /*****     Get A Company Data Through Participant      ******/
    /************************************************************/
    @GetMapping(value = ["/participant/data"], produces = ["application/json"])
    @ApiOperation(value = "Get A Company Data Through Participant")
    private fun getAConnection(@RequestParam id: String, @RequestParam idOfOther: String): ResponseEntity<ResponseDTO> {
        return companyService.getAConnection(id, idOfOther)
    }

}