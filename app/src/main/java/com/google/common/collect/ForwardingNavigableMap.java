package com.google.common.collect;

import com.google.common.annotations.Beta;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.SortedMap;

public abstract class ForwardingNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V> {

    @Beta
    protected class StandardDescendingMap extends DescendingMap<K, V> {

        /* renamed from: com.google.common.collect.ForwardingNavigableMap$StandardDescendingMap$1 */
        class C09051 implements Iterator<Entry<K, V>> {
            private Entry<K, V> nextOrNull = StandardDescendingMap.this.forward().lastEntry();
            private Entry<K, V> toRemove = null;

            C09051() {
            }

            public boolean hasNext() {
                return this.nextOrNull != null;
            }

            public Entry<K, V> next() {
                if (hasNext()) {
                    try {
                        Entry<K, V> entry = this.nextOrNull;
                        return entry;
                    } finally {
                        this.toRemove = this.nextOrNull;
                        this.nextOrNull = StandardDescendingMap.this.forward().lowerEntry(this.nextOrNull.getKey());
                    }
                } else {
                    throw new NoSuchElementException();
                }
            }

            public void remove() {
                CollectPreconditions.checkRemove(this.toRemove != null);
                StandardDescendingMap.this.forward().remove(this.toRemove.getKey());
                this.toRemove = null;
            }
        }

        NavigableMap<K, V> forward() {
            return ForwardingNavigableMap.this;
        }

        protected Iterator<Entry<K, V>> entryIterator() {
            return new C09051();
        }
    }

    @Beta
    protected class StandardNavigableKeySet extends NavigableKeySet<K, V> {
        public StandardNavigableKeySet() {
            super(ForwardingNavigableMap.this);
        }
    }

    protected abstract NavigableMap<K, V> delegate();

    protected ForwardingNavigableMap() {
    }

    public Entry<K, V> lowerEntry(K k) {
        return delegate().lowerEntry(k);
    }

    protected Entry<K, V> standardLowerEntry(K k) {
        return headMap(k, false).lastEntry();
    }

    public K lowerKey(K k) {
        return delegate().lowerKey(k);
    }

    protected K standardLowerKey(K k) {
        return Maps.keyOrNull(lowerEntry(k));
    }

    public Entry<K, V> floorEntry(K k) {
        return delegate().floorEntry(k);
    }

    protected Entry<K, V> standardFloorEntry(K k) {
        return headMap(k, true).lastEntry();
    }

    public K floorKey(K k) {
        return delegate().floorKey(k);
    }

    protected K standardFloorKey(K k) {
        return Maps.keyOrNull(floorEntry(k));
    }

    public Entry<K, V> ceilingEntry(K k) {
        return delegate().ceilingEntry(k);
    }

    protected Entry<K, V> standardCeilingEntry(K k) {
        return tailMap(k, true).firstEntry();
    }

    public K ceilingKey(K k) {
        return delegate().ceilingKey(k);
    }

    protected K standardCeilingKey(K k) {
        return Maps.keyOrNull(ceilingEntry(k));
    }

    public Entry<K, V> higherEntry(K k) {
        return delegate().higherEntry(k);
    }

    protected Entry<K, V> standardHigherEntry(K k) {
        return tailMap(k, false).firstEntry();
    }

    public K higherKey(K k) {
        return delegate().higherKey(k);
    }

    protected K standardHigherKey(K k) {
        return Maps.keyOrNull(higherEntry(k));
    }

    public Entry<K, V> firstEntry() {
        return delegate().firstEntry();
    }

    protected Entry<K, V> standardFirstEntry() {
        return (Entry) Iterables.getFirst(entrySet(), null);
    }

    protected K standardFirstKey() {
        Entry firstEntry = firstEntry();
        if (firstEntry != null) {
            return firstEntry.getKey();
        }
        throw new NoSuchElementException();
    }

    public Entry<K, V> lastEntry() {
        return delegate().lastEntry();
    }

    protected Entry<K, V> standardLastEntry() {
        return (Entry) Iterables.getFirst(descendingMap().entrySet(), null);
    }

    protected K standardLastKey() {
        Entry lastEntry = lastEntry();
        if (lastEntry != null) {
            return lastEntry.getKey();
        }
        throw new NoSuchElementException();
    }

    public Entry<K, V> pollFirstEntry() {
        return delegate().pollFirstEntry();
    }

    protected Entry<K, V> standardPollFirstEntry() {
        return (Entry) Iterators.pollNext(entrySet().iterator());
    }

    public Entry<K, V> pollLastEntry() {
        return delegate().pollLastEntry();
    }

    protected Entry<K, V> standardPollLastEntry() {
        return (Entry) Iterators.pollNext(descendingMap().entrySet().iterator());
    }

    public NavigableMap<K, V> descendingMap() {
        return delegate().descendingMap();
    }

    public NavigableSet<K> navigableKeySet() {
        return delegate().navigableKeySet();
    }

    public NavigableSet<K> descendingKeySet() {
        return delegate().descendingKeySet();
    }

    @Beta
    protected NavigableSet<K> standardDescendingKeySet() {
        return descendingMap().navigableKeySet();
    }

    protected SortedMap<K, V> standardSubMap(K k, K k2) {
        return subMap(k, true, k2, false);
    }

    public NavigableMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
        return delegate().subMap(k, z, k2, z2);
    }

    public NavigableMap<K, V> headMap(K k, boolean z) {
        return delegate().headMap(k, z);
    }

    public NavigableMap<K, V> tailMap(K k, boolean z) {
        return delegate().tailMap(k, z);
    }

    protected SortedMap<K, V> standardHeadMap(K k) {
        return headMap(k, false);
    }

    protected SortedMap<K, V> standardTailMap(K k) {
        return tailMap(k, true);
    }
}
