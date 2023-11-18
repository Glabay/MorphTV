package com.frostwire.jlibtorrent.swig;

public class listen_succeeded_alert extends alert {
    public static final int alert_type = libtorrent_jni.listen_succeeded_alert_alert_type_get();
    public static final int priority = libtorrent_jni.listen_succeeded_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.listen_succeeded_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected listen_succeeded_alert(long j, boolean z) {
        super(libtorrent_jni.listen_succeeded_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(listen_succeeded_alert listen_succeeded_alert) {
        return listen_succeeded_alert == null ? 0 : listen_succeeded_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_listen_succeeded_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.listen_succeeded_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.listen_succeeded_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.listen_succeeded_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.listen_succeeded_alert_message(this.swigCPtr, this);
    }

    public int getPort() {
        return libtorrent_jni.listen_succeeded_alert_port_get(this.swigCPtr, this);
    }

    public socket_type_t getSocket_type() {
        return socket_type_t.swigToEnum(libtorrent_jni.listen_succeeded_alert_socket_type_get(this.swigCPtr, this));
    }

    public address get_address() {
        return new address(libtorrent_jni.listen_succeeded_alert_get_address(this.swigCPtr, this), true);
    }
}
