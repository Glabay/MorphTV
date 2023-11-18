package com.frostwire.jlibtorrent.swig;

public class error_code {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected error_code(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(error_code error_code) {
        return error_code == null ? 0 : error_code.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_error_code(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public error_code() {
        this(libtorrent_jni.new_error_code(), true);
    }

    public void clear() {
        libtorrent_jni.error_code_clear(this.swigCPtr, this);
    }

    public int value() {
        return libtorrent_jni.error_code_value(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.error_code_message(this.swigCPtr, this);
    }
}
