package org.mozilla.universalchardet.prober;

import java.util.Arrays;
import org.mozilla.universalchardet.Constants;
import org.mozilla.universalchardet.prober.CharsetProber.ProbingState;
import org.mozilla.universalchardet.prober.contextanalysis.SJISContextAnalysis;
import org.mozilla.universalchardet.prober.distributionanalysis.SJISDistributionAnalysis;
import org.mozilla.universalchardet.prober.statemachine.CodingStateMachine;
import org.mozilla.universalchardet.prober.statemachine.SJISSMModel;
import org.mozilla.universalchardet.prober.statemachine.SMModel;

public class SJISProber extends CharsetProber {
    private static final SMModel smModel = new SJISSMModel();
    private CodingStateMachine codingSM = new CodingStateMachine(smModel);
    private SJISContextAnalysis contextAnalyzer = new SJISContextAnalysis();
    private SJISDistributionAnalysis distributionAnalyzer = new SJISDistributionAnalysis();
    private byte[] lastChar = new byte[2];
    private ProbingState state;

    public SJISProber() {
        reset();
    }

    public String getCharSetName() {
        return Constants.CHARSET_SHIFT_JIS;
    }

    public float getConfidence() {
        return Math.max(this.contextAnalyzer.getConfidence(), this.distributionAnalyzer.getConfidence());
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
                        this.contextAnalyzer.handleOneChar(this.lastChar, 2 - nextState, nextState);
                        this.distributionAnalyzer.handleOneChar(this.lastChar, 0, nextState);
                    } else {
                        this.contextAnalyzer.handleOneChar(bArr, (i3 + 1) - nextState, nextState);
                        this.distributionAnalyzer.handleOneChar(bArr, i3 - 1, nextState);
                    }
                }
                i3++;
            }
            this.state = probingState;
        }
        this.lastChar[0] = bArr[i2 - 1];
        if (this.state == ProbingState.DETECTING && this.contextAnalyzer.gotEnoughData() && getConfidence() > 0.95f) {
            this.state = ProbingState.FOUND_IT;
        }
        return this.state;
    }

    public void reset() {
        this.codingSM.reset();
        this.state = ProbingState.DETECTING;
        this.contextAnalyzer.reset();
        this.distributionAnalyzer.reset();
        Arrays.fill(this.lastChar, (byte) 0);
    }

    public void setOption() {
    }
}
