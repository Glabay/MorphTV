package com.frostwire.jlibtorrent.swig;

public class address {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected address(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(address address) {
        return address == null ? 0 : address.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_address(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public address() {
        this(libtorrent_jni.new_address__SWIG_0(), true);
    }

    public address(address address) {
        this(libtorrent_jni.new_address__SWIG_1(getCPtr(address), address), true);
    }

    public boolean is_v4() {
        return libtorrent_jni.address_is_v4(this.swigCPtr, this);
    }

    public boolean is_v6() {
        return libtorrent_jni.address_is_v6(this.swigCPtr, this);
    }

    public String to_string(error_code error_code) {
        return libtorrent_jni.address_to_string(this.swigCPtr, this, error_code.getCPtr(error_code), error_code);
    }

    public static address from_string(String str, error_code error_code) {
        return new address(libtorrent_jni.address_from_string(str, error_code.getCPtr(error_code), error_code), true);
    }

    public boolean is_loopback() {
        return libtorrent_jni.address_is_loopback(this.swigCPtr, this);
    }

    public boolean is_unspecified() {
        return libtorrent_jni.address_is_unspecified(this.swigCPtr, this);
    }

    public boolean is_multicast() {
        return libtorrent_jni.address_is_multicast(this.swigCPtr, this);
    }

    public boolean op_lt(address address) {
        return libtorrent_jni.address_op_lt(this.swigCPtr, this, getCPtr(address), address);
    }

    public static int compare(address address, address address2) {
        return libtorrent_jni.address_compare(getCPtr(address), address, getCPtr(address2), address2);
    }
}
