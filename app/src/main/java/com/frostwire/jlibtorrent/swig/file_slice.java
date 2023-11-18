package com.frostwire.jlibtorrent.swig;

public class file_slice {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected file_slice(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(file_slice file_slice) {
        return file_slice == null ? 0 : file_slice.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_file_slice(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void setFile_index(int i) {
        libtorrent_jni.file_slice_file_index_set(this.swigCPtr, this, i);
    }

    public int getFile_index() {
        return libtorrent_jni.file_slice_file_index_get(this.swigCPtr, this);
    }

    public void setOffset(long j) {
        libtorrent_jni.file_slice_offset_set(this.swigCPtr, this, j);
    }

    public long getOffset() {
        return libtorrent_jni.file_slice_offset_get(this.swigCPtr, this);
    }

    public void setSize(long j) {
        libtorrent_jni.file_slice_size_set(this.swigCPtr, this, j);
    }

    public long getSize() {
        return libtorrent_jni.file_slice_size_get(this.swigCPtr, this);
    }

    public file_slice() {
        this(libtorrent_jni.new_file_slice(), true);
    }
}
