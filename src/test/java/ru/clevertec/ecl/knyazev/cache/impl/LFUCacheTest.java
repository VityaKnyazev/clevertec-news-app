package ru.clevertec.ecl.knyazev.cache.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LFUCacheTest {

    @Spy
    private Map<UUID, String> lfuCacheSpy = new HashMap<>();

    @Spy
    private Map<String, Integer> cacheElCounter = new HashMap<>();

    private LFUCache<UUID, String> lfuCache;

    @BeforeEach
    public void setUp() {
        lfuCache = new LFUCache<>(3, lfuCacheSpy, cacheElCounter);
    }

    @Test
    public void checkPutShouldAddToCacheFirstElement() {
        UUID inputUUID = UUID.randomUUID();
        String inputValue = "caching value";

        lfuCache.put(inputUUID, inputValue);

        assertAll(
                () -> assertThat(lfuCacheSpy).hasSize(1)
                        .containsOnly(Map.entry(inputUUID, inputValue)),
                () -> assertThat(cacheElCounter).hasSize(1)
                        .containsOnly(Map.entry(inputValue, 0))
        );
    }

    @Test
    public void checkPutShouldReplaceValueOnExistingKey() {
        UUID inputUUID = UUID.randomUUID();
        String inputValue = "caching value";

        lfuCache.put(inputUUID, "First cashed element");

        String replacingInputValue = "caching value second";

        lfuCache.put(inputUUID, replacingInputValue);

        assertAll(
                () -> assertThat(lfuCacheSpy).hasSize(1)
                        .containsOnly(Map.entry(inputUUID, replacingInputValue)),
                () -> assertThat(cacheElCounter).hasSize(1)
                        .containsOnly(Map.entry(replacingInputValue, 0))
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

        lfuCache.put(inputUUID1, inputValue1);
        lfuCache.put(inputUUID2, inputValue2);
        lfuCache.put(inputUUID3, inputValue3);

        lfuCache.get(inputUUID1);
        lfuCache.get(inputUUID3);

        UUID inputUUID4 = UUID.randomUUID();
        String inputValue4 = "Four cashing element";

        lfuCache.put(inputUUID4, inputValue4);

        assertAll(
                () -> assertThat(lfuCacheSpy).hasSize(3)
                        .containsOnly(Map.entry(inputUUID1, inputValue1),
                                Map.entry(inputUUID3, inputValue3),
                                Map.entry(inputUUID4, inputValue4)),
                () -> assertThat(cacheElCounter).hasSize(3)
                        .containsOnly(Map.entry(inputValue1, 1),
                                Map.entry(inputValue3, 1),
                                Map.entry(inputValue4, 0))
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

        lfuCache.put(inputUUID1, inputValue1);
        lfuCache.put(inputUUID2, inputValue2);
        lfuCache.put(inputUUID3, inputValue3);

        UUID inputKey = inputUUID1;
        String actualValue = lfuCache.get(inputKey);

        assertAll(
                () -> assertThat(actualValue).isEqualTo(inputValue1),
                () -> assertThat(cacheElCounter).hasSize(3)
                        .containsOnly(Map.entry(inputValue1, 1),
                                Map.entry(inputValue2, 0),
                                Map.entry(inputValue3, 0))
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

        lfuCache.put(inputUUID1, inputValue1);
        lfuCache.put(inputUUID2, inputValue2);
        lfuCache.put(inputUUID3, inputValue3);

        UUID inputKey = UUID.randomUUID();

        String actualValue = lfuCache.get(inputKey);

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

        lfuCache.put(inputUUID1, inputValue1);
        lfuCache.put(inputUUID2, inputValue2);
        lfuCache.put(inputUUID3, inputValue3);

        UUID inputUUID = inputUUID1;
        lfuCache.remove(inputUUID);

        assertAll(
                () -> assertThat(lfuCacheSpy).hasSize(2)
                        .containsOnly(Map.entry(inputUUID2, inputValue2), Map.entry(inputUUID3, inputValue3)),
                () -> assertThat(cacheElCounter).hasSize(2)
                        .containsOnly(Map.entry(inputValue2, 0), Map.entry(inputValue3, 0))
        );
    }



}
