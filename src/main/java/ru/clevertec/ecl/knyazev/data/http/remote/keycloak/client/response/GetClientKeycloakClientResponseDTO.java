package ru.clevertec.ecl.knyazev.data.http.remote.keycloak.client.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Represents client DTO that sends from keycloak service in
 * Response
 * <p>
 * on HTTP method GET
 *
 * @param id client uuid
 * @param clientId client id
 * @param name client name
 * @param baseUrl client base url
 * @param enabled is client enabled
 */
@Schema(description = "client DTO on response from keycloak service")
@Builder
public record GetClientKeycloakClientResponseDTO(
        @Schema(description = "client DTO uuid",
                example = "983502cf-f743-4f4d-9fbe-3e97e3d4d8c1")
        String id,

        @Schema(description = "client id (like client unique name identifier)",
                example = "news-app")
        String clientId,

        @Schema(description = "client name",
                example = "News app Service")
        String name,

        @Schema(description = "client base url",
                example = "http://localhost:8080")
        String baseUrl,

        @Schema(description = "is client enabled",
                example = "true|false")
        Boolean enabled
) {
}
