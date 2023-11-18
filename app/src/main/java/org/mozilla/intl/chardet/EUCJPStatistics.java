package org.mozilla.intl.chardet;

public class EUCJPStatistics extends nsEUCStatistics {
    static float[] mFirstByteFreq;
    static float mFirstByteMean;
    static float mFirstByteStdDev;
    static float mFirstByteWeight;
    static float[] mSecondByteFreq;
    static float mSecondByteMean;
    static float mSecondByteStdDev;
    static float mSecondByteWeight;

    public EUCJPStatistics() {
        mFirstByteFreq = new float[]{0.364808f, 0.0f, 0.0f, 0.145325f, 0.304891f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.001835f, 0.010771f, 0.006462f, 0.001157f, 0.002114f, 0.003231f, 0.001356f, 0.00742f, 0.004189f, 0.003231f, 0.003032f, 0.03319f, 0.006303f, 0.006064f, 0.009973f, 0.002354f, 0.00367f, 0.009135f, 0.001675f, 0.002792f, 0.002194f, 0.01472f, 0.011928f, 8.78E-4f, 0.013124f, 0.001077f, 0.009295f, 0.003471f, 0.002872f, 0.002433f, 9.57E-4f, 0.001636f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 8.0E-5f, 2.79E-4f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 8.0E-5f, 0.0f};
        mFirstByteStdDev = 0.050407f;
        mFirstByteMean = 0.010638f;
        mFirstByteWeight = 0.640871f;
        mSecondByteFreq = new float[]{0.002473f, 0.039134f, 0.152745f, 0.009694f, 3.59E-4f, 0.02218f, 7.58E-4f, 0.004308f, 1.6E-4f, 0.002513f, 0.003072f, 0.001316f, 0.00383f, 0.001037f, 0.00359f, 9.57E-4f, 1.6E-4f, 2.39E-4f, 0.006462f, 0.001596f, 0.031554f, 0.001316f, 0.002194f, 0.016555f, 0.003271f, 6.78E-4f, 5.98E-4f, 0.206438f, 7.18E-4f, 0.001077f, 0.00371f, 0.001356f, 0.001356f, 4.39E-4f, 0.004388f, 0.005704f, 8.78E-4f, 0.010172f, 0.007061f, 0.01468f, 6.38E-4f, 0.02573f, 0.002792f, 7.18E-4f, 0.001795f, 0.091551f, 7.58E-4f, 0.003909f, 5.58E-4f, 0.031195f, 0.007061f, 0.001316f, 0.022579f, 0.006981f, 0.00726f, 0.001117f, 2.39E-4f, 0.012127f, 8.78E-4f, 0.00379f, 0.001077f, 7.58E-4f, 0.002114f, 0.002234f, 6.78E-4f, 0.002992f, 0.003311f, 0.023416f, 0.001237f, 0.002753f, 0.005146f, 0.002194f, 0.007021f, 0.008497f, 0.013763f, 0.011768f, 0.006303f, 0.001915f, 6.38E-4f, 0.008776f, 9.18E-4f, 0.003431f, 0.057603f, 4.39E-4f, 4.39E-4f, 7.58E-4f, 0.002872f, 0.001675f, 0.01105f, 0.0f, 2.79E-4f, 0.012127f, 7.18E-4f, 0.00738f};
        mSecondByteStdDev = 0.028247f;
        mSecondByteMean = 0.010638f;
        mSecondByteWeight = 0.359129f;
    }

    public float[] mFirstByteFreq() {
        return mFirstByteFreq;
    }

    public float mFirstByteMean() {
        return mFirstByteMean;
    }

    public float mFirstByteStdDev() {
        return mFirstByteStdDev;
    }

    public float mFirstByteWeight() {
        return mFirstByteWeight;
    }

    public float[] mSecondByteFreq() {
        return mSecondByteFreq;
    }

    public float mSecondByteMean() {
        return mSecondByteMean;
    }

    public float mSecondByteStdDev() {
        return mSecondByteStdDev;
    }

    public float mSecondByteWeight() {
        return mSecondByteWeight;
    }
}
