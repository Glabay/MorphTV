package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
class FilteredKeyMultimap<K, V> extends AbstractMultimap<K, V> implements FilteredMultimap<K, V> {
    final Predicate<? super K> keyPredicate;
    final Multimap<K, V> unfiltered;

    static class AddRejectingList<K, V> extends ForwardingList<V> {
        final K key;

        AddRejectingList(K k) {
            this.key = k;
        }

        public boolean add(V v) {
            add(0, v);
            return true;
        }

        public boolean addAll(Collection<? extends V> collection) {
            addAll(0, collection);
            return true;
        }

        public void add(int i, V v) {
            Preconditions.checkPositionIndex(i, null);
            v = new StringBuilder();
            v.append("Key does not satisfy predicate: ");
            v.append(this.key);
            throw new IllegalArgumentException(v.toString());
        }

        public boolean addAll(int i, Collection<? extends V> collection) {
            Preconditions.checkNotNull(collection);
            Preconditions.checkPositionIndex(i, null);
            collection = new StringBuilder();
            collection.append("Key does not satisfy predicate: ");
            collection.append(this.key);
            throw new IllegalArgumentException(collection.toString());
        }

        protected List<V> delegate() {
            return Collections.emptyList();
        }
    }

    static class AddRejectingSet<K, V> extends ForwardingSet<V> {
        final K key;

        AddRejectingSet(K k) {
            this.key = k;
        }

        public boolean add(V v) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Key does not satisfy predicate: ");
            stringBuilder.append(this.key);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public boolean addAll(Collection<? extends V> collection) {
            Preconditions.checkNotNull(collection);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Key does not satisfy predicate: ");
            stringBuilder.append(this.key);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        protected Set<V> delegate() {
            return Collections.emptySet();
        }
    }

    class Entries extends ForwardingCollection<Entry<K, V>> {
        Entries() {
        }

        protected Collection<Entry<K, V>> delegate() {
            return Collections2.filter(FilteredKeyMultimap.this.unfiltered.entries(), FilteredKeyMultimap.this.entryPredicate());
        }

        public boolean remove(@Nullable Object obj) {
            if (obj instanceof Entry) {
                Entry entry = (Entry) obj;
                if (FilteredKeyMultimap.this.unfiltered.containsKey(entry.getKey()) && FilteredKeyMultimap.this.keyPredicate.apply(entry.getKey())) {
                    return FilteredKeyMultimap.this.unfiltered.remove(entry.getKey(), entry.getValue());
                }
            }
            return null;
        }
    }

    FilteredKeyMultimap(Multimap<K, V> multimap, Predicate<? super K> predicate) {
        this.unfiltered = (Multimap) Preconditions.checkNotNull(multimap);
        this.keyPredicate = (Predicate) Preconditions.checkNotNull(predicate);
    }

    public Multimap<K, V> unfiltered() {
        return this.unfiltered;
    }

    public Predicate<? super Entry<K, V>> entryPredicate() {
        return Maps.keyPredicateOnEntries(this.keyPredicate);
    }

    public int size() {
        int i = 0;
        for (Collection size : asMap().values()) {
            i += size.size();
        }
        return i;
    }

    public boolean containsKey(@Nullable Object obj) {
        return this.unfiltered.containsKey(obj) ? this.keyPredicate.apply(obj) : null;
    }

    public Collection<V> removeAll(Object obj) {
        return containsKey(obj) ? this.unfiltered.removeAll(obj) : unmodifiableEmptyCollection();
    }

    Collection<V> unmodifiableEmptyCollection() {
        if (this.unfiltered instanceof SetMultimap) {
            return ImmutableSet.of();
        }
        return ImmutableList.of();
    }

    public void clear() {
        keySet().clear();
    }

    Set<K> createKeySet() {
        return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
    }

    public Collection<V> get(K k) {
        if (this.keyPredicate.apply(k)) {
            return this.unfiltered.get(k);
        }
        if (this.unfiltered instanceof SetMultimap) {
            return new AddRejectingSet(k);
        }
        return new AddRejectingList(k);
    }

    Iterator<Entry<K, V>> entryIterator() {
        throw new AssertionError("should never be called");
    }

    Collection<Entry<K, V>> createEntries() {
        return new Entries();
    }

    Collection<V> createValues() {
        return new FilteredMultimapValues(this);
    }

    Map<K, Collection<V>> createAsMap() {
        return Maps.filterKeys(this.unfiltered.asMap(), this.keyPredicate);
    }

    Multiset<K> createKeys() {
        return Multisets.filter(this.unfiltered.keys(), this.keyPredicate);
    }
}
