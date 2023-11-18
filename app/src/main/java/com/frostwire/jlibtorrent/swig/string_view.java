package com.frostwire.jlibtorrent.swig;

public class string_view {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected string_view(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(string_view string_view) {
        return string_view == null ? 0 : string_view.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_string_view(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public byte_vector to_bytes() {
        return new byte_vector(libtorrent_jni.string_view_to_bytes(this.swigCPtr, this), true);
    }

    public string_view() {
        this(libtorrent_jni.new_string_view(), true);
    }
}
