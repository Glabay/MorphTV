package com.frostwire.jlibtorrent.swig;

public class entry {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public static final class data_type {
        public static final data_type dictionary_t = new data_type("dictionary_t");
        public static final data_type int_t = new data_type("int_t");
        public static final data_type list_t = new data_type("list_t");
        public static final data_type preformatted_t = new data_type("preformatted_t");
        public static final data_type string_t = new data_type("string_t");
        private static int swigNext;
        private static data_type[] swigValues = new data_type[]{int_t, string_t, list_t, dictionary_t, undefined_t, preformatted_t};
        public static final data_type undefined_t = new data_type("undefined_t");
        private final String swigName;
        private final int swigValue;

        public final int swigValue() {
            return this.swigValue;
        }

        public String toString() {
            return this.swigName;
        }

        public static data_type swigToEnum(int i) {
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
            stringBuilder.append(data_type.class);
            stringBuilder.append(" with value ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        private data_type(String str) {
            this.swigName = str;
            str = swigNext;
            swigNext = str + 1;
            this.swigValue = str;
        }

        private data_type(String str, int i) {
            this.swigName = str;
            this.swigValue = i;
            swigNext = i + 1;
        }

        private data_type(String str, data_type data_type) {
            this.swigName = str;
            this.swigValue = data_type.swigValue;
            swigNext = this.swigValue + 1;
        }
    }

    protected entry(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(entry entry) {
        return entry == null ? 0 : entry.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_entry(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public data_type type() {
        return data_type.swigToEnum(libtorrent_jni.entry_type(this.swigCPtr, this));
    }

    public entry(string_entry_map string_entry_map) {
        this(libtorrent_jni.new_entry__SWIG_0(string_entry_map.getCPtr(string_entry_map), string_entry_map), true);
    }

    public entry(byte_const_span byte_const_span) {
        this(libtorrent_jni.new_entry__SWIG_1(byte_const_span.getCPtr(byte_const_span), byte_const_span), true);
    }

    public entry(String str) {
        this(libtorrent_jni.new_entry__SWIG_2(str), true);
    }

    public entry(entry_vector entry_vector) {
        this(libtorrent_jni.new_entry__SWIG_3(entry_vector.getCPtr(entry_vector), entry_vector), true);
    }

    public entry(long j) {
        this(libtorrent_jni.new_entry__SWIG_4(j), true);
    }

    public entry(data_type data_type) {
        this(libtorrent_jni.new_entry__SWIG_5(data_type.swigValue()), true);
    }

    public entry(entry entry) {
        this(libtorrent_jni.new_entry__SWIG_6(getCPtr(entry), entry), true);
    }

    public entry() {
        this(libtorrent_jni.new_entry__SWIG_7(), true);
    }

    public long integer() {
        return libtorrent_jni.entry_integer(this.swigCPtr, this);
    }

    public String string() {
        return libtorrent_jni.entry_string(this.swigCPtr, this);
    }

    public entry_vector list() {
        return new entry_vector(libtorrent_jni.entry_list(this.swigCPtr, this), false);
    }

    public string_entry_map dict() {
        return new string_entry_map(libtorrent_jni.entry_dict(this.swigCPtr, this), false);
    }

    public entry find_key(string_view string_view) {
        long entry_find_key = libtorrent_jni.entry_find_key(this.swigCPtr, this, string_view.getCPtr(string_view), string_view);
        if (entry_find_key == 0) {
            return null;
        }
        return new entry(entry_find_key, false);
    }

    public String to_string() {
        return libtorrent_jni.entry_to_string(this.swigCPtr, this);
    }

    public entry get(String str) {
        return new entry(libtorrent_jni.entry_get(this.swigCPtr, this, str), null);
    }

    public void set(String str, String str2) {
        libtorrent_jni.entry_set__SWIG_0(this.swigCPtr, this, str, str2);
    }

    public void set(String str, byte_vector byte_vector) {
        libtorrent_jni.entry_set__SWIG_1(this.swigCPtr, this, str, byte_vector.getCPtr(byte_vector), byte_vector);
    }

    public void set(String str, long j) {
        libtorrent_jni.entry_set__SWIG_2(this.swigCPtr, this, str, j);
    }

    public void set(String str, entry entry) {
        libtorrent_jni.entry_set__SWIG_3(this.swigCPtr, this, str, getCPtr(entry), entry);
    }

    public byte_vector string_bytes() {
        return new byte_vector(libtorrent_jni.entry_string_bytes(this.swigCPtr, this), true);
    }

    public byte_vector preformatted_bytes() {
        return new byte_vector(libtorrent_jni.entry_preformatted_bytes(this.swigCPtr, this), true);
    }

    public byte_vector bencode() {
        return new byte_vector(libtorrent_jni.entry_bencode(this.swigCPtr, this), true);
    }

    public static entry from_string_bytes(byte_vector byte_vector) {
        return new entry(libtorrent_jni.entry_from_string_bytes(byte_vector.getCPtr(byte_vector), byte_vector), true);
    }

    public static entry from_preformatted_bytes(byte_vector byte_vector) {
        return new entry(libtorrent_jni.entry_from_preformatted_bytes(byte_vector.getCPtr(byte_vector), byte_vector), true);
    }

    public static entry bdecode(byte_vector byte_vector) {
        return new entry(libtorrent_jni.entry_bdecode(byte_vector.getCPtr(byte_vector), byte_vector), true);
    }
}
