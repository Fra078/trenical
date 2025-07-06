package it.trenical.server.structures;

import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PersistentMapDecorator<K,V> implements Map<K,V>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final transient Map<K, V> internalMap;
    private final transient File file;
    private final transient Object fileLock = new Object();

    public PersistentMapDecorator(Map<K,V> map, String filePath) {
        this.internalMap = map;
        this.file = new File(filePath);
        loadFromFile();
    }

    @Override
    public int size() {
        return internalMap.size();
    }

    @Override
    public boolean isEmpty() {
        return internalMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return internalMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return internalMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return internalMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        V result;
        synchronized (fileLock) {
            result = internalMap.put(key, value);
            persist();
        }
        return result;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        V result;
        synchronized (fileLock) {
            result = internalMap.putIfAbsent(key, value);
            if (result != null)
                persist();
        }
        return result;
    }

    @Override
    public V remove(Object key) {
        V result;
        synchronized (fileLock) {
            result = internalMap.remove(key);
            persist();
        }
        return result;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        synchronized (fileLock) {
            internalMap.putAll(m);
            persist();
        }
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        V result;
        synchronized (fileLock) {
            result = internalMap.computeIfPresent(key, remappingFunction);
            persist();
        }
        return result;
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        V result;
        synchronized (fileLock) {
            result = internalMap.computeIfAbsent(key, mappingFunction);
            persist();
        }
        return result;
    }

    @Override
    public void clear() {
        synchronized (fileLock) {
            internalMap.clear();
            persist();
        }
    }

    @Override
    public Set<K> keySet() {
        return internalMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return internalMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return internalMap.entrySet();
    }

    private void persist() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(internalMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        synchronized (fileLock) {
            if (file.exists() && file.length() > 0) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    Map<K, V> loadedMap = (Map<K, V>) ois.readObject();
                    internalMap.clear();
                    internalMap.putAll(loadedMap);
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Unable to load the map: " + e.getMessage());
                }
            }
        }
    }
}
