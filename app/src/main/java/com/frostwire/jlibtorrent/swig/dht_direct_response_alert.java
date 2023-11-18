package com.frostwire.jlibtorrent.swig;

public class dht_direct_response_alert extends alert {
    public static final int alert_type = libtorrent_jni.dht_direct_response_alert_alert_type_get();
    public static final int priority = libtorrent_jni.dht_direct_response_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.dht_direct_response_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected dht_direct_response_alert(long j, boolean z) {
        super(libtorrent_jni.dht_direct_response_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(dht_direct_response_alert dht_direct_response_alert) {
        return dht_direct_response_alert == null ? 0 : dht_direct_response_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_dht_direct_response_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.dht_direct_response_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.dht_direct_response_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.dht_direct_response_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.dht_direct_response_alert_message(this.swigCPtr, this);
    }

    public bdecode_node response() {
        return new bdecode_node(libtorrent_jni.dht_direct_response_alert_response(this.swigCPtr, this), true);
    }

    public long get_userdata() {
        return libtorrent_jni.dht_direct_response_alert_get_userdata(this.swigCPtr, this);
    }

    public udp_endpoint get_endpoint() {
        return new udp_endpoint(libtorrent_jni.dht_direct_response_alert_get_endpoint(this.swigCPtr, this), true);
    }
}
