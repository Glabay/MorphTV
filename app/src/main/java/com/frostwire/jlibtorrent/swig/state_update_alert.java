package com.frostwire.jlibtorrent.swig;

public class state_update_alert extends alert {
    public static final int alert_type = libtorrent_jni.state_update_alert_alert_type_get();
    public static final int priority = libtorrent_jni.state_update_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.state_update_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected state_update_alert(long j, boolean z) {
        super(libtorrent_jni.state_update_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(state_update_alert state_update_alert) {
        return state_update_alert == null ? 0 : state_update_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_state_update_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.state_update_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.state_update_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.state_update_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.state_update_alert_message(this.swigCPtr, this);
    }

    public void setStatus(torrent_status_vector torrent_status_vector) {
        libtorrent_jni.state_update_alert_status_set(this.swigCPtr, this, torrent_status_vector.getCPtr(torrent_status_vector), torrent_status_vector);
    }

    public torrent_status_vector getStatus() {
        long state_update_alert_status_get = libtorrent_jni.state_update_alert_status_get(this.swigCPtr, this);
        if (state_update_alert_status_get == 0) {
            return null;
        }
        return new torrent_status_vector(state_update_alert_status_get, false);
    }
}
