package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

@GwtIncompatible("hasn't been tested yet")
@Beta
public abstract class ImmutableSortedMultiset<E> extends ImmutableSortedMultisetFauxverideShim<E> implements SortedMultiset<E> {
    private static final ImmutableSortedMultiset<Comparable> NATURAL_EMPTY_MULTISET = new EmptyImmutableSortedMultiset(NATURAL_ORDER);
    private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
    transient ImmutableSortedMultiset<E> descendingMultiset;

    public static class Builder<E> extends com.google.common.collect.ImmutableMultiset.Builder<E> {
        public Builder(Comparator<? super E> comparator) {
            super(TreeMultiset.create((Comparator) Preconditions.checkNotNull(comparator)));
        }

        public Builder<E> add(E e) {
            super.add((Object) e);
            return this;
        }

        public Builder<E> addCopies(E e, int i) {
            super.addCopies(e, i);
            return this;
        }

        public Builder<E> setCount(E e, int i) {
            super.setCount(e, i);
            return this;
        }

        public Builder<E> add(E... eArr) {
            super.add((Object[]) eArr);
            return this;
        }

        public Builder<E> addAll(Iterable<? extends E> iterable) {
            super.addAll((Iterable) iterable);
            return this;
        }

        public Builder<E> addAll(Iterator<? extends E> it) {
            super.addAll((Iterator) it);
            return this;
        }

        public ImmutableSortedMultiset<E> build() {
            return ImmutableSortedMultiset.copyOfSorted((SortedMultiset) this.contents);
        }
    }

    private static final class SerializedForm<E> implements Serializable {
        Comparator<? super E> comparator;
        int[] counts;
        E[] elements;

        SerializedForm(SortedMultiset<E> sortedMultiset) {
            this.comparator = sortedMultiset.comparator();
            int size = sortedMultiset.entrySet().size();
            this.elements = new Object[size];
            this.counts = new int[size];
            size = 0;
            for (E e : sortedMultiset.entrySet()) {
                this.elements[size] = e.getElement();
                this.counts[size] = e.getCount();
                size++;
            }
        }

        Object readResolve() {
            int length = this.elements.length;
            Builder builder = new Builder(this.comparator);
            for (int i = 0; i < length; i++) {
                builder.addCopies(this.elements[i], this.counts[i]);
            }
            return builder.build();
        }
    }

    public abstract ImmutableSortedSet<E> elementSet();

    public abstract ImmutableSortedMultiset<E> headMultiset(E e, BoundType boundType);

    public abstract ImmutableSortedMultiset<E> tailMultiset(E e, BoundType boundType);

    public static <E> ImmutableSortedMultiset<E> of() {
        return NATURAL_EMPTY_MULTISET;
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e) {
        return new RegularImmutableSortedMultiset((RegularImmutableSortedSet) ImmutableSortedSet.of(e), new int[]{1}, new long[]{0, 1}, 0, 1);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e, E e2) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{e, e2}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e, E e2, E e3) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{e, e2, e3}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e, E e2, E e3, E e4) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{e, e2, e3, e4}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e, E e2, E e3, E e4, E e5) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{e, e2, e3, e4, e5}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e, E e2, E e3, E e4, E e5, E e6, E... eArr) {
        Iterable newArrayListWithCapacity = Lists.newArrayListWithCapacity(eArr.length + 6);
        Collections.addAll(newArrayListWithCapacity, new Comparable[]{e, e2, e3, e4, e5, e6});
        Collections.addAll(newArrayListWithCapacity, eArr);
        return copyOf(Ordering.natural(), newArrayListWithCapacity);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> copyOf(E[] eArr) {
        return copyOf(Ordering.natural(), Arrays.asList(eArr));
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Iterable<? extends E> iterable) {
        return copyOf(Ordering.natural(), (Iterable) iterable);
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Iterator<? extends E> it) {
        return copyOf(Ordering.natural(), (Iterator) it);
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> it) {
        Preconditions.checkNotNull(comparator);
        return new Builder(comparator).addAll((Iterator) it).build();
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> iterable) {
        if (iterable instanceof ImmutableSortedMultiset) {
            ImmutableSortedMultiset<E> immutableSortedMultiset = (ImmutableSortedMultiset) iterable;
            if (comparator.equals(immutableSortedMultiset.comparator())) {
                return immutableSortedMultiset.isPartialView() != null ? copyOfSortedEntries(comparator, immutableSortedMultiset.entrySet().asList()) : immutableSortedMultiset;
            }
        }
        iterable = Lists.newArrayList((Iterable) iterable);
        Object create = TreeMultiset.create((Comparator) Preconditions.checkNotNull(comparator));
        Iterables.addAll(create, iterable);
        return copyOfSortedEntries(comparator, create.entrySet());
    }

    public static <E> ImmutableSortedMultiset<E> copyOfSorted(SortedMultiset<E> sortedMultiset) {
        return copyOfSortedEntries(sortedMultiset.comparator(), Lists.newArrayList(sortedMultiset.entrySet()));
    }

    private static <E> ImmutableSortedMultiset<E> copyOfSortedEntries(Comparator<? super E> comparator, Collection<Entry<E>> collection) {
        if (collection.isEmpty()) {
            return emptyMultiset(comparator);
        }
        com.google.common.collect.ImmutableList.Builder builder = new com.google.common.collect.ImmutableList.Builder(collection.size());
        int[] iArr = new int[collection.size()];
        long[] jArr = new long[(collection.size() + 1)];
        int i = 0;
        for (Entry entry : collection) {
            builder.add(entry.getElement());
            iArr[i] = entry.getCount();
            int i2 = i + 1;
            jArr[i2] = jArr[i] + ((long) iArr[i]);
            i = i2;
        }
        return new RegularImmutableSortedMultiset(new RegularImmutableSortedSet(builder.build(), comparator), iArr, jArr, 0, collection.size());
    }

    static <E> ImmutableSortedMultiset<E> emptyMultiset(Comparator<? super E> comparator) {
        if (NATURAL_ORDER.equals(comparator)) {
            return NATURAL_EMPTY_MULTISET;
        }
        return new EmptyImmutableSortedMultiset(comparator);
    }

    ImmutableSortedMultiset() {
    }

    public final Comparator<? super E> comparator() {
        return elementSet().comparator();
    }

    public ImmutableSortedMultiset<E> descendingMultiset() {
        ImmutableSortedMultiset<E> immutableSortedMultiset = this.descendingMultiset;
        if (immutableSortedMultiset != null) {
            return immutableSortedMultiset;
        }
        immutableSortedMultiset = new DescendingImmutableSortedMultiset(this);
        this.descendingMultiset = immutableSortedMultiset;
        return immutableSortedMultiset;
    }

    @Deprecated
    public final Entry<E> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final Entry<E> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    public ImmutableSortedMultiset<E> subMultiset(E e, BoundType boundType, E e2, BoundType boundType2) {
        Preconditions.checkArgument(comparator().compare(e, e2) <= 0, "Expected lowerBound <= upperBound but %s > %s", e, e2);
        return tailMultiset((Object) e, boundType).headMultiset((Object) e2, boundType2);
    }

    public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
        return new Builder(comparator);
    }

    public static <E extends Comparable<E>> Builder<E> reverseOrder() {
        return new Builder(Ordering.natural().reverse());
    }

    public static <E extends Comparable<E>> Builder<E> naturalOrder() {
        return new Builder(Ordering.natural());
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
