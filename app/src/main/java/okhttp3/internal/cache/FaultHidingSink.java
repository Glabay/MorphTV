package okhttp3.internal.cache;

import java.io.IOException;
import okio.Buffer;
import okio.ForwardingSink;
import okio.Sink;

class FaultHidingSink extends ForwardingSink {
    private boolean hasErrors;

    protected void onException(IOException iOException) {
    }

    FaultHidingSink(Sink sink) {
        super(sink);
    }

    public void write(Buffer buffer, long j) throws IOException {
        if (this.hasErrors) {
            buffer.skip(j);
            return;
        }
        try {
            super.write(buffer, j);
        } catch (Buffer buffer2) {
            this.hasErrors = 1;
            onException(buffer2);
        }
    }

    public void flush() throws IOException {
        if (!this.hasErrors) {
            try {
                super.flush();
            } catch (IOException e) {
                this.hasErrors = true;
                onException(e);
            }
        }
    }

    public void close() throws IOException {
        if (!this.hasErrors) {
            try {
                super.close();
            } catch (IOException e) {
                this.hasErrors = true;
                onException(e);
            }
        }
    }
}
