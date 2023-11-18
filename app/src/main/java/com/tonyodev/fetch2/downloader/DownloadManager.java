package com.tonyodev.fetch2.downloader;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.downloader.FileDownloader.Delegate;
import java.io.Closeable;
import java.util.List;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\b\u0010\u0005\u001a\u00020\u0003H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\u0010\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH&J\b\u0010\f\u001a\u00020\bH&J\u000e\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH&J\b\u0010\u0010\u001a\u00020\u0011H&J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u000fH&J\u0010\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u000fH&J\b\u0010\u0016\u001a\u00020\nH&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0004¨\u0006\u0017"}, d2 = {"Lcom/tonyodev/fetch2/downloader/DownloadManager;", "Ljava/io/Closeable;", "isClosed", "", "()Z", "canAccommodateNewDownload", "cancel", "id", "", "cancelAll", "", "contains", "getActiveDownloadCount", "getDownloads", "", "Lcom/tonyodev/fetch2/Download;", "getFileDownloaderDelegate", "Lcom/tonyodev/fetch2/downloader/FileDownloader$Delegate;", "getNewFileDownloaderForDownload", "Lcom/tonyodev/fetch2/downloader/FileDownloader;", "download", "start", "terminateAllDownloads", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: DownloadManager.kt */
public interface DownloadManager extends Closeable {
    boolean canAccommodateNewDownload();

    boolean cancel(int i);

    void cancelAll();

    boolean contains(int i);

    int getActiveDownloadCount();

    @NotNull
    List<Download> getDownloads();

    @NotNull
    Delegate getFileDownloaderDelegate();

    @NotNull
    FileDownloader getNewFileDownloaderForDownload(@NotNull Download download);

    boolean isClosed();

    boolean start(@NotNull Download download);

    void terminateAllDownloads();
}
