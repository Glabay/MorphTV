package com.frostwire.jlibtorrent.swig;

public class torrent_delete_failed_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.torrent_delete_failed_alert_alert_type_get();
    public static final int priority = libtorrent_jni.torrent_delete_failed_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.torrent_delete_failed_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected torrent_delete_failed_alert(long j, boolean z) {
        super(libtorrent_jni.torrent_delete_failed_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(torrent_delete_failed_alert torrent_delete_failed_alert) {
        return torrent_delete_failed_alert == null ? 0 : torrent_delete_failed_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_torrent_delete_failed_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.torrent_delete_failed_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.torrent_delete_failed_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.torrent_delete_failed_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.torrent_delete_failed_alert_message(this.swigCPtr, this);
    }

    public error_code getError() {
        long torrent_delete_failed_alert_error_get = libtorrent_jni.torrent_delete_failed_alert_error_get(this.swigCPtr, this);
        if (torrent_delete_failed_alert_error_get == 0) {
            return null;
        }
        return new error_code(torrent_delete_failed_alert_error_get, false);
    }

    public void setInfo_hash(sha1_hash sha1_hash) {
        libtorrent_jni.torrent_delete_failed_alert_info_hash_set(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public sha1_hash getInfo_hash() {
        long torrent_delete_failed_alert_info_hash_get = libtorrent_jni.torrent_delete_failed_alert_info_hash_get(this.swigCPtr, this);
        if (torrent_delete_failed_alert_info_hash_get == 0) {
            return null;
        }
        return new sha1_hash(torrent_delete_failed_alert_info_hash_get, false);
    }
}
