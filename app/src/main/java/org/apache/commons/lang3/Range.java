package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Comparator;

public final class Range<T> implements Serializable {
    private static final long serialVersionUID = 1;
    private final Comparator<T> comparator;
    private transient int hashCode;
    private final T maximum;
    private final T minimum;
    private transient String toString;

    private enum ComparableComparator implements Comparator {
        INSTANCE;

        public int compare(Object obj, Object obj2) {
            return ((Comparable) obj).compareTo(obj2);
        }
    }

    public static <T extends Comparable<T>> Range<T> is(T t) {
        return between(t, t, null);
    }

    public static <T> Range<T> is(T t, Comparator<T> comparator) {
        return between(t, t, comparator);
    }

    public static <T extends Comparable<T>> Range<T> between(T t, T t2) {
        return between(t, t2, null);
    }

    public static <T> Range<T> between(T t, T t2, Comparator<T> comparator) {
        return new Range(t, t2, comparator);
    }

    private Range(T t, T t2, Comparator<T> comparator) {
        if (t != null) {
            if (t2 != null) {
                if (comparator == null) {
                    this.comparator = ComparableComparator.INSTANCE;
                } else {
                    this.comparator = comparator;
                }
                if (this.comparator.compare(t, t2) < 1) {
                    this.minimum = t;
                    this.maximum = t2;
                    return;
                }
                this.minimum = t2;
                this.maximum = t;
                return;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Elements in a range must not be null: element1=");
        stringBuilder.append(t);
        stringBuilder.append(", element2=");
        stringBuilder.append(t2);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public T getMinimum() {
        return this.minimum;
    }

    public T getMaximum() {
        return this.maximum;
    }

    public Comparator<T> getComparator() {
        return this.comparator;
    }

    public boolean isNaturalOrdering() {
        return this.comparator == ComparableComparator.INSTANCE;
    }

    public boolean contains(T t) {
        boolean z = false;
        if (t == null) {
            return false;
        }
        if (this.comparator.compare(t, this.minimum) > -1 && this.comparator.compare(t, this.maximum) < 1) {
            z = true;
        }
        return z;
    }

    public boolean isAfter(T t) {
        boolean z = false;
        if (t == null) {
            return false;
        }
        if (this.comparator.compare(t, this.minimum) < null) {
            z = true;
        }
        return z;
    }

    public boolean isStartedBy(T t) {
        boolean z = false;
        if (t == null) {
            return false;
        }
        if (this.comparator.compare(t, this.minimum) == null) {
            z = true;
        }
        return z;
    }

    public boolean isEndedBy(T t) {
        boolean z = false;
        if (t == null) {
            return false;
        }
        if (this.comparator.compare(t, this.maximum) == null) {
            z = true;
        }
        return z;
    }

    public boolean isBefore(T t) {
        boolean z = false;
        if (t == null) {
            return false;
        }
        if (this.comparator.compare(t, this.maximum) > null) {
            z = true;
        }
        return z;
    }

    public int elementCompareTo(T t) {
        if (t == null) {
            throw new NullPointerException("Element is null");
        } else if (isAfter(t)) {
            return -1;
        } else {
            return isBefore(t) != null ? 1 : null;
        }
    }

    public boolean containsRange(Range<T> range) {
        boolean z = false;
        if (range == null) {
            return false;
        }
        if (contains(range.minimum) && contains(range.maximum) != null) {
            z = true;
        }
        return z;
    }

    public boolean isAfterRange(Range<T> range) {
        return range == null ? null : isAfter(range.maximum);
    }

    public boolean isOverlappedBy(Range<T> range) {
        boolean z = false;
        if (range == null) {
            return false;
        }
        if (range.contains(this.minimum) || range.contains(this.maximum) || contains(range.minimum) != null) {
            z = true;
        }
        return z;
    }

    public boolean isBeforeRange(Range<T> range) {
        return range == null ? null : isBefore(range.minimum);
    }

    public Range<T> intersectionWith(Range<T> range) {
        if (!isOverlappedBy(range)) {
            throw new IllegalArgumentException(String.format("Cannot calculate intersection with non-overlapping range %s", new Object[]{range}));
        } else if (equals(range)) {
            return this;
        } else {
            return between(getComparator().compare(this.minimum, range.minimum) < 0 ? range.minimum : this.minimum, getComparator().compare(this.maximum, range.maximum) < 0 ? this.maximum : range.maximum, getComparator());
        }
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj != null) {
            if (obj.getClass() == getClass()) {
                Range range = (Range) obj;
                if (!this.minimum.equals(range.minimum) || this.maximum.equals(range.maximum) == null) {
                    z = false;
                }
                return z;
            }
        }
        return false;
    }

    public int hashCode() {
        int i = this.hashCode;
        if (this.hashCode != 0) {
            return i;
        }
        i = ((((629 + getClass().hashCode()) * 37) + this.minimum.hashCode()) * 37) + this.maximum.hashCode();
        this.hashCode = i;
        return i;
    }

    public String toString() {
        if (this.toString == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            stringBuilder.append(this.minimum);
            stringBuilder.append("..");
            stringBuilder.append(this.maximum);
            stringBuilder.append("]");
            this.toString = stringBuilder.toString();
        }
        return this.toString;
    }

    public String toString(String str) {
        return String.format(str, new Object[]{this.minimum, this.maximum, this.comparator});
    }
}
