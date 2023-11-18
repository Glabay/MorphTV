package com.frostwire.jlibtorrent.swig;

public class bdecode_node {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public static final class type_t {
        public static final type_t dict_t = new type_t("dict_t");
        public static final type_t int_t = new type_t("int_t");
        public static final type_t list_t = new type_t("list_t");
        public static final type_t none_t = new type_t("none_t");
        public static final type_t string_t = new type_t("string_t");
        private static int swigNext;
        private static type_t[] swigValues = new type_t[]{none_t, dict_t, list_t, string_t, int_t};
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

    protected bdecode_node(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(bdecode_node bdecode_node) {
        return bdecode_node == null ? 0 : bdecode_node.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (this.swigCPtr != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                libtorrent_jni.delete_bdecode_node(this.swigCPtr);
            }
            this.swigCPtr = 0;
        }
    }

    public bdecode_node() {
        this(libtorrent_jni.new_bdecode_node__SWIG_0(), true);
    }

    public bdecode_node(bdecode_node bdecode_node) {
        this(libtorrent_jni.new_bdecode_node__SWIG_1(getCPtr(bdecode_node), bdecode_node), true);
    }

    public type_t type() {
        return type_t.swigToEnum(libtorrent_jni.bdecode_node_type(this.swigCPtr, this));
    }

    public boolean op_bool() {
        return libtorrent_jni.bdecode_node_op_bool(this.swigCPtr, this);
    }

    public bdecode_node list_at(int i) {
        return new bdecode_node(libtorrent_jni.bdecode_node_list_at(this.swigCPtr, this, i), 1);
    }

    public long list_int_value_at(int i, long j) {
        return libtorrent_jni.bdecode_node_list_int_value_at__SWIG_0(this.swigCPtr, this, i, j);
    }

    public long list_int_value_at(int i) {
        return libtorrent_jni.bdecode_node_list_int_value_at__SWIG_1(this.swigCPtr, this, i);
    }

    public int list_size() {
        return libtorrent_jni.bdecode_node_list_size(this.swigCPtr, this);
    }

    public string_view_bdecode_node_pair dict_at(int i) {
        return new string_view_bdecode_node_pair(libtorrent_jni.bdecode_node_dict_at(this.swigCPtr, this, i), (boolean) 1);
    }

    public int dict_size() {
        return libtorrent_jni.bdecode_node_dict_size(this.swigCPtr, this);
    }

    public long int_value() {
        return libtorrent_jni.bdecode_node_int_value(this.swigCPtr, this);
    }

    public int string_length() {
        return libtorrent_jni.bdecode_node_string_length(this.swigCPtr, this);
    }

    public void clear() {
        libtorrent_jni.bdecode_node_clear(this.swigCPtr, this);
    }

    public String list_string_value_at_s(int i, String str) {
        return libtorrent_jni.bdecode_node_list_string_value_at_s__SWIG_0(this.swigCPtr, this, i, str);
    }

    public String list_string_value_at_s(int i) {
        return libtorrent_jni.bdecode_node_list_string_value_at_s__SWIG_1(this.swigCPtr, this, i);
    }

    public bdecode_node dict_find_s(String str) {
        return new bdecode_node(libtorrent_jni.bdecode_node_dict_find_s(this.swigCPtr, this, str), true);
    }

    public bdecode_node dict_find_dict_s(String str) {
        return new bdecode_node(libtorrent_jni.bdecode_node_dict_find_dict_s(this.swigCPtr, this, str), true);
    }

    public bdecode_node dict_find_list_s(String str) {
        return new bdecode_node(libtorrent_jni.bdecode_node_dict_find_list_s(this.swigCPtr, this, str), true);
    }

    public bdecode_node dict_find_string_s(String str) {
        return new bdecode_node(libtorrent_jni.bdecode_node_dict_find_string_s(this.swigCPtr, this, str), true);
    }

    public bdecode_node dict_find_int_s(String str) {
        return new bdecode_node(libtorrent_jni.bdecode_node_dict_find_int_s(this.swigCPtr, this, str), true);
    }

    public String dict_find_string_value_s(String str, String str2) {
        return libtorrent_jni.bdecode_node_dict_find_string_value_s__SWIG_0(this.swigCPtr, this, str, str2);
    }

    public String dict_find_string_value_s(String str) {
        return libtorrent_jni.bdecode_node_dict_find_string_value_s__SWIG_1(this.swigCPtr, this, str);
    }

    public long dict_find_int_value_s(String str, long j) {
        return libtorrent_jni.bdecode_node_dict_find_int_value_s__SWIG_0(this.swigCPtr, this, str, j);
    }

    public long dict_find_int_value_s(String str) {
        return libtorrent_jni.bdecode_node_dict_find_int_value_s__SWIG_1(this.swigCPtr, this, str);
    }

    public String string_value_s() {
        return libtorrent_jni.bdecode_node_string_value_s(this.swigCPtr, this);
    }

    public static String to_string(bdecode_node bdecode_node, boolean z, int i) {
        return libtorrent_jni.bdecode_node_to_string(getCPtr(bdecode_node), bdecode_node, z, i);
    }

    public static int bdecode(byte_vector byte_vector, bdecode_node bdecode_node, error_code error_code) {
        return libtorrent_jni.bdecode_node_bdecode(byte_vector.getCPtr(byte_vector), byte_vector, getCPtr(bdecode_node), bdecode_node, error_code.getCPtr(error_code), error_code);
    }
}
