package org.mozilla.universalchardet.prober;

import java.util.Arrays;
import org.mozilla.universalchardet.Constants;
import org.mozilla.universalchardet.prober.CharsetProber.ProbingState;
import org.mozilla.universalchardet.prober.distributionanalysis.EUCTWDistributionAnalysis;
import org.mozilla.universalchardet.prober.statemachine.CodingStateMachine;
import org.mozilla.universalchardet.prober.statemachine.EUCTWSMModel;
import org.mozilla.universalchardet.prober.statemachine.SMModel;

public class EUCTWProber extends CharsetProber {
    private static final SMModel smModel = new EUCTWSMModel();
    private CodingStateMachine codingSM = new CodingStateMachine(smModel);
    private EUCTWDistributionAnalysis distributionAnalyzer = new EUCTWDistributionAnalysis();
    private byte[] lastChar = new byte[2];
    private ProbingState state;

    public EUCTWProber() {
        reset();
    }

    public String getCharSetName() {
        return Constants.CHARSET_EUC_TW;
    }

    public float getConfidence() {
        return this.distributionAnalyzer.getConfidence();
    }

    public ProbingState getState() {
        return this.state;
    }

    public ProbingState handleData(byte[] bArr, int i, int i2) {
        i2 += i;
        int i3 = i;
        while (i3 < i2) {
            ProbingState probingState;
            int nextState = this.codingSM.nextState(bArr[i3]);
            if (nextState == 1) {
                probingState = ProbingState.NOT_ME;
            } else if (nextState == 2) {
                probingState = ProbingState.FOUND_IT;
            } else {
                if (nextState == 0) {
                    nextState = this.codingSM.getCurrentCharLen();
                    if (i3 == i) {
                        this.lastChar[1] = bArr[i];
                        this.distributionAnalyzer.handleOneChar(this.lastChar, 0, nextState);
                    } else {
                        this.distributionAnalyzer.handleOneChar(bArr, i3 - 1, nextState);
                    }
                }
                i3++;
            }
            this.state = probingState;
        }
        this.lastChar[0] = bArr[i2 - 1];
        if (this.state == ProbingState.DETECTING && this.distributionAnalyzer.gotEnoughData() && getConfidence() > 0.95f) {
            this.state = ProbingState.FOUND_IT;
        }
        return this.state;
    }

    public void reset() {
        this.codingSM.reset();
        this.state = ProbingState.DETECTING;
        this.distributionAnalyzer.reset();
        Arrays.fill(this.lastChar, (byte) 0);
    }

    public void setOption() {
    }
}
