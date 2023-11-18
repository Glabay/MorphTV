package com.frostwire.jlibtorrent.swig;

public class sha1_hash_udp_endpoint_pair {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected sha1_hash_udp_endpoint_pair(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(sha1_hash_udp_endpoint_pair sha1_hash_udp_endpoint_pair) {
        return sha1_hash_udp_endpoint_pair == null ? 0 : sha1_hash_udp_endpoint_pair.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_sha1_hash_udp_endpoint_pair(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public sha1_hash_udp_endpoint_pair() {
        this(libtorrent_jni.new_sha1_hash_udp_endpoint_pair__SWIG_0(), true);
    }

    public sha1_hash_udp_endpoint_pair(sha1_hash sha1_hash, udp_endpoint udp_endpoint) {
        this(libtorrent_jni.new_sha1_hash_udp_endpoint_pair__SWIG_1(sha1_hash.getCPtr(sha1_hash), sha1_hash, udp_endpoint.getCPtr(udp_endpoint), udp_endpoint), true);
    }

    public sha1_hash_udp_endpoint_pair(sha1_hash_udp_endpoint_pair sha1_hash_udp_endpoint_pair) {
        this(libtorrent_jni.new_sha1_hash_udp_endpoint_pair__SWIG_2(getCPtr(sha1_hash_udp_endpoint_pair), sha1_hash_udp_endpoint_pair), true);
    }

    public void setFirst(sha1_hash sha1_hash) {
        libtorrent_jni.sha1_hash_udp_endpoint_pair_first_set(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public sha1_hash getFirst() {
        long sha1_hash_udp_endpoint_pair_first_get = libtorrent_jni.sha1_hash_udp_endpoint_pair_first_get(this.swigCPtr, this);
        if (sha1_hash_udp_endpoint_pair_first_get == 0) {
            return null;
        }
        return new sha1_hash(sha1_hash_udp_endpoint_pair_first_get, false);
    }

    public void setSecond(udp_endpoint udp_endpoint) {
        libtorrent_jni.sha1_hash_udp_endpoint_pair_second_set(this.swigCPtr, this, udp_endpoint.getCPtr(udp_endpoint), udp_endpoint);
    }

    public udp_endpoint getSecond() {
        long sha1_hash_udp_endpoint_pair_second_get = libtorrent_jni.sha1_hash_udp_endpoint_pair_second_get(this.swigCPtr, this);
        if (sha1_hash_udp_endpoint_pair_second_get == 0) {
            return null;
        }
        return new udp_endpoint(sha1_hash_udp_endpoint_pair_second_get, false);
    }
}
