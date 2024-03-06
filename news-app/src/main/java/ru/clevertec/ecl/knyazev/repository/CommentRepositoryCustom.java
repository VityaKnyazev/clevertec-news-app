package ru.clevertec.ecl.knyazev.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.knyazev.data.domain.searching.Searching;
import ru.clevertec.ecl.knyazev.entity.Comment;
import ru.clevertec.ecl.knyazev.repository.exception.RepositoryException;

/**
 * Represents repository for searching comments
 * with pagination and sorting
 */
public interface CommentRepositoryCustom extends SearchingRepository {
    /**
     * Find all comments or find all comments
     * on given pageable data
     * and searching data
     *
     * @param pageable pageable param
     * @param searching searching param
     * @return page comments or page comments on given pageable data
     * and searching data or empty page
     * @throws RepositoryException when finding error
     */
    Page<Comment> findAll(Pageable pageable, Searching searching) throws RepositoryException;
}
