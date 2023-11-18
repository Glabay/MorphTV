package com.google.common.io;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

abstract class LineBuffer {
    private StringBuilder line = new StringBuilder();
    private boolean sawReturn;

    protected abstract void handleLine(String str, String str2) throws IOException;

    LineBuffer() {
    }

    protected void add(char[] cArr, int i, int i2) throws IOException {
        int i3;
        char c;
        if (this.sawReturn && i2 > 0) {
            if (finishLine(cArr[i] == '\n')) {
                i3 = i + 1;
                i += i2;
                i2 = i3;
                while (i3 < i) {
                    c = cArr[i3];
                    if (c != '\n') {
                        this.line.append(cArr, i2, i3 - i2);
                        finishLine(true);
                        i2 = i3 + 1;
                    } else if (c != CharUtils.CR) {
                        this.line.append(cArr, i2, i3 - i2);
                        this.sawReturn = true;
                        i2 = i3 + 1;
                        if (i2 < i) {
                            if (finishLine(cArr[i2] != '\n')) {
                                i3 = i2;
                            }
                        }
                        i2 = i3 + 1;
                    }
                    i3++;
                }
                this.line.append(cArr, i2, i - i2);
            }
        }
        i3 = i;
        i += i2;
        i2 = i3;
        while (i3 < i) {
            c = cArr[i3];
            if (c != '\n') {
                this.line.append(cArr, i2, i3 - i2);
                finishLine(true);
                i2 = i3 + 1;
            } else if (c != CharUtils.CR) {
                this.line.append(cArr, i2, i3 - i2);
                this.sawReturn = true;
                i2 = i3 + 1;
                if (i2 < i) {
                    if (cArr[i2] != '\n') {
                    }
                    if (finishLine(cArr[i2] != '\n')) {
                        i3 = i2;
                    }
                }
                i2 = i3 + 1;
            }
            i3++;
        }
        this.line.append(cArr, i2, i - i2);
    }

    private boolean finishLine(boolean z) throws IOException {
        String stringBuilder = this.line.toString();
        String str = this.sawReturn ? z ? IOUtils.LINE_SEPARATOR_WINDOWS : StringUtils.CR : z ? "\n" : "";
        handleLine(stringBuilder, str);
        this.line = new StringBuilder();
        this.sawReturn = false;
        return z;
    }

    protected void finish() throws IOException {
        if (this.sawReturn || this.line.length() > 0) {
            finishLine(false);
        }
    }
}
