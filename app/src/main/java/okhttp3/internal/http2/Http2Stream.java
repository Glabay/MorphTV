package okhttp3.internal.http2;

import com.tonyodev.fetch2.util.FetchErrorStrings;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import okio.AsyncTimeout;
import okio.Buffer;
import okio.BufferedSource;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class Http2Stream {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    long bytesLeftInWriteWindow;
    final Http2Connection connection;
    ErrorCode errorCode = null;
    private boolean hasResponseHeaders;
    final int id;
    final StreamTimeout readTimeout = new StreamTimeout();
    private final List<Header> requestHeaders;
    private List<Header> responseHeaders;
    final FramingSink sink;
    private final FramingSource source;
    long unacknowledgedBytesRead = 0;
    final StreamTimeout writeTimeout = new StreamTimeout();

    final class FramingSink implements Sink {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final long EMIT_BUFFER_SIZE = 16384;
        boolean closed;
        boolean finished;
        private final Buffer sendBuffer = new Buffer();

        static {
            Class cls = Http2Stream.class;
        }

        FramingSink() {
        }

        public void write(Buffer buffer, long j) throws IOException {
            this.sendBuffer.write(buffer, j);
            while (this.sendBuffer.size() >= 16384) {
                emitFrame(null);
            }
        }

        private void emitFrame(boolean z) throws IOException {
            synchronized (Http2Stream.this) {
                Http2Stream.this.writeTimeout.enter();
                while (Http2Stream.this.bytesLeftInWriteWindow <= 0 && !this.finished && !this.closed && Http2Stream.this.errorCode == null) {
                    try {
                        Http2Stream.this.waitForIo();
                    } finally {
                        Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
                    }
                }
                Http2Stream.this.checkOutNotClosed();
                long min = Math.min(Http2Stream.this.bytesLeftInWriteWindow, this.sendBuffer.size());
                Http2Stream http2Stream = Http2Stream.this;
                http2Stream.bytesLeftInWriteWindow -= min;
            }
            Http2Stream.this.writeTimeout.enter();
            try {
                Http2Connection http2Connection = Http2Stream.this.connection;
                int i = Http2Stream.this.id;
                boolean z2 = z && min == this.sendBuffer.size();
                http2Connection.writeData(i, z2, this.sendBuffer, min);
            } finally {
                Http2Stream.this.writeTimeout.exitAndThrowIfTimedOut();
            }
        }

        public void flush() throws IOException {
            synchronized (Http2Stream.this) {
                Http2Stream.this.checkOutNotClosed();
            }
            while (this.sendBuffer.size() > 0) {
                emitFrame(false);
                Http2Stream.this.connection.flush();
            }
        }

        public Timeout timeout() {
            return Http2Stream.this.writeTimeout;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void close() throws java.io.IOException {
            /*
            r8 = this;
            r0 = okhttp3.internal.http2.Http2Stream.this;
            monitor-enter(r0);
            r1 = r8.closed;	 Catch:{ all -> 0x0052 }
            if (r1 == 0) goto L_0x0009;
        L_0x0007:
            monitor-exit(r0);	 Catch:{ all -> 0x0052 }
            return;
        L_0x0009:
            monitor-exit(r0);	 Catch:{ all -> 0x0052 }
            r0 = okhttp3.internal.http2.Http2Stream.this;
            r0 = r0.sink;
            r0 = r0.finished;
            r1 = 1;
            if (r0 != 0) goto L_0x003c;
        L_0x0013:
            r0 = r8.sendBuffer;
            r2 = r0.size();
            r4 = 0;
            r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r0 <= 0) goto L_0x002d;
        L_0x001f:
            r0 = r8.sendBuffer;
            r2 = r0.size();
            r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r0 <= 0) goto L_0x003c;
        L_0x0029:
            r8.emitFrame(r1);
            goto L_0x001f;
        L_0x002d:
            r0 = okhttp3.internal.http2.Http2Stream.this;
            r2 = r0.connection;
            r0 = okhttp3.internal.http2.Http2Stream.this;
            r3 = r0.id;
            r4 = 1;
            r5 = 0;
            r6 = 0;
            r2.writeData(r3, r4, r5, r6);
        L_0x003c:
            r2 = okhttp3.internal.http2.Http2Stream.this;
            monitor-enter(r2);
            r8.closed = r1;	 Catch:{ all -> 0x004f }
            monitor-exit(r2);	 Catch:{ all -> 0x004f }
            r0 = okhttp3.internal.http2.Http2Stream.this;
            r0 = r0.connection;
            r0.flush();
            r0 = okhttp3.internal.http2.Http2Stream.this;
            r0.cancelStreamIfNecessary();
            return;
        L_0x004f:
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x004f }
            throw r0;
        L_0x0052:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0052 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Stream.FramingSink.close():void");
        }
    }

    private final class FramingSource implements Source {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        boolean closed;
        boolean finished;
        private final long maxByteCount;
        private final Buffer readBuffer = new Buffer();
        private final Buffer receiveBuffer = new Buffer();

        static {
            Class cls = Http2Stream.class;
        }

        FramingSource(long j) {
            this.maxByteCount = j;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public long read(okio.Buffer r8, long r9) throws java.io.IOException {
            /*
            r7 = this;
            r0 = 0;
            r2 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1));
            if (r2 >= 0) goto L_0x001d;
        L_0x0006:
            r8 = new java.lang.IllegalArgumentException;
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r1 = "byteCount < 0: ";
            r0.append(r1);
            r0.append(r9);
            r9 = r0.toString();
            r8.<init>(r9);
            throw r8;
        L_0x001d:
            r2 = okhttp3.internal.http2.Http2Stream.this;
            monitor-enter(r2);
            r7.waitUntilReadable();	 Catch:{ all -> 0x00b6 }
            r7.checkNotClosed();	 Catch:{ all -> 0x00b6 }
            r3 = r7.readBuffer;	 Catch:{ all -> 0x00b6 }
            r3 = r3.size();	 Catch:{ all -> 0x00b6 }
            r5 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1));
            if (r5 != 0) goto L_0x0034;
        L_0x0030:
            r8 = -1;
            monitor-exit(r2);	 Catch:{ all -> 0x00b6 }
            return r8;
        L_0x0034:
            r3 = r7.readBuffer;	 Catch:{ all -> 0x00b6 }
            r4 = r7.readBuffer;	 Catch:{ all -> 0x00b6 }
            r4 = r4.size();	 Catch:{ all -> 0x00b6 }
            r9 = java.lang.Math.min(r9, r4);	 Catch:{ all -> 0x00b6 }
            r8 = r3.read(r8, r9);	 Catch:{ all -> 0x00b6 }
            r10 = okhttp3.internal.http2.Http2Stream.this;	 Catch:{ all -> 0x00b6 }
            r3 = r10.unacknowledgedBytesRead;	 Catch:{ all -> 0x00b6 }
            r5 = 0;
            r5 = r3 + r8;
            r10.unacknowledgedBytesRead = r5;	 Catch:{ all -> 0x00b6 }
            r10 = okhttp3.internal.http2.Http2Stream.this;	 Catch:{ all -> 0x00b6 }
            r3 = r10.unacknowledgedBytesRead;	 Catch:{ all -> 0x00b6 }
            r10 = okhttp3.internal.http2.Http2Stream.this;	 Catch:{ all -> 0x00b6 }
            r10 = r10.connection;	 Catch:{ all -> 0x00b6 }
            r10 = r10.okHttpSettings;	 Catch:{ all -> 0x00b6 }
            r10 = r10.getInitialWindowSize();	 Catch:{ all -> 0x00b6 }
            r10 = r10 / 2;
            r5 = (long) r10;	 Catch:{ all -> 0x00b6 }
            r10 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
            if (r10 < 0) goto L_0x0075;
        L_0x0062:
            r10 = okhttp3.internal.http2.Http2Stream.this;	 Catch:{ all -> 0x00b6 }
            r10 = r10.connection;	 Catch:{ all -> 0x00b6 }
            r3 = okhttp3.internal.http2.Http2Stream.this;	 Catch:{ all -> 0x00b6 }
            r3 = r3.id;	 Catch:{ all -> 0x00b6 }
            r4 = okhttp3.internal.http2.Http2Stream.this;	 Catch:{ all -> 0x00b6 }
            r4 = r4.unacknowledgedBytesRead;	 Catch:{ all -> 0x00b6 }
            r10.writeWindowUpdateLater(r3, r4);	 Catch:{ all -> 0x00b6 }
            r10 = okhttp3.internal.http2.Http2Stream.this;	 Catch:{ all -> 0x00b6 }
            r10.unacknowledgedBytesRead = r0;	 Catch:{ all -> 0x00b6 }
        L_0x0075:
            monitor-exit(r2);	 Catch:{ all -> 0x00b6 }
            r10 = okhttp3.internal.http2.Http2Stream.this;
            r10 = r10.connection;
            monitor-enter(r10);
            r2 = okhttp3.internal.http2.Http2Stream.this;	 Catch:{ all -> 0x00b3 }
            r2 = r2.connection;	 Catch:{ all -> 0x00b3 }
            r3 = r2.unacknowledgedBytesRead;	 Catch:{ all -> 0x00b3 }
            r5 = 0;
            r5 = r3 + r8;
            r2.unacknowledgedBytesRead = r5;	 Catch:{ all -> 0x00b3 }
            r2 = okhttp3.internal.http2.Http2Stream.this;	 Catch:{ all -> 0x00b3 }
            r2 = r2.connection;	 Catch:{ all -> 0x00b3 }
            r2 = r2.unacknowledgedBytesRead;	 Catch:{ all -> 0x00b3 }
            r4 = okhttp3.internal.http2.Http2Stream.this;	 Catch:{ all -> 0x00b3 }
            r4 = r4.connection;	 Catch:{ all -> 0x00b3 }
            r4 = r4.okHttpSettings;	 Catch:{ all -> 0x00b3 }
            r4 = r4.getInitialWindowSize();	 Catch:{ all -> 0x00b3 }
            r4 = r4 / 2;
            r4 = (long) r4;	 Catch:{ all -> 0x00b3 }
            r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r6 < 0) goto L_0x00b1;
        L_0x009d:
            r2 = okhttp3.internal.http2.Http2Stream.this;	 Catch:{ all -> 0x00b3 }
            r2 = r2.connection;	 Catch:{ all -> 0x00b3 }
            r3 = 0;
            r4 = okhttp3.internal.http2.Http2Stream.this;	 Catch:{ all -> 0x00b3 }
            r4 = r4.connection;	 Catch:{ all -> 0x00b3 }
            r4 = r4.unacknowledgedBytesRead;	 Catch:{ all -> 0x00b3 }
            r2.writeWindowUpdateLater(r3, r4);	 Catch:{ all -> 0x00b3 }
            r2 = okhttp3.internal.http2.Http2Stream.this;	 Catch:{ all -> 0x00b3 }
            r2 = r2.connection;	 Catch:{ all -> 0x00b3 }
            r2.unacknowledgedBytesRead = r0;	 Catch:{ all -> 0x00b3 }
        L_0x00b1:
            monitor-exit(r10);	 Catch:{ all -> 0x00b3 }
            return r8;
        L_0x00b3:
            r8 = move-exception;
            monitor-exit(r10);	 Catch:{ all -> 0x00b3 }
            throw r8;
        L_0x00b6:
            r8 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x00b6 }
            throw r8;
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Stream.FramingSource.read(okio.Buffer, long):long");
        }

        private void waitUntilReadable() throws IOException {
            Http2Stream.this.readTimeout.enter();
            while (this.readBuffer.size() == 0 && !this.finished && !this.closed && Http2Stream.this.errorCode == null) {
                try {
                    Http2Stream.this.waitForIo();
                } catch (Throwable th) {
                    Http2Stream.this.readTimeout.exitAndThrowIfTimedOut();
                }
            }
            Http2Stream.this.readTimeout.exitAndThrowIfTimedOut();
        }

        void receive(BufferedSource bufferedSource, long j) throws IOException {
            while (j > 0) {
                synchronized (Http2Stream.this) {
                    boolean z = this.finished;
                    Object obj = null;
                    Object obj2 = j + this.readBuffer.size() > this.maxByteCount ? 1 : null;
                }
                if (obj2 != null) {
                    bufferedSource.skip(j);
                    Http2Stream.this.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
                    return;
                } else if (z) {
                    bufferedSource.skip(j);
                    return;
                } else {
                    long read = bufferedSource.read(this.receiveBuffer, j);
                    if (read == -1) {
                        throw new EOFException();
                    }
                    long j2 = j - read;
                    synchronized (Http2Stream.this) {
                        if (this.readBuffer.size() == 0) {
                            obj = 1;
                        }
                        this.readBuffer.writeAll(this.receiveBuffer);
                        if (obj != null) {
                            Http2Stream.this.notifyAll();
                        }
                    }
                    j = j2;
                }
            }
        }

        public Timeout timeout() {
            return Http2Stream.this.readTimeout;
        }

        public void close() throws IOException {
            synchronized (Http2Stream.this) {
                this.closed = true;
                this.readBuffer.clear();
                Http2Stream.this.notifyAll();
            }
            Http2Stream.this.cancelStreamIfNecessary();
        }

        private void checkNotClosed() throws IOException {
            if (this.closed) {
                throw new IOException("stream closed");
            } else if (Http2Stream.this.errorCode != null) {
                throw new StreamResetException(Http2Stream.this.errorCode);
            }
        }
    }

    class StreamTimeout extends AsyncTimeout {
        StreamTimeout() {
        }

        protected void timedOut() {
            Http2Stream.this.closeLater(ErrorCode.CANCEL);
        }

        protected IOException newTimeoutException(IOException iOException) {
            IOException socketTimeoutException = new SocketTimeoutException(FetchErrorStrings.CONNECTION_TIMEOUT);
            if (iOException != null) {
                socketTimeoutException.initCause(iOException);
            }
            return socketTimeoutException;
        }

        public void exitAndThrowIfTimedOut() throws IOException {
            if (exit()) {
                throw newTimeoutException(null);
            }
        }
    }

    Http2Stream(int i, Http2Connection http2Connection, boolean z, boolean z2, List<Header> list) {
        if (http2Connection == null) {
            throw new NullPointerException("connection == null");
        } else if (list == null) {
            throw new NullPointerException("requestHeaders == null");
        } else {
            this.id = i;
            this.connection = http2Connection;
            this.bytesLeftInWriteWindow = (long) http2Connection.peerSettings.getInitialWindowSize();
            this.source = new FramingSource((long) http2Connection.okHttpSettings.getInitialWindowSize());
            this.sink = new FramingSink();
            this.source.finished = z2;
            this.sink.finished = z;
            this.requestHeaders = list;
        }
    }

    public int getId() {
        return this.id;
    }

    public synchronized boolean isOpen() {
        if (this.errorCode != null) {
            return false;
        }
        if ((this.source.finished || this.source.closed) && ((this.sink.finished || this.sink.closed) && this.hasResponseHeaders)) {
            return false;
        }
        return true;
    }

    public boolean isLocallyInitiated() {
        if (this.connection.client == ((this.id & 1) == 1)) {
            return true;
        }
        return false;
    }

    public Http2Connection getConnection() {
        return this.connection;
    }

    public List<Header> getRequestHeaders() {
        return this.requestHeaders;
    }

    public synchronized List<Header> takeResponseHeaders() throws IOException {
        List<Header> list;
        if (isLocallyInitiated()) {
            this.readTimeout.enter();
            while (this.responseHeaders == null && this.errorCode == null) {
                try {
                    waitForIo();
                } finally {
                    this.readTimeout.exitAndThrowIfTimedOut();
                }
            }
            list = this.responseHeaders;
            if (list != null) {
                this.responseHeaders = null;
            } else {
                throw new StreamResetException(this.errorCode);
            }
        }
        throw new IllegalStateException("servers cannot read response headers");
        return list;
    }

    public synchronized ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public void sendResponseHeaders(List<Header> list, boolean z) throws IOException {
        if (list == null) {
            throw new NullPointerException("responseHeaders == null");
        }
        boolean z2 = false;
        synchronized (this) {
            this.hasResponseHeaders = true;
            if (!z) {
                this.sink.finished = true;
                z2 = true;
            }
        }
        this.connection.writeSynReply(this.id, z2, list);
        if (z2) {
            this.connection.flush();
        }
    }

    public Timeout readTimeout() {
        return this.readTimeout;
    }

    public Timeout writeTimeout() {
        return this.writeTimeout;
    }

    public Source getSource() {
        return this.source;
    }

    public Sink getSink() {
        synchronized (this) {
            if (this.hasResponseHeaders || isLocallyInitiated()) {
            } else {
                throw new IllegalStateException("reply before requesting the sink");
            }
        }
        return this.sink;
    }

    public void close(ErrorCode errorCode) throws IOException {
        if (closeInternal(errorCode)) {
            this.connection.writeSynReset(this.id, errorCode);
        }
    }

    public void closeLater(ErrorCode errorCode) {
        if (closeInternal(errorCode)) {
            this.connection.writeSynResetLater(this.id, errorCode);
        }
    }

    private boolean closeInternal(ErrorCode errorCode) {
        synchronized (this) {
            if (this.errorCode != null) {
                return false;
            } else if (this.source.finished && this.sink.finished) {
                return false;
            } else {
                this.errorCode = errorCode;
                notifyAll();
                this.connection.removeStream(this.id);
                return true;
            }
        }
    }

    void receiveHeaders(List<Header> list) {
        synchronized (this) {
            boolean z = true;
            this.hasResponseHeaders = true;
            if (this.responseHeaders == null) {
                this.responseHeaders = list;
                z = isOpen();
                notifyAll();
            } else {
                List arrayList = new ArrayList();
                arrayList.addAll(this.responseHeaders);
                arrayList.add(null);
                arrayList.addAll(list);
                this.responseHeaders = arrayList;
            }
        }
        if (!z) {
            this.connection.removeStream(this.id);
        }
    }

    void receiveData(BufferedSource bufferedSource, int i) throws IOException {
        this.source.receive(bufferedSource, (long) i);
    }

    void receiveFin() {
        synchronized (this) {
            this.source.finished = true;
            boolean isOpen = isOpen();
            notifyAll();
        }
        if (!isOpen) {
            this.connection.removeStream(this.id);
        }
    }

    synchronized void receiveRstStream(ErrorCode errorCode) {
        if (this.errorCode == null) {
            this.errorCode = errorCode;
            notifyAll();
        }
    }

    void cancelStreamIfNecessary() throws IOException {
        synchronized (this) {
            Object obj = (!this.source.finished && this.source.closed && (this.sink.finished || this.sink.closed)) ? 1 : null;
            boolean isOpen = isOpen();
        }
        if (obj != null) {
            close(ErrorCode.CANCEL);
        } else if (!isOpen) {
            this.connection.removeStream(this.id);
        }
    }

    void addBytesToWriteWindow(long j) {
        this.bytesLeftInWriteWindow += j;
        if (j > 0) {
            notifyAll();
        }
    }

    void checkOutNotClosed() throws IOException {
        if (this.sink.closed) {
            throw new IOException("stream closed");
        } else if (this.sink.finished) {
            throw new IOException("stream finished");
        } else if (this.errorCode != null) {
            throw new StreamResetException(this.errorCode);
        }
    }

    void waitForIo() throws java.io.InterruptedIOException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r1 = this;
        r1.wait();	 Catch:{ InterruptedException -> 0x0004 }
        return;
    L_0x0004:
        r0 = new java.io.InterruptedIOException;
        r0.<init>();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Stream.waitForIo():void");
    }
}
