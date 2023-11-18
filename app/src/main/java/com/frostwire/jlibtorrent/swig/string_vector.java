package com.frostwire.jlibtorrent.swig;

public class string_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected string_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(string_vector string_vector) {
        return string_vector == null ? 0 : string_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_string_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public string_vector() {
        this(libtorrent_jni.new_string_vector(), true);
    }

    public long size() {
        return libtorrent_jni.string_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.string_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.string_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.string_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.string_vector_clear(this.swigCPtr, this);
    }

    public void push_back(String str) {
        libtorrent_jni.string_vector_push_back(this.swigCPtr, this, str);
    }

    public String get(int i) {
        return libtorrent_jni.string_vector_get(this.swigCPtr, this, i);
    }

    public void set(int i, String str) {
        libtorrent_jni.string_vector_set(this.swigCPtr, this, i, str);
    }
}
