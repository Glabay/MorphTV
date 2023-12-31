package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Multiset.Entry;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public final class Multisets {
    private static final Ordering<Entry<?>> DECREASING_COUNT_ORDERING = new C10405();

    static abstract class AbstractEntry<E> implements Entry<E> {
        AbstractEntry() {
        }

        public boolean equals(@Nullable Object obj) {
            boolean z = false;
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            if (getCount() == entry.getCount() && Objects.equal(getElement(), entry.getElement()) != null) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            int i;
            Object element = getElement();
            if (element == null) {
                i = 0;
            } else {
                i = element.hashCode();
            }
            return i ^ getCount();
        }

        public String toString() {
            String valueOf = String.valueOf(getElement());
            int count = getCount();
            if (count == 1) {
                return valueOf;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(valueOf);
            stringBuilder.append(" x ");
            stringBuilder.append(count);
            return stringBuilder.toString();
        }
    }

    static abstract class ElementSet<E> extends ImprovedAbstractSet<E> {
        abstract Multiset<E> multiset();

        ElementSet() {
        }

        public void clear() {
            multiset().clear();
        }

        public boolean contains(Object obj) {
            return multiset().contains(obj);
        }

        public boolean containsAll(Collection<?> collection) {
            return multiset().containsAll(collection);
        }

        public boolean isEmpty() {
            return multiset().isEmpty();
        }

        public Iterator<E> iterator() {
            return new TransformedIterator<Entry<E>, E>(multiset().entrySet().iterator()) {
                E transform(Entry<E> entry) {
                    return entry.getElement();
                }
            };
        }

        public boolean remove(Object obj) {
            int count = multiset().count(obj);
            if (count <= 0) {
                return null;
            }
            multiset().remove(obj, count);
            return true;
        }

        public int size() {
            return multiset().entrySet().size();
        }
    }

    static abstract class EntrySet<E> extends ImprovedAbstractSet<Entry<E>> {
        abstract Multiset<E> multiset();

        EntrySet() {
        }

        public boolean contains(@Nullable Object obj) {
            boolean z = false;
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            if (entry.getCount() <= 0) {
                return false;
            }
            if (multiset().count(entry.getElement()) == entry.getCount()) {
                z = true;
            }
            return z;
        }

        public boolean remove(Object obj) {
            if (obj instanceof Entry) {
                Entry entry = (Entry) obj;
                Object element = entry.getElement();
                obj = entry.getCount();
                if (obj != null) {
                    return multiset().setCount(element, obj, 0);
                }
            }
            return false;
        }

        public void clear() {
            multiset().clear();
        }
    }

    /* renamed from: com.google.common.collect.Multisets$5 */
    static class C10405 extends Ordering<Entry<?>> {
        C10405() {
        }

        public int compare(Entry<?> entry, Entry<?> entry2) {
            return Ints.compare(entry2.getCount(), entry.getCount());
        }
    }

    private static final class FilteredMultiset<E> extends AbstractMultiset<E> {
        final Predicate<? super E> predicate;
        final Multiset<E> unfiltered;

        /* renamed from: com.google.common.collect.Multisets$FilteredMultiset$1 */
        class C10421 implements Predicate<Entry<E>> {
            C10421() {
            }

            public boolean apply(Entry<E> entry) {
                return FilteredMultiset.this.predicate.apply(entry.getElement());
            }
        }

        FilteredMultiset(Multiset<E> multiset, Predicate<? super E> predicate) {
            this.unfiltered = (Multiset) Preconditions.checkNotNull(multiset);
            this.predicate = (Predicate) Preconditions.checkNotNull(predicate);
        }

        public UnmodifiableIterator<E> iterator() {
            return Iterators.filter(this.unfiltered.iterator(), this.predicate);
        }

        Set<E> createElementSet() {
            return Sets.filter(this.unfiltered.elementSet(), this.predicate);
        }

        Set<Entry<E>> createEntrySet() {
            return Sets.filter(this.unfiltered.entrySet(), new C10421());
        }

        Iterator<Entry<E>> entryIterator() {
            throw new AssertionError("should never be called");
        }

        int distinctElements() {
            return elementSet().size();
        }

        public int count(@Nullable Object obj) {
            int count = this.unfiltered.count(obj);
            if (count <= 0) {
                return 0;
            }
            if (this.predicate.apply(obj) == null) {
                count = 0;
            }
            return count;
        }

        public int add(@Nullable E e, int i) {
            Preconditions.checkArgument(this.predicate.apply(e), "Element %s does not match predicate %s", e, this.predicate);
            return this.unfiltered.add(e, i);
        }

        public int remove(@Nullable Object obj, int i) {
            CollectPreconditions.checkNonnegative(i, "occurrences");
            if (i == 0) {
                return count(obj);
            }
            return contains(obj) ? this.unfiltered.remove(obj, i) : null;
        }

        public void clear() {
            elementSet().clear();
        }
    }

    static final class ImmutableEntry<E> extends AbstractEntry<E> implements Serializable {
        private static final long serialVersionUID = 0;
        final int count;
        @Nullable
        final E element;

        ImmutableEntry(@Nullable E e, int i) {
            this.element = e;
            this.count = i;
            CollectPreconditions.checkNonnegative(i, "count");
        }

        @Nullable
        public E getElement() {
            return this.element;
        }

        public int getCount() {
            return this.count;
        }
    }

    static final class MultisetIteratorImpl<E> implements Iterator<E> {
        private boolean canRemove;
        private Entry<E> currentEntry;
        private final Iterator<Entry<E>> entryIterator;
        private int laterCount;
        private final Multiset<E> multiset;
        private int totalCount;

        MultisetIteratorImpl(Multiset<E> multiset, Iterator<Entry<E>> it) {
            this.multiset = multiset;
            this.entryIterator = it;
        }

        public boolean hasNext() {
            if (this.laterCount <= 0) {
                if (!this.entryIterator.hasNext()) {
                    return false;
                }
            }
            return true;
        }

        public E next() {
            if (hasNext()) {
                if (this.laterCount == 0) {
                    this.currentEntry = (Entry) this.entryIterator.next();
                    int count = this.currentEntry.getCount();
                    this.laterCount = count;
                    this.totalCount = count;
                }
                this.laterCount--;
                this.canRemove = true;
                return this.currentEntry.getElement();
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            CollectPreconditions.checkRemove(this.canRemove);
            if (this.totalCount == 1) {
                this.entryIterator.remove();
            } else {
                this.multiset.remove(this.currentEntry.getElement());
            }
            this.totalCount--;
            this.canRemove = false;
        }
    }

    static class UnmodifiableMultiset<E> extends ForwardingMultiset<E> implements Serializable {
        private static final long serialVersionUID = 0;
        final Multiset<? extends E> delegate;
        transient Set<E> elementSet;
        transient Set<Entry<E>> entrySet;

        UnmodifiableMultiset(Multiset<? extends E> multiset) {
            this.delegate = multiset;
        }

        protected Multiset<E> delegate() {
            return this.delegate;
        }

        Set<E> createElementSet() {
            return Collections.unmodifiableSet(this.delegate.elementSet());
        }

        public Set<E> elementSet() {
            Set<E> set = this.elementSet;
            if (set != null) {
                return set;
            }
            set = createElementSet();
            this.elementSet = set;
            return set;
        }

        public Set<Entry<E>> entrySet() {
            Set<Entry<E>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = Collections.unmodifiableSet(this.delegate.entrySet());
            this.entrySet = set;
            return set;
        }

        public Iterator<E> iterator() {
            return Iterators.unmodifiableIterator(this.delegate.iterator());
        }

        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        public int add(E e, int i) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        public int remove(Object obj, int i) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public int setCount(E e, int i) {
            throw new UnsupportedOperationException();
        }

        public boolean setCount(E e, int i, int i2) {
            throw new UnsupportedOperationException();
        }
    }

    private Multisets() {
    }

    public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> multiset) {
        if (!(multiset instanceof UnmodifiableMultiset)) {
            if (!(multiset instanceof ImmutableMultiset)) {
                return new UnmodifiableMultiset((Multiset) Preconditions.checkNotNull(multiset));
            }
        }
        return multiset;
    }

    @Deprecated
    public static <E> Multiset<E> unmodifiableMultiset(ImmutableMultiset<E> immutableMultiset) {
        return (Multiset) Preconditions.checkNotNull(immutableMultiset);
    }

    @Beta
    public static <E> SortedMultiset<E> unmodifiableSortedMultiset(SortedMultiset<E> sortedMultiset) {
        return new UnmodifiableSortedMultiset((SortedMultiset) Preconditions.checkNotNull(sortedMultiset));
    }

    public static <E> Entry<E> immutableEntry(@Nullable E e, int i) {
        return new ImmutableEntry(e, i);
    }

    @Beta
    public static <E> Multiset<E> filter(Multiset<E> multiset, Predicate<? super E> predicate) {
        if (!(multiset instanceof FilteredMultiset)) {
            return new FilteredMultiset(multiset, predicate);
        }
        FilteredMultiset filteredMultiset = (FilteredMultiset) multiset;
        return new FilteredMultiset(filteredMultiset.unfiltered, Predicates.and(filteredMultiset.predicate, predicate));
    }

    static int inferDistinctElements(Iterable<?> iterable) {
        return iterable instanceof Multiset ? ((Multiset) iterable).elementSet().size() : 11;
    }

    @Beta
    public static <E> Multiset<E> union(final Multiset<? extends E> multiset, final Multiset<? extends E> multiset2) {
        Preconditions.checkNotNull(multiset);
        Preconditions.checkNotNull(multiset2);
        return new AbstractMultiset<E>() {
            public boolean contains(@Nullable Object obj) {
                if (!multiset.contains(obj)) {
                    if (multiset2.contains(obj) == null) {
                        return null;
                    }
                }
                return true;
            }

            public boolean isEmpty() {
                return multiset.isEmpty() && multiset2.isEmpty();
            }

            public int count(Object obj) {
                return Math.max(multiset.count(obj), multiset2.count(obj));
            }

            Set<E> createElementSet() {
                return Sets.union(multiset.elementSet(), multiset2.elementSet());
            }

            Iterator<Entry<E>> entryIterator() {
                final Iterator it = multiset.entrySet().iterator();
                final Iterator it2 = multiset2.entrySet().iterator();
                return new AbstractIterator<Entry<E>>() {
                    protected Entry<E> computeNext() {
                        if (it.hasNext()) {
                            Entry entry = (Entry) it.next();
                            Object element = entry.getElement();
                            return Multisets.immutableEntry(element, Math.max(entry.getCount(), multiset2.count(element)));
                        }
                        while (it2.hasNext()) {
                            entry = (Entry) it2.next();
                            element = entry.getElement();
                            if (!multiset.contains(element)) {
                                return Multisets.immutableEntry(element, entry.getCount());
                            }
                        }
                        return (Entry) endOfData();
                    }
                };
            }

            int distinctElements() {
                return elementSet().size();
            }
        };
    }

    public static <E> Multiset<E> intersection(final Multiset<E> multiset, final Multiset<?> multiset2) {
        Preconditions.checkNotNull(multiset);
        Preconditions.checkNotNull(multiset2);
        return new AbstractMultiset<E>() {
            public int count(Object obj) {
                int count = multiset.count(obj);
                if (count == 0) {
                    return null;
                }
                return Math.min(count, multiset2.count(obj));
            }

            Set<E> createElementSet() {
                return Sets.intersection(multiset.elementSet(), multiset2.elementSet());
            }

            Iterator<Entry<E>> entryIterator() {
                final Iterator it = multiset.entrySet().iterator();
                return new AbstractIterator<Entry<E>>() {
                    protected Entry<E> computeNext() {
                        while (it.hasNext()) {
                            Entry entry = (Entry) it.next();
                            Object element = entry.getElement();
                            int min = Math.min(entry.getCount(), multiset2.count(element));
                            if (min > 0) {
                                return Multisets.immutableEntry(element, min);
                            }
                        }
                        return (Entry) endOfData();
                    }
                };
            }

            int distinctElements() {
                return elementSet().size();
            }
        };
    }

    @Beta
    public static <E> Multiset<E> sum(final Multiset<? extends E> multiset, final Multiset<? extends E> multiset2) {
        Preconditions.checkNotNull(multiset);
        Preconditions.checkNotNull(multiset2);
        return new AbstractMultiset<E>() {
            public boolean contains(@Nullable Object obj) {
                if (!multiset.contains(obj)) {
                    if (multiset2.contains(obj) == null) {
                        return null;
                    }
                }
                return true;
            }

            public boolean isEmpty() {
                return multiset.isEmpty() && multiset2.isEmpty();
            }

            public int size() {
                return multiset.size() + multiset2.size();
            }

            public int count(Object obj) {
                return multiset.count(obj) + multiset2.count(obj);
            }

            Set<E> createElementSet() {
                return Sets.union(multiset.elementSet(), multiset2.elementSet());
            }

            Iterator<Entry<E>> entryIterator() {
                final Iterator it = multiset.entrySet().iterator();
                final Iterator it2 = multiset2.entrySet().iterator();
                return new AbstractIterator<Entry<E>>() {
                    protected Entry<E> computeNext() {
                        if (it.hasNext()) {
                            Entry entry = (Entry) it.next();
                            Object element = entry.getElement();
                            return Multisets.immutableEntry(element, entry.getCount() + multiset2.count(element));
                        }
                        while (it2.hasNext()) {
                            entry = (Entry) it2.next();
                            element = entry.getElement();
                            if (!multiset.contains(element)) {
                                return Multisets.immutableEntry(element, entry.getCount());
                            }
                        }
                        return (Entry) endOfData();
                    }
                };
            }

            int distinctElements() {
                return elementSet().size();
            }
        };
    }

    @Beta
    public static <E> Multiset<E> difference(final Multiset<E> multiset, final Multiset<?> multiset2) {
        Preconditions.checkNotNull(multiset);
        Preconditions.checkNotNull(multiset2);
        return new AbstractMultiset<E>() {
            public int count(@Nullable Object obj) {
                int count = multiset.count(obj);
                if (count == 0) {
                    return 0;
                }
                return Math.max(0, count - multiset2.count(obj));
            }

            Iterator<Entry<E>> entryIterator() {
                final Iterator it = multiset.entrySet().iterator();
                return new AbstractIterator<Entry<E>>() {
                    protected Entry<E> computeNext() {
                        while (it.hasNext()) {
                            Entry entry = (Entry) it.next();
                            Object element = entry.getElement();
                            int count = entry.getCount() - multiset2.count(element);
                            if (count > 0) {
                                return Multisets.immutableEntry(element, count);
                            }
                        }
                        return (Entry) endOfData();
                    }
                };
            }

            int distinctElements() {
                return Iterators.size(entryIterator());
            }
        };
    }

    public static boolean containsOccurrences(Multiset<?> multiset, Multiset<?> multiset2) {
        Preconditions.checkNotNull(multiset);
        Preconditions.checkNotNull(multiset2);
        multiset2 = multiset2.entrySet().iterator();
        while (multiset2.hasNext()) {
            Entry entry = (Entry) multiset2.next();
            if (multiset.count(entry.getElement()) < entry.getCount()) {
                return null;
            }
        }
        return true;
    }

    public static boolean retainOccurrences(Multiset<?> multiset, Multiset<?> multiset2) {
        return retainOccurrencesImpl(multiset, multiset2);
    }

    private static <E> boolean retainOccurrencesImpl(Multiset<E> multiset, Multiset<?> multiset2) {
        Preconditions.checkNotNull(multiset);
        Preconditions.checkNotNull(multiset2);
        Iterator it = multiset.entrySet().iterator();
        boolean z = false;
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            int count = multiset2.count(entry.getElement());
            if (count == 0) {
                it.remove();
            } else if (count < entry.getCount()) {
                multiset.setCount(entry.getElement(), count);
            }
            z = true;
        }
        return z;
    }

    public static boolean removeOccurrences(Multiset<?> multiset, Iterable<?> iterable) {
        if (iterable instanceof Multiset) {
            return removeOccurrencesImpl((Multiset) multiset, (Multiset) iterable);
        }
        return removeOccurrencesImpl((Multiset) multiset, (Iterable) iterable);
    }

    private static boolean removeOccurrencesImpl(Multiset<?> multiset, Iterable<?> iterable) {
        Preconditions.checkNotNull(multiset);
        Preconditions.checkNotNull(iterable);
        boolean z = false;
        for (Object remove : iterable) {
            z |= multiset.remove(remove);
        }
        return z;
    }

    private static <E> boolean removeOccurrencesImpl(Multiset<E> multiset, Multiset<?> multiset2) {
        Preconditions.checkNotNull(multiset);
        Preconditions.checkNotNull(multiset2);
        Iterator it = multiset.entrySet().iterator();
        boolean z = false;
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            int count = multiset2.count(entry.getElement());
            if (count >= entry.getCount()) {
                it.remove();
            } else if (count > 0) {
                multiset.remove(entry.getElement(), count);
            }
            z = true;
        }
        return z;
    }

    static boolean equalsImpl(Multiset<?> multiset, @Nullable Object obj) {
        if (obj == multiset) {
            return true;
        }
        if (!(obj instanceof Multiset)) {
            return false;
        }
        Multiset multiset2 = (Multiset) obj;
        if (multiset.size() == multiset2.size()) {
            if (multiset.entrySet().size() == multiset2.entrySet().size()) {
                for (Entry entry : multiset2.entrySet()) {
                    if (multiset.count(entry.getElement()) != entry.getCount()) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    static <E> boolean addAllImpl(Multiset<E> multiset, Collection<? extends E> collection) {
        if (collection.isEmpty()) {
            return null;
        }
        if (collection instanceof Multiset) {
            collection = cast(collection).entrySet().iterator();
            while (collection.hasNext()) {
                Entry entry = (Entry) collection.next();
                multiset.add(entry.getElement(), entry.getCount());
            }
        } else {
            Iterators.addAll(multiset, collection.iterator());
        }
        return true;
    }

    static boolean removeAllImpl(Multiset<?> multiset, Collection<?> collection) {
        if (collection instanceof Multiset) {
            collection = ((Multiset) collection).elementSet();
        }
        return multiset.elementSet().removeAll(collection);
    }

    static boolean retainAllImpl(Multiset<?> multiset, Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        if (collection instanceof Multiset) {
            collection = ((Multiset) collection).elementSet();
        }
        return multiset.elementSet().retainAll(collection);
    }

    static <E> int setCountImpl(Multiset<E> multiset, E e, int i) {
        CollectPreconditions.checkNonnegative(i, "count");
        int count = multiset.count(e);
        i -= count;
        if (i > 0) {
            multiset.add(e, i);
        } else if (i < 0) {
            multiset.remove(e, -i);
        }
        return count;
    }

    static <E> boolean setCountImpl(Multiset<E> multiset, E e, int i, int i2) {
        CollectPreconditions.checkNonnegative(i, "oldCount");
        CollectPreconditions.checkNonnegative(i2, "newCount");
        if (multiset.count(e) != i) {
            return null;
        }
        multiset.setCount(e, i2);
        return true;
    }

    static <E> Iterator<E> iteratorImpl(Multiset<E> multiset) {
        return new MultisetIteratorImpl(multiset, multiset.entrySet().iterator());
    }

    static int sizeImpl(Multiset<?> multiset) {
        multiset = multiset.entrySet().iterator();
        long j = 0;
        while (multiset.hasNext()) {
            j += (long) ((Entry) multiset.next()).getCount();
        }
        return Ints.saturatedCast(j);
    }

    static <T> Multiset<T> cast(Iterable<T> iterable) {
        return (Multiset) iterable;
    }

    @Beta
    public static <E> ImmutableMultiset<E> copyHighestCountFirst(Multiset<E> multiset) {
        return ImmutableMultiset.copyFromEntries(DECREASING_COUNT_ORDERING.immutableSortedCopy(multiset.entrySet()));
    }
}
