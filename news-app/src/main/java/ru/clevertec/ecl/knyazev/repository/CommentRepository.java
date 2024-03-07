package ru.clevertec.ecl.knyazev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.knyazev.entity.Comment;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    Optional<Comment> findByUuid(UUID commentUUID);

    void deleteByUuid(UUID commentUUID);
}
