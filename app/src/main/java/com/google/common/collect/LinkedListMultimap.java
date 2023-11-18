package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public class LinkedListMultimap<K, V> extends AbstractMultimap<K, V> implements ListMultimap<K, V>, Serializable {
    @GwtIncompatible("java serialization not supported")
    private static final long serialVersionUID = 0;
    private transient Node<K, V> head;
    private transient Map<K, KeyList<K, V>> keyToKeyList;
    private transient int modCount;
    private transient int size;
    private transient Node<K, V> tail;

    /* renamed from: com.google.common.collect.LinkedListMultimap$2 */
    class C09562 extends ImprovedAbstractSet<K> {
        C09562() {
        }

        public int size() {
            return LinkedListMultimap.this.keyToKeyList.size();
        }

        public Iterator<K> iterator() {
            return new DistinctKeyIterator();
        }

        public boolean contains(Object obj) {
            return LinkedListMultimap.this.containsKey(obj);
        }

        public boolean remove(Object obj) {
            return LinkedListMultimap.this.removeAll(obj).isEmpty() ^ 1;
        }
    }

    /* renamed from: com.google.common.collect.LinkedListMultimap$3 */
    class C09583 extends AbstractSequentialList<V> {
        C09583() {
        }

        public int size() {
            return LinkedListMultimap.this.size;
        }

        public ListIterator<V> listIterator(int i) {
            final Object nodeIterator = new NodeIterator(i);
            return new TransformedListIterator<Entry<K, V>, V>(nodeIterator) {
                V transform(Entry<K, V> entry) {
                    return entry.getValue();
                }

                public void set(V v) {
                    nodeIterator.setValue(v);
                }
            };
        }
    }

    /* renamed from: com.google.common.collect.LinkedListMultimap$4 */
    class C09594 extends AbstractSequentialList<Entry<K, V>> {
        C09594() {
        }

        public int size() {
            return LinkedListMultimap.this.size;
        }

        public ListIterator<Entry<K, V>> listIterator(int i) {
            return new NodeIterator(i);
        }
    }

    private class DistinctKeyIterator implements Iterator<K> {
        Node<K, V> current;
        int expectedModCount;
        Node<K, V> next;
        final Set<K> seenKeys;

        private DistinctKeyIterator() {
            this.seenKeys = Sets.newHashSetWithExpectedSize(LinkedListMultimap.this.keySet().size());
            this.next = LinkedListMultimap.this.head;
            this.expectedModCount = LinkedListMultimap.this.modCount;
        }

        private void checkForConcurrentModification() {
            if (LinkedListMultimap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        public boolean hasNext() {
            checkForConcurrentModification();
            return this.next != null;
        }

        public K next() {
            checkForConcurrentModification();
            LinkedListMultimap.checkElement(this.next);
            this.current = this.next;
            this.seenKeys.add(this.current.key);
            do {
                this.next = this.next.next;
                if (this.next == null) {
                    break;
                }
            } while (!this.seenKeys.add(this.next.key));
            return this.current.key;
        }

        public void remove() {
            checkForConcurrentModification();
            CollectPreconditions.checkRemove(this.current != null);
            LinkedListMultimap.this.removeAllNodes(this.current.key);
            this.current = null;
            this.expectedModCount = LinkedListMultimap.this.modCount;
        }
    }

    private static class KeyList<K, V> {
        int count = 1;
        Node<K, V> head;
        Node<K, V> tail;

        KeyList(Node<K, V> node) {
            this.head = node;
            this.tail = node;
        }
    }

    private static final class Node<K, V> extends AbstractMapEntry<K, V> {
        final K key;
        Node<K, V> next;
        Node<K, V> nextSibling = null;
        Node<K, V> previous;
        Node<K, V> previousSibling = null;
        V value;

        Node(@Nullable K k, @Nullable V v) {
            this.key = k;
            this.value = v;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(@Nullable V v) {
            V v2 = this.value;
            this.value = v;
            return v2;
        }
    }

    private class NodeIterator implements ListIterator<Entry<K, V>> {
        Node<K, V> current;
        int expectedModCount = LinkedListMultimap.this.modCount;
        Node<K, V> next;
        int nextIndex;
        Node<K, V> previous;

        NodeIterator(int i) {
            int size = LinkedListMultimap.this.size();
            Preconditions.checkPositionIndex(i, size);
            LinkedListMultimap linkedListMultimap;
            if (i < size / 2) {
                this.next = LinkedListMultimap.this.head;
                while (true) {
                    linkedListMultimap = i - 1;
                    if (i <= 0) {
                        break;
                    }
                    next();
                    i = linkedListMultimap;
                }
            } else {
                this.previous = LinkedListMultimap.this.tail;
                this.nextIndex = size;
                while (true) {
                    linkedListMultimap = i + 1;
                    if (i >= size) {
                        break;
                    }
                    previous();
                    i = linkedListMultimap;
                }
            }
            this.current = null;
        }

        private void checkForConcurrentModification() {
            if (LinkedListMultimap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        public boolean hasNext() {
            checkForConcurrentModification();
            return this.next != null;
        }

        public Node<K, V> next() {
            checkForConcurrentModification();
            LinkedListMultimap.checkElement(this.next);
            Node node = this.next;
            this.current = node;
            this.previous = node;
            this.next = this.next.next;
            this.nextIndex++;
            return this.current;
        }

        public void remove() {
            checkForConcurrentModification();
            CollectPreconditions.checkRemove(this.current != null);
            if (this.current != this.next) {
                this.previous = this.current.previous;
                this.nextIndex--;
            } else {
                this.next = this.current.next;
            }
            LinkedListMultimap.this.removeNode(this.current);
            this.current = null;
            this.expectedModCount = LinkedListMultimap.this.modCount;
        }

        public boolean hasPrevious() {
            checkForConcurrentModification();
            return this.previous != null;
        }

        public Node<K, V> previous() {
            checkForConcurrentModification();
            LinkedListMultimap.checkElement(this.previous);
            Node node = this.previous;
            this.current = node;
            this.next = node;
            this.previous = this.previous.previous;
            this.nextIndex--;
            return this.current;
        }

        public int nextIndex() {
            return this.nextIndex;
        }

        public int previousIndex() {
            return this.nextIndex - 1;
        }

        public void set(Entry<K, V> entry) {
            throw new UnsupportedOperationException();
        }

        public void add(Entry<K, V> entry) {
            throw new UnsupportedOperationException();
        }

        void setValue(V v) {
            Preconditions.checkState(this.current != null);
            this.current.value = v;
        }
    }

    private class ValueForKeyIterator implements ListIterator<V> {
        Node<K, V> current;
        final Object key;
        Node<K, V> next;
        int nextIndex;
        Node<K, V> previous;

        ValueForKeyIterator(@Nullable Object obj) {
            LinkedListMultimap linkedListMultimap;
            this.key = obj;
            KeyList keyList = (KeyList) LinkedListMultimap.this.keyToKeyList.get(obj);
            if (keyList == null) {
                linkedListMultimap = null;
            } else {
                linkedListMultimap = keyList.head;
            }
            this.next = linkedListMultimap;
        }

        public ValueForKeyIterator(@Nullable Object obj, int i) {
            int i2;
            KeyList keyList = (KeyList) LinkedListMultimap.this.keyToKeyList.get(obj);
            if (keyList == null) {
                i2 = 0;
            } else {
                i2 = keyList.count;
            }
            Preconditions.checkPositionIndex(i, i2);
            LinkedListMultimap linkedListMultimap;
            if (i < i2 / 2) {
                if (keyList == null) {
                    linkedListMultimap = null;
                } else {
                    linkedListMultimap = keyList.head;
                }
                this.next = linkedListMultimap;
                while (true) {
                    linkedListMultimap = i - 1;
                    if (i <= 0) {
                        break;
                    }
                    next();
                    i = linkedListMultimap;
                }
            } else {
                if (keyList == null) {
                    linkedListMultimap = null;
                } else {
                    linkedListMultimap = keyList.tail;
                }
                this.previous = linkedListMultimap;
                this.nextIndex = i2;
                while (true) {
                    linkedListMultimap = i + 1;
                    if (i >= i2) {
                        break;
                    }
                    previous();
                    i = linkedListMultimap;
                }
            }
            this.key = obj;
            this.current = null;
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public V next() {
            LinkedListMultimap.checkElement(this.next);
            Node node = this.next;
            this.current = node;
            this.previous = node;
            this.next = this.next.nextSibling;
            this.nextIndex++;
            return this.current.value;
        }

        public boolean hasPrevious() {
            return this.previous != null;
        }

        public V previous() {
            LinkedListMultimap.checkElement(this.previous);
            Node node = this.previous;
            this.current = node;
            this.next = node;
            this.previous = this.previous.previousSibling;
            this.nextIndex--;
            return this.current.value;
        }

        public int nextIndex() {
            return this.nextIndex;
        }

        public int previousIndex() {
            return this.nextIndex - 1;
        }

        public void remove() {
            CollectPreconditions.checkRemove(this.current != null);
            if (this.current != this.next) {
                this.previous = this.current.previousSibling;
                this.nextIndex--;
            } else {
                this.next = this.current.nextSibling;
            }
            LinkedListMultimap.this.removeNode(this.current);
            this.current = null;
        }

        public void set(V v) {
            Preconditions.checkState(this.current != null);
            this.current.value = v;
        }

        public void add(V v) {
            this.previous = LinkedListMultimap.this.addNode(this.key, v, this.next);
            this.nextIndex++;
            this.current = null;
        }
    }

    public /* bridge */ /* synthetic */ Map asMap() {
        return super.asMap();
    }

    public /* bridge */ /* synthetic */ boolean containsEntry(Object obj, Object obj2) {
        return super.containsEntry(obj, obj2);
    }

    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ Set keySet() {
        return super.keySet();
    }

    public /* bridge */ /* synthetic */ Multiset keys() {
        return super.keys();
    }

    public /* bridge */ /* synthetic */ boolean putAll(Multimap multimap) {
        return super.putAll(multimap);
    }

    public /* bridge */ /* synthetic */ boolean putAll(Object obj, Iterable iterable) {
        return super.putAll(obj, iterable);
    }

    public /* bridge */ /* synthetic */ boolean remove(Object obj, Object obj2) {
        return super.remove(obj, obj2);
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public static <K, V> LinkedListMultimap<K, V> create() {
        return new LinkedListMultimap();
    }

    public static <K, V> LinkedListMultimap<K, V> create(int i) {
        return new LinkedListMultimap(i);
    }

    public static <K, V> LinkedListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
        return new LinkedListMultimap((Multimap) multimap);
    }

    LinkedListMultimap() {
        this.keyToKeyList = Maps.newHashMap();
    }

    private LinkedListMultimap(int i) {
        this.keyToKeyList = new HashMap(i);
    }

    private LinkedListMultimap(Multimap<? extends K, ? extends V> multimap) {
        this(multimap.keySet().size());
        putAll(multimap);
    }

    private Node<K, V> addNode(@Nullable K k, @Nullable V v, @Nullable Node<K, V> node) {
        Node<K, V> node2 = new Node(k, v);
        if (this.head == null) {
            this.tail = node2;
            this.head = node2;
            this.keyToKeyList.put(k, new KeyList(node2));
            this.modCount++;
        } else if (node == null) {
            this.tail.next = node2;
            node2.previous = this.tail;
            this.tail = node2;
            r4 = (KeyList) this.keyToKeyList.get(k);
            if (r4 == null) {
                this.keyToKeyList.put(k, new KeyList(node2));
                this.modCount++;
            } else {
                r4.count++;
                k = r4.tail;
                k.nextSibling = node2;
                node2.previousSibling = k;
                r4.tail = node2;
            }
        } else {
            r4 = (KeyList) this.keyToKeyList.get(k);
            r4.count++;
            node2.previous = node.previous;
            node2.previousSibling = node.previousSibling;
            node2.next = node;
            node2.nextSibling = node;
            if (node.previousSibling == null) {
                ((KeyList) this.keyToKeyList.get(k)).head = node2;
            } else {
                node.previousSibling.nextSibling = node2;
            }
            if (node.previous == null) {
                this.head = node2;
            } else {
                node.previous.next = node2;
            }
            node.previous = node2;
            node.previousSibling = node2;
        }
        this.size++;
        return node2;
    }

    private void removeNode(Node<K, V> node) {
        if (node.previous != null) {
            node.previous.next = node.next;
        } else {
            this.head = node.next;
        }
        if (node.next != null) {
            node.next.previous = node.previous;
        } else {
            this.tail = node.previous;
        }
        if (node.previousSibling == null && node.nextSibling == null) {
            ((KeyList) this.keyToKeyList.remove(node.key)).count = 0;
            this.modCount++;
        } else {
            KeyList keyList = (KeyList) this.keyToKeyList.get(node.key);
            keyList.count--;
            if (node.previousSibling == null) {
                keyList.head = node.nextSibling;
            } else {
                node.previousSibling.nextSibling = node.nextSibling;
            }
            if (node.nextSibling == null) {
                keyList.tail = node.previousSibling;
            } else {
                node.nextSibling.previousSibling = node.previousSibling;
            }
        }
        this.size--;
    }

    private void removeAllNodes(@Nullable Object obj) {
        Iterators.clear(new ValueForKeyIterator(obj));
    }

    private static void checkElement(@Nullable Object obj) {
        if (obj == null) {
            throw new NoSuchElementException();
        }
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.head == null;
    }

    public boolean containsKey(@Nullable Object obj) {
        return this.keyToKeyList.containsKey(obj);
    }

    public boolean containsValue(@Nullable Object obj) {
        return values().contains(obj);
    }

    public boolean put(@Nullable K k, @Nullable V v) {
        addNode(k, v, null);
        return true;
    }

    public List<V> replaceValues(@Nullable K k, Iterable<? extends V> iterable) {
        List<V> copy = getCopy(k);
        ListIterator valueForKeyIterator = new ValueForKeyIterator(k);
        k = iterable.iterator();
        while (valueForKeyIterator.hasNext() != null && k.hasNext() != null) {
            valueForKeyIterator.next();
            valueForKeyIterator.set(k.next());
        }
        while (valueForKeyIterator.hasNext() != null) {
            valueForKeyIterator.next();
            valueForKeyIterator.remove();
        }
        while (k.hasNext() != null) {
            valueForKeyIterator.add(k.next());
        }
        return copy;
    }

    private List<V> getCopy(@Nullable Object obj) {
        return Collections.unmodifiableList(Lists.newArrayList(new ValueForKeyIterator(obj)));
    }

    public List<V> removeAll(@Nullable Object obj) {
        List<V> copy = getCopy(obj);
        removeAllNodes(obj);
        return copy;
    }

    public void clear() {
        this.head = null;
        this.tail = null;
        this.keyToKeyList.clear();
        this.size = 0;
        this.modCount++;
    }

    public List<V> get(@Nullable final K k) {
        return new AbstractSequentialList<V>() {
            public int size() {
                KeyList keyList = (KeyList) LinkedListMultimap.this.keyToKeyList.get(k);
                if (keyList == null) {
                    return 0;
                }
                return keyList.count;
            }

            public ListIterator<V> listIterator(int i) {
                return new ValueForKeyIterator(k, i);
            }
        };
    }

    Set<K> createKeySet() {
        return new C09562();
    }

    public List<V> values() {
        return (List) super.values();
    }

    List<V> createValues() {
        return new C09583();
    }

    public List<Entry<K, V>> entries() {
        return (List) super.entries();
    }

    List<Entry<K, V>> createEntries() {
        return new C09594();
    }

    Iterator<Entry<K, V>> entryIterator() {
        throw new AssertionError("should never be called");
    }

    Map<K, Collection<V>> createAsMap() {
        return new AsMap(this);
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(size());
        for (Entry entry : entries()) {
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeObject(entry.getValue());
        }
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.keyToKeyList = Maps.newLinkedHashMap();
        int readInt = objectInputStream.readInt();
        for (int i = 0; i < readInt; i++) {
            put(objectInputStream.readObject(), objectInputStream.readObject());
        }
    }
}
