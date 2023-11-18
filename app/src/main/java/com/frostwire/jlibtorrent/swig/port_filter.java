package com.frostwire.jlibtorrent.swig;

public class port_filter {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public static final class access_flags {
        public static final access_flags blocked = new access_flags("blocked", libtorrent_jni.port_filter_blocked_get());
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

    protected port_filter(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(port_filter port_filter) {
        return port_filter == null ? 0 : port_filter.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_port_filter(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public void add_rule(int i, int i2, long j) {
        libtorrent_jni.port_filter_add_rule(this.swigCPtr, this, i, i2, j);
    }

    public long access(int i) {
        return libtorrent_jni.port_filter_access(this.swigCPtr, this, i);
    }

    public port_filter() {
        this(libtorrent_jni.new_port_filter(), true);
    }
}
