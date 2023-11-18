package com.frostwire.jlibtorrent.swig;

public class dht_lookup {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected dht_lookup(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(dht_lookup dht_lookup) {
        return dht_lookup == null ? 0 : dht_lookup.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_dht_lookup(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void setOutstanding_requests(int i) {
        libtorrent_jni.dht_lookup_outstanding_requests_set(this.swigCPtr, this, i);
    }

    public int getOutstanding_requests() {
        return libtorrent_jni.dht_lookup_outstanding_requests_get(this.swigCPtr, this);
    }

    public void setTimeouts(int i) {
        libtorrent_jni.dht_lookup_timeouts_set(this.swigCPtr, this, i);
    }

    public int getTimeouts() {
        return libtorrent_jni.dht_lookup_timeouts_get(this.swigCPtr, this);
    }

    public void setResponses(int i) {
        libtorrent_jni.dht_lookup_responses_set(this.swigCPtr, this, i);
    }

    public int getResponses() {
        return libtorrent_jni.dht_lookup_responses_get(this.swigCPtr, this);
    }

    public void setBranch_factor(int i) {
        libtorrent_jni.dht_lookup_branch_factor_set(this.swigCPtr, this, i);
    }

    public int getBranch_factor() {
        return libtorrent_jni.dht_lookup_branch_factor_get(this.swigCPtr, this);
    }

    public void setNodes_left(int i) {
        libtorrent_jni.dht_lookup_nodes_left_set(this.swigCPtr, this, i);
    }

    public int getNodes_left() {
        return libtorrent_jni.dht_lookup_nodes_left_get(this.swigCPtr, this);
    }

    public void setLast_sent(int i) {
        libtorrent_jni.dht_lookup_last_sent_set(this.swigCPtr, this, i);
    }

    public int getLast_sent() {
        return libtorrent_jni.dht_lookup_last_sent_get(this.swigCPtr, this);
    }

    public void setFirst_timeout(int i) {
        libtorrent_jni.dht_lookup_first_timeout_set(this.swigCPtr, this, i);
    }

    public int getFirst_timeout() {
        return libtorrent_jni.dht_lookup_first_timeout_get(this.swigCPtr, this);
    }

    public void setTarget(sha1_hash sha1_hash) {
        libtorrent_jni.dht_lookup_target_set(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public sha1_hash getTarget() {
        long dht_lookup_target_get = libtorrent_jni.dht_lookup_target_get(this.swigCPtr, this);
        if (dht_lookup_target_get == 0) {
            return null;
        }
        return new sha1_hash(dht_lookup_target_get, false);
    }

    public String get_type() {
        return libtorrent_jni.dht_lookup_get_type(this.swigCPtr, this);
    }

    public dht_lookup() {
        this(libtorrent_jni.new_dht_lookup(), true);
    }
}
