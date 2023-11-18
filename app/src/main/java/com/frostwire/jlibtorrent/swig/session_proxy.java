package com.frostwire.jlibtorrent.swig;

public class session_proxy {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected session_proxy(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(session_proxy session_proxy) {
        return session_proxy == null ? 0 : session_proxy.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_session_proxy(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public session_proxy() {
        this(libtorrent_jni.new_session_proxy__SWIG_0(), true);
    }

    public session_proxy(session_proxy session_proxy) {
        this(libtorrent_jni.new_session_proxy__SWIG_1(getCPtr(session_proxy), session_proxy), true);
    }
}
