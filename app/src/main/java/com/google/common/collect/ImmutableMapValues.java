package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class ImmutableMapValues<K, V> extends ImmutableCollection<V> {
    private final ImmutableMap<K, V> map;

    /* renamed from: com.google.common.collect.ImmutableMapValues$1 */
    class C09211 extends UnmodifiableIterator<V> {
        final UnmodifiableIterator<Entry<K, V>> entryItr = ImmutableMapValues.this.map.entrySet().iterator();

        C09211() {
        }

        public boolean hasNext() {
            return this.entryItr.hasNext();
        }

        public V next() {
            return ((Entry) this.entryItr.next()).getValue();
        }
    }

    @GwtIncompatible("serialization")
    private static class SerializedForm<V> implements Serializable {
        private static final long serialVersionUID = 0;
        final ImmutableMap<?, V> map;

        SerializedForm(ImmutableMap<?, V> immutableMap) {
            this.map = immutableMap;
        }

        Object readResolve() {
            return this.map.values();
        }
    }

    boolean isPartialView() {
        return true;
    }

    ImmutableMapValues(ImmutableMap<K, V> immutableMap) {
        this.map = immutableMap;
    }

    public int size() {
        return this.map.size();
    }

    public UnmodifiableIterator<V> iterator() {
        return new C09211();
    }

    public boolean contains(@Nullable Object obj) {
        return (obj == null || Iterators.contains(iterator(), obj) == null) ? null : true;
    }

    ImmutableList<V> createAsList() {
        final ImmutableList asList = this.map.entrySet().asList();
        return new ImmutableAsList<V>() {
            public V get(int i) {
                return ((Entry) asList.get(i)).getValue();
            }

            ImmutableCollection<V> delegateCollection() {
                return ImmutableMapValues.this;
            }
        };
    }

    @GwtIncompatible("serialization")
    Object writeReplace() {
        return new SerializedForm(this.map);
    }
}
