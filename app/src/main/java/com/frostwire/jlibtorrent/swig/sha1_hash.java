package com.frostwire.jlibtorrent.swig;

public class sha1_hash {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected sha1_hash(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(sha1_hash sha1_hash) {
        return sha1_hash == null ? 0 : sha1_hash.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_sha1_hash(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static long size() {
        return libtorrent_jni.sha1_hash_size();
    }

    public sha1_hash() {
        this(libtorrent_jni.new_sha1_hash__SWIG_0(), true);
    }

    public sha1_hash(sha1_hash sha1_hash) {
        this(libtorrent_jni.new_sha1_hash__SWIG_1(getCPtr(sha1_hash), sha1_hash), true);
    }

    public static sha1_hash max() {
        return new sha1_hash(libtorrent_jni.sha1_hash_max(), true);
    }

    public static sha1_hash min() {
        return new sha1_hash(libtorrent_jni.sha1_hash_min(), true);
    }

    public void clear() {
        libtorrent_jni.sha1_hash_clear(this.swigCPtr, this);
    }

    public boolean is_all_zeros() {
        return libtorrent_jni.sha1_hash_is_all_zeros(this.swigCPtr, this);
    }

    public int count_leading_zeroes() {
        return libtorrent_jni.sha1_hash_count_leading_zeroes(this.swigCPtr, this);
    }

    public sha1_hash(byte_vector byte_vector) {
        this(libtorrent_jni.new_sha1_hash__SWIG_2(byte_vector.getCPtr(byte_vector), byte_vector), true);
    }

    public int hash_code() {
        return libtorrent_jni.sha1_hash_hash_code(this.swigCPtr, this);
    }

    public byte_vector to_bytes() {
        return new byte_vector(libtorrent_jni.sha1_hash_to_bytes(this.swigCPtr, this), true);
    }

    public String to_hex() {
        return libtorrent_jni.sha1_hash_to_hex(this.swigCPtr, this);
    }

    public boolean op_eq(sha1_hash sha1_hash) {
        return libtorrent_jni.sha1_hash_op_eq(this.swigCPtr, this, getCPtr(sha1_hash), sha1_hash);
    }

    public boolean op_ne(sha1_hash sha1_hash) {
        return libtorrent_jni.sha1_hash_op_ne(this.swigCPtr, this, getCPtr(sha1_hash), sha1_hash);
    }

    public boolean op_lt(sha1_hash sha1_hash) {
        return libtorrent_jni.sha1_hash_op_lt(this.swigCPtr, this, getCPtr(sha1_hash), sha1_hash);
    }

    public static int compare(sha1_hash sha1_hash, sha1_hash sha1_hash2) {
        return libtorrent_jni.sha1_hash_compare(getCPtr(sha1_hash), sha1_hash, getCPtr(sha1_hash2), sha1_hash2);
    }
}
