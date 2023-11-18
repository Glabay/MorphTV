package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMapBasedMultimap$WrappedCollection.WrappedIterator;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
abstract class AbstractMapBasedMultimap<K, V> extends AbstractMultimap<K, V> implements Serializable {
    private static final long serialVersionUID = 2447537837011683357L;
    private transient Map<K, Collection<V>> map;
    private transient int totalSize;

    private abstract class Itr<T> implements Iterator<T> {
        Collection<V> collection = null;
        K key = null;
        final Iterator<Entry<K, Collection<V>>> keyIterator;
        Iterator<V> valueIterator = Iterators.emptyModifiableIterator();

        abstract T output(K k, V v);

        Itr() {
            this.keyIterator = AbstractMapBasedMultimap.this.map.entrySet().iterator();
        }

        public boolean hasNext() {
            if (!this.keyIterator.hasNext()) {
                if (!this.valueIterator.hasNext()) {
                    return false;
                }
            }
            return true;
        }

        public T next() {
            if (!this.valueIterator.hasNext()) {
                Entry entry = (Entry) this.keyIterator.next();
                this.key = entry.getKey();
                this.collection = (Collection) entry.getValue();
                this.valueIterator = this.collection.iterator();
            }
            return output(this.key, this.valueIterator.next());
        }

        public void remove() {
            this.valueIterator.remove();
            if (this.collection.isEmpty()) {
                this.keyIterator.remove();
            }
            AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - 1;
        }
    }

    /* renamed from: com.google.common.collect.AbstractMapBasedMultimap$1 */
    class C08681 extends Itr<V> {
        V output(K k, V v) {
            return v;
        }

        C08681() {
            super();
        }
    }

    /* renamed from: com.google.common.collect.AbstractMapBasedMultimap$2 */
    class C08692 extends Itr<Entry<K, V>> {
        C08692() {
            super();
        }

        Entry<K, V> output(K k, V v) {
            return Maps.immutableEntry(k, v);
        }
    }

    private class AsMap extends ViewCachingAbstractMap<K, Collection<V>> {
        final transient Map<K, Collection<V>> submap;

        class AsMapEntries extends EntrySet<K, Collection<V>> {
            AsMapEntries() {
            }

            Map<K, Collection<V>> map() {
                return AsMap.this;
            }

            public Iterator<Entry<K, Collection<V>>> iterator() {
                return new AsMapIterator();
            }

            public boolean contains(Object obj) {
                return Collections2.safeContains(AsMap.this.submap.entrySet(), obj);
            }

            public boolean remove(Object obj) {
                if (!contains(obj)) {
                    return null;
                }
                AbstractMapBasedMultimap.this.removeValuesForKey(((Entry) obj).getKey());
                return true;
            }
        }

        class AsMapIterator implements Iterator<Entry<K, Collection<V>>> {
            Collection<V> collection;
            final Iterator<Entry<K, Collection<V>>> delegateIterator = AsMap.this.submap.entrySet().iterator();

            AsMapIterator() {
            }

            public boolean hasNext() {
                return this.delegateIterator.hasNext();
            }

            public Entry<K, Collection<V>> next() {
                Entry entry = (Entry) this.delegateIterator.next();
                this.collection = (Collection) entry.getValue();
                return AsMap.this.wrapEntry(entry);
            }

            public void remove() {
                this.delegateIterator.remove();
                AbstractMapBasedMultimap.access$220(AbstractMapBasedMultimap.this, this.collection.size());
                this.collection.clear();
            }
        }

        AsMap(Map<K, Collection<V>> map) {
            this.submap = map;
        }

        protected Set<Entry<K, Collection<V>>> createEntrySet() {
            return new AsMapEntries();
        }

        public boolean containsKey(Object obj) {
            return Maps.safeContainsKey(this.submap, obj);
        }

        public Collection<V> get(Object obj) {
            Collection collection = (Collection) Maps.safeGet(this.submap, obj);
            if (collection == null) {
                return null;
            }
            return AbstractMapBasedMultimap.this.wrapCollection(obj, collection);
        }

        public Set<K> keySet() {
            return AbstractMapBasedMultimap.this.keySet();
        }

        public int size() {
            return this.submap.size();
        }

        public Collection<V> remove(Object obj) {
            Collection collection = (Collection) this.submap.remove(obj);
            if (collection == null) {
                return null;
            }
            Collection<V> createCollection = AbstractMapBasedMultimap.this.createCollection();
            createCollection.addAll(collection);
            AbstractMapBasedMultimap.access$220(AbstractMapBasedMultimap.this, collection.size());
            collection.clear();
            return createCollection;
        }

        public boolean equals(@Nullable Object obj) {
            if (this != obj) {
                if (this.submap.equals(obj) == null) {
                    return null;
                }
            }
            return true;
        }

        public int hashCode() {
            return this.submap.hashCode();
        }

        public String toString() {
            return this.submap.toString();
        }

        public void clear() {
            if (this.submap == AbstractMapBasedMultimap.this.map) {
                AbstractMapBasedMultimap.this.clear();
            } else {
                Iterators.clear(new AsMapIterator());
            }
        }

        Entry<K, Collection<V>> wrapEntry(Entry<K, Collection<V>> entry) {
            Object key = entry.getKey();
            return Maps.immutableEntry(key, AbstractMapBasedMultimap.this.wrapCollection(key, (Collection) entry.getValue()));
        }
    }

    private class KeySet extends KeySet<K, Collection<V>> {
        KeySet(Map<K, Collection<V>> map) {
            super(map);
        }

        public Iterator<K> iterator() {
            final Iterator it = map().entrySet().iterator();
            return new Iterator<K>() {
                Entry<K, Collection<V>> entry;

                public boolean hasNext() {
                    return it.hasNext();
                }

                public K next() {
                    this.entry = (Entry) it.next();
                    return this.entry.getKey();
                }

                public void remove() {
                    CollectPreconditions.checkRemove(this.entry != null);
                    Collection collection = (Collection) this.entry.getValue();
                    it.remove();
                    AbstractMapBasedMultimap.access$220(AbstractMapBasedMultimap.this, collection.size());
                    collection.clear();
                }
            };
        }

        public boolean remove(Object obj) {
            int size;
            Collection collection = (Collection) map().remove(obj);
            if (collection != null) {
                size = collection.size();
                collection.clear();
                AbstractMapBasedMultimap.access$220(AbstractMapBasedMultimap.this, size);
            } else {
                size = 0;
            }
            if (size > 0) {
                return true;
            }
            return false;
        }

        public void clear() {
            Iterators.clear(iterator());
        }

        public boolean containsAll(Collection<?> collection) {
            return map().keySet().containsAll(collection);
        }

        public boolean equals(@Nullable Object obj) {
            if (this != obj) {
                if (map().keySet().equals(obj) == null) {
                    return null;
                }
            }
            return true;
        }

        public int hashCode() {
            return map().keySet().hashCode();
        }
    }

    private class SortedAsMap extends AsMap implements SortedMap<K, Collection<V>> {
        SortedSet<K> sortedKeySet;

        SortedAsMap(SortedMap<K, Collection<V>> sortedMap) {
            super(sortedMap);
        }

        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap) this.submap;
        }

        public Comparator<? super K> comparator() {
            return sortedMap().comparator();
        }

        public K firstKey() {
            return sortedMap().firstKey();
        }

        public K lastKey() {
            return sortedMap().lastKey();
        }

        public SortedMap<K, Collection<V>> headMap(K k) {
            return new SortedAsMap(sortedMap().headMap(k));
        }

        public SortedMap<K, Collection<V>> subMap(K k, K k2) {
            return new SortedAsMap(sortedMap().subMap(k, k2));
        }

        public SortedMap<K, Collection<V>> tailMap(K k) {
            return new SortedAsMap(sortedMap().tailMap(k));
        }

        public SortedSet<K> keySet() {
            SortedSet<K> sortedSet = this.sortedKeySet;
            if (sortedSet != null) {
                return sortedSet;
            }
            sortedSet = createKeySet();
            this.sortedKeySet = sortedSet;
            return sortedSet;
        }

        SortedSet<K> createKeySet() {
            return new SortedKeySet(sortedMap());
        }
    }

    @GwtIncompatible("NavigableAsMap")
    class NavigableAsMap extends SortedAsMap implements NavigableMap<K, Collection<V>> {
        NavigableAsMap(NavigableMap<K, Collection<V>> navigableMap) {
            super(navigableMap);
        }

        NavigableMap<K, Collection<V>> sortedMap() {
            return (NavigableMap) super.sortedMap();
        }

        public Entry<K, Collection<V>> lowerEntry(K k) {
            k = sortedMap().lowerEntry(k);
            if (k == null) {
                return null;
            }
            return wrapEntry(k);
        }

        public K lowerKey(K k) {
            return sortedMap().lowerKey(k);
        }

        public Entry<K, Collection<V>> floorEntry(K k) {
            k = sortedMap().floorEntry(k);
            if (k == null) {
                return null;
            }
            return wrapEntry(k);
        }

        public K floorKey(K k) {
            return sortedMap().floorKey(k);
        }

        public Entry<K, Collection<V>> ceilingEntry(K k) {
            k = sortedMap().ceilingEntry(k);
            if (k == null) {
                return null;
            }
            return wrapEntry(k);
        }

        public K ceilingKey(K k) {
            return sortedMap().ceilingKey(k);
        }

        public Entry<K, Collection<V>> higherEntry(K k) {
            k = sortedMap().higherEntry(k);
            if (k == null) {
                return null;
            }
            return wrapEntry(k);
        }

        public K higherKey(K k) {
            return sortedMap().higherKey(k);
        }

        public Entry<K, Collection<V>> firstEntry() {
            Entry firstEntry = sortedMap().firstEntry();
            if (firstEntry == null) {
                return null;
            }
            return wrapEntry(firstEntry);
        }

        public Entry<K, Collection<V>> lastEntry() {
            Entry lastEntry = sortedMap().lastEntry();
            if (lastEntry == null) {
                return null;
            }
            return wrapEntry(lastEntry);
        }

        public Entry<K, Collection<V>> pollFirstEntry() {
            return pollAsMapEntry(entrySet().iterator());
        }

        public Entry<K, Collection<V>> pollLastEntry() {
            return pollAsMapEntry(descendingMap().entrySet().iterator());
        }

        Entry<K, Collection<V>> pollAsMapEntry(Iterator<Entry<K, Collection<V>>> it) {
            if (!it.hasNext()) {
                return null;
            }
            Entry entry = (Entry) it.next();
            Collection createCollection = AbstractMapBasedMultimap.this.createCollection();
            createCollection.addAll((Collection) entry.getValue());
            it.remove();
            return Maps.immutableEntry(entry.getKey(), AbstractMapBasedMultimap.this.unmodifiableCollectionSubclass(createCollection));
        }

        public NavigableMap<K, Collection<V>> descendingMap() {
            return new NavigableAsMap(sortedMap().descendingMap());
        }

        public NavigableSet<K> keySet() {
            return (NavigableSet) super.keySet();
        }

        NavigableSet<K> createKeySet() {
            return new NavigableKeySet(sortedMap());
        }

        public NavigableSet<K> navigableKeySet() {
            return keySet();
        }

        public NavigableSet<K> descendingKeySet() {
            return descendingMap().navigableKeySet();
        }

        public NavigableMap<K, Collection<V>> subMap(K k, K k2) {
            return subMap(k, true, k2, false);
        }

        public NavigableMap<K, Collection<V>> subMap(K k, boolean z, K k2, boolean z2) {
            return new NavigableAsMap(sortedMap().subMap(k, z, k2, z2));
        }

        public NavigableMap<K, Collection<V>> headMap(K k) {
            return headMap(k, false);
        }

        public NavigableMap<K, Collection<V>> headMap(K k, boolean z) {
            return new NavigableAsMap(sortedMap().headMap(k, z));
        }

        public NavigableMap<K, Collection<V>> tailMap(K k) {
            return tailMap(k, true);
        }

        public NavigableMap<K, Collection<V>> tailMap(K k, boolean z) {
            return new NavigableAsMap(sortedMap().tailMap(k, z));
        }
    }

    private class SortedKeySet extends KeySet implements SortedSet<K> {
        SortedKeySet(SortedMap<K, Collection<V>> sortedMap) {
            super(sortedMap);
        }

        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap) super.map();
        }

        public Comparator<? super K> comparator() {
            return sortedMap().comparator();
        }

        public K first() {
            return sortedMap().firstKey();
        }

        public SortedSet<K> headSet(K k) {
            return new SortedKeySet(sortedMap().headMap(k));
        }

        public K last() {
            return sortedMap().lastKey();
        }

        public SortedSet<K> subSet(K k, K k2) {
            return new SortedKeySet(sortedMap().subMap(k, k2));
        }

        public SortedSet<K> tailSet(K k) {
            return new SortedKeySet(sortedMap().tailMap(k));
        }
    }

    @GwtIncompatible("NavigableSet")
    class NavigableKeySet extends SortedKeySet implements NavigableSet<K> {
        NavigableKeySet(NavigableMap<K, Collection<V>> navigableMap) {
            super(navigableMap);
        }

        NavigableMap<K, Collection<V>> sortedMap() {
            return (NavigableMap) super.sortedMap();
        }

        public K lower(K k) {
            return sortedMap().lowerKey(k);
        }

        public K floor(K k) {
            return sortedMap().floorKey(k);
        }

        public K ceiling(K k) {
            return sortedMap().ceilingKey(k);
        }

        public K higher(K k) {
            return sortedMap().higherKey(k);
        }

        public K pollFirst() {
            return Iterators.pollNext(iterator());
        }

        public K pollLast() {
            return Iterators.pollNext(descendingIterator());
        }

        public NavigableSet<K> descendingSet() {
            return new NavigableKeySet(sortedMap().descendingMap());
        }

        public Iterator<K> descendingIterator() {
            return descendingSet().iterator();
        }

        public NavigableSet<K> headSet(K k) {
            return headSet(k, false);
        }

        public NavigableSet<K> headSet(K k, boolean z) {
            return new NavigableKeySet(sortedMap().headMap(k, z));
        }

        public NavigableSet<K> subSet(K k, K k2) {
            return subSet(k, true, k2, false);
        }

        public NavigableSet<K> subSet(K k, boolean z, K k2, boolean z2) {
            return new NavigableKeySet(sortedMap().subMap(k, z, k2, z2));
        }

        public NavigableSet<K> tailSet(K k) {
            return tailSet(k, true);
        }

        public NavigableSet<K> tailSet(K k, boolean z) {
            return new NavigableKeySet(sortedMap().tailMap(k, z));
        }
    }

    private class WrappedCollection extends AbstractCollection<V> {
        final WrappedCollection ancestor;
        final Collection<V> ancestorDelegate;
        Collection<V> delegate;
        final K key;

        class WrappedIterator implements Iterator<V> {
            final Iterator<V> delegateIterator;
            final Collection<V> originalDelegate = WrappedCollection.this.delegate;

            WrappedIterator() {
                this.delegateIterator = AbstractMapBasedMultimap.this.iteratorOrListIterator(WrappedCollection.this.delegate);
            }

            WrappedIterator(Iterator<V> it) {
                this.delegateIterator = it;
            }

            void validateIterator() {
                WrappedCollection.this.refreshIfEmpty();
                if (WrappedCollection.this.delegate != this.originalDelegate) {
                    throw new ConcurrentModificationException();
                }
            }

            public boolean hasNext() {
                validateIterator();
                return this.delegateIterator.hasNext();
            }

            public V next() {
                validateIterator();
                return this.delegateIterator.next();
            }

            public void remove() {
                this.delegateIterator.remove();
                AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - 1;
                WrappedCollection.this.removeIfEmpty();
            }

            Iterator<V> getDelegateIterator() {
                validateIterator();
                return this.delegateIterator;
            }
        }

        WrappedCollection(@Nullable K k, Collection<V> collection, @Nullable WrappedCollection wrappedCollection) {
            AbstractMapBasedMultimap abstractMapBasedMultimap;
            this.key = k;
            this.delegate = collection;
            this.ancestor = wrappedCollection;
            if (wrappedCollection == null) {
                abstractMapBasedMultimap = null;
            } else {
                abstractMapBasedMultimap = wrappedCollection.getDelegate();
            }
            this.ancestorDelegate = abstractMapBasedMultimap;
        }

        void refreshIfEmpty() {
            if (this.ancestor != null) {
                this.ancestor.refreshIfEmpty();
                if (this.ancestor.getDelegate() != this.ancestorDelegate) {
                    throw new ConcurrentModificationException();
                }
            } else if (this.delegate.isEmpty()) {
                Collection collection = (Collection) AbstractMapBasedMultimap.this.map.get(this.key);
                if (collection != null) {
                    this.delegate = collection;
                }
            }
        }

        void removeIfEmpty() {
            if (this.ancestor != null) {
                this.ancestor.removeIfEmpty();
            } else if (this.delegate.isEmpty()) {
                AbstractMapBasedMultimap.this.map.remove(this.key);
            }
        }

        K getKey() {
            return this.key;
        }

        void addToMap() {
            if (this.ancestor != null) {
                this.ancestor.addToMap();
            } else {
                AbstractMapBasedMultimap.this.map.put(this.key, this.delegate);
            }
        }

        public int size() {
            refreshIfEmpty();
            return this.delegate.size();
        }

        public boolean equals(@Nullable Object obj) {
            if (obj == this) {
                return true;
            }
            refreshIfEmpty();
            return this.delegate.equals(obj);
        }

        public int hashCode() {
            refreshIfEmpty();
            return this.delegate.hashCode();
        }

        public String toString() {
            refreshIfEmpty();
            return this.delegate.toString();
        }

        Collection<V> getDelegate() {
            return this.delegate;
        }

        public Iterator<V> iterator() {
            refreshIfEmpty();
            return new WrappedIterator();
        }

        public boolean add(V v) {
            refreshIfEmpty();
            boolean isEmpty = this.delegate.isEmpty();
            v = this.delegate.add(v);
            if (v != null) {
                AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + 1;
                if (isEmpty) {
                    addToMap();
                }
            }
            return v;
        }

        WrappedCollection getAncestor() {
            return this.ancestor;
        }

        public boolean addAll(Collection<? extends V> collection) {
            if (collection.isEmpty()) {
                return null;
            }
            int size = size();
            collection = this.delegate.addAll(collection);
            if (collection != null) {
                AbstractMapBasedMultimap.access$212(AbstractMapBasedMultimap.this, this.delegate.size() - size);
                if (size == 0) {
                    addToMap();
                }
            }
            return collection;
        }

        public boolean contains(Object obj) {
            refreshIfEmpty();
            return this.delegate.contains(obj);
        }

        public boolean containsAll(Collection<?> collection) {
            refreshIfEmpty();
            return this.delegate.containsAll(collection);
        }

        public void clear() {
            int size = size();
            if (size != 0) {
                this.delegate.clear();
                AbstractMapBasedMultimap.access$220(AbstractMapBasedMultimap.this, size);
                removeIfEmpty();
            }
        }

        public boolean remove(Object obj) {
            refreshIfEmpty();
            obj = this.delegate.remove(obj);
            if (obj != null) {
                AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - 1;
                removeIfEmpty();
            }
            return obj;
        }

        public boolean removeAll(Collection<?> collection) {
            if (collection.isEmpty()) {
                return null;
            }
            int size = size();
            collection = this.delegate.removeAll(collection);
            if (collection != null) {
                AbstractMapBasedMultimap.access$212(AbstractMapBasedMultimap.this, this.delegate.size() - size);
                removeIfEmpty();
            }
            return collection;
        }

        public boolean retainAll(Collection<?> collection) {
            Preconditions.checkNotNull(collection);
            int size = size();
            collection = this.delegate.retainAll(collection);
            if (collection != null) {
                AbstractMapBasedMultimap.access$212(AbstractMapBasedMultimap.this, this.delegate.size() - size);
                removeIfEmpty();
            }
            return collection;
        }
    }

    private class WrappedList extends WrappedCollection implements List<V> {

        private class WrappedListIterator extends WrappedIterator implements ListIterator<V> {
            WrappedListIterator() {
                super();
            }

            public WrappedListIterator(int i) {
                super(WrappedList.this.getListDelegate().listIterator(i));
            }

            private ListIterator<V> getDelegateListIterator() {
                return (ListIterator) getDelegateIterator();
            }

            public boolean hasPrevious() {
                return getDelegateListIterator().hasPrevious();
            }

            public V previous() {
                return getDelegateListIterator().previous();
            }

            public int nextIndex() {
                return getDelegateListIterator().nextIndex();
            }

            public int previousIndex() {
                return getDelegateListIterator().previousIndex();
            }

            public void set(V v) {
                getDelegateListIterator().set(v);
            }

            public void add(V v) {
                boolean isEmpty = WrappedList.this.isEmpty();
                getDelegateListIterator().add(v);
                AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + 1;
                if (isEmpty) {
                    WrappedList.this.addToMap();
                }
            }
        }

        WrappedList(@Nullable K k, List<V> list, @Nullable WrappedCollection wrappedCollection) {
            super(k, list, wrappedCollection);
        }

        List<V> getListDelegate() {
            return (List) getDelegate();
        }

        public boolean addAll(int i, Collection<? extends V> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int size = size();
            i = getListDelegate().addAll(i, collection);
            if (i != 0) {
                AbstractMapBasedMultimap.access$212(AbstractMapBasedMultimap.this, getDelegate().size() - size);
                if (size == 0) {
                    addToMap();
                }
            }
            return i;
        }

        public V get(int i) {
            refreshIfEmpty();
            return getListDelegate().get(i);
        }

        public V set(int i, V v) {
            refreshIfEmpty();
            return getListDelegate().set(i, v);
        }

        public void add(int i, V v) {
            refreshIfEmpty();
            boolean isEmpty = getDelegate().isEmpty();
            getListDelegate().add(i, v);
            AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize + 1;
            if (isEmpty) {
                addToMap();
            }
        }

        public V remove(int i) {
            refreshIfEmpty();
            i = getListDelegate().remove(i);
            AbstractMapBasedMultimap.this.totalSize = AbstractMapBasedMultimap.this.totalSize - 1;
            removeIfEmpty();
            return i;
        }

        public int indexOf(Object obj) {
            refreshIfEmpty();
            return getListDelegate().indexOf(obj);
        }

        public int lastIndexOf(Object obj) {
            refreshIfEmpty();
            return getListDelegate().lastIndexOf(obj);
        }

        public ListIterator<V> listIterator() {
            refreshIfEmpty();
            return new WrappedListIterator();
        }

        public ListIterator<V> listIterator(int i) {
            refreshIfEmpty();
            return new WrappedListIterator(i);
        }

        public List<V> subList(int i, int i2) {
            refreshIfEmpty();
            return AbstractMapBasedMultimap.this.wrapList(getKey(), getListDelegate().subList(i, i2), getAncestor() == 0 ? this : getAncestor());
        }
    }

    private class RandomAccessWrappedList extends WrappedList implements RandomAccess {
        RandomAccessWrappedList(@Nullable K k, List<V> list, @Nullable WrappedCollection wrappedCollection) {
            super(k, list, wrappedCollection);
        }
    }

    private class WrappedSortedSet extends WrappedCollection implements SortedSet<V> {
        WrappedSortedSet(@Nullable K k, SortedSet<V> sortedSet, @Nullable WrappedCollection wrappedCollection) {
            super(k, sortedSet, wrappedCollection);
        }

        SortedSet<V> getSortedSetDelegate() {
            return (SortedSet) getDelegate();
        }

        public Comparator<? super V> comparator() {
            return getSortedSetDelegate().comparator();
        }

        public V first() {
            refreshIfEmpty();
            return getSortedSetDelegate().first();
        }

        public V last() {
            refreshIfEmpty();
            return getSortedSetDelegate().last();
        }

        public SortedSet<V> headSet(V v) {
            refreshIfEmpty();
            return new WrappedSortedSet(getKey(), getSortedSetDelegate().headSet(v), getAncestor() == null ? this : getAncestor());
        }

        public SortedSet<V> subSet(V v, V v2) {
            refreshIfEmpty();
            return new WrappedSortedSet(getKey(), getSortedSetDelegate().subSet(v, v2), getAncestor() == null ? this : getAncestor());
        }

        public SortedSet<V> tailSet(V v) {
            refreshIfEmpty();
            return new WrappedSortedSet(getKey(), getSortedSetDelegate().tailSet(v), getAncestor() == null ? this : getAncestor());
        }
    }

    @GwtIncompatible("NavigableSet")
    class WrappedNavigableSet extends WrappedSortedSet implements NavigableSet<V> {
        WrappedNavigableSet(@Nullable K k, NavigableSet<V> navigableSet, @Nullable WrappedCollection wrappedCollection) {
            super(k, navigableSet, wrappedCollection);
        }

        NavigableSet<V> getSortedSetDelegate() {
            return (NavigableSet) super.getSortedSetDelegate();
        }

        public V lower(V v) {
            return getSortedSetDelegate().lower(v);
        }

        public V floor(V v) {
            return getSortedSetDelegate().floor(v);
        }

        public V ceiling(V v) {
            return getSortedSetDelegate().ceiling(v);
        }

        public V higher(V v) {
            return getSortedSetDelegate().higher(v);
        }

        public V pollFirst() {
            return Iterators.pollNext(iterator());
        }

        public V pollLast() {
            return Iterators.pollNext(descendingIterator());
        }

        private NavigableSet<V> wrap(NavigableSet<V> navigableSet) {
            return new WrappedNavigableSet(this.key, navigableSet, getAncestor() == null ? this : getAncestor());
        }

        public NavigableSet<V> descendingSet() {
            return wrap(getSortedSetDelegate().descendingSet());
        }

        public Iterator<V> descendingIterator() {
            return new WrappedIterator(getSortedSetDelegate().descendingIterator());
        }

        public NavigableSet<V> subSet(V v, boolean z, V v2, boolean z2) {
            return wrap(getSortedSetDelegate().subSet(v, z, v2, z2));
        }

        public NavigableSet<V> headSet(V v, boolean z) {
            return wrap(getSortedSetDelegate().headSet(v, z));
        }

        public NavigableSet<V> tailSet(V v, boolean z) {
            return wrap(getSortedSetDelegate().tailSet(v, z));
        }
    }

    private class WrappedSet extends WrappedCollection implements Set<V> {
        WrappedSet(@Nullable K k, Set<V> set) {
            super(k, set, null);
        }

        public boolean removeAll(Collection<?> collection) {
            if (collection.isEmpty()) {
                return null;
            }
            int size = size();
            collection = Sets.removeAllImpl((Set) this.delegate, (Collection) collection);
            if (collection != null) {
                AbstractMapBasedMultimap.access$212(AbstractMapBasedMultimap.this, this.delegate.size() - size);
                removeIfEmpty();
            }
            return collection;
        }
    }

    abstract Collection<V> createCollection();

    static /* synthetic */ int access$212(AbstractMapBasedMultimap abstractMapBasedMultimap, int i) {
        int i2 = abstractMapBasedMultimap.totalSize + i;
        abstractMapBasedMultimap.totalSize = i2;
        return i2;
    }

    static /* synthetic */ int access$220(AbstractMapBasedMultimap abstractMapBasedMultimap, int i) {
        int i2 = abstractMapBasedMultimap.totalSize - i;
        abstractMapBasedMultimap.totalSize = i2;
        return i2;
    }

    protected AbstractMapBasedMultimap(Map<K, Collection<V>> map) {
        Preconditions.checkArgument(map.isEmpty());
        this.map = map;
    }

    final void setMap(Map<K, Collection<V>> map) {
        this.map = map;
        this.totalSize = 0;
        map = map.values().iterator();
        while (map.hasNext()) {
            Collection collection = (Collection) map.next();
            Preconditions.checkArgument(collection.isEmpty() ^ 1);
            this.totalSize += collection.size();
        }
    }

    Collection<V> createUnmodifiableEmptyCollection() {
        return unmodifiableCollectionSubclass(createCollection());
    }

    Collection<V> createCollection(@Nullable K k) {
        return createCollection();
    }

    Map<K, Collection<V>> backingMap() {
        return this.map;
    }

    public int size() {
        return this.totalSize;
    }

    public boolean containsKey(@Nullable Object obj) {
        return this.map.containsKey(obj);
    }

    public boolean put(@Nullable K k, @Nullable V v) {
        Collection collection = (Collection) this.map.get(k);
        if (collection == null) {
            collection = createCollection(k);
            if (collection.add(v) != null) {
                this.totalSize += 1;
                this.map.put(k, collection);
                return true;
            }
            throw new AssertionError("New Collection violated the Collection spec");
        } else if (collection.add(v) == null) {
            return null;
        } else {
            this.totalSize += 1;
            return true;
        }
    }

    private Collection<V> getOrCreateCollection(@Nullable K k) {
        Collection<V> collection = (Collection) this.map.get(k);
        if (collection != null) {
            return collection;
        }
        collection = createCollection(k);
        this.map.put(k, collection);
        return collection;
    }

    public Collection<V> replaceValues(@Nullable K k, Iterable<? extends V> iterable) {
        iterable = iterable.iterator();
        if (!iterable.hasNext()) {
            return removeAll(k);
        }
        k = getOrCreateCollection(k);
        Collection createCollection = createCollection();
        createCollection.addAll(k);
        this.totalSize -= k.size();
        k.clear();
        while (iterable.hasNext()) {
            if (k.add(iterable.next())) {
                this.totalSize++;
            }
        }
        return unmodifiableCollectionSubclass(createCollection);
    }

    public Collection<V> removeAll(@Nullable Object obj) {
        Collection collection = (Collection) this.map.remove(obj);
        if (collection == null) {
            return createUnmodifiableEmptyCollection();
        }
        Collection createCollection = createCollection();
        createCollection.addAll(collection);
        this.totalSize -= collection.size();
        collection.clear();
        return unmodifiableCollectionSubclass(createCollection);
    }

    Collection<V> unmodifiableCollectionSubclass(Collection<V> collection) {
        if (collection instanceof SortedSet) {
            return Collections.unmodifiableSortedSet((SortedSet) collection);
        }
        if (collection instanceof Set) {
            return Collections.unmodifiableSet((Set) collection);
        }
        if (collection instanceof List) {
            return Collections.unmodifiableList((List) collection);
        }
        return Collections.unmodifiableCollection(collection);
    }

    public void clear() {
        for (Collection clear : this.map.values()) {
            clear.clear();
        }
        this.map.clear();
        this.totalSize = 0;
    }

    public Collection<V> get(@Nullable K k) {
        Collection collection = (Collection) this.map.get(k);
        if (collection == null) {
            collection = createCollection(k);
        }
        return wrapCollection(k, collection);
    }

    Collection<V> wrapCollection(@Nullable K k, Collection<V> collection) {
        if (collection instanceof SortedSet) {
            return new WrappedSortedSet(k, (SortedSet) collection, null);
        }
        if (collection instanceof Set) {
            return new WrappedSet(k, (Set) collection);
        }
        if (collection instanceof List) {
            return wrapList(k, (List) collection, null);
        }
        return new WrappedCollection(k, collection, null);
    }

    private List<V> wrapList(@Nullable K k, List<V> list, @Nullable WrappedCollection wrappedCollection) {
        return list instanceof RandomAccess ? new RandomAccessWrappedList(k, list, wrappedCollection) : new WrappedList(k, list, wrappedCollection);
    }

    private Iterator<V> iteratorOrListIterator(Collection<V> collection) {
        return collection instanceof List ? ((List) collection).listIterator() : collection.iterator();
    }

    Set<K> createKeySet() {
        return this.map instanceof SortedMap ? new SortedKeySet((SortedMap) this.map) : new KeySet(this.map);
    }

    private int removeValuesForKey(Object obj) {
        Collection collection = (Collection) Maps.safeRemove(this.map, obj);
        if (collection == null) {
            return 0;
        }
        int size = collection.size();
        collection.clear();
        this.totalSize -= size;
        return size;
    }

    public Collection<V> values() {
        return super.values();
    }

    Iterator<V> valueIterator() {
        return new C08681();
    }

    public Collection<Entry<K, V>> entries() {
        return super.entries();
    }

    Iterator<Entry<K, V>> entryIterator() {
        return new C08692();
    }

    Map<K, Collection<V>> createAsMap() {
        return this.map instanceof SortedMap ? new SortedAsMap((SortedMap) this.map) : new AsMap(this.map);
    }
}
