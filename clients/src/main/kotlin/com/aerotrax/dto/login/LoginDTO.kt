package com.aerotrax.dto.login

import com.fasterxml.jackson.annotation.JsonCreator

data class LoginDTO @JsonCreator constructor(
        val userName: String,
        val password: String
)