package ru.clevertec.ecl.knyazev.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.knyazev.data.domain.searching.Searching;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.request.PostPutCommentRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.response.GetCommentResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.pageresponse.PageDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.user.response.GetUserResponseDTO;
import ru.clevertec.ecl.knyazev.entity.Comment;
import ru.clevertec.ecl.knyazev.entity.News;
import ru.clevertec.ecl.knyazev.mapper.CommentMapper;
import ru.clevertec.ecl.knyazev.repository.CommentRepository;
import ru.clevertec.ecl.knyazev.service.CommentService;
import ru.clevertec.ecl.knyazev.service.NewsService;
import ru.clevertec.ecl.knyazev.service.UserService;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

import java.util.UUID;

@ConditionalOnProperty(prefix = "cache",
        name = "enabled",
        havingValue = "false")
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final NewsService newsServiceImpl;
    private final UserService userServiceImpl;

    private final CommentMapper commentMapperImpl;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public GetCommentResponseDTO getCommentResponseDTO(UUID commentUUID) throws ServiceException {
        Comment comment = commentRepository.findByUuid(commentUUID)
                .orElseThrow(ServiceException::new);
        GetUserResponseDTO getUserResponseDTO =
                userServiceImpl.getUserResponseDTO(comment.getSubscriberUUID().toString());

        return commentMapperImpl.toGetCommentResponseDTO(comment, getUserResponseDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PageDTO<GetCommentResponseDTO> getAllCommentDTO(Pageable pageable, Searching searching) {
        Page<GetCommentResponseDTO> pageGetCommentResponseDTO =
                commentRepository.findAll(pageable, searching)
                        .map(comment -> commentMapperImpl.toGetCommentResponseDTO(comment,
                                userServiceImpl.getUserResponseDTO(comment.getSubscriberUUID().toString())));

        return new PageDTO<>(pageGetCommentResponseDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public GetCommentResponseDTO add(PostPutCommentRequestDTO postPutCommentRequestDTO) {
        News dbNews = newsServiceImpl.getNews(UUID.fromString(postPutCommentRequestDTO.newsUUID()));
        String userName = SecurityContextHolder.getContext().getAuthentication()
                .getName();
        GetUserResponseDTO getUserResponseDTO =
                userServiceImpl.getUserResponseDTOByUsername(userName);

        return commentMapperImpl.toGetCommentResponseDTO(
                commentRepository.save(commentMapperImpl.toComment(postPutCommentRequestDTO,
                        dbNews,
                        getUserResponseDTO)),
                getUserResponseDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public GetCommentResponseDTO update(UUID commentUUID, PostPutCommentRequestDTO postPutCommentRequestDTO) {
        Comment dbComment = commentRepository.findByUuid(commentUUID)
                .orElseThrow(ServiceException::new);
        News dbNews = newsServiceImpl.getNews(UUID.fromString(postPutCommentRequestDTO.newsUUID()));
        GetUserResponseDTO getUserResponseDTO =
                userServiceImpl.getUserResponseDTO(dbComment.getSubscriberUUID().toString());

        return commentMapperImpl.toGetCommentResponseDTO(
                commentRepository.save(commentMapperImpl.toComment(dbComment,
                        dbNews,
                        postPutCommentRequestDTO)),
                getUserResponseDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void remove(UUID commentUUID) {
        commentRepository.deleteByUuid(commentUUID);
    }
}
