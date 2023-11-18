package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.SortedSet;

@GwtCompatible
final class SortedIterables {
    private SortedIterables() {
    }

    public static boolean hasSameComparator(Comparator<?> comparator, Iterable<?> iterable) {
        Preconditions.checkNotNull(comparator);
        Preconditions.checkNotNull(iterable);
        if (iterable instanceof SortedSet) {
            iterable = comparator((SortedSet) iterable);
        } else if (!(iterable instanceof SortedIterable)) {
            return null;
        } else {
            iterable = ((SortedIterable) iterable).comparator();
        }
        return comparator.equals(iterable);
    }

    public static <E> Comparator<? super E> comparator(SortedSet<E> sortedSet) {
        sortedSet = sortedSet.comparator();
        return sortedSet == null ? Ordering.natural() : sortedSet;
    }
}
