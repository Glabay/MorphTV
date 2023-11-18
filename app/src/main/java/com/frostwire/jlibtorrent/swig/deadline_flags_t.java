package com.frostwire.jlibtorrent.swig;

public class deadline_flags_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected deadline_flags_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(deadline_flags_t deadline_flags_t) {
        return deadline_flags_t == null ? 0 : deadline_flags_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_deadline_flags_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static deadline_flags_t all() {
        return new deadline_flags_t(libtorrent_jni.deadline_flags_t_all(), true);
    }

    public boolean nonZero() {
        return libtorrent_jni.deadline_flags_t_nonZero(this.swigCPtr, this);
    }

    public boolean eq(deadline_flags_t deadline_flags_t) {
        return libtorrent_jni.deadline_flags_t_eq(this.swigCPtr, this, getCPtr(deadline_flags_t), deadline_flags_t);
    }

    public boolean ne(deadline_flags_t deadline_flags_t) {
        return libtorrent_jni.deadline_flags_t_ne(this.swigCPtr, this, getCPtr(deadline_flags_t), deadline_flags_t);
    }

    public deadline_flags_t or_(deadline_flags_t deadline_flags_t) {
        return new deadline_flags_t(libtorrent_jni.deadline_flags_t_or_(this.swigCPtr, this, getCPtr(deadline_flags_t), deadline_flags_t), true);
    }

    public deadline_flags_t and_(deadline_flags_t deadline_flags_t) {
        return new deadline_flags_t(libtorrent_jni.deadline_flags_t_and_(this.swigCPtr, this, getCPtr(deadline_flags_t), deadline_flags_t), true);
    }

    public deadline_flags_t xor(deadline_flags_t deadline_flags_t) {
        return new deadline_flags_t(libtorrent_jni.deadline_flags_t_xor(this.swigCPtr, this, getCPtr(deadline_flags_t), deadline_flags_t), true);
    }

    public deadline_flags_t inv() {
        return new deadline_flags_t(libtorrent_jni.deadline_flags_t_inv(this.swigCPtr, this), true);
    }

    public int to_int() {
        return libtorrent_jni.deadline_flags_t_to_int(this.swigCPtr, this);
    }

    public deadline_flags_t() {
        this(libtorrent_jni.new_deadline_flags_t(), true);
    }
}
