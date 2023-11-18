package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class ComparatorOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    final Comparator<T> comparator;

    ComparatorOrdering(Comparator<T> comparator) {
        this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
    }

    public int compare(T t, T t2) {
        return this.comparator.compare(t, t2);
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ComparatorOrdering)) {
            return null;
        }
        return this.comparator.equals(((ComparatorOrdering) obj).comparator);
    }

    public int hashCode() {
        return this.comparator.hashCode();
    }

    public String toString() {
        return this.comparator.toString();
    }
}
