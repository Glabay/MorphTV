package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import de.timroes.axmlrpc.XMLRPCClient;
import java.io.Serializable;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
    static final double MAX_LOAD_FACTOR = 1.2d;
    private final transient ImmutableMapEntry<K, V>[] entries;
    private final transient int hashCode;
    private transient ImmutableBiMap<V, K> inverse;
    private final transient ImmutableMapEntry<K, V>[] keyTable;
    private final transient int mask;
    private final transient ImmutableMapEntry<K, V>[] valueTable;

    private final class Inverse extends ImmutableBiMap<V, K> {

        final class InverseEntrySet extends ImmutableMapEntrySet<V, K> {

            /* renamed from: com.google.common.collect.RegularImmutableBiMap$Inverse$InverseEntrySet$1 */
            class C10521 extends ImmutableAsList<Entry<V, K>> {
                C10521() {
                }

                public Entry<V, K> get(int i) {
                    i = RegularImmutableBiMap.this.entries[i];
                    return Maps.immutableEntry(i.getValue(), i.getKey());
                }

                ImmutableCollection<Entry<V, K>> delegateCollection() {
                    return InverseEntrySet.this;
                }
            }

            boolean isHashCodeFast() {
                return true;
            }

            InverseEntrySet() {
            }

            ImmutableMap<V, K> map() {
                return Inverse.this;
            }

            public int hashCode() {
                return RegularImmutableBiMap.this.hashCode;
            }

            public UnmodifiableIterator<Entry<V, K>> iterator() {
                return asList().iterator();
            }

            ImmutableList<Entry<V, K>> createAsList() {
                return new C10521();
            }
        }

        boolean isPartialView() {
            return false;
        }

        private Inverse() {
        }

        public int size() {
            return inverse().size();
        }

        public ImmutableBiMap<K, V> inverse() {
            return RegularImmutableBiMap.this;
        }

        public K get(@Nullable Object obj) {
            if (obj == null) {
                return null;
            }
            for (ImmutableMapEntry immutableMapEntry = RegularImmutableBiMap.this.valueTable[Hashing.smear(obj.hashCode()) & RegularImmutableBiMap.this.mask]; immutableMapEntry != null; immutableMapEntry = immutableMapEntry.getNextInValueBucket()) {
                if (obj.equals(immutableMapEntry.getValue())) {
                    return immutableMapEntry.getKey();
                }
            }
            return null;
        }

        ImmutableSet<Entry<V, K>> createEntrySet() {
            return new InverseEntrySet();
        }

        Object writeReplace() {
            return new InverseSerializedForm(RegularImmutableBiMap.this);
        }
    }

    private static class InverseSerializedForm<K, V> implements Serializable {
        private static final long serialVersionUID = 1;
        private final ImmutableBiMap<K, V> forward;

        InverseSerializedForm(ImmutableBiMap<K, V> immutableBiMap) {
            this.forward = immutableBiMap;
        }

        Object readResolve() {
            return this.forward.inverse();
        }
    }

    private static final class NonTerminalBiMapEntry<K, V> extends ImmutableMapEntry<K, V> {
        @Nullable
        private final ImmutableMapEntry<K, V> nextInKeyBucket;
        @Nullable
        private final ImmutableMapEntry<K, V> nextInValueBucket;

        NonTerminalBiMapEntry(K k, V v, @Nullable ImmutableMapEntry<K, V> immutableMapEntry, @Nullable ImmutableMapEntry<K, V> immutableMapEntry2) {
            super(k, v);
            this.nextInKeyBucket = immutableMapEntry;
            this.nextInValueBucket = immutableMapEntry2;
        }

        NonTerminalBiMapEntry(ImmutableMapEntry<K, V> immutableMapEntry, @Nullable ImmutableMapEntry<K, V> immutableMapEntry2, @Nullable ImmutableMapEntry<K, V> immutableMapEntry3) {
            super(immutableMapEntry);
            this.nextInKeyBucket = immutableMapEntry2;
            this.nextInValueBucket = immutableMapEntry3;
        }

        @Nullable
        ImmutableMapEntry<K, V> getNextInKeyBucket() {
            return this.nextInKeyBucket;
        }

        @Nullable
        ImmutableMapEntry<K, V> getNextInValueBucket() {
            return this.nextInValueBucket;
        }
    }

    boolean isHashCodeFast() {
        return true;
    }

    boolean isPartialView() {
        return false;
    }

    RegularImmutableBiMap(TerminalEntry<?, ?>... terminalEntryArr) {
        this(terminalEntryArr.length, terminalEntryArr);
    }

    RegularImmutableBiMap(int i, TerminalEntry<?, ?>[] terminalEntryArr) {
        int closedTableSize = Hashing.closedTableSize(i, MAX_LOAD_FACTOR);
        this.mask = closedTableSize - 1;
        ImmutableMapEntry[] createEntryArray = ImmutableMapEntry.createEntryArray(closedTableSize);
        ImmutableMapEntry[] createEntryArray2 = ImmutableMapEntry.createEntryArray(closedTableSize);
        ImmutableMapEntry[] createEntryArray3 = ImmutableMapEntry.createEntryArray(i);
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            Object obj = terminalEntryArr[i3];
            Object key = obj.getKey();
            Object value = obj.getValue();
            int hashCode = key.hashCode();
            int hashCode2 = value.hashCode();
            int smear = Hashing.smear(hashCode) & this.mask;
            int smear2 = Hashing.smear(hashCode2) & this.mask;
            ImmutableMapEntry immutableMapEntry = createEntryArray[smear];
            RegularImmutableMap.checkNoConflictInKeyBucket(key, obj, immutableMapEntry);
            ImmutableMapEntry immutableMapEntry2 = createEntryArray2[smear2];
            checkNoConflictInValueBucket(value, obj, immutableMapEntry2);
            if (immutableMapEntry != null || immutableMapEntry2 != null) {
                obj = new NonTerminalBiMapEntry(obj, immutableMapEntry, immutableMapEntry2);
            }
            createEntryArray[smear] = obj;
            createEntryArray2[smear2] = obj;
            createEntryArray3[i3] = obj;
            i2 += hashCode ^ hashCode2;
        }
        this.keyTable = createEntryArray;
        this.valueTable = createEntryArray2;
        this.entries = createEntryArray3;
        this.hashCode = i2;
    }

    RegularImmutableBiMap(Entry<?, ?>[] entryArr) {
        Entry<?, ?>[] entryArr2 = entryArr;
        int length = entryArr2.length;
        int closedTableSize = Hashing.closedTableSize(length, MAX_LOAD_FACTOR);
        this.mask = closedTableSize - 1;
        ImmutableMapEntry[] createEntryArray = ImmutableMapEntry.createEntryArray(closedTableSize);
        ImmutableMapEntry[] createEntryArray2 = ImmutableMapEntry.createEntryArray(closedTableSize);
        ImmutableMapEntry[] createEntryArray3 = ImmutableMapEntry.createEntryArray(length);
        int i = 0;
        int i2 = 0;
        while (i < length) {
            Entry entry = entryArr2[i];
            Object key = entry.getKey();
            Object value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            int hashCode = key.hashCode();
            int hashCode2 = value.hashCode();
            int smear = Hashing.smear(hashCode) & r0.mask;
            int smear2 = Hashing.smear(hashCode2) & r0.mask;
            ImmutableMapEntry immutableMapEntry = createEntryArray[smear];
            RegularImmutableMap.checkNoConflictInKeyBucket(key, entry, immutableMapEntry);
            ImmutableMapEntry immutableMapEntry2 = createEntryArray2[smear2];
            checkNoConflictInValueBucket(value, entry, immutableMapEntry2);
            TerminalEntry terminalEntry = (immutableMapEntry == null && immutableMapEntry2 == null) ? new TerminalEntry(key, value) : new NonTerminalBiMapEntry(key, value, immutableMapEntry, immutableMapEntry2);
            createEntryArray[smear] = terminalEntry;
            createEntryArray2[smear2] = terminalEntry;
            createEntryArray3[i] = terminalEntry;
            i2 += hashCode ^ hashCode2;
            i++;
            entryArr2 = entryArr;
        }
        r0.keyTable = createEntryArray;
        r0.valueTable = createEntryArray2;
        r0.entries = createEntryArray3;
        r0.hashCode = i2;
    }

    private static void checkNoConflictInValueBucket(Object obj, Entry<?, ?> entry, @Nullable ImmutableMapEntry<?, ?> immutableMapEntry) {
        while (immutableMapEntry != null) {
            ImmutableMap.checkNoConflict(obj.equals(immutableMapEntry.getValue()) ^ 1, XMLRPCClient.VALUE, entry, immutableMapEntry);
            immutableMapEntry = immutableMapEntry.getNextInValueBucket();
        }
    }

    @Nullable
    public V get(@Nullable Object obj) {
        return RegularImmutableMap.get(obj, this.keyTable, this.mask);
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return new RegularEntrySet(this, this.entries);
    }

    public int hashCode() {
        return this.hashCode;
    }

    public int size() {
        return this.entries.length;
    }

    public ImmutableBiMap<V, K> inverse() {
        ImmutableBiMap<V, K> immutableBiMap = this.inverse;
        if (immutableBiMap != null) {
            return immutableBiMap;
        }
        immutableBiMap = new Inverse();
        this.inverse = immutableBiMap;
        return immutableBiMap;
    }
}
