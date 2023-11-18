package com.frostwire.jlibtorrent.swig;

public class alert_category_t {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected alert_category_t(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(alert_category_t alert_category_t) {
        return alert_category_t == null ? 0 : alert_category_t.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_alert_category_t(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public static alert_category_t all() {
        return new alert_category_t(libtorrent_jni.alert_category_t_all(), true);
    }

    public boolean nonZero() {
        return libtorrent_jni.alert_category_t_nonZero(this.swigCPtr, this);
    }

    public boolean eq(alert_category_t alert_category_t) {
        return libtorrent_jni.alert_category_t_eq(this.swigCPtr, this, getCPtr(alert_category_t), alert_category_t);
    }

    public boolean ne(alert_category_t alert_category_t) {
        return libtorrent_jni.alert_category_t_ne(this.swigCPtr, this, getCPtr(alert_category_t), alert_category_t);
    }

    public alert_category_t or_(alert_category_t alert_category_t) {
        return new alert_category_t(libtorrent_jni.alert_category_t_or_(this.swigCPtr, this, getCPtr(alert_category_t), alert_category_t), true);
    }

    public alert_category_t and_(alert_category_t alert_category_t) {
        return new alert_category_t(libtorrent_jni.alert_category_t_and_(this.swigCPtr, this, getCPtr(alert_category_t), alert_category_t), true);
    }

    public alert_category_t xor(alert_category_t alert_category_t) {
        return new alert_category_t(libtorrent_jni.alert_category_t_xor(this.swigCPtr, this, getCPtr(alert_category_t), alert_category_t), true);
    }

    public alert_category_t inv() {
        return new alert_category_t(libtorrent_jni.alert_category_t_inv(this.swigCPtr, this), true);
    }

    public int to_int() {
        return libtorrent_jni.alert_category_t_to_int(this.swigCPtr, this);
    }

    public alert_category_t() {
        this(libtorrent_jni.new_alert_category_t(), true);
    }
}
