package ru.clevertec.ecl.knyazev.data.http.owner.news.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Represents news DTO that sends in
 * Response
 * <p>
 * on HTTP method GET, POST, PUT
 *
 * @param uuid            news uuid
 * @param authorFirstName news author first name
 * @param authorLastName  news author last name
 * @param authorEmail     news author email
 * @param title           news title
 * @param text            news text
 * @param created         news created date time
 * @param updated         news updated date time
 */
@Schema(description = "news DTO on response")
@Builder
public record GetNewsResponseDTO(
        @Schema(description = "news DTO uuid",
                example = "548f8b02-4f28-49af-864b-b50faa1c1438")
        String uuid,

        @Schema(description = "news DTO author first name",
                example = "Anton")
        String authorFirstName,

        @Schema(description = "news DTO author last name",
                example = "Ivanov")
        String authorLastName,

        @Schema(description = "news DTO author email",
                example = "ivanov@mail.ru")
        String authorEmail,

        @Schema(description = "news DTO title",
                example = "Красная жара надвигается на континент")
        String title,

        @Schema(description = "news DTO text",
                example = "Красная жара надвигается на континент. Засуха на европейском континенте...")
        String text,

        @Schema(description = "news DTO crate date",
                example = "2023-11-02T02:50:12.208")
        String created,

        @Schema(description = "news DTO update date",
                example = "2024-03-04T20:50:22.169"
        )
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String updated
) {
}
