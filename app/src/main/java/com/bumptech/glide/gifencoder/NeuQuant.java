package com.bumptech.glide.gifencoder;

class NeuQuant {
    protected static final int alphabiasshift = 10;
    protected static final int alpharadbias = 262144;
    protected static final int alpharadbshift = 18;
    protected static final int beta = 64;
    protected static final int betagamma = 65536;
    protected static final int betashift = 10;
    protected static final int gamma = 1024;
    protected static final int gammashift = 10;
    protected static final int initalpha = 1024;
    protected static final int initrad = 32;
    protected static final int initradius = 2048;
    protected static final int intbias = 65536;
    protected static final int intbiasshift = 16;
    protected static final int maxnetpos = 255;
    protected static final int minpicturebytes = 1509;
    protected static final int ncycles = 100;
    protected static final int netbiasshift = 4;
    protected static final int netsize = 256;
    protected static final int prime1 = 499;
    protected static final int prime2 = 491;
    protected static final int prime3 = 487;
    protected static final int prime4 = 503;
    protected static final int radbias = 256;
    protected static final int radbiasshift = 8;
    protected static final int radiusbias = 64;
    protected static final int radiusbiasshift = 6;
    protected static final int radiusdec = 30;
    protected int alphadec;
    protected int[] bias = new int[256];
    protected int[] freq = new int[256];
    protected int lengthcount;
    protected int[] netindex = new int[256];
    protected int[][] network;
    protected int[] radpower = new int[32];
    protected int samplefac;
    protected byte[] thepicture;

    public NeuQuant(byte[] bArr, int i, int i2) {
        this.thepicture = bArr;
        this.lengthcount = i;
        this.samplefac = i2;
        this.network = new int[256][];
        for (i = 0; i < 256; i++) {
            this.network[i] = new int[4];
            i2 = this.network[i];
            int i3 = (i << 12) / 256;
            i2[2] = i3;
            i2[1] = i3;
            i2[0] = i3;
            this.freq[i] = 256;
            this.bias[i] = null;
        }
    }

    public byte[] colorMap() {
        int i;
        byte[] bArr = new byte[768];
        int[] iArr = new int[256];
        for (i = 0; i < 256; i++) {
            iArr[this.network[i][3]] = i;
        }
        i = 0;
        int i2 = 0;
        while (i < 256) {
            int i3 = iArr[i];
            int i4 = i2 + 1;
            bArr[i2] = (byte) this.network[i3][0];
            i2 = i4 + 1;
            bArr[i4] = (byte) this.network[i3][1];
            i4 = i2 + 1;
            bArr[i2] = (byte) this.network[i3][2];
            i++;
            i2 = i4;
        }
        return bArr;
    }

    public void inxbuild() {
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i < 256) {
            int i4;
            int[] iArr = this.network[i];
            int i5 = i + 1;
            int i6 = i;
            int i7 = iArr[1];
            for (i4 = i5; i4 < 256; i4++) {
                int[] iArr2 = this.network[i4];
                if (iArr2[1] < i7) {
                    i7 = iArr2[1];
                    i6 = i4;
                }
            }
            int[] iArr3 = this.network[i6];
            if (i != i6) {
                i4 = iArr3[0];
                iArr3[0] = iArr[0];
                iArr[0] = i4;
                i4 = iArr3[1];
                iArr3[1] = iArr[1];
                iArr[1] = i4;
                i6 = iArr3[2];
                iArr3[2] = iArr[2];
                iArr[2] = i6;
                i6 = iArr3[3];
                iArr3[3] = iArr[3];
                iArr[3] = i6;
            }
            if (i7 != i2) {
                this.netindex[i2] = (i3 + i) >> 1;
                while (true) {
                    i2++;
                    if (i2 >= i7) {
                        break;
                    }
                    this.netindex[i2] = i;
                }
                i3 = i;
                i2 = i7;
            }
            i = i5;
        }
        this.netindex[i2] = (i3 + 255) >> 1;
        for (i2++; i2 < 256; i2++) {
            this.netindex[i2] = 255;
        }
    }

    public void learn() {
        int i;
        if (this.lengthcount < minpicturebytes) {
            r6.samplefac = 1;
        }
        r6.alphadec = ((r6.samplefac - 1) / 3) + 30;
        byte[] bArr = r6.thepicture;
        int i2 = r6.lengthcount;
        int i3 = r6.lengthcount / (r6.samplefac * 3);
        int i4 = i3 / 100;
        for (i = 0; i < 32; i++) {
            r6.radpower[i] = 1024 * (((1024 - (i * i)) * 256) / 1024);
        }
        int i5 = r6.lengthcount < minpicturebytes ? 3 : r6.lengthcount % prime1 != 0 ? 1497 : r6.lengthcount % prime2 != 0 ? 1473 : r6.lengthcount % prime3 != 0 ? 1461 : minpicturebytes;
        int i6 = i4;
        int i7 = 0;
        int i8 = 0;
        int i9 = 1024;
        int i10 = 32;
        int i11 = 2048;
        while (i7 < i3) {
            i = (bArr[i8 + 0] & 255) << 4;
            int i12 = (bArr[i8 + 1] & 255) << 4;
            int i13 = (bArr[i8 + 2] & 255) << 4;
            int contest = contest(i, i12, i13);
            int i14 = i13;
            int i15 = i12;
            int i16 = i;
            altersingle(i9, contest, i, i12, i14);
            if (i10 != 0) {
                alterneigh(i10, contest, i16, i15, i14);
            }
            i8 += i5;
            if (i8 >= i2) {
                i8 -= r6.lengthcount;
            }
            i7++;
            if (i6 == 0) {
                i6 = 1;
            }
            if (i7 % i6 == 0) {
                i9 -= i9 / r6.alphadec;
                i11 -= i11 / 30;
                i4 = i11 >> 6;
                if (i4 <= 1) {
                    i4 = 0;
                }
                for (int i17 = 0; i17 < i4; i17++) {
                    i13 = i4 * i4;
                    r6.radpower[i17] = (((i13 - (i17 * i17)) * 256) / i13) * i9;
                }
                i10 = i4;
            }
        }
    }

    public int map(int i, int i2, int i3) {
        int i4 = this.netindex[i2];
        int i5 = i4 - 1;
        int i6 = -1;
        int i7 = 1000;
        while (true) {
            if (i4 >= 256) {
                if (i5 < 0) {
                    return i6;
                }
            }
            if (i4 < 256) {
                int[] iArr = this.network[i4];
                int i8 = iArr[1] - i2;
                if (i8 >= i7) {
                    i4 = 256;
                } else {
                    i4++;
                    if (i8 < 0) {
                        i8 = -i8;
                    }
                    int i9 = iArr[0] - i;
                    if (i9 < 0) {
                        i9 = -i9;
                    }
                    i8 += i9;
                    if (i8 < i7) {
                        i9 = iArr[2] - i3;
                        if (i9 < 0) {
                            i9 = -i9;
                        }
                        i8 += i9;
                        if (i8 < i7) {
                            i6 = iArr[3];
                            i7 = i8;
                        }
                    }
                }
            }
            if (i5 >= 0) {
                int[] iArr2 = this.network[i5];
                int i10 = i2 - iArr2[1];
                if (i10 >= i7) {
                    i5 = -1;
                } else {
                    i5--;
                    if (i10 < 0) {
                        i10 = -i10;
                    }
                    int i11 = iArr2[0] - i;
                    if (i11 < 0) {
                        i11 = -i11;
                    }
                    i10 += i11;
                    if (i10 < i7) {
                        int i12 = iArr2[2] - i3;
                        if (i12 < 0) {
                            i12 = -i12;
                        }
                        i12 += i10;
                        if (i12 < i7) {
                            i6 = iArr2[3];
                            i7 = i12;
                        }
                    }
                }
            }
        }
    }

    public byte[] process() {
        learn();
        unbiasnet();
        inxbuild();
        return colorMap();
    }

    public void unbiasnet() {
        for (int i = 0; i < 256; i++) {
            int[] iArr = this.network[i];
            iArr[0] = iArr[0] >> 4;
            iArr = this.network[i];
            iArr[1] = iArr[1] >> 4;
            iArr = this.network[i];
            iArr[2] = iArr[2] >> 4;
            this.network[i][3] = i;
        }
    }

    protected void alterneigh(int r15, int r16, int r17, int r18, int r19) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r14 = this;
        r0 = r14;
        r3 = r16 - r15;
        r4 = -1;
        if (r3 >= r4) goto L_0x0007;
    L_0x0006:
        r3 = -1;
    L_0x0007:
        r1 = r16 + r15;
        r4 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        if (r1 <= r4) goto L_0x000f;
    L_0x000d:
        r1 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
    L_0x000f:
        r4 = r16 + 1;
        r5 = 1;
        r2 = r16 + -1;
        r6 = 1;
    L_0x0015:
        if (r4 < r1) goto L_0x001b;
    L_0x0017:
        if (r2 <= r3) goto L_0x001a;
    L_0x0019:
        goto L_0x001b;
    L_0x001a:
        return;
    L_0x001b:
        r7 = r0.radpower;
        r8 = r6 + 1;
        r6 = r7[r6];
        r7 = 2;
        r9 = 0;
        r10 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        if (r4 >= r1) goto L_0x0052;
    L_0x0027:
        r11 = r0.network;
        r12 = r4 + 1;
        r4 = r11[r4];
        r11 = r4[r9];	 Catch:{ Exception -> 0x0051 }
        r13 = r4[r9];	 Catch:{ Exception -> 0x0051 }
        r13 = r13 - r17;	 Catch:{ Exception -> 0x0051 }
        r13 = r13 * r6;	 Catch:{ Exception -> 0x0051 }
        r13 = r13 / r10;	 Catch:{ Exception -> 0x0051 }
        r11 = r11 - r13;	 Catch:{ Exception -> 0x0051 }
        r4[r9] = r11;	 Catch:{ Exception -> 0x0051 }
        r11 = r4[r5];	 Catch:{ Exception -> 0x0051 }
        r13 = r4[r5];	 Catch:{ Exception -> 0x0051 }
        r13 = r13 - r18;	 Catch:{ Exception -> 0x0051 }
        r13 = r13 * r6;	 Catch:{ Exception -> 0x0051 }
        r13 = r13 / r10;	 Catch:{ Exception -> 0x0051 }
        r11 = r11 - r13;	 Catch:{ Exception -> 0x0051 }
        r4[r5] = r11;	 Catch:{ Exception -> 0x0051 }
        r11 = r4[r7];	 Catch:{ Exception -> 0x0051 }
        r13 = r4[r7];	 Catch:{ Exception -> 0x0051 }
        r13 = r13 - r19;	 Catch:{ Exception -> 0x0051 }
        r13 = r13 * r6;	 Catch:{ Exception -> 0x0051 }
        r13 = r13 / r10;	 Catch:{ Exception -> 0x0051 }
        r11 = r11 - r13;	 Catch:{ Exception -> 0x0051 }
        r4[r7] = r11;	 Catch:{ Exception -> 0x0051 }
    L_0x0051:
        r4 = r12;
    L_0x0052:
        if (r2 <= r3) goto L_0x0081;
    L_0x0054:
        r11 = r0.network;
        r12 = r2 + -1;
        r2 = r11[r2];
        r11 = r2[r9];	 Catch:{ Exception -> 0x007e }
        r13 = r2[r9];	 Catch:{ Exception -> 0x007e }
        r13 = r13 - r17;	 Catch:{ Exception -> 0x007e }
        r13 = r13 * r6;	 Catch:{ Exception -> 0x007e }
        r13 = r13 / r10;	 Catch:{ Exception -> 0x007e }
        r11 = r11 - r13;	 Catch:{ Exception -> 0x007e }
        r2[r9] = r11;	 Catch:{ Exception -> 0x007e }
        r9 = r2[r5];	 Catch:{ Exception -> 0x007e }
        r11 = r2[r5];	 Catch:{ Exception -> 0x007e }
        r11 = r11 - r18;	 Catch:{ Exception -> 0x007e }
        r11 = r11 * r6;	 Catch:{ Exception -> 0x007e }
        r11 = r11 / r10;	 Catch:{ Exception -> 0x007e }
        r9 = r9 - r11;	 Catch:{ Exception -> 0x007e }
        r2[r5] = r9;	 Catch:{ Exception -> 0x007e }
        r9 = r2[r7];	 Catch:{ Exception -> 0x007e }
        r11 = r2[r7];	 Catch:{ Exception -> 0x007e }
        r11 = r11 - r19;	 Catch:{ Exception -> 0x007e }
        r6 = r6 * r11;	 Catch:{ Exception -> 0x007e }
        r6 = r6 / r10;	 Catch:{ Exception -> 0x007e }
        r9 = r9 - r6;	 Catch:{ Exception -> 0x007e }
        r2[r7] = r9;	 Catch:{ Exception -> 0x007e }
    L_0x007e:
        r6 = r8;
        r2 = r12;
        goto L_0x0015;
    L_0x0081:
        r6 = r8;
        goto L_0x0015;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.gifencoder.NeuQuant.alterneigh(int, int, int, int, int):void");
    }

    protected void altersingle(int i, int i2, int i3, int i4, int i5) {
        i2 = this.network[i2];
        i2[0] = i2[0] - (((i2[0] - i3) * i) / 1024);
        i2[1] = i2[1] - (((i2[1] - i4) * i) / 1024);
        i2[2] = i2[2] - ((i * (i2[2] - i5)) / 1024);
    }

    protected int contest(int i, int i2, int i3) {
        int i4 = -1;
        int i5 = -1;
        int i6 = Integer.MAX_VALUE;
        int i7 = Integer.MAX_VALUE;
        for (int i8 = 0; i8 < 256; i8++) {
            int[] iArr = this.network[i8];
            int i9 = iArr[0] - i;
            if (i9 < 0) {
                i9 = -i9;
            }
            int i10 = iArr[1] - i2;
            if (i10 < 0) {
                i10 = -i10;
            }
            i9 += i10;
            int i11 = iArr[2] - i3;
            if (i11 < 0) {
                i11 = -i11;
            }
            i9 += i11;
            if (i9 < i6) {
                i4 = i8;
                i6 = i9;
            }
            i9 -= this.bias[i8] >> 12;
            if (i9 < i7) {
                i5 = i8;
                i7 = i9;
            }
            i11 = this.freq[i8] >> 10;
            int[] iArr2 = this.freq;
            iArr2[i8] = iArr2[i8] - i11;
            iArr2 = this.bias;
            iArr2[i8] = iArr2[i8] + (i11 << 10);
        }
        i = this.freq;
        i[i4] = i[i4] + 64;
        i = this.bias;
        i[i4] = i[i4] - 65536;
        return i5;
    }
}
