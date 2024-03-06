package ru.clevertec.ecl.knyazev.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.clevertec.ecl.knyazev.data.http.owner.user.request.PostPutUserRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.user.response.GetUserResponseDTO;
import ru.clevertec.ecl.knyazev.service.UserService;
import ru.clevertec.ecl.knyazev.util.UrlTestData;
import ru.clevertec.ecl.knyazev.util.UserTestData;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(value = {UserController.class},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class,
                OAuth2ClientAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class}
)
@RequiredArgsConstructor
public class UserControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockBean
    UserService userServiceImplMock;

    @Test
    public void checkGetUserShouldReturnOk() throws Exception {
        GetUserResponseDTO expectedGetUserResponseDTO =
                UserTestData.expectedUserResponseDTO();

        String userUrl = UrlTestData.getUserRequestUrl(expectedGetUserResponseDTO.uuid());

        when(userServiceImplMock.getUserResponseDTO(any(String.class)))
                .thenReturn(expectedGetUserResponseDTO);

        mockMvc.perform(get(userUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(expectedGetUserResponseDTO.uuid()))
                .andExpect(jsonPath("$.firstName").value(expectedGetUserResponseDTO.firstName()))
                .andExpect(jsonPath("$.lastName").value(expectedGetUserResponseDTO.lastName()))
                .andExpect(jsonPath("$.email").value(expectedGetUserResponseDTO.email()));
    }

    @ParameterizedTest
    @MethodSource("getInvalidUserUUIDData")
    public void checkGetUserShouldReturnBadRequest(String invalidUUID) throws Exception {

        String userUrl = UrlTestData.getUserRequestUrl(invalidUUID);

        mockMvc.perform(get(userUrl))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertThat(result.getResolvedException())
                        .isExactlyInstanceOf(HandlerMethodValidationException.class));
    }

    @Test
    public void checkGetAllUsersShouldReturnOk() throws Exception {
        List<GetUserResponseDTO> expectedUserResponseDTOs = UserTestData.expectedUserResponseDTOs();

        String userUrl = UrlTestData.getUserRequestUrl();

        when(userServiceImplMock.getUserResponseDTOs())
                .thenReturn(expectedUserResponseDTOs);

        mockMvc.perform(get(userUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    public void checkSaveUserShouldReturnCreated() throws Exception {
        String userRequestUrl = UrlTestData.getUserRequestUrl();

        doNothing().when(userServiceImplMock).add(any(PostPutUserRequestDTO.class));

        String jsonUserInput = objectMapper.writeValueAsString(UserTestData.expectedSavingUserRequestDTO());

        mockMvc.perform(post(userRequestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserInput))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("getInvalidPostUserRequestDTOs")
    public void checkSaveUserShouldReturnBadRequest(PostPutUserRequestDTO invalidPostUserRequestDTO)
            throws Exception {
        String userUrl = UrlTestData.getUserRequestUrl();

        String invalidPostUserRequestDTOJson = objectMapper.writeValueAsString(
                invalidPostUserRequestDTO);

        mockMvc.perform(post(userUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPostUserRequestDTOJson))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertThat(result.getResolvedException())
                        .isExactlyInstanceOf(MethodArgumentNotValidException.class));
    }

    @Test
    public void checkUpdateUserShouldReturnOk() throws Exception {
        String inputUserUUID = UserTestData.USER_UUID;
        String userRequestUrl = UrlTestData.getUserRequestUrl(inputUserUUID);

        doNothing().when(userServiceImplMock).update(any(String.class), any(PostPutUserRequestDTO.class));

        String jsonUserInput = objectMapper.writeValueAsString(UserTestData.expectedUpdatingUserRequestDTO());

        mockMvc.perform(put(userRequestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserInput))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("getInvalidPutUserRequestDTOs")
    public void checkUpdateUserShouldReturnBadRequest(PostPutUserRequestDTO invalidPutUserRequestDTO) throws Exception {
        String inputUserUUID = UserTestData.USER_UUID;
        String userUrl = UrlTestData.getUserRequestUrl(inputUserUUID);

        String invalidPostUserRequestDTOJson = objectMapper.writeValueAsString(
                invalidPutUserRequestDTO);

        mockMvc.perform(put(userUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPostUserRequestDTOJson))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertThat(result.getResolvedException())
                        .isExactlyInstanceOf(MethodArgumentNotValidException.class));
    }

    @Test
    public void checkDeleteUserShouldReturnNoContent() throws Exception {
        ArgumentCaptor<String> uuidArgumentCaptor = ArgumentCaptor.forClass(String.class);

        String expectedDeletingUserUUID = UserTestData.USER_UUID;
        String deletingUserUrl = UrlTestData.getUserRequestUrl(
                expectedDeletingUserUUID);

        mockMvc.perform(delete(deletingUserUrl))
                .andExpect(status().isNoContent());

        verify(userServiceImplMock).remove(uuidArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(expectedDeletingUserUUID);
    }

    @ParameterizedTest
    @MethodSource("getInvalidUserUUIDData")
    public void checkDeleteUserShouldReturnBadRequestStatus(String invalidPersonUUID)
            throws Exception {

        String invalidDeletingPersonUrl = UrlTestData.getUserRequestUrl(invalidPersonUUID);

        mockMvc.perform(delete(invalidDeletingPersonUrl))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertThat(result.getResolvedException())
                        .isExactlyInstanceOf(HandlerMethodValidationException.class));
    }

    private static Stream<String> getInvalidUserUUIDData() {
        return Stream.of(
                null,
                "-1",
                " ",
                "   ",
                System.lineSeparator(),
                "128-568-325"
        );
    }

    private static Stream<PostPutUserRequestDTO> getInvalidPostUserRequestDTOs() {
        return Stream.concat(
                getInvalidPostPutUserRequestDTOs(),
                Stream.of(
                        //Invalid roles
                        PostPutUserRequestDTO.builder()
                                .username("Zina")
                                .password("12345")
                                .firstName("Zina")
                                .lastName("Parovoz")
                                .email("zina@mail.ru")
                                .build(),
                        PostPutUserRequestDTO.builder()
                                .username("Zina")
                                .password("12345")
                                .firstName("Zina")
                                .lastName("Parovoz")
                                .email("zina@mail.ru")
                                .roles(List.of("     "))
                                .build(),
                        PostPutUserRequestDTO.builder()
                                .username("Zina")
                                .password("12345")
                                .firstName("Zina")
                                .lastName("Parovoz")
                                .email("zina@mail.ru")
                                .roles(List.of("ROLE_MANAGER"))
                                .build()
                )
        );
    }

    private static Stream<PostPutUserRequestDTO> getInvalidPutUserRequestDTOs() {
        return Stream.concat(
                getInvalidPostPutUserRequestDTOs(),
                Stream.of(
                        PostPutUserRequestDTO.builder()
                                .username("zina")
                                .password("12345")
                                .firstName("Zina")
                                .lastName("Parovoz")
                                .email("zina@mail.ru")
                                .roles(Collections.emptyList())
                                .build(),
                        PostPutUserRequestDTO.builder()
                                .username("zina")
                                .password("12345")
                                .firstName("Zina")
                                .lastName("Parovoz")
                                .email("zina@mail.ru")
                                .roles(List.of(" "))
                                .build(),
                        PostPutUserRequestDTO.builder()
                                .username("zina")
                                .password("12345")
                                .firstName("Zina")
                                .lastName("Parovoz")
                                .email("zina@mail.ru")
                                .roles(List.of(""))
                                .build(),
                        PostPutUserRequestDTO.builder()
                                .username("zina")
                                .password("12345")
                                .firstName("Zina")
                                .lastName("Parovoz")
                                .email("zina@mail.ru")
                                .roles(List.of("ROLE_MANAGER"))
                                .build()
                )
        );
    }

    private static Stream<PostPutUserRequestDTO> getInvalidPostPutUserRequestDTOs() {
        return Stream.of(
                //invalid username
                PostPutUserRequestDTO.builder()
                        .password("12345")
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username(null)
                        .password("12345")
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("   ")
                        .password("12345")
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Na")
                        .password("12345")
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Nasha zina avtor")
                        .password("12345")
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                //invalid password
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password(null)
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("     ")
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("1234")
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("123456789111111111121")
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                //invalid firstName
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .lastName("Parovoz")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .firstName(null)
                        .lastName("Parovoz")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .firstName("   ")
                        .lastName("Parovoz")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .firstName("Na")
                        .lastName("Parovoz")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .firstName("Zinaida Pervichn")
                        .lastName("Parovoz")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                //invalid lastName
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .firstName("Zina")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .firstName("Zina")
                        .lastName(null)
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .firstName("Zina")
                        .lastName("   ")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .firstName("Zina")
                        .lastName("Tu")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .firstName("Zina")
                        .lastName("Parovoz Pervogod")
                        .email("zina@mail.ru")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                //invalid email
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .email(null)
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .email("   ")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .email("1@1")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .email("1@1.by")
                        .roles(List.of("ROLE_MANAGER"))
                        .build(),
                PostPutUserRequestDTO.builder()
                        .username("Zina")
                        .password("12345")
                        .firstName("Zina")
                        .lastName("Parovoz")
                        .email("dsfs5s@")
                        .roles(List.of("ROLE_MANAGER"))
                        .build()
        );
    }
}
