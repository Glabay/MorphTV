package net.lingala.zip4j.util;

import net.lingala.zip4j.exception.ZipException;

public class CRCUtil {
    private static final int BUF_SIZE = 16384;

    public static long computeFileCRC(String str) throws ZipException {
        return computeFileCRC(str, null);
    }

    public static long computeFileCRC(java.lang.String r6, net.lingala.zip4j.progress.ProgressMonitor r7) throws net.lingala.zip4j.exception.ZipException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = net.lingala.zip4j.util.Zip4jUtil.isStringNotNullAndNotEmpty(r6);
        if (r0 != 0) goto L_0x000e;
    L_0x0006:
        r6 = new net.lingala.zip4j.exception.ZipException;
        r7 = "input file is null or empty, cannot calculate CRC for the file";
        r6.<init>(r7);
        throw r6;
    L_0x000e:
        r0 = 0;
        net.lingala.zip4j.util.Zip4jUtil.checkFileReadAccess(r6);	 Catch:{ IOException -> 0x0079, Exception -> 0x0072 }
        r1 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x0079, Exception -> 0x0072 }
        r2 = new java.io.File;	 Catch:{ IOException -> 0x0079, Exception -> 0x0072 }
        r2.<init>(r6);	 Catch:{ IOException -> 0x0079, Exception -> 0x0072 }
        r1.<init>(r2);	 Catch:{ IOException -> 0x0079, Exception -> 0x0072 }
        r6 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        r6 = new byte[r6];	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
        r0 = new java.util.zip.CRC32;	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
        r0.<init>();	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
    L_0x0025:
        r2 = r1.read(r6);	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
        r3 = -1;	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
        if (r2 != r3) goto L_0x003f;	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
    L_0x002c:
        r6 = r0.getValue();	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
        if (r1 == 0) goto L_0x003e;
    L_0x0032:
        r1.close();	 Catch:{ IOException -> 0x0036 }
        goto L_0x003e;
    L_0x0036:
        r6 = new net.lingala.zip4j.exception.ZipException;
        r7 = "error while closing the file after calculating crc";
        r6.<init>(r7);
        throw r6;
    L_0x003e:
        return r6;
    L_0x003f:
        r3 = 0;
        r0.update(r6, r3, r2);	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
        if (r7 == 0) goto L_0x0025;	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
    L_0x0045:
        r4 = (long) r2;	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
        r7.updateWorkCompleted(r4);	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
        r2 = r7.isCancelAllTasks();	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
        if (r2 == 0) goto L_0x0025;	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
    L_0x004f:
        r6 = 3;	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
        r7.setResult(r6);	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
        r7.setState(r3);	 Catch:{ IOException -> 0x006d, Exception -> 0x006a, all -> 0x0067 }
        if (r1 == 0) goto L_0x0064;
    L_0x0058:
        r1.close();	 Catch:{ IOException -> 0x005c }
        goto L_0x0064;
    L_0x005c:
        r6 = new net.lingala.zip4j.exception.ZipException;
        r7 = "error while closing the file after calculating crc";
        r6.<init>(r7);
        throw r6;
    L_0x0064:
        r6 = 0;
        return r6;
    L_0x0067:
        r6 = move-exception;
        r0 = r1;
        goto L_0x0080;
    L_0x006a:
        r6 = move-exception;
        r0 = r1;
        goto L_0x0073;
    L_0x006d:
        r6 = move-exception;
        r0 = r1;
        goto L_0x007a;
    L_0x0070:
        r6 = move-exception;
        goto L_0x0080;
    L_0x0072:
        r6 = move-exception;
    L_0x0073:
        r7 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ all -> 0x0070 }
        r7.<init>(r6);	 Catch:{ all -> 0x0070 }
        throw r7;	 Catch:{ all -> 0x0070 }
    L_0x0079:
        r6 = move-exception;	 Catch:{ all -> 0x0070 }
    L_0x007a:
        r7 = new net.lingala.zip4j.exception.ZipException;	 Catch:{ all -> 0x0070 }
        r7.<init>(r6);	 Catch:{ all -> 0x0070 }
        throw r7;	 Catch:{ all -> 0x0070 }
    L_0x0080:
        if (r0 == 0) goto L_0x008e;
    L_0x0082:
        r0.close();	 Catch:{ IOException -> 0x0086 }
        goto L_0x008e;
    L_0x0086:
        r6 = new net.lingala.zip4j.exception.ZipException;
        r7 = "error while closing the file after calculating crc";
        r6.<init>(r7);
        throw r6;
    L_0x008e:
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.lingala.zip4j.util.CRCUtil.computeFileCRC(java.lang.String, net.lingala.zip4j.progress.ProgressMonitor):long");
    }
}
