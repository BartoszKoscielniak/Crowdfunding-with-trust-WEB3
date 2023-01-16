package com.crowdfunding.crowdfundingapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

import static org.springframework.security.config.Elements.JWT;

@Configuration
@SecurityScheme(
        name = "Bearer Auth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = JWT
)
@OpenAPIDefinition(
        info = @Info(title = "Sample API", version = "v1"),
        security = @SecurityRequirement(name = "Bearer Auth")
)
class OpenApiConfig {

}