package org.mozilla.intl.chardet;

public abstract class nsPSMDetector {
    public static final int ALL = 0;
    public static final int CHINESE = 2;
    public static final int JAPANESE = 1;
    public static final int KOREAN = 5;
    public static final int MAX_VERIFIERS = 16;
    public static final int NO_OF_LANGUAGES = 6;
    public static final int SIMPLIFIED_CHINESE = 3;
    public static final int TRADITIONAL_CHINESE = 4;
    int mClassItems;
    boolean mClassRunSampler;
    boolean mDone;
    int[] mItemIdx;
    int mItems;
    boolean mRunSampler;
    nsEUCSampler mSampler;
    byte[] mState;
    nsEUCStatistics[] mStatisticsData;
    nsVerifier[] mVerifier;

    public nsPSMDetector() {
        this.mSampler = new nsEUCSampler();
        this.mState = new byte[16];
        this.mItemIdx = new int[16];
        initVerifiers(0);
        Reset();
    }

    public nsPSMDetector(int i) {
        this.mSampler = new nsEUCSampler();
        this.mState = new byte[16];
        this.mItemIdx = new int[16];
        initVerifiers(i);
        Reset();
    }

    public nsPSMDetector(int i, nsVerifier[] nsverifierArr, nsEUCStatistics[] nseucstatisticsArr) {
        this.mSampler = new nsEUCSampler();
        this.mState = new byte[16];
        this.mItemIdx = new int[16];
        this.mClassRunSampler = nseucstatisticsArr != null;
        this.mStatisticsData = nseucstatisticsArr;
        this.mVerifier = nsverifierArr;
        this.mClassItems = i;
        Reset();
    }

    public void DataEnd() {
        if (!this.mDone) {
            if (this.mItems == 2) {
                nsVerifier nsverifier;
                if (this.mVerifier[this.mItemIdx[0]].charset().equals("GB18030")) {
                    nsverifier = this.mVerifier[this.mItemIdx[1]];
                } else if (this.mVerifier[this.mItemIdx[1]].charset().equals("GB18030")) {
                    nsverifier = this.mVerifier[this.mItemIdx[0]];
                }
                Report(nsverifier.charset());
                this.mDone = true;
            }
            if (this.mRunSampler) {
                Sample(null, 0, true);
            }
        }
    }

    public boolean HandleData(byte[] bArr, int i) {
        int i2 = 0;
        loop0:
        while (i2 < i) {
            nsVerifier nsverifier;
            byte b = bArr[i2];
            int i3 = 0;
            while (i3 < this.mItems) {
                byte nextState = nsVerifier.getNextState(this.mVerifier[this.mItemIdx[i3]], b, this.mState[i3]);
                if (nextState == (byte) 2) {
                    nsverifier = this.mVerifier[this.mItemIdx[i3]];
                    break loop0;
                } else if (nextState == (byte) 1) {
                    this.mItems--;
                    if (i3 < this.mItems) {
                        this.mItemIdx[i3] = this.mItemIdx[this.mItems];
                        this.mState[i3] = this.mState[this.mItems];
                    }
                } else {
                    int i4 = i3 + 1;
                    this.mState[i3] = nextState;
                    i3 = i4;
                }
            }
            if (this.mItems <= 1) {
                if (1 == this.mItems) {
                    nsverifier = this.mVerifier[this.mItemIdx[0]];
                }
                this.mDone = true;
                return this.mDone;
            }
            int i5 = 0;
            i3 = 0;
            int i6 = 0;
            while (i5 < this.mItems) {
                if (!(this.mVerifier[this.mItemIdx[i5]].isUCS2() || this.mVerifier[this.mItemIdx[i5]].isUCS2())) {
                    i3++;
                    i6 = i5;
                }
                i5++;
            }
            if (1 == i3) {
                nsverifier = this.mVerifier[this.mItemIdx[i6]];
            } else {
                i2++;
            }
            Report(nsverifier.charset());
            this.mDone = true;
            return this.mDone;
        }
        if (this.mRunSampler) {
            Sample(bArr, i);
        }
        return this.mDone;
    }

    public abstract void Report(String str);

    public void Reset() {
        this.mRunSampler = this.mClassRunSampler;
        this.mDone = false;
        this.mItems = this.mClassItems;
        for (int i = 0; i < this.mItems; i++) {
            this.mState[i] = (byte) 0;
            this.mItemIdx[i] = i;
        }
        this.mSampler.Reset();
    }

    public void Sample(byte[] bArr, int i) {
        Sample(bArr, i, false);
    }

    public void Sample(byte[] bArr, int i, boolean z) {
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i3 < this.mItems) {
            if (this.mStatisticsData[this.mItemIdx[i3]] != null) {
                i4++;
            }
            if (!(this.mVerifier[this.mItemIdx[i3]].isUCS2() || this.mVerifier[this.mItemIdx[i3]].charset().equals("GB18030"))) {
                i5++;
            }
            i3++;
        }
        this.mRunSampler = i4 > 1;
        if (this.mRunSampler) {
            this.mRunSampler = this.mSampler.Sample(bArr, i);
            if (((z && this.mSampler.GetSomeData()) || this.mSampler.EnoughData()) && i4 == i5) {
                this.mSampler.CalFreq();
                int i6 = -1;
                i = 0;
                float f = 0.0f;
                while (i2 < this.mItems) {
                    if (!(this.mStatisticsData[this.mItemIdx[i2]] == null || this.mVerifier[this.mItemIdx[i2]].charset().equals("Big5"))) {
                        float GetScore = this.mSampler.GetScore(this.mStatisticsData[this.mItemIdx[i2]].mFirstByteFreq(), this.mStatisticsData[this.mItemIdx[i2]].mFirstByteWeight(), this.mStatisticsData[this.mItemIdx[i2]].mSecondByteFreq(), this.mStatisticsData[this.mItemIdx[i2]].mSecondByteWeight());
                        i5 = i + 1;
                        if (i != 0) {
                            if (f > GetScore) {
                            }
                            i = i5;
                        }
                        i6 = i2;
                        f = GetScore;
                        i = i5;
                    }
                    i2++;
                }
                if (i6 >= 0) {
                    Report(this.mVerifier[this.mItemIdx[i6]].charset());
                    this.mDone = true;
                }
            }
        }
    }

    public String[] getProbableCharsets() {
        if (this.mItems <= 0) {
            return new String[]{"nomatch"};
        }
        String[] strArr = new String[this.mItems];
        for (int i = 0; i < this.mItems; i++) {
            strArr[i] = this.mVerifier[this.mItemIdx[i]].charset();
        }
        return strArr;
    }

    protected void initVerifiers(int i) {
        nsEUCStatistics[] nseucstatisticsArr;
        nsPSMDetector nspsmdetector = this;
        int i2 = i;
        boolean z = false;
        if (i2 < 0 || i2 >= 6) {
            i2 = 0;
        }
        nspsmdetector.mVerifier = null;
        nspsmdetector.mStatisticsData = null;
        if (i2 == 4) {
            nspsmdetector.mVerifier = new nsVerifier[]{new nsUTF8Verifier(), new nsBIG5Verifier(), new nsISO2022CNVerifier(), new nsEUCTWVerifier(), new nsCP1252Verifier(), new nsUCS2BEVerifier(), new nsUCS2LEVerifier()};
            nseucstatisticsArr = new nsEUCStatistics[]{null, new Big5Statistics(), null, new EUCTWStatistics(), null, null, null};
        } else {
            nsVerifier[] nsverifierArr;
            if (i2 == 5) {
                nsverifierArr = new nsVerifier[]{new nsUTF8Verifier(), new nsEUCKRVerifier(), new nsISO2022KRVerifier(), new nsCP1252Verifier(), new nsUCS2BEVerifier(), new nsUCS2LEVerifier()};
            } else if (i2 == 3) {
                nsverifierArr = new nsVerifier[]{new nsUTF8Verifier(), new nsGB2312Verifier(), new nsGB18030Verifier(), new nsISO2022CNVerifier(), new nsHZVerifier(), new nsCP1252Verifier(), new nsUCS2BEVerifier(), new nsUCS2LEVerifier()};
            } else if (i2 == 1) {
                nsverifierArr = new nsVerifier[]{new nsUTF8Verifier(), new nsSJISVerifier(), new nsEUCJPVerifier(), new nsISO2022JPVerifier(), new nsCP1252Verifier(), new nsUCS2BEVerifier(), new nsUCS2LEVerifier()};
            } else if (i2 == 2) {
                nspsmdetector.mVerifier = new nsVerifier[]{new nsUTF8Verifier(), new nsGB2312Verifier(), new nsGB18030Verifier(), new nsBIG5Verifier(), new nsISO2022CNVerifier(), new nsHZVerifier(), new nsEUCTWVerifier(), new nsCP1252Verifier(), new nsUCS2BEVerifier(), new nsUCS2LEVerifier()};
                nseucstatisticsArr = new nsEUCStatistics[]{null, new GB2312Statistics(), null, new Big5Statistics(), null, null, new EUCTWStatistics(), null, null, null};
            } else {
                if (i2 == 0) {
                    nspsmdetector.mVerifier = new nsVerifier[]{new nsUTF8Verifier(), new nsSJISVerifier(), new nsEUCJPVerifier(), new nsISO2022JPVerifier(), new nsEUCKRVerifier(), new nsISO2022KRVerifier(), new nsBIG5Verifier(), new nsEUCTWVerifier(), new nsGB2312Verifier(), new nsGB18030Verifier(), new nsISO2022CNVerifier(), new nsHZVerifier(), new nsCP1252Verifier(), new nsUCS2BEVerifier(), new nsUCS2LEVerifier()};
                    nseucstatisticsArr = new nsEUCStatistics[]{null, null, new EUCJPStatistics(), null, new EUCKRStatistics(), null, new Big5Statistics(), new EUCTWStatistics(), new GB2312Statistics(), null, null, null, null, null, null};
                }
                if (nspsmdetector.mStatisticsData != null) {
                    z = true;
                }
                nspsmdetector.mClassRunSampler = z;
                nspsmdetector.mClassItems = nspsmdetector.mVerifier.length;
            }
            nspsmdetector.mVerifier = nsverifierArr;
            if (nspsmdetector.mStatisticsData != null) {
                z = true;
            }
            nspsmdetector.mClassRunSampler = z;
            nspsmdetector.mClassItems = nspsmdetector.mVerifier.length;
        }
        nspsmdetector.mStatisticsData = nseucstatisticsArr;
        if (nspsmdetector.mStatisticsData != null) {
            z = true;
        }
        nspsmdetector.mClassRunSampler = z;
        nspsmdetector.mClassItems = nspsmdetector.mVerifier.length;
    }
}
