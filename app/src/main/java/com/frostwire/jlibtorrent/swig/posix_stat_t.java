package com.frostwire.jlibtorrent.swig;

public class posix_stat_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected posix_stat_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(posix_stat_t posix_stat_t) {
        return posix_stat_t == null ? 0 : posix_stat_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_posix_stat_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void setSize(long j) {
        libtorrent_jni.posix_stat_t_size_set(this.swigCPtr, this, j);
    }

    public long getSize() {
        return libtorrent_jni.posix_stat_t_size_get(this.swigCPtr, this);
    }

    public void setAtime(long j) {
        libtorrent_jni.posix_stat_t_atime_set(this.swigCPtr, this, j);
    }

    public long getAtime() {
        return libtorrent_jni.posix_stat_t_atime_get(this.swigCPtr, this);
    }

    public void setMtime(long j) {
        libtorrent_jni.posix_stat_t_mtime_set(this.swigCPtr, this, j);
    }

    public long getMtime() {
        return libtorrent_jni.posix_stat_t_mtime_get(this.swigCPtr, this);
    }

    public void setCtime(long j) {
        libtorrent_jni.posix_stat_t_ctime_set(this.swigCPtr, this, j);
    }

    public long getCtime() {
        return libtorrent_jni.posix_stat_t_ctime_get(this.swigCPtr, this);
    }

    public void setMode(int i) {
        libtorrent_jni.posix_stat_t_mode_set(this.swigCPtr, this, i);
    }

    public int getMode() {
        return libtorrent_jni.posix_stat_t_mode_get(this.swigCPtr, this);
    }

    public posix_stat_t() {
        this(libtorrent_jni.new_posix_stat_t(), true);
    }
}
