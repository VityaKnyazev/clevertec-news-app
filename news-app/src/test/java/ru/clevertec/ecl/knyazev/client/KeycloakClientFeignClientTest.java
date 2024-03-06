package ru.clevertec.ecl.knyazev.client;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.ecl.knyazev.config.FeignClientTestConfig;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.client.response.GetClientKeycloakClientResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.response.GetClientRoleResponseDTO;
import ru.clevertec.ecl.knyazev.util.RemoteServiceTestData;

@ActiveProfiles("test")
@Import(FeignClientTestConfig.class)
@RestClientTest
@AutoConfigureWireMock(port = 8060)
@RequiredArgsConstructor
public class KeycloakClientFeignClientTest {

    private final KeycloakClientFeignClient keycloakClientFeignClientImpl;

    @Test
    public void checkGetClientsShouldReturnOKGetClientKeycloakClientResponseDTOs() {
        GetClientKeycloakClientResponseDTO[] expectedGetClientKeycloakClientResponseDTOs =
                RemoteServiceTestData.expectedClientKeycloakClientResponseDTOArr();

        String inputClientIdName = RemoteServiceTestData.CLIENT_ID_NAME;
        GetClientKeycloakClientResponseDTO[] actualGetClientKeycloakClientResponseDTOs =
                keycloakClientFeignClientImpl.getClients(inputClientIdName);

        assertThat(actualGetClientKeycloakClientResponseDTOs)
                .isEqualTo(expectedGetClientKeycloakClientResponseDTOs);
    }

    @Test
    public void checkGetClientsShouldReturnOkEmptyResponse() {
        String inputInvalidClientIdName = RemoteServiceTestData.CLIENT_INVALID_ID_NAME;
        GetClientKeycloakClientResponseDTO[] actualGetClientKeycloakClientResponseDTOs =
                keycloakClientFeignClientImpl.getClients(inputInvalidClientIdName);

        assertThat(actualGetClientKeycloakClientResponseDTOs)
                .isEmpty();
    }

    @Test
    public void checkGetClientRoleByClientUuidAndRoleNameShouldReturnOkGetClientRoleResponseDTO() {
        GetClientRoleResponseDTO expectedGetClientRoleResponseDTO =
                RemoteServiceTestData.expectedClientRoleResponseDTO();

        String clientUUID = RemoteServiceTestData.CLIENT_UUID;
        String roleName = RemoteServiceTestData.ROLE_NAME;
        GetClientRoleResponseDTO actualGetClientRoleResponseDTO =
                keycloakClientFeignClientImpl.getClientRoleByClientUuidAndRoleName(clientUUID, roleName);

        assertThat(actualGetClientRoleResponseDTO)
                .isEqualTo(expectedGetClientRoleResponseDTO);
    }

    @Test
    public void checkGetClientRoleByClientUuidAndRoleNameShouldThrowFeignExceptionOnInvalidClientUUID() {
        String inputInvalidClientUUID = RemoteServiceTestData.CLIENT_INVALID_UUID;
        String inputRoleName = RemoteServiceTestData.ROLE_NAME;

        assertThatExceptionOfType(FeignException.class)
                .isThrownBy(() ->
                        keycloakClientFeignClientImpl.getClientRoleByClientUuidAndRoleName(inputInvalidClientUUID,
                                inputRoleName));
    }

    @Test
    public void checkGetClientRoleByClientUuidAndRoleNameShouldThrowFeignExceptionOnInvalidClientRoleName() {
        String inputClientUUID = RemoteServiceTestData.CLIENT_UUID;
        String inputInvalidRoleName = RemoteServiceTestData.ROLE_INVALID_NAME;

        assertThatExceptionOfType(FeignException.class)
                .isThrownBy(() ->
                        keycloakClientFeignClientImpl.getClientRoleByClientUuidAndRoleName(inputClientUUID,
                                inputInvalidRoleName));
    }
}
