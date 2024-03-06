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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.clevertec.ecl.knyazev.data.domain.searching.Searching;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.response.GetCommentResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.news.request.PostPutNewsRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.news.response.GetNewsResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.pageresponse.PageDTO;
import ru.clevertec.ecl.knyazev.service.NewsService;
import ru.clevertec.ecl.knyazev.util.CommentTestData;
import ru.clevertec.ecl.knyazev.util.NewsTestData;
import ru.clevertec.ecl.knyazev.util.UrlTestData;

import java.util.UUID;
import java.util.stream.Stream;

@WebMvcTest(value = {NewsController.class},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class,
                OAuth2ClientAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class}
)
@RequiredArgsConstructor
public class NewsControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockBean
    NewsService newsServiceImplMock;

    @Test
    public void checkGetNewsShouldReturnOk() throws Exception {
        GetNewsResponseDTO expectedNewsResponseDTO =
                NewsTestData.expectedGetNewsResponseDTO();

        String newsURL = UrlTestData.getNewsRequestUrl(expectedNewsResponseDTO.uuid());

        when(newsServiceImplMock.getNewsResponseDTO(any(UUID.class)))
                .thenReturn(expectedNewsResponseDTO);

        mockMvc.perform(get(newsURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(expectedNewsResponseDTO.uuid()))
                .andExpect(jsonPath("$.authorFirstName").value(expectedNewsResponseDTO.authorFirstName()))
                .andExpect(jsonPath("$.authorLastName").value(expectedNewsResponseDTO.authorLastName()))
                .andExpect(jsonPath("$.authorEmail").value(expectedNewsResponseDTO.authorEmail()))
                .andExpect(jsonPath("$.title").value(expectedNewsResponseDTO.title()))
                .andExpect(jsonPath("$.text").value(expectedNewsResponseDTO.text()));
    }

    @ParameterizedTest
    @MethodSource("getInvalidUUIDData")
    public void checkGetNewsShouldReturnBadRequest(String invalidUUID) throws Exception {

        String newsUrl = UrlTestData.getNewsRequestUrl(invalidUUID);

        mockMvc.perform(get(newsUrl))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertThat(result.getResolvedException())
                        .isExactlyInstanceOf(HandlerMethodValidationException.class));
    }

    @Test
    public void checkGetNewsCommentsShouldReturnOkNewsCommentsDTO() throws Exception {
        PageDTO<GetCommentResponseDTO> pageGetCommentResponseDTO =
                CommentTestData.expectedPageGetCommentResponseDTO();

        String inputNewsUUID = NewsTestData.NEWS_UUID;
        String newsCommentsUrl = UrlTestData.getNewsCommentRequestUrl(inputNewsUUID);

        when(newsServiceImplMock.getNewsCommentResponseDTOs(any(UUID.class), any(Pageable.class)))
                .thenReturn(pageGetCommentResponseDTO);

        mockMvc.perform(get(newsCommentsUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.content[0].uuid").value(
                        pageGetCommentResponseDTO.getContent().get(0).uuid()))
                .andExpect(jsonPath("$.content[0].subscriberFirstName").value(
                        pageGetCommentResponseDTO.getContent().get(0).subscriberFirstName()))
                .andExpect(jsonPath("$.content[0].subscriberLastName").value(
                        pageGetCommentResponseDTO.getContent().get(0).subscriberLastName()))
                .andExpect(jsonPath("$.content[0].subscriberEmail").value(
                        pageGetCommentResponseDTO.getContent().get(0).subscriberEmail()))
                .andExpect(jsonPath("$.content[0].text").value(
                        pageGetCommentResponseDTO.getContent().get(0).text()));

    }

    @ParameterizedTest
    @MethodSource("getInvalidUUIDData")
    public void checkGetNewsCommentsShouldReturnBadRequest(String invalidNewsUUID) throws Exception {

        String newsCommentsUrl = UrlTestData.getNewsCommentRequestUrl(invalidNewsUUID);

        mockMvc.perform(get(newsCommentsUrl))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertThat(result.getResolvedException())
                        .isExactlyInstanceOf(HandlerMethodValidationException.class));
    }

    @Test
    public void checkGetNewsCommentShouldReturnNewsCommentDTO() throws Exception {
        GetCommentResponseDTO expectedGetCommentResponseDTO =
                CommentTestData.expectedGetCommentResponseDTO();

        String inputNewsUUID = NewsTestData.NEWS_UUID;
        String inputCommentUUID = CommentTestData.COMMENT_UUID;
        String inputNewsCommentUrl = UrlTestData.getNewsCommentRequestUrl(inputNewsUUID, inputCommentUUID);

        when(newsServiceImplMock.getNewsCommentResponseDTO(any(UUID.class), any(UUID.class)))
                .thenReturn(expectedGetCommentResponseDTO);

        mockMvc.perform(get(inputNewsCommentUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(expectedGetCommentResponseDTO.uuid()))
                .andExpect(jsonPath("$.subscriberFirstName").value(
                        expectedGetCommentResponseDTO.subscriberFirstName()))
                .andExpect(jsonPath("$.subscriberLastName").value(
                        expectedGetCommentResponseDTO.subscriberLastName()))
                .andExpect(jsonPath("$.subscriberEmail").value(
                        expectedGetCommentResponseDTO.subscriberEmail()))
                .andExpect(jsonPath("$.text").value(expectedGetCommentResponseDTO.text()));
    }

    @ParameterizedTest
    @MethodSource("getInvalidUUIDData")
    public void checkGetNewsCommentShouldReturnBadRequestOnInvalidNewsUUID(String invalidNewsUUID)
            throws Exception {
        String inputNewsCommentUrl = UrlTestData.getNewsCommentRequestUrl(invalidNewsUUID,
                CommentTestData.COMMENT_UUID);

        mockMvc.perform(get(inputNewsCommentUrl))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertThat(result.getResolvedException())
                        .isExactlyInstanceOf(HandlerMethodValidationException.class));
    }

    @ParameterizedTest
    @MethodSource("getInvalidUUIDData")
    public void checkGetNewsCommentShouldReturnBadRequestOnInvalidCommentsUUID(String invalidCommentsUUID)
            throws Exception {
        String inputNewsCommentUrl = UrlTestData.getNewsCommentRequestUrl(NewsTestData.NEWS_UUID,
                invalidCommentsUUID);

        mockMvc.perform(get(inputNewsCommentUrl))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertThat(result.getResolvedException())
                        .isExactlyInstanceOf(HandlerMethodValidationException.class));
    }

    @Test
    public void checkGetAllNewsShouldReturnNewsDTOs() throws Exception {
        PageDTO<GetNewsResponseDTO> expectedPageGetNewsResponseDTOS =
                NewsTestData.expectedPageGetNewsResponseDTO();

        String inputNewsURL = UrlTestData.getNewsRequestUrl();
        String inputSearching = NewsTestData.NEWS_SEARCHING_FIELD;

        when(newsServiceImplMock.getAllNewsDTO(any(Pageable.class), any(Searching.class)))
                .thenReturn(expectedPageGetNewsResponseDTOS);

        mockMvc.perform(get(inputNewsURL)
                        .queryParam(UrlTestData.SEARCH_PARAM, inputSearching))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].uuid").value(
                        expectedPageGetNewsResponseDTOS.getContent().get(0).uuid()))
                .andExpect(jsonPath("$.content[0].authorFirstName").value(
                        expectedPageGetNewsResponseDTOS.getContent().get(0).authorFirstName()))
                .andExpect(jsonPath("$.content[0].authorLastName").value(
                        expectedPageGetNewsResponseDTOS.getContent().get(0).authorLastName()))
                .andExpect(jsonPath("$.content[0].authorEmail").value(
                        expectedPageGetNewsResponseDTOS.getContent().get(0).authorEmail()))
                .andExpect(jsonPath("$.content[0].title").value(
                        expectedPageGetNewsResponseDTOS.getContent().get(0).title()))
                .andExpect(jsonPath("$.content[0].text").value(
                        expectedPageGetNewsResponseDTOS.getContent().get(0).text()));
    }

    @Test
    public void checkSaveNewsShouldReturnCreated() throws Exception {

        GetNewsResponseDTO expectedNewsResponseDTO =
                NewsTestData.expectedGetNewsResponseDTO();

        String newsUrl = UrlTestData.getNewsRequestUrl();

        String jsonNewsInput =
                objectMapper.writeValueAsString(NewsTestData.expectedPostPutNewsRequestDTO());

        when(newsServiceImplMock.add(any(PostPutNewsRequestDTO.class)))
                .thenReturn(expectedNewsResponseDTO);

        mockMvc.perform(post(newsUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNewsInput))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").value(expectedNewsResponseDTO.uuid()))
                .andExpect(jsonPath("$.authorFirstName").value(expectedNewsResponseDTO.authorFirstName()))
                .andExpect(jsonPath("$.authorLastName").value(expectedNewsResponseDTO.authorLastName()))
                .andExpect(jsonPath("$.authorEmail").value(expectedNewsResponseDTO.authorEmail()))
                .andExpect(jsonPath("$.title").value(expectedNewsResponseDTO.title()))
                .andExpect(jsonPath("$.text").value(expectedNewsResponseDTO.text()));
    }

    @ParameterizedTest
    @MethodSource("getInvalidPostPutNewsRequestDTOs")
    public void checkSaveNewsShouldReturnBadRequest(PostPutNewsRequestDTO invalidPostNewsRequestDTO)
            throws Exception {
        String newsUrl = UrlTestData.getNewsRequestUrl();

        String invalidPostNewsRequestDTOJson = objectMapper.writeValueAsString(
                invalidPostNewsRequestDTO);

        mockMvc.perform(post(newsUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPostNewsRequestDTOJson))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertThat(result.getResolvedException())
                        .isExactlyInstanceOf(MethodArgumentNotValidException.class));
    }

    @Test
    public void checkChangeNewsShouldReturnOk() throws Exception {
        GetNewsResponseDTO expectedNewsResponseDTO =
                NewsTestData.expectedGetNewsResponseDTO();

        String newsUrl = UrlTestData.getNewsRequestUrl(NewsTestData.NEWS_UUID);

        String jsonNewsInput =
                objectMapper.writeValueAsString(NewsTestData.expectedPostPutNewsRequestDTO());

        when(newsServiceImplMock.update(any(UUID.class), any(PostPutNewsRequestDTO.class)))
                .thenReturn(expectedNewsResponseDTO);

        mockMvc.perform(put(newsUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonNewsInput))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(expectedNewsResponseDTO.uuid()))
                .andExpect(jsonPath("$.authorFirstName").value(expectedNewsResponseDTO.authorFirstName()))
                .andExpect(jsonPath("$.authorLastName").value(expectedNewsResponseDTO.authorLastName()))
                .andExpect(jsonPath("$.authorEmail").value(expectedNewsResponseDTO.authorEmail()))
                .andExpect(jsonPath("$.title").value(expectedNewsResponseDTO.title()))
                .andExpect(jsonPath("$.text").value(expectedNewsResponseDTO.text()));
    }

    @ParameterizedTest
    @MethodSource("getInvalidPostPutNewsRequestDTOs")
    public void checkUpdateNewsShouldReturnBadRequest(PostPutNewsRequestDTO invalidPutNewsRequestDTO)
            throws Exception {
        String newsUrl = UrlTestData.getNewsRequestUrl(NewsTestData.NEWS_UUID);

        String invalidPostNewsRequestDTOJson = objectMapper.writeValueAsString(
                invalidPutNewsRequestDTO);

        mockMvc.perform(put(newsUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPostNewsRequestDTOJson))
                .andExpect(status().isBadRequest())
                .andDo(result -> assertThat(result.getResolvedException())
                        .isExactlyInstanceOf(MethodArgumentNotValidException.class));
    }

    @Test
    public void checkDeleteNewsShouldReturnNoContent() throws Exception {
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        String deletingUUID = NewsTestData.NEWS_DELETING_UUID;
        String deletingNewsUrl = UrlTestData.getNewsRequestUrl(
                deletingUUID);

        mockMvc.perform(delete(deletingNewsUrl))
                .andExpect(status().isNoContent());

        verify(newsServiceImplMock).remove(uuidArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(UUID.fromString(deletingUUID));
    }

    @ParameterizedTest
    @MethodSource("getInvalidUUIDData")
    public void checkDeleteNewsShouldReturnBadRequestStatus(String invalidNewsUUID)
            throws Exception {

        String invalidDeletingNewsUrl = UrlTestData.getNewsRequestUrl(invalidNewsUUID);

        mockMvc.perform(delete(invalidDeletingNewsUrl))
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

    private static Stream<PostPutNewsRequestDTO> getInvalidPostPutNewsRequestDTOs() {
        return Stream.of(
                //invalid title
                PostPutNewsRequestDTO.builder()
                        .title(null)
                        .text("Good news!")
                        .build(),
                PostPutNewsRequestDTO.builder()
                        .title("   ")
                        .text("Good news!")
                        .build(),
                PostPutNewsRequestDTO.builder()
                        .title("Ne")
                        .text("Good news!")
                        .build(),
                PostPutNewsRequestDTO.builder()
                        .title(RandomStringUtils.randomAlphabetic(151))
                        .text("Good news!")
                        .build(),
                //invalid text
                PostPutNewsRequestDTO.builder()
                        .title("Hello news!")
                        .text(null)
                        .build(),
                PostPutNewsRequestDTO.builder()
                        .title("Hello news!")
                        .text("   ")
                        .build(),
                PostPutNewsRequestDTO.builder()
                        .title("Hello news!")
                        .text("No")
                        .build(),
                PostPutNewsRequestDTO.builder()
                        .title("Hello news!")
                        .text(RandomStringUtils.randomAlphabetic(200_001))
                        .build()
        );
    }
}
