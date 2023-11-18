package com.frostwire.jlibtorrent.swig;

public class performance_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.performance_alert_alert_type_get();
    public static final int priority = libtorrent_jni.performance_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.performance_alert_static_category_get(), false);
    private transient long swigCPtr;

    public static final class performance_warning_t {
        public static final performance_warning_t aio_limit_reached = new performance_warning_t("aio_limit_reached");
        public static final performance_warning_t bittyrant_with_no_uplimit = new performance_warning_t("bittyrant_with_no_uplimit");
        public static final performance_warning_t download_limit_too_low = new performance_warning_t("download_limit_too_low");
        public static final performance_warning_t num_warnings = new performance_warning_t("num_warnings");
        public static final performance_warning_t outstanding_disk_buffer_limit_reached = new performance_warning_t("outstanding_disk_buffer_limit_reached");
        public static final performance_warning_t outstanding_request_limit_reached = new performance_warning_t("outstanding_request_limit_reached");
        public static final performance_warning_t send_buffer_watermark_too_low = new performance_warning_t("send_buffer_watermark_too_low");
        private static int swigNext;
        private static performance_warning_t[] swigValues = new performance_warning_t[]{outstanding_disk_buffer_limit_reached, outstanding_request_limit_reached, upload_limit_too_low, download_limit_too_low, send_buffer_watermark_too_low, too_many_optimistic_unchoke_slots, too_high_disk_queue_limit, aio_limit_reached, bittyrant_with_no_uplimit, too_few_outgoing_ports, too_few_file_descriptors, num_warnings};
        public static final performance_warning_t too_few_file_descriptors = new performance_warning_t("too_few_file_descriptors");
        public static final performance_warning_t too_few_outgoing_ports = new performance_warning_t("too_few_outgoing_ports");
        public static final performance_warning_t too_high_disk_queue_limit = new performance_warning_t("too_high_disk_queue_limit");
        public static final performance_warning_t too_many_optimistic_unchoke_slots = new performance_warning_t("too_many_optimistic_unchoke_slots");
        public static final performance_warning_t upload_limit_too_low = new performance_warning_t("upload_limit_too_low");
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static performance_warning_t swigToEnum(int i) {
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
            stringBuilder.append(performance_warning_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private performance_warning_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private performance_warning_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private performance_warning_t(String str, performance_warning_t performance_warning_t) {
            this.swigName = str;
            this.swigValue = performance_warning_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected performance_alert(long j, boolean z) {
        super(libtorrent_jni.performance_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(performance_alert performance_alert) {
        return performance_alert == null ? 0 : performance_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_performance_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.performance_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.performance_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.performance_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.performance_alert_message(this.swigCPtr, this);
    }

    public performance_warning_t getWarning_code() {
        return performance_warning_t.swigToEnum(libtorrent_jni.performance_alert_warning_code_get(this.swigCPtr, this));
    }
}
