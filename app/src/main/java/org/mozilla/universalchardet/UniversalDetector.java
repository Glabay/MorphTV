package org.mozilla.universalchardet;

import java.io.FileInputStream;
import java.io.PrintStream;
import org.mozilla.universalchardet.prober.CharsetProber;

public class UniversalDetector {
    public static final float MINIMUM_THRESHOLD = 0.2f;
    public static final float SHORTCUT_THRESHOLD = 0.95f;
    private String detectedCharset;
    private boolean done;
    private CharsetProber escCharsetProber = null;
    private boolean gotData;
    private InputState inputState;
    private byte lastChar;
    private CharsetListener listener;
    private CharsetProber[] probers = new CharsetProber[3];
    private boolean start;

    /* renamed from: org.mozilla.universalchardet.UniversalDetector$1 */
    static class C15081 implements CharsetListener {
        C15081() {
        }

        public void report(String str) {
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("charset = ");
            stringBuilder.append(str);
            printStream.println(stringBuilder.toString());
        }
    }

    public enum InputState {
        PURE_ASCII,
        ESC_ASCII,
        HIGHBYTE
    }

    public UniversalDetector(CharsetListener charsetListener) {
        this.listener = charsetListener;
        for (int i = 0; i < this.probers.length; i++) {
            this.probers[i] = null;
        }
        reset();
    }

    public static void main(String[] strArr) throws Exception {
        if (strArr.length != 1) {
            System.out.println("USAGE: java UniversalDetector filename");
            return;
        }
        UniversalDetector universalDetector = new UniversalDetector(new C15081());
        byte[] bArr = new byte[4096];
        FileInputStream fileInputStream = new FileInputStream(strArr[0]);
        while (true) {
            int read = fileInputStream.read(bArr);
            if (read <= 0 || universalDetector.isDone()) {
                universalDetector.dataEnd();
            } else {
                universalDetector.handleData(bArr, 0, read);
            }
        }
        universalDetector.dataEnd();
    }

    public void dataEnd() {
        if (!this.gotData) {
            return;
        }
        if (this.detectedCharset != null) {
            this.done = true;
            if (this.listener != null) {
                this.listener.report(this.detectedCharset);
            }
            return;
        }
        if (this.inputState == InputState.HIGHBYTE) {
            float f = 0.0f;
            int i = 0;
            for (int i2 = 0; i2 < this.probers.length; i2++) {
                float confidence = this.probers[i2].getConfidence();
                if (confidence > f) {
                    i = i2;
                    f = confidence;
                }
            }
            if (f > 0.2f) {
                this.detectedCharset = this.probers[i].getCharSetName();
                if (this.listener != null) {
                    this.listener.report(this.detectedCharset);
                    return;
                }
            }
        }
        InputState inputState = this.inputState;
        inputState = InputState.ESC_ASCII;
    }

    public String getDetectedCharset() {
        return this.detectedCharset;
    }

    public CharsetListener getListener() {
        return this.listener;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleData(byte[] r10, int r11, int r12) {
        /*
        r9 = this;
        r0 = r9.done;
        if (r0 == 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r0 = 1;
        if (r12 <= 0) goto L_0x000a;
    L_0x0008:
        r9.gotData = r0;
    L_0x000a:
        r1 = r9.start;
        r2 = 0;
        r3 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        if (r1 == 0) goto L_0x0076;
    L_0x0011:
        r9.start = r2;
        r1 = 3;
        if (r12 <= r1) goto L_0x0076;
    L_0x0016:
        r1 = r10[r11];
        r1 = r1 & r3;
        r4 = r11 + 1;
        r4 = r10[r4];
        r4 = r4 & r3;
        r5 = r11 + 2;
        r5 = r10[r5];
        r5 = r5 & r3;
        r6 = r11 + 3;
        r6 = r10[r6];
        r6 = r6 & r3;
        r7 = 254; // 0xfe float:3.56E-43 double:1.255E-321;
        if (r1 == 0) goto L_0x005d;
    L_0x002c:
        r8 = 239; // 0xef float:3.35E-43 double:1.18E-321;
        if (r1 == r8) goto L_0x0050;
    L_0x0030:
        switch(r1) {
            case 254: goto L_0x0042;
            case 255: goto L_0x0034;
            default: goto L_0x0033;
        };
    L_0x0033:
        goto L_0x006f;
    L_0x0034:
        if (r4 != r7) goto L_0x003d;
    L_0x0036:
        if (r5 != 0) goto L_0x003d;
    L_0x0038:
        if (r6 != 0) goto L_0x003d;
    L_0x003a:
        r1 = org.mozilla.universalchardet.Constants.CHARSET_UTF_32LE;
        goto L_0x005a;
    L_0x003d:
        if (r4 != r7) goto L_0x006f;
    L_0x003f:
        r1 = org.mozilla.universalchardet.Constants.CHARSET_UTF_16LE;
        goto L_0x005a;
    L_0x0042:
        if (r4 != r3) goto L_0x004b;
    L_0x0044:
        if (r5 != 0) goto L_0x004b;
    L_0x0046:
        if (r6 != 0) goto L_0x004b;
    L_0x0048:
        r1 = org.mozilla.universalchardet.Constants.CHARSET_X_ISO_10646_UCS_4_3412;
        goto L_0x005a;
    L_0x004b:
        if (r4 != r3) goto L_0x006f;
    L_0x004d:
        r1 = org.mozilla.universalchardet.Constants.CHARSET_UTF_16BE;
        goto L_0x005a;
    L_0x0050:
        r1 = 187; // 0xbb float:2.62E-43 double:9.24E-322;
        if (r4 != r1) goto L_0x006f;
    L_0x0054:
        r1 = 191; // 0xbf float:2.68E-43 double:9.44E-322;
        if (r5 != r1) goto L_0x006f;
    L_0x0058:
        r1 = org.mozilla.universalchardet.Constants.CHARSET_UTF_8;
    L_0x005a:
        r9.detectedCharset = r1;
        goto L_0x006f;
    L_0x005d:
        if (r4 != 0) goto L_0x0066;
    L_0x005f:
        if (r5 != r7) goto L_0x0066;
    L_0x0061:
        if (r6 != r3) goto L_0x0066;
    L_0x0063:
        r1 = org.mozilla.universalchardet.Constants.CHARSET_UTF_32BE;
        goto L_0x005a;
    L_0x0066:
        if (r4 != 0) goto L_0x006f;
    L_0x0068:
        if (r5 != r3) goto L_0x006f;
    L_0x006a:
        if (r6 != r7) goto L_0x006f;
    L_0x006c:
        r1 = org.mozilla.universalchardet.Constants.CHARSET_X_ISO_10646_UCS_4_2143;
        goto L_0x005a;
    L_0x006f:
        r1 = r9.detectedCharset;
        if (r1 == 0) goto L_0x0076;
    L_0x0073:
        r9.done = r0;
        return;
    L_0x0076:
        r1 = r11 + r12;
        r4 = r11;
    L_0x0079:
        if (r4 >= r1) goto L_0x00e5;
    L_0x007b:
        r5 = r10[r4];
        r5 = r5 & r3;
        r6 = r5 & 128;
        if (r6 == 0) goto L_0x00c6;
    L_0x0082:
        r6 = 160; // 0xa0 float:2.24E-43 double:7.9E-322;
        if (r5 == r6) goto L_0x00c6;
    L_0x0086:
        r5 = r9.inputState;
        r6 = org.mozilla.universalchardet.UniversalDetector.InputState.HIGHBYTE;
        if (r5 == r6) goto L_0x00e2;
    L_0x008c:
        r5 = org.mozilla.universalchardet.UniversalDetector.InputState.HIGHBYTE;
        r9.inputState = r5;
        r5 = r9.escCharsetProber;
        if (r5 == 0) goto L_0x0097;
    L_0x0094:
        r5 = 0;
        r9.escCharsetProber = r5;
    L_0x0097:
        r5 = r9.probers;
        r5 = r5[r2];
        if (r5 != 0) goto L_0x00a6;
    L_0x009d:
        r5 = r9.probers;
        r6 = new org.mozilla.universalchardet.prober.MBCSGroupProber;
        r6.<init>();
        r5[r2] = r6;
    L_0x00a6:
        r5 = r9.probers;
        r5 = r5[r0];
        if (r5 != 0) goto L_0x00b5;
    L_0x00ac:
        r5 = r9.probers;
        r6 = new org.mozilla.universalchardet.prober.SBCSGroupProber;
        r6.<init>();
        r5[r0] = r6;
    L_0x00b5:
        r5 = r9.probers;
        r6 = 2;
        r5 = r5[r6];
        if (r5 != 0) goto L_0x00e2;
    L_0x00bc:
        r5 = r9.probers;
        r7 = new org.mozilla.universalchardet.prober.Latin1Prober;
        r7.<init>();
        r5[r6] = r7;
        goto L_0x00e2;
    L_0x00c6:
        r6 = r9.inputState;
        r7 = org.mozilla.universalchardet.UniversalDetector.InputState.PURE_ASCII;
        if (r6 != r7) goto L_0x00de;
    L_0x00cc:
        r6 = 27;
        if (r5 == r6) goto L_0x00da;
    L_0x00d0:
        r6 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        if (r5 != r6) goto L_0x00de;
    L_0x00d4:
        r5 = r9.lastChar;
        r6 = 126; // 0x7e float:1.77E-43 double:6.23E-322;
        if (r5 != r6) goto L_0x00de;
    L_0x00da:
        r5 = org.mozilla.universalchardet.UniversalDetector.InputState.ESC_ASCII;
        r9.inputState = r5;
    L_0x00de:
        r5 = r10[r4];
        r9.lastChar = r5;
    L_0x00e2:
        r4 = r4 + 1;
        goto L_0x0079;
    L_0x00e5:
        r1 = r9.inputState;
        r3 = org.mozilla.universalchardet.UniversalDetector.InputState.ESC_ASCII;
        if (r1 != r3) goto L_0x010b;
    L_0x00eb:
        r1 = r9.escCharsetProber;
        if (r1 != 0) goto L_0x00f6;
    L_0x00ef:
        r1 = new org.mozilla.universalchardet.prober.EscCharsetProber;
        r1.<init>();
        r9.escCharsetProber = r1;
    L_0x00f6:
        r1 = r9.escCharsetProber;
        r10 = r1.handleData(r10, r11, r12);
        r11 = org.mozilla.universalchardet.prober.CharsetProber.ProbingState.FOUND_IT;
        if (r10 != r11) goto L_0x0132;
    L_0x0100:
        r9.done = r0;
        r10 = r9.escCharsetProber;
        r10 = r10.getCharSetName();
        r9.detectedCharset = r10;
        return;
    L_0x010b:
        r1 = r9.inputState;
        r3 = org.mozilla.universalchardet.UniversalDetector.InputState.HIGHBYTE;
        if (r1 != r3) goto L_0x0132;
    L_0x0111:
        r1 = r9.probers;
        r1 = r1.length;
        if (r2 >= r1) goto L_0x0132;
    L_0x0116:
        r1 = r9.probers;
        r1 = r1[r2];
        r1 = r1.handleData(r10, r11, r12);
        r3 = org.mozilla.universalchardet.prober.CharsetProber.ProbingState.FOUND_IT;
        if (r1 != r3) goto L_0x012f;
    L_0x0122:
        r9.done = r0;
        r10 = r9.probers;
        r10 = r10[r2];
        r10 = r10.getCharSetName();
        r9.detectedCharset = r10;
        return;
    L_0x012f:
        r2 = r2 + 1;
        goto L_0x0111;
    L_0x0132:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.universalchardet.UniversalDetector.handleData(byte[], int, int):void");
    }

    public boolean isDone() {
        return this.done;
    }

    public void reset() {
        int i = 0;
        this.done = false;
        this.start = true;
        this.detectedCharset = null;
        this.gotData = false;
        this.inputState = InputState.PURE_ASCII;
        this.lastChar = (byte) 0;
        if (this.escCharsetProber != null) {
            this.escCharsetProber.reset();
        }
        while (i < this.probers.length) {
            if (this.probers[i] != null) {
                this.probers[i].reset();
            }
            i++;
        }
    }

    public void setListener(CharsetListener charsetListener) {
        this.listener = charsetListener;
    }
}
