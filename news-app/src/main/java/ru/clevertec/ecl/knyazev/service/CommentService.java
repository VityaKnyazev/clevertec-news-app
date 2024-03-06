package ru.clevertec.ecl.knyazev.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.knyazev.data.domain.searching.Searching;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.request.PostPutCommentRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.response.GetCommentResponseDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.pageresponse.PageDTO;
import ru.clevertec.ecl.knyazev.service.exception.ServiceException;

import java.util.UUID;

/**
 * Represents service for Comment data
 */
public interface CommentService {
    /**
     * Get comment dto by comment uuid
     *
     * @param commentUUID comment uuid
     * @return comment dto
     * @throws ServiceException when comment not found
     */
    GetCommentResponseDTO getCommentResponseDTO(UUID commentUUID) throws ServiceException;

    /**
     * Get comment DTOs using pagination, sorting and searching
     *
     * @param pageable paging and sorting object
     * @param searching searching object
     * @return all comment DTOs by given paging, sorting and
     * searching data or empty list
     */
    PageDTO<GetCommentResponseDTO> getAllCommentDTO(Pageable pageable, Searching searching);

    /**
     * Add comment using comment DTO
     * and get comment DTO as result
     *
     * @param postPutCommentRequestDTO comment DTO
     * @return added comment DTO
     */
    GetCommentResponseDTO add(PostPutCommentRequestDTO postPutCommentRequestDTO);

    /**
     * Update existing comment using comment uuid and comment DTO
     * and get comment DTO as result
     *
     * @param commentUUID comment uuid
     * @param postPutCommentRequestDTO comment DTO
     * @return updated comment DTO
     */
    GetCommentResponseDTO update(UUID commentUUID, PostPutCommentRequestDTO postPutCommentRequestDTO);

    /**
     * Remove existing comment using its UUID
     *
     * @param commentUUID comment uuid
     */
    void remove(UUID commentUUID);
}
