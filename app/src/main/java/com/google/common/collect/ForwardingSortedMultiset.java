package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Multiset.Entry;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;

@GwtCompatible(emulated = true)
@Beta
public abstract class ForwardingSortedMultiset<E> extends ForwardingMultiset<E> implements SortedMultiset<E> {

    protected abstract class StandardDescendingMultiset extends DescendingMultiset<E> {
        SortedMultiset<E> forwardMultiset() {
            return ForwardingSortedMultiset.this;
        }
    }

    protected class StandardElementSet extends NavigableElementSet<E> {
        public StandardElementSet() {
            super(ForwardingSortedMultiset.this);
        }
    }

    protected abstract SortedMultiset<E> delegate();

    protected ForwardingSortedMultiset() {
    }

    public NavigableSet<E> elementSet() {
        return (NavigableSet) super.elementSet();
    }

    public Comparator<? super E> comparator() {
        return delegate().comparator();
    }

    public SortedMultiset<E> descendingMultiset() {
        return delegate().descendingMultiset();
    }

    public Entry<E> firstEntry() {
        return delegate().firstEntry();
    }

    protected Entry<E> standardFirstEntry() {
        Iterator it = entrySet().iterator();
        if (!it.hasNext()) {
            return null;
        }
        Entry entry = (Entry) it.next();
        return Multisets.immutableEntry(entry.getElement(), entry.getCount());
    }

    public Entry<E> lastEntry() {
        return delegate().lastEntry();
    }

    protected Entry<E> standardLastEntry() {
        Iterator it = descendingMultiset().entrySet().iterator();
        if (!it.hasNext()) {
            return null;
        }
        Entry entry = (Entry) it.next();
        return Multisets.immutableEntry(entry.getElement(), entry.getCount());
    }

    public Entry<E> pollFirstEntry() {
        return delegate().pollFirstEntry();
    }

    protected Entry<E> standardPollFirstEntry() {
        Iterator it = entrySet().iterator();
        if (!it.hasNext()) {
            return null;
        }
        Entry entry = (Entry) it.next();
        Entry<E> immutableEntry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
        it.remove();
        return immutableEntry;
    }

    public Entry<E> pollLastEntry() {
        return delegate().pollLastEntry();
    }

    protected Entry<E> standardPollLastEntry() {
        Iterator it = descendingMultiset().entrySet().iterator();
        if (!it.hasNext()) {
            return null;
        }
        Entry entry = (Entry) it.next();
        Entry<E> immutableEntry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
        it.remove();
        return immutableEntry;
    }

    public SortedMultiset<E> headMultiset(E e, BoundType boundType) {
        return delegate().headMultiset(e, boundType);
    }

    public SortedMultiset<E> subMultiset(E e, BoundType boundType, E e2, BoundType boundType2) {
        return delegate().subMultiset(e, boundType, e2, boundType2);
    }

    protected SortedMultiset<E> standardSubMultiset(E e, BoundType boundType, E e2, BoundType boundType2) {
        return tailMultiset(e, boundType).headMultiset(e2, boundType2);
    }

    public SortedMultiset<E> tailMultiset(E e, BoundType boundType) {
        return delegate().tailMultiset(e, boundType);
    }
}
