package com.mega.revelationfix.util.java;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EmptyHashMap<K, V> extends HashMap<K, V> {
    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return defaultValue;
    }

    @Override
    public Collection<V> values() {
        Collection<V> v = super.values();
        v.clear();
        return v;
    }
}
