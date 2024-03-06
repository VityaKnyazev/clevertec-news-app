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
import ru.clevertec.ecl.knyazev.data.http.owner.comment.request.PostPutCommentRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.response.GetCommentResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.pageresponse.PageDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.user.response.GetUserResponseDTO;
import ru.clevertec.ecl.knyazev.entity.Comment;
import ru.clevertec.ecl.knyazev.entity.News;
import ru.clevertec.ecl.knyazev.mapper.CommentMapper;
import ru.clevertec.ecl.knyazev.mapper.CommentMapperImpl;
import ru.clevertec.ecl.knyazev.repository.CommentRepository;
import ru.clevertec.ecl.knyazev.service.NewsService;
import ru.clevertec.ecl.knyazev.service.UserService;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;
import ru.clevertec.ecl.knyazev.util.CommentTestData;
import ru.clevertec.ecl.knyazev.util.NewsTestData;
import ru.clevertec.ecl.knyazev.util.UserTestData;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepositoryMock;

    @Mock
    private NewsService newsServiceMock;
    @Mock
    private UserService userServiceMock;

    @Spy
    private CommentMapper commentMapperImpl = new CommentMapperImpl();

    @Mock
    private SecurityContext securityContextMock;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @InjectMocks
    private CommentServiceImpl commentServiceImpl;

    @Test
    public void checkGetCommentResponseDTOShouldReturnCommentDTO() {
        Comment expectedComment = CommentTestData.expectedComment();
        GetUserResponseDTO expectedGetUserResponseDTO =
                UserTestData.expectedSubscriberResponseDTO();

        when(commentRepositoryMock.findByUuid(any(UUID.class)))
                .thenReturn(Optional.of(expectedComment));
        when(userServiceMock.getUserResponseDTO(anyString()))
                .thenReturn(expectedGetUserResponseDTO);

        UUID inputUUID = UUID.fromString(CommentTestData.COMMENT_UUID);
        GetCommentResponseDTO actualGetCommentResponseDTO = commentServiceImpl.getCommentResponseDTO(inputUUID);

        assertAll(
                () -> assertThat(actualGetCommentResponseDTO.uuid())
                        .isEqualTo(expectedComment.getUuid().toString()),
                () -> assertThat(actualGetCommentResponseDTO.subscriberFirstName())
                        .isEqualTo(expectedGetUserResponseDTO.firstName()),
                () -> assertThat(actualGetCommentResponseDTO.subscriberLastName())
                        .isEqualTo(expectedGetUserResponseDTO.lastName()),
                () -> assertThat(actualGetCommentResponseDTO.subscriberEmail())
                        .isEqualTo(expectedGetUserResponseDTO.email()),
                () -> assertThat(actualGetCommentResponseDTO.text())
                        .isEqualTo(expectedComment.getText())
        );
    }

    @Test
    public void checkGetCommentResponseDTOShouldThrowServiceException() {
        when(commentRepositoryMock.findByUuid(any(UUID.class)))
                .thenReturn(Optional.empty());

        UUID inputUUID = UUID.fromString(CommentTestData.COMMENT_INVALID_UUID);
        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> commentServiceImpl.getCommentResponseDTO(inputUUID));
    }

    @Test
    public void checkGetAllCommentDTOShouldReturnCommentDTO() {
        Page<Comment> expectedPageComments =
                CommentTestData.expectedPageComments();
        GetUserResponseDTO expectedGetUserResponseDTO = UserTestData.expectedUserResponseDTO();

        when(commentRepositoryMock.findAll(any(Pageable.class), any(Searching.class)))
                .thenReturn(expectedPageComments);
        when(userServiceMock.getUserResponseDTO(any(String.class)))
                .thenReturn(expectedGetUserResponseDTO);

        Pageable inputPageable = Pageable.unpaged();
        Searching inputSearching = new SearchingImpl(null);
        PageDTO<GetCommentResponseDTO> actualPageGetCommentResponseDTO =
                commentServiceImpl.getAllCommentDTO(inputPageable, inputSearching);

        assertAll(
                () -> assertThat(actualPageGetCommentResponseDTO.getContent().get(0).uuid())
                        .isEqualTo(expectedPageComments.getContent().get(0).getUuid().toString()),
                () -> assertThat(actualPageGetCommentResponseDTO.getContent().get(0).text())
                        .isEqualTo(expectedPageComments.getContent().get(0).getText()),
                () -> assertThat(actualPageGetCommentResponseDTO.getContent().get(0).subscriberFirstName())
                        .isEqualTo(expectedGetUserResponseDTO.firstName()),
                () -> assertThat(actualPageGetCommentResponseDTO.getContent().get(0).subscriberLastName())
                        .isEqualTo(expectedGetUserResponseDTO.lastName()),
                () -> assertThat(actualPageGetCommentResponseDTO.getContent().get(0).subscriberEmail())
                        .isEqualTo(expectedGetUserResponseDTO.email())
        );
    }

    @Test
    public void checkAddShouldReturnGetCommentResponseDTO() {
        News expectedNews = NewsTestData.expectedNews();
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

            when(newsServiceMock.getNews(any(UUID.class)))
                    .thenReturn(expectedNews);
            when(securityContextMock.getAuthentication())
                    .thenReturn(expectedAuthentication);
            when(userServiceMock.getUserResponseDTOByUsername(anyString()))
                    .thenReturn(expectedGetUserResponseDTO);
            when(commentRepositoryMock.save(any(Comment.class)))
                    .thenAnswer(invocation -> {
                        Comment savedComment = invocation.getArgument(0);
                        savedComment.setId(CommentTestData.COMMENT_ID);
                        return savedComment;
                    });

            PostPutCommentRequestDTO inputPostPutCommentRequestDTO =
                    CommentTestData.expectedPostPutCommentRequestDTO();
            GetCommentResponseDTO actualGetCommentResponseDTO =
                    commentServiceImpl.add(inputPostPutCommentRequestDTO);

            assertAll(
                    () -> assertThat(actualGetCommentResponseDTO.uuid())
                            .isNotNull(),
                    () -> assertThat(actualGetCommentResponseDTO.subscriberFirstName())
                            .isEqualTo(expectedGetUserResponseDTO.firstName()),
                    () -> assertThat(actualGetCommentResponseDTO.subscriberLastName())
                            .isEqualTo(expectedGetUserResponseDTO.lastName()),
                    () -> assertThat(actualGetCommentResponseDTO.subscriberEmail())
                            .isEqualTo(expectedGetUserResponseDTO.email()),
                    () -> assertThat(actualGetCommentResponseDTO.text())
                            .isEqualTo(inputPostPutCommentRequestDTO.text()),
                    () -> assertThat(actualGetCommentResponseDTO.created())
                            .isNotNull(),
                    () -> assertThat(actualGetCommentResponseDTO.updated())
                            .isNull()
            );
        }
    }

    @Test
    public void checkUpdateShouldReturnGetCommentResponseDTO() {
        Comment expectedComment = CommentTestData.expectedComment();
        News expectedNews = NewsTestData.expectedNews();
        GetUserResponseDTO expectedGetUserResponseDTO =
                UserTestData.expectedUserResponseDTO();

        when(commentRepositoryMock.findByUuid(any(UUID.class)))
                .thenReturn(Optional.of(expectedComment));
        when(newsServiceMock.getNews(any(UUID.class)))
                .thenReturn(expectedNews);
        when(userServiceMock.getUserResponseDTO(anyString()))
                .thenReturn(expectedGetUserResponseDTO);
        when(commentRepositoryMock.save(any(Comment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UUID inputCommentUUID = UUID.fromString(CommentTestData.COMMENT_UUID);
        PostPutCommentRequestDTO inputPostPutCommentRequestDTO =
                CommentTestData.expectedPostPutCommentRequestDTO();
        GetCommentResponseDTO actualGetCommentResponseDTO =
                commentServiceImpl.update(inputCommentUUID, inputPostPutCommentRequestDTO);

        assertAll(
                () -> assertThat(actualGetCommentResponseDTO.uuid())
                        .isNotNull(),
                () -> assertThat(actualGetCommentResponseDTO.subscriberFirstName())
                        .isEqualTo(expectedGetUserResponseDTO.firstName()),
                () -> assertThat(actualGetCommentResponseDTO.subscriberLastName())
                        .isEqualTo(expectedGetUserResponseDTO.lastName()),
                () -> assertThat(actualGetCommentResponseDTO.subscriberEmail())
                        .isEqualTo(expectedGetUserResponseDTO.email()),
                () -> assertThat(actualGetCommentResponseDTO.text())
                        .isEqualTo(inputPostPutCommentRequestDTO.text()),
                () -> assertThat(actualGetCommentResponseDTO.updated())
                        .isNotNull()
        );
    }

    @Test
    public void checkUpdateShouldThrowServiceException() {
        when(commentRepositoryMock.findByUuid(any(UUID.class)))
                .thenReturn(Optional.empty());

        UUID invalidCommentUUID = UUID.fromString(CommentTestData.COMMENT_INVALID_UUID);
        PostPutCommentRequestDTO inputPostPutCommentRequestDTO =
                CommentTestData.expectedPostPutCommentRequestDTO();
        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> commentServiceImpl.update(invalidCommentUUID,
                        inputPostPutCommentRequestDTO));
    }

    @Test
    public void checkDeleteShouldRemoveComment() {
        UUID inputCommentUUID = UUID.fromString(CommentTestData.COMMENT_DELETING_UUID);
        commentServiceImpl.remove(inputCommentUUID);

        verify(commentRepositoryMock).deleteByUuid(uuidArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue())
                .isEqualTo(inputCommentUUID);
    }
}
