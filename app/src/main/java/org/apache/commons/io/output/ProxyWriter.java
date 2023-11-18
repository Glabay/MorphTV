package org.apache.commons.io.output;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class ProxyWriter extends FilterWriter {
    protected void afterWrite(int i) throws IOException {
    }

    protected void beforeWrite(int i) throws IOException {
    }

    public ProxyWriter(Writer writer) {
        super(writer);
    }

    public Writer append(char c) throws IOException {
        try {
            beforeWrite(1);
            this.out.append(c);
            afterWrite(1);
        } catch (char c2) {
            handleIOException(c2);
        }
        return this;
    }

    public Writer append(CharSequence charSequence, int i, int i2) throws IOException {
        int i3 = i2 - i;
        try {
            beforeWrite(i3);
            this.out.append(charSequence, i, i2);
            afterWrite(i3);
        } catch (CharSequence charSequence2) {
            handleIOException(charSequence2);
        }
        return this;
    }

    public Writer append(CharSequence charSequence) throws IOException {
        int i = 0;
        if (charSequence != null) {
            try {
                i = charSequence.length();
            } catch (CharSequence charSequence2) {
                handleIOException(charSequence2);
            }
        }
        beforeWrite(i);
        this.out.append(charSequence2);
        afterWrite(i);
        return this;
    }

    public void write(int i) throws IOException {
        try {
            beforeWrite(1);
            this.out.write(i);
            afterWrite(1);
        } catch (int i2) {
            handleIOException(i2);
        }
    }

    public void write(char[] cArr) throws IOException {
        int i = 0;
        if (cArr != null) {
            try {
                i = cArr.length;
            } catch (char[] cArr2) {
                handleIOException(cArr2);
                return;
            }
        }
        beforeWrite(i);
        this.out.write(cArr2);
        afterWrite(i);
    }

    public void write(char[] cArr, int i, int i2) throws IOException {
        try {
            beforeWrite(i2);
            this.out.write(cArr, i, i2);
            afterWrite(i2);
        } catch (char[] cArr2) {
            handleIOException(cArr2);
        }
    }

    public void write(String str) throws IOException {
        int i = 0;
        if (str != null) {
            try {
                i = str.length();
            } catch (String str2) {
                handleIOException(str2);
                return;
            }
        }
        beforeWrite(i);
        this.out.write(str2);
        afterWrite(i);
    }

    public void write(String str, int i, int i2) throws IOException {
        try {
            beforeWrite(i2);
            this.out.write(str, i, i2);
            afterWrite(i2);
        } catch (String str2) {
            handleIOException(str2);
        }
    }

    public void flush() throws IOException {
        try {
            this.out.flush();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    public void close() throws IOException {
        try {
            this.out.close();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    protected void handleIOException(IOException iOException) throws IOException {
        throw iOException;
    }
}
