package com.frostwire.jlibtorrent.swig;

public class piece_index_bitfield {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected piece_index_bitfield(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(piece_index_bitfield piece_index_bitfield) {
        return piece_index_bitfield == null ? 0 : piece_index_bitfield.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_piece_index_bitfield(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public piece_index_bitfield() {
        this(libtorrent_jni.new_piece_index_bitfield__SWIG_0(), true);
    }

    public piece_index_bitfield(int i) {
        this(libtorrent_jni.new_piece_index_bitfield__SWIG_1(i), (boolean) 1);
    }

    public piece_index_bitfield(int i, boolean z) {
        this(libtorrent_jni.new_piece_index_bitfield__SWIG_2(i, z), true);
    }

    public piece_index_bitfield(piece_index_bitfield piece_index_bitfield) {
        this(libtorrent_jni.new_piece_index_bitfield__SWIG_3(getCPtr(piece_index_bitfield), piece_index_bitfield), true);
    }

    public boolean get_bit(int i) {
        return libtorrent_jni.piece_index_bitfield_get_bit(this.swigCPtr, this, i);
    }

    public void clear_bit(int i) {
        libtorrent_jni.piece_index_bitfield_clear_bit(this.swigCPtr, this, i);
    }

    public void set_bit(int i) {
        libtorrent_jni.piece_index_bitfield_set_bit(this.swigCPtr, this, i);
    }

    public int end_index() {
        return libtorrent_jni.piece_index_bitfield_end_index(this.swigCPtr, this);
    }

    public boolean all_set() {
        return libtorrent_jni.piece_index_bitfield_all_set(this.swigCPtr, this);
    }

    public boolean none_set() {
        return libtorrent_jni.piece_index_bitfield_none_set(this.swigCPtr, this);
    }

    public int size() {
        return libtorrent_jni.piece_index_bitfield_size(this.swigCPtr, this);
    }

    public int num_words() {
        return libtorrent_jni.piece_index_bitfield_num_words(this.swigCPtr, this);
    }

    public boolean empty() {
        return libtorrent_jni.piece_index_bitfield_empty(this.swigCPtr, this);
    }

    public int count() {
        return libtorrent_jni.piece_index_bitfield_count(this.swigCPtr, this);
    }

    public int find_first_set() {
        return libtorrent_jni.piece_index_bitfield_find_first_set(this.swigCPtr, this);
    }

    public int find_last_clear() {
        return libtorrent_jni.piece_index_bitfield_find_last_clear(this.swigCPtr, this);
    }

    public void resize(int i, boolean z) {
        libtorrent_jni.piece_index_bitfield_resize__SWIG_0(this.swigCPtr, this, i, z);
    }

    public void resize(int i) {
        libtorrent_jni.piece_index_bitfield_resize__SWIG_1(this.swigCPtr, this, i);
    }

    public void set_all() {
        libtorrent_jni.piece_index_bitfield_set_all(this.swigCPtr, this);
    }

    public void clear_all() {
        libtorrent_jni.piece_index_bitfield_clear_all(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.piece_index_bitfield_clear(this.swigCPtr, this);
    }
}
