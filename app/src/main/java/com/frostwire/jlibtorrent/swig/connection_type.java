package com.frostwire.jlibtorrent.swig;

public final class connection_type {
    public static final connection_type bittorrent = new connection_type("bittorrent");
    public static final connection_type http_seed = new connection_type("http_seed");
    private static int swigNext;
    private static connection_type[] swigValues = new connection_type[]{bittorrent, url_seed, http_seed};
    public static final connection_type url_seed = new connection_type("url_seed");
    private final String swigName;
    private final int swigValue;

    public final int swigValue() {
        return this.swigValue;
    }

    public String toString() {
        return this.swigName;
    }

    public static connection_type swigToEnum(int i) {
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
        stringBuilder.append(connection_type.class);
        stringBuilder.append(" with value ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private connection_type(String str) {
        this.swigName = str;
        str = swigNext;
        swigNext = str + 1;
        this.swigValue = str;
    }

    private connection_type(String str, int i) {
        this.swigName = str;
        this.swigValue = i;
        swigNext = i + 1;
    }

    private connection_type(String str, connection_type connection_type) {
        this.swigName = str;
        this.swigValue = connection_type.swigValue;
        swigNext = this.swigValue + 1;
    }
}
