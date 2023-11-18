package okio;

import java.io.IOException;

class AsyncTimeout$2 implements Source {
    final /* synthetic */ AsyncTimeout this$0;
    final /* synthetic */ Source val$source;

    AsyncTimeout$2(AsyncTimeout asyncTimeout, Source source) {
        this.this$0 = asyncTimeout;
        this.val$source = source;
    }

    public long read(Buffer buffer, long j) throws IOException {
        this.this$0.enter();
        try {
            buffer = this.val$source.read(buffer, j);
            this.this$0.exit(true);
            return buffer;
        } catch (Buffer buffer2) {
            throw this.this$0.exit(buffer2);
        } catch (Throwable th) {
            this.this$0.exit(false);
        }
    }

    public void close() throws IOException {
        try {
            this.val$source.close();
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
        stringBuilder.append("AsyncTimeout.source(");
        stringBuilder.append(this.val$source);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
