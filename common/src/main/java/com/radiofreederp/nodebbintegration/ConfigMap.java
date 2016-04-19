package com.radiofreederp.nodebbintegration;

import java.util.HashMap;

/**
 * Created by Yari on 4/18/2016.
 */
public class ConfigMap<K,V> extends HashMap<K,V> {
    public ConfigMap<K,V> add(K key, V value) {
        this.put(key, value);
        return this;
    }
}
