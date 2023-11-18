package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public abstract class ImmutableMultimap<K, V> extends AbstractMultimap<K, V> implements Serializable {
    private static final long serialVersionUID = 0;
    final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
    final transient int size;

    public static class Builder<K, V> {
        Multimap<K, V> builderMultimap = new BuilderMultimap();
        Comparator<? super K> keyComparator;
        Comparator<? super V> valueComparator;

        public Builder<K, V> put(K k, V v) {
            CollectPreconditions.checkEntryNotNull(k, v);
            this.builderMultimap.put(k, v);
            return this;
        }

        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            return put(entry.getKey(), entry.getValue());
        }

        @Beta
        public Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> iterable) {
            for (Entry put : iterable) {
                put(put);
            }
            return this;
        }

        public Builder<K, V> putAll(K k, Iterable<? extends V> iterable) {
            if (k == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("null key in entry: null=");
                stringBuilder.append(Iterables.toString(iterable));
                throw new NullPointerException(stringBuilder.toString());
            }
            Collection collection = this.builderMultimap.get(k);
            for (Object next : iterable) {
                CollectPreconditions.checkEntryNotNull(k, next);
                collection.add(next);
            }
            return this;
        }

        public Builder<K, V> putAll(K k, V... vArr) {
            return putAll((Object) k, Arrays.asList(vArr));
        }

        public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
            multimap = multimap.asMap().entrySet().iterator();
            while (multimap.hasNext()) {
                Entry entry = (Entry) multimap.next();
                putAll(entry.getKey(), (Iterable) entry.getValue());
            }
            return this;
        }

        public Builder<K, V> orderKeysBy(Comparator<? super K> comparator) {
            this.keyComparator = (Comparator) Preconditions.checkNotNull(comparator);
            return this;
        }

        public Builder<K, V> orderValuesBy(Comparator<? super V> comparator) {
            this.valueComparator = (Comparator) Preconditions.checkNotNull(comparator);
            return this;
        }

        public ImmutableMultimap<K, V> build() {
            if (this.valueComparator != null) {
                for (Collection collection : this.builderMultimap.asMap().values()) {
                    Collections.sort((List) collection, this.valueComparator);
                }
            }
            if (this.keyComparator != null) {
                Multimap builderMultimap = new BuilderMultimap();
                for (Entry entry : Ordering.from(this.keyComparator).onKeys().immutableSortedCopy(this.builderMultimap.asMap().entrySet())) {
                    builderMultimap.putAll(entry.getKey(), (Iterable) entry.getValue());
                }
                this.builderMultimap = builderMultimap;
            }
            return ImmutableMultimap.copyOf(this.builderMultimap);
        }
    }

    private abstract class Itr<T> extends UnmodifiableIterator<T> {
        K key;
        final Iterator<Entry<K, Collection<V>>> mapIterator;
        Iterator<V> valueIterator;

        abstract T output(K k, V v);

        private Itr() {
            this.mapIterator = ImmutableMultimap.this.asMap().entrySet().iterator();
            this.key = null;
            this.valueIterator = Iterators.emptyIterator();
        }

        public boolean hasNext() {
            if (!this.mapIterator.hasNext()) {
                if (!this.valueIterator.hasNext()) {
                    return false;
                }
            }
            return true;
        }

        public T next() {
            if (!this.valueIterator.hasNext()) {
                Entry entry = (Entry) this.mapIterator.next();
                this.key = entry.getKey();
                this.valueIterator = ((Collection) entry.getValue()).iterator();
            }
            return output(this.key, this.valueIterator.next());
        }
    }

    /* renamed from: com.google.common.collect.ImmutableMultimap$1 */
    class C09231 extends Itr<Entry<K, V>> {
        C09231() {
            super();
        }

        Entry<K, V> output(K k, V v) {
            return Maps.immutableEntry(k, v);
        }
    }

    /* renamed from: com.google.common.collect.ImmutableMultimap$2 */
    class C09242 extends Itr<V> {
        V output(K k, V v) {
            return v;
        }

        C09242() {
            super();
        }
    }

    private static class BuilderMultimap<K, V> extends AbstractMapBasedMultimap<K, V> {
        private static final long serialVersionUID = 0;

        BuilderMultimap() {
            super(new LinkedHashMap());
        }

        Collection<V> createCollection() {
            return Lists.newArrayList();
        }
    }

    private static class EntryCollection<K, V> extends ImmutableCollection<Entry<K, V>> {
        private static final long serialVersionUID = 0;
        final ImmutableMultimap<K, V> multimap;

        EntryCollection(ImmutableMultimap<K, V> immutableMultimap) {
            this.multimap = immutableMultimap;
        }

        public UnmodifiableIterator<Entry<K, V>> iterator() {
            return this.multimap.entryIterator();
        }

        boolean isPartialView() {
            return this.multimap.isPartialView();
        }

        public int size() {
            return this.multimap.size();
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Entry)) {
                return null;
            }
            Entry entry = (Entry) obj;
            return this.multimap.containsEntry(entry.getKey(), entry.getValue());
        }
    }

    @GwtIncompatible("java serialization is not supported")
    static class FieldSettersHolder {
        static final FieldSetter<ImmutableSetMultimap> EMPTY_SET_FIELD_SETTER = Serialization.getFieldSetter(ImmutableSetMultimap.class, "emptySet");
        static final FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
        static final FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");

        FieldSettersHolder() {
        }
    }

    class Keys extends ImmutableMultiset<K> {
        boolean isPartialView() {
            return true;
        }

        Keys() {
        }

        public boolean contains(@Nullable Object obj) {
            return ImmutableMultimap.this.containsKey(obj);
        }

        public int count(@Nullable Object obj) {
            Collection collection = (Collection) ImmutableMultimap.this.map.get(obj);
            if (collection == null) {
                return null;
            }
            return collection.size();
        }

        public Set<K> elementSet() {
            return ImmutableMultimap.this.keySet();
        }

        public int size() {
            return ImmutableMultimap.this.size();
        }

        Multiset.Entry<K> getEntry(int i) {
            Entry entry = (Entry) ImmutableMultimap.this.map.entrySet().asList().get(i);
            return Multisets.immutableEntry(entry.getKey(), ((Collection) entry.getValue()).size());
        }
    }

    private static final class Values<K, V> extends ImmutableCollection<V> {
        private static final long serialVersionUID = 0;
        private final transient ImmutableMultimap<K, V> multimap;

        boolean isPartialView() {
            return true;
        }

        Values(ImmutableMultimap<K, V> immutableMultimap) {
            this.multimap = immutableMultimap;
        }

        public boolean contains(@Nullable Object obj) {
            return this.multimap.containsValue(obj);
        }

        public UnmodifiableIterator<V> iterator() {
            return this.multimap.valueIterator();
        }

        @GwtIncompatible("not present in emulated superclass")
        int copyIntoArray(Object[] objArr, int i) {
            Iterator it = this.multimap.map.values().iterator();
            while (it.hasNext()) {
                i = ((ImmutableCollection) it.next()).copyIntoArray(objArr, i);
            }
            return i;
        }

        public int size() {
            return this.multimap.size();
        }
    }

    public abstract ImmutableCollection<V> get(K k);

    public abstract ImmutableMultimap<V, K> inverse();

    public /* bridge */ /* synthetic */ boolean containsEntry(Object obj, Object obj2) {
        return super.containsEntry(obj, obj2);
    }

    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public static <K, V> ImmutableMultimap<K, V> of() {
        return ImmutableListMultimap.of();
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k, V v) {
        return ImmutableListMultimap.of(k, v);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k, V v, K k2, V v2) {
        return ImmutableListMultimap.of(k, v, k2, v2);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k, V v, K k2, V v2, K k3, V v3) {
        return ImmutableListMultimap.of(k, v, k2, v2, k3, v3);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4) {
        return ImmutableListMultimap.of(k, v, k2, v2, k3, v3, k4, v4);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return ImmutableListMultimap.of(k, v, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
        if (multimap instanceof ImmutableMultimap) {
            ImmutableMultimap<K, V> immutableMultimap = (ImmutableMultimap) multimap;
            if (!immutableMultimap.isPartialView()) {
                return immutableMultimap;
            }
        }
        return ImmutableListMultimap.copyOf((Multimap) multimap);
    }

    @Beta
    public static <K, V> ImmutableMultimap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> iterable) {
        return ImmutableListMultimap.copyOf((Iterable) iterable);
    }

    ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> immutableMap, int i) {
        this.map = immutableMap;
        this.size = i;
    }

    @Deprecated
    public ImmutableCollection<V> removeAll(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public ImmutableCollection<V> replaceValues(K k, Iterable<? extends V> iterable) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public boolean put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public boolean putAll(K k, Iterable<? extends V> iterable) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public boolean remove(Object obj, Object obj2) {
        throw new UnsupportedOperationException();
    }

    boolean isPartialView() {
        return this.map.isPartialView();
    }

    public boolean containsKey(@Nullable Object obj) {
        return this.map.containsKey(obj);
    }

    public boolean containsValue(@Nullable Object obj) {
        return (obj == null || super.containsValue(obj) == null) ? null : true;
    }

    public int size() {
        return this.size;
    }

    public ImmutableSet<K> keySet() {
        return this.map.keySet();
    }

    public ImmutableMap<K, Collection<V>> asMap() {
        return this.map;
    }

    Map<K, Collection<V>> createAsMap() {
        throw new AssertionError("should never be called");
    }

    public ImmutableCollection<Entry<K, V>> entries() {
        return (ImmutableCollection) super.entries();
    }

    ImmutableCollection<Entry<K, V>> createEntries() {
        return new EntryCollection(this);
    }

    UnmodifiableIterator<Entry<K, V>> entryIterator() {
        return new C09231();
    }

    public ImmutableMultiset<K> keys() {
        return (ImmutableMultiset) super.keys();
    }

    ImmutableMultiset<K> createKeys() {
        return new Keys();
    }

    public ImmutableCollection<V> values() {
        return (ImmutableCollection) super.values();
    }

    ImmutableCollection<V> createValues() {
        return new Values(this);
    }

    UnmodifiableIterator<V> valueIterator() {
        return new C09242();
    }
}
