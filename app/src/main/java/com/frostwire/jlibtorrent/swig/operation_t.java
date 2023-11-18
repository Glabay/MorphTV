package com.frostwire.jlibtorrent.swig;

public final class operation_t {
    public static final operation_t alloc_cache_piece = new operation_t("alloc_cache_piece");
    public static final operation_t alloc_recvbuf = new operation_t("alloc_recvbuf");
    public static final operation_t alloc_sndbuf = new operation_t("alloc_sndbuf");
    public static final operation_t available = new operation_t("available");
    public static final operation_t bittorrent = new operation_t("bittorrent");
    public static final operation_t check_resume = new operation_t("check_resume");
    public static final operation_t connect = new operation_t("connect");
    public static final operation_t encryption = new operation_t("encryption");
    public static final operation_t enum_if = new operation_t("enum_if");
    public static final operation_t exception = new operation_t("exception");
    public static final operation_t file = new operation_t("file");
    public static final operation_t file_copy = new operation_t("file_copy");
    public static final operation_t file_fallocate = new operation_t("file_fallocate");
    public static final operation_t file_hard_link = new operation_t("file_hard_link");
    public static final operation_t file_open = new operation_t("file_open");
    public static final operation_t file_read = new operation_t("file_read");
    public static final operation_t file_remove = new operation_t("file_remove");
    public static final operation_t file_rename = new operation_t("file_rename");
    public static final operation_t file_stat = new operation_t("file_stat");
    public static final operation_t file_write = new operation_t("file_write");
    public static final operation_t get_interface = new operation_t("get_interface");
    public static final operation_t getname = new operation_t("getname");
    public static final operation_t getpeername = new operation_t("getpeername");
    public static final operation_t hostname_lookup = new operation_t("hostname_lookup");
    public static final operation_t iocontrol = new operation_t("iocontrol");
    public static final operation_t mkdir = new operation_t("mkdir");
    public static final operation_t parse_address = new operation_t("parse_address");
    public static final operation_t partfile_move = new operation_t("partfile_move");
    public static final operation_t partfile_read = new operation_t("partfile_read");
    public static final operation_t partfile_write = new operation_t("partfile_write");
    public static final operation_t sock_accept = new operation_t("sock_accept");
    public static final operation_t sock_bind = new operation_t("sock_bind");
    public static final operation_t sock_bind_to_device = new operation_t("sock_bind_to_device");
    public static final operation_t sock_listen = new operation_t("sock_listen");
    public static final operation_t sock_open = new operation_t("sock_open");
    public static final operation_t sock_read = new operation_t("sock_read");
    public static final operation_t sock_write = new operation_t("sock_write");
    public static final operation_t ssl_handshake = new operation_t("ssl_handshake");
    private static int swigNext;
    private static operation_t[] swigValues = new operation_t[]{unknown, bittorrent, iocontrol, getpeername, getname, alloc_recvbuf, alloc_sndbuf, file_write, file_read, file, sock_write, sock_read, sock_open, sock_bind, available, encryption, connect, ssl_handshake, get_interface, sock_listen, sock_bind_to_device, sock_accept, parse_address, enum_if, file_stat, file_copy, file_fallocate, file_hard_link, file_remove, file_rename, file_open, mkdir, check_resume, exception, alloc_cache_piece, partfile_move, partfile_read, partfile_write, hostname_lookup};
    public static final operation_t unknown = new operation_t("unknown");
    private final String swigName;
    private final int swigValue;

    public final int swigValue() {
        return this.swigValue;
    }

    public String toString() {
        return this.swigName;
    }

    public static operation_t swigToEnum(int i) {
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
        stringBuilder.append(operation_t.class);
        stringBuilder.append(" with value ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private operation_t(String str) {
        this.swigName = str;
        str = swigNext;
        swigNext = str + 1;
        this.swigValue = str;
    }

    private operation_t(String str, int i) {
        this.swigName = str;
        this.swigValue = i;
        swigNext = i + 1;
    }

    private operation_t(String str, operation_t operation_t) {
        this.swigName = str;
        this.swigValue = operation_t.swigValue;
        swigNext = this.swigValue + 1;
    }
}
