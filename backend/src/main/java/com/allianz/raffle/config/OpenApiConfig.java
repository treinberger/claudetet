// File: /backend/src/main/java/com/allianz/raffle/config/OpenApiConfig.java

package com.allianz.raffle.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI raffleSystemOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Raffle System API")
                        .description("API for managing raffles, participants, and prizes")
                        .version("v1.0.0")
                        .license(new License().name("Private").url("https://allianz.com")));
    }
}