package org.mozilla.intl.chardet;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;

public class nsEUCSampler {
    public int[] mFirstByteCnt = new int[94];
    public float[] mFirstByteFreq = new float[94];
    public int[] mSecondByteCnt = new int[94];
    public float[] mSecondByteFreq = new float[94];
    int mState = 0;
    int mThreshold = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
    int mTotal = 0;

    public nsEUCSampler() {
        Reset();
    }

    void CalFreq() {
        for (int i = 0; i < 94; i++) {
            this.mFirstByteFreq[i] = ((float) this.mFirstByteCnt[i]) / ((float) this.mTotal);
            this.mSecondByteFreq[i] = ((float) this.mSecondByteCnt[i]) / ((float) this.mTotal);
        }
    }

    boolean EnoughData() {
        return this.mTotal > this.mThreshold;
    }

    float GetScore(float[] fArr, float f, float[] fArr2, float f2) {
        return (f * GetScore(fArr, this.mFirstByteFreq)) + (f2 * GetScore(fArr2, this.mSecondByteFreq));
    }

    float GetScore(float[] fArr, float[] fArr2) {
        float f = 0.0f;
        for (int i = 0; i < 94; i++) {
            float f2 = fArr[i] - fArr2[i];
            f += f2 * f2;
        }
        return ((float) Math.sqrt((double) f)) / 94.0f;
    }

    boolean GetSomeData() {
        return this.mTotal > 1;
    }

    public void Reset() {
        this.mTotal = 0;
        this.mState = 0;
        for (int i = 0; i < 94; i++) {
            int[] iArr = this.mFirstByteCnt;
            this.mSecondByteCnt[i] = 0;
            iArr[i] = 0;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean Sample(byte[] r9, int r10) {
        /*
        r8 = this;
        r0 = r8.mState;
        r1 = 0;
        r2 = 1;
        if (r0 != r2) goto L_0x0007;
    L_0x0006:
        return r1;
    L_0x0007:
        r0 = 0;
        r3 = 0;
    L_0x0009:
        if (r0 >= r10) goto L_0x0068;
    L_0x000b:
        r4 = r8.mState;
        if (r2 == r4) goto L_0x0068;
    L_0x000f:
        r4 = r8.mState;
        r5 = 161; // 0xa1 float:2.26E-43 double:7.95E-322;
        r6 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        switch(r4) {
            case 0: goto L_0x003f;
            case 1: goto L_0x0063;
            case 2: goto L_0x001b;
            default: goto L_0x0018;
        };
    L_0x0018:
        r8.mState = r2;
        goto L_0x0063;
    L_0x001b:
        r4 = r9[r3];
        r4 = r4 & 128;
        if (r4 == 0) goto L_0x0018;
    L_0x0021:
        r4 = r9[r3];
        r4 = r4 & r6;
        if (r6 == r4) goto L_0x0018;
    L_0x0026:
        r4 = r9[r3];
        r4 = r4 & r6;
        if (r5 <= r4) goto L_0x002c;
    L_0x002b:
        goto L_0x0018;
    L_0x002c:
        r4 = r8.mTotal;
        r4 = r4 + r2;
        r8.mTotal = r4;
        r4 = r8.mSecondByteCnt;
        r7 = r9[r3];
        r6 = r6 & r7;
        r6 = r6 - r5;
        r5 = r4[r6];
        r5 = r5 + r2;
        r4[r6] = r5;
        r8.mState = r1;
        goto L_0x0063;
    L_0x003f:
        r4 = r9[r3];
        r4 = r4 & 128;
        if (r4 == 0) goto L_0x0063;
    L_0x0045:
        r4 = r9[r3];
        r4 = r4 & r6;
        if (r6 == r4) goto L_0x0018;
    L_0x004a:
        r4 = r9[r3];
        r4 = r4 & r6;
        if (r5 <= r4) goto L_0x0050;
    L_0x004f:
        goto L_0x0018;
    L_0x0050:
        r4 = r8.mTotal;
        r4 = r4 + r2;
        r8.mTotal = r4;
        r4 = r8.mFirstByteCnt;
        r7 = r9[r3];
        r6 = r6 & r7;
        r6 = r6 - r5;
        r5 = r4[r6];
        r5 = r5 + r2;
        r4[r6] = r5;
        r4 = 2;
        r8.mState = r4;
    L_0x0063:
        r0 = r0 + 1;
        r3 = r3 + 1;
        goto L_0x0009;
    L_0x0068:
        r9 = r8.mState;
        if (r2 == r9) goto L_0x006d;
    L_0x006c:
        r1 = 1;
    L_0x006d:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.intl.chardet.nsEUCSampler.Sample(byte[], int):boolean");
    }
}
