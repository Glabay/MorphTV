package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

@GwtCompatible
class Doubles$DoubleArrayAsList extends AbstractList<Double> implements RandomAccess, Serializable {
    private static final long serialVersionUID = 0;
    final double[] array;
    final int end;
    final int start;

    public boolean isEmpty() {
        return false;
    }

    Doubles$DoubleArrayAsList(double[] dArr) {
        this(dArr, 0, dArr.length);
    }

    Doubles$DoubleArrayAsList(double[] dArr, int i, int i2) {
        this.array = dArr;
        this.start = i;
        this.end = i2;
    }

    public int size() {
        return this.end - this.start;
    }

    public Double get(int i) {
        Preconditions.checkElementIndex(i, size());
        return Double.valueOf(this.array[this.start + i]);
    }

    public boolean contains(Object obj) {
        return (!(obj instanceof Double) || Doubles.access$000(this.array, ((Double) obj).doubleValue(), this.start, this.end) == -1) ? null : true;
    }

    public int indexOf(Object obj) {
        if (obj instanceof Double) {
            obj = Doubles.access$000(this.array, ((Double) obj).doubleValue(), this.start, this.end);
            if (obj >= null) {
                return obj - this.start;
            }
        }
        return -1;
    }

    public int lastIndexOf(Object obj) {
        if (obj instanceof Double) {
            obj = Doubles.access$100(this.array, ((Double) obj).doubleValue(), this.start, this.end);
            if (obj >= null) {
                return obj - this.start;
            }
        }
        return -1;
    }

    public Double set(int i, Double d) {
        Preconditions.checkElementIndex(i, size());
        double d2 = this.array[this.start + i];
        this.array[this.start + i] = ((Double) Preconditions.checkNotNull(d)).doubleValue();
        return Double.valueOf(d2);
    }

    public List<Double> subList(int i, int i2) {
        Preconditions.checkPositionIndexes(i, i2, size());
        if (i == i2) {
            return Collections.emptyList();
        }
        return new Doubles$DoubleArrayAsList(this.array, this.start + i, this.start + i2);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Doubles$DoubleArrayAsList)) {
            return super.equals(obj);
        }
        Doubles$DoubleArrayAsList doubles$DoubleArrayAsList = (Doubles$DoubleArrayAsList) obj;
        int size = size();
        if (doubles$DoubleArrayAsList.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (this.array[this.start + i] != doubles$DoubleArrayAsList.array[doubles$DoubleArrayAsList.start + i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = 1;
        for (int i2 = this.start; i2 < this.end; i2++) {
            i = (i * 31) + Doubles.hashCode(this.array[i2]);
        }
        return i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(size() * 12);
        stringBuilder.append('[');
        stringBuilder.append(this.array[this.start]);
        int i = this.start;
        while (true) {
            i++;
            if (i < this.end) {
                stringBuilder.append(", ");
                stringBuilder.append(this.array[i]);
            } else {
                stringBuilder.append(']');
                return stringBuilder.toString();
            }
        }
    }

    double[] toDoubleArray() {
        int size = size();
        Object obj = new double[size];
        System.arraycopy(this.array, this.start, obj, 0, size);
        return obj;
    }
}
