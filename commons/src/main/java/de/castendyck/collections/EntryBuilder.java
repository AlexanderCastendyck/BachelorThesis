package de.castendyck.collections;

public class EntryBuilder<K, V> {
    private Object key;
    private Object value;

    public static EntryBuilder anEntry() {
        return new EntryBuilder();
    }

    public EntryBuilder withKey(Object key){
        this.key = key;
        return this;
    }

    public EntryBuilder withValue(Object value){
        this.value = value;
        return this;
    }

    public Entry<K, V> build(){
        final Entry<K, V> entry = new Entry<>((K) key, (V) value);
        return entry;
    }
}
