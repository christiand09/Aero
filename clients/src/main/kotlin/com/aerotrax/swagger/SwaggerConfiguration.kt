package com.aerotrax.swagger

import com.google.common.base.Predicates
import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.ArrayList

@Configuration
@EnableSwagger2
open class SwaggerConfiguration {
    @Bean
    open fun api(): Docket {
        val schemeList = ArrayList<SecurityScheme>()
        (schemeList).add(ApiKey(HttpHeaders.AUTHORIZATION, "Authorization", "header"))
        return Docket(DocumentationType.SWAGGER_2)
                .produces(setOf("application/json"))
                .consumes(setOf("application/json"))
                .ignoredParameterTypes(Authentication::class.java)
                .securitySchemes(schemeList)
                .useDefaultResponseMessages(false)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .paths(PathSelectors.any())
                .build()
    }

}

