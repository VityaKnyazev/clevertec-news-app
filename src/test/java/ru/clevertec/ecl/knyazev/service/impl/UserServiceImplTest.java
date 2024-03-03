package ru.clevertec.ecl.knyazev.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.knyazev.client.KeycloakUserFeignClient;
import ru.clevertec.ecl.knyazev.data.http.owner.user.request.PostPutUserRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.user.response.GetUserResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.client.response.GetClientKeycloakClientResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.request.PostPutClientRoleRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.role.response.GetClientRoleResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.request.PostPutClientUserRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.remote.keycloak.user.response.GetClientUserResponseDTO;
import ru.clevertec.ecl.knyazev.mapper.RoleMapper;
import ru.clevertec.ecl.knyazev.mapper.RoleMapperImpl;
import ru.clevertec.ecl.knyazev.mapper.UserMapper;
import ru.clevertec.ecl.knyazev.mapper.UserMapperImpl;
import ru.clevertec.ecl.knyazev.service.ClientService;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;
import ru.clevertec.ecl.knyazev.util.RemoteServiceTestData;
import ru.clevertec.ecl.knyazev.util.UserTestData;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private KeycloakUserFeignClient keycloakUserFeignClientMock;

    @Mock
    private ClientService clientServiceMock;

    @Spy
    private UserMapper userMapperImpl = new UserMapperImpl();
    @Spy
    private RoleMapper roleMapperImpl = new RoleMapperImpl();

    @Captor
    private ArgumentCaptor<PostPutClientUserRequestDTO> postPutClientUserRequestDTOArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> userUuidArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> clientUuidArgumentCaptor;
    @Captor
    private ArgumentCaptor<PostPutClientRoleRequestDTO[]> postPutClientRoleRequestDTOArgumentCaptor;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    public void setUp() {
        userServiceImpl.setClientId(RemoteServiceTestData.CLIENT_ID_NAME);
    }

    @Test
    public void checkGetUserResponseDTOShouldReturnUserDTO() {

        GetClientUserResponseDTO expectedGetClientUserResponseDTO =
                RemoteServiceTestData.expectedClientUserResponseDTO();

        when(keycloakUserFeignClientMock.getUser(any(String.class)))
                .thenReturn(expectedGetClientUserResponseDTO);

        String inputUUID = "32ce6b0-7e4b-4f25-9703-72103731def1";
        GetUserResponseDTO actualGetClientUserResponseDTO =
                userServiceImpl.getUserResponseDTO(inputUUID);

        assertAll(
                () -> assertThat(actualGetClientUserResponseDTO).isNotNull(),
                () -> assertThat(actualGetClientUserResponseDTO.uuid())
                        .isEqualTo(expectedGetClientUserResponseDTO.id()),
                () -> assertThat(actualGetClientUserResponseDTO.firstName())
                        .isEqualTo(expectedGetClientUserResponseDTO.firstName()),
                () -> assertThat(actualGetClientUserResponseDTO.lastName())
                        .isEqualTo(expectedGetClientUserResponseDTO.lastName()),
                () -> assertThat(actualGetClientUserResponseDTO.email())
                        .isEqualTo(expectedGetClientUserResponseDTO.email())
        );
    }

    @Test
    public void checkGetUserResponseDTOsShouldReturnUserDTOs() {
        List<GetClientUserResponseDTO> expectedGetClientUserResponseDTOs =
                RemoteServiceTestData.expectedClientUserResponseDTOs();

        when(keycloakUserFeignClientMock.getAll())
                .thenReturn(expectedGetClientUserResponseDTOs);

        List<GetUserResponseDTO> actualGetUserResponseDTOs =
                userServiceImpl.getUserResponseDTOs();

        assertAll(
                () -> assertThat(actualGetUserResponseDTOs).isNotNull()
                        .hasSize(1),
                () -> assertThat(actualGetUserResponseDTOs.stream()
                        .map(GetUserResponseDTO::uuid).toList())
                        .isEqualTo(expectedGetClientUserResponseDTOs.stream()
                                .map(GetClientUserResponseDTO::id).toList()),
                () -> assertThat(actualGetUserResponseDTOs.stream()
                        .map(GetUserResponseDTO::firstName).toList())
                        .isEqualTo(expectedGetClientUserResponseDTOs.stream()
                                .map(GetClientUserResponseDTO::firstName).toList()),
                () -> assertThat(actualGetUserResponseDTOs.stream()
                        .map(GetUserResponseDTO::lastName).toList())
                        .isEqualTo(expectedGetClientUserResponseDTOs.stream()
                                .map(GetClientUserResponseDTO::lastName).toList()),
                () -> assertThat(actualGetUserResponseDTOs.stream()
                        .map(GetUserResponseDTO::email).toList())
                        .isEqualTo(expectedGetClientUserResponseDTOs.stream()
                                .map(GetClientUserResponseDTO::email).toList())
        );
    }

    @Test
    public void checkAddShouldAddUserWithRolesToRemoteService() {
        GetClientUserResponseDTO[] expectedGetClientUserResponseDTOS = {
                RemoteServiceTestData.expectedClientUserResponseDTO()
        };
        GetClientKeycloakClientResponseDTO expectedGetClientKeycloakClientResponseDTO =
                RemoteServiceTestData.expectedClientKeycloakClientResponseDTO();
        GetClientRoleResponseDTO expectedGetClientRoleResponseDTO =
                RemoteServiceTestData.expectedClientRoleResponseDTO();

        when(keycloakUserFeignClientMock
                .getUserByUsername(any(String.class), booleanThat(exact -> exact.equals(true))))
                .thenReturn(expectedGetClientUserResponseDTOS);
        when(clientServiceMock.getClientByUniqueIdName(any(String.class)))
                .thenReturn(expectedGetClientKeycloakClientResponseDTO);
        when(clientServiceMock.getClientRoleByClientUniqueIdNameAndRoleName(any(String.class), any(String.class)))
                .thenReturn(expectedGetClientRoleResponseDTO);

        PostPutUserRequestDTO inputPostPutUserRequestDTO = UserTestData.expectedSavingUserRequestDTO();
        userServiceImpl.add(inputPostPutUserRequestDTO);

        verify(keycloakUserFeignClientMock)
                .saveUser(postPutClientUserRequestDTOArgumentCaptor.capture());
        verify(keycloakUserFeignClientMock)
                .mapRoleToUser(userUuidArgumentCaptor.capture(),
                        clientUuidArgumentCaptor.capture(),
                        postPutClientRoleRequestDTOArgumentCaptor.capture());

        assertAll(
                () -> assertThat(postPutClientUserRequestDTOArgumentCaptor).isNotNull(),
                () -> assertThat(postPutClientUserRequestDTOArgumentCaptor.getValue().username())
                        .isEqualTo(inputPostPutUserRequestDTO.username()),
                () -> assertThat(postPutClientUserRequestDTOArgumentCaptor.getValue()
                        .postPutClientCredentialRequestDTOs().get(0).value())
                        .isEqualTo(inputPostPutUserRequestDTO.password()),
                () -> assertThat(postPutClientUserRequestDTOArgumentCaptor.getValue().firstName())
                        .isEqualTo(inputPostPutUserRequestDTO.firstName()),
                () -> assertThat(postPutClientUserRequestDTOArgumentCaptor.getValue().lastName())
                        .isEqualTo(inputPostPutUserRequestDTO.lastName()),
                () -> assertThat(postPutClientUserRequestDTOArgumentCaptor.getValue().email())
                        .isEqualTo(inputPostPutUserRequestDTO.email()),
                () -> assertThat(postPutClientUserRequestDTOArgumentCaptor.getValue().enabled())
                        .isEqualTo(true),

                () -> assertThat(userUuidArgumentCaptor.getValue()).isNotNull()
                        .isEqualTo(expectedGetClientUserResponseDTOS[0].id()),
                () -> assertThat(clientUuidArgumentCaptor.getValue()).isNotNull()
                        .isEqualTo(expectedGetClientKeycloakClientResponseDTO.id())
        );
    }

    @Test
    public void checkAddShouldThrowServiceExceptionWhenMappingRoleToUserError() {

        when(keycloakUserFeignClientMock.getUserByUsername(any(String.class), any(Boolean.class)))
                .thenReturn(new GetClientUserResponseDTO[]{});

        PostPutUserRequestDTO inputPostPutUserRequestDTO = UserTestData.expectedSavingUserRequestDTO();
        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> userServiceImpl.add(inputPostPutUserRequestDTO));
    }

    @Test
    public void checkUpdateShouldUpdateUser() {

        PostPutUserRequestDTO inputPostPutUserRequestDTO = UserTestData.expectedUpdatingUserRequestDTO();
        String inputUserUUID = RemoteServiceTestData.USER_UUID;

        userServiceImpl.update(inputUserUUID, inputPostPutUserRequestDTO);

        verify(keycloakUserFeignClientMock)
                .updateUser(userUuidArgumentCaptor.capture(), postPutClientUserRequestDTOArgumentCaptor.capture());

        assertAll(
                () -> assertThat(userUuidArgumentCaptor.getValue())
                        .isEqualTo(inputUserUUID),
                () -> assertThat(postPutClientUserRequestDTOArgumentCaptor.getValue().username())
                        .isEqualTo(inputPostPutUserRequestDTO.username()),
                () -> assertThat(postPutClientUserRequestDTOArgumentCaptor.getValue()
                        .postPutClientCredentialRequestDTOs().get(0).value())
                        .isEqualTo(inputPostPutUserRequestDTO.password()),
                () -> assertThat(postPutClientUserRequestDTOArgumentCaptor.getValue().firstName())
                        .isEqualTo(inputPostPutUserRequestDTO.firstName()),
                () -> assertThat(postPutClientUserRequestDTOArgumentCaptor.getValue().lastName())
                        .isEqualTo(inputPostPutUserRequestDTO.lastName()),
                () -> assertThat(postPutClientUserRequestDTOArgumentCaptor.getValue().email())
                        .isEqualTo(inputPostPutUserRequestDTO.email())
        );
    }

    @Test
    public void checkRemoveShouldDeleteUser() {
        String inputUserUUID = RemoteServiceTestData.USER_UUID;

        userServiceImpl.remove(inputUserUUID);

        verify(keycloakUserFeignClientMock).deleteUser(userUuidArgumentCaptor.capture());
        assertThat(userUuidArgumentCaptor.getValue())
                .isEqualTo(inputUserUUID);
    }
}
