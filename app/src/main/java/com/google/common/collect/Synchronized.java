package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class Synchronized {

    static class SynchronizedObject implements Serializable {
        @GwtIncompatible("not needed in emulated source")
        private static final long serialVersionUID = 0;
        final Object delegate;
        final Object mutex;

        SynchronizedObject(Object obj, @Nullable Object obj2) {
            this.delegate = Preconditions.checkNotNull(obj);
            if (obj2 == null) {
                obj2 = this;
            }
            this.mutex = obj2;
        }

        Object delegate() {
            return this.delegate;
        }

        public String toString() {
            String obj;
            synchronized (this.mutex) {
                obj = this.delegate.toString();
            }
            return obj;
        }

        @GwtIncompatible("java.io.ObjectOutputStream")
        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            synchronized (this.mutex) {
                objectOutputStream.defaultWriteObject();
            }
        }
    }

    private static class SynchronizedMap<K, V> extends SynchronizedObject implements Map<K, V> {
        private static final long serialVersionUID = 0;
        transient Set<Entry<K, V>> entrySet;
        transient Set<K> keySet;
        transient Collection<V> values;

        SynchronizedMap(Map<K, V> map, @Nullable Object obj) {
            super(map, obj);
        }

        Map<K, V> delegate() {
            return (Map) super.delegate();
        }

        public void clear() {
            synchronized (this.mutex) {
                delegate().clear();
            }
        }

        public boolean containsKey(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().containsKey(obj);
            }
            return obj;
        }

        public boolean containsValue(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().containsValue(obj);
            }
            return obj;
        }

        public Set<Entry<K, V>> entrySet() {
            Set<Entry<K, V>> set;
            synchronized (this.mutex) {
                if (this.entrySet == null) {
                    this.entrySet = Synchronized.set(delegate().entrySet(), this.mutex);
                }
                set = this.entrySet;
            }
            return set;
        }

        public V get(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().get(obj);
            }
            return obj;
        }

        public boolean isEmpty() {
            boolean isEmpty;
            synchronized (this.mutex) {
                isEmpty = delegate().isEmpty();
            }
            return isEmpty;
        }

        public Set<K> keySet() {
            Set<K> set;
            synchronized (this.mutex) {
                if (this.keySet == null) {
                    this.keySet = Synchronized.set(delegate().keySet(), this.mutex);
                }
                set = this.keySet;
            }
            return set;
        }

        public V put(K k, V v) {
            synchronized (this.mutex) {
                k = delegate().put(k, v);
            }
            return k;
        }

        public void putAll(Map<? extends K, ? extends V> map) {
            synchronized (this.mutex) {
                delegate().putAll(map);
            }
        }

        public V remove(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().remove(obj);
            }
            return obj;
        }

        public int size() {
            int size;
            synchronized (this.mutex) {
                size = delegate().size();
            }
            return size;
        }

        public Collection<V> values() {
            Collection<V> collection;
            synchronized (this.mutex) {
                if (this.values == null) {
                    this.values = Synchronized.collection(delegate().values(), this.mutex);
                }
                collection = this.values;
            }
            return collection;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            synchronized (this.mutex) {
                obj = delegate().equals(obj);
            }
            return obj;
        }

        public int hashCode() {
            int hashCode;
            synchronized (this.mutex) {
                hashCode = delegate().hashCode();
            }
            return hashCode;
        }
    }

    private static class SynchronizedAsMap<K, V> extends SynchronizedMap<K, Collection<V>> {
        private static final long serialVersionUID = 0;
        transient Set<Entry<K, Collection<V>>> asMapEntrySet;
        transient Collection<Collection<V>> asMapValues;

        SynchronizedAsMap(Map<K, Collection<V>> map, @Nullable Object obj) {
            super(map, obj);
        }

        public Collection<V> get(Object obj) {
            synchronized (this.mutex) {
                Collection collection = (Collection) super.get(obj);
                if (collection == null) {
                    obj = null;
                } else {
                    obj = Synchronized.typePreservingCollection(collection, this.mutex);
                }
            }
            return obj;
        }

        public Set<Entry<K, Collection<V>>> entrySet() {
            Set<Entry<K, Collection<V>>> set;
            synchronized (this.mutex) {
                if (this.asMapEntrySet == null) {
                    this.asMapEntrySet = new SynchronizedAsMapEntries(delegate().entrySet(), this.mutex);
                }
                set = this.asMapEntrySet;
            }
            return set;
        }

        public Collection<Collection<V>> values() {
            Collection<Collection<V>> collection;
            synchronized (this.mutex) {
                if (this.asMapValues == null) {
                    this.asMapValues = new SynchronizedAsMapValues(delegate().values(), this.mutex);
                }
                collection = this.asMapValues;
            }
            return collection;
        }

        public boolean containsValue(Object obj) {
            return values().contains(obj);
        }
    }

    @VisibleForTesting
    static class SynchronizedCollection<E> extends SynchronizedObject implements Collection<E> {
        private static final long serialVersionUID = 0;

        private SynchronizedCollection(Collection<E> collection, @Nullable Object obj) {
            super(collection, obj);
        }

        Collection<E> delegate() {
            return (Collection) super.delegate();
        }

        public boolean add(E e) {
            synchronized (this.mutex) {
                e = delegate().add(e);
            }
            return e;
        }

        public boolean addAll(Collection<? extends E> collection) {
            synchronized (this.mutex) {
                collection = delegate().addAll(collection);
            }
            return collection;
        }

        public void clear() {
            synchronized (this.mutex) {
                delegate().clear();
            }
        }

        public boolean contains(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().contains(obj);
            }
            return obj;
        }

        public boolean containsAll(Collection<?> collection) {
            synchronized (this.mutex) {
                collection = delegate().containsAll(collection);
            }
            return collection;
        }

        public boolean isEmpty() {
            boolean isEmpty;
            synchronized (this.mutex) {
                isEmpty = delegate().isEmpty();
            }
            return isEmpty;
        }

        public Iterator<E> iterator() {
            return delegate().iterator();
        }

        public boolean remove(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().remove(obj);
            }
            return obj;
        }

        public boolean removeAll(Collection<?> collection) {
            synchronized (this.mutex) {
                collection = delegate().removeAll(collection);
            }
            return collection;
        }

        public boolean retainAll(Collection<?> collection) {
            synchronized (this.mutex) {
                collection = delegate().retainAll(collection);
            }
            return collection;
        }

        public int size() {
            int size;
            synchronized (this.mutex) {
                size = delegate().size();
            }
            return size;
        }

        public Object[] toArray() {
            Object[] toArray;
            synchronized (this.mutex) {
                toArray = delegate().toArray();
            }
            return toArray;
        }

        public <T> T[] toArray(T[] tArr) {
            synchronized (this.mutex) {
                tArr = delegate().toArray(tArr);
            }
            return tArr;
        }
    }

    static class SynchronizedSet<E> extends SynchronizedCollection<E> implements Set<E> {
        private static final long serialVersionUID = 0;

        SynchronizedSet(Set<E> set, @Nullable Object obj) {
            super(set, obj);
        }

        Set<E> delegate() {
            return (Set) super.delegate();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            synchronized (this.mutex) {
                obj = delegate().equals(obj);
            }
            return obj;
        }

        public int hashCode() {
            int hashCode;
            synchronized (this.mutex) {
                hashCode = delegate().hashCode();
            }
            return hashCode;
        }
    }

    private static class SynchronizedAsMapEntries<K, V> extends SynchronizedSet<Entry<K, Collection<V>>> {
        private static final long serialVersionUID = 0;

        SynchronizedAsMapEntries(Set<Entry<K, Collection<V>>> set, @Nullable Object obj) {
            super(set, obj);
        }

        public Iterator<Entry<K, Collection<V>>> iterator() {
            final Iterator it = super.iterator();
            return new ForwardingIterator<Entry<K, Collection<V>>>() {
                protected Iterator<Entry<K, Collection<V>>> delegate() {
                    return it;
                }

                public Entry<K, Collection<V>> next() {
                    final Entry entry = (Entry) super.next();
                    return new ForwardingMapEntry<K, Collection<V>>() {
                        protected Entry<K, Collection<V>> delegate() {
                            return entry;
                        }

                        public Collection<V> getValue() {
                            return Synchronized.typePreservingCollection((Collection) entry.getValue(), SynchronizedAsMapEntries.this.mutex);
                        }
                    };
                }
            };
        }

        public Object[] toArray() {
            Object[] toArrayImpl;
            synchronized (this.mutex) {
                toArrayImpl = ObjectArrays.toArrayImpl(delegate());
            }
            return toArrayImpl;
        }

        public <T> T[] toArray(T[] tArr) {
            synchronized (this.mutex) {
                tArr = ObjectArrays.toArrayImpl(delegate(), tArr);
            }
            return tArr;
        }

        public boolean contains(Object obj) {
            synchronized (this.mutex) {
                obj = Maps.containsEntryImpl(delegate(), obj);
            }
            return obj;
        }

        public boolean containsAll(Collection<?> collection) {
            synchronized (this.mutex) {
                collection = Collections2.containsAllImpl(delegate(), collection);
            }
            return collection;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            synchronized (this.mutex) {
                obj = Sets.equalsImpl(delegate(), obj);
            }
            return obj;
        }

        public boolean remove(Object obj) {
            synchronized (this.mutex) {
                obj = Maps.removeEntryImpl(delegate(), obj);
            }
            return obj;
        }

        public boolean removeAll(Collection<?> collection) {
            synchronized (this.mutex) {
                collection = Iterators.removeAll(delegate().iterator(), collection);
            }
            return collection;
        }

        public boolean retainAll(Collection<?> collection) {
            synchronized (this.mutex) {
                collection = Iterators.retainAll(delegate().iterator(), collection);
            }
            return collection;
        }
    }

    private static class SynchronizedAsMapValues<V> extends SynchronizedCollection<Collection<V>> {
        private static final long serialVersionUID = 0;

        SynchronizedAsMapValues(Collection<Collection<V>> collection, @Nullable Object obj) {
            super(collection, obj);
        }

        public Iterator<Collection<V>> iterator() {
            final Iterator it = super.iterator();
            return new ForwardingIterator<Collection<V>>() {
                protected Iterator<Collection<V>> delegate() {
                    return it;
                }

                public Collection<V> next() {
                    return Synchronized.typePreservingCollection((Collection) super.next(), SynchronizedAsMapValues.this.mutex);
                }
            };
        }
    }

    @VisibleForTesting
    static class SynchronizedBiMap<K, V> extends SynchronizedMap<K, V> implements BiMap<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        private transient BiMap<V, K> inverse;
        private transient Set<V> valueSet;

        private SynchronizedBiMap(BiMap<K, V> biMap, @Nullable Object obj, @Nullable BiMap<V, K> biMap2) {
            super(biMap, obj);
            this.inverse = biMap2;
        }

        BiMap<K, V> delegate() {
            return (BiMap) super.delegate();
        }

        public Set<V> values() {
            Set<V> set;
            synchronized (this.mutex) {
                if (this.valueSet == null) {
                    this.valueSet = Synchronized.set(delegate().values(), this.mutex);
                }
                set = this.valueSet;
            }
            return set;
        }

        public V forcePut(K k, V v) {
            synchronized (this.mutex) {
                k = delegate().forcePut(k, v);
            }
            return k;
        }

        public BiMap<V, K> inverse() {
            BiMap<V, K> biMap;
            synchronized (this.mutex) {
                if (this.inverse == null) {
                    this.inverse = new SynchronizedBiMap(delegate().inverse(), this.mutex, this);
                }
                biMap = this.inverse;
            }
            return biMap;
        }
    }

    private static class SynchronizedQueue<E> extends SynchronizedCollection<E> implements Queue<E> {
        private static final long serialVersionUID = 0;

        SynchronizedQueue(Queue<E> queue, @Nullable Object obj) {
            super(queue, obj);
        }

        Queue<E> delegate() {
            return (Queue) super.delegate();
        }

        public E element() {
            E element;
            synchronized (this.mutex) {
                element = delegate().element();
            }
            return element;
        }

        public boolean offer(E e) {
            synchronized (this.mutex) {
                e = delegate().offer(e);
            }
            return e;
        }

        public E peek() {
            E peek;
            synchronized (this.mutex) {
                peek = delegate().peek();
            }
            return peek;
        }

        public E poll() {
            E poll;
            synchronized (this.mutex) {
                poll = delegate().poll();
            }
            return poll;
        }

        public E remove() {
            E remove;
            synchronized (this.mutex) {
                remove = delegate().remove();
            }
            return remove;
        }
    }

    @GwtIncompatible("Deque")
    private static final class SynchronizedDeque<E> extends SynchronizedQueue<E> implements Deque<E> {
        private static final long serialVersionUID = 0;

        SynchronizedDeque(Deque<E> deque, @Nullable Object obj) {
            super(deque, obj);
        }

        Deque<E> delegate() {
            return (Deque) super.delegate();
        }

        public void addFirst(E e) {
            synchronized (this.mutex) {
                delegate().addFirst(e);
            }
        }

        public void addLast(E e) {
            synchronized (this.mutex) {
                delegate().addLast(e);
            }
        }

        public boolean offerFirst(E e) {
            synchronized (this.mutex) {
                e = delegate().offerFirst(e);
            }
            return e;
        }

        public boolean offerLast(E e) {
            synchronized (this.mutex) {
                e = delegate().offerLast(e);
            }
            return e;
        }

        public E removeFirst() {
            E removeFirst;
            synchronized (this.mutex) {
                removeFirst = delegate().removeFirst();
            }
            return removeFirst;
        }

        public E removeLast() {
            E removeLast;
            synchronized (this.mutex) {
                removeLast = delegate().removeLast();
            }
            return removeLast;
        }

        public E pollFirst() {
            E pollFirst;
            synchronized (this.mutex) {
                pollFirst = delegate().pollFirst();
            }
            return pollFirst;
        }

        public E pollLast() {
            E pollLast;
            synchronized (this.mutex) {
                pollLast = delegate().pollLast();
            }
            return pollLast;
        }

        public E getFirst() {
            E first;
            synchronized (this.mutex) {
                first = delegate().getFirst();
            }
            return first;
        }

        public E getLast() {
            E last;
            synchronized (this.mutex) {
                last = delegate().getLast();
            }
            return last;
        }

        public E peekFirst() {
            E peekFirst;
            synchronized (this.mutex) {
                peekFirst = delegate().peekFirst();
            }
            return peekFirst;
        }

        public E peekLast() {
            E peekLast;
            synchronized (this.mutex) {
                peekLast = delegate().peekLast();
            }
            return peekLast;
        }

        public boolean removeFirstOccurrence(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().removeFirstOccurrence(obj);
            }
            return obj;
        }

        public boolean removeLastOccurrence(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().removeLastOccurrence(obj);
            }
            return obj;
        }

        public void push(E e) {
            synchronized (this.mutex) {
                delegate().push(e);
            }
        }

        public E pop() {
            E pop;
            synchronized (this.mutex) {
                pop = delegate().pop();
            }
            return pop;
        }

        public Iterator<E> descendingIterator() {
            Iterator<E> descendingIterator;
            synchronized (this.mutex) {
                descendingIterator = delegate().descendingIterator();
            }
            return descendingIterator;
        }
    }

    @GwtIncompatible("works but is needed only for NavigableMap")
    private static class SynchronizedEntry<K, V> extends SynchronizedObject implements Entry<K, V> {
        private static final long serialVersionUID = 0;

        SynchronizedEntry(Entry<K, V> entry, @Nullable Object obj) {
            super(entry, obj);
        }

        Entry<K, V> delegate() {
            return (Entry) super.delegate();
        }

        public boolean equals(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().equals(obj);
            }
            return obj;
        }

        public int hashCode() {
            int hashCode;
            synchronized (this.mutex) {
                hashCode = delegate().hashCode();
            }
            return hashCode;
        }

        public K getKey() {
            K key;
            synchronized (this.mutex) {
                key = delegate().getKey();
            }
            return key;
        }

        public V getValue() {
            V value;
            synchronized (this.mutex) {
                value = delegate().getValue();
            }
            return value;
        }

        public V setValue(V v) {
            synchronized (this.mutex) {
                v = delegate().setValue(v);
            }
            return v;
        }
    }

    private static class SynchronizedList<E> extends SynchronizedCollection<E> implements List<E> {
        private static final long serialVersionUID = 0;

        SynchronizedList(List<E> list, @Nullable Object obj) {
            super(list, obj);
        }

        List<E> delegate() {
            return (List) super.delegate();
        }

        public void add(int i, E e) {
            synchronized (this.mutex) {
                delegate().add(i, e);
            }
        }

        public boolean addAll(int i, Collection<? extends E> collection) {
            synchronized (this.mutex) {
                i = delegate().addAll(i, collection);
            }
            return i;
        }

        public E get(int i) {
            synchronized (this.mutex) {
                i = delegate().get(i);
            }
            return i;
        }

        public int indexOf(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().indexOf(obj);
            }
            return obj;
        }

        public int lastIndexOf(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().lastIndexOf(obj);
            }
            return obj;
        }

        public ListIterator<E> listIterator() {
            return delegate().listIterator();
        }

        public ListIterator<E> listIterator(int i) {
            return delegate().listIterator(i);
        }

        public E remove(int i) {
            synchronized (this.mutex) {
                i = delegate().remove(i);
            }
            return i;
        }

        public E set(int i, E e) {
            synchronized (this.mutex) {
                i = delegate().set(i, e);
            }
            return i;
        }

        public List<E> subList(int i, int i2) {
            synchronized (this.mutex) {
                i = Synchronized.list(delegate().subList(i, i2), this.mutex);
            }
            return i;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            synchronized (this.mutex) {
                obj = delegate().equals(obj);
            }
            return obj;
        }

        public int hashCode() {
            int hashCode;
            synchronized (this.mutex) {
                hashCode = delegate().hashCode();
            }
            return hashCode;
        }
    }

    private static class SynchronizedMultimap<K, V> extends SynchronizedObject implements Multimap<K, V> {
        private static final long serialVersionUID = 0;
        transient Map<K, Collection<V>> asMap;
        transient Collection<Entry<K, V>> entries;
        transient Set<K> keySet;
        transient Multiset<K> keys;
        transient Collection<V> valuesCollection;

        Multimap<K, V> delegate() {
            return (Multimap) super.delegate();
        }

        SynchronizedMultimap(Multimap<K, V> multimap, @Nullable Object obj) {
            super(multimap, obj);
        }

        public int size() {
            int size;
            synchronized (this.mutex) {
                size = delegate().size();
            }
            return size;
        }

        public boolean isEmpty() {
            boolean isEmpty;
            synchronized (this.mutex) {
                isEmpty = delegate().isEmpty();
            }
            return isEmpty;
        }

        public boolean containsKey(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().containsKey(obj);
            }
            return obj;
        }

        public boolean containsValue(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().containsValue(obj);
            }
            return obj;
        }

        public boolean containsEntry(Object obj, Object obj2) {
            synchronized (this.mutex) {
                obj = delegate().containsEntry(obj, obj2);
            }
            return obj;
        }

        public Collection<V> get(K k) {
            synchronized (this.mutex) {
                k = Synchronized.typePreservingCollection(delegate().get(k), this.mutex);
            }
            return k;
        }

        public boolean put(K k, V v) {
            synchronized (this.mutex) {
                k = delegate().put(k, v);
            }
            return k;
        }

        public boolean putAll(K k, Iterable<? extends V> iterable) {
            synchronized (this.mutex) {
                k = delegate().putAll(k, iterable);
            }
            return k;
        }

        public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
            synchronized (this.mutex) {
                multimap = delegate().putAll(multimap);
            }
            return multimap;
        }

        public Collection<V> replaceValues(K k, Iterable<? extends V> iterable) {
            synchronized (this.mutex) {
                k = delegate().replaceValues(k, iterable);
            }
            return k;
        }

        public boolean remove(Object obj, Object obj2) {
            synchronized (this.mutex) {
                obj = delegate().remove(obj, obj2);
            }
            return obj;
        }

        public Collection<V> removeAll(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().removeAll(obj);
            }
            return obj;
        }

        public void clear() {
            synchronized (this.mutex) {
                delegate().clear();
            }
        }

        public Set<K> keySet() {
            Set<K> set;
            synchronized (this.mutex) {
                if (this.keySet == null) {
                    this.keySet = Synchronized.typePreservingSet(delegate().keySet(), this.mutex);
                }
                set = this.keySet;
            }
            return set;
        }

        public Collection<V> values() {
            Collection<V> collection;
            synchronized (this.mutex) {
                if (this.valuesCollection == null) {
                    this.valuesCollection = Synchronized.collection(delegate().values(), this.mutex);
                }
                collection = this.valuesCollection;
            }
            return collection;
        }

        public Collection<Entry<K, V>> entries() {
            Collection<Entry<K, V>> collection;
            synchronized (this.mutex) {
                if (this.entries == null) {
                    this.entries = Synchronized.typePreservingCollection(delegate().entries(), this.mutex);
                }
                collection = this.entries;
            }
            return collection;
        }

        public Map<K, Collection<V>> asMap() {
            Map<K, Collection<V>> map;
            synchronized (this.mutex) {
                if (this.asMap == null) {
                    this.asMap = new SynchronizedAsMap(delegate().asMap(), this.mutex);
                }
                map = this.asMap;
            }
            return map;
        }

        public Multiset<K> keys() {
            Multiset<K> multiset;
            synchronized (this.mutex) {
                if (this.keys == null) {
                    this.keys = Synchronized.multiset(delegate().keys(), this.mutex);
                }
                multiset = this.keys;
            }
            return multiset;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            synchronized (this.mutex) {
                obj = delegate().equals(obj);
            }
            return obj;
        }

        public int hashCode() {
            int hashCode;
            synchronized (this.mutex) {
                hashCode = delegate().hashCode();
            }
            return hashCode;
        }
    }

    private static class SynchronizedListMultimap<K, V> extends SynchronizedMultimap<K, V> implements ListMultimap<K, V> {
        private static final long serialVersionUID = 0;

        SynchronizedListMultimap(ListMultimap<K, V> listMultimap, @Nullable Object obj) {
            super(listMultimap, obj);
        }

        ListMultimap<K, V> delegate() {
            return (ListMultimap) super.delegate();
        }

        public List<V> get(K k) {
            synchronized (this.mutex) {
                k = Synchronized.list(delegate().get(k), this.mutex);
            }
            return k;
        }

        public List<V> removeAll(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().removeAll(obj);
            }
            return obj;
        }

        public List<V> replaceValues(K k, Iterable<? extends V> iterable) {
            synchronized (this.mutex) {
                k = delegate().replaceValues(k, iterable);
            }
            return k;
        }
    }

    private static class SynchronizedMultiset<E> extends SynchronizedCollection<E> implements Multiset<E> {
        private static final long serialVersionUID = 0;
        transient Set<E> elementSet;
        transient Set<Multiset.Entry<E>> entrySet;

        SynchronizedMultiset(Multiset<E> multiset, @Nullable Object obj) {
            super(multiset, obj);
        }

        Multiset<E> delegate() {
            return (Multiset) super.delegate();
        }

        public int count(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().count(obj);
            }
            return obj;
        }

        public int add(E e, int i) {
            synchronized (this.mutex) {
                e = delegate().add(e, i);
            }
            return e;
        }

        public int remove(Object obj, int i) {
            synchronized (this.mutex) {
                obj = delegate().remove(obj, i);
            }
            return obj;
        }

        public int setCount(E e, int i) {
            synchronized (this.mutex) {
                e = delegate().setCount(e, i);
            }
            return e;
        }

        public boolean setCount(E e, int i, int i2) {
            synchronized (this.mutex) {
                e = delegate().setCount(e, i, i2);
            }
            return e;
        }

        public Set<E> elementSet() {
            Set<E> set;
            synchronized (this.mutex) {
                if (this.elementSet == null) {
                    this.elementSet = Synchronized.typePreservingSet(delegate().elementSet(), this.mutex);
                }
                set = this.elementSet;
            }
            return set;
        }

        public Set<Multiset.Entry<E>> entrySet() {
            Set<Multiset.Entry<E>> set;
            synchronized (this.mutex) {
                if (this.entrySet == null) {
                    this.entrySet = Synchronized.typePreservingSet(delegate().entrySet(), this.mutex);
                }
                set = this.entrySet;
            }
            return set;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            synchronized (this.mutex) {
                obj = delegate().equals(obj);
            }
            return obj;
        }

        public int hashCode() {
            int hashCode;
            synchronized (this.mutex) {
                hashCode = delegate().hashCode();
            }
            return hashCode;
        }
    }

    static class SynchronizedSortedMap<K, V> extends SynchronizedMap<K, V> implements SortedMap<K, V> {
        private static final long serialVersionUID = 0;

        SynchronizedSortedMap(SortedMap<K, V> sortedMap, @Nullable Object obj) {
            super(sortedMap, obj);
        }

        SortedMap<K, V> delegate() {
            return (SortedMap) super.delegate();
        }

        public Comparator<? super K> comparator() {
            Comparator<? super K> comparator;
            synchronized (this.mutex) {
                comparator = delegate().comparator();
            }
            return comparator;
        }

        public K firstKey() {
            K firstKey;
            synchronized (this.mutex) {
                firstKey = delegate().firstKey();
            }
            return firstKey;
        }

        public SortedMap<K, V> headMap(K k) {
            synchronized (this.mutex) {
                k = Synchronized.sortedMap(delegate().headMap(k), this.mutex);
            }
            return k;
        }

        public K lastKey() {
            K lastKey;
            synchronized (this.mutex) {
                lastKey = delegate().lastKey();
            }
            return lastKey;
        }

        public SortedMap<K, V> subMap(K k, K k2) {
            synchronized (this.mutex) {
                k = Synchronized.sortedMap(delegate().subMap(k, k2), this.mutex);
            }
            return k;
        }

        public SortedMap<K, V> tailMap(K k) {
            synchronized (this.mutex) {
                k = Synchronized.sortedMap(delegate().tailMap(k), this.mutex);
            }
            return k;
        }
    }

    @GwtIncompatible("NavigableMap")
    @VisibleForTesting
    static class SynchronizedNavigableMap<K, V> extends SynchronizedSortedMap<K, V> implements NavigableMap<K, V> {
        private static final long serialVersionUID = 0;
        transient NavigableSet<K> descendingKeySet;
        transient NavigableMap<K, V> descendingMap;
        transient NavigableSet<K> navigableKeySet;

        SynchronizedNavigableMap(NavigableMap<K, V> navigableMap, @Nullable Object obj) {
            super(navigableMap, obj);
        }

        NavigableMap<K, V> delegate() {
            return (NavigableMap) super.delegate();
        }

        public Entry<K, V> ceilingEntry(K k) {
            synchronized (this.mutex) {
                k = Synchronized.nullableSynchronizedEntry(delegate().ceilingEntry(k), this.mutex);
            }
            return k;
        }

        public K ceilingKey(K k) {
            synchronized (this.mutex) {
                k = delegate().ceilingKey(k);
            }
            return k;
        }

        public NavigableSet<K> descendingKeySet() {
            synchronized (this.mutex) {
                if (this.descendingKeySet == null) {
                    NavigableSet<K> navigableSet = Synchronized.navigableSet(delegate().descendingKeySet(), this.mutex);
                    this.descendingKeySet = navigableSet;
                    return navigableSet;
                }
                navigableSet = this.descendingKeySet;
                return navigableSet;
            }
        }

        public NavigableMap<K, V> descendingMap() {
            synchronized (this.mutex) {
                if (this.descendingMap == null) {
                    NavigableMap<K, V> navigableMap = Synchronized.navigableMap(delegate().descendingMap(), this.mutex);
                    this.descendingMap = navigableMap;
                    return navigableMap;
                }
                navigableMap = this.descendingMap;
                return navigableMap;
            }
        }

        public Entry<K, V> firstEntry() {
            Entry<K, V> access$700;
            synchronized (this.mutex) {
                access$700 = Synchronized.nullableSynchronizedEntry(delegate().firstEntry(), this.mutex);
            }
            return access$700;
        }

        public Entry<K, V> floorEntry(K k) {
            synchronized (this.mutex) {
                k = Synchronized.nullableSynchronizedEntry(delegate().floorEntry(k), this.mutex);
            }
            return k;
        }

        public K floorKey(K k) {
            synchronized (this.mutex) {
                k = delegate().floorKey(k);
            }
            return k;
        }

        public NavigableMap<K, V> headMap(K k, boolean z) {
            synchronized (this.mutex) {
                k = Synchronized.navigableMap(delegate().headMap(k, z), this.mutex);
            }
            return k;
        }

        public Entry<K, V> higherEntry(K k) {
            synchronized (this.mutex) {
                k = Synchronized.nullableSynchronizedEntry(delegate().higherEntry(k), this.mutex);
            }
            return k;
        }

        public K higherKey(K k) {
            synchronized (this.mutex) {
                k = delegate().higherKey(k);
            }
            return k;
        }

        public Entry<K, V> lastEntry() {
            Entry<K, V> access$700;
            synchronized (this.mutex) {
                access$700 = Synchronized.nullableSynchronizedEntry(delegate().lastEntry(), this.mutex);
            }
            return access$700;
        }

        public Entry<K, V> lowerEntry(K k) {
            synchronized (this.mutex) {
                k = Synchronized.nullableSynchronizedEntry(delegate().lowerEntry(k), this.mutex);
            }
            return k;
        }

        public K lowerKey(K k) {
            synchronized (this.mutex) {
                k = delegate().lowerKey(k);
            }
            return k;
        }

        public Set<K> keySet() {
            return navigableKeySet();
        }

        public NavigableSet<K> navigableKeySet() {
            synchronized (this.mutex) {
                if (this.navigableKeySet == null) {
                    NavigableSet<K> navigableSet = Synchronized.navigableSet(delegate().navigableKeySet(), this.mutex);
                    this.navigableKeySet = navigableSet;
                    return navigableSet;
                }
                navigableSet = this.navigableKeySet;
                return navigableSet;
            }
        }

        public Entry<K, V> pollFirstEntry() {
            Entry<K, V> access$700;
            synchronized (this.mutex) {
                access$700 = Synchronized.nullableSynchronizedEntry(delegate().pollFirstEntry(), this.mutex);
            }
            return access$700;
        }

        public Entry<K, V> pollLastEntry() {
            Entry<K, V> access$700;
            synchronized (this.mutex) {
                access$700 = Synchronized.nullableSynchronizedEntry(delegate().pollLastEntry(), this.mutex);
            }
            return access$700;
        }

        public NavigableMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
            synchronized (this.mutex) {
                k = Synchronized.navigableMap(delegate().subMap(k, z, k2, z2), this.mutex);
            }
            return k;
        }

        public NavigableMap<K, V> tailMap(K k, boolean z) {
            synchronized (this.mutex) {
                k = Synchronized.navigableMap(delegate().tailMap(k, z), this.mutex);
            }
            return k;
        }

        public SortedMap<K, V> headMap(K k) {
            return headMap(k, false);
        }

        public SortedMap<K, V> subMap(K k, K k2) {
            return subMap(k, true, k2, false);
        }

        public SortedMap<K, V> tailMap(K k) {
            return tailMap(k, true);
        }
    }

    static class SynchronizedSortedSet<E> extends SynchronizedSet<E> implements SortedSet<E> {
        private static final long serialVersionUID = 0;

        SynchronizedSortedSet(SortedSet<E> sortedSet, @Nullable Object obj) {
            super(sortedSet, obj);
        }

        SortedSet<E> delegate() {
            return (SortedSet) super.delegate();
        }

        public Comparator<? super E> comparator() {
            Comparator<? super E> comparator;
            synchronized (this.mutex) {
                comparator = delegate().comparator();
            }
            return comparator;
        }

        public SortedSet<E> subSet(E e, E e2) {
            synchronized (this.mutex) {
                e = Synchronized.sortedSet(delegate().subSet(e, e2), this.mutex);
            }
            return e;
        }

        public SortedSet<E> headSet(E e) {
            synchronized (this.mutex) {
                e = Synchronized.sortedSet(delegate().headSet(e), this.mutex);
            }
            return e;
        }

        public SortedSet<E> tailSet(E e) {
            synchronized (this.mutex) {
                e = Synchronized.sortedSet(delegate().tailSet(e), this.mutex);
            }
            return e;
        }

        public E first() {
            E first;
            synchronized (this.mutex) {
                first = delegate().first();
            }
            return first;
        }

        public E last() {
            E last;
            synchronized (this.mutex) {
                last = delegate().last();
            }
            return last;
        }
    }

    @GwtIncompatible("NavigableSet")
    @VisibleForTesting
    static class SynchronizedNavigableSet<E> extends SynchronizedSortedSet<E> implements NavigableSet<E> {
        private static final long serialVersionUID = 0;
        transient NavigableSet<E> descendingSet;

        SynchronizedNavigableSet(NavigableSet<E> navigableSet, @Nullable Object obj) {
            super(navigableSet, obj);
        }

        NavigableSet<E> delegate() {
            return (NavigableSet) super.delegate();
        }

        public E ceiling(E e) {
            synchronized (this.mutex) {
                e = delegate().ceiling(e);
            }
            return e;
        }

        public Iterator<E> descendingIterator() {
            return delegate().descendingIterator();
        }

        public NavigableSet<E> descendingSet() {
            synchronized (this.mutex) {
                if (this.descendingSet == null) {
                    NavigableSet<E> navigableSet = Synchronized.navigableSet(delegate().descendingSet(), this.mutex);
                    this.descendingSet = navigableSet;
                    return navigableSet;
                }
                navigableSet = this.descendingSet;
                return navigableSet;
            }
        }

        public E floor(E e) {
            synchronized (this.mutex) {
                e = delegate().floor(e);
            }
            return e;
        }

        public NavigableSet<E> headSet(E e, boolean z) {
            synchronized (this.mutex) {
                e = Synchronized.navigableSet(delegate().headSet(e, z), this.mutex);
            }
            return e;
        }

        public E higher(E e) {
            synchronized (this.mutex) {
                e = delegate().higher(e);
            }
            return e;
        }

        public E lower(E e) {
            synchronized (this.mutex) {
                e = delegate().lower(e);
            }
            return e;
        }

        public E pollFirst() {
            E pollFirst;
            synchronized (this.mutex) {
                pollFirst = delegate().pollFirst();
            }
            return pollFirst;
        }

        public E pollLast() {
            E pollLast;
            synchronized (this.mutex) {
                pollLast = delegate().pollLast();
            }
            return pollLast;
        }

        public NavigableSet<E> subSet(E e, boolean z, E e2, boolean z2) {
            synchronized (this.mutex) {
                e = Synchronized.navigableSet(delegate().subSet(e, z, e2, z2), this.mutex);
            }
            return e;
        }

        public NavigableSet<E> tailSet(E e, boolean z) {
            synchronized (this.mutex) {
                e = Synchronized.navigableSet(delegate().tailSet(e, z), this.mutex);
            }
            return e;
        }

        public SortedSet<E> headSet(E e) {
            return headSet(e, false);
        }

        public SortedSet<E> subSet(E e, E e2) {
            return subSet(e, true, e2, false);
        }

        public SortedSet<E> tailSet(E e) {
            return tailSet(e, true);
        }
    }

    private static class SynchronizedRandomAccessList<E> extends SynchronizedList<E> implements RandomAccess {
        private static final long serialVersionUID = 0;

        SynchronizedRandomAccessList(List<E> list, @Nullable Object obj) {
            super(list, obj);
        }
    }

    private static class SynchronizedSetMultimap<K, V> extends SynchronizedMultimap<K, V> implements SetMultimap<K, V> {
        private static final long serialVersionUID = 0;
        transient Set<Entry<K, V>> entrySet;

        SynchronizedSetMultimap(SetMultimap<K, V> setMultimap, @Nullable Object obj) {
            super(setMultimap, obj);
        }

        SetMultimap<K, V> delegate() {
            return (SetMultimap) super.delegate();
        }

        public Set<V> get(K k) {
            synchronized (this.mutex) {
                k = Synchronized.set(delegate().get(k), this.mutex);
            }
            return k;
        }

        public Set<V> removeAll(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().removeAll(obj);
            }
            return obj;
        }

        public Set<V> replaceValues(K k, Iterable<? extends V> iterable) {
            synchronized (this.mutex) {
                k = delegate().replaceValues(k, iterable);
            }
            return k;
        }

        public Set<Entry<K, V>> entries() {
            Set<Entry<K, V>> set;
            synchronized (this.mutex) {
                if (this.entrySet == null) {
                    this.entrySet = Synchronized.set(delegate().entries(), this.mutex);
                }
                set = this.entrySet;
            }
            return set;
        }
    }

    private static class SynchronizedSortedSetMultimap<K, V> extends SynchronizedSetMultimap<K, V> implements SortedSetMultimap<K, V> {
        private static final long serialVersionUID = 0;

        SynchronizedSortedSetMultimap(SortedSetMultimap<K, V> sortedSetMultimap, @Nullable Object obj) {
            super(sortedSetMultimap, obj);
        }

        SortedSetMultimap<K, V> delegate() {
            return (SortedSetMultimap) super.delegate();
        }

        public SortedSet<V> get(K k) {
            synchronized (this.mutex) {
                k = Synchronized.sortedSet(delegate().get(k), this.mutex);
            }
            return k;
        }

        public SortedSet<V> removeAll(Object obj) {
            synchronized (this.mutex) {
                obj = delegate().removeAll(obj);
            }
            return obj;
        }

        public SortedSet<V> replaceValues(K k, Iterable<? extends V> iterable) {
            synchronized (this.mutex) {
                k = delegate().replaceValues(k, iterable);
            }
            return k;
        }

        public Comparator<? super V> valueComparator() {
            Comparator<? super V> valueComparator;
            synchronized (this.mutex) {
                valueComparator = delegate().valueComparator();
            }
            return valueComparator;
        }
    }

    private Synchronized() {
    }

    private static <E> Collection<E> collection(Collection<E> collection, @Nullable Object obj) {
        return new SynchronizedCollection(collection, obj);
    }

    @VisibleForTesting
    static <E> Set<E> set(Set<E> set, @Nullable Object obj) {
        return new SynchronizedSet(set, obj);
    }

    private static <E> SortedSet<E> sortedSet(SortedSet<E> sortedSet, @Nullable Object obj) {
        return new SynchronizedSortedSet(sortedSet, obj);
    }

    private static <E> List<E> list(List<E> list, @Nullable Object obj) {
        return list instanceof RandomAccess ? new SynchronizedRandomAccessList(list, obj) : new SynchronizedList(list, obj);
    }

    static <E> Multiset<E> multiset(Multiset<E> multiset, @Nullable Object obj) {
        if (!(multiset instanceof SynchronizedMultiset)) {
            if (!(multiset instanceof ImmutableMultiset)) {
                return new SynchronizedMultiset(multiset, obj);
            }
        }
        return multiset;
    }

    static <K, V> Multimap<K, V> multimap(Multimap<K, V> multimap, @Nullable Object obj) {
        if (!(multimap instanceof SynchronizedMultimap)) {
            if (!(multimap instanceof ImmutableMultimap)) {
                return new SynchronizedMultimap(multimap, obj);
            }
        }
        return multimap;
    }

    static <K, V> ListMultimap<K, V> listMultimap(ListMultimap<K, V> listMultimap, @Nullable Object obj) {
        if (!(listMultimap instanceof SynchronizedListMultimap)) {
            if (!(listMultimap instanceof ImmutableListMultimap)) {
                return new SynchronizedListMultimap(listMultimap, obj);
            }
        }
        return listMultimap;
    }

    static <K, V> SetMultimap<K, V> setMultimap(SetMultimap<K, V> setMultimap, @Nullable Object obj) {
        if (!(setMultimap instanceof SynchronizedSetMultimap)) {
            if (!(setMultimap instanceof ImmutableSetMultimap)) {
                return new SynchronizedSetMultimap(setMultimap, obj);
            }
        }
        return setMultimap;
    }

    static <K, V> SortedSetMultimap<K, V> sortedSetMultimap(SortedSetMultimap<K, V> sortedSetMultimap, @Nullable Object obj) {
        if (sortedSetMultimap instanceof SynchronizedSortedSetMultimap) {
            return sortedSetMultimap;
        }
        return new SynchronizedSortedSetMultimap(sortedSetMultimap, obj);
    }

    private static <E> Collection<E> typePreservingCollection(Collection<E> collection, @Nullable Object obj) {
        if (collection instanceof SortedSet) {
            return sortedSet((SortedSet) collection, obj);
        }
        if (collection instanceof Set) {
            return set((Set) collection, obj);
        }
        if (collection instanceof List) {
            return list((List) collection, obj);
        }
        return collection(collection, obj);
    }

    private static <E> Set<E> typePreservingSet(Set<E> set, @Nullable Object obj) {
        if (set instanceof SortedSet) {
            return sortedSet((SortedSet) set, obj);
        }
        return set(set, obj);
    }

    @VisibleForTesting
    static <K, V> Map<K, V> map(Map<K, V> map, @Nullable Object obj) {
        return new SynchronizedMap(map, obj);
    }

    static <K, V> SortedMap<K, V> sortedMap(SortedMap<K, V> sortedMap, @Nullable Object obj) {
        return new SynchronizedSortedMap(sortedMap, obj);
    }

    static <K, V> BiMap<K, V> biMap(BiMap<K, V> biMap, @Nullable Object obj) {
        if (!(biMap instanceof SynchronizedBiMap)) {
            if (!(biMap instanceof ImmutableBiMap)) {
                return new SynchronizedBiMap(biMap, obj, null);
            }
        }
        return biMap;
    }

    @GwtIncompatible("NavigableSet")
    static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet, @Nullable Object obj) {
        return new SynchronizedNavigableSet(navigableSet, obj);
    }

    @GwtIncompatible("NavigableSet")
    static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet) {
        return navigableSet(navigableSet, null);
    }

    @GwtIncompatible("NavigableMap")
    static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap) {
        return navigableMap(navigableMap, null);
    }

    @GwtIncompatible("NavigableMap")
    static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap, @Nullable Object obj) {
        return new SynchronizedNavigableMap(navigableMap, obj);
    }

    @GwtIncompatible("works but is needed only for NavigableMap")
    private static <K, V> Entry<K, V> nullableSynchronizedEntry(@Nullable Entry<K, V> entry, @Nullable Object obj) {
        return entry == null ? null : new SynchronizedEntry(entry, obj);
    }

    static <E> Queue<E> queue(Queue<E> queue, @Nullable Object obj) {
        return queue instanceof SynchronizedQueue ? queue : new SynchronizedQueue(queue, obj);
    }

    @GwtIncompatible("Deque")
    static <E> Deque<E> deque(Deque<E> deque, @Nullable Object obj) {
        return new SynchronizedDeque(deque, obj);
    }
}
