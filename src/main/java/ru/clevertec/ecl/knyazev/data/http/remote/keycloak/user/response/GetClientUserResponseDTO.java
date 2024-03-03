package ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Represents User DTO that returns from keycloak service in
 * Response
 * <p>
 * HTTP method GET
 *
 * @param id        user dto uuid
 * @param username  user dto name
 * @param firstName user dto first name
 * @param lastName  user dto last name
 * @param email     user dto email
 */
@Schema(description = "User DTO on response from keycloak service")
@Builder
public record GetClientUserResponseDTO(
        @Schema(description = "user DTO uuid",
                example = "5dec0ec6-cf10-4234-94fc-78db68ee82dd")
        String id,

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
        String email
) {
}
