package ru.clevertec.ecl.knyazev.cache.operator.impl;

import ru.clevertec.ecl.knyazev.cache.Cache;
import ru.clevertec.ecl.knyazev.cache.operator.AbstractCacheOperator;
import ru.clevertec.ecl.knyazev.entity.Comment;

import java.util.UUID;

public class CommentCacheOperator extends AbstractCacheOperator<UUID, Comment> {
    public CommentCacheOperator(Cache<UUID, Comment> cache) {
        super(cache);
    }
}
