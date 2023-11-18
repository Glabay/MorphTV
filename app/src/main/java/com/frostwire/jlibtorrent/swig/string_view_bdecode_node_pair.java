package com.frostwire.jlibtorrent.swig;

public class string_view_bdecode_node_pair {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected string_view_bdecode_node_pair(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(string_view_bdecode_node_pair string_view_bdecode_node_pair) {
        return string_view_bdecode_node_pair == null ? 0 : string_view_bdecode_node_pair.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_string_view_bdecode_node_pair(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public string_view_bdecode_node_pair() {
        this(libtorrent_jni.new_string_view_bdecode_node_pair__SWIG_0(), true);
    }

    public string_view_bdecode_node_pair(string_view string_view, bdecode_node bdecode_node) {
        this(libtorrent_jni.new_string_view_bdecode_node_pair__SWIG_1(string_view.getCPtr(string_view), string_view, bdecode_node.getCPtr(bdecode_node), bdecode_node), true);
    }

    public string_view_bdecode_node_pair(string_view_bdecode_node_pair string_view_bdecode_node_pair) {
        this(libtorrent_jni.new_string_view_bdecode_node_pair__SWIG_2(getCPtr(string_view_bdecode_node_pair), string_view_bdecode_node_pair), true);
    }

    public void setFirst(string_view string_view) {
        libtorrent_jni.string_view_bdecode_node_pair_first_set(this.swigCPtr, this, string_view.getCPtr(string_view), string_view);
    }

    public string_view getFirst() {
        long string_view_bdecode_node_pair_first_get = libtorrent_jni.string_view_bdecode_node_pair_first_get(this.swigCPtr, this);
        if (string_view_bdecode_node_pair_first_get == 0) {
            return null;
        }
        return new string_view(string_view_bdecode_node_pair_first_get, false);
    }

    public void setSecond(bdecode_node bdecode_node) {
        libtorrent_jni.string_view_bdecode_node_pair_second_set(this.swigCPtr, this, bdecode_node.getCPtr(bdecode_node), bdecode_node);
    }

    public bdecode_node getSecond() {
        long string_view_bdecode_node_pair_second_get = libtorrent_jni.string_view_bdecode_node_pair_second_get(this.swigCPtr, this);
        if (string_view_bdecode_node_pair_second_get == 0) {
            return null;
        }
        return new bdecode_node(string_view_bdecode_node_pair_second_get, false);
    }
}
