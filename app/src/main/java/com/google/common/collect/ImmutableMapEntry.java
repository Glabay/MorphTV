package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import javax.annotation.Nullable;

@GwtIncompatible("unnecessary")
abstract class ImmutableMapEntry<K, V> extends ImmutableEntry<K, V> {

    static final class TerminalEntry<K, V> extends ImmutableMapEntry<K, V> {
        @Nullable
        ImmutableMapEntry<K, V> getNextInKeyBucket() {
            return null;
        }

        @Nullable
        ImmutableMapEntry<K, V> getNextInValueBucket() {
            return null;
        }

        TerminalEntry(ImmutableMapEntry<K, V> immutableMapEntry) {
            super(immutableMapEntry);
        }

        TerminalEntry(K k, V v) {
            super(k, v);
        }
    }

    @Nullable
    abstract ImmutableMapEntry<K, V> getNextInKeyBucket();

    @Nullable
    abstract ImmutableMapEntry<K, V> getNextInValueBucket();

    static <K, V> ImmutableMapEntry<K, V>[] createEntryArray(int i) {
        return new ImmutableMapEntry[i];
    }

    ImmutableMapEntry(K k, V v) {
        super(k, v);
        CollectPreconditions.checkEntryNotNull(k, v);
    }

    ImmutableMapEntry(ImmutableMapEntry<K, V> immutableMapEntry) {
        super(immutableMapEntry.getKey(), immutableMapEntry.getValue());
    }
}
