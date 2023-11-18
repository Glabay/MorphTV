package com.frostwire.jlibtorrent.swig;

public class alert_ptr_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected alert_ptr_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(alert_ptr_vector alert_ptr_vector) {
        return alert_ptr_vector == null ? 0 : alert_ptr_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_alert_ptr_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public alert_ptr_vector() {
        this(libtorrent_jni.new_alert_ptr_vector(), true);
    }

    public long size() {
        return libtorrent_jni.alert_ptr_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.alert_ptr_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.alert_ptr_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.alert_ptr_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.alert_ptr_vector_clear(this.swigCPtr, this);
    }

    public void push_back(alert alert) {
        libtorrent_jni.alert_ptr_vector_push_back(this.swigCPtr, this, alert.getCPtr(alert), alert);
    }

    public alert get(int i) {
        long alert_ptr_vector_get = libtorrent_jni.alert_ptr_vector_get(this.swigCPtr, this, i);
        if (alert_ptr_vector_get == 0) {
            return 0;
        }
        return new alert(alert_ptr_vector_get, false);
    }

    public void set(int i, alert alert) {
        libtorrent_jni.alert_ptr_vector_set(this.swigCPtr, this, i, alert.getCPtr(alert), alert);
    }
}
