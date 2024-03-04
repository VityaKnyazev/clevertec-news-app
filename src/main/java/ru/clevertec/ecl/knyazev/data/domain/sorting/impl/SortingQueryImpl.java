package ru.clevertec.ecl.knyazev.data.domain.sorting.impl;

import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.knyazev.data.domain.searching.exception.SearchingException;
import ru.clevertec.ecl.knyazev.data.domain.sorting.SortingQuery;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SortingQueryImpl implements SortingQuery {

    public static final String ORDER_BY = " ORDER BY ";
    private static final String SORTING_QUERY = "%s %s, ";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSortingQueryPart(Sort sorting, String[] sortingFields) throws SearchingException {
        String sortingRes = sorting != null && sorting.isSorted()
                ? sorting.stream()
                .filter(order -> Arrays.stream(sortingFields).anyMatch(f -> f.equals(order.getProperty())))
                .map(order -> String.format(SORTING_QUERY, order.getProperty(), order.isAscending()
                        ? Sort.Direction.ASC.name()
                        : Sort.Direction.DESC.name()))
                .collect(Collectors.joining())
                .replaceFirst(",\\s$", "")
                : "";

        return  ("".equals(sortingRes))
                ? sortingRes
                : ORDER_BY + sortingRes;
    }
}
