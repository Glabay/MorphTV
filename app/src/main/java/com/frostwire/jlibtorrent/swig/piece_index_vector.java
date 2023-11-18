package com.frostwire.jlibtorrent.swig;

public class piece_index_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected piece_index_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(piece_index_vector piece_index_vector) {
        return piece_index_vector == null ? 0 : piece_index_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_piece_index_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public piece_index_vector() {
        this(libtorrent_jni.new_piece_index_vector(), true);
    }

    public long size() {
        return libtorrent_jni.piece_index_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.piece_index_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.piece_index_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.piece_index_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.piece_index_vector_clear(this.swigCPtr, this);
    }

    public void push_back(int i) {
        libtorrent_jni.piece_index_vector_push_back(this.swigCPtr, this, i);
    }

    public int get(int i) {
        return libtorrent_jni.piece_index_vector_get(this.swigCPtr, this, i);
    }

    public void set(int i, int i2) {
        libtorrent_jni.piece_index_vector_set(this.swigCPtr, this, i, i2);
    }
}
