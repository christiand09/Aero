package com.aerotrax.services

import com.aerotrax.dto.MainProductDTO
import com.aerotrax.dto.mapToMainProductDTO
import com.aerotrax.services.interfaces.IProduct
import com.aerotrax.states.ProductState
import com.aerotrax.util.FlowHandlerCompletion
import com.aerotrax.webserver.NodeRPCConnection
import javassist.NotFoundException
import org.springframework.stereotype.Service


@Service
class ProductService (private val rpc: NodeRPCConnection, private val fhc: FlowHandlerCompletion): IProduct
{
    override fun getAllProduct(): List<MainProductDTO>
    {
        val productState = rpc.proxy.vaultQuery(ProductState::class.java).states
        return productState.map { mapToMainProductDTO(it.state.data) }
    }

    override fun getProduct(serialNumber: String): MainProductDTO
    {
        val productState = rpc.proxy.vaultQuery(ProductState::class.java).states
        val product = productState.find { it.state.data.serialNumber == serialNumber }?.state?.data ?: throw NotFoundException("Product Not Found")
        return mapToMainProductDTO(product)
    }



}