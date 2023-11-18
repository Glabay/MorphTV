package com.frostwire.jlibtorrent.swig;

public class file_slice_vector {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected file_slice_vector(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(file_slice_vector file_slice_vector) {
        return file_slice_vector == null ? 0 : file_slice_vector.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_file_slice_vector(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public file_slice_vector() {
        this(libtorrent_jni.new_file_slice_vector(), true);
    }

    public long size() {
        return libtorrent_jni.file_slice_vector_size(this.swigCPtr, this);
    }

    public long capacity() {
        return libtorrent_jni.file_slice_vector_capacity(this.swigCPtr, this);
    }

    public void reserve(long j) {
        libtorrent_jni.file_slice_vector_reserve(this.swigCPtr, this, j);
    }

    public boolean empty() {
        return libtorrent_jni.file_slice_vector_empty(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.file_slice_vector_clear(this.swigCPtr, this);
    }

    public void push_back(file_slice file_slice) {
        libtorrent_jni.file_slice_vector_push_back(this.swigCPtr, this, file_slice.getCPtr(file_slice), file_slice);
    }

    public file_slice get(int i) {
        return new file_slice(libtorrent_jni.file_slice_vector_get(this.swigCPtr, this, i), 0);
    }

    public void set(int i, file_slice file_slice) {
        libtorrent_jni.file_slice_vector_set(this.swigCPtr, this, i, file_slice.getCPtr(file_slice), file_slice);
    }
}
