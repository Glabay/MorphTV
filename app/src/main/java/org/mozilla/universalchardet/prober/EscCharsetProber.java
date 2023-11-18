package org.mozilla.universalchardet.prober;

import org.mozilla.universalchardet.prober.CharsetProber.ProbingState;
import org.mozilla.universalchardet.prober.statemachine.CodingStateMachine;
import org.mozilla.universalchardet.prober.statemachine.HZSMModel;
import org.mozilla.universalchardet.prober.statemachine.ISO2022CNSMModel;
import org.mozilla.universalchardet.prober.statemachine.ISO2022JPSMModel;
import org.mozilla.universalchardet.prober.statemachine.ISO2022KRSMModel;

public class EscCharsetProber extends CharsetProber {
    private static final HZSMModel hzsModel = new HZSMModel();
    private static final ISO2022CNSMModel iso2022cnModel = new ISO2022CNSMModel();
    private static final ISO2022JPSMModel iso2022jpModel = new ISO2022JPSMModel();
    private static final ISO2022KRSMModel iso2022krModel = new ISO2022KRSMModel();
    private int activeSM;
    private CodingStateMachine[] codingSM = new CodingStateMachine[4];
    private String detectedCharset;
    private ProbingState state;

    public EscCharsetProber() {
        this.codingSM[0] = new CodingStateMachine(hzsModel);
        this.codingSM[1] = new CodingStateMachine(iso2022cnModel);
        this.codingSM[2] = new CodingStateMachine(iso2022jpModel);
        this.codingSM[3] = new CodingStateMachine(iso2022krModel);
        reset();
    }

    public String getCharSetName() {
        return this.detectedCharset;
    }

    public float getConfidence() {
        return 0.99f;
    }

    public ProbingState getState() {
        return this.state;
    }

    public ProbingState handleData(byte[] bArr, int i, int i2) {
        i2 += i;
        while (i < i2 && this.state == ProbingState.DETECTING) {
            int i3 = this.activeSM - 1;
            while (i3 >= 0) {
                int nextState = this.codingSM[i3].nextState(bArr[i]);
                if (nextState == 1) {
                    this.activeSM--;
                    if (this.activeSM <= 0) {
                        this.state = ProbingState.NOT_ME;
                    } else {
                        if (i3 != this.activeSM) {
                            CodingStateMachine codingStateMachine = this.codingSM[this.activeSM];
                            this.codingSM[this.activeSM] = this.codingSM[i3];
                            this.codingSM[i3] = codingStateMachine;
                        }
                        i3--;
                    }
                } else if (nextState == 2) {
                    this.state = ProbingState.FOUND_IT;
                    this.detectedCharset = this.codingSM[i3].getCodingStateMachine();
                } else {
                    i3--;
                }
                return this.state;
            }
            i++;
        }
        return this.state;
    }

    public void reset() {
        this.state = ProbingState.DETECTING;
        for (CodingStateMachine reset : this.codingSM) {
            reset.reset();
        }
        this.activeSM = this.codingSM.length;
        this.detectedCharset = null;
    }

    public void setOption() {
    }
}
