package com.frostwire.jlibtorrent.swig;

public class torrent_checked_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.torrent_checked_alert_alert_type_get();
    public static final int priority = libtorrent_jni.torrent_checked_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.torrent_checked_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected torrent_checked_alert(long j, boolean z) {
        super(libtorrent_jni.torrent_checked_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(torrent_checked_alert torrent_checked_alert) {
        return torrent_checked_alert == null ? 0 : torrent_checked_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_torrent_checked_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.torrent_checked_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.torrent_checked_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.torrent_checked_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.torrent_checked_alert_message(this.swigCPtr, this);
    }
}
