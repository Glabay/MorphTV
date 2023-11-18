package com.google.android.exoplayer2.upstream;

import android.support.annotation.NonNull;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.io.InputStream;

public final class DataSourceInputStream extends InputStream {
    private boolean closed = false;
    private final DataSource dataSource;
    private final DataSpec dataSpec;
    private boolean opened = false;
    private final byte[] singleByteArray;
    private long totalBytesRead;

    public DataSourceInputStream(DataSource dataSource, DataSpec dataSpec) {
        this.dataSource = dataSource;
        this.dataSpec = dataSpec;
        this.singleByteArray = new byte[1];
    }

    public long bytesRead() {
        return this.totalBytesRead;
    }

    public void open() throws IOException {
        checkOpened();
    }

    public int read() throws IOException {
        if (read(this.singleByteArray) == -1) {
            return -1;
        }
        return this.singleByteArray[0] & 255;
    }

    public int read(@NonNull byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read(@NonNull byte[] bArr, int i, int i2) throws IOException {
        Assertions.checkState(this.closed ^ 1);
        checkOpened();
        bArr = this.dataSource.read(bArr, i, i2);
        if (bArr == -1) {
            return -1;
        }
        this.totalBytesRead += (long) bArr;
        return bArr;
    }

    public void close() throws IOException {
        if (!this.closed) {
            this.dataSource.close();
            this.closed = true;
        }
    }

    private void checkOpened() throws IOException {
        if (!this.opened) {
            this.dataSource.open(this.dataSpec);
            this.opened = true;
        }
    }
}
