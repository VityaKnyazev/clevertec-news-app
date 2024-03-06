package ru.clevertec.ecl.knyazev.data.http.owner.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Represents User DTO that sends from news-app service in
 * Response
 * <p>
 * on HTTP method Get
 *
 *
 * @param uuid user DTO uuid
 * @param firstName user DTO first name
 * @param lastName user DTO last name
 * @param email user DTO email
 */
@Schema(description = "User DTO on request method GET")
@Builder
public record GetUserResponseDTO(

        @Schema(description = "User DTO uuid",
        example = "d86bc1b6-365b-460c-ad7a-737c62184c97")
        String uuid,

        @Schema(description = "User DTO first name",
                example = "Misha")
        String firstName,

        @Schema(description = "User DTO last name",
                example = "Mavashy")
        String lastName,

        @Schema(description = "User DTO email",
                example = "Mavashy")
        String email
) {
}
