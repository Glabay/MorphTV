package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class LexicographicalOrdering<T> extends Ordering<Iterable<T>> implements Serializable {
    private static final long serialVersionUID = 0;
    final Ordering<? super T> elementOrder;

    LexicographicalOrdering(Ordering<? super T> ordering) {
        this.elementOrder = ordering;
    }

    public int compare(Iterable<T> iterable, Iterable<T> iterable2) {
        iterable2 = iterable2.iterator();
        for (T compare : iterable) {
            if (!iterable2.hasNext()) {
                return 1;
            }
            int compare2 = this.elementOrder.compare(compare, iterable2.next());
            if (compare2 != 0) {
                return compare2;
            }
        }
        return iterable2.hasNext() != null ? -1 : null;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof LexicographicalOrdering)) {
            return null;
        }
        return this.elementOrder.equals(((LexicographicalOrdering) obj).elementOrder);
    }

    public int hashCode() {
        return this.elementOrder.hashCode() ^ 2075626741;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.elementOrder);
        stringBuilder.append(".lexicographical()");
        return stringBuilder.toString();
    }
}
