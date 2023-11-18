package com.frostwire.jlibtorrent.swig;

public class peer_ban_alert extends peer_alert {
    public static final int alert_type = libtorrent_jni.peer_ban_alert_alert_type_get();
    public static final int priority = libtorrent_jni.peer_ban_alert_priority_get();
    private transient long swigCPtr;

    protected peer_ban_alert(long j, boolean z) {
        super(libtorrent_jni.peer_ban_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(peer_ban_alert peer_ban_alert) {
        return peer_ban_alert == null ? 0 : peer_ban_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_peer_ban_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.peer_ban_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.peer_ban_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.peer_ban_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.peer_ban_alert_message(this.swigCPtr, this);
    }
}
