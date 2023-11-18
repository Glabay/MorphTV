package com.frostwire.jlibtorrent.swig;

public class peer_flags_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected peer_flags_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(peer_flags_t peer_flags_t) {
        return peer_flags_t == null ? 0 : peer_flags_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_peer_flags_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static peer_flags_t all() {
        return new peer_flags_t(libtorrent_jni.peer_flags_t_all(), true);
    }

    public boolean nonZero() {
        return libtorrent_jni.peer_flags_t_nonZero(this.swigCPtr, this);
    }

    public boolean eq(peer_flags_t peer_flags_t) {
        return libtorrent_jni.peer_flags_t_eq(this.swigCPtr, this, getCPtr(peer_flags_t), peer_flags_t);
    }

    public boolean ne(peer_flags_t peer_flags_t) {
        return libtorrent_jni.peer_flags_t_ne(this.swigCPtr, this, getCPtr(peer_flags_t), peer_flags_t);
    }

    public peer_flags_t or_(peer_flags_t peer_flags_t) {
        return new peer_flags_t(libtorrent_jni.peer_flags_t_or_(this.swigCPtr, this, getCPtr(peer_flags_t), peer_flags_t), true);
    }

    public peer_flags_t and_(peer_flags_t peer_flags_t) {
        return new peer_flags_t(libtorrent_jni.peer_flags_t_and_(this.swigCPtr, this, getCPtr(peer_flags_t), peer_flags_t), true);
    }

    public peer_flags_t xor(peer_flags_t peer_flags_t) {
        return new peer_flags_t(libtorrent_jni.peer_flags_t_xor(this.swigCPtr, this, getCPtr(peer_flags_t), peer_flags_t), true);
    }

    public peer_flags_t inv() {
        return new peer_flags_t(libtorrent_jni.peer_flags_t_inv(this.swigCPtr, this), true);
    }

    public int to_int() {
        return libtorrent_jni.peer_flags_t_to_int(this.swigCPtr, this);
    }

    public peer_flags_t() {
        this(libtorrent_jni.new_peer_flags_t(), true);
    }
}
