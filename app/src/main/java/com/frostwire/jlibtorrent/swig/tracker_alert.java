package com.frostwire.jlibtorrent.swig;

public class tracker_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.tracker_alert_alert_type_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.tracker_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected tracker_alert(long j, boolean z) {
        super(libtorrent_jni.tracker_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(tracker_alert tracker_alert) {
        return tracker_alert == null ? 0 : tracker_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_tracker_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.tracker_alert_category(this.swigCPtr, this), true);
    }

    public String message() {
        return libtorrent_jni.tracker_alert_message(this.swigCPtr, this);
    }

    public String tracker_url() {
        return libtorrent_jni.tracker_alert_tracker_url(this.swigCPtr, this);
    }
}
