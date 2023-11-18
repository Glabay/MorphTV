package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import javax.annotation.Nullable;

class DescendingImmutableSortedSet<E> extends ImmutableSortedSet<E> {
    private final ImmutableSortedSet<E> forward;

    DescendingImmutableSortedSet(ImmutableSortedSet<E> immutableSortedSet) {
        super(Ordering.from(immutableSortedSet.comparator()).reverse());
        this.forward = immutableSortedSet;
    }

    public boolean contains(@Nullable Object obj) {
        return this.forward.contains(obj);
    }

    public int size() {
        return this.forward.size();
    }

    public UnmodifiableIterator<E> iterator() {
        return this.forward.descendingIterator();
    }

    ImmutableSortedSet<E> headSetImpl(E e, boolean z) {
        return this.forward.tailSet((Object) e, z).descendingSet();
    }

    ImmutableSortedSet<E> subSetImpl(E e, boolean z, E e2, boolean z2) {
        return this.forward.subSet((Object) e2, z2, (Object) e, z).descendingSet();
    }

    ImmutableSortedSet<E> tailSetImpl(E e, boolean z) {
        return this.forward.headSet((Object) e, z).descendingSet();
    }

    @GwtIncompatible("NavigableSet")
    public ImmutableSortedSet<E> descendingSet() {
        return this.forward;
    }

    @GwtIncompatible("NavigableSet")
    public UnmodifiableIterator<E> descendingIterator() {
        return this.forward.iterator();
    }

    @GwtIncompatible("NavigableSet")
    ImmutableSortedSet<E> createDescendingSet() {
        throw new AssertionError("should never be called");
    }

    public E lower(E e) {
        return this.forward.higher(e);
    }

    public E floor(E e) {
        return this.forward.ceiling(e);
    }

    public E ceiling(E e) {
        return this.forward.floor(e);
    }

    public E higher(E e) {
        return this.forward.lower(e);
    }

    int indexOf(@Nullable Object obj) {
        obj = this.forward.indexOf(obj);
        if (obj == -1) {
            return obj;
        }
        return (size() - 1) - obj;
    }

    boolean isPartialView() {
        return this.forward.isPartialView();
    }
}
