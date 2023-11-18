package com.frostwire.jlibtorrent.swig;

public class entry_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected entry_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(entry_vector entry_vector) {
        return entry_vector == null ? 0 : entry_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_entry_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public entry_vector() {
        this(libtorrent_jni.new_entry_vector(), true);
    }

    public long size() {
        return libtorrent_jni.entry_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.entry_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.entry_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.entry_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.entry_vector_clear(this.swigCPtr, this);
    }

    public void push_back(entry entry) {
        libtorrent_jni.entry_vector_push_back(this.swigCPtr, this, entry.getCPtr(entry), entry);
    }

    public entry get(int i) {
        return new entry(libtorrent_jni.entry_vector_get(this.swigCPtr, this, i), 0);
    }

    public void set(int i, entry entry) {
        libtorrent_jni.entry_vector_set(this.swigCPtr, this, i, entry.getCPtr(entry), entry);
    }
}
