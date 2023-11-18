package com.frostwire.jlibtorrent.swig;

public class sha1_hash_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected sha1_hash_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(sha1_hash_vector sha1_hash_vector) {
        return sha1_hash_vector == null ? 0 : sha1_hash_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_sha1_hash_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public sha1_hash_vector() {
        this(libtorrent_jni.new_sha1_hash_vector(), true);
    }

    public long size() {
        return libtorrent_jni.sha1_hash_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.sha1_hash_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.sha1_hash_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.sha1_hash_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.sha1_hash_vector_clear(this.swigCPtr, this);
    }

    public void push_back(sha1_hash sha1_hash) {
        libtorrent_jni.sha1_hash_vector_push_back(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public sha1_hash get(int i) {
        return new sha1_hash(libtorrent_jni.sha1_hash_vector_get(this.swigCPtr, this, i), 0);
    }

    public void set(int i, sha1_hash sha1_hash) {
        libtorrent_jni.sha1_hash_vector_set(this.swigCPtr, this, i, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }
}
