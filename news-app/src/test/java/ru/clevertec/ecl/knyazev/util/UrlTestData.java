package ru.clevertec.ecl.knyazev.util;

public class UrlTestData {
    private static final String SLASH = "/";

    public static String SEARCH_PARAM = "search";

    private static final String USER_REQUEST_URL = "/users";
    private static final String NEWS_REQUEST_URL = "/news";
    private static final String COMMENT_REQUEST_URL = "/comments";

    public static String getUserRequestUrl() {
        return USER_REQUEST_URL;
    }

    public static String getUserRequestUrl(String userId) {
        return USER_REQUEST_URL + SLASH + userId;
    }

    public static String getNewsRequestUrl() {
        return NEWS_REQUEST_URL;
    }

    public static String getNewsRequestUrl(String newsId) {
        return NEWS_REQUEST_URL + SLASH + newsId;
    }

    public static String getCommentRequestUrl() {
        return COMMENT_REQUEST_URL;
    }

    public static String getCommentRequestUrl(String commentId) {
        return COMMENT_REQUEST_URL + SLASH + commentId;
    }

    public static String getNewsCommentRequestUrl(String newsId) {
        return NEWS_REQUEST_URL + SLASH + newsId +
                COMMENT_REQUEST_URL;
    }

    public static String getNewsCommentRequestUrl(String newsId, String commentId) {
        return NEWS_REQUEST_URL + SLASH + newsId +
                COMMENT_REQUEST_URL +
                SLASH + commentId;
    }

}
