package okhttp3.internal.http2;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.Protocol;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;

public final class Http2Connection implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int OKHTTP_CLIENT_WINDOW_SIZE = 16777216;
    static final ExecutorService executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp Http2Connection", true));
    long bytesLeftInWriteWindow;
    final boolean client;
    final Set<Integer> currentPushRequests = new LinkedHashSet();
    final String hostname;
    int lastGoodStreamId;
    final Http2Connection$Listener listener;
    private int nextPingId;
    int nextStreamId;
    Settings okHttpSettings = new Settings();
    final Settings peerSettings = new Settings();
    private Map<Integer, Ping> pings;
    private final ExecutorService pushExecutor;
    final PushObserver pushObserver;
    final ReaderRunnable readerRunnable;
    boolean receivedInitialPeerSettings = false;
    boolean shutdown;
    final Socket socket;
    final Map<Integer, Http2Stream> streams = new LinkedHashMap();
    long unacknowledgedBytesRead = 0;
    final Http2Writer writer;

    public static class Builder {
        boolean client;
        String hostname;
        Http2Connection$Listener listener = Http2Connection$Listener.REFUSE_INCOMING_STREAMS;
        PushObserver pushObserver = PushObserver.CANCEL;
        BufferedSink sink;
        Socket socket;
        BufferedSource source;

        public Builder(boolean z) {
            this.client = z;
        }

        public Builder socket(Socket socket) throws IOException {
            return socket(socket, ((InetSocketAddress) socket.getRemoteSocketAddress()).getHostName(), Okio.buffer(Okio.source(socket)), Okio.buffer(Okio.sink(socket)));
        }

        public Builder socket(Socket socket, String str, BufferedSource bufferedSource, BufferedSink bufferedSink) {
            this.socket = socket;
            this.hostname = str;
            this.source = bufferedSource;
            this.sink = bufferedSink;
            return this;
        }

        public Builder listener(Http2Connection$Listener http2Connection$Listener) {
            this.listener = http2Connection$Listener;
            return this;
        }

        public Builder pushObserver(PushObserver pushObserver) {
            this.pushObserver = pushObserver;
            return this;
        }

        public Http2Connection build() throws IOException {
            return new Http2Connection(this);
        }
    }

    class ReaderRunnable extends NamedRunnable implements Handler {
        final Http2Reader reader;

        public void ackSettings() {
        }

        public void alternateService(int i, String str, ByteString byteString, String str2, int i2, long j) {
        }

        public void priority(int i, int i2, int i3, boolean z) {
        }

        ReaderRunnable(Http2Reader http2Reader) {
            super("OkHttp %s", r4.hostname);
            this.reader = http2Reader;
        }

        protected void execute() {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
            /*
            r5 = this;
            r0 = okhttp3.internal.http2.ErrorCode.INTERNAL_ERROR;
            r1 = okhttp3.internal.http2.ErrorCode.INTERNAL_ERROR;
            r2 = r5.reader;	 Catch:{ IOException -> 0x001e }
            r2.readConnectionPreface(r5);	 Catch:{ IOException -> 0x001e }
        L_0x0009:
            r2 = r5.reader;	 Catch:{ IOException -> 0x001e }
            r3 = 0;	 Catch:{ IOException -> 0x001e }
            r2 = r2.nextFrame(r3, r5);	 Catch:{ IOException -> 0x001e }
            if (r2 == 0) goto L_0x0013;	 Catch:{ IOException -> 0x001e }
        L_0x0012:
            goto L_0x0009;	 Catch:{ IOException -> 0x001e }
        L_0x0013:
            r2 = okhttp3.internal.http2.ErrorCode.NO_ERROR;	 Catch:{ IOException -> 0x001e }
            r0 = okhttp3.internal.http2.ErrorCode.CANCEL;	 Catch:{ IOException -> 0x001a }
            r1 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ IOException -> 0x0027 }
            goto L_0x0024;
        L_0x001a:
            r0 = r2;
            goto L_0x001e;
        L_0x001c:
            r2 = move-exception;
            goto L_0x0031;
        L_0x001e:
            r2 = okhttp3.internal.http2.ErrorCode.PROTOCOL_ERROR;	 Catch:{ all -> 0x001c }
            r0 = okhttp3.internal.http2.ErrorCode.PROTOCOL_ERROR;	 Catch:{ all -> 0x002d }
            r1 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ IOException -> 0x0027 }
        L_0x0024:
            r1.close(r2, r0);	 Catch:{ IOException -> 0x0027 }
        L_0x0027:
            r0 = r5.reader;
            okhttp3.internal.Util.closeQuietly(r0);
            return;
        L_0x002d:
            r0 = move-exception;
            r4 = r2;
            r2 = r0;
            r0 = r4;
        L_0x0031:
            r3 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ IOException -> 0x0036 }
            r3.close(r0, r1);	 Catch:{ IOException -> 0x0036 }
        L_0x0036:
            r0 = r5.reader;
            okhttp3.internal.Util.closeQuietly(r0);
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.ReaderRunnable.execute():void");
        }

        public void data(boolean z, int i, BufferedSource bufferedSource, int i2) throws IOException {
            if (Http2Connection.this.pushedStream(i)) {
                Http2Connection.this.pushDataLater(i, bufferedSource, i2, z);
                return;
            }
            Http2Stream stream = Http2Connection.this.getStream(i);
            if (stream == null) {
                Http2Connection.this.writeSynResetLater(i, ErrorCode.PROTOCOL_ERROR);
                bufferedSource.skip((long) i2);
                return;
            }
            stream.receiveData(bufferedSource, i2);
            if (z) {
                stream.receiveFin();
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void headers(boolean r10, int r11, int r12, java.util.List<okhttp3.internal.http2.Header> r13) {
            /*
            r9 = this;
            r12 = okhttp3.internal.http2.Http2Connection.this;
            r12 = r12.pushedStream(r11);
            if (r12 == 0) goto L_0x000e;
        L_0x0008:
            r12 = okhttp3.internal.http2.Http2Connection.this;
            r12.pushHeadersLater(r11, r13, r10);
            return;
        L_0x000e:
            r12 = okhttp3.internal.http2.Http2Connection.this;
            monitor-enter(r12);
            r0 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ all -> 0x0078 }
            r0 = r0.shutdown;	 Catch:{ all -> 0x0078 }
            if (r0 == 0) goto L_0x0019;
        L_0x0017:
            monitor-exit(r12);	 Catch:{ all -> 0x0078 }
            return;
        L_0x0019:
            r0 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ all -> 0x0078 }
            r0 = r0.getStream(r11);	 Catch:{ all -> 0x0078 }
            if (r0 != 0) goto L_0x006e;
        L_0x0021:
            r0 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ all -> 0x0078 }
            r0 = r0.lastGoodStreamId;	 Catch:{ all -> 0x0078 }
            if (r11 > r0) goto L_0x0029;
        L_0x0027:
            monitor-exit(r12);	 Catch:{ all -> 0x0078 }
            return;
        L_0x0029:
            r0 = r11 % 2;
            r1 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ all -> 0x0078 }
            r1 = r1.nextStreamId;	 Catch:{ all -> 0x0078 }
            r2 = 2;
            r1 = r1 % r2;
            if (r0 != r1) goto L_0x0035;
        L_0x0033:
            monitor-exit(r12);	 Catch:{ all -> 0x0078 }
            return;
        L_0x0035:
            r0 = new okhttp3.internal.http2.Http2Stream;	 Catch:{ all -> 0x0078 }
            r5 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ all -> 0x0078 }
            r6 = 0;
            r3 = r0;
            r4 = r11;
            r7 = r10;
            r8 = r13;
            r3.<init>(r4, r5, r6, r7, r8);	 Catch:{ all -> 0x0078 }
            r10 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ all -> 0x0078 }
            r10.lastGoodStreamId = r11;	 Catch:{ all -> 0x0078 }
            r10 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ all -> 0x0078 }
            r10 = r10.streams;	 Catch:{ all -> 0x0078 }
            r13 = java.lang.Integer.valueOf(r11);	 Catch:{ all -> 0x0078 }
            r10.put(r13, r0);	 Catch:{ all -> 0x0078 }
            r10 = okhttp3.internal.http2.Http2Connection.executor;	 Catch:{ all -> 0x0078 }
            r13 = new okhttp3.internal.http2.Http2Connection$ReaderRunnable$1;	 Catch:{ all -> 0x0078 }
            r1 = "OkHttp %s stream %d";
            r2 = new java.lang.Object[r2];	 Catch:{ all -> 0x0078 }
            r3 = 0;
            r4 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ all -> 0x0078 }
            r4 = r4.hostname;	 Catch:{ all -> 0x0078 }
            r2[r3] = r4;	 Catch:{ all -> 0x0078 }
            r3 = 1;
            r11 = java.lang.Integer.valueOf(r11);	 Catch:{ all -> 0x0078 }
            r2[r3] = r11;	 Catch:{ all -> 0x0078 }
            r13.<init>(r1, r2, r0);	 Catch:{ all -> 0x0078 }
            r10.execute(r13);	 Catch:{ all -> 0x0078 }
            monitor-exit(r12);	 Catch:{ all -> 0x0078 }
            return;
        L_0x006e:
            monitor-exit(r12);	 Catch:{ all -> 0x0078 }
            r0.receiveHeaders(r13);
            if (r10 == 0) goto L_0x0077;
        L_0x0074:
            r0.receiveFin();
        L_0x0077:
            return;
        L_0x0078:
            r10 = move-exception;
            monitor-exit(r12);	 Catch:{ all -> 0x0078 }
            throw r10;
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.ReaderRunnable.headers(boolean, int, int, java.util.List):void");
        }

        public void rstStream(int i, ErrorCode errorCode) {
            if (Http2Connection.this.pushedStream(i)) {
                Http2Connection.this.pushResetLater(i, errorCode);
                return;
            }
            i = Http2Connection.this.removeStream(i);
            if (i != 0) {
                i.receiveRstStream(errorCode);
            }
        }

        public void settings(boolean z, Settings settings) {
            synchronized (Http2Connection.this) {
                boolean initialWindowSize = Http2Connection.this.peerSettings.getInitialWindowSize();
                if (z) {
                    Http2Connection.this.peerSettings.clear();
                }
                Http2Connection.this.peerSettings.merge(settings);
                applyAndAckSettings(settings);
                z = Http2Connection.this.peerSettings.getInitialWindowSize();
                Http2Stream[] http2StreamArr = null;
                if (z || z == initialWindowSize) {
                    z = false;
                } else {
                    z = (long) (z - initialWindowSize);
                    if (!Http2Connection.this.receivedInitialPeerSettings) {
                        Http2Connection.this.addBytesToWriteWindow(z);
                        Http2Connection.this.receivedInitialPeerSettings = true;
                    }
                    if (!Http2Connection.this.streams.isEmpty()) {
                        http2StreamArr = (Http2Stream[]) Http2Connection.this.streams.values().toArray(new Http2Stream[Http2Connection.this.streams.size()]);
                    }
                }
                ExecutorService executorService = Http2Connection.executor;
                Object[] objArr = new Object[1];
                int i = 0;
                objArr[0] = Http2Connection.this.hostname;
                executorService.execute(new NamedRunnable("OkHttp %s settings", objArr) {
                    public void execute() {
                        Http2Connection.this.listener.onSettings(Http2Connection.this);
                    }
                });
            }
            if (http2StreamArr != null && z) {
                int length = http2StreamArr.length;
                while (i < length) {
                    Http2Stream http2Stream = http2StreamArr[i];
                    synchronized (http2Stream) {
                        http2Stream.addBytesToWriteWindow(z);
                    }
                    i++;
                }
            }
        }

        private void applyAndAckSettings(final Settings settings) {
            Http2Connection.executor.execute(new NamedRunnable("OkHttp %s ACK Settings", new Object[]{Http2Connection.this.hostname}) {
                public void execute() {
                    /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
                    /*
                    r2 = this;
                    r0 = okhttp3.internal.http2.Http2Connection.ReaderRunnable.this;	 Catch:{ IOException -> 0x000b }
                    r0 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ IOException -> 0x000b }
                    r0 = r0.writer;	 Catch:{ IOException -> 0x000b }
                    r1 = r7;	 Catch:{ IOException -> 0x000b }
                    r0.applyAndAckSettings(r1);	 Catch:{ IOException -> 0x000b }
                L_0x000b:
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.ReaderRunnable.3.execute():void");
                }
            });
        }

        public void ping(boolean z, int i, int i2) {
            if (z) {
                z = Http2Connection.this.removePing(i);
                if (z) {
                    z.receive();
                    return;
                }
                return;
            }
            Http2Connection.this.writePingLater(true, i, i2, null);
        }

        public void goAway(int i, ErrorCode errorCode, ByteString byteString) {
            byteString.size();
            synchronized (Http2Connection.this) {
                Http2Stream[] http2StreamArr = (Http2Stream[]) Http2Connection.this.streams.values().toArray(new Http2Stream[Http2Connection.this.streams.size()]);
                Http2Connection.this.shutdown = true;
            }
            for (Http2Stream http2Stream : http2StreamArr) {
                if (http2Stream.getId() > i && http2Stream.isLocallyInitiated()) {
                    http2Stream.receiveRstStream(ErrorCode.REFUSED_STREAM);
                    Http2Connection.this.removeStream(http2Stream.getId());
                }
            }
        }

        public void windowUpdate(int i, long j) {
            if (i == 0) {
                synchronized (Http2Connection.this) {
                    i = Http2Connection.this;
                    i.bytesLeftInWriteWindow += j;
                    Http2Connection.this.notifyAll();
                }
                return;
            }
            i = Http2Connection.this.getStream(i);
            if (i != 0) {
                synchronized (i) {
                    i.addBytesToWriteWindow(j);
                }
            }
        }

        public void pushPromise(int i, int i2, List<Header> list) {
            Http2Connection.this.pushRequestLater(i2, list);
        }
    }

    boolean pushedStream(int i) {
        return i != 0 && (i & 1) == 0;
    }

    Http2Connection(Builder builder) {
        this.pushObserver = builder.pushObserver;
        this.client = builder.client;
        this.listener = builder.listener;
        int i = 2;
        this.nextStreamId = builder.client ? 1 : 2;
        if (builder.client) {
            this.nextStreamId += 2;
        }
        if (builder.client) {
            i = 1;
        }
        this.nextPingId = i;
        if (builder.client) {
            this.okHttpSettings.set(7, 16777216);
        }
        this.hostname = builder.hostname;
        this.pushExecutor = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory(Util.format("OkHttp %s Push Observer", this.hostname), true));
        this.peerSettings.set(7, 65535);
        this.peerSettings.set(5, 16384);
        this.bytesLeftInWriteWindow = (long) this.peerSettings.getInitialWindowSize();
        this.socket = builder.socket;
        this.writer = new Http2Writer(builder.sink, this.client);
        this.readerRunnable = new ReaderRunnable(new Http2Reader(builder.source, this.client));
    }

    public Protocol getProtocol() {
        return Protocol.HTTP_2;
    }

    public synchronized int openStreamCount() {
        return this.streams.size();
    }

    synchronized Http2Stream getStream(int i) {
        return (Http2Stream) this.streams.get(Integer.valueOf(i));
    }

    synchronized Http2Stream removeStream(int i) {
        Http2Stream http2Stream;
        http2Stream = (Http2Stream) this.streams.remove(Integer.valueOf(i));
        notifyAll();
        return http2Stream;
    }

    public synchronized int maxConcurrentStreams() {
        return this.peerSettings.getMaxConcurrentStreams(Integer.MAX_VALUE);
    }

    public Http2Stream pushStream(int i, List<Header> list, boolean z) throws IOException {
        if (!this.client) {
            return newStream(i, list, z);
        }
        throw new IllegalStateException("Client cannot push requests.");
    }

    public Http2Stream newStream(List<Header> list, boolean z) throws IOException {
        return newStream(0, list, z);
    }

    private Http2Stream newStream(int i, List<Header> list, boolean z) throws IOException {
        Http2Stream http2Stream;
        boolean z2 = z ^ 1;
        synchronized (this.writer) {
            synchronized (this) {
                if (this.shutdown) {
                    throw new ConnectionShutdownException();
                }
                int i2 = this.nextStreamId;
                this.nextStreamId += 2;
                http2Stream = new Http2Stream(i2, this, z2, false, list);
                if (z && this.bytesLeftInWriteWindow != 0) {
                    if (http2Stream.bytesLeftInWriteWindow != 0) {
                        z = false;
                        if (http2Stream.isOpen()) {
                            this.streams.put(Integer.valueOf(i2), http2Stream);
                        }
                    }
                }
                z = true;
                if (http2Stream.isOpen()) {
                    this.streams.put(Integer.valueOf(i2), http2Stream);
                }
            }
            if (i == 0) {
                this.writer.synStream(z2, i2, i, list);
            } else if (this.client) {
                throw new IllegalArgumentException("client streams shouldn't have associated stream IDs");
            } else {
                this.writer.pushPromise(i, i2, list);
            }
        }
        if (z) {
            this.writer.flush();
        }
        return http2Stream;
    }

    void writeSynReply(int i, boolean z, List<Header> list) throws IOException {
        this.writer.synReply(z, i, list);
    }

    public void writeData(int r11, boolean r12, okio.Buffer r13, long r14) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r10 = this;
        r0 = 0;
        r2 = (r14 > r0 ? 1 : (r14 == r0 ? 0 : -1));
        r3 = 0;
        if (r2 != 0) goto L_0x000d;
    L_0x0007:
        r14 = r10.writer;
        r14.data(r12, r11, r13, r3);
        return;
    L_0x000d:
        r2 = (r14 > r0 ? 1 : (r14 == r0 ? 0 : -1));
        if (r2 <= 0) goto L_0x0066;
    L_0x0011:
        monitor-enter(r10);
    L_0x0012:
        r4 = r10.bytesLeftInWriteWindow;	 Catch:{ InterruptedException -> 0x005e }
        r2 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));	 Catch:{ InterruptedException -> 0x005e }
        if (r2 > 0) goto L_0x0030;	 Catch:{ InterruptedException -> 0x005e }
    L_0x0018:
        r2 = r10.streams;	 Catch:{ InterruptedException -> 0x005e }
        r4 = java.lang.Integer.valueOf(r11);	 Catch:{ InterruptedException -> 0x005e }
        r2 = r2.containsKey(r4);	 Catch:{ InterruptedException -> 0x005e }
        if (r2 != 0) goto L_0x002c;	 Catch:{ InterruptedException -> 0x005e }
    L_0x0024:
        r11 = new java.io.IOException;	 Catch:{ InterruptedException -> 0x005e }
        r12 = "stream closed";	 Catch:{ InterruptedException -> 0x005e }
        r11.<init>(r12);	 Catch:{ InterruptedException -> 0x005e }
        throw r11;	 Catch:{ InterruptedException -> 0x005e }
    L_0x002c:
        r10.wait();	 Catch:{ InterruptedException -> 0x005e }
        goto L_0x0012;
    L_0x0030:
        r4 = r10.bytesLeftInWriteWindow;	 Catch:{ all -> 0x005c }
        r4 = java.lang.Math.min(r14, r4);	 Catch:{ all -> 0x005c }
        r2 = (int) r4;	 Catch:{ all -> 0x005c }
        r4 = r10.writer;	 Catch:{ all -> 0x005c }
        r4 = r4.maxDataLength();	 Catch:{ all -> 0x005c }
        r2 = java.lang.Math.min(r2, r4);	 Catch:{ all -> 0x005c }
        r4 = r10.bytesLeftInWriteWindow;	 Catch:{ all -> 0x005c }
        r6 = (long) r2;	 Catch:{ all -> 0x005c }
        r8 = r4 - r6;	 Catch:{ all -> 0x005c }
        r10.bytesLeftInWriteWindow = r8;	 Catch:{ all -> 0x005c }
        monitor-exit(r10);	 Catch:{ all -> 0x005c }
        r4 = 0;
        r4 = r14 - r6;
        r14 = r10.writer;
        if (r12 == 0) goto L_0x0056;
    L_0x0050:
        r15 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
        if (r15 != 0) goto L_0x0056;
    L_0x0054:
        r15 = 1;
        goto L_0x0057;
    L_0x0056:
        r15 = 0;
    L_0x0057:
        r14.data(r15, r11, r13, r2);
        r14 = r4;
        goto L_0x000d;
    L_0x005c:
        r11 = move-exception;
        goto L_0x0064;
    L_0x005e:
        r11 = new java.io.InterruptedIOException;	 Catch:{ all -> 0x005c }
        r11.<init>();	 Catch:{ all -> 0x005c }
        throw r11;	 Catch:{ all -> 0x005c }
    L_0x0064:
        monitor-exit(r10);	 Catch:{ all -> 0x005c }
        throw r11;
    L_0x0066:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.writeData(int, boolean, okio.Buffer, long):void");
    }

    void addBytesToWriteWindow(long j) {
        this.bytesLeftInWriteWindow += j;
        if (j > 0) {
            notifyAll();
        }
    }

    void writeSynResetLater(int i, ErrorCode errorCode) {
        final int i2 = i;
        final ErrorCode errorCode2 = errorCode;
        executor.execute(new NamedRunnable("OkHttp %s stream %d", new Object[]{this.hostname, Integer.valueOf(i)}) {
            public void execute() {
                /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
                /*
                r3 = this;
                r0 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ IOException -> 0x0009 }
                r1 = r5;	 Catch:{ IOException -> 0x0009 }
                r2 = r6;	 Catch:{ IOException -> 0x0009 }
                r0.writeSynReset(r1, r2);	 Catch:{ IOException -> 0x0009 }
            L_0x0009:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.1.execute():void");
            }
        });
    }

    void writeSynReset(int i, ErrorCode errorCode) throws IOException {
        this.writer.rstStream(i, errorCode);
    }

    void writeWindowUpdateLater(int i, long j) {
        final int i2 = i;
        final long j2 = j;
        executor.execute(new NamedRunnable("OkHttp Window Update %s stream %d", new Object[]{this.hostname, Integer.valueOf(i)}) {
            public void execute() {
                /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
                /*
                r4 = this;
                r0 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ IOException -> 0x000b }
                r0 = r0.writer;	 Catch:{ IOException -> 0x000b }
                r1 = r5;	 Catch:{ IOException -> 0x000b }
                r2 = r6;	 Catch:{ IOException -> 0x000b }
                r0.windowUpdate(r1, r2);	 Catch:{ IOException -> 0x000b }
            L_0x000b:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.2.execute():void");
            }
        });
    }

    public Ping ping() throws IOException {
        int i;
        Ping ping = new Ping();
        synchronized (this) {
            if (this.shutdown) {
                throw new ConnectionShutdownException();
            }
            i = this.nextPingId;
            this.nextPingId += 2;
            if (this.pings == null) {
                this.pings = new LinkedHashMap();
            }
            this.pings.put(Integer.valueOf(i), ping);
        }
        writePing(false, i, 1330343787, ping);
        return ping;
    }

    void writePingLater(boolean z, int i, int i2, Ping ping) {
        final boolean z2 = z;
        final int i3 = i;
        final int i4 = i2;
        final Ping ping2 = ping;
        executor.execute(new NamedRunnable("OkHttp %s ping %08x%08x", new Object[]{this.hostname, Integer.valueOf(i), Integer.valueOf(i2)}) {
            public void execute() {
                /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
                /*
                r5 = this;
                r0 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ IOException -> 0x000d }
                r1 = r5;	 Catch:{ IOException -> 0x000d }
                r2 = r6;	 Catch:{ IOException -> 0x000d }
                r3 = r7;	 Catch:{ IOException -> 0x000d }
                r4 = r8;	 Catch:{ IOException -> 0x000d }
                r0.writePing(r1, r2, r3, r4);	 Catch:{ IOException -> 0x000d }
            L_0x000d:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.3.execute():void");
            }
        });
    }

    void writePing(boolean z, int i, int i2, Ping ping) throws IOException {
        synchronized (this.writer) {
            if (ping != null) {
                ping.send();
            }
            this.writer.ping(z, i, i2);
        }
    }

    synchronized Ping removePing(int i) {
        return this.pings != null ? (Ping) this.pings.remove(Integer.valueOf(i)) : 0;
    }

    public void flush() throws IOException {
        this.writer.flush();
    }

    public void shutdown(ErrorCode errorCode) throws IOException {
        synchronized (this.writer) {
            synchronized (this) {
                if (this.shutdown) {
                    return;
                }
                this.shutdown = true;
                int i = this.lastGoodStreamId;
                this.writer.goAway(i, errorCode, Util.EMPTY_BYTE_ARRAY);
            }
        }
    }

    public void close() throws IOException {
        close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
    }

    void close(ErrorCode errorCode, ErrorCode errorCode2) throws IOException {
        Http2Stream[] http2StreamArr;
        Map map = null;
        try {
            shutdown(errorCode);
            errorCode = null;
        } catch (IOException e) {
            errorCode = e;
        }
        synchronized (this) {
            if (this.streams.isEmpty()) {
                http2StreamArr = null;
            } else {
                http2StreamArr = (Http2Stream[]) this.streams.values().toArray(new Http2Stream[this.streams.size()]);
                this.streams.clear();
            }
            if (this.pings != null) {
                Ping[] pingArr = (Ping[]) this.pings.values().toArray(new Ping[this.pings.size()]);
                this.pings = null;
                map = pingArr;
            }
        }
        if (http2StreamArr != null) {
            ErrorCode errorCode3 = errorCode;
            for (Http2Stream close : http2StreamArr) {
                try {
                    close.close(errorCode2);
                } catch (IOException e2) {
                    if (errorCode3 != null) {
                        errorCode3 = e2;
                    }
                }
            }
            errorCode = errorCode3;
        }
        if (map != null) {
            for (Ping cancel : map) {
                cancel.cancel();
            }
        }
        try {
            this.writer.close();
        } catch (ErrorCode errorCode22) {
            if (errorCode == null) {
                errorCode = errorCode22;
            }
        }
        try {
            this.socket.close();
        } catch (IOException e3) {
            errorCode = e3;
        }
        if (errorCode != null) {
            throw errorCode;
        }
    }

    public void start() throws IOException {
        start(true);
    }

    void start(boolean z) throws IOException {
        if (z) {
            this.writer.connectionPreface();
            this.writer.settings(this.okHttpSettings);
            z = this.okHttpSettings.getInitialWindowSize();
            if (!z) {
                this.writer.windowUpdate(0, (long) (z - true));
            }
        }
        new Thread(this.readerRunnable).start();
    }

    public void setSettings(Settings settings) throws IOException {
        synchronized (this.writer) {
            synchronized (this) {
                if (this.shutdown) {
                    throw new ConnectionShutdownException();
                }
                this.okHttpSettings.merge(settings);
                this.writer.settings(settings);
            }
        }
    }

    public synchronized boolean isShutdown() {
        return this.shutdown;
    }

    void pushRequestLater(int i, List<Header> list) {
        synchronized (this) {
            if (this.currentPushRequests.contains(Integer.valueOf(i))) {
                writeSynResetLater(i, ErrorCode.PROTOCOL_ERROR);
                return;
            }
            this.currentPushRequests.add(Integer.valueOf(i));
            final int i2 = i;
            final List<Header> list2 = list;
            this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Request[%s]", new Object[]{this.hostname, Integer.valueOf(i)}) {
                public void execute() {
                    /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
                    /*
                    r3 = this;
                    r0 = okhttp3.internal.http2.Http2Connection.this;
                    r0 = r0.pushObserver;
                    r1 = r5;
                    r2 = r6;
                    r0 = r0.onRequest(r1, r2);
                    if (r0 == 0) goto L_0x002e;
                L_0x000e:
                    r0 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ IOException -> 0x002e }
                    r0 = r0.writer;	 Catch:{ IOException -> 0x002e }
                    r1 = r5;	 Catch:{ IOException -> 0x002e }
                    r2 = okhttp3.internal.http2.ErrorCode.CANCEL;	 Catch:{ IOException -> 0x002e }
                    r0.rstStream(r1, r2);	 Catch:{ IOException -> 0x002e }
                    r0 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ IOException -> 0x002e }
                    monitor-enter(r0);	 Catch:{ IOException -> 0x002e }
                    r1 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ all -> 0x002b }
                    r1 = r1.currentPushRequests;	 Catch:{ all -> 0x002b }
                    r2 = r5;	 Catch:{ all -> 0x002b }
                    r2 = java.lang.Integer.valueOf(r2);	 Catch:{ all -> 0x002b }
                    r1.remove(r2);	 Catch:{ all -> 0x002b }
                    monitor-exit(r0);	 Catch:{ all -> 0x002b }
                    goto L_0x002e;	 Catch:{ all -> 0x002b }
                L_0x002b:
                    r1 = move-exception;	 Catch:{ all -> 0x002b }
                    monitor-exit(r0);	 Catch:{ all -> 0x002b }
                    throw r1;	 Catch:{ IOException -> 0x002e }
                L_0x002e:
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.4.execute():void");
                }
            });
        }
    }

    void pushHeadersLater(int i, List<Header> list, boolean z) {
        final int i2 = i;
        final List<Header> list2 = list;
        final boolean z2 = z;
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Headers[%s]", new Object[]{this.hostname, Integer.valueOf(i)}) {
            public void execute() {
                /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
                /*
                r4 = this;
                r0 = okhttp3.internal.http2.Http2Connection.this;
                r0 = r0.pushObserver;
                r1 = r5;
                r2 = r6;
                r3 = r7;
                r0 = r0.onHeaders(r1, r2, r3);
                if (r0 == 0) goto L_0x001b;
            L_0x0010:
                r1 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ IOException -> 0x0036 }
                r1 = r1.writer;	 Catch:{ IOException -> 0x0036 }
                r2 = r5;	 Catch:{ IOException -> 0x0036 }
                r3 = okhttp3.internal.http2.ErrorCode.CANCEL;	 Catch:{ IOException -> 0x0036 }
                r1.rstStream(r2, r3);	 Catch:{ IOException -> 0x0036 }
            L_0x001b:
                if (r0 != 0) goto L_0x0021;	 Catch:{ IOException -> 0x0036 }
            L_0x001d:
                r0 = r7;	 Catch:{ IOException -> 0x0036 }
                if (r0 == 0) goto L_0x0036;	 Catch:{ IOException -> 0x0036 }
            L_0x0021:
                r0 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ IOException -> 0x0036 }
                monitor-enter(r0);	 Catch:{ IOException -> 0x0036 }
                r1 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ all -> 0x0033 }
                r1 = r1.currentPushRequests;	 Catch:{ all -> 0x0033 }
                r2 = r5;	 Catch:{ all -> 0x0033 }
                r2 = java.lang.Integer.valueOf(r2);	 Catch:{ all -> 0x0033 }
                r1.remove(r2);	 Catch:{ all -> 0x0033 }
                monitor-exit(r0);	 Catch:{ all -> 0x0033 }
                goto L_0x0036;	 Catch:{ all -> 0x0033 }
            L_0x0033:
                r1 = move-exception;	 Catch:{ all -> 0x0033 }
                monitor-exit(r0);	 Catch:{ all -> 0x0033 }
                throw r1;	 Catch:{ IOException -> 0x0036 }
            L_0x0036:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.5.execute():void");
            }
        });
    }

    void pushDataLater(int i, BufferedSource bufferedSource, int i2, boolean z) throws IOException {
        final Buffer buffer = new Buffer();
        long j = (long) i2;
        bufferedSource.require(j);
        bufferedSource.read(buffer, j);
        if (buffer.size() != j) {
            bufferedSource = new StringBuilder();
            bufferedSource.append(buffer.size());
            bufferedSource.append(" != ");
            bufferedSource.append(i2);
            throw new IOException(bufferedSource.toString());
        }
        final int i3 = i;
        final int i4 = i2;
        final boolean z2 = z;
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Data[%s]", new Object[]{this.hostname, Integer.valueOf(i)}) {
            public void execute() {
                /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
                /*
                r5 = this;
                r0 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ IOException -> 0x0038 }
                r0 = r0.pushObserver;	 Catch:{ IOException -> 0x0038 }
                r1 = r4;	 Catch:{ IOException -> 0x0038 }
                r2 = r5;	 Catch:{ IOException -> 0x0038 }
                r3 = r6;	 Catch:{ IOException -> 0x0038 }
                r4 = r7;	 Catch:{ IOException -> 0x0038 }
                r0 = r0.onData(r1, r2, r3, r4);	 Catch:{ IOException -> 0x0038 }
                if (r0 == 0) goto L_0x001d;	 Catch:{ IOException -> 0x0038 }
            L_0x0012:
                r1 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ IOException -> 0x0038 }
                r1 = r1.writer;	 Catch:{ IOException -> 0x0038 }
                r2 = r4;	 Catch:{ IOException -> 0x0038 }
                r3 = okhttp3.internal.http2.ErrorCode.CANCEL;	 Catch:{ IOException -> 0x0038 }
                r1.rstStream(r2, r3);	 Catch:{ IOException -> 0x0038 }
            L_0x001d:
                if (r0 != 0) goto L_0x0023;	 Catch:{ IOException -> 0x0038 }
            L_0x001f:
                r0 = r7;	 Catch:{ IOException -> 0x0038 }
                if (r0 == 0) goto L_0x0038;	 Catch:{ IOException -> 0x0038 }
            L_0x0023:
                r0 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ IOException -> 0x0038 }
                monitor-enter(r0);	 Catch:{ IOException -> 0x0038 }
                r1 = okhttp3.internal.http2.Http2Connection.this;	 Catch:{ all -> 0x0035 }
                r1 = r1.currentPushRequests;	 Catch:{ all -> 0x0035 }
                r2 = r4;	 Catch:{ all -> 0x0035 }
                r2 = java.lang.Integer.valueOf(r2);	 Catch:{ all -> 0x0035 }
                r1.remove(r2);	 Catch:{ all -> 0x0035 }
                monitor-exit(r0);	 Catch:{ all -> 0x0035 }
                goto L_0x0038;	 Catch:{ all -> 0x0035 }
            L_0x0035:
                r1 = move-exception;	 Catch:{ all -> 0x0035 }
                monitor-exit(r0);	 Catch:{ all -> 0x0035 }
                throw r1;	 Catch:{ IOException -> 0x0038 }
            L_0x0038:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.6.execute():void");
            }
        });
    }

    void pushResetLater(int i, ErrorCode errorCode) {
        final int i2 = i;
        final ErrorCode errorCode2 = errorCode;
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Reset[%s]", new Object[]{this.hostname, Integer.valueOf(i)}) {
            public void execute() {
                Http2Connection.this.pushObserver.onReset(i2, errorCode2);
                synchronized (Http2Connection.this) {
                    Http2Connection.this.currentPushRequests.remove(Integer.valueOf(i2));
                }
            }
        });
    }
}
