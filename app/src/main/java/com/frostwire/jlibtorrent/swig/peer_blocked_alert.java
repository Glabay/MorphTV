package com.frostwire.jlibtorrent.swig;

public class peer_blocked_alert extends peer_alert {
    public static final int alert_type = libtorrent_jni.peer_blocked_alert_alert_type_get();
    public static final int priority = libtorrent_jni.peer_blocked_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.peer_blocked_alert_static_category_get(), false);
    private transient long swigCPtr;

    public static final class reason_t {
        public static final reason_t i2p_mixed = new reason_t("i2p_mixed");
        public static final reason_t invalid_local_interface = new reason_t("invalid_local_interface");
        public static final reason_t ip_filter = new reason_t("ip_filter");
        public static final reason_t port_filter = new reason_t("port_filter");
        public static final reason_t privileged_ports = new reason_t("privileged_ports");
        private static int swigNext;
        private static reason_t[] swigValues = new reason_t[]{ip_filter, port_filter, i2p_mixed, privileged_ports, utp_disabled, tcp_disabled, invalid_local_interface};
        public static final reason_t tcp_disabled = new reason_t("tcp_disabled");
        public static final reason_t utp_disabled = new reason_t("utp_disabled");
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static reason_t swigToEnum(int i) {
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
            stringBuilder.append(reason_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private reason_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private reason_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private reason_t(String str, reason_t reason_t) {
            this.swigName = str;
            this.swigValue = reason_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected peer_blocked_alert(long j, boolean z) {
        super(libtorrent_jni.peer_blocked_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(peer_blocked_alert peer_blocked_alert) {
        return peer_blocked_alert == null ? 0 : peer_blocked_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_peer_blocked_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.peer_blocked_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.peer_blocked_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.peer_blocked_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.peer_blocked_alert_message(this.swigCPtr, this);
    }

    public int getReason() {
        return libtorrent_jni.peer_blocked_alert_reason_get(this.swigCPtr, this);
    }
}
