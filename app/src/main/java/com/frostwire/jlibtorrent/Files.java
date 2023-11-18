package com.frostwire.jlibtorrent;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

final class Files {
    private static final int EOF = -1;

    private Files() {
    }

    public static byte[] bytes(File file) throws IOException {
        InputStream openInputStream;
        try {
            openInputStream = openInputStream(file);
            try {
                file = toByteArray(openInputStream, file.length());
                closeQuietly(openInputStream);
                return file;
            } catch (Throwable th) {
                file = th;
                closeQuietly(openInputStream);
                throw file;
            }
        } catch (Throwable th2) {
            file = th2;
            openInputStream = null;
            closeQuietly(openInputStream);
            throw file;
        }
    }

    private static FileInputStream openInputStream(File file) throws IOException {
        StringBuilder stringBuilder;
        if (!file.exists()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("File '");
            stringBuilder.append(file);
            stringBuilder.append("' does not exist");
            throw new FileNotFoundException(stringBuilder.toString());
        } else if (file.isDirectory()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("File '");
            stringBuilder.append(file);
            stringBuilder.append("' exists but is a directory");
            throw new IOException(stringBuilder.toString());
        } else if (file.canRead()) {
            return new FileInputStream(file);
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("File '");
            stringBuilder.append(file);
            stringBuilder.append("' cannot be read");
            throw new IOException(stringBuilder.toString());
        }
    }

    private static byte[] toByteArray(InputStream inputStream, long j) throws IOException {
        if (j <= 2147483647L) {
            return toByteArray(inputStream, (int) j);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Size cannot be greater than Integer max value: ");
        stringBuilder.append(j);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private static byte[] toByteArray(InputStream inputStream, int i) throws IOException {
        if (i < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Size must be equal or greater than zero: ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        int i2 = 0;
        if (i == 0) {
            return new byte[0];
        }
        byte[] bArr = new byte[i];
        while (i2 < i) {
            int read = inputStream.read(bArr, i2, i - i2);
            if (read == -1) {
                break;
            }
            i2 += read;
        }
        if (i2 == i) {
            return bArr;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Unexpected readed size. current: ");
        stringBuilder2.append(i2);
        stringBuilder2.append(", excepted: ");
        stringBuilder2.append(i);
        throw new IOException(stringBuilder2.toString());
    }

    static void closeQuietly(InputStream inputStream) {
        closeQuietly((Closeable) inputStream);
    }

    static void closeQuietly(java.io.Closeable r0) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        if (r0 == 0) goto L_0x0005;
    L_0x0002:
        r0.close();	 Catch:{ IOException -> 0x0005 }
    L_0x0005:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.frostwire.jlibtorrent.Files.closeQuietly(java.io.Closeable):void");
    }
}
