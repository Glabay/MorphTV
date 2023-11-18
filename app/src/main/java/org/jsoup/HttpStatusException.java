package org.jsoup;

import java.io.IOException;

public class HttpStatusException extends IOException {
    private int statusCode;
    private String url;

    public HttpStatusException(String str, int i, String str2) {
        super(str);
        this.statusCode = i;
        this.url = str2;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getUrl() {
        return this.url;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append(". Status=");
        stringBuilder.append(this.statusCode);
        stringBuilder.append(", URL=");
        stringBuilder.append(this.url);
        return stringBuilder.toString();
    }
}
