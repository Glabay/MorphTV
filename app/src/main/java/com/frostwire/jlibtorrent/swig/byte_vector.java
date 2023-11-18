package com.frostwire.jlibtorrent.swig;

public class byte_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected byte_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(byte_vector byte_vector) {
        return byte_vector == null ? 0 : byte_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_byte_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public byte_vector() {
        this(libtorrent_jni.new_byte_vector(), true);
    }

    public long size() {
        return libtorrent_jni.byte_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.byte_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.byte_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.byte_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.byte_vector_clear(this.swigCPtr, this);
    }

    public void push_back(byte b) {
        libtorrent_jni.byte_vector_push_back(this.swigCPtr, this, b);
    }

    public void resize(long j) {
        libtorrent_jni.byte_vector_resize(this.swigCPtr, this, j);
    }

    public byte get(int i) {
        return libtorrent_jni.byte_vector_get(this.swigCPtr, this, i);
    }

    public void set(int i, byte b) {
        libtorrent_jni.byte_vector_set(this.swigCPtr, this, i, b);
    }
}
