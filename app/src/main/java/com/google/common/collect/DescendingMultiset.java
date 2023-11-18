package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Multiset.Entry;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;

@GwtCompatible(emulated = true)
abstract class DescendingMultiset<E> extends ForwardingMultiset<E> implements SortedMultiset<E> {
    private transient Comparator<? super E> comparator;
    private transient NavigableSet<E> elementSet;
    private transient Set<Entry<E>> entrySet;

    /* renamed from: com.google.common.collect.DescendingMultiset$1 */
    class C08961 extends EntrySet<E> {
        C08961() {
        }

        Multiset<E> multiset() {
            return DescendingMultiset.this;
        }

        public Iterator<Entry<E>> iterator() {
            return DescendingMultiset.this.entryIterator();
        }

        public int size() {
            return DescendingMultiset.this.forwardMultiset().entrySet().size();
        }
    }

    abstract Iterator<Entry<E>> entryIterator();

    abstract SortedMultiset<E> forwardMultiset();

    DescendingMultiset() {
    }

    public Comparator<? super E> comparator() {
        Comparator<? super E> comparator = this.comparator;
        if (comparator != null) {
            return comparator;
        }
        comparator = Ordering.from(forwardMultiset().comparator()).reverse();
        this.comparator = comparator;
        return comparator;
    }

    public NavigableSet<E> elementSet() {
        NavigableSet<E> navigableSet = this.elementSet;
        if (navigableSet != null) {
            return navigableSet;
        }
        navigableSet = new NavigableElementSet(this);
        this.elementSet = navigableSet;
        return navigableSet;
    }

    public Entry<E> pollFirstEntry() {
        return forwardMultiset().pollLastEntry();
    }

    public Entry<E> pollLastEntry() {
        return forwardMultiset().pollFirstEntry();
    }

    public SortedMultiset<E> headMultiset(E e, BoundType boundType) {
        return forwardMultiset().tailMultiset(e, boundType).descendingMultiset();
    }

    public SortedMultiset<E> subMultiset(E e, BoundType boundType, E e2, BoundType boundType2) {
        return forwardMultiset().subMultiset(e2, boundType2, e, boundType).descendingMultiset();
    }

    public SortedMultiset<E> tailMultiset(E e, BoundType boundType) {
        return forwardMultiset().headMultiset(e, boundType).descendingMultiset();
    }

    protected Multiset<E> delegate() {
        return forwardMultiset();
    }

    public SortedMultiset<E> descendingMultiset() {
        return forwardMultiset();
    }

    public Entry<E> firstEntry() {
        return forwardMultiset().lastEntry();
    }

    public Entry<E> lastEntry() {
        return forwardMultiset().firstEntry();
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
        return new C08961();
    }

    public Iterator<E> iterator() {
        return Multisets.iteratorImpl(this);
    }

    public Object[] toArray() {
        return standardToArray();
    }

    public <T> T[] toArray(T[] tArr) {
        return standardToArray(tArr);
    }

    public String toString() {
        return entrySet().toString();
    }
}
