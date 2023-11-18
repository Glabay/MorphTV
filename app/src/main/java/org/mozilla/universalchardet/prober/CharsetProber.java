package org.mozilla.universalchardet.prober;

import java.nio.ByteBuffer;

public abstract class CharsetProber {
    public static final int ASCII_A = 97;
    public static final int ASCII_A_CAPITAL = 65;
    public static final int ASCII_GT = 62;
    public static final int ASCII_LT = 60;
    public static final int ASCII_SP = 32;
    public static final int ASCII_Z = 122;
    public static final int ASCII_Z_CAPITAL = 90;
    public static final float SHORTCUT_THRESHOLD = 0.95f;

    public enum ProbingState {
        DETECTING,
        FOUND_IT,
        NOT_ME
    }

    private boolean isAscii(byte b) {
        return (b & 128) == 0;
    }

    private boolean isAsciiSymbol(byte b) {
        int i = b & 255;
        if (i >= 65 && (i <= 90 || i >= 97)) {
            if (i <= ASCII_Z) {
                return false;
            }
        }
        return true;
    }

    public ByteBuffer filterWithEnglishLetters(byte[] bArr, int i, int i2) {
        ByteBuffer allocate = ByteBuffer.allocate(i2);
        i2 += i;
        int i3 = i;
        Object obj = null;
        while (i < i2) {
            byte b = bArr[i];
            if (b == (byte) 62) {
                obj = null;
            } else if (b == (byte) 60) {
                obj = 1;
            }
            if (isAscii(b) && isAsciiSymbol(b)) {
                if (i > i3 && r2 == null) {
                    allocate.put(bArr, i3, i - i3);
                    allocate.put((byte) 32);
                }
                i3 = i + 1;
            }
            i++;
        }
        if (obj == null && i > i3) {
            allocate.put(bArr, i3, i - i3);
        }
        return allocate;
    }

    public ByteBuffer filterWithoutEnglishLetters(byte[] bArr, int i, int i2) {
        ByteBuffer allocate = ByteBuffer.allocate(i2);
        i2 += i;
        int i3 = i;
        Object obj = null;
        while (i < i2) {
            byte b = bArr[i];
            if (!isAscii(b)) {
                obj = 1;
            } else if (isAsciiSymbol(b)) {
                if (obj == null || i <= i3) {
                    i3 = i + 1;
                } else {
                    allocate.put(bArr, i3, i - i3);
                    allocate.put((byte) 32);
                    i3 = i + 1;
                    obj = null;
                }
            }
            i++;
        }
        if (obj != null && i > i3) {
            allocate.put(bArr, i3, i - i3);
        }
        return allocate;
    }

    public abstract String getCharSetName();

    public abstract float getConfidence();

    public abstract ProbingState getState();

    public abstract ProbingState handleData(byte[] bArr, int i, int i2);

    public abstract void reset();

    public abstract void setOption();
}
