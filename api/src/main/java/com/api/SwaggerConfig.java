package com.api;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;


@SecurityScheme(
		name ="accessToken", type = SecuritySchemeType.HTTP, bearerFormat = "jwt", scheme ="Bearer")
@OpenAPIDefinition(
        info = @Info(title = "Szs Api",version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

  
}
