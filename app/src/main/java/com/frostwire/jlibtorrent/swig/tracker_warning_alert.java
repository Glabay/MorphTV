package com.frostwire.jlibtorrent.swig;

public class tracker_warning_alert extends tracker_alert {
    public static final int alert_type = libtorrent_jni.tracker_warning_alert_alert_type_get();
    public static final int priority = libtorrent_jni.tracker_warning_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.tracker_warning_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected tracker_warning_alert(long j, boolean z) {
        super(libtorrent_jni.tracker_warning_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(tracker_warning_alert tracker_warning_alert) {
        return tracker_warning_alert == null ? 0 : tracker_warning_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_tracker_warning_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.tracker_warning_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.tracker_warning_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.tracker_warning_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.tracker_warning_alert_message(this.swigCPtr, this);
    }

    public String warning_message() {
        return libtorrent_jni.tracker_warning_alert_warning_message(this.swigCPtr, this);
    }
}
