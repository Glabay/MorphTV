package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

@GwtIncompatible("java.util.ArrayDeque")
@Beta
public final class EvictingQueue<E> extends ForwardingQueue<E> implements Serializable {
    private static final long serialVersionUID = 0;
    private final Queue<E> delegate;
    @VisibleForTesting
    final int maxSize;

    private EvictingQueue(int i) {
        Preconditions.checkArgument(i >= 0, "maxSize (%s) must >= 0", Integer.valueOf(i));
        this.delegate = new ArrayDeque(i);
        this.maxSize = i;
    }

    public static <E> EvictingQueue<E> create(int i) {
        return new EvictingQueue(i);
    }

    public int remainingCapacity() {
        return this.maxSize - size();
    }

    protected Queue<E> delegate() {
        return this.delegate;
    }

    public boolean offer(E e) {
        return add(e);
    }

    public boolean add(E e) {
        Preconditions.checkNotNull(e);
        if (this.maxSize == 0) {
            return true;
        }
        if (size() == this.maxSize) {
            this.delegate.remove();
        }
        this.delegate.add(e);
        return true;
    }

    public boolean addAll(Collection<? extends E> collection) {
        return standardAddAll(collection);
    }

    public boolean contains(Object obj) {
        return delegate().contains(Preconditions.checkNotNull(obj));
    }

    public boolean remove(Object obj) {
        return delegate().remove(Preconditions.checkNotNull(obj));
    }
}
