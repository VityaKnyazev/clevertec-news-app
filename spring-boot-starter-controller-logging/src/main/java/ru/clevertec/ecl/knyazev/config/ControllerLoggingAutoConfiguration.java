package ru.clevertec.ecl.knyazev.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.ecl.knyazev.aop.ControllerLoggerAspect;

@Configuration
@ConditionalOnClass(ControllerLoggerAspect.class)
public class ControllerLoggingAutoConfiguration {
    @Bean
    ControllerLoggerAspect controllerLoggerAspect() {
        return new ControllerLoggerAspect();
    }
}
