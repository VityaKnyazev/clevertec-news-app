package ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.credential.request.PostPutClientCredentialRequestDTO;

import java.util.List;

/**
 * Represents User DTO that sends to keycloak service in
 * Request
 * <p>
 * HTTP method POST, PUT
 *
 * @param username                           user dto name
 * @param firstName                          user dto first name
 * @param lastName                           user dto last name
 * @param email                              user dto email
 * @param enabled                            user dto enabled
 * @param postPutClientCredentialRequestDTOs user dto credentials
 */
@Schema(description = "User DTO on request method POST, PUT")
@Builder
public record PostPutClientUserRequestDTO(
        @Schema(description = "User DTO social and registration name",
                example = "masha")
        String username,

        @Schema(description = "User DTO first name",
                example = "Mika")
        String firstName,

        @Schema(description = "User DTO last name",
                example = "Kane")
        String lastName,

        @Schema(description = "User DTO email",
                example = "masha@yandex.ru")
        String email,

        @Schema(description = "User DTO enabled",
                example = "true|false")
        Boolean enabled,

        @Schema(description = "User DTO credentials")
        @JsonProperty("credentials")
        List<PostPutClientCredentialRequestDTO> postPutClientCredentialRequestDTOs
) {
}
