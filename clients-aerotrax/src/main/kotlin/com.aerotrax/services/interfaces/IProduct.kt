package com.aerotrax.services.interfaces

import com.aerotrax.dto.MainProductDTO


interface IProduct
{
    fun getAllProduct(): List<MainProductDTO>
    fun getProduct(serialNumber: String): MainProductDTO
//    fun createProduct(companyLinearID: String, request: registerProductDTO): mainProductDTO

}