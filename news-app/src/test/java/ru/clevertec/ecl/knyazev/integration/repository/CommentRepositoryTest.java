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
import ru.clevertec.ecl.knyazev.entity.Comment;
import ru.clevertec.ecl.knyazev.entity.News;
import ru.clevertec.ecl.knyazev.integration.config.TestContainerInitializer;
import ru.clevertec.ecl.knyazev.repository.CommentRepository;
import ru.clevertec.ecl.knyazev.util.CommentTestData;

import java.util.UUID;
import java.util.stream.Stream;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor
public class CommentRepositoryTest extends TestContainerInitializer {
    private static final String FIND_BY_UUID_QUERY = "SELECT c FROM Comment c WHERE c.uuid = :uuid";

    private final CommentRepository commentRepository;

    private final TestEntityManager testEntityManager;

    @Test
    public void checkFindByUuidShouldReturnOptionalComment() {
        UUID inputNewsUUID = UUID.fromString(CommentTestData.COMMENT_UUID);

        Comment expectedComment = testEntityManager.getEntityManager()
                .createQuery(FIND_BY_UUID_QUERY, Comment.class)
                .setParameter("uuid", inputNewsUUID)
                .getSingleResult();

        Comment actualComment = commentRepository.findByUuid(inputNewsUUID).get();

        assertThat(actualComment)
                .isEqualTo(expectedComment);
    }

    @Test
    public void checkDeleteByUUIDShouldRemoveComment() {
        UUID inpuCommentUUID = UUID.fromString(CommentTestData.COMMENT_DELETING_UUID);

        commentRepository.deleteByUuid(inpuCommentUUID);

        assertThatExceptionOfType(NoResultException.class)
                .isThrownBy(() -> testEntityManager.getEntityManager()
                        .createQuery(FIND_BY_UUID_QUERY, News.class)
                        .setParameter("uuid", inpuCommentUUID)
                        .getSingleResult());
    }

    @Test
    public void checkFindAllShouldReturnAllCommentsData() {
        int expectedPageCommentQuantity = 40;

        Pageable inputPageable = Pageable.unpaged();
        Searching inputSearching = new SearchingImpl(null);

        Page<Comment> actualPageComment = commentRepository.findAll(inputPageable,
                inputSearching);

        assertThat(actualPageComment).hasSize(expectedPageCommentQuantity);
    }

    @ParameterizedTest
    @MethodSource("getDataForFindAll")
    public void checkFindAllShouldReturnCommentsWithPageableAndSearching(Pageable pageable,
                                                                     Searching searching,
                                                                     Long expectedFirstCommentId) {
        Comment expectedComment = testEntityManager.find(Comment.class, expectedFirstCommentId);

        Page<Comment> actualPagePage = commentRepository.findAll(pageable,
                searching);

        assertThat(actualPagePage.stream().findFirst().get())
                .isEqualTo(expectedComment);
    }


    /**
     * Get pageable and searching data
     * for testing findAll method
     * <p>
     * Returns stream of arguments that contains
     * pageable, searching data in various variants
     * for findAll method arguments
     * and
     * expected first comment id from findAll method
     *
     *
     * @return pageable, searching data
     * and
     * expected first comment id from result of findAll method
     */
    private static Stream<Arguments> getDataForFindAll() {
        String sortingField = CommentTestData.COMMENT_SORTING_FIELD;
        Sort.Direction sortDirection = Sort.Direction.ASC;
        String searchingParam = CommentTestData.COMMENT_SEARCHING_FIELD;
        int pageNumber = CommentTestData.COMMENT_PAGE;
        int pageSize = CommentTestData.COMMENT_PAGE_SIZE;

        return Stream.of(
                Arguments.of(PageRequest.of(pageNumber, pageSize),
                        new SearchingImpl(null),
                        1L),

                Arguments.of(PageRequest.of(pageNumber,
                                pageSize,
                                Sort.by(sortDirection, sortingField)),
                        new SearchingImpl(null),
                        36L),

                Arguments.of(PageRequest.of(pageNumber,
                                pageSize),
                        new SearchingImpl(searchingParam),
                        1L),

                Arguments.of(PageRequest.of(pageNumber,
                                pageSize,
                                Sort.by(sortDirection, sortingField)),
                        new SearchingImpl(searchingParam),
                        38L)
        );
    }


}
