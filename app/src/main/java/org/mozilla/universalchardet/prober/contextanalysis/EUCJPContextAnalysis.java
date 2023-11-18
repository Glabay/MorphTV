package org.mozilla.universalchardet.prober.contextanalysis;

public class EUCJPContextAnalysis extends JapaneseContextAnalysis {
    public static final int FIRSTPLANE_HIGHBYTE_BEGIN = 161;
    public static final int FIRSTPLANE_HIGHBYTE_END = 254;
    public static final int HIRAGANA_HIGHBYTE = 164;
    public static final int HIRAGANA_LOWBYTE_BEGIN = 161;
    public static final int HIRAGANA_LOWBYTE_END = 243;
    public static final int SINGLE_SHIFT_2 = 142;
    public static final int SINGLE_SHIFT_3 = 143;

    protected int getOrder(byte[] bArr, int i) {
        if ((bArr[i] & 255) == 164) {
            int i2 = bArr[i + 1] & 255;
            if (i2 >= 161 && i2 <= 243) {
                return i2 - 161;
            }
        }
        return -1;
    }

    protected void getOrder(Order order, byte[] bArr, int i) {
        int i2;
        int i3;
        order.order = -1;
        order.charLength = 1;
        int i4 = bArr[i] & 255;
        if (i4 != SINGLE_SHIFT_2) {
            if (i4 < 161 || i4 > 254) {
                if (i4 == SINGLE_SHIFT_3) {
                    i2 = 3;
                    order.charLength = i2;
                }
                if (i4 == 164) {
                    i3 = bArr[i + 1] & 255;
                    if (i3 >= 161 && i3 <= 243) {
                        order.order = i3 - 161;
                        return;
                    }
                }
            }
        }
        i2 = 2;
        order.charLength = i2;
        if (i4 == 164) {
            i3 = bArr[i + 1] & 255;
            if (i3 >= 161) {
            }
        }
    }
}
