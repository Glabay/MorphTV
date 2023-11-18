package com.google.android.gms.common.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ShuffleFilteredDataBuffer<T> extends FilteredDataBuffer<T> {
    private final List<Integer> zzoh;
    private final int zzoi;

    public ShuffleFilteredDataBuffer(DataBuffer<T> dataBuffer, int i) {
        super(dataBuffer);
        this.zzoi = i;
        int i2 = this.zzoi;
        i = this.mDataBuffer.getCount();
        if (i2 > i) {
            throw new IllegalArgumentException("numIndexes must be smaller or equal to max");
        }
        Object arrayList = new ArrayList(i);
        for (int i3 = 0; i3 < i; i3++) {
            arrayList.add(Integer.valueOf(i3));
        }
        Collections.shuffle(arrayList);
        this.zzoh = arrayList.subList(0, i2);
    }

    public final int computeRealPosition(int i) {
        if (i >= 0) {
            if (i < getCount()) {
                return ((Integer) this.zzoh.get(i)).intValue();
            }
        }
        StringBuilder stringBuilder = new StringBuilder(53);
        stringBuilder.append("Position ");
        stringBuilder.append(i);
        stringBuilder.append(" is out of bounds for this buffer");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public final int getCount() {
        return Math.min(this.zzoi, this.mDataBuffer.getCount());
    }
}
