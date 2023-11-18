package com.frostwire.jlibtorrent.swig;

public class file_flags_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected file_flags_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(file_flags_t file_flags_t) {
        return file_flags_t == null ? 0 : file_flags_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_file_flags_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static file_flags_t all() {
        return new file_flags_t(libtorrent_jni.file_flags_t_all(), true);
    }

    public boolean nonZero() {
        return libtorrent_jni.file_flags_t_nonZero(this.swigCPtr, this);
    }

    public boolean eq(file_flags_t file_flags_t) {
        return libtorrent_jni.file_flags_t_eq(this.swigCPtr, this, getCPtr(file_flags_t), file_flags_t);
    }

    public boolean ne(file_flags_t file_flags_t) {
        return libtorrent_jni.file_flags_t_ne(this.swigCPtr, this, getCPtr(file_flags_t), file_flags_t);
    }

    public file_flags_t or_(file_flags_t file_flags_t) {
        return new file_flags_t(libtorrent_jni.file_flags_t_or_(this.swigCPtr, this, getCPtr(file_flags_t), file_flags_t), true);
    }

    public file_flags_t and_(file_flags_t file_flags_t) {
        return new file_flags_t(libtorrent_jni.file_flags_t_and_(this.swigCPtr, this, getCPtr(file_flags_t), file_flags_t), true);
    }

    public file_flags_t xor(file_flags_t file_flags_t) {
        return new file_flags_t(libtorrent_jni.file_flags_t_xor(this.swigCPtr, this, getCPtr(file_flags_t), file_flags_t), true);
    }

    public file_flags_t inv() {
        return new file_flags_t(libtorrent_jni.file_flags_t_inv(this.swigCPtr, this), true);
    }

    public int to_int() {
        return libtorrent_jni.file_flags_t_to_int(this.swigCPtr, this);
    }

    public file_flags_t() {
        this(libtorrent_jni.new_file_flags_t(), true);
    }
}
