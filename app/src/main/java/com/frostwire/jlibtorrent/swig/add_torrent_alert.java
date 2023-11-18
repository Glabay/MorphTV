package com.frostwire.jlibtorrent.swig;

public class add_torrent_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.add_torrent_alert_alert_type_get();
    public static final int priority = libtorrent_jni.add_torrent_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.add_torrent_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected add_torrent_alert(long j, boolean z) {
        super(libtorrent_jni.add_torrent_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(add_torrent_alert add_torrent_alert) {
        return add_torrent_alert == null ? 0 : add_torrent_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_add_torrent_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.add_torrent_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.add_torrent_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.add_torrent_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.add_torrent_alert_message(this.swigCPtr, this);
    }

    public void setParams(add_torrent_params add_torrent_params) {
        libtorrent_jni.add_torrent_alert_params_set(this.swigCPtr, this, add_torrent_params.getCPtr(add_torrent_params), add_torrent_params);
    }

    public add_torrent_params getParams() {
        long add_torrent_alert_params_get = libtorrent_jni.add_torrent_alert_params_get(this.swigCPtr, this);
        if (add_torrent_alert_params_get == 0) {
            return null;
        }
        return new add_torrent_params(add_torrent_alert_params_get, false);
    }

    public void setError(error_code error_code) {
        libtorrent_jni.add_torrent_alert_error_set(this.swigCPtr, this, error_code.getCPtr(error_code), error_code);
    }

    public error_code getError() {
        long add_torrent_alert_error_get = libtorrent_jni.add_torrent_alert_error_get(this.swigCPtr, this);
        if (add_torrent_alert_error_get == 0) {
            return null;
        }
        return new error_code(add_torrent_alert_error_get, false);
    }
}
