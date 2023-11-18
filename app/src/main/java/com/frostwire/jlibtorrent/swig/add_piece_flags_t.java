package com.frostwire.jlibtorrent.swig;

public class add_piece_flags_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected add_piece_flags_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(add_piece_flags_t add_piece_flags_t) {
        return add_piece_flags_t == null ? 0 : add_piece_flags_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_add_piece_flags_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static add_piece_flags_t all() {
        return new add_piece_flags_t(libtorrent_jni.add_piece_flags_t_all(), true);
    }

    public boolean nonZero() {
        return libtorrent_jni.add_piece_flags_t_nonZero(this.swigCPtr, this);
    }

    public boolean eq(add_piece_flags_t add_piece_flags_t) {
        return libtorrent_jni.add_piece_flags_t_eq(this.swigCPtr, this, getCPtr(add_piece_flags_t), add_piece_flags_t);
    }

    public boolean ne(add_piece_flags_t add_piece_flags_t) {
        return libtorrent_jni.add_piece_flags_t_ne(this.swigCPtr, this, getCPtr(add_piece_flags_t), add_piece_flags_t);
    }

    public add_piece_flags_t or_(add_piece_flags_t add_piece_flags_t) {
        return new add_piece_flags_t(libtorrent_jni.add_piece_flags_t_or_(this.swigCPtr, this, getCPtr(add_piece_flags_t), add_piece_flags_t), true);
    }

    public add_piece_flags_t and_(add_piece_flags_t add_piece_flags_t) {
        return new add_piece_flags_t(libtorrent_jni.add_piece_flags_t_and_(this.swigCPtr, this, getCPtr(add_piece_flags_t), add_piece_flags_t), true);
    }

    public add_piece_flags_t xor(add_piece_flags_t add_piece_flags_t) {
        return new add_piece_flags_t(libtorrent_jni.add_piece_flags_t_xor(this.swigCPtr, this, getCPtr(add_piece_flags_t), add_piece_flags_t), true);
    }

    public add_piece_flags_t inv() {
        return new add_piece_flags_t(libtorrent_jni.add_piece_flags_t_inv(this.swigCPtr, this), true);
    }

    public int to_int() {
        return libtorrent_jni.add_piece_flags_t_to_int(this.swigCPtr, this);
    }

    public add_piece_flags_t() {
        this(libtorrent_jni.new_add_piece_flags_t(), true);
    }
}
