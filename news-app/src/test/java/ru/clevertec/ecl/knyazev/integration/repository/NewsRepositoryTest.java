package ru.clevertec.ecl.knyazev.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.ecl.knyazev.data.domain.searching.Searching;
import ru.clevertec.ecl.knyazev.data.domain.searching.impl.SearchingImpl;
import ru.clevertec.ecl.knyazev.entity.News;
import ru.clevertec.ecl.knyazev.integration.config.TestContainerInitializer;
import ru.clevertec.ecl.knyazev.repository.NewsRepository;
import ru.clevertec.ecl.knyazev.util.NewsTestData;

import java.util.UUID;
import java.util.stream.Stream;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor
public class NewsRepositoryTest extends TestContainerInitializer {
    private static final String FIND_BY_UUID_QUERY = "SELECT n FROM News n WHERE n.uuid = :uuid";

    private final NewsRepository newsRepository;

    private final TestEntityManager testEntityManager;

    @Test
    public void checkFindByUuidShouldReturnOptionalNews() {
        UUID inputNewsUUID = UUID.fromString(NewsTestData.NEWS_UUID);

        News expectedNews = testEntityManager.getEntityManager()
                .createQuery(FIND_BY_UUID_QUERY, News.class)
                .setParameter("uuid", inputNewsUUID)
                .getSingleResult();

        News actualNews = newsRepository.findByUuid(inputNewsUUID).get();

        assertThat(actualNews)
                .isEqualTo(expectedNews);
    }

    @Test
    public void checkDeleteByUUIDShouldRemoveNews() {
        UUID inputNewsUUID = UUID.fromString(NewsTestData.NEWS_DELETING_UUID);

        newsRepository.deleteByUuid(inputNewsUUID);

        assertThatExceptionOfType(NoResultException.class)
                .isThrownBy(() -> testEntityManager.getEntityManager()
                        .createQuery(FIND_BY_UUID_QUERY, News.class)
                        .setParameter("uuid", inputNewsUUID)
                        .getSingleResult());
    }

    @Test
    public void checkFindAllShouldReturnAllNewsData() {
        int expectedPageNewsQuantity = 5;

        Pageable inputPageable = Pageable.unpaged();
        Searching inputSearching = new SearchingImpl(null);

        Page<News> actualPageNews = newsRepository.findAll(inputPageable,
                inputSearching);

        assertThat(actualPageNews).hasSize(expectedPageNewsQuantity);
    }

    @ParameterizedTest
    @MethodSource("getDataForFindAll")
    public void checkFindAllShouldReturnNewsWithPageableAndSearching(Pageable pageable,
                                                                     Searching searching,
                                                                     Long expectedFirstNewsId) {
        News expectedNews = testEntityManager.find(News.class, expectedFirstNewsId);

        Page<News> actualPageNews = newsRepository.findAll(pageable,
                searching);

        assertThat(actualPageNews.stream().findFirst().get())
                .isEqualTo(expectedNews);
    }


    /**
     * Get pageable and searching data
     * for testing findAll method
     * <p>
     * Returns stream of arguments that contains
     * pageable, searching data in various variants
     * for findAll method arguments
     * and
     * expected first news id from findAll method
     *
     *
     * @return pageable, searching data
     * and
     * expected first news id from result of findAll method
     */
    private static Stream<Arguments> getDataForFindAll() {
        String sortingField = NewsTestData.NEWS_SORTING_FIELD;
        Sort.Direction sortDirection = Sort.Direction.ASC;
        String searchingParam = NewsTestData.NEWS_SEARCHING_FIELD;
        int pageNumber = NewsTestData.NEWS_PAGE;
        int pageSize = NewsTestData.NEWS_PAGE_SIZE;

        return Stream.of(
                Arguments.of(PageRequest.of(pageNumber, pageSize),
                        new SearchingImpl(null),
                        1L),

                Arguments.of(PageRequest.of(pageNumber,
                                pageSize,
                                Sort.by(sortDirection, sortingField)),
                        new SearchingImpl(null),
                        5L),

                Arguments.of(PageRequest.of(pageNumber,
                                pageSize),
                        new SearchingImpl(searchingParam),
                        4L),

                Arguments.of(PageRequest.of(pageNumber,
                                pageSize,
                                Sort.by(sortDirection, sortingField)),
                        new SearchingImpl(searchingParam),
                        5L)
        );
    }
}
