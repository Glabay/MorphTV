package ir.mahdi.mzip.rar.unpack.ppm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class AnalyzeHeapDump {
    public static void main(String[] strArr) {
        InputStream bufferedInputStream;
        IOException e;
        Throwable th;
        strArr = new File("P:\\test\\heapdumpc");
        File file = new File("P:\\test\\heapdumpj");
        StringBuilder stringBuilder;
        if (!strArr.exists()) {
            PrintStream printStream = System.err;
            stringBuilder = new StringBuilder();
            stringBuilder.append("File not found: ");
            stringBuilder.append(strArr.getAbsolutePath());
            printStream.println(stringBuilder.toString());
        } else if (file.exists()) {
            long length = strArr.length();
            long length2 = file.length();
            if (length != length2) {
                System.out.println("File size mismatch");
                PrintStream printStream2 = System.out;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("clen = ");
                stringBuilder2.append(length);
                printStream2.println(stringBuilder2.toString());
                printStream2 = System.out;
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("jlen = ");
                stringBuilder2.append(length2);
                printStream2.println(stringBuilder2.toString());
            }
            length = Math.min(length, length2);
            InputStream inputStream = null;
            try {
                bufferedInputStream = new BufferedInputStream(new FileInputStream(strArr), 262144);
                try {
                    strArr = new BufferedInputStream(new FileInputStream(file), 262144);
                    length2 = 0;
                    long j = 0;
                    Object obj = 1;
                    Object obj2 = null;
                    while (length2 < length) {
                        try {
                            if (bufferedInputStream.read() != strArr.read()) {
                                if (obj != null) {
                                    j = length2;
                                    obj = null;
                                    obj2 = 1;
                                }
                            } else if (obj == null) {
                                printMismatch(j, length2);
                                obj = 1;
                            }
                            length2++;
                        } catch (IOException e2) {
                            e = e2;
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    }
                    if (obj == null) {
                        printMismatch(j, length2);
                    }
                    if (obj2 == null) {
                        System.out.println("Files are identical");
                    }
                    System.out.println("Done");
                    try {
                        bufferedInputStream.close();
                        strArr.close();
                    } catch (String[] strArr2) {
                        strArr2.printStackTrace();
                    }
                } catch (IOException e3) {
                    e = e3;
                    strArr2 = null;
                    inputStream = bufferedInputStream;
                    try {
                        e.printStackTrace();
                        inputStream.close();
                        strArr2.close();
                    } catch (Throwable th3) {
                        th = th3;
                        bufferedInputStream = inputStream;
                        try {
                            bufferedInputStream.close();
                            strArr2.close();
                        } catch (String[] strArr22) {
                            strArr22.printStackTrace();
                        }
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    strArr22 = null;
                    bufferedInputStream.close();
                    strArr22.close();
                    throw th;
                }
            } catch (IOException e4) {
                e = e4;
                strArr22 = null;
                e.printStackTrace();
                inputStream.close();
                strArr22.close();
            } catch (Throwable th5) {
                th = th5;
                strArr22 = null;
                bufferedInputStream = strArr22;
                bufferedInputStream.close();
                strArr22.close();
                throw th;
            }
        } else {
            strArr22 = System.err;
            stringBuilder = new StringBuilder();
            stringBuilder.append("File not found: ");
            stringBuilder.append(file.getAbsolutePath());
            strArr22.println(stringBuilder.toString());
        }
    }

    private static void printMismatch(long j, long j2) {
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Mismatch: off=");
        stringBuilder.append(j);
        stringBuilder.append("(0x");
        stringBuilder.append(Long.toHexString(j));
        stringBuilder.append("), len=");
        stringBuilder.append(j2 - j);
        printStream.println(stringBuilder.toString());
    }
}
