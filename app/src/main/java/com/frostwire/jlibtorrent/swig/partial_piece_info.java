package com.frostwire.jlibtorrent.swig;

public class partial_piece_info {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected partial_piece_info(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(partial_piece_info partial_piece_info) {
        return partial_piece_info == null ? 0 : partial_piece_info.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_partial_piece_info(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void setPiece_index(int i) {
        libtorrent_jni.partial_piece_info_piece_index_set(this.swigCPtr, this, i);
    }

    public int getPiece_index() {
        return libtorrent_jni.partial_piece_info_piece_index_get(this.swigCPtr, this);
    }

    public void setBlocks_in_piece(int i) {
        libtorrent_jni.partial_piece_info_blocks_in_piece_set(this.swigCPtr, this, i);
    }

    public int getBlocks_in_piece() {
        return libtorrent_jni.partial_piece_info_blocks_in_piece_get(this.swigCPtr, this);
    }

    public void setFinished(int i) {
        libtorrent_jni.partial_piece_info_finished_set(this.swigCPtr, this, i);
    }

    public int getFinished() {
        return libtorrent_jni.partial_piece_info_finished_get(this.swigCPtr, this);
    }

    public void setWriting(int i) {
        libtorrent_jni.partial_piece_info_writing_set(this.swigCPtr, this, i);
    }

    public int getWriting() {
        return libtorrent_jni.partial_piece_info_writing_get(this.swigCPtr, this);
    }

    public void setRequested(int i) {
        libtorrent_jni.partial_piece_info_requested_set(this.swigCPtr, this, i);
    }

    public int getRequested() {
        return libtorrent_jni.partial_piece_info_requested_get(this.swigCPtr, this);
    }

    public partial_piece_info() {
        this(libtorrent_jni.new_partial_piece_info(), true);
    }
}
