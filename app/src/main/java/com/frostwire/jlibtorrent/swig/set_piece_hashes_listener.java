package com.frostwire.jlibtorrent.swig;

public class set_piece_hashes_listener {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected set_piece_hashes_listener(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(set_piece_hashes_listener set_piece_hashes_listener) {
        return set_piece_hashes_listener == null ? 0 : set_piece_hashes_listener.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_set_piece_hashes_listener(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    protected void swigDirectorDisconnect() {
        this.swigCMemOwn = false;
        delete();
    }

    public void swigReleaseOwnership() {
        this.swigCMemOwn = false;
        libtorrent_jni.set_piece_hashes_listener_change_ownership(this, this.swigCPtr, false);
    }

    public void swigTakeOwnership() {
        this.swigCMemOwn = true;
        libtorrent_jni.set_piece_hashes_listener_change_ownership(this, this.swigCPtr, true);
    }

    public void progress(int i) {
        if (getClass() == set_piece_hashes_listener.class) {
            libtorrent_jni.set_piece_hashes_listener_progress(this.swigCPtr, this, i);
        } else {
            libtorrent_jni.m3x4f33fdd0(this.swigCPtr, this, i);
        }
    }

    public set_piece_hashes_listener() {
        this(libtorrent_jni.new_set_piece_hashes_listener(), true);
        libtorrent_jni.set_piece_hashes_listener_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }
}
