package ru.clevertec.ecl.knyazev.util;

public class UrlTestData {
    private static final String SLASH = "/";

    private static final String USER_REQUEST_URL = "/users";

    public static String getUserRequestUrl() {
        return USER_REQUEST_URL;
    }

    public static String getUserRequestUrl(String userId) {
        return USER_REQUEST_URL + SLASH + userId;
    }
}
