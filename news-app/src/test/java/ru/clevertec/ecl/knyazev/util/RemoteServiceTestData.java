package ru.clevertec.ecl.knyazev.util;

import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.client.response.GetClientKeycloakClientResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.request.PostPutClientRoleRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.response.GetClientRoleResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.credential.request.PostPutClientCredentialRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.request.PostPutClientUserRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.response.GetClientUserResponseDTO;

import java.util.List;

/**
 * Represents test data for Remote service (like keycloak)
 */
public class RemoteServiceTestData {

    public static String USER_UUID = "32ce6b0-7e4b-4f25-9703-72103731def1";
    public static String USER_NAME = "misha";
    public static String USER_CREDENTIALS_TYPE = "password";
    public static String USER_CREDENTIALS_VALUE = "kuLks_45f";
    public static Boolean USER_CREDENTIALS_TEMPORARY = false;
    public static String USER_FIRST_NAME = "Misha";
    public static String USER_LAST_NAME = "Mad";
    public static String USER_EMAIL = "misha@mail.ru";
    public static Boolean USER_ENABLED = true;

    public static String INVALID_USER_UUID = "22d30dff-95db-4fc0-9878-d07acc0ae939";
    public static String INVALID_USER_NAME = "maksimka";

    public static String CLIENT_UUID = "888f589c-3fa9-4dd3-b262-df2064fdbd6d";
    public static String CLIENT_ID_NAME = "some-app";
    public static String CLIENT_NAME = "Some app client";
    public static String CLIENT_URL = "http://localhost:8060";
    public static Boolean CLIENT_ENABLED = true;

    public static String CLIENT_INVALID_ID_NAME = "invalid-app";
    public static String CLIENT_INVALID_UUID = "CLIENT_UUID";

    public static String ROLE_ID = "c8a17642-e4fb-4894-b825-13a875ea5a0d";
    public static String ROLE_NAME = "ROLE_JOURNALIST";
    public static String ROLE_DESCRIPTION = "Journalist role";
    public static Boolean ROLE_IS_CLIENT_ROLE = true;
    public static String ROLE_CONTAINER_ID = "888f589c-3fa9-4dd3-b262-df2064fdbd6d";

    public static String ROLE_INVALID_NAME = "ROLE_KILLER";

    public static GetClientUserResponseDTO expectedClientUserResponseDTO() {
        return GetClientUserResponseDTO.builder()
                .id(USER_UUID)
                .username(USER_NAME)
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(USER_EMAIL)
                .build();
    }

    public static List<GetClientUserResponseDTO> expectedClientUserResponseDTOs() {
        return List.of(
                GetClientUserResponseDTO.builder()
                        .id(USER_UUID)
                        .username(USER_NAME)
                        .firstName(USER_FIRST_NAME)
                        .lastName(USER_LAST_NAME)
                        .email(USER_EMAIL)
                        .build()
        );
    }

    public static GetClientUserResponseDTO[] expectedClientUserResponseDTOArr() {
        return new GetClientUserResponseDTO[]{
                GetClientUserResponseDTO.builder()
                        .id(USER_UUID)
                        .username(USER_NAME)
                        .firstName(USER_FIRST_NAME)
                        .lastName(USER_LAST_NAME)
                        .email(USER_EMAIL)
                        .build()
        };
    }

    public static PostPutClientUserRequestDTO expectedClientUserPostPutRequestDTO() {
        return PostPutClientUserRequestDTO.builder()
                .username(USER_NAME)
                .postPutClientCredentialRequestDTOs(List.of(
                        PostPutClientCredentialRequestDTO.builder()
                                .type(USER_CREDENTIALS_TYPE)
                                .value(USER_CREDENTIALS_VALUE)
                                .temporary(USER_CREDENTIALS_TEMPORARY)
                                .build()))
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(USER_EMAIL)
                .enabled(USER_ENABLED)
                .build();
    }

    public static GetClientKeycloakClientResponseDTO expectedClientKeycloakClientResponseDTO() {
        return GetClientKeycloakClientResponseDTO.builder()
                .id(CLIENT_UUID)
                .clientId(CLIENT_ID_NAME)
                .name(CLIENT_NAME)
                .baseUrl(CLIENT_URL)
                .enabled(CLIENT_ENABLED)
                .build();
    }

    public static GetClientKeycloakClientResponseDTO[] expectedClientKeycloakClientResponseDTOArr() {
        return new GetClientKeycloakClientResponseDTO[]{
                GetClientKeycloakClientResponseDTO.builder()
                        .id(CLIENT_UUID)
                        .clientId(CLIENT_ID_NAME)
                        .name(CLIENT_NAME)
                        .baseUrl(CLIENT_URL)
                        .enabled(CLIENT_ENABLED)
                        .build()
        };
    }

    public static GetClientRoleResponseDTO expectedClientRoleResponseDTO() {
        return GetClientRoleResponseDTO.builder()
                .id(ROLE_ID)
                .name(ROLE_NAME)
                .description(ROLE_DESCRIPTION)
                .clientRole(ROLE_IS_CLIENT_ROLE)
                .containerId(ROLE_CONTAINER_ID)
                .build();
    }

    public static PostPutClientRoleRequestDTO[] expectedPostPutClientRoleRequestDTOs() {
        return new PostPutClientRoleRequestDTO[]{
                PostPutClientRoleRequestDTO.builder()
                        .id(ROLE_ID)
                        .name(ROLE_NAME)
                        .description(ROLE_DESCRIPTION)
                        .clientRole(ROLE_IS_CLIENT_ROLE)
                        .containerId(ROLE_CONTAINER_ID)
                        .build()
        };
    }

}
