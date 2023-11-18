package com.google.common.io;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

@GwtCompatible(emulated = true)
final class GwtWorkarounds {

    interface CharInput {
        void close() throws IOException;

        int read() throws IOException;
    }

    interface CharOutput {
        void close() throws IOException;

        void flush() throws IOException;

        void write(char c) throws IOException;
    }

    interface ByteOutput {
        void close() throws IOException;

        void flush() throws IOException;

        void write(byte b) throws IOException;
    }

    interface ByteInput {
        void close() throws IOException;

        int read() throws IOException;
    }

    private GwtWorkarounds() {
    }

    @GwtIncompatible("Reader")
    static CharInput asCharInput(final Reader reader) {
        Preconditions.checkNotNull(reader);
        return new CharInput() {
            public int read() throws IOException {
                return reader.read();
            }

            public void close() throws IOException {
                reader.close();
            }
        };
    }

    static CharInput asCharInput(final CharSequence charSequence) {
        Preconditions.checkNotNull(charSequence);
        return new CharInput() {
            int index = null;

            public int read() {
                if (this.index >= charSequence.length()) {
                    return -1;
                }
                CharSequence charSequence = charSequence;
                int i = this.index;
                this.index = i + 1;
                return charSequence.charAt(i);
            }

            public void close() {
                this.index = charSequence.length();
            }
        };
    }

    @GwtIncompatible("InputStream")
    static InputStream asInputStream(final ByteInput byteInput) {
        Preconditions.checkNotNull(byteInput);
        return new InputStream() {
            public int read() throws IOException {
                return byteInput.read();
            }

            public int read(byte[] bArr, int i, int i2) throws IOException {
                Preconditions.checkNotNull(bArr);
                Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
                if (i2 == 0) {
                    return null;
                }
                int read = read();
                if (read == -1) {
                    return -1;
                }
                bArr[i] = (byte) read;
                for (read = 1; read < i2; read++) {
                    int read2 = read();
                    if (read2 == -1) {
                        return read;
                    }
                    bArr[i + read] = (byte) read2;
                }
                return i2;
            }

            public void close() throws IOException {
                byteInput.close();
            }
        };
    }

    @GwtIncompatible("OutputStream")
    static OutputStream asOutputStream(final ByteOutput byteOutput) {
        Preconditions.checkNotNull(byteOutput);
        return new OutputStream() {
            public void write(int i) throws IOException {
                byteOutput.write((byte) i);
            }

            public void flush() throws IOException {
                byteOutput.flush();
            }

            public void close() throws IOException {
                byteOutput.close();
            }
        };
    }

    @GwtIncompatible("Writer")
    static CharOutput asCharOutput(final Writer writer) {
        Preconditions.checkNotNull(writer);
        return new CharOutput() {
            public void write(char c) throws IOException {
                writer.append(c);
            }

            public void flush() throws IOException {
                writer.flush();
            }

            public void close() throws IOException {
                writer.close();
            }
        };
    }

    static CharOutput stringBuilderOutput(int i) {
        final StringBuilder stringBuilder = new StringBuilder(i);
        return new CharOutput() {
            public void close() {
            }

            public void flush() {
            }

            public void write(char c) {
                stringBuilder.append(c);
            }

            public String toString() {
                return stringBuilder.toString();
            }
        };
    }
}
