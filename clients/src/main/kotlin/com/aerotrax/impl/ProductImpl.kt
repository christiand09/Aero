package com.aerotrax.impl

import com.aerotrax.dto.ResponseDTO
import com.aerotrax.dto.arc.RegisterARCFlowDTO
import com.aerotrax.dto.arc.mapToMainARCDTO
import com.aerotrax.dto.product.RegisterProductFlowDTO
import com.aerotrax.dto.product.mapToMainProductDTO
import com.aerotrax.flows.product.*
import com.aerotrax.service.ProductService
import com.aerotrax.util.Functions
import com.aerotrax.util.HandlerCompletion
import com.aerotrax.util.Response
import com.aerotrax.webserver.NodeRPCConnection
import javassist.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException

@Service
class ProductImpl (private val rpc: NodeRPCConnection,
                   private val fhc: HandlerCompletion,
                   private val function: Functions,
                   private val response: Response): ProductService {

    companion object {
        private val logger = LoggerFactory.getLogger(RestController::class.java)!!
    }

    override fun getAllProduct(): ResponseEntity<ResponseDTO> {

        return try {

            val productState = function.productState()
            response.successfulResponse(
                    response = productState.map { mapToMainProductDTO(it.state.data) },
                    message = "get all the product"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "get all the product"
            )
        }
    }

    override fun getProduct(serialNumber: String): ResponseEntity<ResponseDTO> {

        return try {

            val productState = function.productState()
            val product = productState.find {
                it.state.data.serialNumber == serialNumber
            }?: throw NotFoundException("Product Not Found")

            response.successfulResponse(
                    response = mapToMainProductDTO(product.state.data),
                    message = "get a product by serial number: $serialNumber"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "get a product by serial number: $serialNumber"
            )
        }
    }

    override fun createProduct(request: RegisterProductFlowDTO): ResponseEntity<ResponseDTO> {

        return try {

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
            fhc.handlerCompletion(registerProductFlowReturn)
            val productState = function.returnProductState(registerProductFlowReturn)

            request.partDetails.map {detail ->
                val registerPartDetailFlowReturn = rpc.proxy.startFlowDynamic(
                        RegisterPartDetailFlow::class.java,
                        productState.linearId.toString(),
                        request.createdBy,
                        detail.title,
                        detail.value,
                        detail.unit
                )
                fhc.handlerCompletion(registerPartDetailFlowReturn)
            }

            request.CMMTitle.map {title ->
                val registerCMMFlowReturn = rpc.proxy.startFlowDynamic(
                        RegisterCMMFlow::class.java,
                        productState.linearId.toString(),
                        title,
                        request.createdBy
                )
                fhc.handlerCompletion(registerCMMFlowReturn)
            }

            val generateARCFlowReturn = rpc.proxy.startFlowDynamic(
                    GenerateARCFlow::class.java,
                    request.companyId,
                    productState.linearId.toString()
            )
            fhc.handlerCompletion(generateARCFlowReturn)

            response.successfulResponse(
                    response = mapToMainProductDTO(productState),
                    message = "create a product"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "create a product"
            )
        }
    }

    override fun createARC(request: RegisterARCFlowDTO): ResponseEntity<ResponseDTO> {

        return try {

            val registerARCFlowReturn = rpc.proxy.startFlowDynamic(
                    RegisterARCFlow::class.java,
                    request.arcId,
                    request.remarks,
                    request.approvedReject,
                    request.createdBy
            )
            fhc.handlerCompletion(registerARCFlowReturn)
            val arcState = function.returnARCState(registerARCFlowReturn)

            response.successfulResponse(
                    response = mapToMainARCDTO(arcState),
                    message = "create a ARC"
            )
        } catch (ex: Exception){
            response.failedResponse(
                    exception = ex,
                    message = "create a ARC"
            )
        }
    }
}