package com.frostwire.jlibtorrent.swig;

public class dht_live_nodes_alert extends alert {
    public static final int alert_type = libtorrent_jni.dht_live_nodes_alert_alert_type_get();
    public static final int priority = libtorrent_jni.dht_live_nodes_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.dht_live_nodes_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected dht_live_nodes_alert(long j, boolean z) {
        super(libtorrent_jni.dht_live_nodes_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(dht_live_nodes_alert dht_live_nodes_alert) {
        return dht_live_nodes_alert == null ? 0 : dht_live_nodes_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_dht_live_nodes_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.dht_live_nodes_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.dht_live_nodes_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.dht_live_nodes_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.dht_live_nodes_alert_message(this.swigCPtr, this);
    }

    public void setNode_id(sha1_hash sha1_hash) {
        libtorrent_jni.dht_live_nodes_alert_node_id_set(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public sha1_hash getNode_id() {
        long dht_live_nodes_alert_node_id_get = libtorrent_jni.dht_live_nodes_alert_node_id_get(this.swigCPtr, this);
        if (dht_live_nodes_alert_node_id_get == 0) {
            return null;
        }
        return new sha1_hash(dht_live_nodes_alert_node_id_get, false);
    }

    public int num_nodes() {
        return libtorrent_jni.dht_live_nodes_alert_num_nodes(this.swigCPtr, this);
    }

    public sha1_hash_udp_endpoint_pair_vector nodes() {
        return new sha1_hash_udp_endpoint_pair_vector(libtorrent_jni.dht_live_nodes_alert_nodes(this.swigCPtr, this), true);
    }
}
