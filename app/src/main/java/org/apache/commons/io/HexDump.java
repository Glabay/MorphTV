package org.apache.commons.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class HexDump {
    public static final String EOL = System.getProperty("line.separator");
    private static final char[] _hexcodes = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final int[] _shifts = new int[]{28, 24, 20, 16, 12, 8, 4, 0};

    public static void dump(byte[] bArr, long j, OutputStream outputStream, int i) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (i >= 0) {
            if (i < bArr.length) {
                if (outputStream == null) {
                    throw new IllegalArgumentException("cannot write to nullstream");
                }
                long j2 = j + ((long) i);
                StringBuilder stringBuilder = new StringBuilder(74);
                while (i < bArr.length) {
                    int i2;
                    int length = bArr.length - i;
                    if (length > 16) {
                        length = 16;
                    }
                    dump(stringBuilder, j2).append(' ');
                    for (i2 = 0; i2 < 16; i2++) {
                        if (i2 < length) {
                            dump(stringBuilder, bArr[i2 + i]);
                        } else {
                            stringBuilder.append("  ");
                        }
                        stringBuilder.append(' ');
                    }
                    for (int i3 = 0; i3 < length; i3++) {
                        i2 = i3 + i;
                        if (bArr[i2] < (byte) 32 || bArr[i2] >= Byte.MAX_VALUE) {
                            stringBuilder.append('.');
                        } else {
                            stringBuilder.append((char) bArr[i2]);
                        }
                    }
                    stringBuilder.append(EOL);
                    outputStream.write(stringBuilder.toString().getBytes(Charset.defaultCharset()));
                    outputStream.flush();
                    stringBuilder.setLength(0);
                    i += 16;
                    j2 += (long) length;
                }
                return;
            }
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("illegal index: ");
        stringBuilder2.append(i);
        stringBuilder2.append(" into array of length ");
        stringBuilder2.append(bArr.length);
        throw new ArrayIndexOutOfBoundsException(stringBuilder2.toString());
    }

    private static StringBuilder dump(StringBuilder stringBuilder, long j) {
        for (int i = 0; i < 8; i++) {
            stringBuilder.append(_hexcodes[((int) (j >> _shifts[i])) & 15]);
        }
        return stringBuilder;
    }

    private static StringBuilder dump(StringBuilder stringBuilder, byte b) {
        for (int i = 0; i < 2; i++) {
            stringBuilder.append(_hexcodes[(b >> _shifts[i + 6]) & 15]);
        }
        return stringBuilder;
    }
}
