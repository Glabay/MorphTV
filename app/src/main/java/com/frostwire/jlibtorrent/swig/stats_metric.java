package com.frostwire.jlibtorrent.swig;

public class stats_metric {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public static final class metric_type_t {
        private static int swigNext;
        private static metric_type_t[] swigValues = new metric_type_t[]{type_counter, type_gauge};
        public static final metric_type_t type_counter = new metric_type_t("type_counter");
        public static final metric_type_t type_gauge = new metric_type_t("type_gauge");
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static metric_type_t swigToEnum(int i) {
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
            stringBuilder.append(metric_type_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private metric_type_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private metric_type_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private metric_type_t(String str, metric_type_t metric_type_t) {
            this.swigName = str;
            this.swigValue = metric_type_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected stats_metric(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(stats_metric stats_metric) {
        return stats_metric == null ? 0 : stats_metric.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_stats_metric(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void setValue_index(int i) {
        libtorrent_jni.stats_metric_value_index_set(this.swigCPtr, this, i);
    }

    public int getValue_index() {
        return libtorrent_jni.stats_metric_value_index_get(this.swigCPtr, this);
    }

    public void setType(metric_type_t metric_type_t) {
        libtorrent_jni.stats_metric_type_set(this.swigCPtr, this, metric_type_t.swigValue());
    }

    public metric_type_t getType() {
        return metric_type_t.swigToEnum(libtorrent_jni.stats_metric_type_get(this.swigCPtr, this));
    }

    public String get_name() {
        return libtorrent_jni.stats_metric_get_name(this.swigCPtr, this);
    }

    public stats_metric() {
        this(libtorrent_jni.new_stats_metric(), true);
    }
}
