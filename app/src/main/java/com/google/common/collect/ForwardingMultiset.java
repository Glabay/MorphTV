package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.collect.Multiset.Entry;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingMultiset<E> extends ForwardingCollection<E> implements Multiset<E> {

    @Beta
    protected class StandardElementSet extends ElementSet<E> {
        Multiset<E> multiset() {
            return ForwardingMultiset.this;
        }
    }

    protected abstract Multiset<E> delegate();

    protected ForwardingMultiset() {
    }

    public int count(Object obj) {
        return delegate().count(obj);
    }

    public int add(E e, int i) {
        return delegate().add(e, i);
    }

    public int remove(Object obj, int i) {
        return delegate().remove(obj, i);
    }

    public Set<E> elementSet() {
        return delegate().elementSet();
    }

    public Set<Entry<E>> entrySet() {
        return delegate().entrySet();
    }

    public boolean equals(@Nullable Object obj) {
        if (obj != this) {
            if (delegate().equals(obj) == null) {
                return null;
            }
        }
        return true;
    }

    public int hashCode() {
        return delegate().hashCode();
    }

    public int setCount(E e, int i) {
        return delegate().setCount(e, i);
    }

    public boolean setCount(E e, int i, int i2) {
        return delegate().setCount(e, i, i2);
    }

    protected boolean standardContains(@Nullable Object obj) {
        return count(obj) > null ? true : null;
    }

    protected void standardClear() {
        Iterators.clear(entrySet().iterator());
    }

    @Beta
    protected int standardCount(@Nullable Object obj) {
        for (Entry entry : entrySet()) {
            if (Objects.equal(entry.getElement(), obj)) {
                return entry.getCount();
            }
        }
        return null;
    }

    protected boolean standardAdd(E e) {
        add(e, 1);
        return true;
    }

    @Beta
    protected boolean standardAddAll(Collection<? extends E> collection) {
        return Multisets.addAllImpl(this, collection);
    }

    protected boolean standardRemove(Object obj) {
        return remove(obj, 1) > null;
    }

    protected boolean standardRemoveAll(Collection<?> collection) {
        return Multisets.removeAllImpl(this, collection);
    }

    protected boolean standardRetainAll(Collection<?> collection) {
        return Multisets.retainAllImpl(this, collection);
    }

    protected int standardSetCount(E e, int i) {
        return Multisets.setCountImpl(this, e, i);
    }

    protected boolean standardSetCount(E e, int i, int i2) {
        return Multisets.setCountImpl(this, e, i, i2);
    }

    protected Iterator<E> standardIterator() {
        return Multisets.iteratorImpl(this);
    }

    protected int standardSize() {
        return Multisets.sizeImpl(this);
    }

    protected boolean standardEquals(@Nullable Object obj) {
        return Multisets.equalsImpl(this, obj);
    }

    protected int standardHashCode() {
        return entrySet().hashCode();
    }

    protected String standardToString() {
        return entrySet().toString();
    }
}
