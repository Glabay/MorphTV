package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class ImmutableSortedAsList<E> extends RegularImmutableAsList<E> implements SortedIterable<E> {
    ImmutableSortedAsList(ImmutableSortedSet<E> immutableSortedSet, ImmutableList<E> immutableList) {
        super((ImmutableCollection) immutableSortedSet, (ImmutableList) immutableList);
    }

    ImmutableSortedSet<E> delegateCollection() {
        return (ImmutableSortedSet) super.delegateCollection();
    }

    public Comparator<? super E> comparator() {
        return delegateCollection().comparator();
    }

    @GwtIncompatible("ImmutableSortedSet.indexOf")
    public int indexOf(@Nullable Object obj) {
        int indexOf = delegateCollection().indexOf(obj);
        return (indexOf < 0 || get(indexOf).equals(obj) == null) ? -1 : indexOf;
    }

    @GwtIncompatible("ImmutableSortedSet.indexOf")
    public int lastIndexOf(@Nullable Object obj) {
        return indexOf(obj);
    }

    public boolean contains(Object obj) {
        return indexOf(obj) >= null ? true : null;
    }

    @GwtIncompatible("super.subListUnchecked does not exist; inherited subList is valid if slow")
    ImmutableList<E> subListUnchecked(int i, int i2) {
        return new RegularImmutableSortedSet(super.subListUnchecked(i, i2), comparator()).asList();
    }
}
