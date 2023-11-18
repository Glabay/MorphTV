package com.tonyodev.fetch2.helper;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.database.DownloadInfo;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\b\b&\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001e\u0010\u000f\u001a\u00020\n8\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\f\"\u0004\b\u0011\u0010\u000e¨\u0006\u0012"}, d2 = {"Lcom/tonyodev/fetch2/helper/DownloadReportingRunnable;", "Ljava/lang/Runnable;", "()V", "download", "Lcom/tonyodev/fetch2/Download;", "getDownload", "()Lcom/tonyodev/fetch2/Download;", "setDownload", "(Lcom/tonyodev/fetch2/Download;)V", "downloadedBytesPerSecond", "", "getDownloadedBytesPerSecond", "()J", "setDownloadedBytesPerSecond", "(J)V", "etaInMilliSeconds", "getEtaInMilliSeconds", "setEtaInMilliSeconds", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: DownloadReportingRunnable.kt */
public abstract class DownloadReportingRunnable implements Runnable {
    @NotNull
    private volatile Download download = new DownloadInfo();
    private volatile long downloadedBytesPerSecond;
    private volatile long etaInMilliSeconds;

    @NotNull
    public final Download getDownload() {
        return this.download;
    }

    public final void setDownload(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "<set-?>");
        this.download = download;
    }

    public final long getEtaInMilliSeconds() {
        return this.etaInMilliSeconds;
    }

    public final void setEtaInMilliSeconds(long j) {
        this.etaInMilliSeconds = j;
    }

    public final long getDownloadedBytesPerSecond() {
        return this.downloadedBytesPerSecond;
    }

    public final void setDownloadedBytesPerSecond(long j) {
        this.downloadedBytesPerSecond = j;
    }
}
