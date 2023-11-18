package com.frostwire.jlibtorrent.swig;

public class udp_error_alert extends alert {
    public static final int alert_type = libtorrent_jni.udp_error_alert_alert_type_get();
    public static final int priority = libtorrent_jni.udp_error_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.udp_error_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected udp_error_alert(long j, boolean z) {
        super(libtorrent_jni.udp_error_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(udp_error_alert udp_error_alert) {
        return udp_error_alert == null ? 0 : udp_error_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_udp_error_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.udp_error_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.udp_error_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.udp_error_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.udp_error_alert_message(this.swigCPtr, this);
    }

    public error_code getError() {
        long udp_error_alert_error_get = libtorrent_jni.udp_error_alert_error_get(this.swigCPtr, this);
        if (udp_error_alert_error_get == 0) {
            return null;
        }
        return new error_code(udp_error_alert_error_get, false);
    }

    public udp_endpoint get_endpoint() {
        return new udp_endpoint(libtorrent_jni.udp_error_alert_get_endpoint(this.swigCPtr, this), true);
    }
}
