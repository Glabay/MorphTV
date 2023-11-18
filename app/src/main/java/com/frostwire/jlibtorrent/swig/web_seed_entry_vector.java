package com.frostwire.jlibtorrent.swig;

public class web_seed_entry_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected web_seed_entry_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(web_seed_entry_vector web_seed_entry_vector) {
        return web_seed_entry_vector == null ? 0 : web_seed_entry_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_web_seed_entry_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public web_seed_entry_vector() {
        this(libtorrent_jni.new_web_seed_entry_vector(), true);
    }

    public long size() {
        return libtorrent_jni.web_seed_entry_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.web_seed_entry_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.web_seed_entry_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.web_seed_entry_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.web_seed_entry_vector_clear(this.swigCPtr, this);
    }

    public void push_back(web_seed_entry web_seed_entry) {
        libtorrent_jni.web_seed_entry_vector_push_back(this.swigCPtr, this, web_seed_entry.getCPtr(web_seed_entry), web_seed_entry);
    }

    public web_seed_entry get(int i) {
        return new web_seed_entry(libtorrent_jni.web_seed_entry_vector_get(this.swigCPtr, this, i), (boolean) 0);
    }

    public void set(int i, web_seed_entry web_seed_entry) {
        libtorrent_jni.web_seed_entry_vector_set(this.swigCPtr, this, i, web_seed_entry.getCPtr(web_seed_entry), web_seed_entry);
    }
}
