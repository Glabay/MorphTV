package okio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.logging.Logger;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

public final class Okio {
    static final Logger logger = Logger.getLogger(Okio.class.getName());

    /* renamed from: okio.Okio$1 */
    class C14331 implements Sink {
        final /* synthetic */ OutputStream val$out;
        final /* synthetic */ Timeout val$timeout;

        C14331(Timeout timeout, OutputStream outputStream) {
            this.val$timeout = timeout;
            this.val$out = outputStream;
        }

        public void write(Buffer buffer, long j) throws IOException {
            Util.checkOffsetAndCount(buffer.size, 0, j);
            while (j > 0) {
                this.val$timeout.throwIfReached();
                Segment segment = buffer.head;
                int min = (int) Math.min(j, (long) (segment.limit - segment.pos));
                this.val$out.write(segment.data, segment.pos, min);
                segment.pos += min;
                long j2 = (long) min;
                long j3 = j - j2;
                buffer.size -= j2;
                if (segment.pos == segment.limit) {
                    buffer.head = segment.pop();
                    SegmentPool.recycle(segment);
                }
                j = j3;
            }
        }

        public void flush() throws IOException {
            this.val$out.flush();
        }

        public void close() throws IOException {
            this.val$out.close();
        }

        public Timeout timeout() {
            return this.val$timeout;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("sink(");
            stringBuilder.append(this.val$out);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    /* renamed from: okio.Okio$2 */
    class C14342 implements Source {
        final /* synthetic */ InputStream val$in;
        final /* synthetic */ Timeout val$timeout;

        C14342(Timeout timeout, InputStream inputStream) {
            this.val$timeout = timeout;
            this.val$in = inputStream;
        }

        public long read(Buffer buffer, long j) throws IOException {
            if (j < 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("byteCount < 0: ");
                stringBuilder.append(j);
                throw new IllegalArgumentException(stringBuilder.toString());
            } else if (j == 0) {
                return 0;
            } else {
                try {
                    this.val$timeout.throwIfReached();
                    Segment writableSegment = buffer.writableSegment(1);
                    j = this.val$in.read(writableSegment.data, writableSegment.limit, (int) Math.min(j, (long) (8192 - writableSegment.limit)));
                    if (j == -1) {
                        return -1;
                    }
                    writableSegment.limit += j;
                    j = (long) j;
                    buffer.size += j;
                    return j;
                } catch (Buffer buffer2) {
                    if (Okio.isAndroidGetsocknameError(buffer2) != null) {
                        throw new IOException(buffer2);
                    }
                    throw buffer2;
                }
            }
        }

        public void close() throws IOException {
            this.val$in.close();
        }

        public Timeout timeout() {
            return this.val$timeout;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("source(");
            stringBuilder.append(this.val$in);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    /* renamed from: okio.Okio$3 */
    class C14353 implements Sink {
        public void close() throws IOException {
        }

        public void flush() throws IOException {
        }

        C14353() {
        }

        public void write(Buffer buffer, long j) throws IOException {
            buffer.skip(j);
        }

        public Timeout timeout() {
            return Timeout.NONE;
        }
    }

    private Okio() {
    }

    public static BufferedSource buffer(Source source) {
        return new RealBufferedSource(source);
    }

    public static BufferedSink buffer(Sink sink) {
        return new RealBufferedSink(sink);
    }

    public static Sink sink(OutputStream outputStream) {
        return sink(outputStream, new Timeout());
    }

    private static Sink sink(OutputStream outputStream, Timeout timeout) {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        } else if (timeout != null) {
            return new C14331(timeout, outputStream);
        } else {
            throw new IllegalArgumentException("timeout == null");
        }
    }

    public static Sink sink(Socket socket) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("socket == null");
        }
        Timeout timeout = timeout(socket);
        return timeout.sink(sink(socket.getOutputStream(), timeout));
    }

    public static Source source(InputStream inputStream) {
        return source(inputStream, new Timeout());
    }

    private static Source source(InputStream inputStream, Timeout timeout) {
        if (inputStream == null) {
            throw new IllegalArgumentException("in == null");
        } else if (timeout != null) {
            return new C14342(timeout, inputStream);
        } else {
            throw new IllegalArgumentException("timeout == null");
        }
    }

    public static Source source(File file) throws FileNotFoundException {
        if (file != null) {
            return source(new FileInputStream(file));
        }
        throw new IllegalArgumentException("file == null");
    }

    @IgnoreJRERequirement
    public static Source source(Path path, OpenOption... openOptionArr) throws IOException {
        if (path != null) {
            return source(Files.newInputStream(path, openOptionArr));
        }
        throw new IllegalArgumentException("path == null");
    }

    public static Sink sink(File file) throws FileNotFoundException {
        if (file != null) {
            return sink(new FileOutputStream(file));
        }
        throw new IllegalArgumentException("file == null");
    }

    public static Sink appendingSink(File file) throws FileNotFoundException {
        if (file != null) {
            return sink(new FileOutputStream(file, true));
        }
        throw new IllegalArgumentException("file == null");
    }

    @IgnoreJRERequirement
    public static Sink sink(Path path, OpenOption... openOptionArr) throws IOException {
        if (path != null) {
            return sink(Files.newOutputStream(path, openOptionArr));
        }
        throw new IllegalArgumentException("path == null");
    }

    public static Sink blackhole() {
        return new C14353();
    }

    public static Source source(Socket socket) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("socket == null");
        }
        Timeout timeout = timeout(socket);
        return timeout.source(source(socket.getInputStream(), timeout));
    }

    private static AsyncTimeout timeout(Socket socket) {
        return new Okio$4(socket);
    }

    static boolean isAndroidGetsocknameError(AssertionError assertionError) {
        return (assertionError.getCause() == null || assertionError.getMessage() == null || assertionError.getMessage().contains("getsockname failed") == null) ? null : true;
    }
}
