package ru.clevertec.ecl.knyazev.data.domain.sorting;

import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.knyazev.data.domain.searching.exception.SearchingException;
import ru.clevertec.ecl.knyazev.data.domain.sorting.impl.SortingQueryImpl;

import java.util.List;

/**
 * Represents object for creating sorting quires
 */
public interface SortingQuery {
    /**
     * Get SQL sorting query part using given fields with order
     *
     * @param sorting params
     * @param sortingFields sorting fields
     * @return string query SQL part for searching on give fields
     * @throws SearchingException when searching not using
     */
    String getSortingQueryPart(Sort sorting, String[] sortingFields) throws SearchingException;

    /**
     *
     * Remove part ORDER BY (with fields) from
     * sql query if ORDER BY is present.
     *
     * @param query sql query
     * @return query without ORDER BY part
     */
    static String removeSortingQueryPart(String query) {
        return query.contains(SortingQueryImpl.ORDER_BY)
                ? query.replaceFirst(SortingQueryImpl.ORDER_BY + ".*$", "")
                : query;
    }
}
