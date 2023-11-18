package com.frostwire.jlibtorrent.swig;

public class pause_flags_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected pause_flags_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(pause_flags_t pause_flags_t) {
        return pause_flags_t == null ? 0 : pause_flags_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_pause_flags_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static pause_flags_t all() {
        return new pause_flags_t(libtorrent_jni.pause_flags_t_all(), true);
    }

    public boolean nonZero() {
        return libtorrent_jni.pause_flags_t_nonZero(this.swigCPtr, this);
    }

    public boolean eq(pause_flags_t pause_flags_t) {
        return libtorrent_jni.pause_flags_t_eq(this.swigCPtr, this, getCPtr(pause_flags_t), pause_flags_t);
    }

    public boolean ne(pause_flags_t pause_flags_t) {
        return libtorrent_jni.pause_flags_t_ne(this.swigCPtr, this, getCPtr(pause_flags_t), pause_flags_t);
    }

    public pause_flags_t or_(pause_flags_t pause_flags_t) {
        return new pause_flags_t(libtorrent_jni.pause_flags_t_or_(this.swigCPtr, this, getCPtr(pause_flags_t), pause_flags_t), true);
    }

    public pause_flags_t and_(pause_flags_t pause_flags_t) {
        return new pause_flags_t(libtorrent_jni.pause_flags_t_and_(this.swigCPtr, this, getCPtr(pause_flags_t), pause_flags_t), true);
    }

    public pause_flags_t xor(pause_flags_t pause_flags_t) {
        return new pause_flags_t(libtorrent_jni.pause_flags_t_xor(this.swigCPtr, this, getCPtr(pause_flags_t), pause_flags_t), true);
    }

    public pause_flags_t inv() {
        return new pause_flags_t(libtorrent_jni.pause_flags_t_inv(this.swigCPtr, this), true);
    }

    public int to_int() {
        return libtorrent_jni.pause_flags_t_to_int(this.swigCPtr, this);
    }

    public pause_flags_t() {
        this(libtorrent_jni.new_pause_flags_t(), true);
    }
}
