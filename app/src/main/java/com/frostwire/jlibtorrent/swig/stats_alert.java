package com.frostwire.jlibtorrent.swig;

public class stats_alert extends torrent_alert {
    public static final int alert_type = libtorrent_jni.stats_alert_alert_type_get();
    public static final int priority = libtorrent_jni.stats_alert_priority_get();
    public static final alert_category_t static_category = new alert_category_t(libtorrent_jni.stats_alert_static_category_get(), false);
    private transient long swigCPtr;

    public static final class stats_channel {
        public static final stats_channel download_ip_protocol = new stats_channel("download_ip_protocol", libtorrent_jni.stats_alert_download_ip_protocol_get());
        public static final stats_channel download_payload = new stats_channel("download_payload");
        public static final stats_channel download_protocol = new stats_channel("download_protocol");
        public static final stats_channel num_channels = new stats_channel("num_channels", libtorrent_jni.stats_alert_num_channels_get());
        private static int swigNext;
        private static stats_channel[] swigValues = new stats_channel[]{upload_payload, upload_protocol, download_payload, download_protocol, upload_ip_protocol, download_ip_protocol, num_channels};
        public static final stats_channel upload_ip_protocol = new stats_channel("upload_ip_protocol");
        public static final stats_channel upload_payload = new stats_channel("upload_payload");
        public static final stats_channel upload_protocol = new stats_channel("upload_protocol");
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static stats_channel swigToEnum(int i) {
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
            stringBuilder.append(stats_channel.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private stats_channel(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private stats_channel(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private stats_channel(String str, stats_channel stats_channel) {
            this.swigName = str;
            this.swigValue = stats_channel.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected stats_alert(long j, boolean z) {
        super(libtorrent_jni.stats_alert_SWIGUpcast(j), z);
        this.swigCPtr = j;
    }

    protected static long getCPtr(stats_alert stats_alert) {
        return stats_alert == null ? 0 : stats_alert.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_stats_alert(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
        super.delete();
    }

    public int type() {
        return libtorrent_jni.stats_alert_type(this.swigCPtr, this);
    }

    public alert_category_t category() {
        return new alert_category_t(libtorrent_jni.stats_alert_category(this.swigCPtr, this), true);
    }

    public String what() {
        return libtorrent_jni.stats_alert_what(this.swigCPtr, this);
    }

    public String message() {
        return libtorrent_jni.stats_alert_message(this.swigCPtr, this);
    }

    public int getInterval() {
        return libtorrent_jni.stats_alert_interval_get(this.swigCPtr, this);
    }

    public int get_transferred(int i) {
        return libtorrent_jni.stats_alert_get_transferred(this.swigCPtr, this, i);
    }
}
