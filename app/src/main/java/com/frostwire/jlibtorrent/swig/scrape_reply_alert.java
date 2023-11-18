package com.frostwire.jlibtorrent.swig;

public class scrape_reply_alert extends tracker_alert {
    public static final int alert_type = libtorrent_jni.scrape_reply_alert_alert_type_get();
    public static final int priority = libtorrent_jni.scrape_reply_alert_priority_get();
    private transient long swigCPtr;

    protected scrape_reply_alert(long j, boolean z) {
        super(libtorrent_jni.scrape_reply_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(scrape_reply_alert scrape_reply_alert) {
        return scrape_reply_alert == null ? 0 : scrape_reply_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_scrape_reply_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.scrape_reply_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.scrape_reply_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.scrape_reply_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.scrape_reply_alert_message(this.swigCPtr, this);
    }

    public int getIncomplete() {
        return libtorrent_jni.scrape_reply_alert_incomplete_get(this.swigCPtr, this);
    }

    public int getComplete() {
        return libtorrent_jni.scrape_reply_alert_complete_get(this.swigCPtr, this);
    }
}
