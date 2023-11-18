package com.frostwire.jlibtorrent.swig;

public class alert_notify_callback {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected alert_notify_callback(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(alert_notify_callback alert_notify_callback) {
        return alert_notify_callback == null ? 0 : alert_notify_callback.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_alert_notify_callback(this.swigCPtr);
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
        libtorrent_jni.alert_notify_callback_change_ownership(this, this.swigCPtr, false);
    }

    public void swigTakeOwnership() {
        this.swigCMemOwn = true;
        libtorrent_jni.alert_notify_callback_change_ownership(this, this.swigCPtr, true);
    }

    public void on_alert() {
        if (getClass() == alert_notify_callback.class) {
            libtorrent_jni.alert_notify_callback_on_alert(this.swigCPtr, this);
        } else {
            libtorrent_jni.alert_notify_callback_on_alertSwigExplicitalert_notify_callback(this.swigCPtr, this);
        }
    }

    public alert_notify_callback() {
        this(libtorrent_jni.new_alert_notify_callback(), true);
        libtorrent_jni.alert_notify_callback_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }
}
