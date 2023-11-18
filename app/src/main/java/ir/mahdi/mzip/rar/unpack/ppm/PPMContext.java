package ir.mahdi.mzip.rar.unpack.ppm;

import android.support.v4.media.session.PlaybackStateCompat;
import ir.mahdi.mzip.rar.io.Raw;

public class PPMContext extends Pointer {
    public static final int[] ExpEscape = new int[]{25, 14, 9, 7, 5, 5, 4, 4, 4, 3, 3, 3, 2, 2, 2, 2};
    public static final int size = ((unionSize + 2) + 4);
    private static final int unionSize = Math.max(6, 6);
    private final FreqData freqData;
    private int numStats;
    private final State oneState;
    private final int[] ps = new int[256];
    private int suffix;
    private PPMContext tempPPMContext = null;
    private final State tempState1 = new State(null);
    private final State tempState2 = new State(null);
    private final State tempState3 = new State(null);
    private final State tempState4 = new State(null);
    private final State tempState5 = new State(null);

    public int getMean(int i, int i2, int i3) {
        return (i + (1 << (i2 - i3))) >>> i2;
    }

    public PPMContext(byte[] bArr) {
        super(bArr);
        this.oneState = new State(bArr);
        this.freqData = new FreqData(bArr);
    }

    public PPMContext init(byte[] bArr) {
        this.mem = bArr;
        this.pos = 0;
        this.oneState.init(bArr);
        this.freqData.init(bArr);
        return this;
    }

    public FreqData getFreqData() {
        return this.freqData;
    }

    public void setFreqData(FreqData freqData) {
        this.freqData.setSummFreq(freqData.getSummFreq());
        this.freqData.setStats(freqData.getStats());
    }

    public final int getNumStats() {
        if (this.mem != null) {
            this.numStats = Raw.readShortLittleEndian(this.mem, this.pos) & 65535;
        }
        return this.numStats;
    }

    public final void setNumStats(int i) {
        this.numStats = 65535 & i;
        if (this.mem != null) {
            Raw.writeShortLittleEndian(this.mem, this.pos, (short) i);
        }
    }

    public State getOneState() {
        return this.oneState;
    }

    public void setOneState(StateRef stateRef) {
        this.oneState.setValues(stateRef);
    }

    public int getSuffix() {
        if (this.mem != null) {
            this.suffix = Raw.readIntLittleEndian(this.mem, this.pos + 8);
        }
        return this.suffix;
    }

    public void setSuffix(int i) {
        this.suffix = i;
        if (this.mem != null) {
            Raw.writeIntLittleEndian(this.mem, this.pos + 8, i);
        }
    }

    public void setSuffix(PPMContext pPMContext) {
        setSuffix(pPMContext.getAddress());
    }

    public void setAddress(int i) {
        super.setAddress(i);
        i += 2;
        this.oneState.setAddress(i);
        this.freqData.setAddress(i);
    }

    private PPMContext getTempPPMContext(byte[] bArr) {
        if (this.tempPPMContext == null) {
            this.tempPPMContext = new PPMContext(null);
        }
        return this.tempPPMContext.init(bArr);
    }

    public int createChild(ModelPPM modelPPM, State state, StateRef stateRef) {
        PPMContext tempPPMContext = getTempPPMContext(modelPPM.getSubAlloc().getHeap());
        tempPPMContext.setAddress(modelPPM.getSubAlloc().allocContext());
        if (tempPPMContext != null) {
            tempPPMContext.setNumStats(1);
            tempPPMContext.setOneState(stateRef);
            tempPPMContext.setSuffix(this);
            state.setSuccessor(tempPPMContext);
        }
        return tempPPMContext.getAddress();
    }

    public void rescale(ModelPPM modelPPM) {
        int numStats = getNumStats();
        int numStats2 = getNumStats() - 1;
        State state = new State(modelPPM.getHeap());
        State state2 = new State(modelPPM.getHeap());
        State state3 = new State(modelPPM.getHeap());
        state2.setAddress(modelPPM.getFoundState().getAddress());
        while (state2.getAddress() != this.freqData.getStats()) {
            state3.setAddress(state2.getAddress() - 6);
            State.ppmdSwap(state2, state3);
            state2.decAddress();
        }
        state3.setAddress(this.freqData.getStats());
        state3.incFreq(4);
        this.freqData.incSummFreq(4);
        int summFreq = this.freqData.getSummFreq() - state2.getFreq();
        int i = modelPPM.getOrderFall() != 0 ? 1 : 0;
        state2.setFreq((state2.getFreq() + i) >>> 1);
        this.freqData.setSummFreq(state2.getFreq());
        do {
            state2.incAddress();
            summFreq -= state2.getFreq();
            state2.setFreq((state2.getFreq() + i) >>> 1);
            this.freqData.incSummFreq(state2.getFreq());
            state3.setAddress(state2.getAddress() - 6);
            if (state2.getFreq() > state3.getFreq()) {
                state.setAddress(state2.getAddress());
                StateRef stateRef = new StateRef();
                stateRef.setValues(state);
                State state4 = new State(modelPPM.getHeap());
                State state5 = new State(modelPPM.getHeap());
                do {
                    state4.setAddress(state.getAddress() - 6);
                    state.setValues(state4);
                    state.decAddress();
                    state5.setAddress(state.getAddress() - 6);
                    if (state.getAddress() == this.freqData.getStats()) {
                        break;
                    }
                } while (stateRef.getFreq() > state5.getFreq());
                state.setValues(stateRef);
            }
            numStats2--;
        } while (numStats2 != 0);
        if (state2.getFreq() == 0) {
            do {
                numStats2++;
                state2.decAddress();
            } while (state2.getFreq() == 0);
            summFreq += numStats2;
            setNumStats(getNumStats() - numStats2);
            if (getNumStats() == 1) {
                StateRef stateRef2 = new StateRef();
                state3.setAddress(this.freqData.getStats());
                stateRef2.setValues(state3);
                do {
                    stateRef2.decFreq(stateRef2.getFreq() >>> 1);
                    summFreq >>>= 1;
                } while (summFreq > 1);
                modelPPM.getSubAlloc().freeUnits(this.freqData.getStats(), (numStats + 1) >>> 1);
                this.oneState.setValues(stateRef2);
                modelPPM.getFoundState().setAddress(this.oneState.getAddress());
                return;
            }
        }
        this.freqData.incSummFreq(summFreq - (summFreq >>> 1));
        numStats = (numStats + 1) >>> 1;
        numStats2 = (getNumStats() + 1) >>> 1;
        if (numStats != numStats2) {
            this.freqData.setStats(modelPPM.getSubAlloc().shrinkUnits(this.freqData.getStats(), numStats, numStats2));
        }
        modelPPM.getFoundState().setAddress(this.freqData.getStats());
    }

    private int getArrayIndex(ModelPPM modelPPM, State state) {
        PPMContext tempPPMContext = getTempPPMContext(modelPPM.getSubAlloc().getHeap());
        tempPPMContext.setAddress(getSuffix());
        return (((modelPPM.getPrevSuccess() + 0) + modelPPM.getNS2BSIndx()[tempPPMContext.getNumStats() - 1]) + (modelPPM.getHiBitsFlag() + (modelPPM.getHB2Flag()[state.getSymbol()] * 2))) + ((modelPPM.getRunLength() >>> 26) & 32);
    }

    public void decodeBinSymbol(ModelPPM modelPPM) {
        State init = this.tempState1.init(modelPPM.getHeap());
        init.setAddress(this.oneState.getAddress());
        modelPPM.setHiBitsFlag(modelPPM.getHB2Flag()[modelPPM.getFoundState().getSymbol()]);
        int freq = init.getFreq() - 1;
        int arrayIndex = getArrayIndex(modelPPM, init);
        int i = modelPPM.getBinSumm()[freq][arrayIndex];
        long j = (long) i;
        int i2 = 0;
        if (modelPPM.getCoder().getCurrentShiftCount(14) < j) {
            modelPPM.getFoundState().setAddress(init.getAddress());
            if (init.getFreq() < 128) {
                i2 = 1;
            }
            init.incFreq(i2);
            modelPPM.getCoder().getSubRange().setLowCount(0);
            modelPPM.getCoder().getSubRange().setHighCount(j);
            modelPPM.getBinSumm()[freq][arrayIndex] = ((i + 128) - getMean(i, 7, 2)) & 65535;
            modelPPM.setPrevSuccess(1);
            modelPPM.incRunLength(1);
            return;
        }
        modelPPM.getCoder().getSubRange().setLowCount(j);
        i = (i - getMean(i, 7, 2)) & 65535;
        modelPPM.getBinSumm()[freq][arrayIndex] = i;
        modelPPM.getCoder().getSubRange().setHighCount(PlaybackStateCompat.ACTION_PREPARE);
        modelPPM.setInitEsc(ExpEscape[i >>> 10]);
        modelPPM.setNumMasked(1);
        modelPPM.getCharMask()[init.getSymbol()] = modelPPM.getEscCount();
        modelPPM.setPrevSuccess(0);
        modelPPM.getFoundState().setAddress(0);
    }

    public void update1(ModelPPM modelPPM, int i) {
        modelPPM.getFoundState().setAddress(i);
        modelPPM.getFoundState().incFreq(4);
        this.freqData.incSummFreq(4);
        State init = this.tempState3.init(modelPPM.getHeap());
        State init2 = this.tempState4.init(modelPPM.getHeap());
        init.setAddress(i);
        init2.setAddress(i - 6);
        if (init.getFreq() > init2.getFreq()) {
            State.ppmdSwap(init, init2);
            modelPPM.getFoundState().setAddress(init2.getAddress());
            if (init2.getFreq() > ModelPPM.MAX_FREQ) {
                rescale(modelPPM);
            }
        }
    }

    public boolean decodeSymbol2(ModelPPM modelPPM) {
        int numStats = getNumStats() - modelPPM.getNumMasked();
        SEE2Context makeEscFreq2 = makeEscFreq2(modelPPM, numStats);
        RangeCoder coder = modelPPM.getCoder();
        State init = this.tempState1.init(modelPPM.getHeap());
        State init2 = this.tempState2.init(modelPPM.getHeap());
        init.setAddress(this.freqData.getStats() - 6);
        int i = 0;
        int i2 = numStats;
        numStats = 0;
        int i3 = 0;
        while (true) {
            init.incAddress();
            if (modelPPM.getCharMask()[init.getSymbol()] != modelPPM.getEscCount()) {
                numStats += init.getFreq();
                int i4 = i3 + 1;
                this.ps[i3] = init.getAddress();
                i2--;
                if (i2 == 0) {
                    break;
                }
                i3 = i4;
            }
        }
        coder.getSubRange().incScale(numStats);
        long currentCount = (long) coder.getCurrentCount();
        if (currentCount >= coder.getSubRange().getScale()) {
            return false;
        }
        init.setAddress(this.ps[0]);
        long j = (long) numStats;
        if (currentCount < j) {
            numStats = 0;
            while (true) {
                i += init.getFreq();
                j = (long) i;
                if (j > currentCount) {
                    break;
                }
                numStats++;
                init.setAddress(this.ps[numStats]);
            }
            coder.getSubRange().setHighCount(j);
            coder.getSubRange().setLowCount((long) (i - init.getFreq()));
            makeEscFreq2.update();
            update2(modelPPM, init.getAddress());
        } else {
            coder.getSubRange().setLowCount(j);
            coder.getSubRange().setHighCount(coder.getSubRange().getScale());
            int numStats2 = getNumStats() - modelPPM.getNumMasked();
            numStats = -1;
            do {
                numStats++;
                init2.setAddress(this.ps[numStats]);
                modelPPM.getCharMask()[init2.getSymbol()] = modelPPM.getEscCount();
                numStats2--;
            } while (numStats2 != 0);
            makeEscFreq2.incSumm((int) coder.getSubRange().getScale());
            modelPPM.setNumMasked(getNumStats());
        }
        return true;
    }

    public void update2(ModelPPM modelPPM, int i) {
        State init = this.tempState5.init(modelPPM.getHeap());
        init.setAddress(i);
        modelPPM.getFoundState().setAddress(i);
        modelPPM.getFoundState().incFreq(4);
        this.freqData.incSummFreq(4);
        if (init.getFreq() > ModelPPM.MAX_FREQ) {
            rescale(modelPPM);
        }
        modelPPM.incEscCount(1);
        modelPPM.setRunLength(modelPPM.getInitRL());
    }

    private SEE2Context makeEscFreq2(ModelPPM modelPPM, int i) {
        int numStats = getNumStats();
        if (numStats != 256) {
            PPMContext tempPPMContext = getTempPPMContext(modelPPM.getHeap());
            tempPPMContext.setAddress(getSuffix());
            int i2 = modelPPM.getNS2Indx()[i - 1];
            int i3 = 0;
            int i4 = ((i < tempPPMContext.getNumStats() - numStats ? 1 : 0) + 0) + ((this.freqData.getSummFreq() < numStats * 11 ? 1 : 0) * 2);
            if (modelPPM.getNumMasked() > i) {
                i3 = 1;
            }
            i = modelPPM.getSEE2Cont()[i2][(i4 + (i3 * 4)) + modelPPM.getHiBitsFlag()];
            modelPPM.getCoder().getSubRange().setScale((long) i.getMean());
            return i;
        }
        i = modelPPM.getDummySEE2Cont();
        modelPPM.getCoder().getSubRange().setScale(1);
        return i;
    }

    public boolean decodeSymbol1(ModelPPM modelPPM) {
        RangeCoder coder = modelPPM.getCoder();
        coder.getSubRange().setScale((long) this.freqData.getSummFreq());
        State state = new State(modelPPM.getHeap());
        state.setAddress(this.freqData.getStats());
        long currentCount = (long) coder.getCurrentCount();
        int i = 0;
        if (currentCount >= coder.getSubRange().getScale()) {
            return false;
        }
        int freq = state.getFreq();
        long j = (long) freq;
        if (currentCount < j) {
            coder.getSubRange().setHighCount(j);
            if (((long) (freq * 2)) > coder.getSubRange().getScale()) {
                i = 1;
            }
            modelPPM.setPrevSuccess(i);
            modelPPM.incRunLength(modelPPM.getPrevSuccess());
            freq += 4;
            modelPPM.getFoundState().setAddress(state.getAddress());
            modelPPM.getFoundState().setFreq(freq);
            this.freqData.incSummFreq(4);
            if (freq > ModelPPM.MAX_FREQ) {
                rescale(modelPPM);
            }
            coder.getSubRange().setLowCount(0);
            return true;
        } else if (modelPPM.getFoundState().getAddress() == 0) {
            return false;
        } else {
            long j2;
            modelPPM.setPrevSuccess(0);
            int numStats = getNumStats();
            int i2 = numStats - 1;
            int i3 = i2;
            do {
                freq += state.incAddress().getFreq();
                j2 = (long) freq;
                if (j2 <= currentCount) {
                    i3--;
                } else {
                    coder.getSubRange().setLowCount((long) (freq - state.getFreq()));
                    coder.getSubRange().setHighCount(j2);
                    update1(modelPPM, state.getAddress());
                    return true;
                }
            } while (i3 != 0);
            modelPPM.setHiBitsFlag(modelPPM.getHB2Flag()[modelPPM.getFoundState().getSymbol()]);
            coder.getSubRange().setLowCount(j2);
            modelPPM.getCharMask()[state.getSymbol()] = modelPPM.getEscCount();
            modelPPM.setNumMasked(numStats);
            modelPPM.getFoundState().setAddress(0);
            do {
                modelPPM.getCharMask()[state.decAddress().getSymbol()] = modelPPM.getEscCount();
                i2--;
            } while (i2 != 0);
            coder.getSubRange().setHighCount(coder.getSubRange().getScale());
            return true;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PPMContext[");
        stringBuilder.append("\n  pos=");
        stringBuilder.append(this.pos);
        stringBuilder.append("\n  size=");
        stringBuilder.append(size);
        stringBuilder.append("\n  numStats=");
        stringBuilder.append(getNumStats());
        stringBuilder.append("\n  Suffix=");
        stringBuilder.append(getSuffix());
        stringBuilder.append("\n  freqData=");
        stringBuilder.append(this.freqData);
        stringBuilder.append("\n  oneState=");
        stringBuilder.append(this.oneState);
        stringBuilder.append("\n]");
        return stringBuilder.toString();
    }
}
