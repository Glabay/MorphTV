package com.frostwire.jlibtorrent.swig;

public class incoming_request_alert extends peer_alert {
    public static final int alert_type = libtorrent_jni.incoming_request_alert_alert_type_get();
    public static final int priority = libtorrent_jni.incoming_request_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.incoming_request_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected incoming_request_alert(long j, boolean z) {
        super(libtorrent_jni.incoming_request_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(incoming_request_alert incoming_request_alert) {
        return incoming_request_alert == null ? 0 : incoming_request_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_incoming_request_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.incoming_request_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.incoming_request_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.incoming_request_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.incoming_request_alert_message(this.swigCPtr, this);
    }

    public void setReq(peer_request peer_request) {
        libtorrent_jni.incoming_request_alert_req_set(this.swigCPtr, this, peer_request.getCPtr(peer_request), peer_request);
    }

    public peer_request getReq() {
        long incoming_request_alert_req_get = libtorrent_jni.incoming_request_alert_req_get(this.swigCPtr, this);
        if (incoming_request_alert_req_get == 0) {
            return null;
        }
        return new peer_request(incoming_request_alert_req_get, false);
    }
}
