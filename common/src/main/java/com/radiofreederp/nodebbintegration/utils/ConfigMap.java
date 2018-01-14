package com.radiofreederp.nodebbintegration.utils;

import java.util.HashMap;

public class ConfigMap<K,V> extends HashMap<K,V> {
    public ConfigMap<K,V> add(K key, V value) {
        this.put(key, value);
        return this;
    }
}
