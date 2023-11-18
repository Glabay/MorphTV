package com.frostwire.jlibtorrent.swig;

public class status_flags_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected status_flags_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(status_flags_t status_flags_t) {
        return status_flags_t == null ? 0 : status_flags_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_status_flags_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static status_flags_t all() {
        return new status_flags_t(libtorrent_jni.status_flags_t_all(), true);
    }

    public boolean nonZero() {
        return libtorrent_jni.status_flags_t_nonZero(this.swigCPtr, this);
    }

    public boolean eq(status_flags_t status_flags_t) {
        return libtorrent_jni.status_flags_t_eq(this.swigCPtr, this, getCPtr(status_flags_t), status_flags_t);
    }

    public boolean ne(status_flags_t status_flags_t) {
        return libtorrent_jni.status_flags_t_ne(this.swigCPtr, this, getCPtr(status_flags_t), status_flags_t);
    }

    public status_flags_t or_(status_flags_t status_flags_t) {
        return new status_flags_t(libtorrent_jni.status_flags_t_or_(this.swigCPtr, this, getCPtr(status_flags_t), status_flags_t), true);
    }

    public status_flags_t and_(status_flags_t status_flags_t) {
        return new status_flags_t(libtorrent_jni.status_flags_t_and_(this.swigCPtr, this, getCPtr(status_flags_t), status_flags_t), true);
    }

    public status_flags_t xor(status_flags_t status_flags_t) {
        return new status_flags_t(libtorrent_jni.status_flags_t_xor(this.swigCPtr, this, getCPtr(status_flags_t), status_flags_t), true);
    }

    public status_flags_t inv() {
        return new status_flags_t(libtorrent_jni.status_flags_t_inv(this.swigCPtr, this), true);
    }

    public int to_int() {
        return libtorrent_jni.status_flags_t_to_int(this.swigCPtr, this);
    }

    public status_flags_t() {
        this(libtorrent_jni.new_status_flags_t(), true);
    }
}
