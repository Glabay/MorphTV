package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.annotation.Nullable;

final class MultiInputStream extends InputStream {
    private InputStream in;
    private Iterator<? extends ByteSource> it;

    public boolean markSupported() {
        return false;
    }

    public MultiInputStream(Iterator<? extends ByteSource> it) throws IOException {
        this.it = (Iterator) Preconditions.checkNotNull(it);
        advance();
    }

    public void close() throws IOException {
        if (this.in != null) {
            try {
                this.in.close();
            } finally {
                this.in = null;
            }
        }
    }

    private void advance() throws IOException {
        close();
        if (this.it.hasNext()) {
            this.in = ((ByteSource) this.it.next()).openStream();
        }
    }

    public int available() throws IOException {
        if (this.in == null) {
            return 0;
        }
        return this.in.available();
    }

    public int read() throws IOException {
        if (this.in == null) {
            return -1;
        }
        int read = this.in.read();
        if (read != -1) {
            return read;
        }
        advance();
        return read();
    }

    public int read(@Nullable byte[] bArr, int i, int i2) throws IOException {
        if (this.in == null) {
            return -1;
        }
        int read = this.in.read(bArr, i, i2);
        if (read != -1) {
            return read;
        }
        advance();
        return read(bArr, i, i2);
    }

    public long skip(long j) throws IOException {
        if (this.in != null) {
            if (j > 0) {
                long skip = this.in.skip(j);
                if (skip != 0) {
                    return skip;
                }
                if (read() == -1) {
                    return 0;
                }
                return this.in.skip(j - 1) + 1;
            }
        }
        return 0;
    }
}
