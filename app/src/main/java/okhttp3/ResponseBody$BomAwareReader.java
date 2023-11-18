package okhttp3;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import okhttp3.internal.Util;
import okio.BufferedSource;

final class ResponseBody$BomAwareReader extends Reader {
    private final Charset charset;
    private boolean closed;
    private Reader delegate;
    private final BufferedSource source;

    ResponseBody$BomAwareReader(BufferedSource bufferedSource, Charset charset) {
        this.source = bufferedSource;
        this.charset = charset;
    }

    public int read(char[] cArr, int i, int i2) throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
        Reader reader = this.delegate;
        if (reader == null) {
            Reader inputStreamReader = new InputStreamReader(this.source.inputStream(), Util.bomAwareCharset(this.source, this.charset));
            this.delegate = inputStreamReader;
            reader = inputStreamReader;
        }
        return reader.read(cArr, i, i2);
    }

    public void close() throws IOException {
        this.closed = true;
        if (this.delegate != null) {
            this.delegate.close();
        } else {
            this.source.close();
        }
    }
}
