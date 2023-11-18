package com.frostwire.jlibtorrent.swig;

public final class socket_type_t {
    public static final socket_type_t i2p = new socket_type_t("i2p");
    public static final socket_type_t socks5 = new socket_type_t("socks5");
    private static int swigNext;
    private static socket_type_t[] swigValues = new socket_type_t[]{tcp, tcp_ssl, udp, i2p, socks5, utp_ssl};
    public static final socket_type_t tcp = new socket_type_t("tcp");
    public static final socket_type_t tcp_ssl = new socket_type_t("tcp_ssl");
    public static final socket_type_t udp = new socket_type_t("udp");
    public static final socket_type_t utp_ssl = new socket_type_t("utp_ssl");
    private final String swigName;
    private final int swigValue;

    public final int swigValue() {
        return this.swigValue;
    }

    public String toString() {
        return this.swigName;
    }

    public static socket_type_t swigToEnum(int i) {
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
        stringBuilder.append(socket_type_t.class);
        stringBuilder.append(" with value ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private socket_type_t(String str) {
        this.swigName = str;
        str = swigNext;
        swigNext = str + 1;
        this.swigValue = str;
    }

    private socket_type_t(String str, int i) {
        this.swigName = str;
        this.swigValue = i;
        swigNext = i + 1;
    }

    private socket_type_t(String str, socket_type_t socket_type_t) {
        this.swigName = str;
        this.swigValue = socket_type_t.swigValue;
        swigNext = this.swigValue + 1;
    }
}
