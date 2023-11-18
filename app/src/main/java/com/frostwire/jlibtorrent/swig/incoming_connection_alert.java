package com.frostwire.jlibtorrent.swig;

public class incoming_connection_alert extends alert {
    public static final int alert_type = libtorrent_jni.incoming_connection_alert_alert_type_get();
    public static final int priority = libtorrent_jni.incoming_connection_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.incoming_connection_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected incoming_connection_alert(long j, boolean z) {
        super(libtorrent_jni.incoming_connection_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(incoming_connection_alert incoming_connection_alert) {
        return incoming_connection_alert == null ? 0 : incoming_connection_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_incoming_connection_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.incoming_connection_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.incoming_connection_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.incoming_connection_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.incoming_connection_alert_message(this.swigCPtr, this);
    }

    public int getSocket_type() {
        return libtorrent_jni.incoming_connection_alert_socket_type_get(this.swigCPtr, this);
    }

    public tcp_endpoint get_endpoint() {
        return new tcp_endpoint(libtorrent_jni.incoming_connection_alert_get_endpoint(this.swigCPtr, this), true);
    }
}
