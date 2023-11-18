package com.frostwire.jlibtorrent.swig;

public class bloom_filter_128 {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected bloom_filter_128(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(bloom_filter_128 bloom_filter_128) {
        return bloom_filter_128 == null ? 0 : bloom_filter_128.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_bloom_filter_128(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public boolean find(sha1_hash sha1_hash) {
        return libtorrent_jni.bloom_filter_128_find(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public void set(sha1_hash sha1_hash) {
        libtorrent_jni.bloom_filter_128_set(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public void clear() {
        libtorrent_jni.bloom_filter_128_clear(this.swigCPtr, this);
    }

    public float size() {
        return libtorrent_jni.bloom_filter_128_size(this.swigCPtr, this);
    }

    public bloom_filter_128() {
        this(libtorrent_jni.new_bloom_filter_128(), true);
    }

    public byte_vector to_bytes() {
        return new byte_vector(libtorrent_jni.bloom_filter_128_to_bytes(this.swigCPtr, this), true);
    }

    public void from_bytes(byte_vector byte_vector) {
        libtorrent_jni.bloom_filter_128_from_bytes(this.swigCPtr, this, byte_vector.getCPtr(byte_vector), byte_vector);
    }
}
