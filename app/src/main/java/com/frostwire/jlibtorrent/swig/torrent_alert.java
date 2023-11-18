package com.frostwire.jlibtorrent.swig;

public class torrent_alert extends alert {
    public static final int alert_type = libtorrent_jni.torrent_alert_alert_type_get();
    private transient long swigCPtr;

    protected torrent_alert(long j, boolean z) {
        super(libtorrent_jni.torrent_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(torrent_alert torrent_alert) {
        return torrent_alert == null ? 0 : torrent_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_torrent_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public String message() {
        return libtorrent_jni.torrent_alert_message(this.swigCPtr, this);
    }

    public void setHandle(torrent_handle torrent_handle) {
        libtorrent_jni.torrent_alert_handle_set(this.swigCPtr, this, torrent_handle.getCPtr(torrent_handle), torrent_handle);
    }

    public torrent_handle getHandle() {
        long torrent_alert_handle_get = libtorrent_jni.torrent_alert_handle_get(this.swigCPtr, this);
        if (torrent_alert_handle_get == 0) {
            return null;
        }
        return new torrent_handle(torrent_alert_handle_get, false);
    }

    public String torrent_name() {
        return libtorrent_jni.torrent_alert_torrent_name(this.swigCPtr, this);
    }
}
