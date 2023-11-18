package com.frostwire.jlibtorrent.swig;

public class save_resume_data_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.save_resume_data_alert_alert_type_get();
    public static final int priority = libtorrent_jni.save_resume_data_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.save_resume_data_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected save_resume_data_alert(long j, boolean z) {
        super(libtorrent_jni.save_resume_data_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(save_resume_data_alert save_resume_data_alert) {
        return save_resume_data_alert == null ? 0 : save_resume_data_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_save_resume_data_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.save_resume_data_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.save_resume_data_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.save_resume_data_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.save_resume_data_alert_message(this.swigCPtr, this);
    }

    public void setParams(add_torrent_params add_torrent_params) {
        libtorrent_jni.save_resume_data_alert_params_set(this.swigCPtr, this, add_torrent_params.getCPtr(add_torrent_params), add_torrent_params);
    }

    public add_torrent_params getParams() {
        long save_resume_data_alert_params_get = libtorrent_jni.save_resume_data_alert_params_get(this.swigCPtr, this);
        if (save_resume_data_alert_params_get == 0) {
            return null;
        }
        return new add_torrent_params(save_resume_data_alert_params_get, false);
    }
}
