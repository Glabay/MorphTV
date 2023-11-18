package com.frostwire.jlibtorrent.swig;

public class dht_sample_infohashes_alert extends alert {
    public static final int alert_type = libtorrent_jni.dht_sample_infohashes_alert_alert_type_get();
    public static final int priority = libtorrent_jni.dht_sample_infohashes_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.dht_sample_infohashes_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected dht_sample_infohashes_alert(long j, boolean z) {
        super(libtorrent_jni.dht_sample_infohashes_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(dht_sample_infohashes_alert dht_sample_infohashes_alert) {
        return dht_sample_infohashes_alert == null ? 0 : dht_sample_infohashes_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_dht_sample_infohashes_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.dht_sample_infohashes_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.dht_sample_infohashes_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.dht_sample_infohashes_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.dht_sample_infohashes_alert_message(this.swigCPtr, this);
    }

    public int getNum_infohashes() {
        return libtorrent_jni.dht_sample_infohashes_alert_num_infohashes_get(this.swigCPtr, this);
    }

    public int num_samples() {
        return libtorrent_jni.dht_sample_infohashes_alert_num_samples(this.swigCPtr, this);
    }

    public sha1_hash_vector samples() {
        return new sha1_hash_vector(libtorrent_jni.dht_sample_infohashes_alert_samples(this.swigCPtr, this), true);
    }

    public int num_nodes() {
        return libtorrent_jni.dht_sample_infohashes_alert_num_nodes(this.swigCPtr, this);
    }

    public sha1_hash_udp_endpoint_pair_vector nodes() {
        return new sha1_hash_udp_endpoint_pair_vector(libtorrent_jni.dht_sample_infohashes_alert_nodes(this.swigCPtr, this), true);
    }

    public udp_endpoint get_endpoint() {
        return new udp_endpoint(libtorrent_jni.dht_sample_infohashes_alert_get_endpoint(this.swigCPtr, this), true);
    }

    public long get_interval() {
        return libtorrent_jni.dht_sample_infohashes_alert_get_interval(this.swigCPtr, this);
    }
}
