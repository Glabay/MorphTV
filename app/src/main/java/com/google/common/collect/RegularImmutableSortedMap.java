package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class RegularImmutableSortedMap<K, V> extends ImmutableSortedMap<K, V> {
    private final transient RegularImmutableSortedSet<K> keySet;
    private final transient ImmutableList<V> valueList;

    private class EntrySet extends ImmutableMapEntrySet<K, V> {

        /* renamed from: com.google.common.collect.RegularImmutableSortedMap$EntrySet$1 */
        class C10541 extends ImmutableAsList<Entry<K, V>> {
            private final ImmutableList<K> keyList = RegularImmutableSortedMap.this.keySet().asList();

            C10541() {
            }

            public Entry<K, V> get(int i) {
                return Maps.immutableEntry(this.keyList.get(i), RegularImmutableSortedMap.this.valueList.get(i));
            }

            ImmutableCollection<Entry<K, V>> delegateCollection() {
                return EntrySet.this;
            }
        }

        private EntrySet() {
        }

        public UnmodifiableIterator<Entry<K, V>> iterator() {
            return asList().iterator();
        }

        ImmutableList<Entry<K, V>> createAsList() {
            return new C10541();
        }

        ImmutableMap<K, V> map() {
            return RegularImmutableSortedMap.this;
        }
    }

    RegularImmutableSortedMap(RegularImmutableSortedSet<K> regularImmutableSortedSet, ImmutableList<V> immutableList) {
        this.keySet = regularImmutableSortedSet;
        this.valueList = immutableList;
    }

    RegularImmutableSortedMap(RegularImmutableSortedSet<K> regularImmutableSortedSet, ImmutableList<V> immutableList, ImmutableSortedMap<K, V> immutableSortedMap) {
        super(immutableSortedMap);
        this.keySet = regularImmutableSortedSet;
        this.valueList = immutableList;
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return new EntrySet();
    }

    public ImmutableSortedSet<K> keySet() {
        return this.keySet;
    }

    public ImmutableCollection<V> values() {
        return this.valueList;
    }

    public V get(@Nullable Object obj) {
        obj = this.keySet.indexOf(obj);
        if (obj == -1) {
            return null;
        }
        return this.valueList.get(obj);
    }

    private ImmutableSortedMap<K, V> getSubMap(int i, int i2) {
        if (i == 0 && i2 == size()) {
            return this;
        }
        if (i == i2) {
            return ImmutableSortedMap.emptyMap(comparator());
        }
        return new RegularImmutableSortedMap((RegularImmutableSortedSet) this.keySet.getSubSet(i, i2), this.valueList.subList(i, i2));
    }

    public ImmutableSortedMap<K, V> headMap(K k, boolean z) {
        return getSubMap(false, this.keySet.headIndex(Preconditions.checkNotNull(k), z));
    }

    public ImmutableSortedMap<K, V> tailMap(K k, boolean z) {
        return getSubMap(this.keySet.tailIndex(Preconditions.checkNotNull(k), z), size());
    }

    ImmutableSortedMap<K, V> createDescendingMap() {
        return new RegularImmutableSortedMap((RegularImmutableSortedSet) this.keySet.descendingSet(), this.valueList.reverse(), this);
    }
}
