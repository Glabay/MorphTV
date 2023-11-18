package org.mozilla.universalchardet.prober;

import java.nio.ByteBuffer;
import org.mozilla.universalchardet.Constants;
import org.mozilla.universalchardet.prober.CharsetProber.ProbingState;

public class Latin1Prober extends CharsetProber {
    public static final byte ACO = (byte) 5;
    public static final byte ACV = (byte) 4;
    public static final byte ASC = (byte) 2;
    public static final byte ASO = (byte) 7;
    public static final byte ASS = (byte) 3;
    public static final byte ASV = (byte) 6;
    public static final int CLASS_NUM = 8;
    public static final int FREQ_CAT_NUM = 4;
    public static final byte OTH = (byte) 1;
    public static final byte UDF = (byte) 0;
    private static final byte[] latin1CharToClass = new byte[]{(byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 1, (byte) 7, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 5, (byte) 1, (byte) 5, (byte) 0, (byte) 5, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 7, (byte) 1, (byte) 7, (byte) 0, (byte) 7, (byte) 5, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 5, (byte) 5, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 5, (byte) 5, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 1, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 5, (byte) 5, (byte) 5, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 7, (byte) 7, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 7, (byte) 7, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 1, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 7, (byte) 7, (byte) 7};
    private static final byte[] latin1ClassModel = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 0, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 0, (byte) 3, (byte) 3, (byte) 3, (byte) 1, (byte) 1, (byte) 3, (byte) 3, (byte) 0, (byte) 3, (byte) 3, (byte) 3, (byte) 1, (byte) 2, (byte) 1, (byte) 2, (byte) 0, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 0, (byte) 3, (byte) 1, (byte) 3, (byte) 1, (byte) 1, (byte) 1, (byte) 3, (byte) 0, (byte) 3, (byte) 1, (byte) 3, (byte) 1, (byte) 1, (byte) 3, (byte) 3};
    private int[] freqCounter = new int[4];
    private byte lastCharClass;
    private ProbingState state;

    public Latin1Prober() {
        reset();
    }

    public String getCharSetName() {
        return Constants.CHARSET_WINDOWS_1252;
    }

    public float getConfidence() {
        if (this.state == ProbingState.NOT_ME) {
            return 0.01f;
        }
        float f;
        int i = 0;
        for (int i2 : this.freqCounter) {
            i += i2;
        }
        float f2 = 0.0f;
        if (i <= 0) {
            f = 0.0f;
        } else {
            f = (float) i;
            f = ((((float) this.freqCounter[3]) * 1.0f) / f) - ((((float) this.freqCounter[1]) * 20.0f) / f);
        }
        if (f >= 0.0f) {
            f2 = f;
        }
        return f2 * 0.5f;
    }

    public ProbingState getState() {
        return this.state;
    }

    public ProbingState handleData(byte[] bArr, int i, int i2) {
        ByteBuffer filterWithEnglishLetters = filterWithEnglishLetters(bArr, i, i2);
        byte[] array = filterWithEnglishLetters.array();
        int position = filterWithEnglishLetters.position();
        for (i2 = 0; i2 < position; i2++) {
            byte b = latin1CharToClass[array[i2] & 255];
            byte b2 = latin1ClassModel[(this.lastCharClass * 8) + b];
            if (b2 == (byte) 0) {
                this.state = ProbingState.NOT_ME;
                break;
            }
            int[] iArr = this.freqCounter;
            iArr[b2] = iArr[b2] + 1;
            this.lastCharClass = b;
        }
        return this.state;
    }

    public void reset() {
        this.state = ProbingState.DETECTING;
        this.lastCharClass = (byte) 1;
        for (int i = 0; i < this.freqCounter.length; i++) {
            this.freqCounter[i] = 0;
        }
    }

    public void setOption() {
    }
}
