package com.frostwire.jlibtorrent.swig;

public class address_sha1_hash_pair {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected address_sha1_hash_pair(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(address_sha1_hash_pair address_sha1_hash_pair) {
        return address_sha1_hash_pair == null ? 0 : address_sha1_hash_pair.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_address_sha1_hash_pair(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public address_sha1_hash_pair() {
        this(libtorrent_jni.new_address_sha1_hash_pair__SWIG_0(), true);
    }

    public address_sha1_hash_pair(address address, sha1_hash sha1_hash) {
        this(libtorrent_jni.new_address_sha1_hash_pair__SWIG_1(address.getCPtr(address), address, sha1_hash.getCPtr(sha1_hash), sha1_hash), true);
    }

    public address_sha1_hash_pair(address_sha1_hash_pair address_sha1_hash_pair) {
        this(libtorrent_jni.new_address_sha1_hash_pair__SWIG_2(getCPtr(address_sha1_hash_pair), address_sha1_hash_pair), true);
    }

    public void setFirst(address address) {
        libtorrent_jni.address_sha1_hash_pair_first_set(this.swigCPtr, this, address.getCPtr(address), address);
    }

    public address getFirst() {
        long address_sha1_hash_pair_first_get = libtorrent_jni.address_sha1_hash_pair_first_get(this.swigCPtr, this);
        if (address_sha1_hash_pair_first_get == 0) {
            return null;
        }
        return new address(address_sha1_hash_pair_first_get, false);
    }

    public void setSecond(sha1_hash sha1_hash) {
        libtorrent_jni.address_sha1_hash_pair_second_set(this.swigCPtr, this, sha1_hash.getCPtr(sha1_hash), sha1_hash);
    }

    public sha1_hash getSecond() {
        long address_sha1_hash_pair_second_get = libtorrent_jni.address_sha1_hash_pair_second_get(this.swigCPtr, this);
        if (address_sha1_hash_pair_second_get == 0) {
            return null;
        }
        return new sha1_hash(address_sha1_hash_pair_second_get, false);
    }
}
