package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class ImmutableMapKeySet<K, V> extends ImmutableSet<K> {
    private final ImmutableMap<K, V> map;

    @GwtIncompatible("serialization")
    private static class KeySetSerializedForm<K> implements Serializable {
        private static final long serialVersionUID = 0;
        final ImmutableMap<K, ?> map;

        KeySetSerializedForm(ImmutableMap<K, ?> immutableMap) {
            this.map = immutableMap;
        }

        Object readResolve() {
            return this.map.keySet();
        }
    }

    boolean isPartialView() {
        return true;
    }

    ImmutableMapKeySet(ImmutableMap<K, V> immutableMap) {
        this.map = immutableMap;
    }

    public int size() {
        return this.map.size();
    }

    public UnmodifiableIterator<K> iterator() {
        return this.map.keyIterator();
    }

    public boolean contains(@Nullable Object obj) {
        return this.map.containsKey(obj);
    }

    ImmutableList<K> createAsList() {
        final ImmutableList asList = this.map.entrySet().asList();
        return new ImmutableAsList<K>() {
            public K get(int i) {
                return ((Entry) asList.get(i)).getKey();
            }

            ImmutableCollection<K> delegateCollection() {
                return ImmutableMapKeySet.this;
            }
        };
    }

    @GwtIncompatible("serialization")
    Object writeReplace() {
        return new KeySetSerializedForm(this.map);
    }
}
