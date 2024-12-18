package com.vsantos.springtechevents;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI().info(new Info()
        .title("Spring Tech Events API")
        .description(
            "Spring Tech Events API implemented with Spring Boot RESTful service and documented using springdoc-openapi and Swagger UI")
        .version("1.0"));
  }

}
