package ru.clevertec.ecl.knyazev.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.knyazev.data.domain.searching.Searching;
import ru.clevertec.ecl.knyazev.entity.News;
import ru.clevertec.ecl.knyazev.repository.exception.RepositoryException;

/**
 * Represents repository for searching news
 * with pagination and sorting
 */
public interface NewsRepositoryCustom extends SearchingRepository {
    /**
     * Find all news or find all news
     * on given pageable data
     * and searching data
     *
     * @param pageable pageable param
     * @param searching searching param
     * @return page news or page news on given pageable data
     * and searching data or empty page
     * @throws RepositoryException when finding error
     */
    Page<News> findAll(Pageable pageable, Searching searching) throws RepositoryException;
}
