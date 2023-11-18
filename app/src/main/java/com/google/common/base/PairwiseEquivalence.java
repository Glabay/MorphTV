package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class PairwiseEquivalence<T> extends Equivalence<Iterable<T>> implements Serializable {
    private static final long serialVersionUID = 1;
    final Equivalence<? super T> elementEquivalence;

    PairwiseEquivalence(Equivalence<? super T> equivalence) {
        this.elementEquivalence = (Equivalence) Preconditions.checkNotNull(equivalence);
    }

    protected boolean doEquivalent(Iterable<T> iterable, Iterable<T> iterable2) {
        iterable = iterable.iterator();
        iterable2 = iterable2.iterator();
        do {
            boolean z = false;
            if (!iterable.hasNext() || !iterable2.hasNext()) {
                if (iterable.hasNext() == null && iterable2.hasNext() == null) {
                    z = true;
                }
                return z;
            }
        } while (this.elementEquivalence.equivalent(iterable.next(), iterable2.next()));
        return false;
    }

    protected int doHash(Iterable<T> iterable) {
        int i = 78721;
        for (T hash : iterable) {
            i = (i * 24943) + this.elementEquivalence.hash(hash);
        }
        return i;
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof PairwiseEquivalence)) {
            return null;
        }
        return this.elementEquivalence.equals(((PairwiseEquivalence) obj).elementEquivalence);
    }

    public int hashCode() {
        return this.elementEquivalence.hashCode() ^ 1185147655;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.elementEquivalence);
        stringBuilder.append(".pairwise()");
        return stringBuilder.toString();
    }
}
