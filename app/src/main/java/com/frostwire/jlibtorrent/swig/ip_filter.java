package com.frostwire.jlibtorrent.swig;

public class ip_filter {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public static final class access_flags {
        public static final access_flags blocked = new access_flags("blocked", libtorrent_jni.ip_filter_blocked_get());
        private static int swigNext;
        private static access_flags[] swigValues = new access_flags[]{blocked};
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static access_flags swigToEnum(int i) {
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
            stringBuilder.append(access_flags.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private access_flags(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private access_flags(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private access_flags(String str, access_flags access_flags) {
            this.swigName = str;
            this.swigValue = access_flags.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected ip_filter(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(ip_filter ip_filter) {
        return ip_filter == null ? 0 : ip_filter.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_ip_filter(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void add_rule(address address, address address2, long j) {
        libtorrent_jni.ip_filter_add_rule(this.swigCPtr, this, address.getCPtr(address), address, address.getCPtr(address2), address2, j);
    }

    public long access(address address) {
        return libtorrent_jni.ip_filter_access(this.swigCPtr, this, address.getCPtr(address), address);
    }

    public ip_filter() {
        this(libtorrent_jni.new_ip_filter(), true);
    }
}
