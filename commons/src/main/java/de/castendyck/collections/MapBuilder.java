package de.castendyck.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapBuilder<K, V> {
    private final List<EntryBuilder<K,V>> entryBuilders;

    public MapBuilder() {
        this.entryBuilders = new ArrayList<>();
    }

    public static MapBuilder aMap(){
        return new MapBuilder();
    }

    public MapBuilder withAnEntry(EntryBuilder entryBuilder){
        entryBuilders.add((EntryBuilder<K, V> ) entryBuilder);
        return this;
    }

    public Map<K, V> build(){

        Map<K, V> map = new HashMap<>();
        for (EntryBuilder<K, V> entryBuilder : entryBuilders) {
            final Entry<K, V> entry = entryBuilder.build();
            final K key = entry.getKey();
            final V value = entry.getValue();
            map.put(key, value);
        }

        return map;
    }
}
