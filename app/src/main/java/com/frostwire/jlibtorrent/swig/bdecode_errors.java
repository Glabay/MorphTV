package com.frostwire.jlibtorrent.swig;

public final class bdecode_errors {
    public static final bdecode_errors bdecode_no_error = new bdecode_errors("bdecode_no_error", libtorrent_jni.bdecode_no_error_get());
    public static final bdecode_errors depth_exceeded = new bdecode_errors("depth_exceeded");
    public static final bdecode_errors error_code_max = new bdecode_errors("error_code_max");
    public static final bdecode_errors expected_colon = new bdecode_errors("expected_colon");
    public static final bdecode_errors expected_digit = new bdecode_errors("expected_digit");
    public static final bdecode_errors expected_value = new bdecode_errors("expected_value");
    public static final bdecode_errors limit_exceeded = new bdecode_errors("limit_exceeded");
    public static final bdecode_errors overflow = new bdecode_errors("overflow");
    private static int swigNext;
    private static bdecode_errors[] swigValues = new bdecode_errors[]{bdecode_no_error, expected_digit, expected_colon, unexpected_eof, expected_value, depth_exceeded, limit_exceeded, overflow, error_code_max};
    public static final bdecode_errors unexpected_eof = new bdecode_errors("unexpected_eof");
    private final String swigName;
    private final int swigValue;

    public final int swigValue() {
        return this.swigValue;
    }

    public String toString() {
        return this.swigName;
    }

    public static bdecode_errors swigToEnum(int i) {
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
        stringBuilder.append(bdecode_errors.class);
        stringBuilder.append(" with value ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private bdecode_errors(String str) {
        this.swigName = str;
        str = swigNext;
        swigNext = str + 1;
        this.swigValue = str;
    }

    private bdecode_errors(String str, int i) {
        this.swigName = str;
        this.swigValue = i;
        swigNext = i + 1;
    }

    private bdecode_errors(String str, bdecode_errors bdecode_errors) {
        this.swigName = str;
        this.swigValue = bdecode_errors.swigValue;
        swigNext = this.swigValue + 1;
    }
}
