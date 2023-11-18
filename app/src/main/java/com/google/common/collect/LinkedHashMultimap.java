package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public final class LinkedHashMultimap<K, V> extends AbstractSetMultimap<K, V> {
    private static final int DEFAULT_KEY_CAPACITY = 16;
    private static final int DEFAULT_VALUE_SET_CAPACITY = 2;
    @VisibleForTesting
    static final double VALUE_SET_LOAD_FACTOR = 1.0d;
    @GwtIncompatible("java serialization not supported")
    private static final long serialVersionUID = 1;
    private transient ValueEntry<K, V> multimapHeaderEntry;
    @VisibleForTesting
    transient int valueSetCapacity = 2;

    /* renamed from: com.google.common.collect.LinkedHashMultimap$1 */
    class C09531 implements Iterator<Entry<K, V>> {
        ValueEntry<K, V> nextEntry = LinkedHashMultimap.this.multimapHeaderEntry.successorInMultimap;
        ValueEntry<K, V> toRemove;

        C09531() {
        }

        public boolean hasNext() {
            return this.nextEntry != LinkedHashMultimap.this.multimapHeaderEntry;
        }

        public Entry<K, V> next() {
            if (hasNext()) {
                Entry entry = this.nextEntry;
                this.toRemove = entry;
                this.nextEntry = this.nextEntry.successorInMultimap;
                return entry;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            CollectPreconditions.checkRemove(this.toRemove != null);
            LinkedHashMultimap.this.remove(this.toRemove.getKey(), this.toRemove.getValue());
            this.toRemove = null;
        }
    }

    private interface ValueSetLink<K, V> {
        ValueSetLink<K, V> getPredecessorInValueSet();

        ValueSetLink<K, V> getSuccessorInValueSet();

        void setPredecessorInValueSet(ValueSetLink<K, V> valueSetLink);

        void setSuccessorInValueSet(ValueSetLink<K, V> valueSetLink);
    }

    @VisibleForTesting
    static final class ValueEntry<K, V> extends ImmutableEntry<K, V> implements ValueSetLink<K, V> {
        @Nullable
        ValueEntry<K, V> nextInValueBucket;
        ValueEntry<K, V> predecessorInMultimap;
        ValueSetLink<K, V> predecessorInValueSet;
        final int smearedValueHash;
        ValueEntry<K, V> successorInMultimap;
        ValueSetLink<K, V> successorInValueSet;

        ValueEntry(@Nullable K k, @Nullable V v, int i, @Nullable ValueEntry<K, V> valueEntry) {
            super(k, v);
            this.smearedValueHash = i;
            this.nextInValueBucket = valueEntry;
        }

        boolean matchesValue(@Nullable Object obj, int i) {
            return (this.smearedValueHash != i || Objects.equal(getValue(), obj) == null) ? null : true;
        }

        public ValueSetLink<K, V> getPredecessorInValueSet() {
            return this.predecessorInValueSet;
        }

        public ValueSetLink<K, V> getSuccessorInValueSet() {
            return this.successorInValueSet;
        }

        public void setPredecessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.predecessorInValueSet = valueSetLink;
        }

        public void setSuccessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.successorInValueSet = valueSetLink;
        }

        public ValueEntry<K, V> getPredecessorInMultimap() {
            return this.predecessorInMultimap;
        }

        public ValueEntry<K, V> getSuccessorInMultimap() {
            return this.successorInMultimap;
        }

        public void setSuccessorInMultimap(ValueEntry<K, V> valueEntry) {
            this.successorInMultimap = valueEntry;
        }

        public void setPredecessorInMultimap(ValueEntry<K, V> valueEntry) {
            this.predecessorInMultimap = valueEntry;
        }
    }

    @VisibleForTesting
    final class ValueSet extends ImprovedAbstractSet<V> implements ValueSetLink<K, V> {
        private ValueSetLink<K, V> firstEntry;
        @VisibleForTesting
        ValueEntry<K, V>[] hashTable;
        private final K key;
        private ValueSetLink<K, V> lastEntry;
        private int modCount = 0;
        private int size = 0;

        /* renamed from: com.google.common.collect.LinkedHashMultimap$ValueSet$1 */
        class C09541 implements Iterator<V> {
            int expectedModCount = ValueSet.this.modCount;
            ValueSetLink<K, V> nextEntry = ValueSet.this.firstEntry;
            ValueEntry<K, V> toRemove;

            C09541() {
            }

            private void checkForComodification() {
                if (ValueSet.this.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }

            public boolean hasNext() {
                checkForComodification();
                return this.nextEntry != ValueSet.this;
            }

            public V next() {
                if (hasNext()) {
                    ValueEntry valueEntry = (ValueEntry) this.nextEntry;
                    V value = valueEntry.getValue();
                    this.toRemove = valueEntry;
                    this.nextEntry = valueEntry.getSuccessorInValueSet();
                    return value;
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                checkForComodification();
                CollectPreconditions.checkRemove(this.toRemove != null);
                ValueSet.this.remove(this.toRemove.getValue());
                this.expectedModCount = ValueSet.this.modCount;
                this.toRemove = null;
            }
        }

        ValueSet(K k, int i) {
            this.key = k;
            this.firstEntry = this;
            this.lastEntry = this;
            this.hashTable = new ValueEntry[Hashing.closedTableSize(i, 0)];
        }

        private int mask() {
            return this.hashTable.length - 1;
        }

        public ValueSetLink<K, V> getPredecessorInValueSet() {
            return this.lastEntry;
        }

        public ValueSetLink<K, V> getSuccessorInValueSet() {
            return this.firstEntry;
        }

        public void setPredecessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.lastEntry = valueSetLink;
        }

        public void setSuccessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.firstEntry = valueSetLink;
        }

        public Iterator<V> iterator() {
            return new C09541();
        }

        public int size() {
            return this.size;
        }

        public boolean contains(@Nullable Object obj) {
            int smearedHash = Hashing.smearedHash(obj);
            for (ValueEntry valueEntry = this.hashTable[mask() & smearedHash]; valueEntry != null; valueEntry = valueEntry.nextInValueBucket) {
                if (valueEntry.matchesValue(obj, smearedHash)) {
                    return true;
                }
            }
            return null;
        }

        public boolean add(@Nullable V v) {
            ValueEntry valueEntry;
            int smearedHash = Hashing.smearedHash(v);
            int mask = mask() & smearedHash;
            ValueEntry valueEntry2 = this.hashTable[mask];
            for (valueEntry = valueEntry2; valueEntry != null; valueEntry = valueEntry.nextInValueBucket) {
                if (valueEntry.matchesValue(v, smearedHash)) {
                    return null;
                }
            }
            valueEntry = new ValueEntry(this.key, v, smearedHash, valueEntry2);
            LinkedHashMultimap.succeedsInValueSet(this.lastEntry, valueEntry);
            LinkedHashMultimap.succeedsInValueSet(valueEntry, this);
            LinkedHashMultimap.succeedsInMultimap(LinkedHashMultimap.this.multimapHeaderEntry.getPredecessorInMultimap(), valueEntry);
            LinkedHashMultimap.succeedsInMultimap(valueEntry, LinkedHashMultimap.this.multimapHeaderEntry);
            this.hashTable[mask] = valueEntry;
            this.size += 1;
            this.modCount += 1;
            rehashIfNecessary();
            return true;
        }

        private void rehashIfNecessary() {
            if (Hashing.needsResizing(this.size, this.hashTable.length, LinkedHashMultimap.VALUE_SET_LOAD_FACTOR)) {
                ValueEntry[] valueEntryArr = new ValueEntry[(this.hashTable.length * 2)];
                this.hashTable = valueEntryArr;
                int length = valueEntryArr.length - 1;
                for (ValueSet valueSet = this.firstEntry; valueSet != this; valueSet = valueSet.getSuccessorInValueSet()) {
                    ValueEntry valueEntry = (ValueEntry) valueSet;
                    int i = valueEntry.smearedValueHash & length;
                    valueEntry.nextInValueBucket = valueEntryArr[i];
                    valueEntryArr[i] = valueEntry;
                }
            }
        }

        public boolean remove(@Nullable Object obj) {
            int smearedHash = Hashing.smearedHash(obj);
            int mask = mask() & smearedHash;
            ValueEntry valueEntry = this.hashTable[mask];
            ValueEntry valueEntry2 = null;
            while (true) {
                ValueEntry valueEntry3 = valueEntry2;
                valueEntry2 = valueEntry;
                valueEntry = valueEntry3;
                if (valueEntry2 == null) {
                    return null;
                }
                if (valueEntry2.matchesValue(obj, smearedHash)) {
                    break;
                }
                valueEntry = valueEntry2.nextInValueBucket;
            }
            if (valueEntry == null) {
                this.hashTable[mask] = valueEntry2.nextInValueBucket;
            } else {
                valueEntry.nextInValueBucket = valueEntry2.nextInValueBucket;
            }
            LinkedHashMultimap.deleteFromValueSet(valueEntry2);
            LinkedHashMultimap.deleteFromMultimap(valueEntry2);
            this.size -= 1;
            this.modCount += 1;
            return true;
        }

        public void clear() {
            Arrays.fill(this.hashTable, null);
            this.size = 0;
            for (ValueSet valueSet = this.firstEntry; valueSet != this; valueSet = valueSet.getSuccessorInValueSet()) {
                LinkedHashMultimap.deleteFromMultimap((ValueEntry) valueSet);
            }
            LinkedHashMultimap.succeedsInValueSet(this, this);
            this.modCount++;
        }
    }

    public /* bridge */ /* synthetic */ Map asMap() {
        return super.asMap();
    }

    public /* bridge */ /* synthetic */ boolean containsEntry(Object obj, Object obj2) {
        return super.containsEntry(obj, obj2);
    }

    public /* bridge */ /* synthetic */ boolean containsKey(Object obj) {
        return super.containsKey(obj);
    }

    public /* bridge */ /* synthetic */ boolean containsValue(Object obj) {
        return super.containsValue(obj);
    }

    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public /* bridge */ /* synthetic */ Set get(Object obj) {
        return super.get(obj);
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    public /* bridge */ /* synthetic */ Set keySet() {
        return super.keySet();
    }

    public /* bridge */ /* synthetic */ Multiset keys() {
        return super.keys();
    }

    public /* bridge */ /* synthetic */ boolean put(Object obj, Object obj2) {
        return super.put(obj, obj2);
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

    public /* bridge */ /* synthetic */ Set removeAll(Object obj) {
        return super.removeAll(obj);
    }

    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public static <K, V> LinkedHashMultimap<K, V> create() {
        return new LinkedHashMultimap(16, 2);
    }

    public static <K, V> LinkedHashMultimap<K, V> create(int i, int i2) {
        return new LinkedHashMultimap(Maps.capacity(i), Maps.capacity(i2));
    }

    public static <K, V> LinkedHashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
        LinkedHashMultimap<K, V> create = create(multimap.keySet().size(), 2);
        create.putAll(multimap);
        return create;
    }

    private static <K, V> void succeedsInValueSet(ValueSetLink<K, V> valueSetLink, ValueSetLink<K, V> valueSetLink2) {
        valueSetLink.setSuccessorInValueSet(valueSetLink2);
        valueSetLink2.setPredecessorInValueSet(valueSetLink);
    }

    private static <K, V> void succeedsInMultimap(ValueEntry<K, V> valueEntry, ValueEntry<K, V> valueEntry2) {
        valueEntry.setSuccessorInMultimap(valueEntry2);
        valueEntry2.setPredecessorInMultimap(valueEntry);
    }

    private static <K, V> void deleteFromValueSet(ValueSetLink<K, V> valueSetLink) {
        succeedsInValueSet(valueSetLink.getPredecessorInValueSet(), valueSetLink.getSuccessorInValueSet());
    }

    private static <K, V> void deleteFromMultimap(ValueEntry<K, V> valueEntry) {
        succeedsInMultimap(valueEntry.getPredecessorInMultimap(), valueEntry.getSuccessorInMultimap());
    }

    private LinkedHashMultimap(int i, int i2) {
        super(new LinkedHashMap(i));
        CollectPreconditions.checkNonnegative(i2, "expectedValuesPerKey");
        this.valueSetCapacity = i2;
        this.multimapHeaderEntry = new ValueEntry(null, null, 0, null);
        succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
    }

    Set<V> createCollection() {
        return new LinkedHashSet(this.valueSetCapacity);
    }

    Collection<V> createCollection(K k) {
        return new ValueSet(k, this.valueSetCapacity);
    }

    public Set<V> replaceValues(@Nullable K k, Iterable<? extends V> iterable) {
        return super.replaceValues((Object) k, (Iterable) iterable);
    }

    public Set<Entry<K, V>> entries() {
        return super.entries();
    }

    public Collection<V> values() {
        return super.values();
    }

    Iterator<Entry<K, V>> entryIterator() {
        return new C09531();
    }

    Iterator<V> valueIterator() {
        return Maps.valueIterator(entryIterator());
    }

    public void clear() {
        super.clear();
        succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.valueSetCapacity);
        objectOutputStream.writeInt(keySet().size());
        for (Object writeObject : keySet()) {
            objectOutputStream.writeObject(writeObject);
        }
        objectOutputStream.writeInt(size());
        for (Entry entry : entries()) {
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeObject(entry.getValue());
        }
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int i = 0;
        this.multimapHeaderEntry = new ValueEntry(null, null, 0, null);
        succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
        this.valueSetCapacity = objectInputStream.readInt();
        int readInt = objectInputStream.readInt();
        Map linkedHashMap = new LinkedHashMap(Maps.capacity(readInt));
        for (int i2 = 0; i2 < readInt; i2++) {
            Object readObject = objectInputStream.readObject();
            linkedHashMap.put(readObject, createCollection(readObject));
        }
        readInt = objectInputStream.readInt();
        while (i < readInt) {
            Object readObject2 = objectInputStream.readObject();
            ((Collection) linkedHashMap.get(readObject2)).add(objectInputStream.readObject());
            i++;
        }
        setMap(linkedHashMap);
    }
}
