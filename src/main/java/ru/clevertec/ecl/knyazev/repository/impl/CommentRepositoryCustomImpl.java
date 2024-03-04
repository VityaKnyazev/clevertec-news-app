package ru.clevertec.ecl.knyazev.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.knyazev.data.domain.searching.Searching;
import ru.clevertec.ecl.knyazev.entity.Comment;
import ru.clevertec.ecl.knyazev.repository.CommentRepositoryCustom;
import ru.clevertec.ecl.knyazev.repository.exception.RepositoryException;

@NoArgsConstructor
@AllArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final static String[] SEARCHING_SORTING_FIELDS = {"text"};

    private static final String FIND_ALL_QUERY = """
            SELECT DISTINCT id, uuid, subscriber_uuid, text, news_id, create_date, update_date FROM comment""";
    private static final String FIND_ALL_COUNT_QUERY = "SELECT DISTINCT COUNT(id) FROM comment";

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Comment> findAll(Pageable pageable, Searching searching) throws RepositoryException {

        return findAllNativeQuery(entityManager,
                FIND_ALL_QUERY,
                searching,
                pageable,
                SEARCHING_SORTING_FIELDS,
                Comment.class,
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
