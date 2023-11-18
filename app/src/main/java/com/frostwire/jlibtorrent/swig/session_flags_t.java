package com.frostwire.jlibtorrent.swig;

public class session_flags_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected session_flags_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(session_flags_t session_flags_t) {
        return session_flags_t == null ? 0 : session_flags_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_session_flags_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static session_flags_t all() {
        return new session_flags_t(libtorrent_jni.session_flags_t_all(), true);
    }

    public boolean nonZero() {
        return libtorrent_jni.session_flags_t_nonZero(this.swigCPtr, this);
    }

    public boolean eq(session_flags_t session_flags_t) {
        return libtorrent_jni.session_flags_t_eq(this.swigCPtr, this, getCPtr(session_flags_t), session_flags_t);
    }

    public boolean ne(session_flags_t session_flags_t) {
        return libtorrent_jni.session_flags_t_ne(this.swigCPtr, this, getCPtr(session_flags_t), session_flags_t);
    }

    public session_flags_t or_(session_flags_t session_flags_t) {
        return new session_flags_t(libtorrent_jni.session_flags_t_or_(this.swigCPtr, this, getCPtr(session_flags_t), session_flags_t), true);
    }

    public session_flags_t and_(session_flags_t session_flags_t) {
        return new session_flags_t(libtorrent_jni.session_flags_t_and_(this.swigCPtr, this, getCPtr(session_flags_t), session_flags_t), true);
    }

    public session_flags_t xor(session_flags_t session_flags_t) {
        return new session_flags_t(libtorrent_jni.session_flags_t_xor(this.swigCPtr, this, getCPtr(session_flags_t), session_flags_t), true);
    }

    public session_flags_t inv() {
        return new session_flags_t(libtorrent_jni.session_flags_t_inv(this.swigCPtr, this), true);
    }

    public int to_int() {
        return libtorrent_jni.session_flags_t_to_int(this.swigCPtr, this);
    }

    public session_flags_t() {
        this(libtorrent_jni.new_session_flags_t(), true);
    }
}
