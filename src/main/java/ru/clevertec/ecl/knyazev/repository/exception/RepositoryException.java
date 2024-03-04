package ru.clevertec.ecl.knyazev.repository.exception;

import org.springframework.dao.DataAccessException;

public class RepositoryException extends DataAccessException {

    public static final String UPDATING_ERROR = "Updating error";
    public static final String SAVING_ERROR = "Saving error";
    public static final String SAVING_OR_UPDATING_ERROR = "Saving or updating error";
    public static final String DELETING_ERROR = "Deleting error";

    public static final String ENTITY_NOT_FOUND = "Entity not found with uuid=";
    public static final String FIND_ALL_ERROR = "Error when searching all entities";

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
