package ru.clevertec.ecl.knyazev.util;

import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.client.response.GetClientKeycloakClientResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.response.GetClientRoleResponseDTO;

public class ClientTestData {
    public static final String CLIENT_ID = "dd9fb3b6-a0b2-414c-a0e8-c711ba40fb42";
    public static final String CLIENT_ID_UNIQUE_STRING = "test-app";
    public static final String CLIENT_NAME = "Test app";
    public static final String CLIENT_BASE_URL = "http://localhost:8060";
    public static final Boolean CLIENT_ENABLED = true;

    public static final String CLIENT_ROLE_ID = "f6a0ad44-d74d-4c06-ae9b-fc472480797e";
    public static final String CLIENT_ROLE_NAME = "ROLE_JOURNALIST";
    public static final String CLIENT_ROLE_DESCRIPTION = "Role journalist to write articles etc";
    public static final Boolean CLIENT_ROLE_IS = true;
    public static final String CLIENT_ROLE_CONTAINER_ID = "fe6f0443-7051-4bbd-abbf-97399322a0eb";


    public static final String CLIENT_INVALID_ID_UNIQUE_STRING = "invalid-app";

    public static GetClientKeycloakClientResponseDTO expectedClientResponseDTO() {
        return GetClientKeycloakClientResponseDTO.builder()
                .id(CLIENT_ID)
                .clientId(CLIENT_ID_UNIQUE_STRING)
                .name(CLIENT_NAME)
                .baseUrl(CLIENT_BASE_URL)
                .enabled(CLIENT_ENABLED)
                .build();
    }

    public static GetClientKeycloakClientResponseDTO[] expectedClientResponseDTOs() {
        return new GetClientKeycloakClientResponseDTO[]{
                GetClientKeycloakClientResponseDTO.builder()
                        .id(CLIENT_ID)
                        .clientId(CLIENT_ID_UNIQUE_STRING)
                        .name(CLIENT_NAME)
                        .baseUrl(CLIENT_BASE_URL)
                        .enabled(CLIENT_ENABLED)
                        .build()
        };
    }

    public static GetClientRoleResponseDTO expectedClientRoleResponseDTO() {
        return GetClientRoleResponseDTO.builder()
                .id(CLIENT_ROLE_ID)
                .name(CLIENT_ROLE_NAME)
                .description(CLIENT_ROLE_DESCRIPTION)
                .clientRole(CLIENT_ROLE_IS)
                .containerId(CLIENT_ROLE_CONTAINER_ID)
                .build();
    }
}
