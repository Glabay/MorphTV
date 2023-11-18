package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
class EmptyImmutableSortedSet<E> extends ImmutableSortedSet<E> {
    public boolean contains(@Nullable Object obj) {
        return false;
    }

    int copyIntoArray(Object[] objArr, int i) {
        return i;
    }

    public int hashCode() {
        return 0;
    }

    ImmutableSortedSet<E> headSetImpl(E e, boolean z) {
        return this;
    }

    int indexOf(@Nullable Object obj) {
        return -1;
    }

    public boolean isEmpty() {
        return true;
    }

    boolean isPartialView() {
        return false;
    }

    public int size() {
        return 0;
    }

    ImmutableSortedSet<E> subSetImpl(E e, boolean z, E e2, boolean z2) {
        return this;
    }

    ImmutableSortedSet<E> tailSetImpl(E e, boolean z) {
        return this;
    }

    public String toString() {
        return "[]";
    }

    EmptyImmutableSortedSet(Comparator<? super E> comparator) {
        super(comparator);
    }

    public boolean containsAll(Collection<?> collection) {
        return collection.isEmpty();
    }

    public UnmodifiableIterator<E> iterator() {
        return Iterators.emptyIterator();
    }

    @GwtIncompatible("NavigableSet")
    public UnmodifiableIterator<E> descendingIterator() {
        return Iterators.emptyIterator();
    }

    public ImmutableList<E> asList() {
        return ImmutableList.of();
    }

    public boolean equals(@Nullable Object obj) {
        return obj instanceof Set ? ((Set) obj).isEmpty() : null;
    }

    public E first() {
        throw new NoSuchElementException();
    }

    public E last() {
        throw new NoSuchElementException();
    }

    ImmutableSortedSet<E> createDescendingSet() {
        return new EmptyImmutableSortedSet(Ordering.from(this.comparator).reverse());
    }
}
