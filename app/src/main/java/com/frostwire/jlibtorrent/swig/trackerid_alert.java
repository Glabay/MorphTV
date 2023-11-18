package com.frostwire.jlibtorrent.swig;

public class trackerid_alert extends tracker_alert {
    public static final int alert_type = libtorrent_jni.trackerid_alert_alert_type_get();
    public static final int priority = libtorrent_jni.trackerid_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.trackerid_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected trackerid_alert(long j, boolean z) {
        super(libtorrent_jni.trackerid_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(trackerid_alert trackerid_alert) {
        return trackerid_alert == null ? 0 : trackerid_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_trackerid_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.trackerid_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.trackerid_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.trackerid_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.trackerid_alert_message(this.swigCPtr, this);
    }

    public String tracker_id() {
        return libtorrent_jni.trackerid_alert_tracker_id(this.swigCPtr, this);
    }
}
