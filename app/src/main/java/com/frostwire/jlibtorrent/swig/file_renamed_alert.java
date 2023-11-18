package com.frostwire.jlibtorrent.swig;

public class file_renamed_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.file_renamed_alert_alert_type_get();
    public static final int priority = libtorrent_jni.file_renamed_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.file_renamed_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected file_renamed_alert(long j, boolean z) {
        super(libtorrent_jni.file_renamed_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(file_renamed_alert file_renamed_alert) {
        return file_renamed_alert == null ? 0 : file_renamed_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_file_renamed_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.file_renamed_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.file_renamed_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.file_renamed_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.file_renamed_alert_message(this.swigCPtr, this);
    }

    public String new_name() {
        return libtorrent_jni.file_renamed_alert_new_name(this.swigCPtr, this);
    }

    public int getIndex() {
        return libtorrent_jni.file_renamed_alert_index_get(this.swigCPtr, this);
    }
}
