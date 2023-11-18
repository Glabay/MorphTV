package com.frostwire.jlibtorrent.swig;

public class byte_span {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected byte_span(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(byte_span byte_span) {
        return byte_span == null ? 0 : byte_span.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_byte_span(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public byte_span() {
        this(libtorrent_jni.new_byte_span(), true);
    }

    public long size() {
        return libtorrent_jni.byte_span_size(this.swigCPtr, this);
    }

    public boolean empty() {
        return libtorrent_jni.byte_span_empty(this.swigCPtr, this);
    }

    public byte front() {
        return libtorrent_jni.byte_span_front(this.swigCPtr, this);
    }

    public byte back() {
        return libtorrent_jni.byte_span_back(this.swigCPtr, this);
    }

    public byte_span first(long j) {
        return new byte_span(libtorrent_jni.byte_span_first(this.swigCPtr, this, j), true);
    }

    public byte_span last(long j) {
        return new byte_span(libtorrent_jni.byte_span_last(this.swigCPtr, this, j), true);
    }

    public byte_span subspan(long j) {
        return new byte_span(libtorrent_jni.byte_span_subspan__SWIG_0(this.swigCPtr, this, j), true);
    }

    public byte_span subspan(long j, long j2) {
        return new byte_span(libtorrent_jni.byte_span_subspan__SWIG_1(this.swigCPtr, this, j, j2), 1);
    }

    public byte get(long j) {
        return libtorrent_jni.byte_span_get(this.swigCPtr, this, j);
    }

    public void set(long j, byte b) {
        libtorrent_jni.byte_span_set(this.swigCPtr, this, j, b);
    }
}
