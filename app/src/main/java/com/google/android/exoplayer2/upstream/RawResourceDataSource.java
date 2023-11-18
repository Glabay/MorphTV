package com.google.android.exoplayer2.upstream;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.net.Uri;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public final class RawResourceDataSource implements DataSource {
    public static final String RAW_RESOURCE_SCHEME = "rawresource";
    private AssetFileDescriptor assetFileDescriptor;
    private long bytesRemaining;
    private InputStream inputStream;
    private final TransferListener<? super RawResourceDataSource> listener;
    private boolean opened;
    private final Resources resources;
    private Uri uri;

    public static class RawResourceDataSourceException extends IOException {
        public RawResourceDataSourceException(String str) {
            super(str);
        }

        public RawResourceDataSourceException(IOException iOException) {
            super(iOException);
        }
    }

    public static Uri buildRawResourceUri(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("rawresource:///");
        stringBuilder.append(i);
        return Uri.parse(stringBuilder.toString());
    }

    public RawResourceDataSource(Context context) {
        this(context, null);
    }

    public RawResourceDataSource(Context context, TransferListener<? super RawResourceDataSource> transferListener) {
        this.resources = context.getResources();
        this.listener = transferListener;
    }

    public long open(com.google.android.exoplayer2.upstream.DataSpec r7) throws com.google.android.exoplayer2.upstream.RawResourceDataSource.RawResourceDataSourceException {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r6 = this;
        r0 = r7.uri;	 Catch:{ IOException -> 0x008f }
        r6.uri = r0;	 Catch:{ IOException -> 0x008f }
        r0 = "rawresource";	 Catch:{ IOException -> 0x008f }
        r1 = r6.uri;	 Catch:{ IOException -> 0x008f }
        r1 = r1.getScheme();	 Catch:{ IOException -> 0x008f }
        r0 = android.text.TextUtils.equals(r0, r1);	 Catch:{ IOException -> 0x008f }
        if (r0 != 0) goto L_0x001a;	 Catch:{ IOException -> 0x008f }
    L_0x0012:
        r7 = new com.google.android.exoplayer2.upstream.RawResourceDataSource$RawResourceDataSourceException;	 Catch:{ IOException -> 0x008f }
        r0 = "URI must use scheme rawresource";	 Catch:{ IOException -> 0x008f }
        r7.<init>(r0);	 Catch:{ IOException -> 0x008f }
        throw r7;	 Catch:{ IOException -> 0x008f }
    L_0x001a:
        r0 = r6.uri;	 Catch:{ NumberFormatException -> 0x0087 }
        r0 = r0.getLastPathSegment();	 Catch:{ NumberFormatException -> 0x0087 }
        r0 = java.lang.Integer.parseInt(r0);	 Catch:{ NumberFormatException -> 0x0087 }
        r1 = r6.resources;	 Catch:{ IOException -> 0x008f }
        r0 = r1.openRawResourceFd(r0);	 Catch:{ IOException -> 0x008f }
        r6.assetFileDescriptor = r0;	 Catch:{ IOException -> 0x008f }
        r0 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x008f }
        r1 = r6.assetFileDescriptor;	 Catch:{ IOException -> 0x008f }
        r1 = r1.getFileDescriptor();	 Catch:{ IOException -> 0x008f }
        r0.<init>(r1);	 Catch:{ IOException -> 0x008f }
        r6.inputStream = r0;	 Catch:{ IOException -> 0x008f }
        r0 = r6.inputStream;	 Catch:{ IOException -> 0x008f }
        r1 = r6.assetFileDescriptor;	 Catch:{ IOException -> 0x008f }
        r1 = r1.getStartOffset();	 Catch:{ IOException -> 0x008f }
        r0.skip(r1);	 Catch:{ IOException -> 0x008f }
        r0 = r6.inputStream;	 Catch:{ IOException -> 0x008f }
        r1 = r7.position;	 Catch:{ IOException -> 0x008f }
        r0 = r0.skip(r1);	 Catch:{ IOException -> 0x008f }
        r2 = r7.position;	 Catch:{ IOException -> 0x008f }
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));	 Catch:{ IOException -> 0x008f }
        if (r4 >= 0) goto L_0x0058;	 Catch:{ IOException -> 0x008f }
    L_0x0052:
        r7 = new java.io.EOFException;	 Catch:{ IOException -> 0x008f }
        r7.<init>();	 Catch:{ IOException -> 0x008f }
        throw r7;	 Catch:{ IOException -> 0x008f }
    L_0x0058:
        r0 = r7.length;	 Catch:{ IOException -> 0x008f }
        r2 = -1;	 Catch:{ IOException -> 0x008f }
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));	 Catch:{ IOException -> 0x008f }
        if (r4 == 0) goto L_0x0065;	 Catch:{ IOException -> 0x008f }
    L_0x0060:
        r0 = r7.length;	 Catch:{ IOException -> 0x008f }
        r6.bytesRemaining = r0;	 Catch:{ IOException -> 0x008f }
        goto L_0x0078;	 Catch:{ IOException -> 0x008f }
    L_0x0065:
        r0 = r6.assetFileDescriptor;	 Catch:{ IOException -> 0x008f }
        r0 = r0.getLength();	 Catch:{ IOException -> 0x008f }
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));	 Catch:{ IOException -> 0x008f }
        if (r4 != 0) goto L_0x0070;	 Catch:{ IOException -> 0x008f }
    L_0x006f:
        goto L_0x0076;	 Catch:{ IOException -> 0x008f }
    L_0x0070:
        r2 = r7.position;	 Catch:{ IOException -> 0x008f }
        r4 = 0;	 Catch:{ IOException -> 0x008f }
        r4 = r0 - r2;	 Catch:{ IOException -> 0x008f }
        r2 = r4;	 Catch:{ IOException -> 0x008f }
    L_0x0076:
        r6.bytesRemaining = r2;	 Catch:{ IOException -> 0x008f }
    L_0x0078:
        r0 = 1;
        r6.opened = r0;
        r0 = r6.listener;
        if (r0 == 0) goto L_0x0084;
    L_0x007f:
        r0 = r6.listener;
        r0.onTransferStart(r6, r7);
    L_0x0084:
        r0 = r6.bytesRemaining;
        return r0;
    L_0x0087:
        r7 = new com.google.android.exoplayer2.upstream.RawResourceDataSource$RawResourceDataSourceException;	 Catch:{ IOException -> 0x008f }
        r0 = "Resource identifier must be an integer.";	 Catch:{ IOException -> 0x008f }
        r7.<init>(r0);	 Catch:{ IOException -> 0x008f }
        throw r7;	 Catch:{ IOException -> 0x008f }
    L_0x008f:
        r7 = move-exception;
        r0 = new com.google.android.exoplayer2.upstream.RawResourceDataSource$RawResourceDataSourceException;
        r0.<init>(r7);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.upstream.RawResourceDataSource.open(com.google.android.exoplayer2.upstream.DataSpec):long");
    }

    public int read(byte[] bArr, int i, int i2) throws RawResourceDataSourceException {
        if (i2 == 0) {
            return null;
        }
        if (this.bytesRemaining == 0) {
            return -1;
        }
        try {
            if (this.bytesRemaining != -1) {
                i2 = (int) Math.min(this.bytesRemaining, (long) i2);
            }
            bArr = this.inputStream.read(bArr, i, i2);
            if (bArr != -1) {
                if (this.bytesRemaining != -1) {
                    this.bytesRemaining -= (long) bArr;
                }
                if (this.listener != 0) {
                    this.listener.onBytesTransferred(this, bArr);
                }
                return bArr;
            } else if (this.bytesRemaining == -1) {
                return -1;
            } else {
                throw new RawResourceDataSourceException(new EOFException());
            }
        } catch (IOException e) {
            throw new RawResourceDataSourceException(e);
        }
    }

    public Uri getUri() {
        return this.uri;
    }

    public void close() throws RawResourceDataSourceException {
        this.uri = null;
        try {
            if (this.inputStream != null) {
                this.inputStream.close();
            }
            this.inputStream = null;
            try {
                if (this.assetFileDescriptor != null) {
                    this.assetFileDescriptor.close();
                }
                this.assetFileDescriptor = null;
                if (this.opened) {
                    this.opened = false;
                    if (this.listener != null) {
                        this.listener.onTransferEnd(this);
                    }
                }
            } catch (IOException e) {
                throw new RawResourceDataSourceException(e);
            } catch (Throwable th) {
                this.assetFileDescriptor = null;
                if (this.opened) {
                    this.opened = false;
                    if (this.listener != null) {
                        this.listener.onTransferEnd(this);
                    }
                }
            }
        } catch (IOException e2) {
            throw new RawResourceDataSourceException(e2);
        } catch (Throwable th2) {
            this.inputStream = null;
            try {
                if (this.assetFileDescriptor != null) {
                    this.assetFileDescriptor.close();
                }
                this.assetFileDescriptor = null;
                if (this.opened) {
                    this.opened = false;
                    if (this.listener != null) {
                        this.listener.onTransferEnd(this);
                    }
                }
            } catch (IOException e22) {
                throw new RawResourceDataSourceException(e22);
            } catch (Throwable th3) {
                this.assetFileDescriptor = null;
                if (this.opened) {
                    this.opened = false;
                    if (this.listener != null) {
                        this.listener.onTransferEnd(this);
                    }
                }
            }
        }
    }
}
