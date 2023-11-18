package com.google.common.collect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

final class Serialization {

    static final class FieldSetter<T> {
        private final Field field;

        private FieldSetter(Field field) {
            this.field = field;
            field.setAccessible(true);
        }

        void set(T t, Object obj) {
            try {
                this.field.set(t, obj);
            } catch (T t2) {
                throw new AssertionError(t2);
            }
        }

        void set(T t, int i) {
            try {
                this.field.set(t, Integer.valueOf(i));
            } catch (T t2) {
                throw new AssertionError(t2);
            }
        }
    }

    private Serialization() {
    }

    static int readCount(ObjectInputStream objectInputStream) throws IOException {
        return objectInputStream.readInt();
    }

    static <K, V> void writeMap(Map<K, V> map, ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeInt(map.size());
        map = map.entrySet().iterator();
        while (map.hasNext()) {
            Entry entry = (Entry) map.next();
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeObject(entry.getValue());
        }
    }

    static <K, V> void populateMap(Map<K, V> map, ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        populateMap(map, objectInputStream, objectInputStream.readInt());
    }

    static <K, V> void populateMap(Map<K, V> map, ObjectInputStream objectInputStream, int i) throws IOException, ClassNotFoundException {
        for (int i2 = 0; i2 < i; i2++) {
            map.put(objectInputStream.readObject(), objectInputStream.readObject());
        }
    }

    static <E> void writeMultiset(Multiset<E> multiset, ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeInt(multiset.entrySet().size());
        for (E e : multiset.entrySet()) {
            objectOutputStream.writeObject(e.getElement());
            objectOutputStream.writeInt(e.getCount());
        }
    }

    static <E> void populateMultiset(Multiset<E> multiset, ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        populateMultiset(multiset, objectInputStream, objectInputStream.readInt());
    }

    static <E> void populateMultiset(Multiset<E> multiset, ObjectInputStream objectInputStream, int i) throws IOException, ClassNotFoundException {
        for (int i2 = 0; i2 < i; i2++) {
            multiset.add(objectInputStream.readObject(), objectInputStream.readInt());
        }
    }

    static <K, V> void writeMultimap(Multimap<K, V> multimap, ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeInt(multimap.asMap().size());
        multimap = multimap.asMap().entrySet().iterator();
        while (multimap.hasNext()) {
            Entry entry = (Entry) multimap.next();
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeInt(((Collection) entry.getValue()).size());
            for (Object writeObject : (Collection) entry.getValue()) {
                objectOutputStream.writeObject(writeObject);
            }
        }
    }

    static <K, V> void populateMultimap(Multimap<K, V> multimap, ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        populateMultimap(multimap, objectInputStream, objectInputStream.readInt());
    }

    static <K, V> void populateMultimap(Multimap<K, V> multimap, ObjectInputStream objectInputStream, int i) throws IOException, ClassNotFoundException {
        for (int i2 = 0; i2 < i; i2++) {
            Collection collection = multimap.get(objectInputStream.readObject());
            int readInt = objectInputStream.readInt();
            for (int i3 = 0; i3 < readInt; i3++) {
                collection.add(objectInputStream.readObject());
            }
        }
    }

    static <T> FieldSetter<T> getFieldSetter(Class<T> cls, String str) {
        try {
            return new FieldSetter(cls.getDeclaredField(str));
        } catch (Class<T> cls2) {
            throw new AssertionError(cls2);
        }
    }
}
