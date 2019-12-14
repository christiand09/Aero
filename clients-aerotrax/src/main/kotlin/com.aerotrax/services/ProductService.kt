package com.aerotrax.services

import com.aerotrax.dto.MainProductDTO
import com.aerotrax.dto.RegisterProductFlowDTO
import com.aerotrax.dto.mapToMainProductDTO
import com.aerotrax.flows.product.RegisterCMMFlow
import com.aerotrax.flows.product.RegisterPartDetailFlow
import com.aerotrax.flows.product.RegisterProductFlow
import com.aerotrax.services.interfaces.IProduct
import com.aerotrax.util.AppConstants
import com.aerotrax.util.FlowHandlerCompletion
import com.aerotrax.util.Functions
import com.aerotrax.webserver.NodeRPCConnection
import javassist.NotFoundException
import org.springframework.stereotype.Service


@Service
class ProductService (private val rpc: NodeRPCConnection, private val fhc: FlowHandlerCompletion, private val state: Functions): IProduct {

    override var currentCompanyId: String = AppConstants.DEFAULT_COMPANY_ID

    override fun setCurrentCompanyDetails(userId: String?) {
        currentCompanyId = userId ?: currentCompanyId
    }

    override fun get(linearId: String): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllProduct(): List<MainProductDTO> {
        val productState = state.productState()
        return productState.map { mapToMainProductDTO(it.state.data) }
    }

    override fun getProduct(serialNumber: String): MainProductDTO {
        val productState = state.productState()
        val product = productState.find { it.state.data.serialNumber == serialNumber }?: throw NotFoundException("Product Not Found")
        return mapToMainProductDTO(product.state.data)
    }

    override fun createProduct(request: RegisterProductFlowDTO): MainProductDTO {

        val registerProductFlowReturn = rpc.proxy.startFlowDynamic(
                RegisterProductFlow::class.java,
                request.companyId,
                request.partName,
                request.partImage,
                request.partNumber,
                request.serialNumber,
                request.status,
                request.category,
                request.manufacturer,
                request.createdBy
        )
        fhc.flowHandlerCompletion(registerProductFlowReturn)
        val productState = state.returnProductState(registerProductFlowReturn)

        request.partDetails.map {detail ->
            val registerPartDetailFlowReturn = rpc.proxy.startFlowDynamic(
                    RegisterPartDetailFlow::class.java,
                    productState.linearId.toString(),
                    request.createdBy,
                    detail.title,
                    detail.value,
                    detail.unit
            )
            fhc.flowHandlerCompletion(registerPartDetailFlowReturn)
        }

        request.CMMTitle.map {title ->
            val registerCMMFlowReturn = rpc.proxy.startFlowDynamic(
                    RegisterCMMFlow::class.java,
                    productState.linearId.toString(),
                    title,
                    request.createdBy
            )
            fhc.flowHandlerCompletion(registerCMMFlowReturn)
        }

        return mapToMainProductDTO(productState)
    }
}