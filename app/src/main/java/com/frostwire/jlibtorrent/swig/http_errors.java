package com.frostwire.jlibtorrent.swig;

public final class http_errors {
    public static final http_errors accepted = new http_errors("accepted", libtorrent_jni.accepted_get());
    public static final http_errors bad_gateway = new http_errors("bad_gateway", libtorrent_jni.bad_gateway_get());
    public static final http_errors bad_request = new http_errors("bad_request", libtorrent_jni.bad_request_get());
    public static final http_errors cont = new http_errors("cont", libtorrent_jni.cont_get());
    public static final http_errors created = new http_errors("created", libtorrent_jni.created_get());
    public static final http_errors forbidden = new http_errors("forbidden", libtorrent_jni.forbidden_get());
    public static final http_errors internal_server_error = new http_errors("internal_server_error", libtorrent_jni.internal_server_error_get());
    public static final http_errors moved_permanently = new http_errors("moved_permanently", libtorrent_jni.moved_permanently_get());
    public static final http_errors moved_temporarily = new http_errors("moved_temporarily", libtorrent_jni.moved_temporarily_get());
    public static final http_errors multiple_choices = new http_errors("multiple_choices", libtorrent_jni.multiple_choices_get());
    public static final http_errors no_content = new http_errors("no_content", libtorrent_jni.no_content_get());
    public static final http_errors not_found = new http_errors("not_found", libtorrent_jni.not_found_get());
    public static final http_errors not_implemented = new http_errors("not_implemented", libtorrent_jni.not_implemented_get());
    public static final http_errors not_modified = new http_errors("not_modified", libtorrent_jni.not_modified_get());
    public static final http_errors ok = new http_errors("ok", libtorrent_jni.ok_get());
    public static final http_errors service_unavailable = new http_errors("service_unavailable", libtorrent_jni.service_unavailable_get());
    private static int swigNext;
    private static http_errors[] swigValues = new http_errors[]{cont, ok, created, accepted, no_content, multiple_choices, moved_permanently, moved_temporarily, not_modified, bad_request, unauthorized, forbidden, not_found, internal_server_error, not_implemented, bad_gateway, service_unavailable};
    public static final http_errors unauthorized = new http_errors("unauthorized", libtorrent_jni.unauthorized_get());
    private final String swigName;
    private final int swigValue;

    public final int swigValue() {
        return this.swigValue;
    }

    public String toString() {
        return this.swigName;
    }

    public static http_errors swigToEnum(int i) {
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
        stringBuilder.append(http_errors.class);
        stringBuilder.append(" with value ");
        stringBuilder.append(i);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private http_errors(String str) {
        this.swigName = str;
        str = swigNext;
        swigNext = str + 1;
        this.swigValue = str;
    }

    private http_errors(String str, int i) {
        this.swigName = str;
        this.swigValue = i;
        swigNext = i + 1;
    }

    private http_errors(String str, http_errors http_errors) {
        this.swigName = str;
        this.swigValue = http_errors.swigValue;
        swigNext = this.swigValue + 1;
    }
}
