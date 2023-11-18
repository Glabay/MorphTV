package org.mozilla.universalchardet.prober;

import org.mozilla.universalchardet.Constants;
import org.mozilla.universalchardet.prober.CharsetProber.ProbingState;
import org.mozilla.universalchardet.prober.statemachine.CodingStateMachine;
import org.mozilla.universalchardet.prober.statemachine.SMModel;
import org.mozilla.universalchardet.prober.statemachine.UTF8SMModel;

public class UTF8Prober extends CharsetProber {
    public static final float ONE_CHAR_PROB = 0.5f;
    private static final SMModel smModel = new UTF8SMModel();
    private CodingStateMachine codingSM = new CodingStateMachine(smModel);
    private int numOfMBChar = 0;
    private ProbingState state;

    public UTF8Prober() {
        reset();
    }

    public String getCharSetName() {
        return Constants.CHARSET_UTF_8;
    }

    public float getConfidence() {
        float f = 0.99f;
        if (this.numOfMBChar >= 6) {
            return 0.99f;
        }
        for (int i = 0; i < this.numOfMBChar; i++) {
            f *= 0.5f;
        }
        return 1.0f - f;
    }

    public ProbingState getState() {
        return this.state;
    }

    public ProbingState handleData(byte[] bArr, int i, int i2) {
        i2 += i;
        while (i < i2) {
            ProbingState probingState;
            int nextState = this.codingSM.nextState(bArr[i]);
            if (nextState == 1) {
                probingState = ProbingState.NOT_ME;
            } else if (nextState == 2) {
                probingState = ProbingState.FOUND_IT;
            } else {
                if (nextState == 0 && this.codingSM.getCurrentCharLen() >= 2) {
                    this.numOfMBChar++;
                }
                i++;
            }
            this.state = probingState;
            break;
        }
        if (this.state == ProbingState.DETECTING && getConfidence() > 0.95f) {
            this.state = ProbingState.FOUND_IT;
        }
        return this.state;
    }

    public void reset() {
        this.codingSM.reset();
        this.numOfMBChar = 0;
        this.state = ProbingState.DETECTING;
    }

    public void setOption() {
    }
}
