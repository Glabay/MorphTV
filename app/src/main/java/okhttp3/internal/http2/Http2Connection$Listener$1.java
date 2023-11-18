package okhttp3.internal.http2;

import java.io.IOException;

class Http2Connection$Listener$1 extends Http2Connection$Listener {
    Http2Connection$Listener$1() {
    }

    public void onStream(Http2Stream http2Stream) throws IOException {
        http2Stream.close(ErrorCode.REFUSED_STREAM);
    }
}
