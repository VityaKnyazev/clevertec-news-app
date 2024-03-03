package ru.clevertec.ecl.knyazev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile({"dev", "prod"})
public class KeycloakConfig {

    private static final String PREFERRED_USERNAME = "preferred_username";
    private static final String USER_ROLES = "roles";

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setPrincipalClaimName(PREFERRED_USERNAME);
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt ->
            jwt.getClaimAsStringList(USER_ROLES).stream()
                    .map(SimpleGrantedAuthority::new)
                    .map(GrantedAuthority.class::cast)
                    .toList());

        return jwtAuthenticationConverter;
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oauth2UserService() {
        return userRequest ->
                new DefaultOidcUser(userRequest.getIdToken()
                        .getClaimAsStringList(USER_ROLES).stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList(),
                        userRequest.getIdToken());
    }
}
