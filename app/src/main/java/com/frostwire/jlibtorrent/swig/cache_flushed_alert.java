package com.frostwire.jlibtorrent.swig;

public class cache_flushed_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.cache_flushed_alert_alert_type_get();
    public static final int priority = libtorrent_jni.cache_flushed_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.cache_flushed_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected cache_flushed_alert(long j, boolean z) {
        super(libtorrent_jni.cache_flushed_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(cache_flushed_alert cache_flushed_alert) {
        return cache_flushed_alert == null ? 0 : cache_flushed_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_cache_flushed_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.cache_flushed_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.cache_flushed_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.cache_flushed_alert_what(this.swigCPtr, this);
    }
}
