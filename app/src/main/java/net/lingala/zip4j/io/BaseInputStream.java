package net.lingala.zip4j.io;

import java.io.IOException;
import java.io.InputStream;
import net.lingala.zip4j.unzip.UnzipEngine;

public abstract class BaseInputStream extends InputStream {
    public int available() throws IOException {
        return 0;
    }

    public UnzipEngine getUnzipEngine() {
        return null;
    }

    public int read() throws IOException {
        return 0;
    }

    public void seek(long j) throws IOException {
    }
}
