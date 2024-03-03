package ru.clevertec.ecl.knyazev.service.exception;

public class ServiceException extends RuntimeException {

    public static final String DEFAULT_ERROR_MESSAGE = "Searching object(s) not found";
    public static final String USER_SEARCHING_ERROR_MESSAGE = "ERROR when trying to add role to user %s";


    public ServiceException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
