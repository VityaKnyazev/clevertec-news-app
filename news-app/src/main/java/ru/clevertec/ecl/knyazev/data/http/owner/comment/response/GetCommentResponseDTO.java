package ru.clevertec.ecl.knyazev.data.http.owner.comment.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Represents comment DTO that sends in
 * Response
 * <p>
 * on HTTP method GET, POST, PUT
 *
 * @param uuid
 * @param subscriberFirstName
 * @param subscriberLastName
 * @param subscriberEmail
 * @param text
 * @param created
 * @param updated
 */
@Schema(description = "comment DTO on response")
@Builder
public record GetCommentResponseDTO(
        @Schema(description = "comment DTO uuid",
                example = "92e737dd-844b-436b-b226-9adde7a7a12d")
        String uuid,

        @Schema(description = "comment DTO subscriber first name",
                example = "Petr")
        String subscriberFirstName,

        @Schema(description = "comment DTO subscriber last name",
                example = "Petrov")
        String subscriberLastName,

        @Schema(description = "comment DTO subscriber email",
                example = "petrov@mail.ru")
        String subscriberEmail,

        @Schema(description = "comment DTO text",
                example = "Да все хорошо.. Вот скоро еще один год переживем!")
        String text,

        @Schema(description = "comment DTO crate date",
                example = "2023-12-02T01:48:10.131")
        String created,

        @Schema(description = "comment DTO update date",
                example = "2024-03-03T12:50:22.169"
        )
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String updated
) {
}
