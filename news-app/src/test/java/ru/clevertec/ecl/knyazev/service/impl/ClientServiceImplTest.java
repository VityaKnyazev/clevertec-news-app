package ru.clevertec.ecl.knyazev.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.knyazev.client.KeycloakClientFeignClient;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.client.response.GetClientKeycloakClientResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.response.GetClientRoleResponseDTO;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;
import ru.clevertec.ecl.knyazev.util.ClientTestData;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {
    @Mock
    private KeycloakClientFeignClient keycloakClientFeignClientMock;

    @InjectMocks
    private ClientServiceImpl clientServiceImpl;

    @Test
    public void checkGetClientKeycloakClientResponseDTOShouldReturnClientDTO() {
        GetClientKeycloakClientResponseDTO[] expectedClientResponseDTOs =
                ClientTestData.expectedClientResponseDTOs();

        when(keycloakClientFeignClientMock.getClients(any(String.class)))
                .thenReturn(expectedClientResponseDTOs);

        String inputUniqueIdName = ClientTestData.CLIENT_ID_UNIQUE_STRING;
        GetClientKeycloakClientResponseDTO actualGetClientKeycloakClientResponseDTO =
                clientServiceImpl.getClientByUniqueIdName(inputUniqueIdName);

        assertThat(actualGetClientKeycloakClientResponseDTO)
                .isEqualTo(expectedClientResponseDTOs[0]);

    }

    @Test
    public void checkGetClientKeycloakClientResponseDTOShouldThrowServiceException() {
        when(keycloakClientFeignClientMock.getClients(any(String.class)))
                .thenReturn(new GetClientKeycloakClientResponseDTO[]{});

        String inputUniqueIdName = ClientTestData.CLIENT_INVALID_ID_UNIQUE_STRING;

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> clientServiceImpl.getClientByUniqueIdName(inputUniqueIdName));

    }

    @Test
    public void checkGetClientRoleByClientUniqueIdNameAndRoleNameShouldReturnClientRoleDTO() {
        GetClientRoleResponseDTO expectedGetClientRoleResponseDTO =
                ClientTestData.expectedClientRoleResponseDTO();

        when(keycloakClientFeignClientMock.getClientRoleByClientUuidAndRoleName(any(String.class),
                any(String.class)))
                .thenReturn(expectedGetClientRoleResponseDTO);

        String clientUniqueIdName = ClientTestData.CLIENT_ID_UNIQUE_STRING;
        String roleName = ClientTestData.CLIENT_ROLE_NAME;
        GetClientRoleResponseDTO actualGetClientRoleResponseDTO =
                clientServiceImpl.getClientRoleByClientUniqueIdNameAndRoleName(clientUniqueIdName,
                        roleName);

        assertThat(actualGetClientRoleResponseDTO).isEqualTo(expectedGetClientRoleResponseDTO);
    }
}
