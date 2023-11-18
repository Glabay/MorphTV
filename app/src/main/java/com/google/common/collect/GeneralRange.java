package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class GeneralRange<T> implements Serializable {
    private final Comparator<? super T> comparator;
    private final boolean hasLowerBound;
    private final boolean hasUpperBound;
    private final BoundType lowerBoundType;
    @Nullable
    private final T lowerEndpoint;
    private transient GeneralRange<T> reverse;
    private final BoundType upperBoundType;
    @Nullable
    private final T upperEndpoint;

    static <T extends Comparable> GeneralRange<T> from(Range<T> range) {
        Comparable comparable = null;
        Object lowerEndpoint = range.hasLowerBound() ? range.lowerEndpoint() : null;
        BoundType lowerBoundType = range.hasLowerBound() ? range.lowerBoundType() : BoundType.OPEN;
        if (range.hasUpperBound()) {
            comparable = range.upperEndpoint();
        }
        return new GeneralRange(Ordering.natural(), range.hasLowerBound(), lowerEndpoint, lowerBoundType, range.hasUpperBound(), comparable, range.hasUpperBound() ? range.upperBoundType() : BoundType.OPEN);
    }

    static <T> GeneralRange<T> all(Comparator<? super T> comparator) {
        return new GeneralRange(comparator, false, null, BoundType.OPEN, false, null, BoundType.OPEN);
    }

    static <T> GeneralRange<T> downTo(Comparator<? super T> comparator, @Nullable T t, BoundType boundType) {
        return new GeneralRange(comparator, true, t, boundType, false, null, BoundType.OPEN);
    }

    static <T> GeneralRange<T> upTo(Comparator<? super T> comparator, @Nullable T t, BoundType boundType) {
        return new GeneralRange(comparator, false, null, BoundType.OPEN, true, t, boundType);
    }

    static <T> GeneralRange<T> range(Comparator<? super T> comparator, @Nullable T t, BoundType boundType, @Nullable T t2, BoundType boundType2) {
        return new GeneralRange(comparator, true, t, boundType, true, t2, boundType2);
    }

    private GeneralRange(Comparator<? super T> comparator, boolean z, @Nullable T t, BoundType boundType, boolean z2, @Nullable T t2, BoundType boundType2) {
        this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
        this.hasLowerBound = z;
        this.hasUpperBound = z2;
        this.lowerEndpoint = t;
        this.lowerBoundType = (BoundType) Preconditions.checkNotNull(boundType);
        this.upperEndpoint = t2;
        this.upperBoundType = (BoundType) Preconditions.checkNotNull(boundType2);
        if (z) {
            comparator.compare(t, t);
        }
        if (z2) {
            comparator.compare(t2, t2);
        }
        if (z && z2) {
            comparator = comparator.compare(t, t2);
            z = false;
            Preconditions.checkArgument(comparator <= null, "lowerEndpoint (%s) > upperEndpoint (%s)", t, t2);
            if (comparator == null) {
                comparator = boundType != BoundType.OPEN ? true : null;
                if (boundType2 != BoundType.OPEN) {
                    z = true;
                }
                Preconditions.checkArgument(comparator | z);
            }
        }
    }

    Comparator<? super T> comparator() {
        return this.comparator;
    }

    boolean hasLowerBound() {
        return this.hasLowerBound;
    }

    boolean hasUpperBound() {
        return this.hasUpperBound;
    }

    boolean isEmpty() {
        return (hasUpperBound() && tooLow(getUpperEndpoint())) || (hasLowerBound() && tooHigh(getLowerEndpoint()));
    }

    boolean tooLow(@Nullable T t) {
        if (!hasLowerBound()) {
            return false;
        }
        t = this.comparator.compare(t, getLowerEndpoint());
        int i = 1;
        int i2 = t < null ? 1 : 0;
        t = t == null ? true : null;
        if (getLowerBoundType() != BoundType.OPEN) {
            i = 0;
        }
        return (t & i) | i2;
    }

    boolean tooHigh(@Nullable T t) {
        if (!hasUpperBound()) {
            return false;
        }
        t = this.comparator.compare(t, getUpperEndpoint());
        int i = 1;
        int i2 = t > null ? 1 : 0;
        t = t == null ? true : null;
        if (getUpperBoundType() != BoundType.OPEN) {
            i = 0;
        }
        return (t & i) | i2;
    }

    boolean contains(@Nullable T t) {
        return (tooLow(t) || tooHigh(t) != null) ? null : true;
    }

    GeneralRange<T> intersect(GeneralRange<T> generalRange) {
        BoundType boundType;
        BoundType boundType2;
        Object obj;
        Preconditions.checkNotNull(generalRange);
        Preconditions.checkArgument(this.comparator.equals(generalRange.comparator));
        boolean z = this.hasLowerBound;
        Object lowerEndpoint = getLowerEndpoint();
        BoundType lowerBoundType = getLowerBoundType();
        if (!hasLowerBound()) {
            z = generalRange.hasLowerBound;
            lowerEndpoint = generalRange.getLowerEndpoint();
            lowerBoundType = generalRange.getLowerBoundType();
        } else if (generalRange.hasLowerBound()) {
            int compare = this.comparator.compare(getLowerEndpoint(), generalRange.getLowerEndpoint());
            if (compare < 0 || (compare == 0 && generalRange.getLowerBoundType() == BoundType.OPEN)) {
                lowerEndpoint = generalRange.getLowerEndpoint();
                lowerBoundType = generalRange.getLowerBoundType();
            }
        }
        boolean z2 = z;
        z = this.hasUpperBound;
        Object upperEndpoint = getUpperEndpoint();
        BoundType upperBoundType = getUpperBoundType();
        if (!hasUpperBound()) {
            z = generalRange.hasUpperBound;
            upperEndpoint = generalRange.getUpperEndpoint();
            upperBoundType = generalRange.getUpperBoundType();
        } else if (generalRange.hasUpperBound()) {
            int compare2 = this.comparator.compare(getUpperEndpoint(), generalRange.getUpperEndpoint());
            if (compare2 > 0 || (compare2 == 0 && generalRange.getUpperBoundType() == BoundType.OPEN)) {
                upperEndpoint = generalRange.getUpperEndpoint();
                upperBoundType = generalRange.getUpperBoundType();
            }
        }
        boolean z3 = z;
        Object obj2 = upperEndpoint;
        if (z2 && z3) {
            generalRange = this.comparator.compare(lowerEndpoint, obj2);
            if (generalRange > null || (generalRange == null && lowerBoundType == BoundType.OPEN && upperBoundType == BoundType.OPEN)) {
                boundType = BoundType.OPEN;
                boundType2 = BoundType.CLOSED;
                obj = obj2;
                return new GeneralRange(this.comparator, z2, obj, boundType, z3, obj2, boundType2);
            }
        }
        boundType = lowerBoundType;
        boundType2 = upperBoundType;
        obj = lowerEndpoint;
        return new GeneralRange(this.comparator, z2, obj, boundType, z3, obj2, boundType2);
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof GeneralRange)) {
            return false;
        }
        GeneralRange generalRange = (GeneralRange) obj;
        if (this.comparator.equals(generalRange.comparator) && this.hasLowerBound == generalRange.hasLowerBound && this.hasUpperBound == generalRange.hasUpperBound && getLowerBoundType().equals(generalRange.getLowerBoundType()) && getUpperBoundType().equals(generalRange.getUpperBoundType()) && Objects.equal(getLowerEndpoint(), generalRange.getLowerEndpoint()) && Objects.equal(getUpperEndpoint(), generalRange.getUpperEndpoint()) != null) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hashCode(this.comparator, getLowerEndpoint(), getLowerBoundType(), getUpperEndpoint(), getUpperBoundType());
    }

    GeneralRange<T> reverse() {
        GeneralRange<T> generalRange = this.reverse;
        if (generalRange != null) {
            return generalRange;
        }
        GeneralRange<T> generalRange2 = new GeneralRange(Ordering.from(this.comparator).reverse(), this.hasUpperBound, getUpperEndpoint(), getUpperBoundType(), this.hasLowerBound, getLowerEndpoint(), getLowerBoundType());
        generalRange2.reverse = this;
        this.reverse = generalRange2;
        return generalRange2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.comparator);
        stringBuilder.append(":");
        stringBuilder.append(this.lowerBoundType == BoundType.CLOSED ? '[' : '(');
        stringBuilder.append(this.hasLowerBound ? this.lowerEndpoint : "-∞");
        stringBuilder.append(',');
        stringBuilder.append(this.hasUpperBound ? this.upperEndpoint : "∞");
        stringBuilder.append(this.upperBoundType == BoundType.CLOSED ? ']' : ')');
        return stringBuilder.toString();
    }

    T getLowerEndpoint() {
        return this.lowerEndpoint;
    }

    BoundType getLowerBoundType() {
        return this.lowerBoundType;
    }

    T getUpperEndpoint() {
        return this.upperEndpoint;
    }

    BoundType getUpperBoundType() {
        return this.upperBoundType;
    }
}
