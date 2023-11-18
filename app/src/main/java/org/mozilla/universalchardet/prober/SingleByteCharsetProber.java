package org.mozilla.universalchardet.prober;

import org.mozilla.universalchardet.prober.CharsetProber.ProbingState;
import org.mozilla.universalchardet.prober.sequence.SequenceModel;

public class SingleByteCharsetProber extends CharsetProber {
    public static final int NEGATIVE_CAT = 0;
    public static final float NEGATIVE_SHORTCUT_THRESHOLD = 0.05f;
    public static final int NUMBER_OF_SEQ_CAT = 4;
    public static final int POSITIVE_CAT = 3;
    public static final float POSITIVE_SHORTCUT_THRESHOLD = 0.95f;
    public static final int SAMPLE_SIZE = 64;
    public static final int SB_ENOUGH_REL_THRESHOLD = 1024;
    public static final int SYMBOL_CAT_ORDER = 250;
    private int freqChar;
    private short lastOrder;
    private SequenceModel model;
    private CharsetProber nameProber;
    private boolean reversed;
    private int[] seqCounters;
    private ProbingState state;
    private int totalChar;
    private int totalSeqs;

    public SingleByteCharsetProber(SequenceModel sequenceModel) {
        this.model = sequenceModel;
        this.reversed = false;
        this.nameProber = null;
        this.seqCounters = new int[4];
        reset();
    }

    public SingleByteCharsetProber(SequenceModel sequenceModel, boolean z, CharsetProber charsetProber) {
        this.model = sequenceModel;
        this.reversed = z;
        this.nameProber = charsetProber;
        this.seqCounters = new int[4];
        reset();
    }

    public String getCharSetName() {
        return this.nameProber == null ? this.model.getCharsetName() : this.nameProber.getCharSetName();
    }

    public float getConfidence() {
        if (this.totalSeqs <= 0) {
            return 0.01f;
        }
        float typicalPositiveRatio = ((((((float) this.seqCounters[3]) * 1.0f) / ((float) this.totalSeqs)) / this.model.getTypicalPositiveRatio()) * ((float) this.freqChar)) / ((float) this.totalChar);
        if (typicalPositiveRatio >= 1.0f) {
            typicalPositiveRatio = 0.99f;
        }
        return typicalPositiveRatio;
    }

    public ProbingState getState() {
        return this.state;
    }

    public ProbingState handleData(byte[] bArr, int i, int i2) {
        i2 += i;
        while (i < i2) {
            short order = this.model.getOrder(bArr[i]);
            if (order < (short) 250) {
                this.totalChar++;
            }
            if (order < (short) 64) {
                this.freqChar++;
                if (this.lastOrder < (short) 64) {
                    this.totalSeqs++;
                    if (this.reversed) {
                        int[] iArr = this.seqCounters;
                        byte precedence = this.model.getPrecedence((order * 64) + this.lastOrder);
                        iArr[precedence] = iArr[precedence] + 1;
                    } else {
                        int[] iArr2 = this.seqCounters;
                        byte precedence2 = this.model.getPrecedence((this.lastOrder * 64) + order);
                        iArr2[precedence2] = iArr2[precedence2] + 1;
                    }
                }
            }
            this.lastOrder = order;
            i++;
        }
        if (this.state == ProbingState.DETECTING && this.totalSeqs > 1024) {
            ProbingState probingState;
            float confidence = getConfidence();
            if (confidence > 0.95f) {
                probingState = ProbingState.FOUND_IT;
            } else if (confidence < 0.05f) {
                probingState = ProbingState.NOT_ME;
            }
            this.state = probingState;
        }
        return this.state;
    }

    boolean keepEnglishLetters() {
        return this.model.getKeepEnglishLetter();
    }

    public void reset() {
        this.state = ProbingState.DETECTING;
        this.lastOrder = (short) 255;
        for (int i = 0; i < 4; i++) {
            this.seqCounters[i] = 0;
        }
        this.totalSeqs = 0;
        this.totalChar = 0;
        this.freqChar = 0;
    }

    public void setOption() {
    }
}
