package com.frostwire.jlibtorrent.swig;

public class udp_endpoint_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected udp_endpoint_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(udp_endpoint_vector udp_endpoint_vector) {
        return udp_endpoint_vector == null ? 0 : udp_endpoint_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_udp_endpoint_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public udp_endpoint_vector() {
        this(libtorrent_jni.new_udp_endpoint_vector(), true);
    }

    public long size() {
        return libtorrent_jni.udp_endpoint_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.udp_endpoint_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.udp_endpoint_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.udp_endpoint_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.udp_endpoint_vector_clear(this.swigCPtr, this);
    }

    public void push_back(udp_endpoint udp_endpoint) {
        libtorrent_jni.udp_endpoint_vector_push_back(this.swigCPtr, this, udp_endpoint.getCPtr(udp_endpoint), udp_endpoint);
    }

    public udp_endpoint get(int i) {
        return new udp_endpoint(libtorrent_jni.udp_endpoint_vector_get(this.swigCPtr, this, i), (boolean) 0);
    }

    public void set(int i, udp_endpoint udp_endpoint) {
        libtorrent_jni.udp_endpoint_vector_set(this.swigCPtr, this, i, udp_endpoint.getCPtr(udp_endpoint), udp_endpoint);
    }
}
