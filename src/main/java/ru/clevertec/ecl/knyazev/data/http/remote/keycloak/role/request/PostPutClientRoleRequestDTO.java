package ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Represents user Role DTO that sends to keycloak service in
 * Request
 * <p>
 * HTTP method POST, PUT
 * <p>
 * OR in REQUEST body
 *
 * @param id          role dto uuid
 * @param name        role dto name
 * @param description user dto first name
 * @param clientRole  user dto last name
 * @param containerId user dto email
 */
@Schema(description = "user Role DTO on request methods POST, PUT")
@Builder
public record PostPutClientRoleRequestDTO(
        @Schema(description = "user Role DTO uuid",
                example = "740c106d-b5a9-4886-8ef3-b960eeec4ad9")
        String id,

        @Schema(description = "user Role DTO name",
                example = "ROLE_JOURNALIST")
        String name,

        @Schema(description = "user Role DTO description",
                example = "Journalist role")
        String description,

        @Schema(description = "determine possessing user Role to client",
                example = "true|false")
        Boolean clientRole,

        @Schema(description = "User DTO containerId",
                example = "cbb85e09-401b-4f96-9c65-7ba32140f93e")
        String containerId
) {
}
