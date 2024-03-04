package ru.clevertec.ecl.knyazev.cache.factory;

import ru.clevertec.ecl.knyazev.cache.Cache;
import ru.clevertec.ecl.knyazev.cache.exception.CacheException;

/**
 * Represents abstract cache factory
 */
public abstract class AbstractCacheFactory {
    protected String CACHE_ALGORITHM;
    protected Integer DEFAULT_CACHE_SIZE;

    public AbstractCacheFactory(String cacheAlgorithm, Integer cacheSize) {
        CACHE_ALGORITHM = cacheAlgorithm;
        DEFAULT_CACHE_SIZE = cacheSize;
    }

    /**
     *
     * Init cache using determined cache algorithm
     *
     * @return cache that uses determined cache algorithm
     * @param <K> cache key type
     * @param <V> cache value type
     * @throws CacheException when cache type finding error
     */
    public abstract <K, V> Cache<K,V> initCache() throws CacheException;

    /**
     * Represents types of cache algorithms
     */
    protected enum CacheAlgorithm {
        LRU, LFU, undefined;

        public static CacheAlgorithm findByName(String name) {

            CacheAlgorithm result = null;
            for (CacheAlgorithm cacheAlgorithmType : values()) {

                if (cacheAlgorithmType.name().equalsIgnoreCase(name)) {
                    result = cacheAlgorithmType;
                    break;
                }

            }

            return result != null ? result : undefined;
        }
    }
}
