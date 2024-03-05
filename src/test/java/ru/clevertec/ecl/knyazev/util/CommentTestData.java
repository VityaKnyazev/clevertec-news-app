package ru.clevertec.ecl.knyazev.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.knyazev.data.http.owner.comment.request.PostPutCommentRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.news.request.PostPutNewsRequestDTO;
import ru.clevertec.ecl.knyazev.entity.Comment;
import ru.clevertec.ecl.knyazev.entity.News;

import java.util.List;
import java.util.UUID;

public class CommentTestData {
    public static Long COMMENT_ID = 1L;
    public static String COMMENT_UUID = "5da9af91-1f22-4a09-85b6-a09eadace32e";
    public static String COMMENT_INVALID_UUID = "ddd61e52-0008-4fb2-94be-f7b270c3cb37";
    public static String COMMENT_NEWS_UUID = "548f8b02-4f28-49af-864b-b50faa1c1438";
    public static String COMMENT_TEXT = "Да бред это сивой... ... Не было, говорю!";
    public static String COMMENT_SUBSCRIBER_UUID = "b0436b81-7336-4e66-a7e2-ba009a3767b0";
    public static String COMMENT_DELETING_UUID = "8b9e6e08-ed5c-432e-801c-a9637bb5acee";

    public static Integer COMMENT_PAGE = 0;
    public static Integer COMMENT_PAGE_SIZE = 4;
    public static String COMMENT_SORTING_FIELD = "text";
    public static String COMMENT_SEARCHING_FIELD = "был";

    public static Comment expectedComment() {
        return Comment.builder()
                .id(COMMENT_ID)
                .uuid(UUID.fromString(COMMENT_UUID))
                .subscriberUUID(UUID.fromString(COMMENT_SUBSCRIBER_UUID))
                .text("Да бред это сивой... ... Не было, говорю!")
                .build();
    }

    public static Comment expectedCommentWithNews() {
        News news = NewsTestData.expectedNews();

        return Comment.builder()
                .id(COMMENT_ID)
                .uuid(UUID.fromString(COMMENT_UUID))
                .subscriberUUID(UUID.fromString(COMMENT_SUBSCRIBER_UUID))
                .text("Да бред это сивой... ... Не было, говорю!")
                .news(news)
                .build();
    }

    public static Page<Comment> expectedPageComments() {
        Comment comment = Comment.builder()
                .id(COMMENT_ID)
                .uuid(UUID.fromString(COMMENT_UUID))
                .subscriberUUID(UUID.fromString(COMMENT_SUBSCRIBER_UUID))
                .text("Да бред это сивой... ... Не было, говорю!")
                .build();

        return new PageImpl<>(List.of(comment));
    }

    public static PostPutCommentRequestDTO expectedPostPutCommentRequestDTO() {
        return PostPutCommentRequestDTO.builder()
                .newsUUID(COMMENT_NEWS_UUID)
                .text(COMMENT_TEXT)
                .build();
    }
}
