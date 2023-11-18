package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import java.io.IOException;

public final class DummyDataSource implements DataSource {
    public static final Factory FACTORY = new C07641();
    public static final DummyDataSource INSTANCE = new DummyDataSource();

    /* renamed from: com.google.android.exoplayer2.upstream.DummyDataSource$1 */
    static class C07641 implements Factory {
        C07641() {
        }

        public DataSource createDataSource() {
            return new DummyDataSource();
        }
    }

    public void close() throws IOException {
    }

    public Uri getUri() {
        return null;
    }

    private DummyDataSource() {
    }

    public long open(DataSpec dataSpec) throws IOException {
        throw new IOException("Dummy source");
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        throw new UnsupportedOperationException();
    }
}
