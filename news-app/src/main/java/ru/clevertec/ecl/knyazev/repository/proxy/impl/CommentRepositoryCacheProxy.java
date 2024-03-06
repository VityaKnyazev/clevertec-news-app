package ru.clevertec.ecl.knyazev.repository.proxy.impl;

import org.springframework.dao.DataAccessException;
import ru.clevertec.ecl.knyazev.cache.operator.AbstractCacheOperator;
import ru.clevertec.ecl.knyazev.entity.Comment;
import ru.clevertec.ecl.knyazev.repository.CommentRepository;
import ru.clevertec.ecl.knyazev.repository.proxy.RepositoryCacheProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

public class CommentRepositoryCacheProxy extends RepositoryCacheProxy<UUID, Comment> implements InvocationHandler {

    private final CommentRepository commentRepository;

    public CommentRepositoryCacheProxy(CommentRepository commentRepository,
                                       AbstractCacheOperator<UUID, Comment> cacheOperator) {
        super(cacheOperator);

        this.commentRepository = commentRepository;
    }

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
        return cacheOperator.find(uuid)
                .or(() -> {
                    Optional<Comment> dbComment = commentRepository.findByUuid(uuid);
                    dbComment.ifPresent(commentDB -> cacheOperator.add(commentDB.getUuid(), commentDB));
                    return dbComment;
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Comment whenSaveOrUpdate(Comment savingOrUpdatingComment) throws DataAccessException {

        Comment savedOrUpdatedComment = commentRepository.save(savingOrUpdatingComment);

        cacheOperator.add(savedOrUpdatedComment.getUuid(), savedOrUpdatedComment);
        return savedOrUpdatedComment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void whenDelete(UUID deletingCommentUUID) throws DataAccessException {
        commentRepository.deleteByUuid(deletingCommentUUID);
        cacheOperator.delete(deletingCommentUUID);
    }
}
