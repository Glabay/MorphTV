package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class EmptyImmutableSet extends ImmutableSet<Object> {
    static final EmptyImmutableSet INSTANCE = new EmptyImmutableSet();
    private static final long serialVersionUID = 0;

    public boolean contains(@Nullable Object obj) {
        return false;
    }

    int copyIntoArray(Object[] objArr, int i) {
        return i;
    }

    public final int hashCode() {
        return 0;
    }

    public boolean isEmpty() {
        return true;
    }

    boolean isHashCodeFast() {
        return true;
    }

    boolean isPartialView() {
        return false;
    }

    public int size() {
        return 0;
    }

    public String toString() {
        return "[]";
    }

    private EmptyImmutableSet() {
    }

    public boolean containsAll(Collection<?> collection) {
        return collection.isEmpty();
    }

    public UnmodifiableIterator<Object> iterator() {
        return Iterators.emptyIterator();
    }

    public ImmutableList<Object> asList() {
        return ImmutableList.of();
    }

    public boolean equals(@Nullable Object obj) {
        return obj instanceof Set ? ((Set) obj).isEmpty() : null;
    }

    Object readResolve() {
        return INSTANCE;
    }
}
