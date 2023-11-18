package okio;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.IOException;

class AsyncTimeout$1 implements Sink {
    final /* synthetic */ AsyncTimeout this$0;
    final /* synthetic */ Sink val$sink;

    AsyncTimeout$1(AsyncTimeout asyncTimeout, Sink sink) {
        this.this$0 = asyncTimeout;
        this.val$sink = sink;
    }

    public void write(Buffer buffer, long j) throws IOException {
        Util.checkOffsetAndCount(buffer.size, 0, j);
        while (true) {
            long j2 = 0;
            if (j > 0) {
                Segment segment = buffer.head;
                while (j2 < PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH) {
                    long j3 = j2 + ((long) (buffer.head.limit - buffer.head.pos));
                    if (j3 >= j) {
                        j2 = j;
                        break;
                    } else {
                        segment = segment.next;
                        j2 = j3;
                    }
                }
                this.this$0.enter();
                try {
                    this.val$sink.write(buffer, j2);
                    long j4 = j - j2;
                    this.this$0.exit(1);
                    j = j4;
                } catch (Buffer buffer2) {
                    throw this.this$0.exit(buffer2);
                } catch (Throwable th) {
                    this.this$0.exit(false);
                }
            } else {
                return;
            }
        }
    }

    public void flush() throws IOException {
        this.this$0.enter();
        try {
            this.val$sink.flush();
            this.this$0.exit(true);
        } catch (IOException e) {
            throw this.this$0.exit(e);
        } catch (Throwable th) {
            this.this$0.exit(false);
        }
    }

    public void close() throws IOException {
        this.this$0.enter();
        try {
            this.val$sink.close();
            this.this$0.exit(true);
        } catch (IOException e) {
            throw this.this$0.exit(e);
        } catch (Throwable th) {
            this.this$0.exit(false);
        }
    }

    public Timeout timeout() {
        return this.this$0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AsyncTimeout.sink(");
        stringBuilder.append(this.val$sink);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
