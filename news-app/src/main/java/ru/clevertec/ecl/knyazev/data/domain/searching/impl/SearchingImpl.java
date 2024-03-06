package ru.clevertec.ecl.knyazev.data.domain.searching.impl;

import ru.clevertec.ecl.knyazev.data.domain.searching.Searching;
import ru.clevertec.ecl.knyazev.data.domain.searching.exception.SearchingException;

import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents part SQL query creator for full text searching
 */
public class SearchingImpl extends Searching {

    private static final String SQL_SEARCH_QUERY = "%s LIKE ?";
    private static final int SQL_RESULT_REPEATING_COUNT = 3;

    private static final String BEGIN_DATA_QUERY = "%s%%";
    private static final String MIDDLE_DATA_QUERY = "%%%s%%";
    private static final String END_DATA_QUERY = "%%%s";

    private static final String WHERE = " WHERE ";
    private static final String OR = " OR ";

    private final String searchData;
    private final ArrayDeque<String> searchDataValues = new ArrayDeque<>(SQL_RESULT_REPEATING_COUNT) {{
        addFirst(BEGIN_DATA_QUERY);
        addLast(MIDDLE_DATA_QUERY);
        addLast(END_DATA_QUERY);
    }};

    private final Boolean useSearching;

    public SearchingImpl(String searchData) {
        if (searchData == null || searchData.isEmpty()) {
            this.searchData = "";
            useSearching = false;
        } else {
            this.searchData = searchData.strip();
            useSearching = true;
        }
    }

    /**
     * Get SQL searching query part.
     * Example: WHERE name LIKE ? OR name LIKE ? OR name LIKE ?
     *
     * @param fields fields on which searching will be going on
     * @return string query SQL part for searching on give fields
     * @throws SearchingException when searching not using
     */
    @Override
    public String getSearchingQueryPart(List<String> fields) throws SearchingException {

        if (!useSearching) {
            throw new SearchingException(SearchingException.SEARCHING_NOT_USING_ERROR);
        }

        return (fields != null && !fields.isEmpty())
                ? WHERE + fields.stream()
                .map(field -> (String.format(SQL_SEARCH_QUERY, field)
                        + OR).repeat(SQL_RESULT_REPEATING_COUNT))
                .collect(Collectors.joining())
                .replaceFirst(OR + "$", "")
                : "";
    }

    /**
     * Get searching value from searching data infinite queue
     * Example: %val or val% or %val%
     *
     * @return searching value from searching data infinite queue
     * @throws SearchingException when searching not using
     */
    @Override
    protected String getSearchingValue() throws SearchingException {
        if (!useSearching()) {
            throw new SearchingException(SearchingException.SEARCHING_NOT_USING_ERROR);
        }

        String searchingValue = searchDataValues.pollFirst();
        searchDataValues.addLast(searchingValue);

        return String.format(searchingValue, searchData);
    }

    /**
     * Get quantity of sql searching parameters
     * {inheritDoc}
     *
     * @throws SearchingException when searching not using
     */
    @Override
    protected Integer getSearchingParameters(int fieldQuantity) throws SearchingException {
        if (!useSearching()) {
            throw new SearchingException(SearchingException.SEARCHING_NOT_USING_ERROR);
        }

        return SQL_RESULT_REPEATING_COUNT * fieldQuantity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean useSearching() {
        return useSearching;
    }
}
