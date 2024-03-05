package ru.clevertec.ecl.knyazev.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.knyazev.data.domain.searching.Searching;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.response.GetCommentResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.news.request.PostPutNewsRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.news.response.GetNewsResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.pageresponse.PageDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.user.response.GetUserResponseDTO;
import ru.clevertec.ecl.knyazev.entity.Comment;
import ru.clevertec.ecl.knyazev.entity.News;
import ru.clevertec.ecl.knyazev.mapper.CommentMapper;
import ru.clevertec.ecl.knyazev.mapper.NewsMapper;
import ru.clevertec.ecl.knyazev.repository.NewsRepository;
import ru.clevertec.ecl.knyazev.service.NewsService;
import ru.clevertec.ecl.knyazev.service.UserService;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

import java.util.List;
import java.util.UUID;

@ConditionalOnProperty(prefix = "cache",
        name = "enabled",
        havingValue = "false")
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    private final UserService userServiceImpl;

    private final NewsMapper newsMapperImpl;
    private final CommentMapper commentMapperImpl;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public News getNews(UUID newsUUID) throws ServiceException {
        return newsRepository.findByUuid(newsUUID)
                .orElseThrow(ServiceException::new);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public GetNewsResponseDTO getNewsResponseDTO(UUID newsUUID) throws ServiceException {
        News news = newsRepository.findByUuid(newsUUID)
                .orElseThrow(ServiceException::new);
        GetUserResponseDTO getUserResponseDTO =
                userServiceImpl.getUserResponseDTO(news.getJournalistUUID().toString());

        return newsMapperImpl.toGetNewsResponseDTO(news, getUserResponseDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public GetCommentResponseDTO getNewsCommentResponseDTO(UUID newsUUID, UUID commentUUID) throws ServiceException {
        Comment newsComment = newsRepository.findWithCommentsByUuid(newsUUID)
                .orElseThrow(ServiceException::new)
                .getComments().stream()
                .filter(comment -> comment.getUuid().equals(commentUUID))
                .findFirst()
                .orElseThrow(ServiceException::new);
        GetUserResponseDTO getUserResponseDTO =
                userServiceImpl.getUserResponseDTO(newsComment.getSubscriberUUID().toString());

        return commentMapperImpl.toGetCommentResponseDTO(newsComment, getUserResponseDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PageDTO<GetNewsResponseDTO> getAllNewsDTO(Pageable pageable, Searching searching) {
        Page<GetNewsResponseDTO> pageGetNewsResponseDTO =
                newsRepository.findAll(pageable, searching)
                        .map(news -> newsMapperImpl.toGetNewsResponseDTO(news,
                                userServiceImpl.getUserResponseDTO(news.getJournalistUUID().toString())));

        return new PageDTO<>(pageGetNewsResponseDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PageDTO<GetCommentResponseDTO> getNewsCommentResponseDTOs(UUID newsUUID, Pageable pageable)
            throws ServiceException {
        List<GetCommentResponseDTO> newsCommentDTOs = newsRepository.findWithCommentsByUuid(newsUUID)
                .orElseThrow(ServiceException::new)
                .getComments().stream()
                .map(comment -> commentMapperImpl.toGetCommentResponseDTO(comment,
                        userServiceImpl.getUserResponseDTO(comment.getSubscriberUUID().toString())))
                .toList();

        return new PageDTO<>(new PageImpl<>(newsCommentDTOs,
                pageable,
                newsCommentDTOs.size()));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public GetNewsResponseDTO add(PostPutNewsRequestDTO postPutNewsRequestDTO) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        GetUserResponseDTO getUserResponseDTO =
                userServiceImpl.getUserResponseDTOByUsername(userName);

        return newsMapperImpl.toGetNewsResponseDTO(
                newsRepository.save(newsMapperImpl.toNews(postPutNewsRequestDTO,
                        getUserResponseDTO)),
                getUserResponseDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public GetNewsResponseDTO update(UUID newsUUID, PostPutNewsRequestDTO postPutNewsRequestDTO)
            throws ServiceException {
        News dbNews = newsRepository.findByUuid(newsUUID)
                .orElseThrow(ServiceException::new);
        GetUserResponseDTO getUserResponseDTO =
                userServiceImpl.getUserResponseDTO(dbNews.getJournalistUUID().toString());

        return newsMapperImpl.toGetNewsResponseDTO(
                newsRepository.save(newsMapperImpl.toNews(dbNews, postPutNewsRequestDTO)),
                getUserResponseDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void remove(UUID newsUUID) {
        newsRepository.deleteByUuid(newsUUID);
    }
}
