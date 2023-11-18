package com.frostwire.jlibtorrent.swig;

public class session_params {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected session_params(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(session_params session_params) {
        return session_params == null ? 0 : session_params.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_session_params(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public session_params(settings_pack settings_pack) {
        this(libtorrent_jni.new_session_params__SWIG_0(settings_pack.getCPtr(settings_pack), settings_pack), true);
    }

    public session_params() {
        this(libtorrent_jni.new_session_params__SWIG_1(), true);
    }

    public session_params(session_params session_params) {
        this(libtorrent_jni.new_session_params__SWIG_2(getCPtr(session_params), session_params), true);
    }

    public void setSettings(settings_pack settings_pack) {
        libtorrent_jni.session_params_settings_set(this.swigCPtr, this, settings_pack.getCPtr(settings_pack), settings_pack);
    }

    public settings_pack getSettings() {
        long session_params_settings_get = libtorrent_jni.session_params_settings_get(this.swigCPtr, this);
        if (session_params_settings_get == 0) {
            return null;
        }
        return new settings_pack(session_params_settings_get, false);
    }

    public void setDht_settings(dht_settings dht_settings) {
        libtorrent_jni.session_params_dht_settings_set(this.swigCPtr, this, dht_settings.getCPtr(dht_settings), dht_settings);
    }

    public dht_settings getDht_settings() {
        long session_params_dht_settings_get = libtorrent_jni.session_params_dht_settings_get(this.swigCPtr, this);
        if (session_params_dht_settings_get == 0) {
            return null;
        }
        return new dht_settings(session_params_dht_settings_get, false);
    }

    public void setDht_state(dht_state dht_state) {
        libtorrent_jni.session_params_dht_state_set(this.swigCPtr, this, dht_state.getCPtr(dht_state), dht_state);
    }

    public dht_state getDht_state() {
        long session_params_dht_state_get = libtorrent_jni.session_params_dht_state_get(this.swigCPtr, this);
        if (session_params_dht_state_get == 0) {
            return null;
        }
        return new dht_state(session_params_dht_state_get, false);
    }
}
