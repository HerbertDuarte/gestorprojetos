package com.herbertduarte.gestorprojetos.config.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Value("${api.prod.url}")
    private String prodUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gestor de Projetos")
                        .description("API REST para gerenciamento de projetos, membros e autenticação")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Herbert Duarte")
                                .email("herbertduarte.santos@gmail.com")
                                .url("https://www.linkedin.com/in/herbert-duarte-8534b71a2")))
                .servers(List.of(
                        new Server()
                                .url(prodUrl)
                                .description("Servidor de Produção"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor Local")

                ))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT para autenticação")))
                .security(List.of(
                        new SecurityRequirement().addList("bearer-jwt")
                ));
    }
}
