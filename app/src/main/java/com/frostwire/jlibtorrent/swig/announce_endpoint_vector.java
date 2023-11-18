package com.frostwire.jlibtorrent.swig;

public class announce_endpoint_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected announce_endpoint_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(announce_endpoint_vector announce_endpoint_vector) {
        return announce_endpoint_vector == null ? 0 : announce_endpoint_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_announce_endpoint_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public announce_endpoint_vector() {
        this(libtorrent_jni.new_announce_endpoint_vector(), true);
    }

    public long size() {
        return libtorrent_jni.announce_endpoint_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.announce_endpoint_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.announce_endpoint_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.announce_endpoint_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.announce_endpoint_vector_clear(this.swigCPtr, this);
    }

    public void push_back(announce_endpoint announce_endpoint) {
        libtorrent_jni.announce_endpoint_vector_push_back(this.swigCPtr, this, announce_endpoint.getCPtr(announce_endpoint), announce_endpoint);
    }

    public announce_endpoint get(int i) {
        return new announce_endpoint(libtorrent_jni.announce_endpoint_vector_get(this.swigCPtr, this, i), 0);
    }

    public void set(int i, announce_endpoint announce_endpoint) {
        libtorrent_jni.announce_endpoint_vector_set(this.swigCPtr, this, i, announce_endpoint.getCPtr(announce_endpoint), announce_endpoint);
    }
}
