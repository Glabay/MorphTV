package com.frostwire.jlibtorrent.swig;

public class string_int_pair {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected string_int_pair(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(string_int_pair string_int_pair) {
        return string_int_pair == null ? 0 : string_int_pair.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_string_int_pair(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public string_int_pair() {
        this(libtorrent_jni.new_string_int_pair__SWIG_0(), true);
    }

    public string_int_pair(String str, int i) {
        this(libtorrent_jni.new_string_int_pair__SWIG_1(str, i), true);
    }

    public string_int_pair(string_int_pair string_int_pair) {
        this(libtorrent_jni.new_string_int_pair__SWIG_2(getCPtr(string_int_pair), string_int_pair), true);
    }

    public void setFirst(String str) {
        libtorrent_jni.string_int_pair_first_set(this.swigCPtr, this, str);
    }

    public String getFirst() {
        return libtorrent_jni.string_int_pair_first_get(this.swigCPtr, this);
    }

    public void setSecond(int i) {
        libtorrent_jni.string_int_pair_second_set(this.swigCPtr, this, i);
    }

    public int getSecond() {
        return libtorrent_jni.string_int_pair_second_get(this.swigCPtr, this);
    }
}
