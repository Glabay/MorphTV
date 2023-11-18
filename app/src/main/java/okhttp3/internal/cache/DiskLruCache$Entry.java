package okhttp3.internal.cache;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import okio.BufferedSink;

final class DiskLruCache$Entry {
    final File[] cleanFiles;
    DiskLruCache$Editor currentEditor;
    final File[] dirtyFiles;
    final String key;
    final long[] lengths;
    boolean readable;
    long sequenceNumber;
    final /* synthetic */ DiskLruCache this$0;

    DiskLruCache$Entry(DiskLruCache diskLruCache, String str) {
        this.this$0 = diskLruCache;
        this.key = str;
        this.lengths = new long[diskLruCache.valueCount];
        this.cleanFiles = new File[diskLruCache.valueCount];
        this.dirtyFiles = new File[diskLruCache.valueCount];
        StringBuilder stringBuilder = new StringBuilder(str);
        stringBuilder.append('.');
        str = stringBuilder.length();
        for (int i = 0; i < diskLruCache.valueCount; i++) {
            stringBuilder.append(i);
            this.cleanFiles[i] = new File(diskLruCache.directory, stringBuilder.toString());
            stringBuilder.append(".tmp");
            this.dirtyFiles[i] = new File(diskLruCache.directory, stringBuilder.toString());
            stringBuilder.setLength(str);
        }
    }

    void setLengths(java.lang.String[] r5) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r4 = this;
        r0 = r5.length;
        r1 = r4.this$0;
        r1 = r1.valueCount;
        if (r0 == r1) goto L_0x000c;
    L_0x0007:
        r5 = r4.invalidLengths(r5);
        throw r5;
    L_0x000c:
        r0 = 0;
    L_0x000d:
        r1 = r5.length;	 Catch:{ NumberFormatException -> 0x001e }
        if (r0 >= r1) goto L_0x001d;	 Catch:{ NumberFormatException -> 0x001e }
    L_0x0010:
        r1 = r4.lengths;	 Catch:{ NumberFormatException -> 0x001e }
        r2 = r5[r0];	 Catch:{ NumberFormatException -> 0x001e }
        r2 = java.lang.Long.parseLong(r2);	 Catch:{ NumberFormatException -> 0x001e }
        r1[r0] = r2;	 Catch:{ NumberFormatException -> 0x001e }
        r0 = r0 + 1;
        goto L_0x000d;
    L_0x001d:
        return;
    L_0x001e:
        r5 = r4.invalidLengths(r5);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache$Entry.setLengths(java.lang.String[]):void");
    }

    void writeLengths(BufferedSink bufferedSink) throws IOException {
        for (long writeDecimalLong : this.lengths) {
            bufferedSink.writeByte(32).writeDecimalLong(writeDecimalLong);
        }
    }

    private IOException invalidLengths(String[] strArr) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unexpected journal line: ");
        stringBuilder.append(Arrays.toString(strArr));
        throw new IOException(stringBuilder.toString());
    }

    okhttp3.internal.cache.DiskLruCache.Snapshot snapshot() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r10 = this;
        r0 = r10.this$0;
        r0 = java.lang.Thread.holdsLock(r0);
        if (r0 != 0) goto L_0x000e;
    L_0x0008:
        r0 = new java.lang.AssertionError;
        r0.<init>();
        throw r0;
    L_0x000e:
        r0 = r10.this$0;
        r0 = r0.valueCount;
        r0 = new okio.Source[r0];
        r1 = r10.lengths;
        r1 = r1.clone();
        r7 = r1;
        r7 = (long[]) r7;
        r8 = 0;
        r1 = 0;
    L_0x001f:
        r2 = r10.this$0;	 Catch:{ FileNotFoundException -> 0x0044 }
        r2 = r2.valueCount;	 Catch:{ FileNotFoundException -> 0x0044 }
        if (r1 >= r2) goto L_0x0036;	 Catch:{ FileNotFoundException -> 0x0044 }
    L_0x0025:
        r2 = r10.this$0;	 Catch:{ FileNotFoundException -> 0x0044 }
        r2 = r2.fileSystem;	 Catch:{ FileNotFoundException -> 0x0044 }
        r3 = r10.cleanFiles;	 Catch:{ FileNotFoundException -> 0x0044 }
        r3 = r3[r1];	 Catch:{ FileNotFoundException -> 0x0044 }
        r2 = r2.source(r3);	 Catch:{ FileNotFoundException -> 0x0044 }
        r0[r1] = r2;	 Catch:{ FileNotFoundException -> 0x0044 }
        r1 = r1 + 1;	 Catch:{ FileNotFoundException -> 0x0044 }
        goto L_0x001f;	 Catch:{ FileNotFoundException -> 0x0044 }
    L_0x0036:
        r9 = new okhttp3.internal.cache.DiskLruCache$Snapshot;	 Catch:{ FileNotFoundException -> 0x0044 }
        r2 = r10.this$0;	 Catch:{ FileNotFoundException -> 0x0044 }
        r3 = r10.key;	 Catch:{ FileNotFoundException -> 0x0044 }
        r4 = r10.sequenceNumber;	 Catch:{ FileNotFoundException -> 0x0044 }
        r1 = r9;	 Catch:{ FileNotFoundException -> 0x0044 }
        r6 = r0;	 Catch:{ FileNotFoundException -> 0x0044 }
        r1.<init>(r2, r3, r4, r6, r7);	 Catch:{ FileNotFoundException -> 0x0044 }
        return r9;
    L_0x0044:
        r1 = r10.this$0;
        r1 = r1.valueCount;
        if (r8 >= r1) goto L_0x0056;
    L_0x004a:
        r1 = r0[r8];
        if (r1 == 0) goto L_0x0056;
    L_0x004e:
        r1 = r0[r8];
        okhttp3.internal.Util.closeQuietly(r1);
        r8 = r8 + 1;
        goto L_0x0044;
    L_0x0056:
        r0 = r10.this$0;	 Catch:{ IOException -> 0x005b }
        r0.removeEntry(r10);	 Catch:{ IOException -> 0x005b }
    L_0x005b:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache$Entry.snapshot():okhttp3.internal.cache.DiskLruCache$Snapshot");
    }
}
