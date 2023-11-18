package com.frostwire.jlibtorrent.swig;

public class external_ip_alert extends alert {
    public static final int alert_type = libtorrent_jni.external_ip_alert_alert_type_get();
    public static final int priority = libtorrent_jni.external_ip_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.external_ip_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected external_ip_alert(long j, boolean z) {
        super(libtorrent_jni.external_ip_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(external_ip_alert external_ip_alert) {
        return external_ip_alert == null ? 0 : external_ip_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_external_ip_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.external_ip_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.external_ip_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.external_ip_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.external_ip_alert_message(this.swigCPtr, this);
    }

    public address get_external_address() {
        return new address(libtorrent_jni.external_ip_alert_get_external_address(this.swigCPtr, this), true);
    }
}
