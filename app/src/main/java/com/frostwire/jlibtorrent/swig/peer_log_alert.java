package com.frostwire.jlibtorrent.swig;

public class peer_log_alert extends peer_alert {
    public static final int alert_type = libtorrent_jni.peer_log_alert_alert_type_get();
    public static final int priority = libtorrent_jni.peer_log_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.peer_log_alert_static_category_get(), false);
    private transient long swigCPtr;

    public static final class direction_t {
        public static final direction_t incoming = new direction_t("incoming");
        public static final direction_t incoming_message = new direction_t("incoming_message");
        public static final direction_t info = new direction_t("info");
        public static final direction_t outgoing = new direction_t("outgoing");
        public static final direction_t outgoing_message = new direction_t("outgoing_message");
        private static int swigNext;
        private static direction_t[] swigValues = new direction_t[]{incoming_message, outgoing_message, incoming, outgoing, info};
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

    protected peer_log_alert(long j, boolean z) {
        super(libtorrent_jni.peer_log_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(peer_log_alert peer_log_alert) {
        return peer_log_alert == null ? 0 : peer_log_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_peer_log_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.peer_log_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.peer_log_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.peer_log_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.peer_log_alert_message(this.swigCPtr, this);
    }

    public void setDirection(direction_t direction_t) {
        libtorrent_jni.peer_log_alert_direction_set(this.swigCPtr, this, direction_t.swigValue());
    }

    public direction_t getDirection() {
        return direction_t.swigToEnum(libtorrent_jni.peer_log_alert_direction_get(this.swigCPtr, this));
    }

    public String log_message() {
        return libtorrent_jni.peer_log_alert_log_message(this.swigCPtr, this);
    }

    public String get_event_type() {
        return libtorrent_jni.peer_log_alert_get_event_type(this.swigCPtr, this);
    }
}
