package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class SingletonImmutableSet<E> extends ImmutableSet<E> {
    private transient int cachedHashCode;
    final transient E element;

    public boolean isEmpty() {
        return false;
    }

    boolean isPartialView() {
        return false;
    }

    public int size() {
        return 1;
    }

    SingletonImmutableSet(E e) {
        this.element = Preconditions.checkNotNull(e);
    }

    SingletonImmutableSet(E e, int i) {
        this.element = e;
        this.cachedHashCode = i;
    }

    public boolean contains(Object obj) {
        return this.element.equals(obj);
    }

    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }

    int copyIntoArray(Object[] objArr, int i) {
        objArr[i] = this.element;
        return i + 1;
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Set set = (Set) obj;
        if (set.size() != 1 || this.element.equals(set.iterator().next()) == null) {
            z = false;
        }
        return z;
    }

    public final int hashCode() {
        int i = this.cachedHashCode;
        if (i != 0) {
            return i;
        }
        i = this.element.hashCode();
        this.cachedHashCode = i;
        return i;
    }

    boolean isHashCodeFast() {
        return this.cachedHashCode != 0;
    }

    public String toString() {
        String obj = this.element.toString();
        StringBuilder stringBuilder = new StringBuilder(obj.length() + 2);
        stringBuilder.append('[');
        stringBuilder.append(obj);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}
