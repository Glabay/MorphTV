package com.frostwire.jlibtorrent.swig;

public class announce_entry_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected announce_entry_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(announce_entry_vector announce_entry_vector) {
        return announce_entry_vector == null ? 0 : announce_entry_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_announce_entry_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public announce_entry_vector() {
        this(libtorrent_jni.new_announce_entry_vector(), true);
    }

    public long size() {
        return libtorrent_jni.announce_entry_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.announce_entry_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.announce_entry_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.announce_entry_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.announce_entry_vector_clear(this.swigCPtr, this);
    }

    public void push_back(announce_entry announce_entry) {
        libtorrent_jni.announce_entry_vector_push_back(this.swigCPtr, this, announce_entry.getCPtr(announce_entry), announce_entry);
    }

    public announce_entry get(int i) {
        return new announce_entry(libtorrent_jni.announce_entry_vector_get(this.swigCPtr, this, i), 0);
    }

    public void set(int i, announce_entry announce_entry) {
        libtorrent_jni.announce_entry_vector_set(this.swigCPtr, this, i, announce_entry.getCPtr(announce_entry), announce_entry);
    }
}
