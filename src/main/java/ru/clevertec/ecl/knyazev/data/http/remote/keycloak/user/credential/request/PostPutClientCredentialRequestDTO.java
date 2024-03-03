package ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.credential.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Map;

/**
 * Represents user Credential DTO that sends to keycloak service in
 * Request
 * <p>
 * HTTP method POST, PUT
 *
 * @param type credential type
 * @param value credential value (password value)
 * @param temporary is credential temp
 */
@Schema(description = "user Credential DTO on request method POST, PUT")
@Builder
public record PostPutClientCredentialRequestDTO(

        @Schema(description = "User credential type",
        example = "password")
        String type,

        @Schema(description = "User credential password value",
                example = "125GH_58zkLop")
        String value,

        @Schema(description = "determine is user Credential temporary",
                example = "true|false")
        Boolean temporary
) {
}
