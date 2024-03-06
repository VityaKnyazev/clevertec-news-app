package ru.clevertec.ecl.knyazev.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.ecl.knyazev.controller.exception.DefaultResponseEntityExceptionHandler;

@Configuration
@ConditionalOnClass(DefaultResponseEntityExceptionHandler.class)
@ComponentScan(basePackages = {"ru.clevertec.ecl.knyazev.controller.exception"})
public class ExceptionControllerAutoConfiguration {

}
