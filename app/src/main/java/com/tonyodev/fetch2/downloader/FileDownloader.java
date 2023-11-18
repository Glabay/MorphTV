package com.tonyodev.fetch2.downloader;

import com.tonyodev.fetch2.Download;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\bf\u0018\u00002\u00020\u0001:\u0001\u0018R\u0018\u0010\u0002\u001a\u00020\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u0004\u0018\u00010\tX¦\u000e¢\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0012\u0010\u000e\u001a\u00020\u000fX¦\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0018\u0010\u0012\u001a\u00020\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0013\u0010\u0005\"\u0004\b\u0014\u0010\u0007R\u0018\u0010\u0015\u001a\u00020\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0016\u0010\u0005\"\u0004\b\u0017\u0010\u0007¨\u0006\u0019"}, d2 = {"Lcom/tonyodev/fetch2/downloader/FileDownloader;", "Ljava/lang/Runnable;", "completedDownload", "", "getCompletedDownload", "()Z", "setCompletedDownload", "(Z)V", "delegate", "Lcom/tonyodev/fetch2/downloader/FileDownloader$Delegate;", "getDelegate", "()Lcom/tonyodev/fetch2/downloader/FileDownloader$Delegate;", "setDelegate", "(Lcom/tonyodev/fetch2/downloader/FileDownloader$Delegate;)V", "download", "Lcom/tonyodev/fetch2/Download;", "getDownload", "()Lcom/tonyodev/fetch2/Download;", "interrupted", "getInterrupted", "setInterrupted", "terminated", "getTerminated", "setTerminated", "Delegate", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: FileDownloader.kt */
public interface FileDownloader extends Runnable {

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J \u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH&J \u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH&J\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u000e"}, d2 = {"Lcom/tonyodev/fetch2/downloader/FileDownloader$Delegate;", "", "onComplete", "", "download", "Lcom/tonyodev/fetch2/Download;", "onError", "onProgress", "etaInMilliSeconds", "", "downloadedBytesPerSecond", "onStarted", "etaInMilliseconds", "saveDownloadProgress", "fetch2_release"}, k = 1, mv = {1, 1, 10})
    /* compiled from: FileDownloader.kt */
    public interface Delegate {
        void onComplete(@NotNull Download download);

        void onError(@NotNull Download download);

        void onProgress(@NotNull Download download, long j, long j2);

        void onStarted(@NotNull Download download, long j, long j2);

        void saveDownloadProgress(@NotNull Download download);
    }

    boolean getCompletedDownload();

    @Nullable
    Delegate getDelegate();

    @NotNull
    Download getDownload();

    boolean getInterrupted();

    boolean getTerminated();

    void setCompletedDownload(boolean z);

    void setDelegate(@Nullable Delegate delegate);

    void setInterrupted(boolean z);

    void setTerminated(boolean z);
}
