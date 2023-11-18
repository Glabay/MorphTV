package com.frostwire.jlibtorrent.swig;

public class tcp_endpoint {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected tcp_endpoint(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(tcp_endpoint tcp_endpoint) {
        return tcp_endpoint == null ? 0 : tcp_endpoint.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_tcp_endpoint(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public tcp_endpoint() {
        this(libtorrent_jni.new_tcp_endpoint__SWIG_0(), true);
    }

    public tcp_endpoint(address address, int i) {
        this(libtorrent_jni.new_tcp_endpoint__SWIG_1(address.getCPtr(address), address, i), true);
    }

    public tcp_endpoint(tcp_endpoint tcp_endpoint) {
        this(libtorrent_jni.new_tcp_endpoint__SWIG_2(getCPtr(tcp_endpoint), tcp_endpoint), true);
    }

    public int port() {
        return libtorrent_jni.tcp_endpoint_port(this.swigCPtr, this);
    }

    public address address() {
        return new address(libtorrent_jni.tcp_endpoint_address(this.swigCPtr, this), true);
    }
}
