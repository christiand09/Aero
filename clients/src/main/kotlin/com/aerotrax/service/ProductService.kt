package com.aerotrax.service

import com.aerotrax.dto.ResponseDTO
import com.aerotrax.dto.arc.RegisterARCFlowDTO
import com.aerotrax.dto.product.RegisterProductFlowDTO
import org.springframework.http.ResponseEntity

interface ProductService {

    /** get all the list of the product **/
    fun getAllProduct(): ResponseEntity<ResponseDTO>

    /** get product by serialNumber **/
    fun getProduct(serialNumber: String): ResponseEntity<ResponseDTO>

    /** create a product **/
    fun createProduct(request: RegisterProductFlowDTO): ResponseEntity<ResponseDTO>

    /** create a arc **/
    fun createARC(request: RegisterARCFlowDTO): ResponseEntity<ResponseDTO>

}