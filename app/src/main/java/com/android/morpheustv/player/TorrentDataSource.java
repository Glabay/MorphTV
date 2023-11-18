package com.android.morpheustv.player;

import android.net.Uri;
import com.github.se_bastiaan.torrentstream.Torrent;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import net.lingala.zip4j.util.InternalZipConstants;

public class TorrentDataSource implements DataSource {
    private long bytesRemaining;
    private RandomAccessFile file;
    private final TransferListener<? super TorrentDataSource> listener;
    private boolean opened;
    private Torrent torrent;
    private Uri uri;

    public static class TorrentDataSourceException extends IOException {
        public TorrentDataSourceException(IOException iOException) {
            super(iOException);
        }
    }

    public TorrentDataSource(Torrent torrent) {
        this(torrent, 0, null);
    }

    public TorrentDataSource(Torrent torrent, long j, TransferListener<? super TorrentDataSource> transferListener) {
        this.listener = transferListener;
        this.torrent = torrent;
        torrent.setInterestedBytes(j);
    }

    private void waitForInterestedBytes(int r7) {
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
        if (r7 <= 0) goto L_0x0020;
    L_0x0002:
        r0 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0020 }
        r2 = (long) r7;	 Catch:{ Exception -> 0x0020 }
        r4 = r0 + r2;	 Catch:{ Exception -> 0x0020 }
    L_0x0009:
        r7 = r6.torrent;	 Catch:{ Exception -> 0x0020 }
        r0 = 0;	 Catch:{ Exception -> 0x0020 }
        r7 = r7.hasInterestedBytes(r0);	 Catch:{ Exception -> 0x0020 }
        if (r7 != 0) goto L_0x0020;	 Catch:{ Exception -> 0x0020 }
    L_0x0012:
        r0 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0020 }
        r7 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));	 Catch:{ Exception -> 0x0020 }
        if (r7 >= 0) goto L_0x0020;	 Catch:{ Exception -> 0x0020 }
    L_0x001a:
        r0 = 100;	 Catch:{ Exception -> 0x0020 }
        java.lang.Thread.sleep(r0);	 Catch:{ Exception -> 0x0020 }
        goto L_0x0009;
    L_0x0020:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.morpheustv.player.TorrentDataSource.waitForInterestedBytes(int):void");
    }

    public long open(DataSpec dataSpec) throws TorrentDataSourceException {
        try {
            this.uri = dataSpec.uri;
            long length = this.torrent.getVideoFile().length();
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("OpenDataSource at position ");
            stringBuilder.append(String.valueOf(dataSpec.position));
            stringBuilder.append(" of ");
            stringBuilder.append(String.valueOf(length));
            printStream.println(stringBuilder.toString());
            this.torrent.setInterestedBytes(dataSpec.position);
            waitForInterestedBytes(10000);
            this.file = new RandomAccessFile(this.torrent.getVideoFile(), InternalZipConstants.READ_MODE);
            this.file.seek(dataSpec.position);
            this.bytesRemaining = dataSpec.length == -1 ? length - dataSpec.position : dataSpec.length;
            if (this.bytesRemaining < 0) {
                throw new EOFException();
            }
            this.opened = true;
            if (this.listener != null) {
                this.listener.onTransferStart(this, dataSpec);
            }
            return this.bytesRemaining;
        } catch (DataSpec dataSpec2) {
            throw new TorrentDataSourceException(dataSpec2);
        }
    }

    public int read(byte[] bArr, int i, int i2) throws TorrentDataSourceException {
        if (i2 == 0) {
            return null;
        }
        if (this.bytesRemaining == 0) {
            return -1;
        }
        try {
            bArr = this.file.read(bArr, i, (int) Math.min(this.bytesRemaining, (long) i2));
            if (bArr > null) {
                this.bytesRemaining -= (long) bArr;
                if (this.listener != 0) {
                    this.listener.onBytesTransferred(this, bArr);
                }
            }
            return bArr;
        } catch (byte[] bArr2) {
            throw new TorrentDataSourceException(bArr2);
        }
    }

    public Uri getUri() {
        return this.uri;
    }

    public void close() throws TorrentDataSourceException {
        this.uri = null;
        try {
            if (this.file != null) {
                this.file.close();
            }
            this.file = null;
            if (this.opened) {
                this.opened = false;
                if (this.listener != null) {
                    this.listener.onTransferEnd(this);
                }
            }
        } catch (IOException e) {
            throw new TorrentDataSourceException(e);
        } catch (Throwable th) {
            this.file = null;
            if (this.opened) {
                this.opened = false;
                if (this.listener != null) {
                    this.listener.onTransferEnd(this);
                }
            }
        }
    }
}
