package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class NullsLastOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    final Ordering<? super T> ordering;

    public <S extends T> Ordering<S> nullsLast() {
        return this;
    }

    NullsLastOrdering(Ordering<? super T> ordering) {
        this.ordering = ordering;
    }

    public int compare(@Nullable T t, @Nullable T t2) {
        if (t == t2) {
            return null;
        }
        if (t == null) {
            return 1;
        }
        return t2 == null ? -1 : this.ordering.compare(t, t2);
    }

    public <S extends T> Ordering<S> reverse() {
        return this.ordering.reverse().nullsFirst();
    }

    public <S extends T> Ordering<S> nullsFirst() {
        return this.ordering.nullsFirst();
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof NullsLastOrdering)) {
            return null;
        }
        return this.ordering.equals(((NullsLastOrdering) obj).ordering);
    }

    public int hashCode() {
        return this.ordering.hashCode() ^ -921210296;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.ordering);
        stringBuilder.append(".nullsLast()");
        return stringBuilder.toString();
    }
}
