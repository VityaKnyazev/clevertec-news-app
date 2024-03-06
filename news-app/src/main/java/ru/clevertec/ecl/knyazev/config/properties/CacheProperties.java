package ru.clevertec.ecl.knyazev.config.properties;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * Represents cache properties from application property file
 *
 * @param algorithm cache algorithm type
 * @param size cache size
 */
@ConfigurationProperties(prefix = "cache")
@Builder
public record CacheProperties (
    String algorithm,

    Integer size
) {}
