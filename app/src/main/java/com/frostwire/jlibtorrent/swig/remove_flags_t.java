package com.frostwire.jlibtorrent.swig;

public class remove_flags_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected remove_flags_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(remove_flags_t remove_flags_t) {
        return remove_flags_t == null ? 0 : remove_flags_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_remove_flags_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static remove_flags_t all() {
        return new remove_flags_t(libtorrent_jni.remove_flags_t_all(), true);
    }

    public boolean nonZero() {
        return libtorrent_jni.remove_flags_t_nonZero(this.swigCPtr, this);
    }

    public boolean eq(remove_flags_t remove_flags_t) {
        return libtorrent_jni.remove_flags_t_eq(this.swigCPtr, this, getCPtr(remove_flags_t), remove_flags_t);
    }

    public boolean ne(remove_flags_t remove_flags_t) {
        return libtorrent_jni.remove_flags_t_ne(this.swigCPtr, this, getCPtr(remove_flags_t), remove_flags_t);
    }

    public remove_flags_t or_(remove_flags_t remove_flags_t) {
        return new remove_flags_t(libtorrent_jni.remove_flags_t_or_(this.swigCPtr, this, getCPtr(remove_flags_t), remove_flags_t), true);
    }

    public remove_flags_t and_(remove_flags_t remove_flags_t) {
        return new remove_flags_t(libtorrent_jni.remove_flags_t_and_(this.swigCPtr, this, getCPtr(remove_flags_t), remove_flags_t), true);
    }

    public remove_flags_t xor(remove_flags_t remove_flags_t) {
        return new remove_flags_t(libtorrent_jni.remove_flags_t_xor(this.swigCPtr, this, getCPtr(remove_flags_t), remove_flags_t), true);
    }

    public remove_flags_t inv() {
        return new remove_flags_t(libtorrent_jni.remove_flags_t_inv(this.swigCPtr, this), true);
    }

    public int to_int() {
        return libtorrent_jni.remove_flags_t_to_int(this.swigCPtr, this);
    }

    public remove_flags_t() {
        this(libtorrent_jni.new_remove_flags_t(), true);
    }
}
