package ru.clevertec.ecl.knyazev.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ConfigurationPropertiesScan(basePackages = {"ru.clevertec.ecl.knyazev.config.properties"})
@EnableJpaRepositories(basePackages = {"ru.clevertec.ecl.knyazev.repository"})
@EnableWebMvc
public class AppConfig {

}
