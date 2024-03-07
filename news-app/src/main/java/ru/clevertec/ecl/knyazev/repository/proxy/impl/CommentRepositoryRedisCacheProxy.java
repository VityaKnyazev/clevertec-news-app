package ru.clevertec.ecl.knyazev.repository.proxy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import ru.clevertec.ecl.knyazev.entity.Comment;
import ru.clevertec.ecl.knyazev.mapper.CommentMapper;
import ru.clevertec.ecl.knyazev.repository.CommentRepository;
import ru.clevertec.ecl.knyazev.repository.proxy.RepositoryCacheProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class CommentRepositoryRedisCacheProxy extends RepositoryCacheProxy<UUID, Comment> implements InvocationHandler {

    private static final String COMMENT_HASH_KEY = "comments";

    private final CommentRepository commentRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private final CommentMapper commentMapperImpl;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return super.executeProxyMethod(commentRepository,
                method,
                args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Optional<Comment> whenFindByUuid(UUID uuid) {
        Optional<Comment> dbComment;

        HashOperations<String, UUID, Comment> hashOperations = redisTemplate.opsForHash();

        if (redisTemplate.opsForHash().hasKey(COMMENT_HASH_KEY, uuid.toString())) {
            dbComment = Optional.ofNullable(hashOperations.get(COMMENT_HASH_KEY, uuid));
        } else {
            dbComment = commentRepository.findByUuid(uuid);
            dbComment.ifPresent(comment -> hashOperations.put(COMMENT_HASH_KEY,
                    uuid,
                    commentMapperImpl.toCommentWithoutNews(comment)));
        }

        return dbComment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Comment whenSaveOrUpdate(Comment savingOrUpdatingComment) throws DataAccessException {
        HashOperations<String, UUID, Comment> hashOperations = redisTemplate.opsForHash();

        Comment savedOrUpdatedComment = commentRepository.save(savingOrUpdatingComment);

        hashOperations.put(COMMENT_HASH_KEY,
                savedOrUpdatedComment.getUuid(),
                commentMapperImpl.toCommentWithoutNews(savedOrUpdatedComment));

        return savedOrUpdatedComment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void whenDelete(UUID deletingCommentUUID) throws DataAccessException {
        HashOperations<String, UUID, Comment> hashOperations = redisTemplate.opsForHash();
        commentRepository.deleteByUuid(deletingCommentUUID);
        hashOperations.delete(COMMENT_HASH_KEY, deletingCommentUUID);
    }
}
