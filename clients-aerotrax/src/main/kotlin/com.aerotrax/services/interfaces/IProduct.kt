package com.aerotrax.services.interfaces

import com.aerotrax.dto.MainARCDTO
import com.aerotrax.dto.MainProductDTO
import com.aerotrax.dto.RegisterARCFlowDTO
import com.aerotrax.dto.RegisterProductFlowDTO


interface IProduct: IService {
    fun getAllProduct(): List<MainProductDTO>
    fun getProduct(serialNumber: String): MainProductDTO
    fun createProduct(request: RegisterProductFlowDTO): MainProductDTO

    //ARC
    fun createARC(request: RegisterARCFlowDTO): MainARCDTO
}