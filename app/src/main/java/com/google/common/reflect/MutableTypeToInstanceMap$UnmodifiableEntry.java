package com.google.common.reflect;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingMapEntry;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

final class MutableTypeToInstanceMap$UnmodifiableEntry<K, V> extends ForwardingMapEntry<K, V> {
    private final Entry<K, V> delegate;

    /* renamed from: com.google.common.reflect.MutableTypeToInstanceMap$UnmodifiableEntry$2 */
    static class C11822 implements Function<Entry<K, V>, Entry<K, V>> {
        C11822() {
        }

        public Entry<K, V> apply(Entry<K, V> entry) {
            return new MutableTypeToInstanceMap$UnmodifiableEntry(entry);
        }
    }

    static <K, V> Set<Entry<K, V>> transformEntries(final Set<Entry<K, V>> set) {
        return new ForwardingSet<Entry<K, V>>() {
            protected Set<Entry<K, V>> delegate() {
                return set;
            }

            public Iterator<Entry<K, V>> iterator() {
                return MutableTypeToInstanceMap$UnmodifiableEntry.transformEntries(super.iterator());
            }

            public Object[] toArray() {
                return standardToArray();
            }

            public <T> T[] toArray(T[] tArr) {
                return standardToArray(tArr);
            }
        };
    }

    private static <K, V> Iterator<Entry<K, V>> transformEntries(Iterator<Entry<K, V>> it) {
        return Iterators.transform(it, new C11822());
    }

    private MutableTypeToInstanceMap$UnmodifiableEntry(Entry<K, V> entry) {
        this.delegate = (Entry) Preconditions.checkNotNull(entry);
    }

    protected Entry<K, V> delegate() {
        return this.delegate;
    }

    public V setValue(V v) {
        throw new UnsupportedOperationException();
    }
}
