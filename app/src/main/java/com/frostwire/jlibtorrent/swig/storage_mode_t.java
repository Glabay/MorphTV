package com.frostwire.jlibtorrent.swig;

public final class storage_mode_t {
    public static final storage_mode_t storage_mode_allocate = new storage_mode_t("storage_mode_allocate");
    public static final storage_mode_t storage_mode_sparse = new storage_mode_t("storage_mode_sparse");
    private static int swigNext;
    private static storage_mode_t[] swigValues = new storage_mode_t[]{storage_mode_allocate, storage_mode_sparse};
    private final String swigName;
    private final int swigValue;

    public final int swigValue() {
        return this.swigValue;
    }

    public String toString() {
        return this.swigName;
    }

    public static storage_mode_t swigToEnum(int i) {
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
        stringBuilder.append(storage_mode_t.class);
        stringBuilder.append(" with value ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private storage_mode_t(String str) {
        this.swigName = str;
        str = swigNext;
        swigNext = str + 1;
        this.swigValue = str;
    }

    private storage_mode_t(String str, int i) {
        this.swigName = str;
        this.swigValue = i;
        swigNext = i + 1;
    }

    private storage_mode_t(String str, storage_mode_t storage_mode_t) {
        this.swigName = str;
        this.swigValue = storage_mode_t.swigValue;
        swigNext = this.swigValue + 1;
    }
}
