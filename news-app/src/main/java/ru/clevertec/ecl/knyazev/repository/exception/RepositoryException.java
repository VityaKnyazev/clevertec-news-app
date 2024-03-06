package ru.clevertec.ecl.knyazev.repository.exception;

import org.springframework.dao.DataAccessException;

public class RepositoryException extends DataAccessException {

    public static final String DEFAULT_EXCEPTION = "Repository exception";
    public static final String FIND_ALL_ERROR = "Error when searching all entities";

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
