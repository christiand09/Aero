package com.aerotrax.util

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WhitelistErrorHandler {
    @GetMapping(value = ["/**/{path:[^\\.]*}"])
    fun forward(): String {
        return "forward:/"
    }
}
