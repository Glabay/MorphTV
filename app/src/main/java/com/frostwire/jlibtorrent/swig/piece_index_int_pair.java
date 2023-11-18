package com.frostwire.jlibtorrent.swig;

public class piece_index_int_pair {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected piece_index_int_pair(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(piece_index_int_pair piece_index_int_pair) {
        return piece_index_int_pair == null ? 0 : piece_index_int_pair.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_piece_index_int_pair(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public piece_index_int_pair() {
        this(libtorrent_jni.new_piece_index_int_pair__SWIG_0(), true);
    }

    public piece_index_int_pair(int i, int i2) {
        this(libtorrent_jni.new_piece_index_int_pair__SWIG_1(i, i2), true);
    }

    public piece_index_int_pair(piece_index_int_pair piece_index_int_pair) {
        this(libtorrent_jni.new_piece_index_int_pair__SWIG_2(getCPtr(piece_index_int_pair), piece_index_int_pair), true);
    }

    public void setFirst(int i) {
        libtorrent_jni.piece_index_int_pair_first_set(this.swigCPtr, this, i);
    }

    public int getFirst() {
        return libtorrent_jni.piece_index_int_pair_first_get(this.swigCPtr, this);
    }

    public void setSecond(int i) {
        libtorrent_jni.piece_index_int_pair_second_set(this.swigCPtr, this, i);
    }

    public int getSecond() {
        return libtorrent_jni.piece_index_int_pair_second_get(this.swigCPtr, this);
    }
}
