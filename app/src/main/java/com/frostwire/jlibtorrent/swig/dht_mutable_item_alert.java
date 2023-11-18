package com.frostwire.jlibtorrent.swig;

public class dht_mutable_item_alert extends alert {
    public static final int alert_type = libtorrent_jni.dht_mutable_item_alert_alert_type_get();
    public static final int priority = libtorrent_jni.dht_mutable_item_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.dht_mutable_item_alert_static_category_get(), false);
    private transient long swigCPtr;

    protected dht_mutable_item_alert(long j, boolean z) {
        super(libtorrent_jni.dht_mutable_item_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(dht_mutable_item_alert dht_mutable_item_alert) {
        return dht_mutable_item_alert == null ? 0 : dht_mutable_item_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_dht_mutable_item_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.dht_mutable_item_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.dht_mutable_item_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.dht_mutable_item_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.dht_mutable_item_alert_message(this.swigCPtr, this);
    }

    public void setItem(entry entry) {
        libtorrent_jni.dht_mutable_item_alert_item_set(this.swigCPtr, this, entry.getCPtr(entry), entry);
    }

    public entry getItem() {
        long dht_mutable_item_alert_item_get = libtorrent_jni.dht_mutable_item_alert_item_get(this.swigCPtr, this);
        if (dht_mutable_item_alert_item_get == 0) {
            return null;
        }
        return new entry(dht_mutable_item_alert_item_get, false);
    }

    public void setAuthoritative(boolean z) {
        libtorrent_jni.dht_mutable_item_alert_authoritative_set(this.swigCPtr, this, z);
    }

    public boolean getAuthoritative() {
        return libtorrent_jni.dht_mutable_item_alert_authoritative_get(this.swigCPtr, this);
    }

    public byte_vector get_key() {
        return new byte_vector(libtorrent_jni.dht_mutable_item_alert_get_key(this.swigCPtr, this), true);
    }

    public byte_vector get_signature() {
        return new byte_vector(libtorrent_jni.dht_mutable_item_alert_get_signature(this.swigCPtr, this), true);
    }

    public long get_seq() {
        return libtorrent_jni.dht_mutable_item_alert_get_seq(this.swigCPtr, this);
    }

    public byte_vector get_salt() {
        return new byte_vector(libtorrent_jni.dht_mutable_item_alert_get_salt(this.swigCPtr, this), true);
    }
}
