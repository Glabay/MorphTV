package com.frostwire.jlibtorrent.swig;

public final class move_flags_t {
    public static final move_flags_t always_replace_files = new move_flags_t("always_replace_files");
    public static final move_flags_t dont_replace = new move_flags_t("dont_replace");
    public static final move_flags_t fail_if_exist = new move_flags_t("fail_if_exist");
    private static int swigNext;
    private static move_flags_t[] swigValues = new move_flags_t[]{always_replace_files, fail_if_exist, dont_replace};
    private final String swigName;
    private final int swigValue;

    public final int swigValue() {
        return this.swigValue;
    }

    public String toString() {
        return this.swigName;
    }

    public static move_flags_t swigToEnum(int i) {
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
        stringBuilder.append(move_flags_t.class);
        stringBuilder.append(" with value ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private move_flags_t(String str) {
        this.swigName = str;
        str = swigNext;
        swigNext = str + 1;
        this.swigValue = str;
    }

    private move_flags_t(String str, int i) {
        this.swigName = str;
        this.swigValue = i;
        swigNext = i + 1;
    }

    private move_flags_t(String str, move_flags_t move_flags_t) {
        this.swigName = str;
        this.swigValue = move_flags_t.swigValue;
        swigNext = this.swigValue + 1;
    }
}
