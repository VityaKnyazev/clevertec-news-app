package ru.clevertec.ecl.knyazev.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.knyazev.data.domain.searching.Searching;
import ru.clevertec.ecl.knyazev.entity.News;
import ru.clevertec.ecl.knyazev.repository.NewsRepositoryCustom;
import ru.clevertec.ecl.knyazev.repository.exception.RepositoryException;

@NoArgsConstructor
@AllArgsConstructor
public class NewsRepositoryCustomImpl implements NewsRepositoryCustom {

    private final static String[] SEARCHING_SORTING_FIELDS = {"title", "text"};

    private static final String FIND_ALL_QUERY = """
            SELECT DISTINCT id, uuid, journalist_uuid, title, text, create_date, update_date FROM news""";
    private static final String FIND_ALL_COUNT_QUERY = "SELECT DISTINCT COUNT(id) FROM news";

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<News> findAll(Pageable pageable, Searching searching) throws RepositoryException {

        return findAllNativeQuery(entityManager,
                FIND_ALL_QUERY,
                searching,
                pageable,
                SEARCHING_SORTING_FIELDS,
                News.class,
                () -> findAllCount(searching, pageable));
    }

    private Long findAllCount(Searching searching,
                              Pageable pageable) {

        return findAllCount(entityManager,
                FIND_ALL_COUNT_QUERY,
                searching,
                pageable,SEARCHING_SORTING_FIELDS);
    }
}
