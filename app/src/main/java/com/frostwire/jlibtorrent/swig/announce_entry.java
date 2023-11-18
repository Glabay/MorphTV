package com.frostwire.jlibtorrent.swig;

public class announce_entry {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public static final class tracker_source {
        public static final tracker_source source_client = new tracker_source("source_client", libtorrent_jni.announce_entry_source_client_get());
        public static final tracker_source source_magnet_link = new tracker_source("source_magnet_link", libtorrent_jni.announce_entry_source_magnet_link_get());
        public static final tracker_source source_tex = new tracker_source("source_tex", libtorrent_jni.announce_entry_source_tex_get());
        public static final tracker_source source_torrent = new tracker_source("source_torrent", libtorrent_jni.announce_entry_source_torrent_get());
        private static int swigNext;
        private static tracker_source[] swigValues = new tracker_source[]{source_torrent, source_client, source_magnet_link, source_tex};
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static tracker_source swigToEnum(int i) {
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
            stringBuilder.append(tracker_source.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private tracker_source(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private tracker_source(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private tracker_source(String str, tracker_source tracker_source) {
            this.swigName = str;
            this.swigValue = tracker_source.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected announce_entry(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(announce_entry announce_entry) {
        return announce_entry == null ? 0 : announce_entry.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_announce_entry(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public announce_entry() {
        this(libtorrent_jni.new_announce_entry__SWIG_0(), true);
    }

    public announce_entry(announce_entry announce_entry) {
        this(libtorrent_jni.new_announce_entry__SWIG_1(getCPtr(announce_entry), announce_entry), true);
    }

    public void setUrl(String str) {
        libtorrent_jni.announce_entry_url_set(this.swigCPtr, this, str);
    }

    public String getUrl() {
        return libtorrent_jni.announce_entry_url_get(this.swigCPtr, this);
    }

    public void setTrackerid(String str) {
        libtorrent_jni.announce_entry_trackerid_set(this.swigCPtr, this, str);
    }

    public String getTrackerid() {
        return libtorrent_jni.announce_entry_trackerid_get(this.swigCPtr, this);
    }

    public void setEndpoints(announce_endpoint_vector announce_endpoint_vector) {
        libtorrent_jni.announce_entry_endpoints_set(this.swigCPtr, this, announce_endpoint_vector.getCPtr(announce_endpoint_vector), announce_endpoint_vector);
    }

    public announce_endpoint_vector getEndpoints() {
        long announce_entry_endpoints_get = libtorrent_jni.announce_entry_endpoints_get(this.swigCPtr, this);
        if (announce_entry_endpoints_get == 0) {
            return null;
        }
        return new announce_endpoint_vector(announce_entry_endpoints_get, false);
    }

    public void setTier(short s) {
        libtorrent_jni.announce_entry_tier_set(this.swigCPtr, this, s);
    }

    public short getTier() {
        return libtorrent_jni.announce_entry_tier_get(this.swigCPtr, this);
    }

    public void setFail_limit(short s) {
        libtorrent_jni.announce_entry_fail_limit_set(this.swigCPtr, this, s);
    }

    public short getFail_limit() {
        return libtorrent_jni.announce_entry_fail_limit_get(this.swigCPtr, this);
    }

    public void setSource(short s) {
        libtorrent_jni.announce_entry_source_set(this.swigCPtr, this, s);
    }

    public short getSource() {
        return libtorrent_jni.announce_entry_source_get(this.swigCPtr, this);
    }

    public void setVerified(boolean z) {
        libtorrent_jni.announce_entry_verified_set(this.swigCPtr, this, z);
    }

    public boolean getVerified() {
        return libtorrent_jni.announce_entry_verified_get(this.swigCPtr, this);
    }

    public void reset() {
        libtorrent_jni.announce_entry_reset(this.swigCPtr, this);
    }

    public void trim() {
        libtorrent_jni.announce_entry_trim(this.swigCPtr, this);
    }

    public announce_entry(String str) {
        this(libtorrent_jni.new_announce_entry__SWIG_2(str), true);
    }
}
