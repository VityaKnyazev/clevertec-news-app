package ru.clevertec.ecl.knyazev.cache;

/**
 * Representation of caching mechanism using cashing algorithms.
 * For caching some V value on K key.
 *
 * @param <K> key that will be containing cached value.
 * @param <V> value that should be cashed
 * @author Vitya Knyazev
 */
public interface Cache<K, V> {

    /**
     * Put some V value to cache according to K key.
     *
     * @param key   for identifying and fetching caching value
     * @param value for caching
     */
    void put(K key, V value);

    /**
     * Get cached V value on input K key
     *
     * @param key for identifying and fetching cached value
     * @return V value from cache on input K key
     */
    V get(K key);

    /**
     * Remove cached V value on input K key
     *
     * @param key for identifying and fetching cached value
     */
    void remove(K key);
}
