package com.frostwire.jlibtorrent.swig;

public class peer_request {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected peer_request(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(peer_request peer_request) {
        return peer_request == null ? 0 : peer_request.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_peer_request(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void setPiece(int i) {
        libtorrent_jni.peer_request_piece_set(this.swigCPtr, this, i);
    }

    public int getPiece() {
        return libtorrent_jni.peer_request_piece_get(this.swigCPtr, this);
    }

    public void setStart(int i) {
        libtorrent_jni.peer_request_start_set(this.swigCPtr, this, i);
    }

    public int getStart() {
        return libtorrent_jni.peer_request_start_get(this.swigCPtr, this);
    }

    public void setLength(int i) {
        libtorrent_jni.peer_request_length_set(this.swigCPtr, this, i);
    }

    public int getLength() {
        return libtorrent_jni.peer_request_length_get(this.swigCPtr, this);
    }

    public boolean op_eq(peer_request peer_request) {
        return libtorrent_jni.peer_request_op_eq(this.swigCPtr, this, getCPtr(peer_request), peer_request);
    }

    public peer_request() {
        this(libtorrent_jni.new_peer_request(), true);
    }
}
