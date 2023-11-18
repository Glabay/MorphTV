package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.collect.Multiset.Entry;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMultiset<E> extends AbstractCollection<E> implements Multiset<E> {
    private transient Set<E> elementSet;
    private transient Set<Entry<E>> entrySet;

    class ElementSet extends ElementSet<E> {
        ElementSet() {
        }

        Multiset<E> multiset() {
            return AbstractMultiset.this;
        }
    }

    class EntrySet extends EntrySet<E> {
        EntrySet() {
        }

        Multiset<E> multiset() {
            return AbstractMultiset.this;
        }

        public Iterator<Entry<E>> iterator() {
            return AbstractMultiset.this.entryIterator();
        }

        public int size() {
            return AbstractMultiset.this.distinctElements();
        }
    }

    abstract int distinctElements();

    abstract Iterator<Entry<E>> entryIterator();

    AbstractMultiset() {
    }

    public int size() {
        return Multisets.sizeImpl(this);
    }

    public boolean isEmpty() {
        return entrySet().isEmpty();
    }

    public boolean contains(@Nullable Object obj) {
        return count(obj) > null ? true : null;
    }

    public Iterator<E> iterator() {
        return Multisets.iteratorImpl(this);
    }

    public int count(@Nullable Object obj) {
        for (Entry entry : entrySet()) {
            if (Objects.equal(entry.getElement(), obj)) {
                return entry.getCount();
            }
        }
        return null;
    }

    public boolean add(@Nullable E e) {
        add(e, 1);
        return true;
    }

    public int add(@Nullable E e, int i) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(@Nullable Object obj) {
        return remove(obj, 1) > null;
    }

    public int remove(@Nullable Object obj, int i) {
        throw new UnsupportedOperationException();
    }

    public int setCount(@Nullable E e, int i) {
        return Multisets.setCountImpl(this, e, i);
    }

    public boolean setCount(@Nullable E e, int i, int i2) {
        return Multisets.setCountImpl(this, e, i, i2);
    }

    public boolean addAll(Collection<? extends E> collection) {
        return Multisets.addAllImpl(this, collection);
    }

    public boolean removeAll(Collection<?> collection) {
        return Multisets.removeAllImpl(this, collection);
    }

    public boolean retainAll(Collection<?> collection) {
        return Multisets.retainAllImpl(this, collection);
    }

    public void clear() {
        Iterators.clear(entryIterator());
    }

    public Set<E> elementSet() {
        Set<E> set = this.elementSet;
        if (set != null) {
            return set;
        }
        set = createElementSet();
        this.elementSet = set;
        return set;
    }

    Set<E> createElementSet() {
        return new ElementSet();
    }

    public Set<Entry<E>> entrySet() {
        Set<Entry<E>> set = this.entrySet;
        if (set != null) {
            return set;
        }
        set = createEntrySet();
        this.entrySet = set;
        return set;
    }

    Set<Entry<E>> createEntrySet() {
        return new EntrySet();
    }

    public boolean equals(@Nullable Object obj) {
        return Multisets.equalsImpl(this, obj);
    }

    public int hashCode() {
        return entrySet().hashCode();
    }

    public String toString() {
        return entrySet().toString();
    }
}
