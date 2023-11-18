package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

@Beta
public final class CharStreams {
    private static final int BUF_SIZE = 2048;

    private static final class NullWriter extends Writer {
        private static final NullWriter INSTANCE = new NullWriter();

        public Writer append(char c) {
            return this;
        }

        public void close() {
        }

        public void flush() {
        }

        public String toString() {
            return "CharStreams.nullWriter()";
        }

        public void write(int i) {
        }

        private NullWriter() {
        }

        public void write(char[] cArr) {
            Preconditions.checkNotNull(cArr);
        }

        public void write(char[] cArr, int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2 + i, cArr.length);
        }

        public void write(String str) {
            Preconditions.checkNotNull(str);
        }

        public void write(String str, int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2 + i, str.length());
        }

        public Writer append(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return this;
        }

        public Writer append(CharSequence charSequence, int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, charSequence.length());
            return this;
        }
    }

    private CharStreams() {
    }

    public static long copy(Readable readable, Appendable appendable) throws IOException {
        Preconditions.checkNotNull(readable);
        Preconditions.checkNotNull(appendable);
        CharBuffer allocate = CharBuffer.allocate(2048);
        long j = 0;
        while (readable.read(allocate) != -1) {
            allocate.flip();
            appendable.append(allocate);
            long remaining = j + ((long) allocate.remaining());
            allocate.clear();
            j = remaining;
        }
        return j;
    }

    public static String toString(Readable readable) throws IOException {
        return toStringBuilder(readable).toString();
    }

    private static StringBuilder toStringBuilder(Readable readable) throws IOException {
        Appendable stringBuilder = new StringBuilder();
        copy(readable, stringBuilder);
        return stringBuilder;
    }

    public static List<String> readLines(Readable readable) throws IOException {
        List<String> arrayList = new ArrayList();
        LineReader lineReader = new LineReader(readable);
        while (true) {
            readable = lineReader.readLine();
            if (readable == null) {
                return arrayList;
            }
            arrayList.add(readable);
        }
    }

    public static <T> T readLines(Readable readable, LineProcessor<T> lineProcessor) throws IOException {
        Preconditions.checkNotNull(readable);
        Preconditions.checkNotNull(lineProcessor);
        LineReader lineReader = new LineReader(readable);
        do {
            readable = lineReader.readLine();
            if (readable == null) {
                break;
            }
        } while (lineProcessor.processLine(readable) != null);
        return lineProcessor.getResult();
    }

    public static void skipFully(Reader reader, long j) throws IOException {
        Preconditions.checkNotNull(reader);
        while (j > 0) {
            long skip = reader.skip(j);
            if (skip != 0) {
                j -= skip;
            } else if (reader.read() == -1) {
                throw new EOFException();
            } else {
                j--;
            }
        }
    }

    public static Writer nullWriter() {
        return NullWriter.INSTANCE;
    }

    public static Writer asWriter(Appendable appendable) {
        if (appendable instanceof Writer) {
            return (Writer) appendable;
        }
        return new AppendableWriter(appendable);
    }

    static Reader asReader(final Readable readable) {
        Preconditions.checkNotNull(readable);
        if (readable instanceof Reader) {
            return (Reader) readable;
        }
        return new Reader() {
            public int read(char[] cArr, int i, int i2) throws IOException {
                return read(CharBuffer.wrap(cArr, i, i2));
            }

            public int read(CharBuffer charBuffer) throws IOException {
                return readable.read(charBuffer);
            }

            public void close() throws IOException {
                if (readable instanceof Closeable) {
                    ((Closeable) readable).close();
                }
            }
        };
    }
}
