package com.frostwire.jlibtorrent.swig;

public class dht_state {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected dht_state(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(dht_state dht_state) {
        return dht_state == null ? 0 : dht_state.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_dht_state(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void setNids(address_sha1_hash_pair_vector address_sha1_hash_pair_vector) {
        libtorrent_jni.dht_state_nids_set(this.swigCPtr, this, address_sha1_hash_pair_vector.getCPtr(address_sha1_hash_pair_vector), address_sha1_hash_pair_vector);
    }

    public address_sha1_hash_pair_vector getNids() {
        long dht_state_nids_get = libtorrent_jni.dht_state_nids_get(this.swigCPtr, this);
        if (dht_state_nids_get == 0) {
            return null;
        }
        return new address_sha1_hash_pair_vector(dht_state_nids_get, false);
    }

    public void setNodes(udp_endpoint_vector udp_endpoint_vector) {
        libtorrent_jni.dht_state_nodes_set(this.swigCPtr, this, udp_endpoint_vector.getCPtr(udp_endpoint_vector), udp_endpoint_vector);
    }

    public udp_endpoint_vector getNodes() {
        long dht_state_nodes_get = libtorrent_jni.dht_state_nodes_get(this.swigCPtr, this);
        if (dht_state_nodes_get == 0) {
            return null;
        }
        return new udp_endpoint_vector(dht_state_nodes_get, false);
    }

    public void setNodes6(udp_endpoint_vector udp_endpoint_vector) {
        libtorrent_jni.dht_state_nodes6_set(this.swigCPtr, this, udp_endpoint_vector.getCPtr(udp_endpoint_vector), udp_endpoint_vector);
    }

    public udp_endpoint_vector getNodes6() {
        long dht_state_nodes6_get = libtorrent_jni.dht_state_nodes6_get(this.swigCPtr, this);
        if (dht_state_nodes6_get == 0) {
            return null;
        }
        return new udp_endpoint_vector(dht_state_nodes6_get, false);
    }

    public void clear() {
        libtorrent_jni.dht_state_clear(this.swigCPtr, this);
    }

    public dht_state() {
        this(libtorrent_jni.new_dht_state(), true);
    }
}
