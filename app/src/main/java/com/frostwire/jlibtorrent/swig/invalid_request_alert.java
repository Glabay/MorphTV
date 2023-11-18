package com.frostwire.jlibtorrent.swig;

public class invalid_request_alert extends peer_alert {
    public static final int alert_type = libtorrent_jni.invalid_request_alert_alert_type_get();
    public static final int priority = libtorrent_jni.invalid_request_alert_priority_get();
    private transient long swigCPtr;

    protected invalid_request_alert(long j, boolean z) {
        super(libtorrent_jni.invalid_request_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(invalid_request_alert invalid_request_alert) {
        return invalid_request_alert == null ? 0 : invalid_request_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_invalid_request_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.invalid_request_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.invalid_request_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.invalid_request_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.invalid_request_alert_message(this.swigCPtr, this);
    }

    public peer_request getRequest() {
        long invalid_request_alert_request_get = libtorrent_jni.invalid_request_alert_request_get(this.swigCPtr, this);
        if (invalid_request_alert_request_get == 0) {
            return null;
        }
        return new peer_request(invalid_request_alert_request_get, false);
    }

    public boolean getWe_have() {
        return libtorrent_jni.invalid_request_alert_we_have_get(this.swigCPtr, this);
    }

    public boolean getPeer_interested() {
        return libtorrent_jni.invalid_request_alert_peer_interested_get(this.swigCPtr, this);
    }

    public boolean getWithheld() {
        return libtorrent_jni.invalid_request_alert_withheld_get(this.swigCPtr, this);
    }
}
