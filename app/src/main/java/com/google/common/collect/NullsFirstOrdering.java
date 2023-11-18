package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class NullsFirstOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    final Ordering<? super T> ordering;

    public <S extends T> Ordering<S> nullsFirst() {
        return this;
    }

    NullsFirstOrdering(Ordering<? super T> ordering) {
        this.ordering = ordering;
    }

    public int compare(@Nullable T t, @Nullable T t2) {
        if (t == t2) {
            return null;
        }
        if (t == null) {
            return -1;
        }
        return t2 == null ? 1 : this.ordering.compare(t, t2);
    }

    public <S extends T> Ordering<S> reverse() {
        return this.ordering.reverse().nullsLast();
    }

    public <S extends T> Ordering<S> nullsLast() {
        return this.ordering.nullsLast();
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof NullsFirstOrdering)) {
            return null;
        }
        return this.ordering.equals(((NullsFirstOrdering) obj).ordering);
    }

    public int hashCode() {
        return this.ordering.hashCode() ^ 957692532;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.ordering);
        stringBuilder.append(".nullsFirst()");
        return stringBuilder.toString();
    }
}
