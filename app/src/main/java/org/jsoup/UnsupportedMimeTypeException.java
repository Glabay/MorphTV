package org.jsoup;

import java.io.IOException;

public class UnsupportedMimeTypeException extends IOException {
    private String mimeType;
    private String url;

    public UnsupportedMimeTypeException(String str, String str2, String str3) {
        super(str);
        this.mimeType = str2;
        this.url = str3;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public String getUrl() {
        return this.url;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append(". Mimetype=");
        stringBuilder.append(this.mimeType);
        stringBuilder.append(", URL=");
        stringBuilder.append(this.url);
        return stringBuilder.toString();
    }
}
