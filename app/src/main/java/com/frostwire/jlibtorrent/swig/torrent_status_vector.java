package com.frostwire.jlibtorrent.swig;

public class torrent_status_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected torrent_status_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(torrent_status_vector torrent_status_vector) {
        return torrent_status_vector == null ? 0 : torrent_status_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_torrent_status_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public torrent_status_vector() {
        this(libtorrent_jni.new_torrent_status_vector(), true);
    }

    public long size() {
        return libtorrent_jni.torrent_status_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.torrent_status_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.torrent_status_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.torrent_status_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.torrent_status_vector_clear(this.swigCPtr, this);
    }

    public void push_back(torrent_status torrent_status) {
        libtorrent_jni.torrent_status_vector_push_back(this.swigCPtr, this, torrent_status.getCPtr(torrent_status), torrent_status);
    }

    public torrent_status get(int i) {
        return new torrent_status(libtorrent_jni.torrent_status_vector_get(this.swigCPtr, this, i), 0);
    }

    public void set(int i, torrent_status torrent_status) {
        libtorrent_jni.torrent_status_vector_set(this.swigCPtr, this, i, torrent_status.getCPtr(torrent_status), torrent_status);
    }
}
