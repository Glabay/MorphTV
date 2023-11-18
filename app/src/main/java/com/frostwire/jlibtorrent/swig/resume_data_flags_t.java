package com.frostwire.jlibtorrent.swig;

public class resume_data_flags_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected resume_data_flags_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(resume_data_flags_t resume_data_flags_t) {
        return resume_data_flags_t == null ? 0 : resume_data_flags_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_resume_data_flags_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static resume_data_flags_t all() {
        return new resume_data_flags_t(libtorrent_jni.resume_data_flags_t_all(), true);
    }

    public boolean nonZero() {
        return libtorrent_jni.resume_data_flags_t_nonZero(this.swigCPtr, this);
    }

    public boolean eq(resume_data_flags_t resume_data_flags_t) {
        return libtorrent_jni.resume_data_flags_t_eq(this.swigCPtr, this, getCPtr(resume_data_flags_t), resume_data_flags_t);
    }

    public boolean ne(resume_data_flags_t resume_data_flags_t) {
        return libtorrent_jni.resume_data_flags_t_ne(this.swigCPtr, this, getCPtr(resume_data_flags_t), resume_data_flags_t);
    }

    public resume_data_flags_t or_(resume_data_flags_t resume_data_flags_t) {
        return new resume_data_flags_t(libtorrent_jni.resume_data_flags_t_or_(this.swigCPtr, this, getCPtr(resume_data_flags_t), resume_data_flags_t), true);
    }

    public resume_data_flags_t and_(resume_data_flags_t resume_data_flags_t) {
        return new resume_data_flags_t(libtorrent_jni.resume_data_flags_t_and_(this.swigCPtr, this, getCPtr(resume_data_flags_t), resume_data_flags_t), true);
    }

    public resume_data_flags_t xor(resume_data_flags_t resume_data_flags_t) {
        return new resume_data_flags_t(libtorrent_jni.resume_data_flags_t_xor(this.swigCPtr, this, getCPtr(resume_data_flags_t), resume_data_flags_t), true);
    }

    public resume_data_flags_t inv() {
        return new resume_data_flags_t(libtorrent_jni.resume_data_flags_t_inv(this.swigCPtr, this), true);
    }

    public int to_int() {
        return libtorrent_jni.resume_data_flags_t_to_int(this.swigCPtr, this);
    }

    public resume_data_flags_t() {
        this(libtorrent_jni.new_resume_data_flags_t(), true);
    }
}
