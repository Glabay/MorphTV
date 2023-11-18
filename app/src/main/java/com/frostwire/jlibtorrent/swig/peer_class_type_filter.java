package com.frostwire.jlibtorrent.swig;

public class peer_class_type_filter {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public static final class socket_type_t {
        public static final socket_type_t i2p_socket = new socket_type_t("i2p_socket");
        public static final socket_type_t num_socket_types = new socket_type_t("num_socket_types");
        public static final socket_type_t ssl_tcp_socket = new socket_type_t("ssl_tcp_socket");
        public static final socket_type_t ssl_utp_socket = new socket_type_t("ssl_utp_socket");
        private static int swigNext;
        private static socket_type_t[] swigValues = new socket_type_t[]{tcp_socket, utp_socket, ssl_tcp_socket, ssl_utp_socket, i2p_socket, num_socket_types};
        public static final socket_type_t tcp_socket = new socket_type_t("tcp_socket", libtorrent_jni.peer_class_type_filter_tcp_socket_get());
        public static final socket_type_t utp_socket = new socket_type_t("utp_socket");
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static socket_type_t swigToEnum(int i) {
            if (i < swigValues.length && i >= 0 && swigValues[i].swigValue == i) {
                return swigValues[i];
            }
            for (int i2 = 0; i2 < swigValues.length; i2++) {
                if (swigValues[i2].swigValue == i) {
                    return swigValues[i2];
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("No enum ");
            stringBuilder.append(socket_type_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private socket_type_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private socket_type_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private socket_type_t(String str, socket_type_t socket_type_t) {
            this.swigName = str;
            this.swigValue = socket_type_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected peer_class_type_filter(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(peer_class_type_filter peer_class_type_filter) {
        return peer_class_type_filter == null ? 0 : peer_class_type_filter.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_peer_class_type_filter(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public peer_class_type_filter() {
        this(libtorrent_jni.new_peer_class_type_filter(), true);
    }

    public void add(socket_type_t socket_type_t, int i) {
        libtorrent_jni.peer_class_type_filter_add(this.swigCPtr, this, socket_type_t.swigValue(), i);
    }

    public void remove(socket_type_t socket_type_t, int i) {
        libtorrent_jni.peer_class_type_filter_remove(this.swigCPtr, this, socket_type_t.swigValue(), i);
    }

    public void disallow(socket_type_t socket_type_t, int i) {
        libtorrent_jni.peer_class_type_filter_disallow(this.swigCPtr, this, socket_type_t.swigValue(), i);
    }

    public void allow(socket_type_t socket_type_t, int i) {
        libtorrent_jni.peer_class_type_filter_allow(this.swigCPtr, this, socket_type_t.swigValue(), i);
    }

    public long apply(int i, long j) {
        return libtorrent_jni.peer_class_type_filter_apply(this.swigCPtr, this, i, j);
    }
}
