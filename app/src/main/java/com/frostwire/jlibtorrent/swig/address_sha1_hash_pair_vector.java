package com.frostwire.jlibtorrent.swig;

public class address_sha1_hash_pair_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected address_sha1_hash_pair_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(address_sha1_hash_pair_vector address_sha1_hash_pair_vector) {
        return address_sha1_hash_pair_vector == null ? 0 : address_sha1_hash_pair_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_address_sha1_hash_pair_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public address_sha1_hash_pair_vector() {
        this(libtorrent_jni.new_address_sha1_hash_pair_vector(), true);
    }

    public long size() {
        return libtorrent_jni.address_sha1_hash_pair_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.address_sha1_hash_pair_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.address_sha1_hash_pair_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.address_sha1_hash_pair_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.address_sha1_hash_pair_vector_clear(this.swigCPtr, this);
    }

    public void push_back(address_sha1_hash_pair address_sha1_hash_pair) {
        libtorrent_jni.address_sha1_hash_pair_vector_push_back(this.swigCPtr, this, address_sha1_hash_pair.getCPtr(address_sha1_hash_pair), address_sha1_hash_pair);
    }

    public address_sha1_hash_pair get(int i) {
        return new address_sha1_hash_pair(libtorrent_jni.address_sha1_hash_pair_vector_get(this.swigCPtr, this, i), (boolean) 0);
    }

    public void set(int i, address_sha1_hash_pair address_sha1_hash_pair) {
        libtorrent_jni.address_sha1_hash_pair_vector_set(this.swigCPtr, this, i, address_sha1_hash_pair.getCPtr(address_sha1_hash_pair), address_sha1_hash_pair);
    }
}
