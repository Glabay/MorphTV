package com.frostwire.jlibtorrent.swig;

public class add_files_listener {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected add_files_listener(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(add_files_listener add_files_listener) {
        return add_files_listener == null ? 0 : add_files_listener.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_add_files_listener(this.swigCPtr);
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
        libtorrent_jni.add_files_listener_change_ownership(this, this.swigCPtr, false);
    }

    public void swigTakeOwnership() {
        this.swigCMemOwn = true;
        libtorrent_jni.add_files_listener_change_ownership(this, this.swigCPtr, true);
    }

    public boolean pred(String str) {
        return getClass() == add_files_listener.class ? libtorrent_jni.add_files_listener_pred(this.swigCPtr, this, str) : libtorrent_jni.add_files_listener_predSwigExplicitadd_files_listener(this.swigCPtr, this, str);
    }

    public add_files_listener() {
        this(libtorrent_jni.new_add_files_listener(), true);
        libtorrent_jni.add_files_listener_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }
}
