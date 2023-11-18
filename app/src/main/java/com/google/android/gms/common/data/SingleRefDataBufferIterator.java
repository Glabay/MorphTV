package com.google.android.gms.common.data;

import java.util.NoSuchElementException;

public class SingleRefDataBufferIterator<T> extends DataBufferIterator<T> {
    private T zzoj;

    public SingleRefDataBufferIterator(DataBuffer<T> dataBuffer) {
        super(dataBuffer);
    }

    public T next() {
        if (hasNext()) {
            this.mPosition++;
            if (this.mPosition == 0) {
                this.zzoj = this.mDataBuffer.get(0);
                if (!(this.zzoj instanceof DataBufferRef)) {
                    String valueOf = String.valueOf(this.zzoj.getClass());
                    StringBuilder stringBuilder = new StringBuilder(String.valueOf(valueOf).length() + 44);
                    stringBuilder.append("DataBuffer reference of type ");
                    stringBuilder.append(valueOf);
                    stringBuilder.append(" is not movable");
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
            ((DataBufferRef) this.zzoj).setDataRow(this.mPosition);
            return this.zzoj;
        }
        int i = this.mPosition;
        stringBuilder = new StringBuilder(46);
        stringBuilder.append("Cannot advance the iterator beyond ");
        stringBuilder.append(i);
        throw new NoSuchElementException(stringBuilder.toString());
    }
}
