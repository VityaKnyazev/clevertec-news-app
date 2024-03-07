package ru.clevertec.ecl.knyazev.util;

import ru.clevertec.ecl.knyazev.data.http.owner.user.request.PostPutUserRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.user.response.GetUserResponseDTO;

import java.util.List;

/**
 * Represents test data for User
 */
public class UserTestData {
    public static String USER_UUID = "32e48816-5d1f-4b3d-a4c9-622f35f2fc1c";
    public static String USER_SUBSCRIBER_UUID = "b0436b81-7336-4e66-a7e2-ba009a3767b0";
    public static String USER_NAME = "misha";
    public static String USER_PASSWORD = "25Asd5K";
    public static String USER_FIRST_NAME = "Misha";
    public static String USER_LAST_NAME = "Mad";
    public static String USER_EMAIL = "misha@mail.ru";
    public static String USER_ROLE = "ROLE_JOURNALIST";

    public static GetUserResponseDTO expectedSubscriberResponseDTO() {
        return GetUserResponseDTO.builder()
                .uuid(USER_SUBSCRIBER_UUID)
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(USER_EMAIL)
                .build();
    }

    public static GetUserResponseDTO expectedUserResponseDTO() {
        return GetUserResponseDTO.builder()
                .uuid(USER_UUID)
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(USER_EMAIL)
                .build();
    }

    public static List<GetUserResponseDTO> expectedUserResponseDTOs() {
        return List.of(
                GetUserResponseDTO.builder()
                        .uuid(USER_UUID)
                        .firstName(USER_FIRST_NAME)
                        .lastName(USER_LAST_NAME)
                        .email(USER_EMAIL)
                        .build()
        );
    }

    public static PostPutUserRequestDTO expectedSavingUserRequestDTO() {
        return PostPutUserRequestDTO.builder()
                .username(USER_NAME)
                .password(USER_PASSWORD)
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(USER_EMAIL)
                .roles(List.of(USER_ROLE))
                .build();
    }

    public static PostPutUserRequestDTO expectedUpdatingUserRequestDTO() {
        return PostPutUserRequestDTO.builder()
                .username(USER_NAME)
                .password(USER_PASSWORD)
                .firstName(USER_FIRST_NAME)
                .lastName(USER_LAST_NAME)
                .email(USER_EMAIL)
                .build();
    }
}
