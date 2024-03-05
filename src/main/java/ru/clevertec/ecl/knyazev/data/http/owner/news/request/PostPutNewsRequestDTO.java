package ru.clevertec.ecl.knyazev.data.http.owner.news.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.UUID;

/**
 * Represents News DTO that sends in
 * Request
 * <p>
 * HTTP method POST, PUT
 *
 * @param title          news title
 * @param text           news text
 */
@Schema(description = "News DTO on request methods POST, PUT")
@Builder
public record PostPutNewsRequestDTO(
        @Schema(description = "news DTO title",
                example = "Красная жара надвигается на континент")
        @NotNull(message = "news DTO title must not be null")
        @Size(min = 3, max = 150, message = "News DTO title must be from 3 to 150 symbols")
        String title,

        @Schema(description = "news DTO text",
                example = "Красная жара надвигается на континент. Засуха на европейском континенте...")
        @NotNull(message = "news DTO text must not be null")
        @Size(min = 3, max = 200000, message = "News DTO text must be from 3 to 200_000 symbols")
        String text
) {
}
