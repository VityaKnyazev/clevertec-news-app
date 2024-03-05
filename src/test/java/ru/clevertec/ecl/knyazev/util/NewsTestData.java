package ru.clevertec.ecl.knyazev.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.clevertec.ecl.knyazev.data.http.owner.news.request.PostPutNewsRequestDTO;
import ru.clevertec.ecl.knyazev.data.http.owner.user.request.PostPutUserRequestDTO;
import ru.clevertec.ecl.knyazev.entity.Comment;
import ru.clevertec.ecl.knyazev.entity.News;

import java.util.List;
import java.util.UUID;

public class NewsTestData {
    public static Long NEWS_ID = 1L;
    public static String NEWS_UUID = "548f8b02-4f28-49af-864b-b50faa1c1438";
    public static String NEWS_DELETING_UUID = "36068389-38c6-4799-a0cc-3e4793e92450";
    public static String NEWS_INVALID_UUID = "5048f592-0c71-45a9-a513-234fc7a7bbae";
    public static String NEWS_JOURNALIST_UUID = "d86bc1b6-365b-460c-ad7a-737c62184c97";
    public static String NEWS_TITLE = "Красная жара надвигается на континент";
    public static String NEWS_TEXT = "Красная жара надвигается на континент. Засуха на европейском...";
    public static Integer NEWS_PAGE = 0;
    public static Integer NEWS_PAGE_SIZE = 4;
    public static String NEWS_SORTING_FIELD = "text";
    public static String NEWS_SEARCHING_FIELD = "при";


    public static News expectedNews() {
        return News.builder()
                .id(NEWS_ID)
                .uuid(UUID.fromString(NEWS_UUID))
                .journalistUUID(UUID.fromString(NEWS_JOURNALIST_UUID))
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .build();
    }

    public static News expectedNewsWithComments() {
        Comment comment = CommentTestData.expectedComment();

        return News.builder()
                .id(NEWS_ID)
                .uuid(UUID.fromString(NEWS_UUID))
                .journalistUUID(UUID.fromString(NEWS_JOURNALIST_UUID))
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .comments(List.of(comment))
                .build();
    }

    public static Page<News> expectedPageNews() {
        News news = News.builder()
                .id(NEWS_ID)
                .uuid(UUID.fromString(NEWS_UUID))
                .journalistUUID(UUID.fromString(NEWS_JOURNALIST_UUID))
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .build();

        return new PageImpl<>(List.of(news));
    }

    public static PostPutNewsRequestDTO expectedPostPutNewsRequestDTO() {
        return PostPutNewsRequestDTO.builder()
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .build();
    }
}
