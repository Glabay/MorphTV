package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

@GwtCompatible
class Floats$FloatArrayAsList extends AbstractList<Float> implements RandomAccess, Serializable {
    private static final long serialVersionUID = 0;
    final float[] array;
    final int end;
    final int start;

    public boolean isEmpty() {
        return false;
    }

    Floats$FloatArrayAsList(float[] fArr) {
        this(fArr, 0, fArr.length);
    }

    Floats$FloatArrayAsList(float[] fArr, int i, int i2) {
        this.array = fArr;
        this.start = i;
        this.end = i2;
    }

    public int size() {
        return this.end - this.start;
    }

    public Float get(int i) {
        Preconditions.checkElementIndex(i, size());
        return Float.valueOf(this.array[this.start + i]);
    }

    public boolean contains(Object obj) {
        return (!(obj instanceof Float) || Floats.access$000(this.array, ((Float) obj).floatValue(), this.start, this.end) == -1) ? null : true;
    }

    public int indexOf(Object obj) {
        if (obj instanceof Float) {
            obj = Floats.access$000(this.array, ((Float) obj).floatValue(), this.start, this.end);
            if (obj >= null) {
                return obj - this.start;
            }
        }
        return -1;
    }

    public int lastIndexOf(Object obj) {
        if (obj instanceof Float) {
            obj = Floats.access$100(this.array, ((Float) obj).floatValue(), this.start, this.end);
            if (obj >= null) {
                return obj - this.start;
            }
        }
        return -1;
    }

    public Float set(int i, Float f) {
        Preconditions.checkElementIndex(i, size());
        float f2 = this.array[this.start + i];
        this.array[this.start + i] = ((Float) Preconditions.checkNotNull(f)).floatValue();
        return Float.valueOf(f2);
    }

    public List<Float> subList(int i, int i2) {
        Preconditions.checkPositionIndexes(i, i2, size());
        if (i == i2) {
            return Collections.emptyList();
        }
        return new Floats$FloatArrayAsList(this.array, this.start + i, this.start + i2);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Floats$FloatArrayAsList)) {
            return super.equals(obj);
        }
        Floats$FloatArrayAsList floats$FloatArrayAsList = (Floats$FloatArrayAsList) obj;
        int size = size();
        if (floats$FloatArrayAsList.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (this.array[this.start + i] != floats$FloatArrayAsList.array[floats$FloatArrayAsList.start + i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = 1;
        for (int i2 = this.start; i2 < this.end; i2++) {
            i = (i * 31) + Floats.hashCode(this.array[i2]);
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

    float[] toFloatArray() {
        int size = size();
        Object obj = new float[size];
        System.arraycopy(this.array, this.start, obj, 0, size);
        return obj;
    }
}
