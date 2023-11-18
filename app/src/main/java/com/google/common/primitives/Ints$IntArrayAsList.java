package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

@GwtCompatible
class Ints$IntArrayAsList extends AbstractList<Integer> implements RandomAccess, Serializable {
    private static final long serialVersionUID = 0;
    final int[] array;
    final int end;
    final int start;

    public boolean isEmpty() {
        return false;
    }

    Ints$IntArrayAsList(int[] iArr) {
        this(iArr, 0, iArr.length);
    }

    Ints$IntArrayAsList(int[] iArr, int i, int i2) {
        this.array = iArr;
        this.start = i;
        this.end = i2;
    }

    public int size() {
        return this.end - this.start;
    }

    public Integer get(int i) {
        Preconditions.checkElementIndex(i, size());
        return Integer.valueOf(this.array[this.start + i]);
    }

    public boolean contains(Object obj) {
        return (!(obj instanceof Integer) || Ints.access$000(this.array, ((Integer) obj).intValue(), this.start, this.end) == -1) ? null : true;
    }

    public int indexOf(Object obj) {
        if (obj instanceof Integer) {
            obj = Ints.access$000(this.array, ((Integer) obj).intValue(), this.start, this.end);
            if (obj >= null) {
                return obj - this.start;
            }
        }
        return -1;
    }

    public int lastIndexOf(Object obj) {
        if (obj instanceof Integer) {
            obj = Ints.access$100(this.array, ((Integer) obj).intValue(), this.start, this.end);
            if (obj >= null) {
                return obj - this.start;
            }
        }
        return -1;
    }

    public Integer set(int i, Integer num) {
        Preconditions.checkElementIndex(i, size());
        int i2 = this.array[this.start + i];
        this.array[this.start + i] = ((Integer) Preconditions.checkNotNull(num)).intValue();
        return Integer.valueOf(i2);
    }

    public List<Integer> subList(int i, int i2) {
        Preconditions.checkPositionIndexes(i, i2, size());
        if (i == i2) {
            return Collections.emptyList();
        }
        return new Ints$IntArrayAsList(this.array, this.start + i, this.start + i2);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Ints$IntArrayAsList)) {
            return super.equals(obj);
        }
        Ints$IntArrayAsList ints$IntArrayAsList = (Ints$IntArrayAsList) obj;
        int size = size();
        if (ints$IntArrayAsList.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (this.array[this.start + i] != ints$IntArrayAsList.array[ints$IntArrayAsList.start + i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = 1;
        for (int i2 = this.start; i2 < this.end; i2++) {
            i = (i * 31) + Ints.hashCode(this.array[i2]);
        }
        return i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(size() * 5);
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

    int[] toIntArray() {
        int size = size();
        Object obj = new int[size];
        System.arraycopy(this.array, this.start, obj, 0, size);
        return obj;
    }
}
