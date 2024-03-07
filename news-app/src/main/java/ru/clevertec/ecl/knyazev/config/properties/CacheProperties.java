package ru.clevertec.ecl.knyazev.config.properties;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Represents cache properties from application property file
 *
 * @param enabled   is cache enabled
 * @param type      cache type custom or redis
 * @param algorithm cache algorithm type
 * @param size      cache size
 */
@ConfigurationProperties(prefix = "cache")
@Builder
public record CacheProperties(
        @NotNull(message = "cache must be enabled or disabled but not null")
        Boolean enabled,

        @Pattern(regexp = "^(redis|custom)$",
                message = "cache type must be custom or redis")
        String type,

        @Pattern(regexp = "^(LRU|LFU)$",
                message = "custom cache algorithm must be LRU or LFU")
        String algorithm,

        @Positive(message = "cache size has to be positive")
        Integer size
) {
}
