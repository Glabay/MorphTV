package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Multiset.Entry;
import java.util.Comparator;
import java.util.NavigableSet;

@GwtCompatible(emulated = true)
final class UnmodifiableSortedMultiset<E> extends UnmodifiableMultiset<E> implements SortedMultiset<E> {
    private static final long serialVersionUID = 0;
    private transient UnmodifiableSortedMultiset<E> descendingMultiset;

    UnmodifiableSortedMultiset(SortedMultiset<E> sortedMultiset) {
        super(sortedMultiset);
    }

    protected SortedMultiset<E> delegate() {
        return (SortedMultiset) super.delegate();
    }

    public Comparator<? super E> comparator() {
        return delegate().comparator();
    }

    NavigableSet<E> createElementSet() {
        return Sets.unmodifiableNavigableSet(delegate().elementSet());
    }

    public NavigableSet<E> elementSet() {
        return (NavigableSet) super.elementSet();
    }

    public SortedMultiset<E> descendingMultiset() {
        SortedMultiset sortedMultiset = this.descendingMultiset;
        if (sortedMultiset != null) {
            return sortedMultiset;
        }
        sortedMultiset = new UnmodifiableSortedMultiset(delegate().descendingMultiset());
        sortedMultiset.descendingMultiset = this;
        this.descendingMultiset = sortedMultiset;
        return sortedMultiset;
    }

    public Entry<E> firstEntry() {
        return delegate().firstEntry();
    }

    public Entry<E> lastEntry() {
        return delegate().lastEntry();
    }

    public Entry<E> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    public Entry<E> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    public SortedMultiset<E> headMultiset(E e, BoundType boundType) {
        return Multisets.unmodifiableSortedMultiset(delegate().headMultiset(e, boundType));
    }

    public SortedMultiset<E> subMultiset(E e, BoundType boundType, E e2, BoundType boundType2) {
        return Multisets.unmodifiableSortedMultiset(delegate().subMultiset(e, boundType, e2, boundType2));
    }

    public SortedMultiset<E> tailMultiset(E e, BoundType boundType) {
        return Multisets.unmodifiableSortedMultiset(delegate().tailMultiset(e, boundType));
    }
}
