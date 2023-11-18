package org.mozilla.intl.chardet;

public class GB2312Statistics extends nsEUCStatistics {
    static float[] mFirstByteFreq;
    static float mFirstByteMean;
    static float mFirstByteStdDev;
    static float mFirstByteWeight;
    static float[] mSecondByteFreq;
    static float mSecondByteMean;
    static float mSecondByteStdDev;
    static float mSecondByteWeight;

    public GB2312Statistics() {
        mFirstByteFreq = new float[]{0.011628f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.011628f, 0.012403f, 0.009302f, 0.003876f, 0.017829f, 0.037209f, 0.008527f, 0.010078f, 0.01938f, 0.054264f, 0.010078f, 0.041085f, 0.02093f, 0.018605f, 0.010078f, 0.013178f, 0.016279f, 0.006202f, 0.009302f, 0.017054f, 0.011628f, 0.008527f, 0.004651f, 0.006202f, 0.017829f, 0.024806f, 0.020155f, 0.013953f, 0.032558f, 0.035659f, 0.068217f, 0.010853f, 0.036434f, 0.117054f, 0.027907f, 0.100775f, 0.010078f, 0.017829f, 0.062016f, 0.012403f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.00155f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        mFirstByteStdDev = 0.020081f;
        mFirstByteMean = 0.010638f;
        mFirstByteWeight = 0.586533f;
        mSecondByteFreq = new float[]{0.006202f, 0.031008f, 0.005426f, 0.003101f, 0.00155f, 0.003101f, 0.082171f, 0.014729f, 0.006977f, 0.00155f, 0.013953f, 0.0f, 0.013953f, 0.010078f, 0.008527f, 0.006977f, 0.004651f, 0.003101f, 0.003101f, 0.003101f, 0.008527f, 0.003101f, 0.005426f, 0.005426f, 0.005426f, 0.003101f, 0.00155f, 0.006202f, 0.014729f, 0.010853f, 0.0f, 0.011628f, 0.0f, 0.031783f, 0.013953f, 0.030233f, 0.039535f, 0.008527f, 0.015504f, 0.0f, 0.003101f, 0.008527f, 0.016279f, 0.005426f, 0.00155f, 0.013953f, 0.013953f, 0.044961f, 0.003101f, 0.004651f, 0.006977f, 0.00155f, 0.005426f, 0.012403f, 0.00155f, 0.015504f, 0.0f, 0.006202f, 0.00155f, 0.0f, 0.007752f, 0.006977f, 0.00155f, 0.009302f, 0.011628f, 0.004651f, 0.010853f, 0.012403f, 0.017829f, 0.005426f, 0.024806f, 0.0f, 0.006202f, 0.0f, 0.082171f, 0.015504f, 0.004651f, 0.0f, 0.006977f, 0.004651f, 0.0f, 0.008527f, 0.012403f, 0.004651f, 0.003876f, 0.003101f, 0.022481f, 0.024031f, 0.00155f, 0.047287f, 0.009302f, 0.00155f, 0.005426f, 0.017054f};
        mSecondByteStdDev = 0.014156f;
        mSecondByteMean = 0.010638f;
        mSecondByteWeight = 0.413467f;
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
