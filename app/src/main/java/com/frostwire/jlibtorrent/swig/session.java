package com.frostwire.jlibtorrent.swig;

public class session extends session_handle {
    private transient long swigCPtr;

    protected session(long j, boolean z) {
        super(libtorrent_jni.session_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(session session) {
        return session == null ? 0 : session.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_session(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public session(session_params session_params) {
        this(libtorrent_jni.new_session__SWIG_0(session_params.getCPtr(session_params), session_params), true);
    }

    public session() {
        this(libtorrent_jni.new_session__SWIG_1(), true);
    }

    public session(settings_pack settings_pack, session_flags_t session_flags_t) {
        this(libtorrent_jni.new_session__SWIG_2(settings_pack.getCPtr(settings_pack), settings_pack, session_flags_t.getCPtr(session_flags_t), session_flags_t), true);
    }

    public session(settings_pack settings_pack) {
        this(libtorrent_jni.new_session__SWIG_3(settings_pack.getCPtr(settings_pack), settings_pack), true);
    }

    public session(session session) {
        this(libtorrent_jni.new_session__SWIG_4(getCPtr(session), session), true);
    }

    public session_proxy abort() {
        return new session_proxy(libtorrent_jni.session_abort(this.swigCPtr, this), true);
    }
}
