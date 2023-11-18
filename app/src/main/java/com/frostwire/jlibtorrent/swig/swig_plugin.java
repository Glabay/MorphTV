package com.frostwire.jlibtorrent.swig;

public class swig_plugin {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    protected swig_plugin(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(swig_plugin swig_plugin) {
        return swig_plugin == null ? 0 : swig_plugin.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_swig_plugin(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    protected void swigDirectorDisconnect() {
        this.swigCMemOwn = false;
        delete();
    }

    public void swigReleaseOwnership() {
        this.swigCMemOwn = false;
        libtorrent_jni.swig_plugin_change_ownership(this, this.swigCPtr, false);
    }

    public void swigTakeOwnership() {
        this.swigCMemOwn = true;
        libtorrent_jni.swig_plugin_change_ownership(this, this.swigCPtr, true);
    }

    public boolean on_dht_request(string_view string_view, udp_endpoint udp_endpoint, bdecode_node bdecode_node, entry entry) {
        swig_plugin swig_plugin = this;
        if (getClass() == swig_plugin.class) {
            return libtorrent_jni.swig_plugin_on_dht_request(swig_plugin.swigCPtr, swig_plugin, string_view.getCPtr(string_view), string_view, udp_endpoint.getCPtr(udp_endpoint), udp_endpoint, bdecode_node.getCPtr(bdecode_node), bdecode_node, entry.getCPtr(entry), entry);
        }
        return libtorrent_jni.swig_plugin_on_dht_requestSwigExplicitswig_plugin(swig_plugin.swigCPtr, swig_plugin, string_view.getCPtr(string_view), string_view, udp_endpoint.getCPtr(udp_endpoint), udp_endpoint, bdecode_node.getCPtr(bdecode_node), bdecode_node, entry.getCPtr(entry), entry);
    }

    public swig_plugin() {
        this(libtorrent_jni.new_swig_plugin(), true);
        libtorrent_jni.swig_plugin_director_connect(this, this.swigCPtr, this.swigCMemOwn, true);
    }
}
