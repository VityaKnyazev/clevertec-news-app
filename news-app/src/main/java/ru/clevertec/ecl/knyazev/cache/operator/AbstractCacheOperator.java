package ru.clevertec.ecl.knyazev.cache.operator;

import lombok.RequiredArgsConstructor;
import ru.clevertec.ecl.knyazev.cache.Cache;

import java.util.Optional;

/**
 * Represents operator for working with cache
 *
 * @param <K> cache key type (id, uuid etc.)
 * @param <V> cache value type(entity or other objects)
 */
@RequiredArgsConstructor
public abstract class AbstractCacheOperator<K, V> {

    protected final Cache<K, V> cache;

    /**
     * find value (entity or another object) from cache
     *
     * @param key cache key (id, uuid etc.) to find value
     * @return optional cache value or optional empty - if not found
     */
    public Optional<V> find(K key) {
        V cacheEntity = cache.get(key);

        return cacheEntity != null
                ? Optional.of(cacheEntity)
                : Optional.empty();
    }

    /**
     * Add key-value pair to cache
     *
     * @param key   cache key (id, uuid etc.) to find value
     * @param value cache value (entity or another object)
     */
    public void add(K key, V value) {
        cache.put(key, value);
    }

    /**
     * Delete value from cache by its key
     *
     * @param key cache key (id, uuid etc.) to find value
     */
    public void delete(K key) {
        cache.remove(key);
    }
}
