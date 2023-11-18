package org.mozilla.universalchardet.prober;

import org.mozilla.universalchardet.Constants;
import org.mozilla.universalchardet.prober.CharsetProber.ProbingState;

public class HebrewProber extends CharsetProber {
    public static final int FINAL_KAF = 234;
    public static final int FINAL_MEM = 237;
    public static final int FINAL_NUN = 239;
    public static final int FINAL_PE = 243;
    public static final int FINAL_TSADI = 245;
    public static final int MIN_FINAL_CHAR_DISTANCE = 5;
    public static final float MIN_MODEL_DISTANCE = 0.01f;
    public static final int NORMAL_KAF = 235;
    public static final int NORMAL_MEM = 238;
    public static final int NORMAL_NUN = 240;
    public static final int NORMAL_PE = 244;
    public static final int NORMAL_TSADI = 246;
    public static final byte SPACE = (byte) 32;
    private byte beforePrev;
    private int finalCharLogicalScore;
    private int finalCharVisualScore;
    private CharsetProber logicalProber = null;
    private byte prev;
    private CharsetProber visualProber = null;

    public HebrewProber() {
        reset();
    }

    protected static boolean isFinal(byte b) {
        int i = b & 255;
        if (!(i == FINAL_KAF || i == FINAL_MEM || i == 239 || i == 243)) {
            if (i != FINAL_TSADI) {
                return false;
            }
        }
        return true;
    }

    protected static boolean isNonFinal(byte b) {
        int i = b & 255;
        if (!(i == NORMAL_KAF || i == NORMAL_MEM || i == 240)) {
            if (i != NORMAL_PE) {
                return false;
            }
        }
        return true;
    }

    public String getCharSetName() {
        int i = this.finalCharLogicalScore - this.finalCharVisualScore;
        if (i >= 5) {
            return Constants.CHARSET_WINDOWS_1255;
        }
        if (i <= -5) {
            return Constants.CHARSET_ISO_8859_8;
        }
        float confidence = this.logicalProber.getConfidence() - this.visualProber.getConfidence();
        return confidence > 0.01f ? Constants.CHARSET_WINDOWS_1255 : confidence < -0.01f ? Constants.CHARSET_ISO_8859_8 : i < 0 ? Constants.CHARSET_ISO_8859_8 : Constants.CHARSET_WINDOWS_1255;
    }

    public float getConfidence() {
        return 0.0f;
    }

    public ProbingState getState() {
        return (this.logicalProber.getState() == ProbingState.NOT_ME && this.visualProber.getState() == ProbingState.NOT_ME) ? ProbingState.NOT_ME : ProbingState.DETECTING;
    }

    public ProbingState handleData(byte[] bArr, int i, int i2) {
        if (getState() == ProbingState.NOT_ME) {
            return ProbingState.NOT_ME;
        }
        i2 += i;
        while (i < i2) {
            byte b = bArr[i];
            if (b == (byte) 32) {
                if (this.beforePrev != (byte) 32) {
                    if (isFinal(this.prev)) {
                        this.finalCharLogicalScore++;
                    } else if (!isNonFinal(this.prev)) {
                    }
                }
                this.beforePrev = this.prev;
                this.prev = b;
                i++;
            } else {
                if (this.beforePrev == (byte) 32 && isFinal(this.prev) && b != (byte) 32) {
                }
                this.beforePrev = this.prev;
                this.prev = b;
                i++;
            }
            this.finalCharVisualScore++;
            this.beforePrev = this.prev;
            this.prev = b;
            i++;
        }
        return ProbingState.DETECTING;
    }

    public void reset() {
        this.finalCharLogicalScore = 0;
        this.finalCharVisualScore = 0;
        this.prev = (byte) 32;
        this.beforePrev = (byte) 32;
    }

    public void setModalProbers(CharsetProber charsetProber, CharsetProber charsetProber2) {
        this.logicalProber = charsetProber;
        this.visualProber = charsetProber2;
    }

    public void setOption() {
    }
}
