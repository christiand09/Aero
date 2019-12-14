package com.aerotrax.controller

import com.aerotrax.dto.ResponseDTO
import javassist.NotFoundException
import net.corda.core.contracts.TransactionVerificationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.io.PrintWriter
import java.io.StringWriter
import java.time.Instant
import java.util.*
import javax.validation.ValidationException

abstract class BaseController
{
    companion object
    {
        private val logger = LoggerFactory.getLogger(BaseController::class.java)
    }

    fun getUserUID(headers: HttpHeaders): String? {
        return headers.getFirst("user-uid")
    }


    fun handleException(ex: Exception, identity: String, function: String) : ResponseEntity<ResponseDTO> {
        val sw = StringWriter()
        ex.printStackTrace(PrintWriter(sw))
        val exceptionAsString = sw.toString()
        logger.info("########################################################")
        logger.info("Date & Time: ${Date.from(Instant.now())}")
        logger.info("Function Used: $function")
        logger.info("Name: $identity")
        logger.info("Exception Errors: ${ex.localizedMessage}")
        logger.info("Exception Stacktrace: $exceptionAsString")
        return when (ex) {
//            is UnauthorizedException -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDTO(
//                    message = "Unauthorized",
//                    result = ex.localizedMessage.toString()
//            ))
            is NotFoundException -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDTO(
                    message = "Not Found",
                    result = ex.localizedMessage.toString()
            ))
            is TransactionVerificationException -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO(
                    message = "Failed",
                    result = ex.localizedMessage.toString()
            ))
//            is MailerException -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO(
//                    message = "Failed",
//                    result = "Mailer failed to send"
//            ))
            is ValidationException -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO(
                    message = "Validation Error",
                    result = ex.localizedMessage.toString()
            ))
            else -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO(
                    message = "Failed",
                    result = ex.localizedMessage.toString()
            ))
        }
    }

    fun handleExceptionRender(ex: Exception) : ResponseEntity<Any> {
        logger.error(ex.printStackTrace().toString())
        return when (ex) {
//            is UnauthorizedException -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.message.toString())
            is NotFoundException -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message.toString())
            is TransactionVerificationException -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.localizedMessage.toString())
//            is MailerException -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Mailer failed to send, error: %s", ex.message.toString()))
            else -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Action has failed, error: %s", ex.message.toString()))
        }
    }
}

