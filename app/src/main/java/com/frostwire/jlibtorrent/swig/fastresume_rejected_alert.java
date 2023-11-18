package com.frostwire.jlibtorrent.swig;

public class fastresume_rejected_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.fastresume_rejected_alert_alert_type_get();
    public static final int priority = libtorrent_jni.fastresume_rejected_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.fastresume_rejected_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected fastresume_rejected_alert(long j, boolean z) {
        super(libtorrent_jni.fastresume_rejected_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(fastresume_rejected_alert fastresume_rejected_alert) {
        return fastresume_rejected_alert == null ? 0 : fastresume_rejected_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_fastresume_rejected_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.fastresume_rejected_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.fastresume_rejected_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.fastresume_rejected_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.fastresume_rejected_alert_message(this.swigCPtr, this);
    }

    public void setError(error_code error_code) {
        libtorrent_jni.fastresume_rejected_alert_error_set(this.swigCPtr, this, error_code.getCPtr(error_code), error_code);
    }

    public error_code getError() {
        long fastresume_rejected_alert_error_get = libtorrent_jni.fastresume_rejected_alert_error_get(this.swigCPtr, this);
        if (fastresume_rejected_alert_error_get == 0) {
            return null;
        }
        return new error_code(fastresume_rejected_alert_error_get, false);
    }

    public String file_path() {
        return libtorrent_jni.fastresume_rejected_alert_file_path(this.swigCPtr, this);
    }

    public void setOp(operation_t operation_t) {
        libtorrent_jni.fastresume_rejected_alert_op_set(this.swigCPtr, this, operation_t.swigValue());
    }

    public operation_t getOp() {
        return operation_t.swigToEnum(libtorrent_jni.fastresume_rejected_alert_op_get(this.swigCPtr, this));
    }
}
