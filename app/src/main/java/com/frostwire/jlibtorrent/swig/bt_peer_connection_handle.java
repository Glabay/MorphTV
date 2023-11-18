package com.frostwire.jlibtorrent.swig;

public class bt_peer_connection_handle extends peer_connection_handle {
    private transient long swigCPtr;

    protected bt_peer_connection_handle(long j, boolean z) {
        super(libtorrent_jni.bt_peer_connection_handle_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(bt_peer_connection_handle bt_peer_connection_handle) {
        return bt_peer_connection_handle == null ? 0 : bt_peer_connection_handle.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_bt_peer_connection_handle(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public bt_peer_connection_handle(peer_connection_handle peer_connection_handle) {
        this(libtorrent_jni.new_bt_peer_connection_handle(peer_connection_handle.getCPtr(peer_connection_handle), peer_connection_handle), true);
    }

    public boolean packet_finished() {
        return libtorrent_jni.bt_peer_connection_handle_packet_finished(this.swigCPtr, this);
    }

    public boolean support_extensions() {
        return libtorrent_jni.bt_peer_connection_handle_support_extensions(this.swigCPtr, this);
    }

    public boolean supports_encryption() {
        return libtorrent_jni.bt_peer_connection_handle_supports_encryption(this.swigCPtr, this);
    }
}
