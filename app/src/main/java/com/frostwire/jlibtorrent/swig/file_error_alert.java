package com.frostwire.jlibtorrent.swig;

public class file_error_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.file_error_alert_alert_type_get();
    public static final int priority = libtorrent_jni.file_error_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.file_error_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected file_error_alert(long j, boolean z) {
        super(libtorrent_jni.file_error_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(file_error_alert file_error_alert) {
        return file_error_alert == null ? 0 : file_error_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_file_error_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.file_error_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.file_error_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.file_error_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.file_error_alert_message(this.swigCPtr, this);
    }

    public error_code getError() {
        long file_error_alert_error_get = libtorrent_jni.file_error_alert_error_get(this.swigCPtr, this);
        if (file_error_alert_error_get == 0) {
            return null;
        }
        return new error_code(file_error_alert_error_get, false);
    }

    public void setOp(operation_t operation_t) {
        libtorrent_jni.file_error_alert_op_set(this.swigCPtr, this, operation_t.swigValue());
    }

    public operation_t getOp() {
        return operation_t.swigToEnum(libtorrent_jni.file_error_alert_op_get(this.swigCPtr, this));
    }

    public String filename() {
        return libtorrent_jni.file_error_alert_filename(this.swigCPtr, this);
    }
}
