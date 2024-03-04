package ru.clevertec.ecl.knyazev.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.knyazev.data.domain.searching.Searching;
import ru.clevertec.ecl.knyazev.data.domain.sorting.SortingQuery;
import ru.clevertec.ecl.knyazev.data.domain.sorting.impl.SortingQueryImpl;
import ru.clevertec.ecl.knyazev.repository.exception.RepositoryException;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Represents custom repository for realization searching
 * with pagination and sorting using native query
 */
public interface SearchingRepository {

    /**
     * Create native sql query with searching, paging, sorting
     *
     * @param queryRes               sql native query
     * @param searching              searching param
     * @param pageable               pageable param
     * @param sortingSearchingFields fields on which searching or sorting works
     * @return native sql query with paging, sorting, searching
     */
    default String createNativeSqlQuery(String queryRes,
                                        Searching searching,
                                        Pageable pageable,
                                        String[] sortingSearchingFields) {
        if (searching.useSearching()) {
            queryRes += searching.getSearchingQueryPart(Arrays.asList(sortingSearchingFields));
        }

        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            SortingQuery sortingQueryImpl = new SortingQueryImpl();

            queryRes += sortingQueryImpl.getSortingQueryPart(sort, sortingSearchingFields);
        }

        return queryRes;
    }

    /**
     * Execute native find all query and get page entity
     * representation using paging, searching and sorting
     *
     * @param entityManager          entity manager
     * @param query                  string native sql query for find all
     * @param searching              searching object param
     * @param pageable               pageable object param
     * @param sortingSearchingFields fields on which sorting and searching using
     * @param classType              result entity class type
     * @param findAllCount           produce count entities in database for
     *                               given query result
     * @param <T>                    entity class type
     * @return page with collection content of entities of T type
     * @throws RepositoryException if finding error
     */
    @SuppressWarnings("unchecked")
    default <T> Page<T> findAllNativeQuery(EntityManager entityManager,
                                           String query,
                                           Searching searching,
                                           Pageable pageable,
                                           String[] sortingSearchingFields,
                                           Class<T> classType,
                                           Supplier<Long> findAllCount) throws RepositoryException {
        try {
            String queryRes = createNativeSqlQuery(query,
                    searching,
                    pageable,
                    sortingSearchingFields);

            Query queryAll = entityManager.createNativeQuery(queryRes,
                    classType);

            if (searching.useSearching()) {
                queryAll = searching.createSearchingQuery(queryAll, sortingSearchingFields);
            }

            if (pageable.isPaged()) {
                queryAll = queryAll.setFirstResult((int) pageable.getOffset())
                        .setMaxResults(pageable.getPageSize());
            }

            List<T> entityResults = (List<T>) queryAll.getResultList();

            return new PageImpl<>(entityResults, pageable, findAllCount.get());

        } catch (IllegalArgumentException | IllegalStateException | PersistenceException e) {
            throw new RepositoryException(String.format("%s: %s",
                    RepositoryException.FIND_ALL_ERROR,
                    e.getMessage()), e);
        }
    }

    /**
     * get entity COUNT(*) in database
     * for concrete findAll sql query
     * without sorting part
     *
     * @param entityManager          entity manager
     * @param query                  string native sql query for find all
     * @param searching              searching object param
     * @param pageable               pageable object param
     * @param sortingSearchingFields fields on which sorting and searching using
     * @return entity COUNT(*) in database for concrete findAll sql query
     *                without sorting part
     * @throws RepositoryException if finding error
     */
    default Long findAllCount(EntityManager entityManager,
                              String query,
                              Searching searching,
                              Pageable pageable,
                              String[] sortingSearchingFields) throws RepositoryException {

        String queryRes = createNativeSqlQuery(query,
                searching,
                pageable,
                sortingSearchingFields);

        Query queryCount = entityManager.createNativeQuery(SortingQuery.removeSortingQueryPart(queryRes),
                Long.class);

        if (searching.useSearching()) {
            queryCount = searching.createSearchingQuery(queryCount, sortingSearchingFields);
        }

        return (long) queryCount.getSingleResult();
    }
}
