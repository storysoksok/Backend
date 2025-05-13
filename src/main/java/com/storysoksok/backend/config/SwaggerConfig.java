package com.storysoksok.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "🎫  : STORY-SOKSOK 🎫",
                description = """
                        ### 💻 **GitHub 저장소**
                        - **[백엔드 소스코드](https://github.com/storysoksok/Backend)**
                        """,
                version = "1.0v"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "로컬 서버"),
                @Server(url = "http://13.209.45.179:8080", description = "배포 서버")
        }
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme apiKey = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .scheme("bearer")
                .bearerFormat("JWT");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Token");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
                .addSecurityItem(securityRequirement)
                .servers(List.of(
                                new io.swagger.v3.oas.models.servers.Server()
                                        .url("http://localhost:8080")
                                        .description("로컬 서버"),
                                new io.swagger.v3.oas.models.servers.Server()
                                        .url("https://story-sok-sok.kro.kr")
                                        .description("배포 서버")
                        )
                        // TODO aws 인스턴스 서버 주소 추가
                );
    }
}