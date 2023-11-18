package com.frostwire.jlibtorrent.swig;

public class string_long_map {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected string_long_map(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(string_long_map string_long_map) {
        return string_long_map == null ? 0 : string_long_map.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_string_long_map(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public string_long_map() {
        this(libtorrent_jni.new_string_long_map__SWIG_0(), true);
    }

    public string_long_map(string_long_map string_long_map) {
        this(libtorrent_jni.new_string_long_map__SWIG_1(getCPtr(string_long_map), string_long_map), true);
    }

    public long size() {
        return libtorrent_jni.string_long_map_size(this.swigCPtr, this);
    }

    public boolean empty() {
        return libtorrent_jni.string_long_map_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.string_long_map_clear(this.swigCPtr, this);
    }

    public int get(String str) {
        return libtorrent_jni.string_long_map_get(this.swigCPtr, this, str);
    }

    public void set(String str, int i) {
        libtorrent_jni.string_long_map_set(this.swigCPtr, this, str, i);
    }

    public void erase(String str) {
        libtorrent_jni.string_long_map_erase(this.swigCPtr, this, str);
    }

    public boolean has_key(String str) {
        return libtorrent_jni.string_long_map_has_key(this.swigCPtr, this, str);
    }

    public string_vector keys() {
        return new string_vector(libtorrent_jni.string_long_map_keys(this.swigCPtr, this), true);
    }
}
