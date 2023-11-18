package retrofit2;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

final class OkHttpCall$ExceptionCatchingRequestBody extends ResponseBody {
    private final ResponseBody delegate;
    IOException thrownException;

    OkHttpCall$ExceptionCatchingRequestBody(ResponseBody responseBody) {
        this.delegate = responseBody;
    }

    public MediaType contentType() {
        return this.delegate.contentType();
    }

    public long contentLength() {
        return this.delegate.contentLength();
    }

    public BufferedSource source() {
        return Okio.buffer(new ForwardingSource(this.delegate.source()) {
            public long read(Buffer buffer, long j) throws IOException {
                try {
                    return super.read(buffer, j);
                } catch (Buffer buffer2) {
                    OkHttpCall$ExceptionCatchingRequestBody.this.thrownException = buffer2;
                    throw buffer2;
                }
            }
        });
    }

    public void close() {
        this.delegate.close();
    }

    void throwIfCaught() throws IOException {
        if (this.thrownException != null) {
            throw this.thrownException;
        }
    }
}
