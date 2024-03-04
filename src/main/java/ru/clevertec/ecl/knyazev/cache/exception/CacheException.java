package ru.clevertec.ecl.knyazev.cache.exception;

public class CacheException extends RuntimeException {
    public static final String CACHE_INSTANTIATION_EXCEPTION = "Error when instantiating cache";

    public CacheException() {
        super(CACHE_INSTANTIATION_EXCEPTION);
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }
}
