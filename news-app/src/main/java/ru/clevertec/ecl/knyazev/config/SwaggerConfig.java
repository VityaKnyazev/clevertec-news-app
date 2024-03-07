package ru.clevertec.ecl.knyazev.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@ConditionalOnProperty(prefix = "springdoc.api-docs",
        name = "enabled",
        havingValue = "true")
public class SwaggerConfig {
    private static final String INFO_AUTHOR = "Vita Knyazev";
    private static final String INFO_EMAIL = "Utopio@tut.by";
    private static final String INFO_DESCRIPTION = "API final news app";

    private static final String SERVER_URL = "http://localhost:8080";
    private static final String SERVER_DESCRIPTION = "Server for news app";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .description(INFO_DESCRIPTION)
                        .contact(new Contact()
                                .name(INFO_AUTHOR)
                                .email(INFO_EMAIL)))
                .servers(List.of(new Server()
                        .url(SERVER_URL)
                        .description(SERVER_DESCRIPTION)));
    }
}
