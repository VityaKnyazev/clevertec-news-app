package ru.clevertec.ecl.knyazev.cache.operator.impl;

import ru.clevertec.ecl.knyazev.cache.Cache;
import ru.clevertec.ecl.knyazev.cache.operator.AbstractCacheOperator;
import ru.clevertec.ecl.knyazev.entity.News;

import java.util.UUID;

public class NewsCacheOperator extends AbstractCacheOperator<UUID, News> {
    public NewsCacheOperator(Cache<UUID, News> cache) {
        super(cache);
    }
}
