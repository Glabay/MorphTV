package com.frostwire.jlibtorrent.swig;

public class dht_get_peers_reply_alert extends alert {
    public static final int alert_type = libtorrent_jni.dht_get_peers_reply_alert_alert_type_get();
    public static final int priority = libtorrent_jni.dht_get_peers_reply_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.dht_get_peers_reply_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected dht_get_peers_reply_alert(long j, boolean z) {
        super(libtorrent_jni.dht_get_peers_reply_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(dht_get_peers_reply_alert dht_get_peers_reply_alert) {
        return dht_get_peers_reply_alert == null ? 0 : dht_get_peers_reply_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_dht_get_peers_reply_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.dht_get_peers_reply_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.dht_get_peers_reply_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.dht_get_peers_reply_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.dht_get_peers_reply_alert_message(this.swigCPtr, this);
    }

    public void setInfo_hash(sha1_hash sha1_hash) {
        libtorrent_jni.dht_get_peers_reply_alert_info_hash_set(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public sha1_hash getInfo_hash() {
        long dht_get_peers_reply_alert_info_hash_get = libtorrent_jni.dht_get_peers_reply_alert_info_hash_get(this.swigCPtr, this);
        if (dht_get_peers_reply_alert_info_hash_get == 0) {
            return null;
        }
        return new sha1_hash(dht_get_peers_reply_alert_info_hash_get, false);
    }

    public int num_peers() {
        return libtorrent_jni.dht_get_peers_reply_alert_num_peers(this.swigCPtr, this);
    }

    public tcp_endpoint_vector peers() {
        return new tcp_endpoint_vector(libtorrent_jni.dht_get_peers_reply_alert_peers(this.swigCPtr, this), true);
    }
}
