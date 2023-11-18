package com.frostwire.jlibtorrent.swig;

public class dht_routing_bucket {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected dht_routing_bucket(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(dht_routing_bucket dht_routing_bucket) {
        return dht_routing_bucket == null ? 0 : dht_routing_bucket.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_dht_routing_bucket(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void setNum_nodes(int i) {
        libtorrent_jni.dht_routing_bucket_num_nodes_set(this.swigCPtr, this, i);
    }

    public int getNum_nodes() {
        return libtorrent_jni.dht_routing_bucket_num_nodes_get(this.swigCPtr, this);
    }

    public void setNum_replacements(int i) {
        libtorrent_jni.dht_routing_bucket_num_replacements_set(this.swigCPtr, this, i);
    }

    public int getNum_replacements() {
        return libtorrent_jni.dht_routing_bucket_num_replacements_get(this.swigCPtr, this);
    }

    public void setLast_active(int i) {
        libtorrent_jni.dht_routing_bucket_last_active_set(this.swigCPtr, this, i);
    }

    public int getLast_active() {
        return libtorrent_jni.dht_routing_bucket_last_active_get(this.swigCPtr, this);
    }

    public dht_routing_bucket() {
        this(libtorrent_jni.new_dht_routing_bucket(), true);
    }
}
