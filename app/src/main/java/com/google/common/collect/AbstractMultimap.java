package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMultimap<K, V> implements Multimap<K, V> {
    private transient Map<K, Collection<V>> asMap;
    private transient Collection<Entry<K, V>> entries;
    private transient Set<K> keySet;
    private transient Multiset<K> keys;
    private transient Collection<V> values;

    private class Entries extends Entries<K, V> {
        private Entries() {
        }

        Multimap<K, V> multimap() {
            return AbstractMultimap.this;
        }

        public Iterator<Entry<K, V>> iterator() {
            return AbstractMultimap.this.entryIterator();
        }
    }

    private class EntrySet extends Entries implements Set<Entry<K, V>> {
        private EntrySet() {
            super();
        }

        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }

        public boolean equals(@Nullable Object obj) {
            return Sets.equalsImpl(this, obj);
        }
    }

    class Values extends AbstractCollection<V> {
        Values() {
        }

        public Iterator<V> iterator() {
            return AbstractMultimap.this.valueIterator();
        }

        public int size() {
            return AbstractMultimap.this.size();
        }

        public boolean contains(@Nullable Object obj) {
            return AbstractMultimap.this.containsValue(obj);
        }

        public void clear() {
            AbstractMultimap.this.clear();
        }
    }

    abstract Map<K, Collection<V>> createAsMap();

    abstract Iterator<Entry<K, V>> entryIterator();

    AbstractMultimap() {
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsValue(@Nullable Object obj) {
        for (Collection contains : asMap().values()) {
            if (contains.contains(obj)) {
                return true;
            }
        }
        return null;
    }

    public boolean containsEntry(@Nullable Object obj, @Nullable Object obj2) {
        Collection collection = (Collection) asMap().get(obj);
        return (collection == null || collection.contains(obj2) == null) ? null : true;
    }

    public boolean remove(@Nullable Object obj, @Nullable Object obj2) {
        Collection collection = (Collection) asMap().get(obj);
        return (collection == null || collection.remove(obj2) == null) ? null : true;
    }

    public boolean put(@Nullable K k, @Nullable V v) {
        return get(k).add(v);
    }

    public boolean putAll(@Nullable K k, Iterable<? extends V> iterable) {
        Preconditions.checkNotNull(iterable);
        boolean z = false;
        if (iterable instanceof Collection) {
            Collection collection = (Collection) iterable;
            if (!(collection.isEmpty() || get(k).addAll(collection) == null)) {
                z = true;
            }
            return z;
        }
        iterable = iterable.iterator();
        if (iterable.hasNext() && Iterators.addAll(get(k), iterable) != null) {
            z = true;
        }
        return z;
    }

    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        multimap = multimap.entries().iterator();
        boolean z = false;
        while (multimap.hasNext()) {
            Entry entry = (Entry) multimap.next();
            z |= put(entry.getKey(), entry.getValue());
        }
        return z;
    }

    public Collection<V> replaceValues(@Nullable K k, Iterable<? extends V> iterable) {
        Preconditions.checkNotNull(iterable);
        Collection<V> removeAll = removeAll(k);
        putAll(k, iterable);
        return removeAll;
    }

    public Collection<Entry<K, V>> entries() {
        Collection<Entry<K, V>> collection = this.entries;
        if (collection != null) {
            return collection;
        }
        collection = createEntries();
        this.entries = collection;
        return collection;
    }

    Collection<Entry<K, V>> createEntries() {
        if (this instanceof SetMultimap) {
            return new EntrySet();
        }
        return new Entries();
    }

    public Set<K> keySet() {
        Set<K> set = this.keySet;
        if (set != null) {
            return set;
        }
        set = createKeySet();
        this.keySet = set;
        return set;
    }

    Set<K> createKeySet() {
        return new KeySet(asMap());
    }

    public Multiset<K> keys() {
        Multiset<K> multiset = this.keys;
        if (multiset != null) {
            return multiset;
        }
        multiset = createKeys();
        this.keys = multiset;
        return multiset;
    }

    Multiset<K> createKeys() {
        return new Keys(this);
    }

    public Collection<V> values() {
        Collection<V> collection = this.values;
        if (collection != null) {
            return collection;
        }
        collection = createValues();
        this.values = collection;
        return collection;
    }

    Collection<V> createValues() {
        return new Values();
    }

    Iterator<V> valueIterator() {
        return Maps.valueIterator(entries().iterator());
    }

    public Map<K, Collection<V>> asMap() {
        Map<K, Collection<V>> map = this.asMap;
        if (map != null) {
            return map;
        }
        map = createAsMap();
        this.asMap = map;
        return map;
    }

    public boolean equals(@Nullable Object obj) {
        return Multimaps.equalsImpl(this, obj);
    }

    public int hashCode() {
        return asMap().hashCode();
    }

    public String toString() {
        return asMap().toString();
    }
}
