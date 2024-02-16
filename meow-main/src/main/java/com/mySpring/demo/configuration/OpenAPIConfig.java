package com.mySpring.demo.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Test")
                        .description("Swagger3")
                        .version("v1"))
                .externalDocs(new ExternalDocumentation()
                        .description("API doc")
                        .url("/api-docs"));
    }
}
