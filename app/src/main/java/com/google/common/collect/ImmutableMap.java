package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {
    static final Entry<?, ?>[] EMPTY_ENTRY_ARRAY = new Entry[0];
    private transient ImmutableSet<Entry<K, V>> entrySet;
    private transient ImmutableSet<K> keySet;
    private transient ImmutableSetMultimap<K, V> multimapView;
    private transient ImmutableCollection<V> values;

    static abstract class IteratorBasedImmutableMap<K, V> extends ImmutableMap<K, V> {

        /* renamed from: com.google.common.collect.ImmutableMap$IteratorBasedImmutableMap$1 */
        class C09171 extends ImmutableMapEntrySet<K, V> {
            C09171() {
            }

            ImmutableMap<K, V> map() {
                return IteratorBasedImmutableMap.this;
            }

            public UnmodifiableIterator<Entry<K, V>> iterator() {
                return IteratorBasedImmutableMap.this.entryIterator();
            }
        }

        abstract UnmodifiableIterator<Entry<K, V>> entryIterator();

        IteratorBasedImmutableMap() {
        }

        public /* bridge */ /* synthetic */ Set entrySet() {
            return super.entrySet();
        }

        public /* bridge */ /* synthetic */ Set keySet() {
            return super.keySet();
        }

        public /* bridge */ /* synthetic */ Collection values() {
            return super.values();
        }

        ImmutableSet<Entry<K, V>> createEntrySet() {
            return new C09171();
        }
    }

    public static class Builder<K, V> {
        TerminalEntry<K, V>[] entries;
        int size;

        public Builder() {
            this(4);
        }

        Builder(int i) {
            this.entries = new TerminalEntry[i];
            this.size = 0;
        }

        private void ensureCapacity(int i) {
            if (i > this.entries.length) {
                this.entries = (TerminalEntry[]) ObjectArrays.arraysCopyOf(this.entries, com.google.common.collect.ImmutableCollection.Builder.expandedCapacity(this.entries.length, i));
            }
        }

        public Builder<K, V> put(K k, V v) {
            ensureCapacity(this.size + 1);
            k = ImmutableMap.entryOf(k, v);
            v = this.entries;
            int i = this.size;
            this.size = i + 1;
            v[i] = k;
            return this;
        }

        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            return put(entry.getKey(), entry.getValue());
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            ensureCapacity(this.size + map.size());
            return putAll(map.entrySet());
        }

        @Beta
        public Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> iterable) {
            for (Entry put : iterable) {
                put(put);
            }
            return this;
        }

        public ImmutableMap<K, V> build() {
            switch (this.size) {
                case 0:
                    return ImmutableMap.of();
                case 1:
                    return ImmutableMap.of(this.entries[0].getKey(), this.entries[0].getValue());
                default:
                    return new RegularImmutableMap(this.size, this.entries);
            }
        }
    }

    static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        private final Object[] keys;
        private final Object[] values;

        SerializedForm(ImmutableMap<?, ?> immutableMap) {
            this.keys = new Object[immutableMap.size()];
            this.values = new Object[immutableMap.size()];
            immutableMap = immutableMap.entrySet().iterator();
            int i = 0;
            while (immutableMap.hasNext()) {
                Entry entry = (Entry) immutableMap.next();
                this.keys[i] = entry.getKey();
                this.values[i] = entry.getValue();
                i++;
            }
        }

        Object readResolve() {
            return createMap(new Builder());
        }

        Object createMap(Builder<Object, Object> builder) {
            for (int i = 0; i < this.keys.length; i++) {
                builder.put(this.keys[i], this.values[i]);
            }
            return builder.build();
        }
    }

    private final class MapViewOfValuesAsSingletonSets extends IteratorBasedImmutableMap<K, ImmutableSet<V>> {
        private MapViewOfValuesAsSingletonSets() {
        }

        public int size() {
            return ImmutableMap.this.size();
        }

        public ImmutableSet<K> keySet() {
            return ImmutableMap.this.keySet();
        }

        public boolean containsKey(@Nullable Object obj) {
            return ImmutableMap.this.containsKey(obj);
        }

        public ImmutableSet<V> get(@Nullable Object obj) {
            obj = ImmutableMap.this.get(obj);
            if (obj == null) {
                return null;
            }
            return ImmutableSet.of(obj);
        }

        boolean isPartialView() {
            return ImmutableMap.this.isPartialView();
        }

        public int hashCode() {
            return ImmutableMap.this.hashCode();
        }

        boolean isHashCodeFast() {
            return ImmutableMap.this.isHashCodeFast();
        }

        UnmodifiableIterator<Entry<K, ImmutableSet<V>>> entryIterator() {
            final Iterator it = ImmutableMap.this.entrySet().iterator();
            return new UnmodifiableIterator<Entry<K, ImmutableSet<V>>>() {
                public boolean hasNext() {
                    return it.hasNext();
                }

                public Entry<K, ImmutableSet<V>> next() {
                    final Entry entry = (Entry) it.next();
                    return new AbstractMapEntry<K, ImmutableSet<V>>() {
                        public K getKey() {
                            return entry.getKey();
                        }

                        public ImmutableSet<V> getValue() {
                            return ImmutableSet.of(entry.getValue());
                        }
                    };
                }
            };
        }
    }

    abstract ImmutableSet<Entry<K, V>> createEntrySet();

    public abstract V get(@Nullable Object obj);

    boolean isHashCodeFast() {
        return false;
    }

    abstract boolean isPartialView();

    public static <K, V> ImmutableMap<K, V> of() {
        return ImmutableBiMap.of();
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v) {
        return ImmutableBiMap.of(k, v);
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2) {
        return new RegularImmutableMap(entryOf(k, v), entryOf(k2, v2));
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3) {
        return new RegularImmutableMap(entryOf(k, v), entryOf(k2, v2), entryOf(k3, v3));
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4) {
        return new RegularImmutableMap(entryOf(k, v), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4));
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return new RegularImmutableMap(entryOf(k, v), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5));
    }

    static <K, V> TerminalEntry<K, V> entryOf(K k, V v) {
        return new TerminalEntry(k, v);
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    static void checkNoConflict(boolean z, String str, Entry<?, ?> entry, Entry<?, ?> entry2) {
        if (!z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Multiple entries with same ");
            stringBuilder.append(str);
            stringBuilder.append(": ");
            stringBuilder.append(entry);
            stringBuilder.append(" and ");
            stringBuilder.append(entry2);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        if ((map instanceof ImmutableMap) && !(map instanceof ImmutableSortedMap)) {
            ImmutableMap<K, V> immutableMap = (ImmutableMap) map;
            if (!immutableMap.isPartialView()) {
                return immutableMap;
            }
        } else if (map instanceof EnumMap) {
            return copyOfEnumMapUnsafe(map);
        }
        return copyOf(map.entrySet());
    }

    @Beta
    public static <K, V> ImmutableMap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> iterable) {
        Entry[] entryArr = (Entry[]) Iterables.toArray((Iterable) iterable, EMPTY_ENTRY_ARRAY);
        switch (entryArr.length) {
            case 0:
                return of();
            case 1:
                iterable = entryArr[0];
                return of(iterable.getKey(), iterable.getValue());
            default:
                return new RegularImmutableMap(entryArr);
        }
    }

    private static <K, V> ImmutableMap<K, V> copyOfEnumMapUnsafe(Map<? extends K, ? extends V> map) {
        return copyOfEnumMap((EnumMap) map);
    }

    private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(EnumMap<K, ? extends V> enumMap) {
        EnumMap enumMap2 = new EnumMap(enumMap);
        enumMap = enumMap2.entrySet().iterator();
        while (enumMap.hasNext()) {
            Entry entry = (Entry) enumMap.next();
            CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
        }
        return ImmutableEnumMap.asImmutable(enumMap2);
    }

    ImmutableMap() {
    }

    @Deprecated
    public final V put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final V remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(@Nullable Object obj) {
        return get(obj) != null ? true : null;
    }

    public boolean containsValue(@Nullable Object obj) {
        return values().contains(obj);
    }

    public ImmutableSet<Entry<K, V>> entrySet() {
        ImmutableSet<Entry<K, V>> immutableSet = this.entrySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        immutableSet = createEntrySet();
        this.entrySet = immutableSet;
        return immutableSet;
    }

    public ImmutableSet<K> keySet() {
        ImmutableSet<K> immutableSet = this.keySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        immutableSet = createKeySet();
        this.keySet = immutableSet;
        return immutableSet;
    }

    ImmutableSet<K> createKeySet() {
        return new ImmutableMapKeySet(this);
    }

    UnmodifiableIterator<K> keyIterator() {
        final UnmodifiableIterator it = entrySet().iterator();
        return new UnmodifiableIterator<K>() {
            public boolean hasNext() {
                return it.hasNext();
            }

            public K next() {
                return ((Entry) it.next()).getKey();
            }
        };
    }

    public ImmutableCollection<V> values() {
        ImmutableCollection<V> immutableCollection = this.values;
        if (immutableCollection != null) {
            return immutableCollection;
        }
        immutableCollection = new ImmutableMapValues(this);
        this.values = immutableCollection;
        return immutableCollection;
    }

    @Beta
    public ImmutableSetMultimap<K, V> asMultimap() {
        ImmutableSetMultimap<K, V> immutableSetMultimap = this.multimapView;
        if (immutableSetMultimap != null) {
            return immutableSetMultimap;
        }
        immutableSetMultimap = new ImmutableSetMultimap(new MapViewOfValuesAsSingletonSets(), size(), null);
        this.multimapView = immutableSetMultimap;
        return immutableSetMultimap;
    }

    public boolean equals(@Nullable Object obj) {
        return Maps.equalsImpl(this, obj);
    }

    public int hashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    public String toString() {
        return Maps.toStringImpl(this);
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
