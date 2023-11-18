package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingList<E> extends ForwardingCollection<E> implements List<E> {
    protected abstract List<E> delegate();

    protected ForwardingList() {
    }

    public void add(int i, E e) {
        delegate().add(i, e);
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        return delegate().addAll(i, collection);
    }

    public E get(int i) {
        return delegate().get(i);
    }

    public int indexOf(Object obj) {
        return delegate().indexOf(obj);
    }

    public int lastIndexOf(Object obj) {
        return delegate().lastIndexOf(obj);
    }

    public ListIterator<E> listIterator() {
        return delegate().listIterator();
    }

    public ListIterator<E> listIterator(int i) {
        return delegate().listIterator(i);
    }

    public E remove(int i) {
        return delegate().remove(i);
    }

    public E set(int i, E e) {
        return delegate().set(i, e);
    }

    public List<E> subList(int i, int i2) {
        return delegate().subList(i, i2);
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

    protected boolean standardAdd(E e) {
        add(size(), e);
        return true;
    }

    protected boolean standardAddAll(int i, Iterable<? extends E> iterable) {
        return Lists.addAllImpl(this, i, iterable);
    }

    protected int standardIndexOf(@Nullable Object obj) {
        return Lists.indexOfImpl(this, obj);
    }

    protected int standardLastIndexOf(@Nullable Object obj) {
        return Lists.lastIndexOfImpl(this, obj);
    }

    protected Iterator<E> standardIterator() {
        return listIterator();
    }

    protected ListIterator<E> standardListIterator() {
        return listIterator(0);
    }

    @Beta
    protected ListIterator<E> standardListIterator(int i) {
        return Lists.listIteratorImpl(this, i);
    }

    @Beta
    protected List<E> standardSubList(int i, int i2) {
        return Lists.subListImpl(this, i, i2);
    }

    @Beta
    protected boolean standardEquals(@Nullable Object obj) {
        return Lists.equalsImpl(this, obj);
    }

    @Beta
    protected int standardHashCode() {
        return Lists.hashCodeImpl(this);
    }
}
