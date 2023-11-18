package com.frostwire.jlibtorrent.swig;

public class picker_flags_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected picker_flags_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(picker_flags_t picker_flags_t) {
        return picker_flags_t == null ? 0 : picker_flags_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_picker_flags_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static picker_flags_t all() {
        return new picker_flags_t(libtorrent_jni.picker_flags_t_all(), true);
    }

    public boolean nonZero() {
        return libtorrent_jni.picker_flags_t_nonZero(this.swigCPtr, this);
    }

    public boolean eq(picker_flags_t picker_flags_t) {
        return libtorrent_jni.picker_flags_t_eq(this.swigCPtr, this, getCPtr(picker_flags_t), picker_flags_t);
    }

    public boolean ne(picker_flags_t picker_flags_t) {
        return libtorrent_jni.picker_flags_t_ne(this.swigCPtr, this, getCPtr(picker_flags_t), picker_flags_t);
    }

    public picker_flags_t or_(picker_flags_t picker_flags_t) {
        return new picker_flags_t(libtorrent_jni.picker_flags_t_or_(this.swigCPtr, this, getCPtr(picker_flags_t), picker_flags_t), true);
    }

    public picker_flags_t and_(picker_flags_t picker_flags_t) {
        return new picker_flags_t(libtorrent_jni.picker_flags_t_and_(this.swigCPtr, this, getCPtr(picker_flags_t), picker_flags_t), true);
    }

    public picker_flags_t xor(picker_flags_t picker_flags_t) {
        return new picker_flags_t(libtorrent_jni.picker_flags_t_xor(this.swigCPtr, this, getCPtr(picker_flags_t), picker_flags_t), true);
    }

    public picker_flags_t inv() {
        return new picker_flags_t(libtorrent_jni.picker_flags_t_inv(this.swigCPtr, this), true);
    }

    public int to_int() {
        return libtorrent_jni.picker_flags_t_to_int(this.swigCPtr, this);
    }

    public picker_flags_t() {
        this(libtorrent_jni.new_picker_flags_t(), true);
    }
}
