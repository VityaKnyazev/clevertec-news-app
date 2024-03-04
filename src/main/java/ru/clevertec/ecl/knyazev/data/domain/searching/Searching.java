package ru.clevertec.ecl.knyazev.data.domain.searching;

import jakarta.persistence.Query;
import ru.clevertec.ecl.knyazev.data.domain.searching.exception.SearchingException;

import java.util.List;

/**
 * Represents object for working with searching input fields
 * and creating searching quires
 */
public abstract class Searching {

    /**
     * Get SQL searching query part using given fields
     *
     * @param fields fields on which searching will be going on
     * @return string query SQL part for searching on give fields
     * @throws SearchingException when searching not using
     */
    public abstract String getSearchingQueryPart(List<String> fields) throws SearchingException;

    /**
     * Get quantity of sql searching parameters in quire
     * using field and query variants for a single field quantities
     *
     * @param fieldQuantity field quantity
     * @return quantity of sql searching parameters in quire
     * @throws SearchingException when searching not using
     */

    protected abstract Integer getSearchingParameters(int fieldQuantity) throws SearchingException;
    /**
     * Get searching value
     *
     * @return searching value for inserting
     * into searching query
     * @throws SearchingException when searching not using
     */
    protected abstract String getSearchingValue() throws SearchingException;;

    /**
     * Check use of searching
     *
     * @return true - if search is using, otherwise - false
     */
    public abstract Boolean useSearching();

    /**
     *
     * Crete searching native query and set searching
     * parameters to searching fields
     *
     * @param query native query
     * @param searchingFields searching fields
     * @return native query with set searching parameters
     * @throws SearchingException when searching not using
     */
    public Query createSearchingQuery(Query query, String[] searchingFields) throws SearchingException {

        if (!useSearching()) {
            throw new SearchingException(SearchingException.SEARCHING_NOT_USING_ERROR);
        }

        Integer paramQuantities = getSearchingParameters(searchingFields.length);

        for (int i = 1; i <= paramQuantities; i++) {
            query = query.setParameter(i, getSearchingValue());
        }

        return query;
    }
}
