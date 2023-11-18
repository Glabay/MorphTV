package com.frostwire.jlibtorrent.swig;

public class udp_endpoint {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected udp_endpoint(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(udp_endpoint udp_endpoint) {
        return udp_endpoint == null ? 0 : udp_endpoint.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_udp_endpoint(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public udp_endpoint() {
        this(libtorrent_jni.new_udp_endpoint__SWIG_0(), true);
    }

    public udp_endpoint(address address, int i) {
        this(libtorrent_jni.new_udp_endpoint__SWIG_1(address.getCPtr(address), address, i), true);
    }

    public udp_endpoint(udp_endpoint udp_endpoint) {
        this(libtorrent_jni.new_udp_endpoint__SWIG_2(getCPtr(udp_endpoint), udp_endpoint), true);
    }

    public int port() {
        return libtorrent_jni.udp_endpoint_port(this.swigCPtr, this);
    }

    public address address() {
        return new address(libtorrent_jni.udp_endpoint_address(this.swigCPtr, this), true);
    }
}
