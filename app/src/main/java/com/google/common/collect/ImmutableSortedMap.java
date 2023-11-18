package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.SortedMap;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V> implements NavigableMap<K, V> {
    private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new EmptyImmutableSortedMap(NATURAL_ORDER);
    private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
    private static final long serialVersionUID = 0;
    private transient ImmutableSortedMap<K, V> descendingMap;

    public static class Builder<K, V> extends com.google.common.collect.ImmutableMap.Builder<K, V> {
        private final Comparator<? super K> comparator;

        public Builder(Comparator<? super K> comparator) {
            this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
        }

        public Builder<K, V> put(K k, V v) {
            super.put(k, v);
            return this;
        }

        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            super.put(entry);
            return this;
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            super.putAll((Map) map);
            return this;
        }

        @Beta
        public Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> iterable) {
            super.putAll((Iterable) iterable);
            return this;
        }

        public ImmutableSortedMap<K, V> build() {
            switch (this.size) {
                case 0:
                    return ImmutableSortedMap.emptyMap(this.comparator);
                case 1:
                    return ImmutableSortedMap.of(this.comparator, this.entries[0].getKey(), this.entries[0].getValue());
                default:
                    return ImmutableSortedMap.fromEntries(this.comparator, false, this.entries, this.size);
            }
        }
    }

    private static class SerializedForm extends SerializedForm {
        private static final long serialVersionUID = 0;
        private final Comparator<Object> comparator;

        SerializedForm(ImmutableSortedMap<?, ?> immutableSortedMap) {
            super(immutableSortedMap);
            this.comparator = immutableSortedMap.comparator();
        }

        Object readResolve() {
            return createMap(new Builder(this.comparator));
        }
    }

    abstract ImmutableSortedMap<K, V> createDescendingMap();

    public abstract ImmutableSortedMap<K, V> headMap(K k, boolean z);

    public abstract ImmutableSortedSet<K> keySet();

    public abstract ImmutableSortedMap<K, V> tailMap(K k, boolean z);

    public abstract ImmutableCollection<V> values();

    static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
        if (Ordering.natural().equals(comparator)) {
            return of();
        }
        return new EmptyImmutableSortedMap(comparator);
    }

    public static <K, V> ImmutableSortedMap<K, V> of() {
        return NATURAL_EMPTY_MAP;
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k, V v) {
        return of(Ordering.natural(), k, v);
    }

    private static <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K k, V v) {
        return new RegularImmutableSortedMap(new RegularImmutableSortedSet(ImmutableList.of(k), (Comparator) Preconditions.checkNotNull(comparator)), ImmutableList.of(v));
    }

    private static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> ofEntries(TerminalEntry<K, V>... terminalEntryArr) {
        return fromEntries(Ordering.natural(), false, terminalEntryArr, terminalEntryArr.length);
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k, V v, K k2, V v2) {
        return ofEntries(ImmutableMap.entryOf(k, v), ImmutableMap.entryOf(k2, v2));
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3) {
        return ofEntries(ImmutableMap.entryOf(k, v), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3));
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4) {
        return ofEntries(ImmutableMap.entryOf(k, v), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4));
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return ofEntries(ImmutableMap.entryOf(k, v), ImmutableMap.entryOf(k2, v2), ImmutableMap.entryOf(k3, v3), ImmutableMap.entryOf(k4, v4), ImmutableMap.entryOf(k5, v5));
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        return copyOfInternal(map, (Ordering) NATURAL_ORDER);
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        return copyOfInternal(map, (Comparator) Preconditions.checkNotNull(comparator));
    }

    @Beta
    public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> iterable) {
        return copyOf((Iterable) iterable, (Ordering) NATURAL_ORDER);
    }

    @Beta
    public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> iterable, Comparator<? super K> comparator) {
        return fromEntries((Comparator) Preconditions.checkNotNull(comparator), false, iterable);
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> sortedMap) {
        Comparator comparator = sortedMap.comparator();
        if (comparator == null) {
            comparator = NATURAL_ORDER;
        }
        if (sortedMap instanceof ImmutableSortedMap) {
            ImmutableSortedMap<K, V> immutableSortedMap = (ImmutableSortedMap) sortedMap;
            if (!immutableSortedMap.isPartialView()) {
                return immutableSortedMap;
            }
        }
        return fromEntries(comparator, true, sortedMap.entrySet());
    }

    private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        boolean z = false;
        if (map instanceof SortedMap) {
            Comparator comparator2 = ((SortedMap) map).comparator();
            if (comparator2 != null) {
                z = comparator.equals(comparator2);
            } else if (comparator == NATURAL_ORDER) {
                z = true;
            }
        }
        if (z && (map instanceof ImmutableSortedMap)) {
            ImmutableSortedMap<K, V> immutableSortedMap = (ImmutableSortedMap) map;
            if (!immutableSortedMap.isPartialView()) {
                return immutableSortedMap;
            }
        }
        return fromEntries(comparator, z, map.entrySet());
    }

    private static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean z, Iterable<? extends Entry<? extends K, ? extends V>> iterable) {
        Entry[] entryArr = (Entry[]) Iterables.toArray((Iterable) iterable, EMPTY_ENTRY_ARRAY);
        return fromEntries(comparator, z, entryArr, entryArr.length);
    }

    private static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean z, Entry<K, V>[] entryArr, int i) {
        int i2 = 0;
        switch (i) {
            case 0:
                return emptyMap(comparator);
            case 1:
                return of(comparator, entryArr[0].getKey(), entryArr[0].getValue());
            default:
                Object[] objArr = new Object[i];
                Object[] objArr2 = new Object[i];
                if (z) {
                    while (i2 < i) {
                        z = entryArr[i2].getKey();
                        Object value = entryArr[i2].getValue();
                        CollectPreconditions.checkEntryNotNull(z, value);
                        objArr[i2] = z;
                        objArr2[i2] = value;
                        i2++;
                    }
                } else {
                    Arrays.sort(entryArr, 0, i, Ordering.from((Comparator) comparator).onKeys());
                    z = entryArr[0].getKey();
                    objArr[0] = z;
                    objArr2[0] = entryArr[0].getValue();
                    Object obj = z;
                    z = true;
                    while (z < i) {
                        Object key = entryArr[z].getKey();
                        Object value2 = entryArr[z].getValue();
                        CollectPreconditions.checkEntryNotNull(key, value2);
                        objArr[z] = key;
                        objArr2[z] = value2;
                        ImmutableMap.checkNoConflict(comparator.compare(obj, key) != 0, "key", entryArr[z - 1], entryArr[z]);
                        z++;
                        obj = key;
                    }
                }
                return new RegularImmutableSortedMap(new RegularImmutableSortedSet(new RegularImmutableList(objArr), comparator), new RegularImmutableList(objArr2));
        }
    }

    public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() {
        return new Builder(Ordering.natural());
    }

    public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) {
        return new Builder(comparator);
    }

    public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() {
        return new Builder(Ordering.natural().reverse());
    }

    ImmutableSortedMap() {
    }

    ImmutableSortedMap(ImmutableSortedMap<K, V> immutableSortedMap) {
        this.descendingMap = immutableSortedMap;
    }

    public int size() {
        return values().size();
    }

    public boolean containsValue(@Nullable Object obj) {
        return values().contains(obj);
    }

    boolean isPartialView() {
        if (!keySet().isPartialView()) {
            if (!values().isPartialView()) {
                return false;
            }
        }
        return true;
    }

    public ImmutableSet<Entry<K, V>> entrySet() {
        return super.entrySet();
    }

    public Comparator<? super K> comparator() {
        return keySet().comparator();
    }

    public K firstKey() {
        return keySet().first();
    }

    public K lastKey() {
        return keySet().last();
    }

    public ImmutableSortedMap<K, V> headMap(K k) {
        return headMap((Object) k, false);
    }

    public ImmutableSortedMap<K, V> subMap(K k, K k2) {
        return subMap((Object) k, true, (Object) k2, false);
    }

    public ImmutableSortedMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(k2);
        Preconditions.checkArgument(comparator().compare(k, k2) <= 0, "expected fromKey <= toKey but %s > %s", k, k2);
        return headMap((Object) k2, z2).tailMap((Object) k, z);
    }

    public ImmutableSortedMap<K, V> tailMap(K k) {
        return tailMap((Object) k, true);
    }

    public Entry<K, V> lowerEntry(K k) {
        return headMap((Object) k, false).lastEntry();
    }

    public K lowerKey(K k) {
        return Maps.keyOrNull(lowerEntry(k));
    }

    public Entry<K, V> floorEntry(K k) {
        return headMap((Object) k, true).lastEntry();
    }

    public K floorKey(K k) {
        return Maps.keyOrNull(floorEntry(k));
    }

    public Entry<K, V> ceilingEntry(K k) {
        return tailMap((Object) k, true).firstEntry();
    }

    public K ceilingKey(K k) {
        return Maps.keyOrNull(ceilingEntry(k));
    }

    public Entry<K, V> higherEntry(K k) {
        return tailMap((Object) k, false).firstEntry();
    }

    public K higherKey(K k) {
        return Maps.keyOrNull(higherEntry(k));
    }

    public Entry<K, V> firstEntry() {
        return isEmpty() ? null : (Entry) entrySet().asList().get(0);
    }

    public Entry<K, V> lastEntry() {
        return isEmpty() ? null : (Entry) entrySet().asList().get(size() - 1);
    }

    @Deprecated
    public final Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    public ImmutableSortedMap<K, V> descendingMap() {
        ImmutableSortedMap<K, V> immutableSortedMap = this.descendingMap;
        if (immutableSortedMap != null) {
            return immutableSortedMap;
        }
        immutableSortedMap = createDescendingMap();
        this.descendingMap = immutableSortedMap;
        return immutableSortedMap;
    }

    public ImmutableSortedSet<K> navigableKeySet() {
        return keySet();
    }

    public ImmutableSortedSet<K> descendingKeySet() {
        return keySet().descendingSet();
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
