package org.mozilla.universalchardet.prober;

import org.mozilla.universalchardet.prober.CharsetProber.ProbingState;

public class MBCSGroupProber extends CharsetProber {
    private int activeNum;
    private int bestGuess;
    private boolean[] isActive = new boolean[7];
    private CharsetProber[] probers = new CharsetProber[7];
    private ProbingState state;

    public MBCSGroupProber() {
        this.probers[0] = new UTF8Prober();
        this.probers[1] = new SJISProber();
        this.probers[2] = new EUCJPProber();
        this.probers[3] = new GB18030Prober();
        this.probers[4] = new EUCKRProber();
        this.probers[5] = new Big5Prober();
        this.probers[6] = new EUCTWProber();
        reset();
    }

    public String getCharSetName() {
        if (this.bestGuess == -1) {
            getConfidence();
            if (this.bestGuess == -1) {
                this.bestGuess = 0;
            }
        }
        return this.probers[this.bestGuess].getCharSetName();
    }

    public float getConfidence() {
        if (this.state == ProbingState.FOUND_IT) {
            return 0.99f;
        }
        if (this.state == ProbingState.NOT_ME) {
            return 0.01f;
        }
        float f = 0.0f;
        for (int i = 0; i < this.probers.length; i++) {
            if (this.isActive[i]) {
                float confidence = this.probers[i].getConfidence();
                if (f < confidence) {
                    this.bestGuess = i;
                    f = confidence;
                }
            }
        }
        return f;
    }

    public ProbingState getState() {
        return this.state;
    }

    public ProbingState handleData(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[i2];
        i2 += i;
        int i3 = 0;
        Object obj = 1;
        while (i < i2) {
            int i4;
            if ((bArr[i] & 128) != 0) {
                i4 = i3 + 1;
                bArr2[i3] = bArr[i];
                i3 = i4;
                obj = 1;
            } else if (obj != null) {
                i4 = i3 + 1;
                bArr2[i3] = bArr[i];
                i3 = i4;
                obj = null;
            }
            i++;
        }
        for (int i5 = 0; i5 < this.probers.length; i5++) {
            if (this.isActive[i5]) {
                ProbingState probingState;
                ProbingState handleData = this.probers[i5].handleData(bArr2, 0, i3);
                if (handleData == ProbingState.FOUND_IT) {
                    this.bestGuess = i5;
                    probingState = ProbingState.FOUND_IT;
                } else if (handleData == ProbingState.NOT_ME) {
                    this.isActive[i5] = false;
                    this.activeNum--;
                    if (this.activeNum <= 0) {
                        probingState = ProbingState.NOT_ME;
                    }
                } else {
                    continue;
                }
                this.state = probingState;
                break;
            }
        }
        return this.state;
    }

    public void reset() {
        int i = 0;
        this.activeNum = 0;
        while (i < this.probers.length) {
            this.probers[i].reset();
            this.isActive[i] = true;
            this.activeNum++;
            i++;
        }
        this.bestGuess = -1;
        this.state = ProbingState.DETECTING;
    }

    public void setOption() {
    }
}
