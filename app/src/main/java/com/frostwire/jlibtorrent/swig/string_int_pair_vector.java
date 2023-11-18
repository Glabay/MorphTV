package com.frostwire.jlibtorrent.swig;

public class string_int_pair_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected string_int_pair_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(string_int_pair_vector string_int_pair_vector) {
        return string_int_pair_vector == null ? 0 : string_int_pair_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_string_int_pair_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public string_int_pair_vector() {
        this(libtorrent_jni.new_string_int_pair_vector(), true);
    }

    public long size() {
        return libtorrent_jni.string_int_pair_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.string_int_pair_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.string_int_pair_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.string_int_pair_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.string_int_pair_vector_clear(this.swigCPtr, this);
    }

    public void push_back(string_int_pair string_int_pair) {
        libtorrent_jni.string_int_pair_vector_push_back(this.swigCPtr, this, string_int_pair.getCPtr(string_int_pair), string_int_pair);
    }

    public string_int_pair get(int i) {
        return new string_int_pair(libtorrent_jni.string_int_pair_vector_get(this.swigCPtr, this, i), (boolean) 0);
    }

    public void set(int i, string_int_pair string_int_pair) {
        libtorrent_jni.string_int_pair_vector_set(this.swigCPtr, this, i, string_int_pair.getCPtr(string_int_pair), string_int_pair);
    }
}
