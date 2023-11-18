package com.frostwire.jlibtorrent.swig;

import com.frostwire.jlibtorrent.swig.torrent_status.state_t;

public class state_changed_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.state_changed_alert_alert_type_get();
    public static final int priority = libtorrent_jni.state_changed_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.state_changed_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected state_changed_alert(long j, boolean z) {
        super(libtorrent_jni.state_changed_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(state_changed_alert state_changed_alert) {
        return state_changed_alert == null ? 0 : state_changed_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_state_changed_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.state_changed_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.state_changed_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.state_changed_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.state_changed_alert_message(this.swigCPtr, this);
    }

    public state_t getState() {
        return state_t.swigToEnum(libtorrent_jni.state_changed_alert_state_get(this.swigCPtr, this));
    }

    public state_t getPrev_state() {
        return state_t.swigToEnum(libtorrent_jni.state_changed_alert_prev_state_get(this.swigCPtr, this));
    }
}
