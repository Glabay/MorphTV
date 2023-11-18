package com.frostwire.jlibtorrent.swig;

public class portmap_alert extends alert {
    public static final int alert_type = libtorrent_jni.portmap_alert_alert_type_get();
    public static final int priority = libtorrent_jni.portmap_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.portmap_alert_static_category_get(), false);
    private transient long swigCPtr;

    public static final class protocol_t {
        private static int swigNext;
        private static protocol_t[] swigValues = new protocol_t[]{tcp, udp};
        public static final protocol_t tcp = new protocol_t("tcp");
        public static final protocol_t udp = new protocol_t("udp");
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static protocol_t swigToEnum(int i) {
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
            stringBuilder.append(protocol_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private protocol_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private protocol_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private protocol_t(String str, protocol_t protocol_t) {
            this.swigName = str;
            this.swigValue = protocol_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected portmap_alert(long j, boolean z) {
        super(libtorrent_jni.portmap_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(portmap_alert portmap_alert) {
        return portmap_alert == null ? 0 : portmap_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_portmap_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.portmap_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.portmap_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.portmap_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.portmap_alert_message(this.swigCPtr, this);
    }

    public int getMapping() {
        return libtorrent_jni.portmap_alert_mapping_get(this.swigCPtr, this);
    }

    public int getExternal_port() {
        return libtorrent_jni.portmap_alert_external_port_get(this.swigCPtr, this);
    }

    public int getMap_type() {
        return libtorrent_jni.portmap_alert_map_type_get(this.swigCPtr, this);
    }

    public int getProtocol() {
        return libtorrent_jni.portmap_alert_protocol_get(this.swigCPtr, this);
    }
}
