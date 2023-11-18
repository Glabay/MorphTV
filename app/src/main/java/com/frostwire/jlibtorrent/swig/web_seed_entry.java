package com.frostwire.jlibtorrent.swig;

public class web_seed_entry {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public static final class type_t {
        public static final type_t http_seed = new type_t("http_seed");
        private static int swigNext;
        private static type_t[] swigValues = new type_t[]{url_seed, http_seed};
        public static final type_t url_seed = new type_t("url_seed");
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static type_t swigToEnum(int i) {
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
            stringBuilder.append(type_t.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private type_t(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private type_t(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private type_t(String str, type_t type_t) {
            this.swigName = str;
            this.swigValue = type_t.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected web_seed_entry(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(web_seed_entry web_seed_entry) {
        return web_seed_entry == null ? 0 : web_seed_entry.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_web_seed_entry(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public web_seed_entry(String str, type_t type_t, String str2, string_string_pair_vector string_string_pair_vector) {
        this(libtorrent_jni.new_web_seed_entry__SWIG_0(str, type_t.swigValue(), str2, string_string_pair_vector.getCPtr(string_string_pair_vector), string_string_pair_vector), true);
    }

    public web_seed_entry(String str, type_t type_t, String str2) {
        this(libtorrent_jni.new_web_seed_entry__SWIG_1(str, type_t.swigValue(), str2), true);
    }

    public web_seed_entry(String str, type_t type_t) {
        this(libtorrent_jni.new_web_seed_entry__SWIG_2(str, type_t.swigValue()), true);
    }

    public boolean op_eq(web_seed_entry web_seed_entry) {
        return libtorrent_jni.web_seed_entry_op_eq(this.swigCPtr, this, getCPtr(web_seed_entry), web_seed_entry);
    }

    public boolean op_lt(web_seed_entry web_seed_entry) {
        return libtorrent_jni.web_seed_entry_op_lt(this.swigCPtr, this, getCPtr(web_seed_entry), web_seed_entry);
    }

    public void setUrl(String str) {
        libtorrent_jni.web_seed_entry_url_set(this.swigCPtr, this, str);
    }

    public String getUrl() {
        return libtorrent_jni.web_seed_entry_url_get(this.swigCPtr, this);
    }

    public void setAuth(String str) {
        libtorrent_jni.web_seed_entry_auth_set(this.swigCPtr, this, str);
    }

    public String getAuth() {
        return libtorrent_jni.web_seed_entry_auth_get(this.swigCPtr, this);
    }

    public void setExtra_headers(string_string_pair_vector string_string_pair_vector) {
        libtorrent_jni.web_seed_entry_extra_headers_set(this.swigCPtr, this, string_string_pair_vector.getCPtr(string_string_pair_vector), string_string_pair_vector);
    }

    public string_string_pair_vector getExtra_headers() {
        long web_seed_entry_extra_headers_get = libtorrent_jni.web_seed_entry_extra_headers_get(this.swigCPtr, this);
        if (web_seed_entry_extra_headers_get == 0) {
            return null;
        }
        return new string_string_pair_vector(web_seed_entry_extra_headers_get, false);
    }

    public void setType(short s) {
        libtorrent_jni.web_seed_entry_type_set(this.swigCPtr, this, s);
    }

    public short getType() {
        return libtorrent_jni.web_seed_entry_type_get(this.swigCPtr, this);
    }
}
