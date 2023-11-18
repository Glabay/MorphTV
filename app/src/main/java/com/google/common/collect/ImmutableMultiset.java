package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableMultiset<E> extends ImmutableCollection<E> implements Multiset<E> {
    private static final ImmutableMultiset<Object> EMPTY = new RegularImmutableMultiset(ImmutableMap.of(), 0);
    private transient ImmutableSet<Entry<E>> entrySet;

    public static class Builder<E> extends com.google.common.collect.ImmutableCollection.Builder<E> {
        final Multiset<E> contents;

        public Builder() {
            this(LinkedHashMultiset.create());
        }

        Builder(Multiset<E> multiset) {
            this.contents = multiset;
        }

        public Builder<E> add(E e) {
            this.contents.add(Preconditions.checkNotNull(e));
            return this;
        }

        public Builder<E> addCopies(E e, int i) {
            this.contents.add(Preconditions.checkNotNull(e), i);
            return this;
        }

        public Builder<E> setCount(E e, int i) {
            this.contents.setCount(Preconditions.checkNotNull(e), i);
            return this;
        }

        public Builder<E> add(E... eArr) {
            super.add((Object[]) eArr);
            return this;
        }

        public Builder<E> addAll(Iterable<? extends E> iterable) {
            if (iterable instanceof Multiset) {
                iterable = Multisets.cast(iterable).entrySet().iterator();
                while (iterable.hasNext()) {
                    Entry entry = (Entry) iterable.next();
                    addCopies(entry.getElement(), entry.getCount());
                }
            } else {
                super.addAll((Iterable) iterable);
            }
            return this;
        }

        public Builder<E> addAll(Iterator<? extends E> it) {
            super.addAll((Iterator) it);
            return this;
        }

        public ImmutableMultiset<E> build() {
            return ImmutableMultiset.copyOf(this.contents);
        }
    }

    private final class EntrySet extends ImmutableSet<Entry<E>> {
        private static final long serialVersionUID = 0;

        /* renamed from: com.google.common.collect.ImmutableMultiset$EntrySet$1 */
        class C09261 extends ImmutableAsList<Entry<E>> {
            C09261() {
            }

            public Entry<E> get(int i) {
                return ImmutableMultiset.this.getEntry(i);
            }

            ImmutableCollection<Entry<E>> delegateCollection() {
                return EntrySet.this;
            }
        }

        private EntrySet() {
        }

        boolean isPartialView() {
            return ImmutableMultiset.this.isPartialView();
        }

        public UnmodifiableIterator<Entry<E>> iterator() {
            return asList().iterator();
        }

        ImmutableList<Entry<E>> createAsList() {
            return new C09261();
        }

        public int size() {
            return ImmutableMultiset.this.elementSet().size();
        }

        public boolean contains(Object obj) {
            boolean z = false;
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            if (entry.getCount() <= 0) {
                return false;
            }
            if (ImmutableMultiset.this.count(entry.getElement()) == entry.getCount()) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return ImmutableMultiset.this.hashCode();
        }

        Object writeReplace() {
            return new EntrySetSerializedForm(ImmutableMultiset.this);
        }
    }

    static class EntrySetSerializedForm<E> implements Serializable {
        final ImmutableMultiset<E> multiset;

        EntrySetSerializedForm(ImmutableMultiset<E> immutableMultiset) {
            this.multiset = immutableMultiset;
        }

        Object readResolve() {
            return this.multiset.entrySet();
        }
    }

    private static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final int[] counts;
        final Object[] elements;

        SerializedForm(Multiset<?> multiset) {
            int size = multiset.entrySet().size();
            this.elements = new Object[size];
            this.counts = new int[size];
            multiset = multiset.entrySet().iterator();
            size = 0;
            while (multiset.hasNext()) {
                Entry entry = (Entry) multiset.next();
                this.elements[size] = entry.getElement();
                this.counts[size] = entry.getCount();
                size++;
            }
        }

        Object readResolve() {
            Iterable create = LinkedHashMultiset.create(this.elements.length);
            for (int i = 0; i < this.elements.length; i++) {
                create.add(this.elements[i], this.counts[i]);
            }
            return ImmutableMultiset.copyOf(create);
        }
    }

    abstract Entry<E> getEntry(int i);

    public static <E> ImmutableMultiset<E> of() {
        return EMPTY;
    }

    public static <E> ImmutableMultiset<E> of(E e) {
        return copyOfInternal(e);
    }

    public static <E> ImmutableMultiset<E> of(E e, E e2) {
        return copyOfInternal(e, e2);
    }

    public static <E> ImmutableMultiset<E> of(E e, E e2, E e3) {
        return copyOfInternal(e, e2, e3);
    }

    public static <E> ImmutableMultiset<E> of(E e, E e2, E e3, E e4) {
        return copyOfInternal(e, e2, e3, e4);
    }

    public static <E> ImmutableMultiset<E> of(E e, E e2, E e3, E e4, E e5) {
        return copyOfInternal(e, e2, e3, e4, e5);
    }

    public static <E> ImmutableMultiset<E> of(E e, E e2, E e3, E e4, E e5, E e6, E... eArr) {
        return new Builder().add((Object) e).add((Object) e2).add((Object) e3).add((Object) e4).add((Object) e5).add((Object) e6).add((Object[]) eArr).build();
    }

    public static <E> ImmutableMultiset<E> copyOf(E[] eArr) {
        return copyOf(Arrays.asList(eArr));
    }

    public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> iterable) {
        if (iterable instanceof ImmutableMultiset) {
            ImmutableMultiset<E> immutableMultiset = (ImmutableMultiset) iterable;
            if (!immutableMultiset.isPartialView()) {
                return immutableMultiset;
            }
        }
        return copyOfInternal(iterable instanceof Multiset ? Multisets.cast(iterable) : LinkedHashMultiset.create((Iterable) iterable));
    }

    private static <E> ImmutableMultiset<E> copyOfInternal(E... eArr) {
        return copyOf(Arrays.asList(eArr));
    }

    private static <E> ImmutableMultiset<E> copyOfInternal(Multiset<? extends E> multiset) {
        return copyFromEntries(multiset.entrySet());
    }

    static <E> ImmutableMultiset<E> copyFromEntries(Collection<? extends Entry<? extends E>> collection) {
        com.google.common.collect.ImmutableMap.Builder builder = new com.google.common.collect.ImmutableMap.Builder(collection.size());
        long j = 0;
        for (Entry entry : collection) {
            int count = entry.getCount();
            if (count > 0) {
                builder.put(entry.getElement(), Integer.valueOf(count));
                j += (long) count;
            }
        }
        if (j == 0) {
            return of();
        }
        return new RegularImmutableMultiset(builder.build(), Ints.saturatedCast(j));
    }

    public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> it) {
        Multiset create = LinkedHashMultiset.create();
        Iterators.addAll(create, it);
        return copyOfInternal(create);
    }

    ImmutableMultiset() {
    }

    public UnmodifiableIterator<E> iterator() {
        final Iterator it = entrySet().iterator();
        return new UnmodifiableIterator<E>() {
            E element;
            int remaining;

            public boolean hasNext() {
                if (this.remaining <= 0) {
                    if (!it.hasNext()) {
                        return false;
                    }
                }
                return true;
            }

            public E next() {
                if (this.remaining <= 0) {
                    Entry entry = (Entry) it.next();
                    this.element = entry.getElement();
                    this.remaining = entry.getCount();
                }
                this.remaining--;
                return this.element;
            }
        };
    }

    public boolean contains(@Nullable Object obj) {
        return count(obj) > null ? true : null;
    }

    public boolean containsAll(Collection<?> collection) {
        return elementSet().containsAll(collection);
    }

    @Deprecated
    public final int add(E e, int i) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final int remove(Object obj, int i) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final int setCount(E e, int i) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final boolean setCount(E e, int i, int i2) {
        throw new UnsupportedOperationException();
    }

    @GwtIncompatible("not present in emulated superclass")
    int copyIntoArray(Object[] objArr, int i) {
        Iterator it = entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            Arrays.fill(objArr, i, entry.getCount() + i, entry.getElement());
            i += entry.getCount();
        }
        return i;
    }

    public boolean equals(@Nullable Object obj) {
        return Multisets.equalsImpl(this, obj);
    }

    public int hashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    public String toString() {
        return entrySet().toString();
    }

    public ImmutableSet<Entry<E>> entrySet() {
        ImmutableSet<Entry<E>> immutableSet = this.entrySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        immutableSet = createEntrySet();
        this.entrySet = immutableSet;
        return immutableSet;
    }

    private final ImmutableSet<Entry<E>> createEntrySet() {
        return isEmpty() ? ImmutableSet.of() : new EntrySet();
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }

    public static <E> Builder<E> builder() {
        return new Builder();
    }
}
