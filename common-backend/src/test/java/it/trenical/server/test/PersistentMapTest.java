package it.trenical.server.test;

import it.trenical.server.structures.PersistentMapDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class PersistentMapTest {
    @TempDir
    Path tempDir;
    private Path testFile;

    @BeforeEach
    void setUp() {
        testFile = tempDir.resolve("test_data.db");
    }

    @Test
    void checkDelegationAndPersistence() {
        Map<String, String> internalMap = new HashMap<>();
        Map<String, String> persistentMap = new PersistentMapDecorator<>(internalMap, testFile.toString());

        persistentMap.put("key1", "value1");
        assertEquals("value1", internalMap.get("key1"));

        assertEquals("value1",persistentMap.get("key1"));
        assertEquals(1,persistentMap.size());

        Map<String, String> reloadedMap = new PersistentMapDecorator<>(new HashMap<>(), testFile.toString());
        assertEquals("value1",reloadedMap.get("key1"));
    }

    @Test
    void checkLoadFromFileOnInitialization() {
        new PersistentMapDecorator<>(new HashMap<>(), testFile.toString())
            .put("persisted_key", "persisted_value");

        Map<String, String> mapToDecorate = new HashMap<>();
        mapToDecorate.put("in_memory_key", "in_memory_value");
        Map<String, String> persistentMap = new PersistentMapDecorator<>(mapToDecorate, testFile.toString());


        assertEquals(1, persistentMap.size());
        assertEquals("persisted_value", persistentMap.get("persisted_key"));
        assertFalse(persistentMap.containsKey("in_memory_key"));
    }

    @Test
    void checkClearAndPersist() {
        Map<String, String> persistentMap = new PersistentMapDecorator<>(new HashMap<>(), testFile.toString());
        persistentMap.put("key1", "value1");
        persistentMap.put("key2", "value2");

        assertFalse(persistentMap.isEmpty());
        persistentMap.clear();
        assertTrue(persistentMap.isEmpty());

        Map<String, String> reloadedMap = new PersistentMapDecorator<>(new HashMap<>(), testFile.toString());
        assertTrue(reloadedMap.isEmpty());
    }

    @Test
    void checkConcurrencyWork() throws InterruptedException {
        Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        Map<String, Integer> persistentMap = new PersistentMapDecorator<>(concurrentMap, testFile.toString());

        int threadCount = 10;
        int operations = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            executor.submit(() -> {
                for (int j = 0; j < operations; j++) {
                    String key = "key-" + threadIndex + "-"+j;
                    persistentMap.put(key, j);
                }
            });
        }
        executor.shutdown();
        boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);

        assertTrue(finished);
        assertEquals(threadCount * operations, persistentMap.size());

        Map<String, Integer> reloadedMap = new PersistentMapDecorator<>(new HashMap<>(), testFile.toString());
        assertEquals(threadCount * operations, reloadedMap.size());
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < operations; j++)
                assertEquals(j, reloadedMap.get("key-" + i + "-"+j));
        }
    }

}
