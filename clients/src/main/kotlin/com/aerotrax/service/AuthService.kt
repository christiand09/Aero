package com.aerotrax.service

import com.aerotrax.dto.ResponseDTO
import org.springframework.http.ResponseEntity

interface AuthService {

    /** seed data **/
    fun seedCompany(name: String): ResponseEntity<ResponseDTO>

}