package com.frostwire.jlibtorrent.swig;

public class dht_log_alert extends alert {
    public static final int alert_type = libtorrent_jni.dht_log_alert_alert_type_get();
    public static final int priority = libtorrent_jni.dht_log_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.dht_log_alert_static_category_get(), false);
    private transient long swigCPtr;

    public static final class dht_module_t {
        public static final dht_module_t node = new dht_module_t("node");
        public static final dht_module_t routing_table = new dht_module_t("routing_table");
        public static final dht_module_t rpc_manager = new dht_module_t("rpc_manager");
        private static int swigNext;
        private static dht_module_t[] swigValues = new dht_module_t[]{tracker, node, routing_table, rpc_manager, traversal};
        public static final dht_module_t tracker = new dht_module_t("tracker");
        public static final dht_module_t traversal = new dht_module_t("traversal");
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static dht_module_t swigToEnum(int i) {
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
            stringBuilder.append(dht_module_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private dht_module_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private dht_module_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private dht_module_t(String str, dht_module_t dht_module_t) {
            this.swigName = str;
            this.swigValue = dht_module_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected dht_log_alert(long j, boolean z) {
        super(libtorrent_jni.dht_log_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(dht_log_alert dht_log_alert) {
        return dht_log_alert == null ? 0 : dht_log_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_dht_log_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.dht_log_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.dht_log_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.dht_log_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.dht_log_alert_message(this.swigCPtr, this);
    }

    public String log_message() {
        return libtorrent_jni.dht_log_alert_log_message(this.swigCPtr, this);
    }

    public void setModule(dht_module_t dht_module_t) {
        libtorrent_jni.dht_log_alert_module_set(this.swigCPtr, this, dht_module_t.swigValue());
    }

    public dht_module_t getModule() {
        return dht_module_t.swigToEnum(libtorrent_jni.dht_log_alert_module_get(this.swigCPtr, this));
    }
}
