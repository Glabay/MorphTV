package com.frostwire.jlibtorrent.swig;

public class dht_routing_bucket_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected dht_routing_bucket_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(dht_routing_bucket_vector dht_routing_bucket_vector) {
        return dht_routing_bucket_vector == null ? 0 : dht_routing_bucket_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_dht_routing_bucket_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public dht_routing_bucket_vector() {
        this(libtorrent_jni.new_dht_routing_bucket_vector(), true);
    }

    public long size() {
        return libtorrent_jni.dht_routing_bucket_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.dht_routing_bucket_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.dht_routing_bucket_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.dht_routing_bucket_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.dht_routing_bucket_vector_clear(this.swigCPtr, this);
    }

    public void push_back(dht_routing_bucket dht_routing_bucket) {
        libtorrent_jni.dht_routing_bucket_vector_push_back(this.swigCPtr, this, dht_routing_bucket.getCPtr(dht_routing_bucket), dht_routing_bucket);
    }

    public dht_routing_bucket get(int i) {
        return new dht_routing_bucket(libtorrent_jni.dht_routing_bucket_vector_get(this.swigCPtr, this, i), 0);
    }

    public void set(int i, dht_routing_bucket dht_routing_bucket) {
        libtorrent_jni.dht_routing_bucket_vector_set(this.swigCPtr, this, i, dht_routing_bucket.getCPtr(dht_routing_bucket), dht_routing_bucket);
    }
}
