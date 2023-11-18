package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.ListIterator;

@GwtCompatible
public abstract class ForwardingListIterator<E> extends ForwardingIterator<E> implements ListIterator<E> {
    protected abstract ListIterator<E> delegate();

    protected ForwardingListIterator() {
    }

    public void add(E e) {
        delegate().add(e);
    }

    public boolean hasPrevious() {
        return delegate().hasPrevious();
    }

    public int nextIndex() {
        return delegate().nextIndex();
    }

    public E previous() {
        return delegate().previous();
    }

    public int previousIndex() {
        return delegate().previousIndex();
    }

    public void set(E e) {
        delegate().set(e);
    }
}
