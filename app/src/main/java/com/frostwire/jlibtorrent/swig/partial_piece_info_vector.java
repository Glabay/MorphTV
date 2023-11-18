package com.frostwire.jlibtorrent.swig;

public class partial_piece_info_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected partial_piece_info_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(partial_piece_info_vector partial_piece_info_vector) {
        return partial_piece_info_vector == null ? 0 : partial_piece_info_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_partial_piece_info_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public partial_piece_info_vector() {
        this(libtorrent_jni.new_partial_piece_info_vector(), true);
    }

    public long size() {
        return libtorrent_jni.partial_piece_info_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.partial_piece_info_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.partial_piece_info_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.partial_piece_info_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.partial_piece_info_vector_clear(this.swigCPtr, this);
    }

    public void push_back(partial_piece_info partial_piece_info) {
        libtorrent_jni.partial_piece_info_vector_push_back(this.swigCPtr, this, partial_piece_info.getCPtr(partial_piece_info), partial_piece_info);
    }

    public partial_piece_info get(int i) {
        return new partial_piece_info(libtorrent_jni.partial_piece_info_vector_get(this.swigCPtr, this, i), 0);
    }

    public void set(int i, partial_piece_info partial_piece_info) {
        libtorrent_jni.partial_piece_info_vector_set(this.swigCPtr, this, i, partial_piece_info.getCPtr(partial_piece_info), partial_piece_info);
    }
}
