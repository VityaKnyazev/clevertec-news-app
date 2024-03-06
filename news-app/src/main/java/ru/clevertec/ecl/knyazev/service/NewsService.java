package ru.clevertec.ecl.knyazev.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.knyazev.data.domain.searching.Searching;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.response.GetCommentResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.news.request.PostPutNewsRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.news.response.GetNewsResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.pageresponse.PageDTO;
import ru.clevertec.ecl.knyazev.entity.News;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

import java.util.UUID;

/**
 * Represents service for News data
 */
public interface NewsService {

    /**
     * Get news by news uuid
     *
     * @param newsUUID news uuid
     * @return news
     * @throws ServiceException when news not found
     */
    News getNews(UUID newsUUID) throws ServiceException;

    /**
     * Get news dto by news uuid
     *
     * @param newsUUID news uuid
     * @return news dto
     * @throws ServiceException when news not found
     */
    GetNewsResponseDTO getNewsResponseDTO(UUID newsUUID) throws ServiceException;

    /**
     * Get news comment response DTO by given news uuid and comment uuid
     *
     * @param newsUUID news uuid
     * @param commentUUID news comment uuid
     * @return comment dto
     * @throws ServiceException when news not found or comment not found
     * or comment doesn't possess to news
     */
    GetCommentResponseDTO getNewsCommentResponseDTO(UUID newsUUID, UUID commentUUID) throws ServiceException;

    /**
     * Get page news DTOs using pagination, sorting and searching
     *
     * @param pageable paging and sorting object
     * @param searching searching object
     * @return all news DTOs by given paging, sorting and
     * searching data or empty list
     */
    PageDTO<GetNewsResponseDTO> getAllNewsDTO(Pageable pageable, Searching searching);

    /**
     * Get page comments DTOs by given news uuid
     * and paging, sorting data
     *
     * @param newsUUID news uuid
     * @param pageable pageable object
     * @return comment DTO by news uuid and paging, sorting data
     * @throws ServiceException when news not found
     */
    PageDTO<GetCommentResponseDTO> getNewsCommentResponseDTOs(UUID newsUUID, Pageable pageable)
            throws ServiceException;

    /**
     * Add news using news DTO
     * and get news DTO as result
     *
     * @param postPutNewsRequestDTO news DTO
     * @return added news DTO
     */
    GetNewsResponseDTO add(PostPutNewsRequestDTO postPutNewsRequestDTO);

    /**
     * Update existing news using news uuid and news DTO
     * and get news DTO as result
     *
     * @param newsUUID news uuid
     * @param postPutNewsRequestDTO news DTO
     * @return updated news DTO
     */
    GetNewsResponseDTO update(UUID newsUUID, PostPutNewsRequestDTO postPutNewsRequestDTO);

    /**
     * Remove existing news using its UUID
     *
     * @param newsUUID news uuid
     */
    void remove(UUID newsUUID);
}
