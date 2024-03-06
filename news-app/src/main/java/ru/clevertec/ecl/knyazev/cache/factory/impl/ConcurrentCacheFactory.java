package ru.clevertec.ecl.knyazev.cache.factory.impl;

import ru.clevertec.ecl.knyazev.cache.factory.AbstractCacheFactory;
import ru.clevertec.ecl.knyazev.cache.Cache;
import ru.clevertec.ecl.knyazev.cache.exception.CacheException;
import ru.clevertec.ecl.knyazev.cache.impl.LFUCache;
import ru.clevertec.ecl.knyazev.cache.impl.LRUCache;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Represents concurrent cache factory that instantiate cache on LRU or
 * LFU algorithm
 */
public class ConcurrentCacheFactory extends AbstractCacheFactory {

    public ConcurrentCacheFactory(String cacheAlgorithm, Integer cacheSize) {
        super(cacheAlgorithm, cacheSize);
    }

    /**
     * Instantiate concurrent cache that use LRU or LFU algorithm
     * <p>
     * {@inheritDoc}
     */
    @Override
    public <K, V> Cache<K, V> initCache() throws CacheException {
        CacheAlgorithm algorithm = CacheAlgorithm.findByName(CACHE_ALGORITHM.toUpperCase(Locale.getDefault()));

        return switch (algorithm) {
            case LFU -> new LFUCache<>(DEFAULT_CACHE_SIZE,
                    Collections.synchronizedMap(new HashMap<>()),
                    Collections.synchronizedMap(new HashMap<>()));
            case LRU -> new LRUCache<>(DEFAULT_CACHE_SIZE,
                    Collections.synchronizedMap(new HashMap<>()),
                    (LinkedList<V>) Collections.synchronizedList(new LinkedList<V>()));
            default -> throw new CacheException(CacheException.CACHE_INSTANTIATION_EXCEPTION);
        };
    }


}
