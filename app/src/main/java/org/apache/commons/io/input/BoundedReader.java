package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;

public class BoundedReader extends Reader {
    private static final int INVALID = -1;
    private int charsRead = 0;
    private int markedAt = -1;
    private final int maxCharsFromTargetReader;
    private int readAheadLimit;
    private final Reader target;

    public BoundedReader(Reader reader, int i) throws IOException {
        this.target = reader;
        this.maxCharsFromTargetReader = i;
    }

    public void close() throws IOException {
        this.target.close();
    }

    public void reset() throws IOException {
        this.charsRead = this.markedAt;
        this.target.reset();
    }

    public void mark(int i) throws IOException {
        this.readAheadLimit = i - this.charsRead;
        this.markedAt = this.charsRead;
        this.target.mark(i);
    }

    public int read() throws IOException {
        if (this.charsRead >= this.maxCharsFromTargetReader) {
            return -1;
        }
        if (this.markedAt >= 0 && this.charsRead - this.markedAt >= this.readAheadLimit) {
            return -1;
        }
        this.charsRead++;
        return this.target.read();
    }

    public int read(char[] cArr, int i, int i2) throws IOException {
        int i3 = 0;
        while (i3 < i2) {
            int read = read();
            if (read == -1) {
                if (i3 == 0) {
                    i3 = -1;
                }
                return i3;
            }
            cArr[i + i3] = (char) read;
            i3++;
        }
        return i2;
    }
}
