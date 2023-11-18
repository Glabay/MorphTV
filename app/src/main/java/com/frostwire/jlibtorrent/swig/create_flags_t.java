package com.frostwire.jlibtorrent.swig;

public class create_flags_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected create_flags_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(create_flags_t create_flags_t) {
        return create_flags_t == null ? 0 : create_flags_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_create_flags_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static create_flags_t all() {
        return new create_flags_t(libtorrent_jni.create_flags_t_all(), true);
    }

    public boolean nonZero() {
        return libtorrent_jni.create_flags_t_nonZero(this.swigCPtr, this);
    }

    public boolean eq(create_flags_t create_flags_t) {
        return libtorrent_jni.create_flags_t_eq(this.swigCPtr, this, getCPtr(create_flags_t), create_flags_t);
    }

    public boolean ne(create_flags_t create_flags_t) {
        return libtorrent_jni.create_flags_t_ne(this.swigCPtr, this, getCPtr(create_flags_t), create_flags_t);
    }

    public create_flags_t or_(create_flags_t create_flags_t) {
        return new create_flags_t(libtorrent_jni.create_flags_t_or_(this.swigCPtr, this, getCPtr(create_flags_t), create_flags_t), true);
    }

    public create_flags_t and_(create_flags_t create_flags_t) {
        return new create_flags_t(libtorrent_jni.create_flags_t_and_(this.swigCPtr, this, getCPtr(create_flags_t), create_flags_t), true);
    }

    public create_flags_t xor(create_flags_t create_flags_t) {
        return new create_flags_t(libtorrent_jni.create_flags_t_xor(this.swigCPtr, this, getCPtr(create_flags_t), create_flags_t), true);
    }

    public create_flags_t inv() {
        return new create_flags_t(libtorrent_jni.create_flags_t_inv(this.swigCPtr, this), true);
    }

    public int to_int() {
        return libtorrent_jni.create_flags_t_to_int(this.swigCPtr, this);
    }

    public create_flags_t() {
        this(libtorrent_jni.new_create_flags_t(), true);
    }
}
