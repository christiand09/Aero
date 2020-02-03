package com.aerotrax.controller

import com.aerotrax.dto.ResponseDTO
import com.aerotrax.dto.arc.RegisterARCFlowDTO
import com.aerotrax.dto.product.RegisterProductFlowDTO
import com.aerotrax.service.ProductService
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private const val CONTROLLER_NAME = "/product"

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(CONTROLLER_NAME) /** The paths for HTTP requests are relative to this base path. **/
class ProductController (private val productService: ProductService) {

    /******************************************/
    /*****     Get All The Product      *******/
    /******************************************/
    @PostMapping(value = ["/all"], produces = ["application/json"])
    @ApiOperation(value = "Get All The Product")
    private fun getAllProduct(): ResponseEntity<ResponseDTO> {
        return productService.getAllProduct()
    }

    /****************************************/
    /*****     Get Data Product      *******/
    /***************************************/
    @PostMapping(value = ["/{serialNumber}"], produces = ["application/json"])
    @ApiOperation(value = "Get Data Product")
    private fun getProduct(@PathVariable serialNumber: String): ResponseEntity<ResponseDTO> {
        return productService.getProduct(serialNumber)
    }

    /****************************************/
    /*****     Create A Product      *******/
    /***************************************/
    @PostMapping(value = [""], produces = ["application/json"])
    @ApiOperation(value = "Create A Product")
    private fun createProduct(@RequestBody request: RegisterProductFlowDTO): ResponseEntity<ResponseDTO> {
        return productService.createProduct(request)
    }

    /************************************/
    /*****     Create A ARC      *******/
    /***********************************/
    @PostMapping(value = ["/arc"], produces = ["application/json"])
    @ApiOperation(value = "Create A ARC")
    private fun createARC(@RequestBody request: RegisterARCFlowDTO): ResponseEntity<ResponseDTO> {
        return productService.createARC(request)
    }

}