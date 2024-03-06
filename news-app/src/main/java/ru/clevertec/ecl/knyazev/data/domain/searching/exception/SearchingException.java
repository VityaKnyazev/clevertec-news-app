package ru.clevertec.ecl.knyazev.data.domain.searching.exception;

public class SearchingException extends RuntimeException {

    private static final String DEFAULT_ERROR = "Searching error";
    public static final String SEARCHING_NOT_USING_ERROR = "Searching not using";

    public SearchingException() {
        super(DEFAULT_ERROR);
    }

    public SearchingException(String message) {
        super(message);
    }

    public SearchingException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchingException(Throwable cause) {
        super(cause);
    }
}
