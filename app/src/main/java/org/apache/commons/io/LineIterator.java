package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LineIterator implements Iterator<String>, Closeable {
    private final BufferedReader bufferedReader;
    private String cachedLine;
    private boolean finished = false;

    protected boolean isValidLine(String str) {
        return true;
    }

    public LineIterator(Reader reader) throws IllegalArgumentException {
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be null");
        } else if (reader instanceof BufferedReader) {
            this.bufferedReader = (BufferedReader) reader;
        } else {
            this.bufferedReader = new BufferedReader(reader);
        }
    }

    public boolean hasNext() {
        if (this.cachedLine != null) {
            return true;
        }
        if (this.finished) {
            return false;
        }
        String readLine;
        do {
            try {
                readLine = this.bufferedReader.readLine();
                if (readLine == null) {
                    this.finished = true;
                    return false;
                }
            } catch (Throwable e) {
                try {
                    close();
                } catch (Throwable e2) {
                    e.addSuppressed(e2);
                }
                throw new IllegalStateException(e);
            }
        } while (!isValidLine(readLine));
        this.cachedLine = readLine;
        return true;
    }

    public String next() {
        return nextLine();
    }

    public String nextLine() {
        if (hasNext()) {
            String str = this.cachedLine;
            this.cachedLine = null;
            return str;
        }
        throw new NoSuchElementException("No more lines");
    }

    public void close() throws IOException {
        this.finished = true;
        this.cachedLine = null;
        if (this.bufferedReader != null) {
            this.bufferedReader.close();
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("Remove unsupported on LineIterator");
    }

    @java.lang.Deprecated
    public static void closeQuietly(org.apache.commons.io.LineIterator r0) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r0 == 0) goto L_0x0005;
    L_0x0002:
        r0.close();	 Catch:{ IOException -> 0x0005 }
    L_0x0005:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.LineIterator.closeQuietly(org.apache.commons.io.LineIterator):void");
    }
}
