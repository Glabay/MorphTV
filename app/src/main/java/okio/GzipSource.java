package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Inflater;

public final class GzipSource implements Source {
    private static final byte FCOMMENT = (byte) 4;
    private static final byte FEXTRA = (byte) 2;
    private static final byte FHCRC = (byte) 1;
    private static final byte FNAME = (byte) 3;
    private static final byte SECTION_BODY = (byte) 1;
    private static final byte SECTION_DONE = (byte) 3;
    private static final byte SECTION_HEADER = (byte) 0;
    private static final byte SECTION_TRAILER = (byte) 2;
    private final CRC32 crc = new CRC32();
    private final Inflater inflater;
    private final InflaterSource inflaterSource;
    private int section = 0;
    private final BufferedSource source;

    public GzipSource(Source source) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        this.inflater = new Inflater(true);
        this.source = Okio.buffer(source);
        this.inflaterSource = new InflaterSource(this.source, this.inflater);
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
            if (this.section == 0) {
                consumeHeader();
                this.section = 1;
            }
            if (this.section == 1) {
                long j2 = buffer.size;
                j = this.inflaterSource.read(buffer, j);
                if (j != -1) {
                    updateCrc(buffer, j2, j);
                    return j;
                }
                this.section = 2;
            }
            if (this.section == 2) {
                consumeTrailer();
                this.section = 3;
                if (this.source.exhausted() == null) {
                    throw new IOException("gzip finished without exhausting source");
                }
            }
            return -1;
        }
    }

    private void consumeHeader() throws IOException {
        this.source.require(10);
        byte b = this.source.buffer().getByte(3);
        Object obj = ((b >> 1) & 1) == 1 ? 1 : null;
        if (obj != null) {
            updateCrc(r6.source.buffer(), 0, 10);
        }
        checkEqual("ID1ID2", 8075, r6.source.readShort());
        r6.source.skip(8);
        if (((b >> 2) & 1) == 1) {
            r6.source.require(2);
            if (obj != null) {
                updateCrc(r6.source.buffer(), 0, 2);
            }
            long readShortLe = (long) r6.source.buffer().readShortLe();
            r6.source.require(readShortLe);
            if (obj != null) {
                updateCrc(r6.source.buffer(), 0, readShortLe);
            }
            r6.source.skip(readShortLe);
        }
        if (((b >> 3) & 1) == 1) {
            long indexOf = r6.source.indexOf((byte) 0);
            if (indexOf == -1) {
                throw new EOFException();
            }
            if (obj != null) {
                updateCrc(r6.source.buffer(), 0, indexOf + 1);
            }
            r6.source.skip(indexOf + 1);
        }
        if (((b >> 4) & 1) == 1) {
            long indexOf2 = r6.source.indexOf((byte) 0);
            if (indexOf2 == -1) {
                throw new EOFException();
            }
            if (obj != null) {
                updateCrc(r6.source.buffer(), 0, indexOf2 + 1);
            }
            r6.source.skip(indexOf2 + 1);
        }
        if (obj != null) {
            checkEqual("FHCRC", r6.source.readShortLe(), (short) ((int) r6.crc.getValue()));
            r6.crc.reset();
        }
    }

    private void consumeTrailer() throws IOException {
        checkEqual("CRC", this.source.readIntLe(), (int) this.crc.getValue());
        checkEqual("ISIZE", this.source.readIntLe(), (int) this.inflater.getBytesWritten());
    }

    public Timeout timeout() {
        return this.source.timeout();
    }

    public void close() throws IOException {
        this.inflaterSource.close();
    }

    private void updateCrc(Buffer buffer, long j, long j2) {
        buffer = buffer.head;
        while (j >= ((long) (buffer.limit - buffer.pos))) {
            long j3 = j - ((long) (buffer.limit - buffer.pos));
            buffer = buffer.next;
            j = j3;
        }
        while (j2 > 0) {
            j = (int) (((long) buffer.pos) + j);
            int min = (int) Math.min((long) (buffer.limit - j), j2);
            this.crc.update(buffer.data, j, min);
            j3 = j2 - ((long) min);
            buffer = buffer.next;
            j = 0;
            j2 = j3;
        }
    }

    private void checkEqual(String str, int i, int i2) throws IOException {
        if (i2 != i) {
            throw new IOException(String.format("%s: actual 0x%08x != expected 0x%08x", new Object[]{str, Integer.valueOf(i2), Integer.valueOf(i)}));
        }
    }
}
