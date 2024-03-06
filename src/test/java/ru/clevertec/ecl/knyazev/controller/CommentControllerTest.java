package ru.clevertec.ecl.knyazev.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.clevertec.ecl.knyazev.data.domain.searching.Searching;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.request.PostPutCommentRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.response.GetCommentResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.pageresponse.PageDTO;
import ru.clevertec.ecl.knyazev.service.CommentService;
import ru.clevertec.ecl.knyazev.util.CommentTestData;
import ru.clevertec.ecl.knyazev.util.UrlTestData;

import java.util.UUID;
import java.util.stream.Stream;

@WebMvcTest(value = {CommentController.class},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class,
                OAuth2ClientAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class}
)
@RequiredArgsConstructor
public class CommentControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockBean
    CommentService commentServiceImplMock;

    @Test
    public void checkGetCommentShouldReturnOk() throws Exception {
        GetCommentResponseDTO expectedCommentResponseDTO =
                CommentTestData.expectedGetCommentResponseDTO();

        String commentURL = UrlTestData.getCommentRequestUrl(expectedCommentResponseDTO.uuid());

        when(commentServiceImplMock.getCommentResponseDTO(any(UUID.class)))
                .thenReturn(expectedCommentResponseDTO);

        mockMvc.perform(get(commentURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(expectedCommentResponseDTO.uuid()))
                .andExpect(jsonPath("$.subscriberFirstName").value(
                        expectedCommentResponseDTO.subscriberFirstName()))
                .andExpect(jsonPath("$.subscriberLastName").value(
                        expectedCommentResponseDTO.subscriberLastName()))
                .andExpect(jsonPath("$.subscriberEmail").value(
                        expectedCommentResponseDTO.subscriberEmail()))
                .andExpect(jsonPath("$.text").value(expectedCommentResponseDTO.text()));
    }

    @ParameterizedTest
    @MethodSource("getInvalidUUIDData")
    public void checkGetCommentShouldReturnBadRequest(String invalidUUID) throws Exception {

        String commentUrl = UrlTestData.getCommentRequestUrl(invalidUUID);

        mockMvc.perform(get(commentUrl))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertThat(result.getResolvedException())
                        .isExactlyInstanceOf(HandlerMethodValidationException.class));
    }

    @Test
    public void checkGetAllCommentsShouldReturnCommentDTOs() throws Exception {
        PageDTO<GetCommentResponseDTO> expectedPageGetCommentResponseDTOS =
                CommentTestData.expectedPageGetCommentResponseDTO();

        String inputCommentURL = UrlTestData.getCommentRequestUrl();
        String inputSearching = CommentTestData.COMMENT_SEARCHING_FIELD;

        when(commentServiceImplMock.getAllCommentDTO(any(Pageable.class), any(Searching.class)))
                .thenReturn(expectedPageGetCommentResponseDTOS);

        mockMvc.perform(get(inputCommentURL)
                        .queryParam(UrlTestData.SEARCH_PARAM, inputSearching))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].uuid").value(
                        expectedPageGetCommentResponseDTOS.getContent().get(0).uuid()))
                .andExpect(jsonPath("$.content[0].subscriberFirstName").value(
                        expectedPageGetCommentResponseDTOS.getContent().get(0).subscriberFirstName()))
                .andExpect(jsonPath("$.content[0].subscriberLastName").value(
                        expectedPageGetCommentResponseDTOS.getContent().get(0).subscriberLastName()))
                .andExpect(jsonPath("$.content[0].subscriberEmail").value(
                        expectedPageGetCommentResponseDTOS.getContent().get(0).subscriberEmail()))
                .andExpect(jsonPath("$.content[0].text").value(
                        expectedPageGetCommentResponseDTOS.getContent().get(0).text()));
    }

    @Test
    public void checkSaveCommentShouldReturnCreated() throws Exception {

        GetCommentResponseDTO expectedCommentResponseDTO =
                CommentTestData.expectedGetCommentResponseDTO();

        String commentUrl = UrlTestData.getCommentRequestUrl();

        String jsonCommentInput =
                objectMapper.writeValueAsString(CommentTestData.expectedPostPutCommentRequestDTO());

        when(commentServiceImplMock.add(any(PostPutCommentRequestDTO.class)))
                .thenReturn(expectedCommentResponseDTO);

        mockMvc.perform(post(commentUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommentInput))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").value(expectedCommentResponseDTO.uuid()))
                .andExpect(jsonPath("$.subscriberFirstName").value(
                        expectedCommentResponseDTO.subscriberFirstName()))
                .andExpect(jsonPath("$.subscriberLastName").value(
                        expectedCommentResponseDTO.subscriberLastName()))
                .andExpect(jsonPath("$.subscriberEmail").value(
                        expectedCommentResponseDTO.subscriberEmail()))
                .andExpect(jsonPath("$.text").value(expectedCommentResponseDTO.text()));
    }

    @ParameterizedTest
    @MethodSource("getInvalidPostPutCommentRequestDTOs")
    public void checkSaveCommentShouldReturnBadRequest(PostPutCommentRequestDTO invalidPostCommentRequestDTO)
            throws Exception {
        String commentUrl = UrlTestData.getCommentRequestUrl();

        String invalidPostCommentRequestDTOJson = objectMapper.writeValueAsString(
                invalidPostCommentRequestDTO);

        mockMvc.perform(post(commentUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPostCommentRequestDTOJson))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertThat(result.getResolvedException())
                        .isExactlyInstanceOf(MethodArgumentNotValidException.class));
    }

    @Test
    public void checkChangeCommentShouldReturnOk() throws Exception {
        GetCommentResponseDTO expectedCommentResponseDTO =
                CommentTestData.expectedGetCommentResponseDTO();

        String commentUrl = UrlTestData.getCommentRequestUrl(CommentTestData.COMMENT_UUID);

        String jsonCommentInput =
                objectMapper.writeValueAsString(CommentTestData.expectedPostPutCommentRequestDTO());

        when(commentServiceImplMock.update(any(UUID.class), any(PostPutCommentRequestDTO.class)))
                .thenReturn(expectedCommentResponseDTO);

        mockMvc.perform(put(commentUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommentInput))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(expectedCommentResponseDTO.uuid()))
                .andExpect(jsonPath("$.subscriberFirstName").value(
                        expectedCommentResponseDTO.subscriberFirstName()))
                .andExpect(jsonPath("$.subscriberLastName").value(
                        expectedCommentResponseDTO.subscriberLastName()))
                .andExpect(jsonPath("$.subscriberEmail").value(
                        expectedCommentResponseDTO.subscriberEmail()))
                .andExpect(jsonPath("$.text").value(expectedCommentResponseDTO.text()));
    }

    @ParameterizedTest
    @MethodSource("getInvalidPostPutCommentRequestDTOs")
    public void checkUpdateCommentShouldReturnBadRequest(PostPutCommentRequestDTO invalidPutCommentRequestDTO)
            throws Exception {
        String commentUrl = UrlTestData.getCommentRequestUrl(CommentTestData.COMMENT_UUID);

        String invalidPostCommentRequestDTOJson = objectMapper.writeValueAsString(
                invalidPutCommentRequestDTO);

        mockMvc.perform(put(commentUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPostCommentRequestDTOJson))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertThat(result.getResolvedException())
                        .isExactlyInstanceOf(MethodArgumentNotValidException.class));
    }

    @Test
    public void checkDeleteCommentShouldReturnNoContent() throws Exception {
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        String deletingUUID = CommentTestData.COMMENT_DELETING_UUID;
        String deletingCommentUrl = UrlTestData.getCommentRequestUrl(
                deletingUUID);

        mockMvc.perform(delete(deletingCommentUrl))
                .andExpect(status().isNoContent());

        verify(commentServiceImplMock).remove(uuidArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(UUID.fromString(deletingUUID));
    }

    @ParameterizedTest
    @MethodSource("getInvalidUUIDData")
    public void checkDeleteCommentShouldReturnBadRequestStatus(String invalidCommentUUID)
            throws Exception {

        String invalidDeletingCommentUrl = UrlTestData.getCommentRequestUrl(invalidCommentUUID);

        mockMvc.perform(delete(invalidDeletingCommentUrl))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertThat(result.getResolvedException())
                        .isExactlyInstanceOf(HandlerMethodValidationException.class));
    }

    private static Stream<String> getInvalidUUIDData() {
        return Stream.of(
                null,
                "-1",
                " ",
                "   ",
                System.lineSeparator(),
                "128-568-325"
        );
    }

    private static Stream<PostPutCommentRequestDTO> getInvalidPostPutCommentRequestDTOs() {
        return Stream.of(
                //invalid text
                PostPutCommentRequestDTO.builder()
                        .text(null)
                        .newsUUID("926a53df-efc4-4afd-bd6e-33a1e7c0832b")
                        .build(),
                PostPutCommentRequestDTO.builder()
                        .text("  ")
                        .newsUUID("926a53df-efc4-4afd-bd6e-33a1e7c0832b")
                        .build(),
                PostPutCommentRequestDTO.builder()
                        .text("No")
                        .newsUUID("926a53df-efc4-4afd-bd6e-33a1e7c0832b")
                        .build(),
                PostPutCommentRequestDTO.builder()
                        .text(RandomStringUtils.randomAlphabetic(801))
                        .newsUUID("926a53df-efc4-4afd-bd6e-33a1e7c0832b")
                        .build(),
                //Invalid news uuid
                PostPutCommentRequestDTO.builder()
                        .text("Замечательный комент!")
                        .newsUUID(null)
                        .build(),
                PostPutCommentRequestDTO.builder()
                        .text("Замечательный комент!")
                        .newsUUID(" ")
                        .build(),
                PostPutCommentRequestDTO.builder()
                        .text("Замечательный комент!")
                        .newsUUID("a-a-5568-s4-s")
                        .build(),
                PostPutCommentRequestDTO.builder()
                        .text("Замечательный комент!")
                        .newsUUID("12sd1s555")
                        .build()
        );
    }
}
