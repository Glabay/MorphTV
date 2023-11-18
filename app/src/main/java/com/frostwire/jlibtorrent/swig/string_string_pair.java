package com.frostwire.jlibtorrent.swig;

public class string_string_pair {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected string_string_pair(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(string_string_pair string_string_pair) {
        return string_string_pair == null ? 0 : string_string_pair.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_string_string_pair(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public string_string_pair() {
        this(libtorrent_jni.new_string_string_pair__SWIG_0(), true);
    }

    public string_string_pair(String str, String str2) {
        this(libtorrent_jni.new_string_string_pair__SWIG_1(str, str2), true);
    }

    public string_string_pair(string_string_pair string_string_pair) {
        this(libtorrent_jni.new_string_string_pair__SWIG_2(getCPtr(string_string_pair), string_string_pair), true);
    }

    public void setFirst(String str) {
        libtorrent_jni.string_string_pair_first_set(this.swigCPtr, this, str);
    }

    public String getFirst() {
        return libtorrent_jni.string_string_pair_first_get(this.swigCPtr, this);
    }

    public void setSecond(String str) {
        libtorrent_jni.string_string_pair_second_set(this.swigCPtr, this, str);
    }

    public String getSecond() {
        return libtorrent_jni.string_string_pair_second_get(this.swigCPtr, this);
    }
}
