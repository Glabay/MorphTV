package com.frostwire.jlibtorrent.swig;

public class dht_bootstrap_alert extends alert {
    public static final int alert_type = libtorrent_jni.dht_bootstrap_alert_alert_type_get();
    public static final int priority = libtorrent_jni.dht_bootstrap_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.dht_bootstrap_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected dht_bootstrap_alert(long j, boolean z) {
        super(libtorrent_jni.dht_bootstrap_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(dht_bootstrap_alert dht_bootstrap_alert) {
        return dht_bootstrap_alert == null ? 0 : dht_bootstrap_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_dht_bootstrap_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.dht_bootstrap_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.dht_bootstrap_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.dht_bootstrap_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.dht_bootstrap_alert_message(this.swigCPtr, this);
    }
}
