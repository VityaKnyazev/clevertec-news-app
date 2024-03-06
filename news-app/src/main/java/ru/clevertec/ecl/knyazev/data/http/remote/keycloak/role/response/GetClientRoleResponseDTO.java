package ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Represents user Role DTO that sends from keycloak service in
 * Response
 * <p>
 * on HTTP method GET
 *
 * @param id          role dto uuid
 * @param name        role dto name
 * @param description role dto description
 * @param clientRole  is role dto a client role
 * @param containerId role dto container id
 */
@Schema(description = "user Role DTO on response from keycloak service")
@Builder
public record GetClientRoleResponseDTO(
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
