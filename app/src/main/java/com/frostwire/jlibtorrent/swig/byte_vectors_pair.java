package com.frostwire.jlibtorrent.swig;

public class byte_vectors_pair {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected byte_vectors_pair(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(byte_vectors_pair byte_vectors_pair) {
        return byte_vectors_pair == null ? 0 : byte_vectors_pair.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_byte_vectors_pair(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public byte_vectors_pair() {
        this(libtorrent_jni.new_byte_vectors_pair__SWIG_0(), true);
    }

    public byte_vectors_pair(byte_vector byte_vector, byte_vector byte_vector2) {
        this(libtorrent_jni.new_byte_vectors_pair__SWIG_1(byte_vector.getCPtr(byte_vector), byte_vector, byte_vector.getCPtr(byte_vector2), byte_vector2), true);
    }

    public byte_vectors_pair(byte_vectors_pair byte_vectors_pair) {
        this(libtorrent_jni.new_byte_vectors_pair__SWIG_2(getCPtr(byte_vectors_pair), byte_vectors_pair), true);
    }

    public void setFirst(byte_vector byte_vector) {
        libtorrent_jni.byte_vectors_pair_first_set(this.swigCPtr, this, byte_vector.getCPtr(byte_vector), byte_vector);
    }

    public byte_vector getFirst() {
        long byte_vectors_pair_first_get = libtorrent_jni.byte_vectors_pair_first_get(this.swigCPtr, this);
        if (byte_vectors_pair_first_get == 0) {
            return null;
        }
        return new byte_vector(byte_vectors_pair_first_get, false);
    }

    public void setSecond(byte_vector byte_vector) {
        libtorrent_jni.byte_vectors_pair_second_set(this.swigCPtr, this, byte_vector.getCPtr(byte_vector), byte_vector);
    }

    public byte_vector getSecond() {
        long byte_vectors_pair_second_get = libtorrent_jni.byte_vectors_pair_second_get(this.swigCPtr, this);
        if (byte_vectors_pair_second_get == 0) {
            return null;
        }
        return new byte_vector(byte_vectors_pair_second_get, false);
    }
}
