package ru.clevertec.ecl.knyazev.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.knyazev.entity.News;

import java.util.Optional;
import java.util.UUID;

public interface NewsRepository extends JpaRepository<News, Long>, NewsRepositoryCustom {
    @EntityGraph("News.comments")
    Optional<News> findByUuid(UUID newsUUID);

    void deleteByUuid(UUID newsUUID);
}
