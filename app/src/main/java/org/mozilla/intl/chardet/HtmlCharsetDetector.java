package org.mozilla.intl.chardet;

import java.io.BufferedInputStream;
import java.io.PrintStream;
import java.net.URL;

public class HtmlCharsetDetector {
    public static boolean found;

    /* renamed from: org.mozilla.intl.chardet.HtmlCharsetDetector$1 */
    static class C15071 implements nsICharsetDetectionObserver {
        C15071() {
        }

        public void Notify(String str) {
            HtmlCharsetDetector.found = true;
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CHARSET = ");
            stringBuilder.append(str);
            printStream.println(stringBuilder.toString());
        }
    }

    public static void main(String[] strArr) throws Exception {
        if (strArr.length == 1 || strArr.length == 2) {
            int i = 0;
            nsDetector nsdetector = new nsDetector(strArr.length == 2 ? Integer.parseInt(strArr[1]) : 0);
            nsdetector.Init(new C15071());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL(strArr[0]).openStream());
            byte[] bArr = new byte[1024];
            boolean z = true;
            boolean z2 = false;
            while (true) {
                int read = bufferedInputStream.read(bArr, 0, bArr.length);
                if (read == -1) {
                    break;
                }
                if (z) {
                    z = nsdetector.isAscii(bArr, read);
                }
                if (!(z || r5)) {
                    z2 = nsdetector.DoIt(bArr, read, false);
                }
            }
            nsdetector.DataEnd();
            if (z) {
                System.out.println("CHARSET = ASCII");
                found = true;
            }
            if (!found) {
                strArr = nsdetector.getProbableCharsets();
                while (i < strArr.length) {
                    PrintStream printStream = System.out;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Probable Charset = ");
                    stringBuilder.append(strArr[i]);
                    printStream.println(stringBuilder.toString());
                    i++;
                }
            }
            return;
        }
        System.out.println("Usage: HtmlCharsetDetector <url> [<languageHint>]");
        System.out.println("");
        System.out.println("Where <url> is http://...");
        System.out.println("For optional <languageHint>. Use following...");
        System.out.println("\t\t1 => Japanese");
        System.out.println("\t\t2 => Chinese");
        System.out.println("\t\t3 => Simplified Chinese");
        System.out.println("\t\t4 => Traditional Chinese");
        System.out.println("\t\t5 => Korean");
        System.out.println("\t\t6 => Dont know (default)");
    }
}
