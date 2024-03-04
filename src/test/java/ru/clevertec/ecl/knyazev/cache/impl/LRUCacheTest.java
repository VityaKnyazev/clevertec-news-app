package ru.clevertec.ecl.knyazev.cache.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LRUCacheTest {

    @Spy
    private Map<UUID, String> lruCacheSpy = new HashMap<>();

    @Spy
    private LinkedList<String> cacheOldSpy = new LinkedList<>();

    private LRUCache<UUID, String> lruCache;

    @BeforeEach
    public void setUp() {
        lruCache = new LRUCache<>(3, lruCacheSpy, cacheOldSpy);
    }

    @Test
    public void checkPutShouldAddToCache() {
        UUID inputUUID = UUID.randomUUID();
        String inputValue = "caching value";

        lruCache.put(inputUUID, inputValue);

        assertAll(
                () -> assertThat(lruCacheSpy).hasSize(1)
                        .containsOnly(Map.entry(inputUUID, inputValue)),
                () -> assertThat(cacheOldSpy).hasSize(1)
                        .contains(inputValue)
        );
    }

    @Test
    public void checkPutShouldReplaceValueOnExistingKey() {
        UUID inputUUID = UUID.randomUUID();
        String inputValue = "caching value";

        lruCache.put(inputUUID, "First cashed element");

        String replacingInputValue = "caching value second";

        lruCache.put(inputUUID, replacingInputValue);

        assertAll(
                () -> assertThat(lruCacheSpy).hasSize(1)
                        .containsOnly(Map.entry(inputUUID, replacingInputValue)),
                () -> assertThat(cacheOldSpy).hasSize(1)
                        .containsOnly(replacingInputValue)
        );
    }

    @Test
    public void checkPutShouldAddValueOnMaxHashSize() {
        UUID inputUUID1 = UUID.randomUUID();
        String inputValue1 = "First cashing element";
        UUID inputUUID2 = UUID.randomUUID();
        String inputValue2 = "Second cashing element";
        UUID inputUUID3 = UUID.randomUUID();
        String inputValue3 = "Third cashing element";


        lruCache.put(inputUUID1, inputValue1);
        lruCache.put(inputUUID2, inputValue2);
        lruCache.put(inputUUID3, inputValue3);

        UUID inputUUID4 = UUID.randomUUID();
        String inputValue4 = "Four cashing element";

        lruCache.put(inputUUID4, inputValue4);

        assertAll(
                () -> assertThat(lruCacheSpy).hasSize(3)
                        .containsKeys(inputUUID2, inputUUID3, inputUUID4)
                        .containsValues(inputValue2, inputValue3, inputValue4),
                () -> assertThat(cacheOldSpy).hasSize(3)
                        .containsExactly(inputValue4, inputValue3, inputValue2)
        );

    }

    @Test
    public void checkGetShouldReturnValueOnKey() {
        UUID inputUUID1 = UUID.randomUUID();
        String inputValue1 = "First cashing element";
        UUID inputUUID2 = UUID.randomUUID();
        String inputValue2 = "Second cashing element";
        UUID inputUUID3 = UUID.randomUUID();
        String inputValue3 = "Third cashing element";

        lruCache.put(inputUUID1, inputValue1);
        lruCache.put(inputUUID2, inputValue2);
        lruCache.put(inputUUID3, inputValue3);

        UUID inputKey = inputUUID2;
        String actualValue = lruCache.get(inputKey);

        assertAll(
                () -> assertThat(actualValue).isEqualTo(inputValue2),
                () -> assertThat(cacheOldSpy).hasSize(3)
                        .containsExactly(inputValue2, inputValue3, inputValue1)
        );
    }

    @Test
    public void checkGetShouldReturnNullOnNonExistingKey() {
        UUID inputUUID1 = UUID.randomUUID();
        String inputValue1 = "First cashing element";
        UUID inputUUID2 = UUID.randomUUID();
        String inputValue2 = "Second cashing element";
        UUID inputUUID3 = UUID.randomUUID();
        String inputValue3 = "Third cashing element";

        lruCache.put(inputUUID1, inputValue1);
        lruCache.put(inputUUID2, inputValue2);
        lruCache.put(inputUUID3, inputValue3);

        UUID inputKey = UUID.randomUUID();

        String actualValue = lruCache.get(inputKey);

        assertThat(actualValue).isNull();
    }

    @Test
    public void checkRemoveShouldRemoveFromCache() {
        UUID inputUUID1 = UUID.randomUUID();
        String inputValue1 = "First cashing element";
        UUID inputUUID2 = UUID.randomUUID();
        String inputValue2 = "Second cashing element";
        UUID inputUUID3 = UUID.randomUUID();
        String inputValue3 = "Third cashing element";

        lruCache.put(inputUUID1, inputValue1);
        lruCache.put(inputUUID2, inputValue2);
        lruCache.put(inputUUID3, inputValue3);

        UUID inputUUID = inputUUID2;
        lruCache.remove(inputUUID);

        assertAll(
                () -> assertThat(lruCacheSpy).hasSize(2)
                        .containsOnly(Map.entry(inputUUID1, inputValue1), Map.entry(inputUUID3, inputValue3)),
                () -> assertThat(cacheOldSpy).hasSize(2)
                        .containsExactly(inputValue3, inputValue1)
        );
    }
}
