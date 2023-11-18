package org.mozilla.universalchardet.prober.contextanalysis;

public class SJISContextAnalysis extends JapaneseContextAnalysis {
    public static final int HIGHBYTE_BEGIN_1 = 129;
    public static final int HIGHBYTE_BEGIN_2 = 224;
    public static final int HIGHBYTE_END_1 = 159;
    public static final int HIGHBYTE_END_2 = 239;
    public static final int HIRAGANA_HIGHBYTE = 130;
    public static final int HIRAGANA_LOWBYTE_BEGIN = 159;
    public static final int HIRAGANA_LOWBYTE_END = 241;

    protected int getOrder(byte[] bArr, int i) {
        if ((bArr[i] & 255) == 130) {
            int i2 = bArr[i + 1] & 255;
            if (i2 >= 159 && i2 <= HIRAGANA_LOWBYTE_END) {
                return i2 - 159;
            }
        }
        return -1;
    }

    protected void getOrder(Order order, byte[] bArr, int i) {
        order.order = -1;
        order.charLength = 1;
        int i2 = bArr[i] & 255;
        if ((i2 >= 129 && i2 <= 159) || (i2 >= 224 && i2 <= 239)) {
            order.charLength = 2;
        }
        if (i2 == 130) {
            int i3 = bArr[i + 1] & 255;
            if (i3 >= 159 && i3 <= HIRAGANA_LOWBYTE_END) {
                order.order = i3 - 159;
            }
        }
    }
}
