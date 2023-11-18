package com.frostwire.jlibtorrent.swig;

public class bandwidth_state_flags_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected bandwidth_state_flags_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(bandwidth_state_flags_t bandwidth_state_flags_t) {
        return bandwidth_state_flags_t == null ? 0 : bandwidth_state_flags_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_bandwidth_state_flags_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static bandwidth_state_flags_t all() {
        return new bandwidth_state_flags_t(libtorrent_jni.bandwidth_state_flags_t_all(), true);
    }

    public boolean nonZero() {
        return libtorrent_jni.bandwidth_state_flags_t_nonZero(this.swigCPtr, this);
    }

    public boolean eq(bandwidth_state_flags_t bandwidth_state_flags_t) {
        return libtorrent_jni.bandwidth_state_flags_t_eq(this.swigCPtr, this, getCPtr(bandwidth_state_flags_t), bandwidth_state_flags_t);
    }

    public boolean ne(bandwidth_state_flags_t bandwidth_state_flags_t) {
        return libtorrent_jni.bandwidth_state_flags_t_ne(this.swigCPtr, this, getCPtr(bandwidth_state_flags_t), bandwidth_state_flags_t);
    }

    public bandwidth_state_flags_t or_(bandwidth_state_flags_t bandwidth_state_flags_t) {
        return new bandwidth_state_flags_t(libtorrent_jni.bandwidth_state_flags_t_or_(this.swigCPtr, this, getCPtr(bandwidth_state_flags_t), bandwidth_state_flags_t), true);
    }

    public bandwidth_state_flags_t and_(bandwidth_state_flags_t bandwidth_state_flags_t) {
        return new bandwidth_state_flags_t(libtorrent_jni.bandwidth_state_flags_t_and_(this.swigCPtr, this, getCPtr(bandwidth_state_flags_t), bandwidth_state_flags_t), true);
    }

    public bandwidth_state_flags_t xor(bandwidth_state_flags_t bandwidth_state_flags_t) {
        return new bandwidth_state_flags_t(libtorrent_jni.bandwidth_state_flags_t_xor(this.swigCPtr, this, getCPtr(bandwidth_state_flags_t), bandwidth_state_flags_t), true);
    }

    public bandwidth_state_flags_t inv() {
        return new bandwidth_state_flags_t(libtorrent_jni.bandwidth_state_flags_t_inv(this.swigCPtr, this), true);
    }

    public int to_int() {
        return libtorrent_jni.bandwidth_state_flags_t_to_int(this.swigCPtr, this);
    }

    public bandwidth_state_flags_t() {
        this(libtorrent_jni.new_bandwidth_state_flags_t(), true);
    }
}
