package com.frostwire.jlibtorrent.swig;

public class dht_pkt_alert extends alert {
    public static final int alert_type = libtorrent_jni.dht_pkt_alert_alert_type_get();
    public static final int priority = libtorrent_jni.dht_pkt_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.dht_pkt_alert_static_category_get(), false);
    private transient long swigCPtr;

    public static final class direction_t {
        public static final direction_t incoming = new direction_t("incoming");
        public static final direction_t outgoing = new direction_t("outgoing");
        private static int swigNext;
        private static direction_t[] swigValues = new direction_t[]{incoming, outgoing};
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static direction_t swigToEnum(int i) {
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
            stringBuilder.append(direction_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private direction_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private direction_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private direction_t(String str, direction_t direction_t) {
            this.swigName = str;
            this.swigValue = direction_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected dht_pkt_alert(long j, boolean z) {
        super(libtorrent_jni.dht_pkt_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(dht_pkt_alert dht_pkt_alert) {
        return dht_pkt_alert == null ? 0 : dht_pkt_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_dht_pkt_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.dht_pkt_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.dht_pkt_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.dht_pkt_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.dht_pkt_alert_message(this.swigCPtr, this);
    }

    public byte_const_span pkt_buf() {
        return new byte_const_span(libtorrent_jni.dht_pkt_alert_pkt_buf(this.swigCPtr, this), true);
    }

    public void setDirection(direction_t direction_t) {
        libtorrent_jni.dht_pkt_alert_direction_set(this.swigCPtr, this, direction_t.swigValue());
    }

    public direction_t getDirection() {
        return direction_t.swigToEnum(libtorrent_jni.dht_pkt_alert_direction_get(this.swigCPtr, this));
    }

    public udp_endpoint get_node() {
        return new udp_endpoint(libtorrent_jni.dht_pkt_alert_get_node(this.swigCPtr, this), true);
    }
}
