package com.aerotrax.controller

import com.aerotrax.dto.RegisterProductFlowDTO
import com.aerotrax.dto.ResponseDTO
import com.aerotrax.services.CompanyService
import com.aerotrax.services.ProductService
import com.aerotrax.webserver.NodeRPCConnection
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private const val CONTROLLER_NAME = "api/v1/product"

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(CONTROLLER_NAME) // The paths for HTTP requests are relative to this base path.
class ProductController(private val productService: ProductService, private val rpcConnection: NodeRPCConnection) : BaseController() {

    @GetMapping(value = [], produces = ["application/json"])
    private fun getAll(): ResponseEntity<ResponseDTO>
    {
        return try {
            val response = productService.getAllProduct()
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "getAll"
            return this.handleException(e, identity, function)
        }
    }

    @PostMapping(value = ["/product"], produces = ["application/json"])
    private fun createProduct(@RequestHeader headers: HttpHeaders, @RequestBody request: RegisterProductFlowDTO): ResponseEntity<ResponseDTO> {
        return try {
            productService.setCurrentCompanyDetails(getUserUID(headers))
            val response = productService.createProduct(request)
            ResponseEntity.ok(ResponseDTO(
                    message = "Success",
                    result = response
            ))
        } catch (e: Exception) {
            val identity = rpcConnection.proxy.nodeInfo().legalIdentities.toString()
            val function = "createProduct"
            return this.handleException(e, identity, function)
        }
    }


}