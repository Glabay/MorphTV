package okhttp3.internal.ws;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.IOException;
import java.util.Random;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.Sink;
import okio.Timeout;
import org.mozilla.universalchardet.prober.distributionanalysis.Big5DistributionAnalysis;

final class WebSocketWriter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    boolean activeWriter;
    final Buffer buffer = new Buffer();
    final FrameSink frameSink = new FrameSink();
    final boolean isClient;
    final byte[] maskBuffer;
    final byte[] maskKey;
    final Random random;
    final BufferedSink sink;
    boolean writerClosed;

    final class FrameSink implements Sink {
        boolean closed;
        long contentLength;
        int formatOpcode;
        boolean isFirstFrame;

        FrameSink() {
        }

        public void write(Buffer buffer, long j) throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            WebSocketWriter.this.buffer.write(buffer, j);
            buffer = (this.isFirstFrame == null || this.contentLength == -1 || WebSocketWriter.this.buffer.size() <= this.contentLength - PlaybackStateCompat.ACTION_PLAY_FROM_URI) ? null : true;
            long completeSegmentByteCount = WebSocketWriter.this.buffer.completeSegmentByteCount();
            if (completeSegmentByteCount > 0 && buffer == null) {
                synchronized (WebSocketWriter.this) {
                    WebSocketWriter.this.writeMessageFrameSynchronized(this.formatOpcode, completeSegmentByteCount, this.isFirstFrame, false);
                }
                this.isFirstFrame = 0;
            }
        }

        public void flush() throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            synchronized (WebSocketWriter.this) {
                WebSocketWriter.this.writeMessageFrameSynchronized(this.formatOpcode, WebSocketWriter.this.buffer.size(), this.isFirstFrame, false);
            }
            this.isFirstFrame = false;
        }

        public Timeout timeout() {
            return WebSocketWriter.this.sink.timeout();
        }

        public void close() throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            synchronized (WebSocketWriter.this) {
                WebSocketWriter.this.writeMessageFrameSynchronized(this.formatOpcode, WebSocketWriter.this.buffer.size(), this.isFirstFrame, true);
            }
            this.closed = true;
            WebSocketWriter.this.activeWriter = false;
        }
    }

    WebSocketWriter(boolean z, BufferedSink bufferedSink, Random random) {
        if (bufferedSink == null) {
            throw new NullPointerException("sink == null");
        } else if (random == null) {
            throw new NullPointerException("random == null");
        } else {
            this.isClient = z;
            this.sink = bufferedSink;
            this.random = random;
            bufferedSink = null;
            this.maskKey = z ? new byte[4] : null;
            if (z) {
                bufferedSink = new byte[true];
            }
            this.maskBuffer = bufferedSink;
        }
    }

    void writePing(ByteString byteString) throws IOException {
        synchronized (this) {
            writeControlFrameSynchronized(9, byteString);
        }
    }

    void writePong(ByteString byteString) throws IOException {
        synchronized (this) {
            writeControlFrameSynchronized(10, byteString);
        }
    }

    void writeClose(int i, ByteString byteString) throws IOException {
        ByteString byteString2 = ByteString.EMPTY;
        if (!(i == 0 && byteString == null)) {
            if (i != 0) {
                WebSocketProtocol.validateCloseCode(i);
            }
            Buffer buffer = new Buffer();
            buffer.writeShort(i);
            if (byteString != null) {
                buffer.write(byteString);
            }
            byteString2 = buffer.readByteString();
        }
        synchronized (this) {
            try {
                writeControlFrameSynchronized(8, byteString2);
                this.writerClosed = true;
            } catch (Throwable th) {
                this.writerClosed = true;
            }
        }
    }

    private void writeControlFrameSynchronized(int i, ByteString byteString) throws IOException {
        if (this.writerClosed) {
            throw new IOException("closed");
        }
        int size = byteString.size();
        if (((long) size) > 125) {
            throw new IllegalArgumentException("Payload size must be less than or equal to 125");
        }
        this.sink.writeByte(i | 128);
        if (this.isClient != 0) {
            this.sink.writeByte(size | 128);
            this.random.nextBytes(this.maskKey);
            this.sink.write(this.maskKey);
            i = byteString.toByteArray();
            WebSocketProtocol.toggleMask(i, (long) i.length, this.maskKey, 0);
            this.sink.write(i);
        } else {
            this.sink.writeByte(size);
            this.sink.write(byteString);
        }
        this.sink.flush();
    }

    Sink newMessageSink(int i, long j) {
        if (this.activeWriter) {
            throw new IllegalStateException("Another message writer is active. Did you call close()?");
        }
        this.activeWriter = true;
        this.frameSink.formatOpcode = i;
        this.frameSink.contentLength = j;
        this.frameSink.isFirstFrame = true;
        this.frameSink.closed = 0;
        return this.frameSink;
    }

    void writeMessageFrameSynchronized(int i, long j, boolean z, boolean z2) throws IOException {
        if (this.writerClosed) {
            throw new IOException("closed");
        }
        if (!z) {
            i = 0;
        }
        if (z2) {
            i |= 128;
        }
        this.sink.writeByte(i);
        i = this.isClient != 0 ? 128 : 0;
        if (j <= true) {
            this.sink.writeByte(i | ((int) j));
        } else if (j <= true) {
            this.sink.writeByte(i | Big5DistributionAnalysis.LOWBYTE_END_1);
            this.sink.writeShort((int) j);
        } else {
            this.sink.writeByte(i | 127);
            this.sink.writeLong(j);
        }
        if (this.isClient != 0) {
            this.random.nextBytes(this.maskKey);
            this.sink.write(this.maskKey);
            z = 0;
            while (z < j) {
                i = this.buffer.read(this.maskBuffer, 0, (int) Math.min(j, (long) this.maskBuffer.length));
                if (i == -1) {
                    throw new AssertionError();
                }
                long j2 = (long) i;
                WebSocketProtocol.toggleMask(this.maskBuffer, j2, this.maskKey, z);
                this.sink.write(this.maskBuffer, 0, i);
                z += j2;
            }
        } else {
            this.sink.write(this.buffer, j);
        }
        this.sink.emit();
    }
}
