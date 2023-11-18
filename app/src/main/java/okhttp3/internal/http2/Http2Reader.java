package okhttp3.internal.http2;

import android.support.v4.view.ViewCompat;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

final class Http2Reader implements Closeable {
    static final Logger logger = Logger.getLogger(Http2.class.getName());
    private final boolean client;
    private final ContinuationSource continuation = new ContinuationSource(this.source);
    final Reader hpackReader = new Reader(4096, this.continuation);
    private final BufferedSource source;

    interface Handler {
        void ackSettings();

        void alternateService(int i, String str, ByteString byteString, String str2, int i2, long j);

        void data(boolean z, int i, BufferedSource bufferedSource, int i2) throws IOException;

        void goAway(int i, ErrorCode errorCode, ByteString byteString);

        void headers(boolean z, int i, int i2, List<Header> list);

        void ping(boolean z, int i, int i2);

        void priority(int i, int i2, int i3, boolean z);

        void pushPromise(int i, int i2, List<Header> list) throws IOException;

        void rstStream(int i, ErrorCode errorCode);

        void settings(boolean z, Settings settings);

        void windowUpdate(int i, long j);
    }

    static final class ContinuationSource implements Source {
        byte flags;
        int left;
        int length;
        short padding;
        private final BufferedSource source;
        int streamId;

        public void close() throws IOException {
        }

        ContinuationSource(BufferedSource bufferedSource) {
            this.source = bufferedSource;
        }

        public long read(Buffer buffer, long j) throws IOException {
            while (this.left == 0) {
                this.source.skip((long) this.padding);
                this.padding = (short) 0;
                if ((this.flags & 4) != 0) {
                    return -1;
                }
                readContinuationHeader();
            }
            buffer = this.source.read(buffer, Math.min(j, (long) this.left));
            if (buffer == -1) {
                return -1;
            }
            this.left = (int) (((long) this.left) - buffer);
            return buffer;
        }

        public Timeout timeout() {
            return this.source.timeout();
        }

        private void readContinuationHeader() throws IOException {
            int i = this.streamId;
            int readMedium = Http2Reader.readMedium(this.source);
            this.left = readMedium;
            this.length = readMedium;
            byte readByte = (byte) (this.source.readByte() & 255);
            this.flags = (byte) (this.source.readByte() & 255);
            if (Http2Reader.logger.isLoggable(Level.FINE)) {
                Http2Reader.logger.fine(Http2.frameLog(true, this.streamId, this.length, readByte, this.flags));
            }
            this.streamId = this.source.readInt() & Integer.MAX_VALUE;
            if (readByte != (byte) 9) {
                throw Http2.ioException("%s != TYPE_CONTINUATION", Byte.valueOf(readByte));
            } else if (this.streamId != i) {
                throw Http2.ioException("TYPE_CONTINUATION streamId changed", new Object[0]);
            }
        }
    }

    Http2Reader(BufferedSource bufferedSource, boolean z) {
        this.source = bufferedSource;
        this.client = z;
    }

    public void readConnectionPreface(Handler handler) throws IOException {
        if (!this.client) {
            handler = this.source.readByteString((long) Http2.CONNECTION_PREFACE.size());
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(Util.format("<< CONNECTION %s", handler.hex()));
            }
            if (!Http2.CONNECTION_PREFACE.equals(handler)) {
                throw Http2.ioException("Expected a connection header but was %s", handler.utf8());
            }
        } else if (nextFrame(true, handler) == null) {
            throw Http2.ioException("Required SETTINGS preface not received", new Object[0]);
        }
    }

    public boolean nextFrame(boolean r7, okhttp3.internal.http2.Http2Reader.Handler r8) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r6 = this;
        r0 = 0;
        r1 = r6.source;	 Catch:{ IOException -> 0x0097 }
        r2 = 9;	 Catch:{ IOException -> 0x0097 }
        r1.require(r2);	 Catch:{ IOException -> 0x0097 }
        r1 = r6.source;
        r1 = readMedium(r1);
        r2 = 1;
        if (r1 < 0) goto L_0x0088;
    L_0x0011:
        r3 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        if (r1 <= r3) goto L_0x0017;
    L_0x0015:
        goto L_0x0088;
    L_0x0017:
        r3 = r6.source;
        r3 = r3.readByte();
        r3 = r3 & 255;
        r3 = (byte) r3;
        if (r7 == 0) goto L_0x0034;
    L_0x0022:
        r7 = 4;
        if (r3 == r7) goto L_0x0034;
    L_0x0025:
        r7 = "Expected a SETTINGS frame but was %s";
        r8 = new java.lang.Object[r2];
        r1 = java.lang.Byte.valueOf(r3);
        r8[r0] = r1;
        r7 = okhttp3.internal.http2.Http2.ioException(r7, r8);
        throw r7;
    L_0x0034:
        r7 = r6.source;
        r7 = r7.readByte();
        r7 = r7 & 255;
        r7 = (byte) r7;
        r0 = r6.source;
        r0 = r0.readInt();
        r4 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r0 = r0 & r4;
        r4 = logger;
        r5 = java.util.logging.Level.FINE;
        r4 = r4.isLoggable(r5);
        if (r4 == 0) goto L_0x005a;
    L_0x0051:
        r4 = logger;
        r5 = okhttp3.internal.http2.Http2.frameLog(r2, r0, r1, r3, r7);
        r4.fine(r5);
    L_0x005a:
        switch(r3) {
            case 0: goto L_0x0084;
            case 1: goto L_0x0080;
            case 2: goto L_0x007c;
            case 3: goto L_0x0078;
            case 4: goto L_0x0074;
            case 5: goto L_0x0070;
            case 6: goto L_0x006c;
            case 7: goto L_0x0068;
            case 8: goto L_0x0064;
            default: goto L_0x005d;
        };
    L_0x005d:
        r7 = r6.source;
        r0 = (long) r1;
        r7.skip(r0);
        goto L_0x0087;
    L_0x0064:
        r6.readWindowUpdate(r8, r1, r7, r0);
        goto L_0x0087;
    L_0x0068:
        r6.readGoAway(r8, r1, r7, r0);
        goto L_0x0087;
    L_0x006c:
        r6.readPing(r8, r1, r7, r0);
        goto L_0x0087;
    L_0x0070:
        r6.readPushPromise(r8, r1, r7, r0);
        goto L_0x0087;
    L_0x0074:
        r6.readSettings(r8, r1, r7, r0);
        goto L_0x0087;
    L_0x0078:
        r6.readRstStream(r8, r1, r7, r0);
        goto L_0x0087;
    L_0x007c:
        r6.readPriority(r8, r1, r7, r0);
        goto L_0x0087;
    L_0x0080:
        r6.readHeaders(r8, r1, r7, r0);
        goto L_0x0087;
    L_0x0084:
        r6.readData(r8, r1, r7, r0);
    L_0x0087:
        return r2;
    L_0x0088:
        r7 = "FRAME_SIZE_ERROR: %s";
        r8 = new java.lang.Object[r2];
        r1 = java.lang.Integer.valueOf(r1);
        r8[r0] = r1;
        r7 = okhttp3.internal.http2.Http2.ioException(r7, r8);
        throw r7;
    L_0x0097:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Reader.nextFrame(boolean, okhttp3.internal.http2.Http2Reader$Handler):boolean");
    }

    private void readHeaders(Handler handler, int i, byte b, int i2) throws IOException {
        short s = (short) 0;
        if (i2 == 0) {
            throw Http2.ioException("PROTOCOL_ERROR: TYPE_HEADERS streamId == 0", new Object[0]);
        }
        boolean z = (b & 1) != 0;
        if ((b & 8) != 0) {
            s = (short) (this.source.readByte() & 255);
        }
        if ((b & 32) != 0) {
            readPriority(handler, i2);
            i -= 5;
        }
        handler.headers(z, i2, (byte) -1, readHeaderBlock(lengthWithoutPadding(i, b, s), s, b, i2));
    }

    private List<Header> readHeaderBlock(int i, short s, byte b, int i2) throws IOException {
        ContinuationSource continuationSource = this.continuation;
        this.continuation.left = i;
        continuationSource.length = i;
        this.continuation.padding = s;
        this.continuation.flags = b;
        this.continuation.streamId = i2;
        this.hpackReader.readHeaders();
        return this.hpackReader.getAndResetHeaderList();
    }

    private void readData(Handler handler, int i, byte b, int i2) throws IOException {
        short s = (short) 0;
        if (i2 == 0) {
            throw Http2.ioException("PROTOCOL_ERROR: TYPE_DATA streamId == 0", new Object[0]);
        }
        Object obj = 1;
        boolean z = (b & 1) != 0;
        if ((b & 32) == 0) {
            obj = null;
        }
        if (obj != null) {
            throw Http2.ioException("PROTOCOL_ERROR: FLAG_COMPRESSED without SETTINGS_COMPRESS_DATA", new Object[0]);
        }
        if ((b & 8) != 0) {
            s = (short) (this.source.readByte() & 255);
        }
        handler.data(z, i2, this.source, lengthWithoutPadding(i, b, s));
        this.source.skip((long) s);
    }

    private void readPriority(Handler handler, int i, byte b, int i2) throws IOException {
        if (i != 5) {
            throw Http2.ioException("TYPE_PRIORITY length: %d != 5", new Object[]{Integer.valueOf(i)});
        } else if (i2 == 0) {
            throw Http2.ioException("TYPE_PRIORITY streamId == 0", new Object[0]);
        } else {
            readPriority(handler, i2);
        }
    }

    private void readPriority(Handler handler, int i) throws IOException {
        int readInt = this.source.readInt();
        handler.priority(i, readInt & Integer.MAX_VALUE, (this.source.readByte() & 255) + 1, (Integer.MIN_VALUE & readInt) != 0);
    }

    private void readRstStream(Handler handler, int i, byte b, int i2) throws IOException {
        if (i != 4) {
            throw Http2.ioException("TYPE_RST_STREAM length: %d != 4", new Object[]{Integer.valueOf(i)});
        } else if (i2 == 0) {
            throw Http2.ioException("TYPE_RST_STREAM streamId == 0", new Object[0]);
        } else {
            ErrorCode fromHttp2 = ErrorCode.fromHttp2(this.source.readInt());
            if (fromHttp2 == null) {
                throw Http2.ioException("TYPE_RST_STREAM unexpected error code: %d", new Object[]{Integer.valueOf(i)});
            } else {
                handler.rstStream(i2, fromHttp2);
            }
        }
    }

    private void readSettings(Handler handler, int i, byte b, int i2) throws IOException {
        if (i2 != 0) {
            throw Http2.ioException("TYPE_SETTINGS streamId != 0", new Object[0]);
        } else if ((b & (byte) 1) != (byte) 0) {
            if (i != 0) {
                throw Http2.ioException("FRAME_SIZE_ERROR ack frame should be empty!", new Object[0]);
            }
            handler.ackSettings();
        } else if (i % 6 != (byte) 0) {
            throw Http2.ioException("TYPE_SETTINGS length %% 6 != 0: %s", new Object[]{Integer.valueOf(i)});
        } else {
            b = new Settings();
            for (int i3 = 0; i3 < i; i3 += 6) {
                int readShort = this.source.readShort();
                int readInt = this.source.readInt();
                switch (readShort) {
                    case 1:
                    case 6:
                        break;
                    case 2:
                        if (!(readInt == 0 || readInt == 1)) {
                            throw Http2.ioException("PROTOCOL_ERROR SETTINGS_ENABLE_PUSH != 0 or 1", new Object[0]);
                        }
                    case 3:
                        readShort = 4;
                        break;
                    case 4:
                        readShort = 7;
                        if (readInt >= 0) {
                            break;
                        }
                        throw Http2.ioException("PROTOCOL_ERROR SETTINGS_INITIAL_WINDOW_SIZE > 2^31 - 1", new Object[0]);
                    case 5:
                        if (readInt >= 16384 && readInt <= ViewCompat.MEASURED_SIZE_MASK) {
                            break;
                        }
                        throw Http2.ioException("PROTOCOL_ERROR SETTINGS_MAX_FRAME_SIZE: %s", new Object[]{Integer.valueOf(readInt)});
                        break;
                    default:
                        break;
                }
                b.set(readShort, readInt);
            }
            handler.settings(false, b);
        }
    }

    private void readPushPromise(Handler handler, int i, byte b, int i2) throws IOException {
        short s = (short) 0;
        if (i2 == 0) {
            throw Http2.ioException("PROTOCOL_ERROR: TYPE_PUSH_PROMISE streamId == 0", new Object[0]);
        }
        if ((b & 8) != 0) {
            s = (short) (this.source.readByte() & 255);
        }
        handler.pushPromise(i2, this.source.readInt() & Integer.MAX_VALUE, readHeaderBlock(lengthWithoutPadding(i - 4, b, s), s, b, i2));
    }

    private void readPing(Handler handler, int i, byte b, int i2) throws IOException {
        boolean z = false;
        if (i != 8) {
            throw Http2.ioException("TYPE_PING length != 8: %s", new Object[]{Integer.valueOf(i)});
        } else if (i2 != 0) {
            throw Http2.ioException("TYPE_PING streamId != 0", new Object[0]);
        } else {
            i = this.source.readInt();
            i2 = this.source.readInt();
            if ((b & (byte) 1) != (byte) 0) {
                z = true;
            }
            handler.ping(z, i, i2);
        }
    }

    private void readGoAway(Handler handler, int i, byte b, int i2) throws IOException {
        if (i < 8) {
            throw Http2.ioException("TYPE_GOAWAY length < 8: %s", new Object[]{Integer.valueOf(i)});
        } else if (i2 != 0) {
            throw Http2.ioException("TYPE_GOAWAY streamId != 0", new Object[0]);
        } else {
            i2 = this.source.readInt();
            i -= 8;
            ErrorCode fromHttp2 = ErrorCode.fromHttp2(this.source.readInt());
            if (fromHttp2 == null) {
                throw Http2.ioException("TYPE_GOAWAY unexpected error code: %d", new Object[]{Integer.valueOf(r2)});
            }
            b = ByteString.EMPTY;
            if (i > 0) {
                b = this.source.readByteString((long) i);
            }
            handler.goAway(i2, fromHttp2, b);
        }
    }

    private void readWindowUpdate(Handler handler, int i, byte b, int i2) throws IOException {
        if (i != 4) {
            throw Http2.ioException("TYPE_WINDOW_UPDATE length !=4: %s", new Object[]{Integer.valueOf(i)});
        }
        long readInt = ((long) this.source.readInt()) & 2147483647L;
        if (readInt == 0) {
            throw Http2.ioException("windowSizeIncrement was 0", new Object[]{Long.valueOf(readInt)});
        } else {
            handler.windowUpdate(i2, readInt);
        }
    }

    public void close() throws IOException {
        this.source.close();
    }

    static int readMedium(BufferedSource bufferedSource) throws IOException {
        return (bufferedSource.readByte() & 255) | (((bufferedSource.readByte() & 255) << 16) | ((bufferedSource.readByte() & 255) << 8));
    }

    static int lengthWithoutPadding(int i, byte b, short s) throws IOException {
        if ((b & 8) != (byte) 0) {
            short s2 = i - 1;
        }
        if (s <= s2) {
            return (short) (s2 - s);
        }
        throw Http2.ioException("PROTOCOL_ERROR padding %s > remaining length %s", Short.valueOf(s), Integer.valueOf(s2));
    }
}
