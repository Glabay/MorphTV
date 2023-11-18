package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

final class CharSequenceReader extends Reader {
    private int mark;
    private int pos;
    private CharSequence seq;

    public boolean markSupported() {
        return true;
    }

    public CharSequenceReader(CharSequence charSequence) {
        this.seq = (CharSequence) Preconditions.checkNotNull(charSequence);
    }

    private void checkOpen() throws IOException {
        if (this.seq == null) {
            throw new IOException("reader closed");
        }
    }

    private boolean hasRemaining() {
        return remaining() > 0;
    }

    private int remaining() {
        return this.seq.length() - this.pos;
    }

    public synchronized int read(CharBuffer charBuffer) throws IOException {
        Preconditions.checkNotNull(charBuffer);
        checkOpen();
        if (!hasRemaining()) {
            return -1;
        }
        int min = Math.min(charBuffer.remaining(), remaining());
        for (int i = 0; i < min; i++) {
            CharSequence charSequence = this.seq;
            int i2 = this.pos;
            this.pos = i2 + 1;
            charBuffer.put(charSequence.charAt(i2));
        }
        return min;
    }

    public synchronized int read() throws IOException {
        int charAt;
        checkOpen();
        if (hasRemaining()) {
            CharSequence charSequence = this.seq;
            int i = this.pos;
            this.pos = i + 1;
            charAt = charSequence.charAt(i);
        } else {
            charAt = -1;
        }
        return charAt;
    }

    public synchronized int read(char[] cArr, int i, int i2) throws IOException {
        Preconditions.checkPositionIndexes(i, i + i2, cArr.length);
        checkOpen();
        if (!hasRemaining()) {
            return -1;
        }
        i2 = Math.min(i2, remaining());
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = i + i3;
            CharSequence charSequence = this.seq;
            int i5 = this.pos;
            this.pos = i5 + 1;
            cArr[i4] = charSequence.charAt(i5);
        }
        return i2;
    }

    public synchronized long skip(long j) throws IOException {
        Preconditions.checkArgument(j >= 0, "n (%s) may not be negative", Long.valueOf(j));
        checkOpen();
        j = (int) Math.min((long) remaining(), j);
        this.pos += j;
        return (long) j;
    }

    public synchronized boolean ready() throws IOException {
        checkOpen();
        return true;
    }

    public synchronized void mark(int i) throws IOException {
        Preconditions.checkArgument(i >= 0, "readAheadLimit (%s) may not be negative", Integer.valueOf(i));
        checkOpen();
        this.mark = this.pos;
    }

    public synchronized void reset() throws IOException {
        checkOpen();
        this.pos = this.mark;
    }

    public synchronized void close() throws IOException {
        this.seq = null;
    }
}
