package com.frostwire.jlibtorrent.swig;

public class peer_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.peer_alert_alert_type_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.peer_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected peer_alert(long j, boolean z) {
        super(libtorrent_jni.peer_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(peer_alert peer_alert) {
        return peer_alert == null ? 0 : peer_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_peer_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.peer_alert_category(this.swigCPtr, this), true);
    }

    public String message() {
        return libtorrent_jni.peer_alert_message(this.swigCPtr, this);
    }

    public void setPid(sha1_hash sha1_hash) {
        libtorrent_jni.peer_alert_pid_set(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public sha1_hash getPid() {
        long peer_alert_pid_get = libtorrent_jni.peer_alert_pid_get(this.swigCPtr, this);
        if (peer_alert_pid_get == 0) {
            return null;
        }
        return new sha1_hash(peer_alert_pid_get, false);
    }

    public tcp_endpoint get_endpoint() {
        return new tcp_endpoint(libtorrent_jni.peer_alert_get_endpoint(this.swigCPtr, this), true);
    }
}
