package ru.clevertec.ecl.knyazev.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import ru.clevertec.ecl.knyazev.client.KeycloakClientFeignClient;
import ru.clevertec.ecl.knyazev.client.KeycloakUserFeignClient;

@TestConfiguration
@Import({FeignAutoConfiguration.class})
@EnableFeignClients(clients = { KeycloakUserFeignClient.class, KeycloakClientFeignClient.class })
public class FeignClientTestConfig {
}
