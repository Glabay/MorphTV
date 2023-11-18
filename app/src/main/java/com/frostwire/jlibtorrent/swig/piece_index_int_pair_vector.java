package com.frostwire.jlibtorrent.swig;

public class piece_index_int_pair_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected piece_index_int_pair_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(piece_index_int_pair_vector piece_index_int_pair_vector) {
        return piece_index_int_pair_vector == null ? 0 : piece_index_int_pair_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_piece_index_int_pair_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public piece_index_int_pair_vector() {
        this(libtorrent_jni.new_piece_index_int_pair_vector(), true);
    }

    public long size() {
        return libtorrent_jni.piece_index_int_pair_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.piece_index_int_pair_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.piece_index_int_pair_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.piece_index_int_pair_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.piece_index_int_pair_vector_clear(this.swigCPtr, this);
    }

    public void push_back(piece_index_int_pair piece_index_int_pair) {
        libtorrent_jni.piece_index_int_pair_vector_push_back(this.swigCPtr, this, piece_index_int_pair.getCPtr(piece_index_int_pair), piece_index_int_pair);
    }

    public piece_index_int_pair get(int i) {
        return new piece_index_int_pair(libtorrent_jni.piece_index_int_pair_vector_get(this.swigCPtr, this, i), (boolean) 0);
    }

    public void set(int i, piece_index_int_pair piece_index_int_pair) {
        libtorrent_jni.piece_index_int_pair_vector_set(this.swigCPtr, this, i, piece_index_int_pair.getCPtr(piece_index_int_pair), piece_index_int_pair);
    }
}
