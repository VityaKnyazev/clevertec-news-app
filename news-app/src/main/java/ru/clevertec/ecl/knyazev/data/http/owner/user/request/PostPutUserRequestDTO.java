package ru.clevertec.ecl.knyazev.data.http.owner.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import ru.clevertec.ecl.knyazev.validation.group.Save;
import ru.clevertec.ecl.knyazev.validation.group.Update;

import java.util.List;

/**
 * Represents User DTO that sends to news-app service in
 * Request
 * <p>
 * HTTP method POST, PUT
 *
 * @param username  user dto name
 * @param password  user dto password
 * @param firstName user dto first name
 * @param lastName  user dto last name
 * @param email     user dto email
 * @param roles     user dto roles
 */
@Schema(description = "User DTO on request methods POST, PUT")
@Builder
public record PostPutUserRequestDTO(

        @Schema(description = "User DTO social and registration name",
                example = "masha")
        @NotBlank(message = "Username must be not null or whitespaces")
        @Size(min = 3, max = 15,
                message = "Username must contains from 3 to 15 symbols")
        String username,

        @Schema(description = "User DTO password",
                example = "type_pass")
        @NotBlank(message = "Password must be not null or whitespaces")
        @Size(min = 5, max = 20,
                message = "Password must contains from 5 to 20 symbols")
        String password,

        @Schema(description = "User DTO first name",
                example = "Mika")
        @NotBlank(message = "First name must be not null or whitespaces")
        @Size(min = 3, max = 15,
                message = "First name must contains from 3 to 15 symbols")
        String firstName,

        @Schema(description = "User DTO last name",
                example = "Kane")
        @NotBlank(message = "Last name must be not null or whitespaces")
        @Size(min = 3, max = 15,
                message = "Last name must contains from 3 to 15 symbols")
        String lastName,

        @Schema(description = "User DTO email",
                example = "masha@yandex.ru")
        @NotNull(message = "Email must not be null")
        @Email(message = "Email must be valid")
        String email,

        @Schema(description = "User DTO roles",
                example = "ROLE_JOURNALIST|ROLE_SUBSCRIBER")
        @Null(message = "Roles must be null, because you can't change roles. May contact to resource ADMIN",
                groups = Update.class)
        @NotNull(message = "Roles must not be null",
                groups = Save.class)
        List<@Pattern(regexp = "^(ROLE_JOURNALIST|ROLE_SUBSCRIBER)$",
                message = "Roles must have values ROLE_JOURNALIST or ROLE_SUBSCRIBER",
                groups = Save.class)
                String> roles
) {
}
