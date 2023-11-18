package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {
    private static final double MAX_LOAD_FACTOR = 1.2d;
    private static final long serialVersionUID = 0;
    private final transient ImmutableMapEntry<K, V>[] entries;
    private final transient int mask;
    private final transient ImmutableMapEntry<K, V>[] table;

    private static final class NonTerminalMapEntry<K, V> extends ImmutableMapEntry<K, V> {
        private final ImmutableMapEntry<K, V> nextInKeyBucket;

        @Nullable
        ImmutableMapEntry<K, V> getNextInValueBucket() {
            return null;
        }

        NonTerminalMapEntry(K k, V v, ImmutableMapEntry<K, V> immutableMapEntry) {
            super(k, v);
            this.nextInKeyBucket = immutableMapEntry;
        }

        NonTerminalMapEntry(ImmutableMapEntry<K, V> immutableMapEntry, ImmutableMapEntry<K, V> immutableMapEntry2) {
            super(immutableMapEntry);
            this.nextInKeyBucket = immutableMapEntry2;
        }

        ImmutableMapEntry<K, V> getNextInKeyBucket() {
            return this.nextInKeyBucket;
        }
    }

    boolean isPartialView() {
        return false;
    }

    RegularImmutableMap(TerminalEntry<?, ?>... terminalEntryArr) {
        this(terminalEntryArr.length, terminalEntryArr);
    }

    RegularImmutableMap(int i, TerminalEntry<?, ?>[] terminalEntryArr) {
        this.entries = ImmutableMapEntry.createEntryArray(i);
        int closedTableSize = Hashing.closedTableSize(i, MAX_LOAD_FACTOR);
        this.table = ImmutableMapEntry.createEntryArray(closedTableSize);
        this.mask = closedTableSize - 1;
        for (closedTableSize = 0; closedTableSize < i; closedTableSize++) {
            Entry entry = terminalEntryArr[closedTableSize];
            Object key = entry.getKey();
            int smear = Hashing.smear(key.hashCode()) & this.mask;
            ImmutableMapEntry immutableMapEntry = this.table[smear];
            if (immutableMapEntry != null) {
                entry = new NonTerminalMapEntry(entry, immutableMapEntry);
            }
            this.table[smear] = entry;
            this.entries[closedTableSize] = entry;
            checkNoConflictInKeyBucket(key, entry, immutableMapEntry);
        }
    }

    RegularImmutableMap(Entry<?, ?>[] entryArr) {
        int length = entryArr.length;
        this.entries = ImmutableMapEntry.createEntryArray(length);
        int closedTableSize = Hashing.closedTableSize(length, MAX_LOAD_FACTOR);
        this.table = ImmutableMapEntry.createEntryArray(closedTableSize);
        this.mask = closedTableSize - 1;
        for (closedTableSize = 0; closedTableSize < length; closedTableSize++) {
            Entry entry = entryArr[closedTableSize];
            Object key = entry.getKey();
            Object value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            int smear = Hashing.smear(key.hashCode()) & this.mask;
            ImmutableMapEntry immutableMapEntry = this.table[smear];
            Entry terminalEntry = immutableMapEntry == null ? new TerminalEntry(key, value) : new NonTerminalMapEntry(key, value, immutableMapEntry);
            this.table[smear] = terminalEntry;
            this.entries[closedTableSize] = terminalEntry;
            checkNoConflictInKeyBucket(key, terminalEntry, immutableMapEntry);
        }
    }

    static void checkNoConflictInKeyBucket(Object obj, Entry<?, ?> entry, @Nullable ImmutableMapEntry<?, ?> immutableMapEntry) {
        while (immutableMapEntry != null) {
            ImmutableMap.checkNoConflict(obj.equals(immutableMapEntry.getKey()) ^ 1, "key", entry, immutableMapEntry);
            immutableMapEntry = immutableMapEntry.getNextInKeyBucket();
        }
    }

    public V get(@Nullable Object obj) {
        return get(obj, this.table, this.mask);
    }

    @Nullable
    static <V> V get(@Nullable Object obj, ImmutableMapEntry<?, V>[] immutableMapEntryArr, int i) {
        if (obj == null) {
            return null;
        }
        for (immutableMapEntryArr = immutableMapEntryArr[i & Hashing.smear(obj.hashCode())]; immutableMapEntryArr != null; immutableMapEntryArr = immutableMapEntryArr.getNextInKeyBucket()) {
            if (obj.equals(immutableMapEntryArr.getKey()) != 0) {
                return immutableMapEntryArr.getValue();
            }
        }
        return null;
    }

    public int size() {
        return this.entries.length;
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return new RegularEntrySet(this, this.entries);
    }
}
