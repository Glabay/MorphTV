package com.frostwire.jlibtorrent.swig;

public class torrent_flags_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected torrent_flags_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(torrent_flags_t torrent_flags_t) {
        return torrent_flags_t == null ? 0 : torrent_flags_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_torrent_flags_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static torrent_flags_t all() {
        return new torrent_flags_t(libtorrent_jni.torrent_flags_t_all(), true);
    }

    public boolean nonZero() {
        return libtorrent_jni.torrent_flags_t_nonZero(this.swigCPtr, this);
    }

    public boolean eq(torrent_flags_t torrent_flags_t) {
        return libtorrent_jni.torrent_flags_t_eq(this.swigCPtr, this, getCPtr(torrent_flags_t), torrent_flags_t);
    }

    public boolean ne(torrent_flags_t torrent_flags_t) {
        return libtorrent_jni.torrent_flags_t_ne(this.swigCPtr, this, getCPtr(torrent_flags_t), torrent_flags_t);
    }

    public torrent_flags_t or_(torrent_flags_t torrent_flags_t) {
        return new torrent_flags_t(libtorrent_jni.torrent_flags_t_or_(this.swigCPtr, this, getCPtr(torrent_flags_t), torrent_flags_t), true);
    }

    public torrent_flags_t and_(torrent_flags_t torrent_flags_t) {
        return new torrent_flags_t(libtorrent_jni.torrent_flags_t_and_(this.swigCPtr, this, getCPtr(torrent_flags_t), torrent_flags_t), true);
    }

    public torrent_flags_t xor(torrent_flags_t torrent_flags_t) {
        return new torrent_flags_t(libtorrent_jni.torrent_flags_t_xor(this.swigCPtr, this, getCPtr(torrent_flags_t), torrent_flags_t), true);
    }

    public torrent_flags_t inv() {
        return new torrent_flags_t(libtorrent_jni.torrent_flags_t_inv(this.swigCPtr, this), true);
    }

    public int to_int() {
        return libtorrent_jni.torrent_flags_t_to_int(this.swigCPtr, this);
    }

    public torrent_flags_t() {
        this(libtorrent_jni.new_torrent_flags_t(), true);
    }
}
