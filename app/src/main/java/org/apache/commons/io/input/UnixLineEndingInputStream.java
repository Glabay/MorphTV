package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class UnixLineEndingInputStream extends InputStream {
    private final boolean ensureLineFeedAtEndOfFile;
    private boolean eofSeen = false;
    private boolean slashNSeen = false;
    private boolean slashRSeen = false;
    private final InputStream target;

    public UnixLineEndingInputStream(InputStream inputStream, boolean z) {
        this.target = inputStream;
        this.ensureLineFeedAtEndOfFile = z;
    }

    private int readWithUpdate() throws IOException {
        int read = this.target.read();
        boolean z = false;
        this.eofSeen = read == -1;
        if (this.eofSeen) {
            return read;
        }
        this.slashNSeen = read == 10;
        if (read == 13) {
            z = true;
        }
        this.slashRSeen = z;
        return read;
    }

    public int read() throws IOException {
        boolean z = this.slashRSeen;
        if (this.eofSeen) {
            return eofGame(z);
        }
        int readWithUpdate = readWithUpdate();
        if (this.eofSeen) {
            return eofGame(z);
        }
        if (this.slashRSeen) {
            return 10;
        }
        return (z && this.slashNSeen) ? read() : readWithUpdate;
    }

    private int eofGame(boolean z) {
        if (!z) {
            if (this.ensureLineFeedAtEndOfFile) {
                if (this.slashNSeen) {
                    return -1;
                }
                this.slashNSeen = true;
                return true;
            }
        }
        return -1;
    }

    public void close() throws IOException {
        super.close();
        this.target.close();
    }

    public synchronized void mark(int i) {
        throw new UnsupportedOperationException("Mark notsupported");
    }
}
