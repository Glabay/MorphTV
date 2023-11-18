package com.frostwire.jlibtorrent.swig;

public class anonymous_mode_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.anonymous_mode_alert_alert_type_get();
    public static final int priority = libtorrent_jni.anonymous_mode_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.anonymous_mode_alert_static_category_get(), false);
    private transient long swigCPtr;

    public static final class kind_t {
        private static int swigNext;
        private static kind_t[] swigValues = new kind_t[]{tracker_not_anonymous};
        public static final kind_t tracker_not_anonymous = new kind_t("tracker_not_anonymous", libtorrent_jni.anonymous_mode_alert_tracker_not_anonymous_get());
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static kind_t swigToEnum(int i) {
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
            stringBuilder.append(kind_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private kind_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private kind_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private kind_t(String str, kind_t kind_t) {
            this.swigName = str;
            this.swigValue = kind_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected anonymous_mode_alert(long j, boolean z) {
        super(libtorrent_jni.anonymous_mode_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(anonymous_mode_alert anonymous_mode_alert) {
        return anonymous_mode_alert == null ? 0 : anonymous_mode_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_anonymous_mode_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.anonymous_mode_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.anonymous_mode_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.anonymous_mode_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.anonymous_mode_alert_message(this.swigCPtr, this);
    }

    public void setKind(int i) {
        libtorrent_jni.anonymous_mode_alert_kind_set(this.swigCPtr, this, i);
    }

    public int getKind() {
        return libtorrent_jni.anonymous_mode_alert_kind_get(this.swigCPtr, this);
    }

    public void setStr(String str) {
        libtorrent_jni.anonymous_mode_alert_str_set(this.swigCPtr, this, str);
    }

    public String getStr() {
        return libtorrent_jni.anonymous_mode_alert_str_get(this.swigCPtr, this);
    }
}
