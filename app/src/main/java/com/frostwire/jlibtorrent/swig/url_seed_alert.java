package com.frostwire.jlibtorrent.swig;

public class url_seed_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.url_seed_alert_alert_type_get();
    public static final int priority = libtorrent_jni.url_seed_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.url_seed_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected url_seed_alert(long j, boolean z) {
        super(libtorrent_jni.url_seed_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(url_seed_alert url_seed_alert) {
        return url_seed_alert == null ? 0 : url_seed_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_url_seed_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.url_seed_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.url_seed_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.url_seed_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.url_seed_alert_message(this.swigCPtr, this);
    }

    public error_code getError() {
        long url_seed_alert_error_get = libtorrent_jni.url_seed_alert_error_get(this.swigCPtr, this);
        if (url_seed_alert_error_get == 0) {
            return null;
        }
        return new error_code(url_seed_alert_error_get, false);
    }

    public String server_url() {
        return libtorrent_jni.url_seed_alert_server_url(this.swigCPtr, this);
    }

    public String error_message() {
        return libtorrent_jni.url_seed_alert_error_message(this.swigCPtr, this);
    }
}
