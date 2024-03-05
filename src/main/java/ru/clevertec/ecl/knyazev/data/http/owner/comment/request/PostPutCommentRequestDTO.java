package ru.clevertec.ecl.knyazev.data.http.owner.comment.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

/**
 * Represents Comment DTO that sends in
 * Request
 * <p>
 * HTTP method POST, PUT
 *
 * @param text comment DTO text
 * @param newsUUID comment DTO news uuid
 */
@Schema(description = "Comment DTO on request methods POST, PUT")
@Builder
public record PostPutCommentRequestDTO(
        @Schema(description = "comment DTO text",
                example = "Славный коментарий получился")
        @NotNull(message = "comment DTO text must not be null")
        @Size(min = 3, max = 800, message = "comment DTO text must be from 3 to 800 symbols")
        String text,

        @Schema(description = "comment DTO news uuid",
                example = "3")
        @NotNull(message = "comment DTO news uuid must be not null")
        @UUID(message = "comment DTO news id must be uuid string in lower case")
        String newsUUID
) {
}
