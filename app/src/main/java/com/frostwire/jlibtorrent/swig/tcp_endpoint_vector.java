package com.frostwire.jlibtorrent.swig;

public class tcp_endpoint_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected tcp_endpoint_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(tcp_endpoint_vector tcp_endpoint_vector) {
        return tcp_endpoint_vector == null ? 0 : tcp_endpoint_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_tcp_endpoint_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public tcp_endpoint_vector() {
        this(libtorrent_jni.new_tcp_endpoint_vector(), true);
    }

    public long size() {
        return libtorrent_jni.tcp_endpoint_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.tcp_endpoint_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.tcp_endpoint_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.tcp_endpoint_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.tcp_endpoint_vector_clear(this.swigCPtr, this);
    }

    public void push_back(tcp_endpoint tcp_endpoint) {
        libtorrent_jni.tcp_endpoint_vector_push_back(this.swigCPtr, this, tcp_endpoint.getCPtr(tcp_endpoint), tcp_endpoint);
    }

    public tcp_endpoint get(int i) {
        return new tcp_endpoint(libtorrent_jni.tcp_endpoint_vector_get(this.swigCPtr, this, i), (boolean) 0);
    }

    public void set(int i, tcp_endpoint tcp_endpoint) {
        libtorrent_jni.tcp_endpoint_vector_set(this.swigCPtr, this, i, tcp_endpoint.getCPtr(tcp_endpoint), tcp_endpoint);
    }
}
