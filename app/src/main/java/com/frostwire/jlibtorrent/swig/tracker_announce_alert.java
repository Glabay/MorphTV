package com.frostwire.jlibtorrent.swig;

public class tracker_announce_alert extends tracker_alert {
    public static final int alert_type = libtorrent_jni.tracker_announce_alert_alert_type_get();
    public static final int priority = libtorrent_jni.tracker_announce_alert_priority_get();
    private transient long swigCPtr;

    protected tracker_announce_alert(long j, boolean z) {
        super(libtorrent_jni.tracker_announce_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(tracker_announce_alert tracker_announce_alert) {
        return tracker_announce_alert == null ? 0 : tracker_announce_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_tracker_announce_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.tracker_announce_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.tracker_announce_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.tracker_announce_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.tracker_announce_alert_message(this.swigCPtr, this);
    }

    public int getEvent() {
        return libtorrent_jni.tracker_announce_alert_event_get(this.swigCPtr, this);
    }
}
