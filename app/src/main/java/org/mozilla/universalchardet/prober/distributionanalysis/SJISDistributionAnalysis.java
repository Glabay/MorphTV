package org.mozilla.universalchardet.prober.distributionanalysis;

public class SJISDistributionAnalysis extends JISDistributionAnalysis {
    public static final int HIGHBYTE_BEGIN_1 = 129;
    public static final int HIGHBYTE_BEGIN_2 = 224;
    public static final int HIGHBYTE_END_1 = 159;
    public static final int HIGHBYTE_END_2 = 239;
    public static final int LOWBYTE_BEGIN_1 = 64;
    public static final int LOWBYTE_BEGIN_2 = 128;

    protected int getOrder(byte[] bArr, int i) {
        int i2 = bArr[i] & 255;
        if (i2 >= 129 && i2 <= 159) {
            i2 -= 129;
        } else if (i2 < 224 || i2 > 239) {
            return -1;
        } else {
            i2 = (i2 - 224) + 31;
        }
        int i3 = bArr[i + 1] & 255;
        i2 = (i2 * 188) + (i3 - 64);
        if (i3 >= 128) {
            i2--;
        }
        return i2;
    }
}
