package com.frostwire.jlibtorrent.swig;

public class unwanted_block_alert extends peer_alert {
    public static final int alert_type = libtorrent_jni.unwanted_block_alert_alert_type_get();
    public static final int priority = libtorrent_jni.unwanted_block_alert_priority_get();
    private transient long swigCPtr;

    protected unwanted_block_alert(long j, boolean z) {
        super(libtorrent_jni.unwanted_block_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(unwanted_block_alert unwanted_block_alert) {
        return unwanted_block_alert == null ? 0 : unwanted_block_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_unwanted_block_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.unwanted_block_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.unwanted_block_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.unwanted_block_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.unwanted_block_alert_message(this.swigCPtr, this);
    }

    public int getBlock_index() {
        return libtorrent_jni.unwanted_block_alert_block_index_get(this.swigCPtr, this);
    }

    public int getPiece_index() {
        return libtorrent_jni.unwanted_block_alert_piece_index_get(this.swigCPtr, this);
    }
}
