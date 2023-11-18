package com.frostwire.jlibtorrent.swig;

public class metadata_failed_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.metadata_failed_alert_alert_type_get();
    public static final int priority = libtorrent_jni.metadata_failed_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.metadata_failed_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected metadata_failed_alert(long j, boolean z) {
        super(libtorrent_jni.metadata_failed_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(metadata_failed_alert metadata_failed_alert) {
        return metadata_failed_alert == null ? 0 : metadata_failed_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_metadata_failed_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.metadata_failed_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.metadata_failed_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.metadata_failed_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.metadata_failed_alert_message(this.swigCPtr, this);
    }

    public error_code getError() {
        long metadata_failed_alert_error_get = libtorrent_jni.metadata_failed_alert_error_get(this.swigCPtr, this);
        if (metadata_failed_alert_error_get == 0) {
            return null;
        }
        return new error_code(metadata_failed_alert_error_get, false);
    }
}
