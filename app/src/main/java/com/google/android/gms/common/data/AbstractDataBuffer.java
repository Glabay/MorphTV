package com.google.android.gms.common.data;

import android.os.Bundle;
import java.util.Iterator;

public abstract class AbstractDataBuffer<T> implements DataBuffer<T> {
    protected final DataHolder mDataHolder;

    protected AbstractDataBuffer(DataHolder dataHolder) {
        this.mDataHolder = dataHolder;
    }

    @Deprecated
    public final void close() {
        release();
    }

    public abstract T get(int i);

    public int getCount() {
        return this.mDataHolder == null ? 0 : this.mDataHolder.getCount();
    }

    public Bundle getMetadata() {
        return this.mDataHolder.getMetadata();
    }

    @Deprecated
    public boolean isClosed() {
        if (this.mDataHolder != null) {
            if (!this.mDataHolder.isClosed()) {
                return false;
            }
        }
        return true;
    }

    public Iterator<T> iterator() {
        return new DataBufferIterator(this);
    }

    public void release() {
        if (this.mDataHolder != null) {
            this.mDataHolder.close();
        }
    }

    public Iterator<T> singleRefIterator() {
        return new SingleRefDataBufferIterator(this);
    }
}
