package com.frostwire.jlibtorrent.swig;

public class bloom_filter_256 {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected bloom_filter_256(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(bloom_filter_256 bloom_filter_256) {
        return bloom_filter_256 == null ? 0 : bloom_filter_256.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_bloom_filter_256(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public boolean find(sha1_hash sha1_hash) {
        return libtorrent_jni.bloom_filter_256_find(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public void set(sha1_hash sha1_hash) {
        libtorrent_jni.bloom_filter_256_set(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public void clear() {
        libtorrent_jni.bloom_filter_256_clear(this.swigCPtr, this);
    }

    public float size() {
        return libtorrent_jni.bloom_filter_256_size(this.swigCPtr, this);
    }

    public bloom_filter_256() {
        this(libtorrent_jni.new_bloom_filter_256(), true);
    }

    public byte_vector to_bytes() {
        return new byte_vector(libtorrent_jni.bloom_filter_256_to_bytes(this.swigCPtr, this), true);
    }

    public void from_bytes(byte_vector byte_vector) {
        libtorrent_jni.bloom_filter_256_from_bytes(this.swigCPtr, this, byte_vector.getCPtr(byte_vector), byte_vector);
    }
}
