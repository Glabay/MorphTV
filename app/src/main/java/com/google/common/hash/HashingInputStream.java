package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@Beta
public final class HashingInputStream extends FilterInputStream {
    private final Hasher hasher;

    public void mark(int i) {
    }

    public boolean markSupported() {
        return false;
    }

    public HashingInputStream(HashFunction hashFunction, InputStream inputStream) {
        super((InputStream) Preconditions.checkNotNull(inputStream));
        this.hasher = (Hasher) Preconditions.checkNotNull(hashFunction.newHasher());
    }

    public int read() throws IOException {
        int read = this.in.read();
        if (read != -1) {
            this.hasher.putByte((byte) read);
        }
        return read;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        i2 = this.in.read(bArr, i, i2);
        if (i2 != -1) {
            this.hasher.putBytes(bArr, i, i2);
        }
        return i2;
    }

    public void reset() throws IOException {
        throw new IOException("reset not supported");
    }

    public HashCode hash() {
        return this.hasher.hash();
    }
}
