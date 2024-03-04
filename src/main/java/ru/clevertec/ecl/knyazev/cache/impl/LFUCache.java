package ru.clevertec.ecl.knyazev.cache.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.clevertec.ecl.knyazev.cache.Cache;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Current class is a realization of LFU caching mechanism. Value is in cache
 * while quantity of using of current value is bigger than quantity of using of
 * others values. When frequency of using becomes the lowest, value will be
 * deleted from cache. will be deleted from cache.
 * <p>
 * For correct algorithm work in classes of types K,V that are used in LFUCache
 * must be overriding equals and hashcode methods.
 *
 * @author Vitya Knyazev
 *
 * @param <K> key on which V value storing in cache.
 * @param <V> value that storing in cache.
 */
@RequiredArgsConstructor
public class LFUCache<K, V> implements Cache<K, V> {
    private final Integer maxCacheSize;

    private final Map<K, V> lfuCache;
    private final Map<V, Integer> cacheElCounter;

    public LFUCache(Integer maxCacheSize) {
        this.maxCacheSize = maxCacheSize;

        lfuCache = new HashMap<>();
        cacheElCounter = new HashMap<>();
    }

    /**
     *
     * Adding V value in cache on K key using LFU algorithm mechanism.
     * Counter of V value that added in cache becomes equals to zero.
     *
     *
     * @param key on which V value storing in cache.
     * @param value that storing in cache.
     */
    @Override
    public void put(K key, V value) {
        int lfuCacheSize = lfuCache.size();

        if (lfuCache.containsKey(key)) {
            V oldValue = lfuCache.get(key);

            if (!oldValue.equals(value)) {
                lfuCache.replace(key, value);
                replaceInCacheElCounter(oldValue, value);
            }
        } else {
            if (lfuCacheSize < maxCacheSize) {
                lfuCache.put(key, value);
                cacheElCounter.put(value, 0);
            } else {
                V removingValue = cacheElCounter.entrySet().stream()
                        .min(Comparator.comparingInt(Map.Entry::getValue))
                        .get()
                        .getKey();
                K removingKey = lfuCache.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(removingValue))
                        .findFirst()
                        .get()
                        .getKey();

                if (lfuCache.remove(removingKey, removingValue)) {
                    cacheElCounter.remove(removingValue);
                    lfuCache.put(key, value);
                    cacheElCounter.put(value, 0);
                }
            }
        }
    }

    /**
     *
     * Getting V value from cache on K key using LFU algorithm mechanism.
     * Counter of V value that got from cache increasing on one.
     *
     *
     * @param key on which V value getting from cache.
     * @return value that getting from cache or null if value not found.
     */
    @Override
    public V get(K key) {
        V value = null;

        if (lfuCache.containsKey(key)) {
            value = lfuCache.get(key);
            increaseCacheElCounter(value);
        }

        return value;
    }

    /**
     *
     * Remove V value from cache on K key.
     * V value also removing from counter.
     *
     * @param key on which removing V value stored in cache.
     */
    @Override
    public void remove(K key) {
        if (lfuCache.containsKey(key)) {
            V removedValue = lfuCache.remove(key);
            cacheElCounter.remove(removedValue);
        }
    }

    /**
     *
     * Replace V value in cacheElCounter with zero counter value.
     *
     * @param oldValue V that stored in cache counter.
     * @param newValue V value that saving in cache counter with zero counter.
     */
    private void replaceInCacheElCounter(V oldValue, V newValue) {
        if (cacheElCounter.containsKey(oldValue)) {
            cacheElCounter.remove(oldValue);
            cacheElCounter.put(newValue, 0);
        }
    }

    /**
     *
     * Increase cacheElCounter counter of V value on one.
     *
     * @param value V which counter increasing  on one.
     */
    private void increaseCacheElCounter(V value) {
        Integer usageQuantity = cacheElCounter.get(value);
        usageQuantity++;
        cacheElCounter.replace(value, usageQuantity);
    }
}
