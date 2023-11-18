package com.frostwire.jlibtorrent.swig;

public class read_piece_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.read_piece_alert_alert_type_get();
    public static final int priority = libtorrent_jni.read_piece_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.read_piece_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected read_piece_alert(long j, boolean z) {
        super(libtorrent_jni.read_piece_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(read_piece_alert read_piece_alert) {
        return read_piece_alert == null ? 0 : read_piece_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_read_piece_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.read_piece_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.read_piece_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.read_piece_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.read_piece_alert_message(this.swigCPtr, this);
    }

    public error_code getError() {
        long read_piece_alert_error_get = libtorrent_jni.read_piece_alert_error_get(this.swigCPtr, this);
        if (read_piece_alert_error_get == 0) {
            return null;
        }
        return new error_code(read_piece_alert_error_get, false);
    }

    public int getPiece() {
        return libtorrent_jni.read_piece_alert_piece_get(this.swigCPtr, this);
    }

    public int getSize() {
        return libtorrent_jni.read_piece_alert_size_get(this.swigCPtr, this);
    }

    public long buffer_ptr() {
        return libtorrent_jni.read_piece_alert_buffer_ptr(this.swigCPtr, this);
    }
}
