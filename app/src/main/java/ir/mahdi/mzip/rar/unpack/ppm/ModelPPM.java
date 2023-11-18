package ir.mahdi.mzip.rar.unpack.ppm;

import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.unpack.Unpack;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import org.mozilla.universalchardet.prober.HebrewProber;

public class ModelPPM {
    public static final int BIN_SCALE = 16384;
    public static final int INTERVAL = 128;
    public static final int INT_BITS = 7;
    private static int[] InitBinEsc = new int[]{15581, 7999, 22975, 18675, 25761, 23228, 26162, 24657};
    public static final int MAX_FREQ = 124;
    public static final int MAX_O = 64;
    public static final int PERIOD_BITS = 7;
    public static final int TOT_BITS = 14;
    private int[] HB2Flag = new int[256];
    private int[] NS2BSIndx = new int[256];
    private int[] NS2Indx = new int[256];
    private SEE2Context[][] SEE2Cont = ((SEE2Context[][]) Array.newInstance(SEE2Context.class, new int[]{25, 16}));
    private int[][] binSumm = ((int[][]) Array.newInstance(int.class, new int[]{128, 64}));
    private int[] charMask = new int[256];
    private RangeCoder coder = new RangeCoder();
    private SEE2Context dummySEE2Cont;
    private int escCount;
    private State foundState;
    private int hiBitsFlag;
    private int initEsc;
    private int initRL;
    private PPMContext maxContext = null;
    private int maxOrder;
    private PPMContext medContext = null;
    private PPMContext minContext = null;
    private int numMasked;
    private int orderFall;
    private int prevSuccess;
    private final int[] ps = new int[64];
    private int runLength;
    private SubAllocator subAlloc = new SubAllocator();
    private final PPMContext tempPPMContext1 = new PPMContext(null);
    private final PPMContext tempPPMContext2 = new PPMContext(null);
    private final PPMContext tempPPMContext3 = new PPMContext(null);
    private final PPMContext tempPPMContext4 = new PPMContext(null);
    private final State tempState1 = new State(null);
    private final State tempState2 = new State(null);
    private final State tempState3 = new State(null);
    private final State tempState4 = new State(null);
    private final StateRef tempStateRef1 = new StateRef();
    private final StateRef tempStateRef2 = new StateRef();

    public SubAllocator getSubAlloc() {
        return this.subAlloc;
    }

    private void restartModelRare() {
        Arrays.fill(this.charMask, 0);
        this.subAlloc.initSubAllocator();
        int i = 12;
        if (this.maxOrder < 12) {
            i = this.maxOrder;
        }
        this.initRL = (-i) - 1;
        int allocContext = this.subAlloc.allocContext();
        this.minContext.setAddress(allocContext);
        this.maxContext.setAddress(allocContext);
        this.minContext.setSuffix(0);
        this.orderFall = this.maxOrder;
        this.minContext.setNumStats(256);
        this.minContext.getFreqData().setSummFreq(this.minContext.getNumStats() + 1);
        allocContext = this.subAlloc.allocUnits(128);
        this.foundState.setAddress(allocContext);
        this.minContext.getFreqData().setStats(allocContext);
        State state = new State(this.subAlloc.getHeap());
        int stats = this.minContext.getFreqData().getStats();
        this.runLength = this.initRL;
        this.prevSuccess = 0;
        for (int i2 = 0; i2 < 256; i2++) {
            state.setAddress((i2 * 6) + stats);
            state.setSymbol(i2);
            state.setFreq(1);
            state.setSuccessor(0);
        }
        for (allocContext = 0; allocContext < 128; allocContext++) {
            for (i = 0; i < 8; i++) {
                for (int i3 = 0; i3 < 64; i3 += 8) {
                    this.binSumm[allocContext][i + i3] = 16384 - (InitBinEsc[i] / (allocContext + 2));
                }
            }
        }
        for (allocContext = 0; allocContext < 25; allocContext++) {
            for (i = 0; i < 16; i++) {
                this.SEE2Cont[allocContext][i].init((allocContext * 5) + 10);
            }
        }
    }

    private void startModelRare(int i) {
        this.escCount = 1;
        this.maxOrder = i;
        restartModelRare();
        int i2 = 0;
        this.NS2BSIndx[0] = null;
        this.NS2BSIndx[1] = 2;
        for (i = 0; i < 9; i++) {
            this.NS2BSIndx[i + 2] = 4;
        }
        for (i = 0; i < HebrewProber.FINAL_TSADI; i++) {
            this.NS2BSIndx[i + 11] = 6;
        }
        i = 0;
        while (i < 3) {
            this.NS2Indx[i] = i;
            i++;
        }
        int i3 = i;
        int i4 = 1;
        int i5 = 1;
        while (i < 256) {
            this.NS2Indx[i] = i3;
            i4--;
            if (i4 == 0) {
                i5++;
                i3++;
                i4 = i5;
            }
            i++;
        }
        for (i = 0; i < 64; i++) {
            this.HB2Flag[i] = 0;
        }
        while (i2 < 192) {
            this.HB2Flag[i2 + 64] = 8;
            i2++;
        }
        this.dummySEE2Cont.setShift(7);
    }

    private void clearMask() {
        this.escCount = 1;
        Arrays.fill(this.charMask, 0);
    }

    public boolean decodeInit(Unpack unpack, int i) throws IOException, RarException {
        int i2;
        i = unpack.getChar() & 255;
        boolean z = false;
        Object obj = (i & 32) != 0 ? 1 : null;
        if (obj != null) {
            i2 = unpack.getChar();
        } else if (this.subAlloc.GetAllocatedMemory() == 0) {
            return false;
        } else {
            i2 = 0;
        }
        if ((i & 64) != 0) {
            unpack.setPpmEscChar(unpack.getChar());
        }
        this.coder.initDecoder(unpack);
        if (obj != null) {
            unpack = (i & 31) + 1;
            if (unpack > 16) {
                unpack = ((unpack - 16) * 3) + 16;
            }
            if (unpack == 1) {
                this.subAlloc.stopSubAllocator();
                return false;
            }
            this.subAlloc.startSubAllocator(i2 + 1);
            this.minContext = new PPMContext(getHeap());
            this.medContext = new PPMContext(getHeap());
            this.maxContext = new PPMContext(getHeap());
            this.foundState = new State(getHeap());
            this.dummySEE2Cont = new SEE2Context();
            for (int i3 = 0; i3 < 25; i3++) {
                for (i2 = 0; i2 < 16; i2++) {
                    this.SEE2Cont[i3][i2] = new SEE2Context();
                }
            }
            startModelRare(unpack);
        }
        if (this.minContext.getAddress() != null) {
            z = true;
        }
        return z;
    }

    public int decodeChar() throws IOException, RarException {
        if (this.minContext.getAddress() > this.subAlloc.getPText()) {
            if (this.minContext.getAddress() <= this.subAlloc.getHeapEnd()) {
                if (this.minContext.getNumStats() != 1) {
                    if (this.minContext.getFreqData().getStats() > this.subAlloc.getPText()) {
                        if (this.minContext.getFreqData().getStats() <= this.subAlloc.getHeapEnd()) {
                            if (!this.minContext.decodeSymbol1(this)) {
                                return -1;
                            }
                        }
                    }
                    return -1;
                }
                this.minContext.decodeBinSymbol(this);
                this.coder.decode();
                while (this.foundState.getAddress() == 0) {
                    this.coder.ariDecNormalize();
                    do {
                        this.orderFall++;
                        this.minContext.setAddress(this.minContext.getSuffix());
                        if (this.minContext.getAddress() > this.subAlloc.getPText()) {
                            if (this.minContext.getAddress() > this.subAlloc.getHeapEnd()) {
                            }
                        }
                        return -1;
                    } while (this.minContext.getNumStats() == this.numMasked);
                    if (!this.minContext.decodeSymbol2(this)) {
                        return -1;
                    }
                    this.coder.decode();
                }
                int symbol = this.foundState.getSymbol();
                if (this.orderFall != 0 || this.foundState.getSuccessor() <= this.subAlloc.getPText()) {
                    updateModel();
                    if (this.escCount == 0) {
                        clearMask();
                    }
                } else {
                    int successor = this.foundState.getSuccessor();
                    this.minContext.setAddress(successor);
                    this.maxContext.setAddress(successor);
                }
                this.coder.ariDecNormalize();
                return symbol;
            }
        }
        return -1;
    }

    public SEE2Context[][] getSEE2Cont() {
        return this.SEE2Cont;
    }

    public SEE2Context getDummySEE2Cont() {
        return this.dummySEE2Cont;
    }

    public int getInitRL() {
        return this.initRL;
    }

    public int getEscCount() {
        return this.escCount;
    }

    public void setEscCount(int i) {
        this.escCount = i & 255;
    }

    public void incEscCount(int i) {
        setEscCount(getEscCount() + i);
    }

    public int[] getCharMask() {
        return this.charMask;
    }

    public int getNumMasked() {
        return this.numMasked;
    }

    public void setNumMasked(int i) {
        this.numMasked = i;
    }

    public int getInitEsc() {
        return this.initEsc;
    }

    public void setInitEsc(int i) {
        this.initEsc = i;
    }

    public int getRunLength() {
        return this.runLength;
    }

    public void setRunLength(int i) {
        this.runLength = i;
    }

    public void incRunLength(int i) {
        setRunLength(getRunLength() + i);
    }

    public int getPrevSuccess() {
        return this.prevSuccess;
    }

    public void setPrevSuccess(int i) {
        this.prevSuccess = i & 255;
    }

    public int getHiBitsFlag() {
        return this.hiBitsFlag;
    }

    public void setHiBitsFlag(int i) {
        this.hiBitsFlag = i & 255;
    }

    public int[][] getBinSumm() {
        return this.binSumm;
    }

    public RangeCoder getCoder() {
        return this.coder;
    }

    public int[] getHB2Flag() {
        return this.HB2Flag;
    }

    public int[] getNS2BSIndx() {
        return this.NS2BSIndx;
    }

    public int[] getNS2Indx() {
        return this.NS2Indx;
    }

    public State getFoundState() {
        return this.foundState;
    }

    public byte[] getHeap() {
        return this.subAlloc.getHeap();
    }

    public int getOrderFall() {
        return this.orderFall;
    }

    private int createSuccessors(boolean r10, ir.mahdi.mzip.rar.unpack.ppm.State r11) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:37)
	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:61)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r9 = this;
        r0 = r9.tempStateRef2;
        r1 = r9.tempState1;
        r2 = r9.getHeap();
        r1 = r1.init(r2);
        r2 = r9.tempPPMContext1;
        r3 = r9.getHeap();
        r2 = r2.init(r3);
        r3 = r9.minContext;
        r3 = r3.getAddress();
        r2.setAddress(r3);
        r3 = r9.tempPPMContext2;
        r4 = r9.getHeap();
        r3 = r3.init(r4);
        r4 = r9.foundState;
        r4 = r4.getSuccessor();
        r3.setAddress(r4);
        r4 = r9.tempState2;
        r5 = r9.getHeap();
        r4 = r4.init(r5);
        r5 = 0;
        r6 = 1;
        if (r10 != 0) goto L_0x0055;
    L_0x0040:
        r10 = r9.ps;
        r7 = r9.foundState;
        r7 = r7.getAddress();
        r10[r5] = r7;
        r10 = r2.getSuffix();
        if (r10 != 0) goto L_0x0052;
    L_0x0050:
        r10 = 1;
        goto L_0x0053;
    L_0x0052:
        r10 = 0;
    L_0x0053:
        r7 = 1;
        goto L_0x0057;
    L_0x0055:
        r10 = 0;
        r7 = 0;
    L_0x0057:
        if (r10 != 0) goto L_0x00d7;
    L_0x0059:
        r10 = r11.getAddress();
        if (r10 == 0) goto L_0x006f;
    L_0x005f:
        r10 = r11.getAddress();
        r4.setAddress(r10);
        r10 = r2.getSuffix();
        r2.setAddress(r10);
        r10 = 1;
        goto L_0x0070;
    L_0x006f:
        r10 = 0;
    L_0x0070:
        if (r10 != 0) goto L_0x00b1;
    L_0x0072:
        r10 = r2.getSuffix();
        r2.setAddress(r10);
        r10 = r2.getNumStats();
        if (r10 == r6) goto L_0x00a6;
    L_0x007f:
        r10 = r2.getFreqData();
        r10 = r10.getStats();
        r4.setAddress(r10);
        r10 = r4.getSymbol();
        r11 = r9.foundState;
        r11 = r11.getSymbol();
        if (r10 == r11) goto L_0x00b1;
    L_0x0096:
        r4.incAddress();
        r10 = r4.getSymbol();
        r11 = r9.foundState;
        r11 = r11.getSymbol();
        if (r10 != r11) goto L_0x0096;
    L_0x00a5:
        goto L_0x00b1;
    L_0x00a6:
        r10 = r2.getOneState();
        r10 = r10.getAddress();
        r4.setAddress(r10);
    L_0x00b1:
        r10 = r4.getSuccessor();
        r11 = r3.getAddress();
        if (r10 == r11) goto L_0x00c3;
    L_0x00bb:
        r10 = r4.getSuccessor();
        r2.setAddress(r10);
        goto L_0x00d7;
    L_0x00c3:
        r10 = r9.ps;
        r11 = r7 + 1;
        r8 = r4.getAddress();
        r10[r7] = r8;
        r10 = r2.getSuffix();
        if (r10 != 0) goto L_0x00d5;
    L_0x00d3:
        r7 = r11;
        goto L_0x00d7;
    L_0x00d5:
        r7 = r11;
        goto L_0x006f;
    L_0x00d7:
        if (r7 != 0) goto L_0x00de;
    L_0x00d9:
        r10 = r2.getAddress();
        return r10;
    L_0x00de:
        r10 = r9.getHeap();
        r11 = r3.getAddress();
        r10 = r10[r11];
        r0.setSymbol(r10);
        r10 = r3.getAddress();
        r10 = r10 + r6;
        r0.setSuccessor(r10);
        r10 = r2.getNumStats();
        if (r10 == r6) goto L_0x0154;
    L_0x00f9:
        r10 = r2.getAddress();
        r11 = r9.subAlloc;
        r11 = r11.getPText();
        if (r10 > r11) goto L_0x0106;
    L_0x0105:
        return r5;
    L_0x0106:
        r10 = r2.getFreqData();
        r10 = r10.getStats();
        r4.setAddress(r10);
        r10 = r4.getSymbol();
        r11 = r0.getSymbol();
        if (r10 == r11) goto L_0x0128;
    L_0x011b:
        r4.incAddress();
        r10 = r4.getSymbol();
        r11 = r0.getSymbol();
        if (r10 != r11) goto L_0x011b;
    L_0x0128:
        r10 = r4.getFreq();
        r10 = r10 - r6;
        r11 = r2.getFreqData();
        r11 = r11.getSummFreq();
        r3 = r2.getNumStats();
        r11 = r11 - r3;
        r11 = r11 - r10;
        r3 = r10 * 2;
        if (r3 > r11) goto L_0x0147;
    L_0x013f:
        r10 = r10 * 5;
        if (r10 <= r11) goto L_0x0145;
    L_0x0143:
        r10 = 1;
        goto L_0x014f;
    L_0x0145:
        r10 = 0;
        goto L_0x014f;
    L_0x0147:
        r10 = r11 * 3;
        r3 = r3 + r10;
        r3 = r3 - r6;
        r11 = r11 * 2;
        r10 = r3 / r11;
    L_0x014f:
        r10 = r10 + r6;
        r0.setFreq(r10);
        goto L_0x015f;
    L_0x0154:
        r10 = r2.getOneState();
        r10 = r10.getFreq();
        r0.setFreq(r10);
    L_0x015f:
        r10 = r9.ps;
        r7 = r7 + -1;
        r10 = r10[r7];
        r1.setAddress(r10);
        r10 = r2.createChild(r9, r1, r0);
        r2.setAddress(r10);
        r10 = r2.getAddress();
        if (r10 != 0) goto L_0x0176;
    L_0x0175:
        return r5;
    L_0x0176:
        if (r7 != 0) goto L_0x015f;
    L_0x0178:
        r10 = r2.getAddress();
        return r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: ir.mahdi.mzip.rar.unpack.ppm.ModelPPM.createSuccessors(boolean, ir.mahdi.mzip.rar.unpack.ppm.State):int");
    }

    private void updateModelRestart() {
        restartModelRare();
        this.escCount = 0;
    }

    private void updateModel() {
        StateRef stateRef = this.tempStateRef1;
        stateRef.setValues(this.foundState);
        State init = this.tempState3.init(getHeap());
        State init2 = this.tempState4.init(getHeap());
        PPMContext init3 = this.tempPPMContext3.init(getHeap());
        PPMContext init4 = this.tempPPMContext4.init(getHeap());
        init3.setAddress(this.minContext.getSuffix());
        if (stateRef.getFreq() < 31 && init3.getAddress() != 0) {
            if (init3.getNumStats() != 1) {
                init.setAddress(init3.getFreqData().getStats());
                if (init.getSymbol() != stateRef.getSymbol()) {
                    do {
                        init.incAddress();
                    } while (init.getSymbol() != stateRef.getSymbol());
                    init2.setAddress(init.getAddress() - 6);
                    if (init.getFreq() >= init2.getFreq()) {
                        State.ppmdSwap(init, init2);
                        init.decAddress();
                    }
                }
                if (init.getFreq() < 115) {
                    init.incFreq(2);
                    init3.getFreqData().incSummFreq(2);
                }
            } else {
                init.setAddress(init3.getOneState().getAddress());
                if (init.getFreq() < 32) {
                    init.incFreq(1);
                }
            }
        }
        if (this.orderFall == 0) {
            this.foundState.setSuccessor(createSuccessors(true, init));
            this.minContext.setAddress(this.foundState.getSuccessor());
            this.maxContext.setAddress(this.foundState.getSuccessor());
            if (this.minContext.getAddress() == 0) {
                updateModelRestart();
                return;
            }
            return;
        }
        this.subAlloc.getHeap()[this.subAlloc.getPText()] = (byte) stateRef.getSymbol();
        this.subAlloc.incPText();
        init4.setAddress(this.subAlloc.getPText());
        if (this.subAlloc.getPText() >= this.subAlloc.getFakeUnitsStart()) {
            updateModelRestart();
            return;
        }
        int i;
        if (stateRef.getSuccessor() != 0) {
            if (stateRef.getSuccessor() <= this.subAlloc.getPText()) {
                stateRef.setSuccessor(createSuccessors(false, init));
                if (stateRef.getSuccessor() == 0) {
                    updateModelRestart();
                    return;
                }
            }
            i = this.orderFall - 1;
            this.orderFall = i;
            if (i == 0) {
                init4.setAddress(stateRef.getSuccessor());
                if (this.maxContext.getAddress() != this.minContext.getAddress()) {
                    this.subAlloc.decPText(1);
                }
            }
        } else {
            this.foundState.setSuccessor(init4.getAddress());
            stateRef.setSuccessor(this.minContext);
        }
        i = this.minContext.getNumStats();
        int summFreq = (this.minContext.getFreqData().getSummFreq() - i) - (stateRef.getFreq() - 1);
        init3.setAddress(this.maxContext.getAddress());
        while (init3.getAddress() != this.minContext.getAddress()) {
            int i2;
            int numStats = init3.getNumStats();
            if (numStats != 1) {
                if ((numStats & 1) == 0) {
                    init3.getFreqData().setStats(this.subAlloc.expandUnits(init3.getFreqData().getStats(), numStats >>> 1));
                    if (init3.getFreqData().getStats() == 0) {
                        updateModelRestart();
                        return;
                    }
                }
                init3.getFreqData().incSummFreq((numStats * 2 < i ? 1 : 0) + (((numStats * 4 <= i ? 1 : 0) & (init3.getFreqData().getSummFreq() <= numStats * 8 ? 1 : 0)) * 2));
            } else {
                init.setAddress(this.subAlloc.allocUnits(1));
                if (init.getAddress() == 0) {
                    updateModelRestart();
                    return;
                }
                init.setValues(init3.getOneState());
                init3.getFreqData().setStats(init);
                if (init.getFreq() < 30) {
                    init.incFreq(init.getFreq());
                } else {
                    init.setFreq(120);
                }
                init3.getFreqData().setSummFreq((init.getFreq() + this.initEsc) + (i > 3 ? 1 : 0));
            }
            int freq = (stateRef.getFreq() * 2) * (init3.getFreqData().getSummFreq() + 6);
            int summFreq2 = init3.getFreqData().getSummFreq() + summFreq;
            if (freq < summFreq2 * 6) {
                i2 = ((freq > summFreq2 ? 1 : 0) + 1) + (freq >= summFreq2 * 4 ? 1 : 0);
                init3.getFreqData().incSummFreq(3);
            } else {
                i2 = (((freq >= summFreq2 * 9 ? 1 : 0) + 4) + (freq >= summFreq2 * 12 ? 1 : 0)) + (freq >= summFreq2 * 15 ? 1 : 0);
                init3.getFreqData().incSummFreq(i2);
            }
            init.setAddress(init3.getFreqData().getStats() + (numStats * 6));
            init.setSuccessor(init4);
            init.setSymbol(stateRef.getSymbol());
            init.setFreq(i2);
            init3.setNumStats(numStats + 1);
            init3.setAddress(init3.getSuffix());
        }
        int successor = stateRef.getSuccessor();
        this.maxContext.setAddress(successor);
        this.minContext.setAddress(successor);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ModelPPM[");
        stringBuilder.append("\n  numMasked=");
        stringBuilder.append(this.numMasked);
        stringBuilder.append("\n  initEsc=");
        stringBuilder.append(this.initEsc);
        stringBuilder.append("\n  orderFall=");
        stringBuilder.append(this.orderFall);
        stringBuilder.append("\n  maxOrder=");
        stringBuilder.append(this.maxOrder);
        stringBuilder.append("\n  runLength=");
        stringBuilder.append(this.runLength);
        stringBuilder.append("\n  initRL=");
        stringBuilder.append(this.initRL);
        stringBuilder.append("\n  escCount=");
        stringBuilder.append(this.escCount);
        stringBuilder.append("\n  prevSuccess=");
        stringBuilder.append(this.prevSuccess);
        stringBuilder.append("\n  foundState=");
        stringBuilder.append(this.foundState);
        stringBuilder.append("\n  coder=");
        stringBuilder.append(this.coder);
        stringBuilder.append("\n  subAlloc=");
        stringBuilder.append(this.subAlloc);
        stringBuilder.append("\n]");
        return stringBuilder.toString();
    }
}
