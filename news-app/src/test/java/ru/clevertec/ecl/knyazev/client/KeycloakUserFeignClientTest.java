package ru.clevertec.ecl.knyazev.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.ecl.knyazev.config.FeignClientTestConfig;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.request.PostPutClientRoleRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.request.PostPutClientUserRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.response.GetClientUserResponseDTO;
import ru.clevertec.ecl.knyazev.util.RemoteServiceTestData;

import java.util.List;

@ActiveProfiles("test")
@Import(FeignClientTestConfig.class)
@RestClientTest
@AutoConfigureWireMock(port = 8060)
@RequiredArgsConstructor
public class KeycloakUserFeignClientTest {

    private final KeycloakUserFeignClient keycloakUserFeignClientImpl;

    @Test
    public void checkGetUserShouldReturnGetClientUserResponseDTO() {
        GetClientUserResponseDTO expectedGetClientUserResponseDTO =
                RemoteServiceTestData.expectedClientUserResponseDTO();

        String inputUserUUID = expectedGetClientUserResponseDTO.id();
        GetClientUserResponseDTO actualGetClientUserResponseDTO =
                keycloakUserFeignClientImpl.getUser(inputUserUUID);

        assertThat(actualGetClientUserResponseDTO)
                .isEqualTo(expectedGetClientUserResponseDTO);
    }

    @Test
    public void checkGetUserShouldThrowFeignException() {
        String invalidUserUUID = RemoteServiceTestData.INVALID_USER_UUID;

        assertThatExceptionOfType(FeignException.class)
                .isThrownBy(() -> keycloakUserFeignClientImpl.getUser(invalidUserUUID));
    }

    @Test
    public void checkGetUserByUsernameShouldReturnGetClientUserResponseDTOs() {
        GetClientUserResponseDTO[] expectedGetClientUserResponseDTOs =
                RemoteServiceTestData.expectedClientUserResponseDTOArr();

        String inputUserName = expectedGetClientUserResponseDTOs[0].username();
        Boolean inputExact = true;
        GetClientUserResponseDTO[] actualGetClientUserResponseDTOs =
                keycloakUserFeignClientImpl.getUserByUsername(inputUserName, inputExact);

        assertThat(actualGetClientUserResponseDTOs)
                .isEqualTo(expectedGetClientUserResponseDTOs);
    }

    @Test
    public void checkGetUserByUsernameShouldReturnEmptyArray() {
        String inputInvalidUserName = RemoteServiceTestData.INVALID_USER_NAME;
        Boolean inputExact = true;
        GetClientUserResponseDTO[] actualGetClientUserResponseDTOs =
                keycloakUserFeignClientImpl.getUserByUsername(inputInvalidUserName, inputExact);

        assertThat(actualGetClientUserResponseDTOs).isEmpty();
    }

    @Test
    public void checkGetAllShouldReturnGetClientUserResponseDTOs() {
        List<GetClientUserResponseDTO> expectedGetClientUserResponseDTOS =
                RemoteServiceTestData.expectedClientUserResponseDTOs();

        List<GetClientUserResponseDTO> actualGetClientUserResponseDTOs =
                keycloakUserFeignClientImpl.getAll();

        assertThat(actualGetClientUserResponseDTOs)
                .isEqualTo(expectedGetClientUserResponseDTOS);
    }

    @Test
    public void checkSaveUserShouldReturnCreatedAndDoesntThrowAnyException() {
        PostPutClientUserRequestDTO inputPostClientUserRequestDTO =
                RemoteServiceTestData.expectedClientUserPostPutRequestDTO();

        assertThatCode(() -> keycloakUserFeignClientImpl.saveUser(inputPostClientUserRequestDTO))
                .doesNotThrowAnyException();
    }

    @Test
    public void checkMapRoleToUserShouldReturnOkAndSaveRoleToUser() {

        String inputUserUUID = RemoteServiceTestData.USER_UUID;
        String clientUUID = RemoteServiceTestData.CLIENT_UUID;
        PostPutClientRoleRequestDTO[] inputPostPutClientRoleRequestDTOs =
                RemoteServiceTestData.expectedPostPutClientRoleRequestDTOs();

        assertThatCode(() -> keycloakUserFeignClientImpl.mapRoleToUser(inputUserUUID,
                clientUUID,
                inputPostPutClientRoleRequestDTOs))
                .doesNotThrowAnyException();
    }

    @Test
    public void checkUpdateUserShouldReturnOkAndDoesntThrowAnyException() {
        String inputUserUUID = RemoteServiceTestData.USER_UUID;
        PostPutClientUserRequestDTO inputPostPutClientUserRequestDTO =
                RemoteServiceTestData.expectedClientUserPostPutRequestDTO();

        assertThatCode(() -> keycloakUserFeignClientImpl.updateUser(inputUserUUID,
                inputPostPutClientUserRequestDTO))
                .doesNotThrowAnyException();
    }

    @Test
    public void checkDeleteUserShouldReturnNoContentAndDoesntThrowAnyException() {
        String inputUserUUID = RemoteServiceTestData.USER_UUID;

        assertThatCode(() -> keycloakUserFeignClientImpl.deleteUser(inputUserUUID))
                .doesNotThrowAnyException();
    }
}
