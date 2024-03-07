package ru.clevertec.ecl.knyazev.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import ru.clevertec.ecl.knyazev.data.domain.searching.Searching;
import ru.clevertec.ecl.knyazev.data.domain.searching.impl.SearchingImpl;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.response.GetCommentResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.news.request.PostPutNewsRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.news.response.GetNewsResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.pageresponse.PageDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.user.response.GetUserResponseDTO;
import ru.clevertec.ecl.knyazev.entity.News;
import ru.clevertec.ecl.knyazev.mapper.CommentMapper;
import ru.clevertec.ecl.knyazev.mapper.CommentMapperImpl;
import ru.clevertec.ecl.knyazev.mapper.NewsMapper;
import ru.clevertec.ecl.knyazev.mapper.NewsMapperImpl;
import ru.clevertec.ecl.knyazev.repository.NewsRepository;
import ru.clevertec.ecl.knyazev.service.UserService;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;
import ru.clevertec.ecl.knyazev.util.CommentTestData;
import ru.clevertec.ecl.knyazev.util.NewsTestData;
import ru.clevertec.ecl.knyazev.util.UserTestData;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class NewsServiceImplTest {

    @Mock
    private NewsRepository newsRepositoryMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private SecurityContext securityContextMock;

    @Spy
    private NewsMapper newsMapperImpl = new NewsMapperImpl();
    @Spy
    private CommentMapper commentMapperImpl = new CommentMapperImpl();

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @InjectMocks
    private NewsServiceImpl newsServiceImpl;

    @Test
    public void checkGetNewsShouldReturnNews() {
        News expectedNews = NewsTestData.expectedNews();

        when(newsRepositoryMock.findByUuid(any(UUID.class)))
                .thenReturn(Optional.of(expectedNews));

        UUID inputUUID = UUID.fromString(NewsTestData.NEWS_UUID);
        News actualNews = newsServiceImpl.getNews(inputUUID);

        assertThat(actualNews).isEqualTo(expectedNews);
    }

    @Test
    public void checkGetNewsShouldThrowServiceException() {
        when(newsRepositoryMock.findByUuid(any(UUID.class)))
                .thenReturn(Optional.empty());

        UUID inputUUID = UUID.fromString(NewsTestData.NEWS_INVALID_UUID);
        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> newsServiceImpl.getNews(inputUUID));
    }

    @Test
    public void checkGetNewsResponseDTOShouldReturnGetNewsResponseDTO() {
        News expectedNews = NewsTestData.expectedNews();
        GetUserResponseDTO expectedGetUserResponseDTO =
                UserTestData.expectedUserResponseDTO();

        when(newsRepositoryMock.findByUuid(any(UUID.class)))
                .thenReturn(Optional.of(expectedNews));
        when(userServiceMock.getUserResponseDTO(any(String.class)))
                .thenReturn(expectedGetUserResponseDTO);


        UUID inputUUID = UUID.fromString(NewsTestData.NEWS_UUID);
        GetNewsResponseDTO actualGetNewsResponseDTO =
                newsServiceImpl.getNewsResponseDTO(inputUUID);
        assertAll(
                () -> assertThat(actualGetNewsResponseDTO.uuid())
                        .isEqualTo(expectedNews.getUuid().toString()),
                () -> assertThat(actualGetNewsResponseDTO.authorFirstName())
                        .isEqualTo(expectedGetUserResponseDTO.firstName()),
                () -> assertThat(actualGetNewsResponseDTO.authorLastName())
                        .isEqualTo(expectedGetUserResponseDTO.lastName()),
                () -> assertThat(actualGetNewsResponseDTO.authorEmail())
                        .isEqualTo(expectedGetUserResponseDTO.email()),
                () -> assertThat(actualGetNewsResponseDTO.title())
                        .isEqualTo(expectedNews.getTitle()),
                () -> assertThat(actualGetNewsResponseDTO.text())
                        .isEqualTo(expectedNews.getText())
        );
    }

    @Test
    public void checkGetNewsResponseDTOShouldThrowServiceException() {
        when(newsRepositoryMock.findByUuid(any(UUID.class)))
                .thenReturn(Optional.empty());

        UUID inputUUID = UUID.fromString(NewsTestData.NEWS_INVALID_UUID);
        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> newsServiceImpl.getNewsResponseDTO(inputUUID));
    }

    @Test
    public void checkGetNewsCommentResponseDTOShouldReturnNewsCommentDTO() {
        News expectedNewsWithComments = NewsTestData.expectedNewsWithComments();
        GetUserResponseDTO expectedGetUserResponseDTO =
                UserTestData.expectedSubscriberResponseDTO();

        when(newsRepositoryMock.findWithCommentsByUuid(any(UUID.class)))
                .thenReturn(Optional.of(expectedNewsWithComments));
        when(userServiceMock.getUserResponseDTO(anyString()))
                .thenReturn(expectedGetUserResponseDTO);

        UUID inputNewsUUID = UUID.fromString(NewsTestData.NEWS_UUID);
        UUID inputCommentUUID = UUID.fromString(CommentTestData.COMMENT_UUID);

        GetCommentResponseDTO actualGetCommentResponseDTO =
                newsServiceImpl.getNewsCommentResponseDTO(inputNewsUUID,
                        inputCommentUUID);

        assertAll(
                () -> assertThat(actualGetCommentResponseDTO.uuid())
                        .isEqualTo(expectedNewsWithComments.getComments().get(0).getUuid().toString()),
                () -> assertThat(actualGetCommentResponseDTO.subscriberFirstName())
                        .isEqualTo(expectedGetUserResponseDTO.firstName()),
                () -> assertThat(actualGetCommentResponseDTO.subscriberLastName())
                        .isEqualTo(expectedGetUserResponseDTO.lastName()),
                () -> assertThat(actualGetCommentResponseDTO.subscriberEmail())
                        .isEqualTo(expectedGetUserResponseDTO.email()),
                () -> assertThat(actualGetCommentResponseDTO.text())
                        .isEqualTo(expectedNewsWithComments.getComments().get(0).getText())
        );
    }

    @Test
    public void checkGetNewsCommentResponseDTOShouldThrowServiceException() {
        when(newsRepositoryMock.findWithCommentsByUuid(any(UUID.class)))
                .thenReturn(Optional.empty());

        UUID inputNewsUUID = UUID.fromString(NewsTestData.NEWS_INVALID_UUID);
        UUID inputCommentUUID = UUID.fromString(CommentTestData.COMMENT_UUID);
        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> newsServiceImpl.getNewsCommentResponseDTO(inputNewsUUID,
                        inputCommentUUID));
    }

    @Test
    public void checkGetAllNewsDTOShouldReturnNewsDTO() {
        Page<News> expectedPageNews =
                NewsTestData.expectedPageNews();
        GetUserResponseDTO expectedGetUserResponseDTO = UserTestData.expectedUserResponseDTO();

        when(newsRepositoryMock.findAll(any(Pageable.class), any(Searching.class)))
                .thenReturn(expectedPageNews);
        when(userServiceMock.getUserResponseDTO(any(String.class)))
                .thenReturn(expectedGetUserResponseDTO);

        Pageable inputPageable = Pageable.unpaged();
        Searching inputSearching = new SearchingImpl(null);
        PageDTO<GetNewsResponseDTO> actualGetNewsResponseDTO =
                newsServiceImpl.getAllNewsDTO(inputPageable, inputSearching);

        assertAll(
                () -> assertThat(actualGetNewsResponseDTO.getContent().get(0).uuid())
                        .isEqualTo(expectedPageNews.getContent().get(0).getUuid().toString()),
                () -> assertThat(actualGetNewsResponseDTO.getContent().get(0).title())
                        .isEqualTo(expectedPageNews.getContent().get(0).getTitle()),
                () -> assertThat(actualGetNewsResponseDTO.getContent().get(0).text())
                        .isEqualTo(expectedPageNews.getContent().get(0).getText()),
                () -> assertThat(actualGetNewsResponseDTO.getContent().get(0).authorFirstName())
                        .isEqualTo(expectedGetUserResponseDTO.firstName()),
                () -> assertThat(actualGetNewsResponseDTO.getContent().get(0).authorLastName())
                        .isEqualTo(expectedGetUserResponseDTO.lastName()),
                () -> assertThat(actualGetNewsResponseDTO.getContent().get(0).authorEmail())
                        .isEqualTo(expectedGetUserResponseDTO.email())
        );
    }

    @Test
    public void checkGetNewsCommentResponseDTOsShouldReturnNewsCommentsDTO() {
        News expectedNewsWithComments = NewsTestData.expectedNewsWithComments();
        GetUserResponseDTO expectedGetUserResponseDTO =
                UserTestData.expectedSubscriberResponseDTO();

        when(newsRepositoryMock.findWithCommentsByUuid(any(UUID.class)))
                .thenReturn(Optional.of(expectedNewsWithComments));
        when(userServiceMock.getUserResponseDTO(anyString()))
                .thenReturn(expectedGetUserResponseDTO);

        UUID inputNewsUUID = UUID.fromString(NewsTestData.NEWS_UUID);
        Pageable inputPageable = Pageable.unpaged();


        PageDTO<GetCommentResponseDTO> actualPageGetCommentResponseDTO =
                newsServiceImpl.getNewsCommentResponseDTOs(inputNewsUUID,
                        inputPageable);

        assertAll(
                () -> assertThat(actualPageGetCommentResponseDTO.getContent().get(0).uuid())
                        .isEqualTo(expectedNewsWithComments.getComments().get(0).getUuid().toString()),
                () -> assertThat(actualPageGetCommentResponseDTO.getContent().get(0).subscriberFirstName())
                        .isEqualTo(expectedGetUserResponseDTO.firstName()),
                () -> assertThat(actualPageGetCommentResponseDTO.getContent().get(0).subscriberLastName())
                        .isEqualTo(expectedGetUserResponseDTO.lastName()),
                () -> assertThat(actualPageGetCommentResponseDTO.getContent().get(0).subscriberEmail())
                        .isEqualTo(expectedGetUserResponseDTO.email()),
                () -> assertThat(actualPageGetCommentResponseDTO.getContent().get(0).text())
                        .isEqualTo(expectedNewsWithComments.getComments().get(0).getText())
        );
    }

    @Test
    public void checkGetNewsCommentResponseDTOsShouldThrowServiceException() {
        when(newsRepositoryMock.findWithCommentsByUuid(any(UUID.class)))
                .thenReturn(Optional.empty());

        UUID inputNewsUUID = UUID.fromString(NewsTestData.NEWS_INVALID_UUID);
        Pageable inputPageable = Pageable.unpaged();
        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> newsServiceImpl.getNewsCommentResponseDTOs(inputNewsUUID,
                        inputPageable));
    }

    @Test
    public void checkAddShouldReturnGetNewsResponseDTO() {
        UserDetails expectedUser = User.builder()
                .username(UserTestData.USER_NAME)
                .password(UserTestData.USER_PASSWORD)
                .build();
        Authentication expectedAuthentication = new UsernamePasswordAuthenticationToken(expectedUser,
                expectedUser.getPassword(),
                expectedUser.getAuthorities());
        GetUserResponseDTO expectedGetUserResponseDTO =
                UserTestData.expectedUserResponseDTO();

        try (MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic
                     = mockStatic(SecurityContextHolder.class)) {
            securityContextHolderMockedStatic.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContextMock);


            when(securityContextMock.getAuthentication())
                    .thenReturn(expectedAuthentication);
            when(userServiceMock.getUserResponseDTOByUsername(anyString()))
                    .thenReturn(expectedGetUserResponseDTO);
            when(newsRepositoryMock.save(any(News.class)))
                    .thenAnswer(invocation -> {
                        News savedNews = invocation.getArgument(0);
                        savedNews.setId(NewsTestData.NEWS_ID);
                        return savedNews;
                    });

            PostPutNewsRequestDTO inputPostPutNewsRequestDTO =
                    NewsTestData.expectedPostPutNewsRequestDTO();
            GetNewsResponseDTO actualGetNewsResponseDTO =
                    newsServiceImpl.add(inputPostPutNewsRequestDTO);

            assertAll(
                    () -> assertThat(actualGetNewsResponseDTO.uuid())
                            .isNotNull(),
                    () -> assertThat(actualGetNewsResponseDTO.authorFirstName())
                            .isEqualTo(expectedGetUserResponseDTO.firstName()),
                    () -> assertThat(actualGetNewsResponseDTO.authorLastName())
                            .isEqualTo(expectedGetUserResponseDTO.lastName()),
                    () -> assertThat(actualGetNewsResponseDTO.authorEmail())
                            .isEqualTo(expectedGetUserResponseDTO.email()),
                    () -> assertThat(actualGetNewsResponseDTO.title())
                            .isEqualTo(inputPostPutNewsRequestDTO.title()),
                    () -> assertThat(actualGetNewsResponseDTO.text())
                            .isEqualTo(inputPostPutNewsRequestDTO.text()),
                    () -> assertThat(actualGetNewsResponseDTO.created())
                            .isNotNull(),
                    () -> assertThat(actualGetNewsResponseDTO.updated())
                            .isNull()
            );
        }
    }

    @Test
    public void checkUpdateShouldReturnGetNewsResponseDTO() {
        News expectedNews = NewsTestData.expectedNews();
        GetUserResponseDTO expectedGetUserResponseDTO =
                UserTestData.expectedUserResponseDTO();

        when(newsRepositoryMock.findByUuid(any(UUID.class)))
                .thenReturn(Optional.of(expectedNews));
        when(userServiceMock.getUserResponseDTO(anyString()))
                .thenReturn(expectedGetUserResponseDTO);
        when(newsRepositoryMock.save(any(News.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UUID inputNewsUUID = UUID.fromString(NewsTestData.NEWS_UUID);
        PostPutNewsRequestDTO inputPostPutNewsRequestDTO =
                NewsTestData.expectedPostPutNewsRequestDTO();
        GetNewsResponseDTO actualGetNewsResponseDTO =
                newsServiceImpl.update(inputNewsUUID, inputPostPutNewsRequestDTO);

        assertAll(
                () -> assertThat(actualGetNewsResponseDTO.uuid())
                        .isNotNull(),
                () -> assertThat(actualGetNewsResponseDTO.authorFirstName())
                        .isEqualTo(expectedGetUserResponseDTO.firstName()),
                () -> assertThat(actualGetNewsResponseDTO.authorLastName())
                        .isEqualTo(expectedGetUserResponseDTO.lastName()),
                () -> assertThat(actualGetNewsResponseDTO.authorEmail())
                        .isEqualTo(expectedGetUserResponseDTO.email()),
                () -> assertThat(actualGetNewsResponseDTO.title())
                        .isEqualTo(inputPostPutNewsRequestDTO.title()),
                () -> assertThat(actualGetNewsResponseDTO.text())
                        .isEqualTo(inputPostPutNewsRequestDTO.text()),
                () -> assertThat(actualGetNewsResponseDTO.updated())
                        .isNotNull()
        );
    }

    @Test
    public void checkUpdateShouldThrowServiceException() {
        when(newsRepositoryMock.findByUuid(any(UUID.class)))
                .thenReturn(Optional.empty());

        UUID invalidNewsUUID = UUID.fromString(NewsTestData.NEWS_INVALID_UUID);
        PostPutNewsRequestDTO inputPostPutNewsRequestDTO =
                NewsTestData.expectedPostPutNewsRequestDTO();
        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> newsServiceImpl.update(invalidNewsUUID,
                        inputPostPutNewsRequestDTO));
    }

    @Test
    public void checkDeleteShouldRemoveNews() {

        UUID inputNewsUUID = UUID.fromString(NewsTestData.NEWS_DELETING_UUID);
        newsServiceImpl.remove(inputNewsUUID);

        verify(newsRepositoryMock).deleteByUuid(uuidArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue())
                .isEqualTo(inputNewsUUID);
    }
}
