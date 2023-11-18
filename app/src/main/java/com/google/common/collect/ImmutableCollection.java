package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public abstract class ImmutableCollection<E> extends AbstractCollection<E> implements Serializable {
    private transient ImmutableList<E> asList;

    public static abstract class Builder<E> {
        static final int DEFAULT_INITIAL_CAPACITY = 4;

        public abstract Builder<E> add(E e);

        public abstract ImmutableCollection<E> build();

        static int expandedCapacity(int i, int i2) {
            if (i2 < 0) {
                throw new AssertionError("cannot store more than MAX_VALUE elements");
            }
            i = (i + (i >> 1)) + 1;
            if (i < i2) {
                i = Integer.highestOneBit(i2 - 1) << 1;
            }
            return i < 0 ? Integer.MAX_VALUE : i;
        }

        Builder() {
        }

        public Builder<E> add(E... eArr) {
            for (Object add : eArr) {
                add(add);
            }
            return this;
        }

        public Builder<E> addAll(Iterable<? extends E> iterable) {
            for (Object add : iterable) {
                add(add);
            }
            return this;
        }

        public Builder<E> addAll(Iterator<? extends E> it) {
            while (it.hasNext()) {
                add(it.next());
            }
            return this;
        }
    }

    static abstract class ArrayBasedBuilder<E> extends Builder<E> {
        Object[] contents;
        int size = 0;

        ArrayBasedBuilder(int i) {
            CollectPreconditions.checkNonnegative(i, "initialCapacity");
            this.contents = new Object[i];
        }

        private void ensureCapacity(int i) {
            if (this.contents.length < i) {
                this.contents = ObjectArrays.arraysCopyOf(this.contents, Builder.expandedCapacity(this.contents.length, i));
            }
        }

        public ArrayBasedBuilder<E> add(E e) {
            Preconditions.checkNotNull(e);
            ensureCapacity(this.size + 1);
            Object[] objArr = this.contents;
            int i = this.size;
            this.size = i + 1;
            objArr[i] = e;
            return this;
        }

        public Builder<E> add(E... eArr) {
            ObjectArrays.checkElementsNotNull(eArr);
            ensureCapacity(this.size + eArr.length);
            System.arraycopy(eArr, 0, this.contents, this.size, eArr.length);
            this.size += eArr.length;
            return this;
        }

        public Builder<E> addAll(Iterable<? extends E> iterable) {
            if (iterable instanceof Collection) {
                ensureCapacity(this.size + ((Collection) iterable).size());
            }
            super.addAll((Iterable) iterable);
            return this;
        }
    }

    public abstract boolean contains(@Nullable Object obj);

    abstract boolean isPartialView();

    public abstract UnmodifiableIterator<E> iterator();

    ImmutableCollection() {
    }

    public final Object[] toArray() {
        int size = size();
        if (size == 0) {
            return ObjectArrays.EMPTY_ARRAY;
        }
        Object[] objArr = new Object[size];
        copyIntoArray(objArr, 0);
        return objArr;
    }

    public final <T> T[] toArray(T[] tArr) {
        Preconditions.checkNotNull(tArr);
        int size = size();
        if (tArr.length < size) {
            tArr = ObjectArrays.newArray((Object[]) tArr, size);
        } else if (tArr.length > size) {
            tArr[size] = null;
        }
        copyIntoArray(tArr, 0);
        return tArr;
    }

    @Deprecated
    public final boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final boolean remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    public ImmutableList<E> asList() {
        ImmutableList<E> immutableList = this.asList;
        if (immutableList != null) {
            return immutableList;
        }
        immutableList = createAsList();
        this.asList = immutableList;
        return immutableList;
    }

    ImmutableList<E> createAsList() {
        switch (size()) {
            case 0:
                return ImmutableList.of();
            case 1:
                return ImmutableList.of(iterator().next());
            default:
                return new RegularImmutableAsList(this, toArray());
        }
    }

    int copyIntoArray(Object[] objArr, int i) {
        Iterator it = iterator();
        while (it.hasNext()) {
            int i2 = i + 1;
            objArr[i] = it.next();
            i = i2;
        }
        return i;
    }

    Object writeReplace() {
        return new SerializedForm(toArray());
    }
}
