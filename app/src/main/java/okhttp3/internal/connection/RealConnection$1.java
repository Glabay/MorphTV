package okhttp3.internal.connection;

import java.io.IOException;
import okhttp3.internal.ws.RealWebSocket.Streams;
import okio.BufferedSink;
import okio.BufferedSource;

class RealConnection$1 extends Streams {
    final /* synthetic */ RealConnection this$0;
    final /* synthetic */ StreamAllocation val$streamAllocation;

    RealConnection$1(RealConnection realConnection, boolean z, BufferedSource bufferedSource, BufferedSink bufferedSink, StreamAllocation streamAllocation) {
        this.this$0 = realConnection;
        this.val$streamAllocation = streamAllocation;
        super(z, bufferedSource, bufferedSink);
    }

    public void close() throws IOException {
        this.val$streamAllocation.streamFinished(true, this.val$streamAllocation.codec());
    }
}
