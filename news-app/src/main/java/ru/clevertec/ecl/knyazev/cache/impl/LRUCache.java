package ru.clevertec.ecl.knyazev.cache.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.ecl.knyazev.cache.Cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

/**
 * Current class is a realization of LRU caching mechanism. Value is in cache
 * while using. When it becomes older than V value and K key will be deleted from
 * cache.
 * <p>
 * For correct algorithm work in classes of types K,V that are used in LRUCache
 * must be overriding equals and hashcode methods.
 *
 * @param <K> key on which V value storing in cache.
 * @param <V> value that storing in cache.
 * @author Vitya Knyazev
 */
@RequiredArgsConstructor
public class LRUCache<K, V> implements Cache<K, V> {
    private final Integer maxCacheSize;

    private final Map<K, V> lruCache;
    private final LinkedList<V> cacheOld;

    public LRUCache(Integer maxCacheSize) {
        this.maxCacheSize = maxCacheSize;

        lruCache = new HashMap<>();
        cacheOld = new LinkedList<>();
    }

    /**
     * Adding V value in cache on K key using LRU algorithm mechanism.
     * V value that added in cache becomes the youngest value.
     *
     * @param key   on which V value storing in cache.
     * @param value that storing in cache.
     */
    @Override
    public void put(K key, V value) {
        Integer lruCacheSize = lruCache.size();

        if (lruCache.containsKey(key)) {
            V oldValue = lruCache.get(key);

            if (!oldValue.equals(value)) {
                lruCache.replace(key, value);
                replaceValueInCacheOld(oldValue, value);
            }
        } else {
            if (lruCacheSize < maxCacheSize) {
                lruCache.put(key, value);
                cacheOld.addFirst(value);
            } else {
                V oldValue = cacheOld.removeLast();
                K oldKey = lruCache.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(oldValue))
                        .map(Map.Entry::getKey).findFirst().get();
                if (lruCache.remove(oldKey, oldValue)) {
                    lruCache.put(key, value);
                    cacheOld.addFirst(value);
                }
            }
        }
    }

    /**
     * Getting V value from cache on K key using LRU algorithm mechanism.
     * V value that got from cache becomes the youngest value.
     *
     * @param key on which V value getting from cache.
     * @return value that getting from cache or null if value not found.
     */
    @Override
    public V get(K key) {
        V value = null;

        if (lruCache.containsKey(key)) {
            value = lruCache.get(key);
            rebaseCacheOld(value);
        }

        return value;
    }

    /**
     * Remove V value from cache on K key.
     * V value also removing from cache old linked list.
     *
     * @param key on which removing V value stored in cache.
     */
    @Override
    public void remove(K key) {

        if (lruCache.containsKey(key)) {
            V removedValue = lruCache.remove(key);

            removeValueFromCacheOld(removedValue);
        }
    }

    /**
     * Replace value in time cache when value changing on existing K key.
     *
     * @param oldValue V value that will be replaced
     * @param newValue V value is a new value that'll be storing.
     */
    private void replaceValueInCacheOld(V oldValue, V newValue) {

        removeValueFromCacheOld(oldValue);

        cacheOld.addFirst(newValue);
    }

    /**
     * Rebasing time cache when using V value that saving in cache. Then value
     * becomes "younger".
     *
     * @param value V that should be moved to the head of cache as the most "young"
     */
    private void rebaseCacheOld(V value) {
        if (cacheOld.getLast().equals(value)) {
            cacheOld.removeLast();
        } else {
            ListIterator<V> cacheOldIterator = cacheOld.listIterator();

            while (cacheOldIterator.hasNext()) {

                if (value.equals(cacheOldIterator.next())) {
                    cacheOldIterator.remove();
                    break;
                }
            }
        }

        cacheOld.addFirst(value);
    }

    /**
     *
     * Removing value from "old" cache
     *
     * @param oldValue value to remove
     */
    private void removeValueFromCacheOld(V oldValue) {

        if (cacheOld.getLast().equals(oldValue)) {
            cacheOld.removeLast();
        } else {
            Iterator<V> cacheOldIterator = cacheOld.iterator();

            while (cacheOldIterator.hasNext()) {

                if (cacheOldIterator.next().equals(oldValue)) {
                    cacheOldIterator.remove();
                    break;
                }
            }
        }
    }
}
