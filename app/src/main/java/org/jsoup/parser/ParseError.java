package org.jsoup.parser;

public class ParseError {
    private String errorMsg;
    private int pos;

    ParseError(int i, String str) {
        this.pos = i;
        this.errorMsg = str;
    }

    ParseError(int i, String str, Object... objArr) {
        this.errorMsg = String.format(str, objArr);
        this.pos = i;
    }

    public String getErrorMessage() {
        return this.errorMsg;
    }

    public int getPosition() {
        return this.pos;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.pos);
        stringBuilder.append(": ");
        stringBuilder.append(this.errorMsg);
        return stringBuilder.toString();
    }
}
