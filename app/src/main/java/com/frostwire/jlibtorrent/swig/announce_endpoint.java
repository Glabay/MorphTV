package com.frostwire.jlibtorrent.swig;

public class announce_endpoint {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected announce_endpoint(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(announce_endpoint announce_endpoint) {
        return announce_endpoint == null ? 0 : announce_endpoint.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_announce_endpoint(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void setMessage(String str) {
        libtorrent_jni.announce_endpoint_message_set(this.swigCPtr, this, str);
    }

    public String getMessage() {
        return libtorrent_jni.announce_endpoint_message_get(this.swigCPtr, this);
    }

    public void setLast_error(error_code error_code) {
        libtorrent_jni.announce_endpoint_last_error_set(this.swigCPtr, this, error_code.getCPtr(error_code), error_code);
    }

    public error_code getLast_error() {
        long announce_endpoint_last_error_get = libtorrent_jni.announce_endpoint_last_error_get(this.swigCPtr, this);
        if (announce_endpoint_last_error_get == 0) {
            return null;
        }
        return new error_code(announce_endpoint_last_error_get, false);
    }

    public void setLocal_endpoint(tcp_endpoint tcp_endpoint) {
        libtorrent_jni.announce_endpoint_local_endpoint_set(this.swigCPtr, this, tcp_endpoint.getCPtr(tcp_endpoint), tcp_endpoint);
    }

    public tcp_endpoint getLocal_endpoint() {
        long announce_endpoint_local_endpoint_get = libtorrent_jni.announce_endpoint_local_endpoint_get(this.swigCPtr, this);
        if (announce_endpoint_local_endpoint_get == 0) {
            return null;
        }
        return new tcp_endpoint(announce_endpoint_local_endpoint_get, false);
    }

    public void setScrape_incomplete(int i) {
        libtorrent_jni.announce_endpoint_scrape_incomplete_set(this.swigCPtr, this, i);
    }

    public int getScrape_incomplete() {
        return libtorrent_jni.announce_endpoint_scrape_incomplete_get(this.swigCPtr, this);
    }

    public void setScrape_complete(int i) {
        libtorrent_jni.announce_endpoint_scrape_complete_set(this.swigCPtr, this, i);
    }

    public int getScrape_complete() {
        return libtorrent_jni.announce_endpoint_scrape_complete_get(this.swigCPtr, this);
    }

    public void setScrape_downloaded(int i) {
        libtorrent_jni.announce_endpoint_scrape_downloaded_set(this.swigCPtr, this, i);
    }

    public int getScrape_downloaded() {
        return libtorrent_jni.announce_endpoint_scrape_downloaded_get(this.swigCPtr, this);
    }

    public void setFails(short s) {
        libtorrent_jni.announce_endpoint_fails_set(this.swigCPtr, this, s);
    }

    public short getFails() {
        return libtorrent_jni.announce_endpoint_fails_get(this.swigCPtr, this);
    }

    public void setUpdating(boolean z) {
        libtorrent_jni.announce_endpoint_updating_set(this.swigCPtr, this, z);
    }

    public boolean getUpdating() {
        return libtorrent_jni.announce_endpoint_updating_get(this.swigCPtr, this);
    }

    public void setStart_sent(boolean z) {
        libtorrent_jni.announce_endpoint_start_sent_set(this.swigCPtr, this, z);
    }

    public boolean getStart_sent() {
        return libtorrent_jni.announce_endpoint_start_sent_get(this.swigCPtr, this);
    }

    public void setComplete_sent(boolean z) {
        libtorrent_jni.announce_endpoint_complete_sent_set(this.swigCPtr, this, z);
    }

    public boolean getComplete_sent() {
        return libtorrent_jni.announce_endpoint_complete_sent_get(this.swigCPtr, this);
    }

    public void reset() {
        libtorrent_jni.announce_endpoint_reset(this.swigCPtr, this);
    }

    public boolean is_working() {
        return libtorrent_jni.announce_endpoint_is_working(this.swigCPtr, this);
    }

    public long get_next_announce() {
        return libtorrent_jni.announce_endpoint_get_next_announce(this.swigCPtr, this);
    }

    public long get_min_announce() {
        return libtorrent_jni.announce_endpoint_get_min_announce(this.swigCPtr, this);
    }
}
