package com.aerotrax.util

import com.aerotrax.dto.ResponseDTO
import com.aerotrax.webserver.NodeRPCConnection
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class Response {
    fun successfulResponse(response: Any, message: String): ResponseEntity<ResponseDTO> {
        return ResponseEntity.ok(
                ResponseDTO(
                        status = "success",
                        message = "$message successful",
                        result = response
                )
        )
    }

    fun failedResponse(exception: Exception, message: String): ResponseEntity<ResponseDTO> {
        return ResponseEntity.badRequest().body(
                ResponseDTO(
                        status = "failed",
                        message = "$message failed",
                        result = exception.localizedMessage.toString()
                )
        )
    }
}