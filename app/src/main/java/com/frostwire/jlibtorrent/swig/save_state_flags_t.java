package com.frostwire.jlibtorrent.swig;

public class save_state_flags_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected save_state_flags_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(save_state_flags_t save_state_flags_t) {
        return save_state_flags_t == null ? 0 : save_state_flags_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_save_state_flags_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static save_state_flags_t all() {
        return new save_state_flags_t(libtorrent_jni.save_state_flags_t_all(), true);
    }

    public boolean nonZero() {
        return libtorrent_jni.save_state_flags_t_nonZero(this.swigCPtr, this);
    }

    public boolean eq(save_state_flags_t save_state_flags_t) {
        return libtorrent_jni.save_state_flags_t_eq(this.swigCPtr, this, getCPtr(save_state_flags_t), save_state_flags_t);
    }

    public boolean ne(save_state_flags_t save_state_flags_t) {
        return libtorrent_jni.save_state_flags_t_ne(this.swigCPtr, this, getCPtr(save_state_flags_t), save_state_flags_t);
    }

    public save_state_flags_t or_(save_state_flags_t save_state_flags_t) {
        return new save_state_flags_t(libtorrent_jni.save_state_flags_t_or_(this.swigCPtr, this, getCPtr(save_state_flags_t), save_state_flags_t), true);
    }

    public save_state_flags_t and_(save_state_flags_t save_state_flags_t) {
        return new save_state_flags_t(libtorrent_jni.save_state_flags_t_and_(this.swigCPtr, this, getCPtr(save_state_flags_t), save_state_flags_t), true);
    }

    public save_state_flags_t xor(save_state_flags_t save_state_flags_t) {
        return new save_state_flags_t(libtorrent_jni.save_state_flags_t_xor(this.swigCPtr, this, getCPtr(save_state_flags_t), save_state_flags_t), true);
    }

    public save_state_flags_t inv() {
        return new save_state_flags_t(libtorrent_jni.save_state_flags_t_inv(this.swigCPtr, this), true);
    }

    public int to_int() {
        return libtorrent_jni.save_state_flags_t_to_int(this.swigCPtr, this);
    }

    public save_state_flags_t() {
        this(libtorrent_jni.new_save_state_flags_t(), true);
    }
}
