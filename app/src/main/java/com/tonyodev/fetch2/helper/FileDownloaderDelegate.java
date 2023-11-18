package com.tonyodev.fetch2.helper;

import android.os.Handler;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Downloader;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.RequestOptions;
import com.tonyodev.fetch2.Status;
import com.tonyodev.fetch2.database.DownloadInfo;
import com.tonyodev.fetch2.downloader.FileDownloader.Delegate;
import com.tonyodev.fetch2.util.FetchDefaults;
import com.tonyodev.fetch2.util.FetchUtils;
import java.io.File;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0005*\u0001\u0015\u0018\u00002\u00020\u0001BK\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012¢\u0006\u0002\u0010\u0013J\u001a\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\b\b\u0002\u0010\u001b\u001a\u00020\u000bH\u0002J\u0010\u0010\u001c\u001a\u00020\u00182\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\u0010\u0010\u001f\u001a\u00020\u00182\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J \u0010 \u001a\u00020\u00182\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\"H\u0016J \u0010$\u001a\u00020\u00182\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\"2\u0006\u0010#\u001a\u00020\"H\u0016J\u0010\u0010&\u001a\u00020\u00182\u0006\u0010\u001d\u001a\u00020\u001eH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0004\n\u0002\u0010\u0016R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006'"}, d2 = {"Lcom/tonyodev/fetch2/helper/FileDownloaderDelegate;", "Lcom/tonyodev/fetch2/downloader/FileDownloader$Delegate;", "downloadInfoUpdater", "Lcom/tonyodev/fetch2/helper/DownloadInfoUpdater;", "uiHandler", "Landroid/os/Handler;", "fetchListener", "Lcom/tonyodev/fetch2/FetchListener;", "logger", "Lcom/tonyodev/fetch2/Logger;", "retryOnNetworkGain", "", "requestOptions", "", "Lcom/tonyodev/fetch2/RequestOptions;", "downloader", "Lcom/tonyodev/fetch2/Downloader;", "fileTempDir", "", "(Lcom/tonyodev/fetch2/helper/DownloadInfoUpdater;Landroid/os/Handler;Lcom/tonyodev/fetch2/FetchListener;Lcom/tonyodev/fetch2/Logger;ZLjava/util/Set;Lcom/tonyodev/fetch2/Downloader;Ljava/lang/String;)V", "progressRunnable", "com/tonyodev/fetch2/helper/FileDownloaderDelegate$progressRunnable$1", "Lcom/tonyodev/fetch2/helper/FileDownloaderDelegate$progressRunnable$1;", "deleteDownloadInfo", "", "downloadInfo", "Lcom/tonyodev/fetch2/database/DownloadInfo;", "deleteFile", "onComplete", "download", "Lcom/tonyodev/fetch2/Download;", "onError", "onProgress", "etaInMilliSeconds", "", "downloadedBytesPerSecond", "onStarted", "etaInMilliseconds", "saveDownloadProgress", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: FileDownloaderDelegate.kt */
public final class FileDownloaderDelegate implements Delegate {
    private final DownloadInfoUpdater downloadInfoUpdater;
    private final Downloader downloader;
    private final FetchListener fetchListener;
    private final String fileTempDir;
    private final Logger logger;
    private final FileDownloaderDelegate$progressRunnable$1 progressRunnable = new FileDownloaderDelegate$progressRunnable$1(this);
    private final Set<RequestOptions> requestOptions;
    private final boolean retryOnNetworkGain;
    private final Handler uiHandler;

    public FileDownloaderDelegate(@NotNull DownloadInfoUpdater downloadInfoUpdater, @NotNull Handler handler, @NotNull FetchListener fetchListener, @NotNull Logger logger, boolean z, @NotNull Set<? extends RequestOptions> set, @NotNull Downloader downloader, @NotNull String str) {
        Intrinsics.checkParameterIsNotNull(downloadInfoUpdater, "downloadInfoUpdater");
        Intrinsics.checkParameterIsNotNull(handler, "uiHandler");
        Intrinsics.checkParameterIsNotNull(fetchListener, "fetchListener");
        Intrinsics.checkParameterIsNotNull(logger, "logger");
        Intrinsics.checkParameterIsNotNull(set, "requestOptions");
        Intrinsics.checkParameterIsNotNull(downloader, "downloader");
        Intrinsics.checkParameterIsNotNull(str, "fileTempDir");
        this.downloadInfoUpdater = downloadInfoUpdater;
        this.uiHandler = handler;
        this.fetchListener = fetchListener;
        this.logger = logger;
        this.retryOnNetworkGain = z;
        this.requestOptions = set;
        this.downloader = downloader;
        this.fileTempDir = str;
    }

    public void onStarted(@NotNull Download download, long j, long j2) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        DownloadInfo downloadInfo = (DownloadInfo) download;
        downloadInfo.setStatus(Status.DOWNLOADING);
        try {
            this.downloadInfoUpdater.update(downloadInfo);
            this.uiHandler.post(new FileDownloaderDelegate$onStarted$1(this, downloadInfo, j, j2));
        } catch (Download download2) {
            this.logger.mo4163e("DownloadManagerDelegate", (Throwable) download2);
        }
    }

    public void onProgress(@NotNull Download download, long j, long j2) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        try {
            this.progressRunnable.setDownload(download);
            this.progressRunnable.setEtaInMilliSeconds(j);
            this.progressRunnable.setDownloadedBytesPerSecond(j2);
            this.uiHandler.post((Runnable) this.progressRunnable);
        } catch (Download download2) {
            this.logger.mo4163e("DownloadManagerDelegate", (Throwable) download2);
        }
    }

    public void onError(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        DownloadInfo downloadInfo = (DownloadInfo) download;
        try {
            if (this.retryOnNetworkGain && downloadInfo.getError() == Error.NO_NETWORK_CONNECTION) {
                downloadInfo.setStatus(Status.QUEUED);
                downloadInfo.setError(FetchDefaults.getDefaultNoError());
                this.downloadInfoUpdater.update(downloadInfo);
                this.uiHandler.post(new FileDownloaderDelegate$onError$1(this, downloadInfo));
                return;
            }
            downloadInfo.setStatus(Status.FAILED);
            if (this.requestOptions.contains(RequestOptions.AUTO_REMOVE_ON_FAILED)) {
                deleteDownloadInfo$default(this, downloadInfo, false, 2, null);
            } else if (this.requestOptions.contains(RequestOptions.AUTO_REMOVE_ON_FAILED_DELETE_FILE)) {
                deleteDownloadInfo(downloadInfo, true);
            } else {
                this.downloadInfoUpdater.update(downloadInfo);
            }
            this.uiHandler.post(new FileDownloaderDelegate$onError$2(this, downloadInfo));
        } catch (Download download2) {
            this.logger.mo4163e("DownloadManagerDelegate", (Throwable) download2);
        }
    }

    public void onComplete(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        DownloadInfo downloadInfo = (DownloadInfo) download;
        downloadInfo.setStatus(Status.COMPLETED);
        try {
            if (this.requestOptions.contains(RequestOptions.AUTO_REMOVE_ON_COMPLETED)) {
                deleteDownloadInfo$default(this, downloadInfo, false, 2, null);
            } else if (this.requestOptions.contains(RequestOptions.AUTO_REMOVE_ON_COMPLETED_DELETE_FILE)) {
                deleteDownloadInfo(downloadInfo, true);
            } else {
                this.downloadInfoUpdater.update(downloadInfo);
            }
            this.uiHandler.post(new FileDownloaderDelegate$onComplete$1(this, downloadInfo));
        } catch (Download download2) {
            this.logger.mo4163e("DownloadManagerDelegate", (Throwable) download2);
        }
    }

    public void saveDownloadProgress(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        try {
            DownloadInfo downloadInfo = (DownloadInfo) download;
            downloadInfo.setStatus(Status.DOWNLOADING);
            this.downloadInfoUpdater.updateFileBytesInfoAndStatusOnly(downloadInfo);
        } catch (Download download2) {
            this.logger.mo4163e("DownloadManagerDelegate", (Throwable) download2);
        }
    }

    static /* bridge */ /* synthetic */ void deleteDownloadInfo$default(FileDownloaderDelegate fileDownloaderDelegate, DownloadInfo downloadInfo, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        fileDownloaderDelegate.deleteDownloadInfo(downloadInfo, z);
    }

    private final void deleteDownloadInfo(DownloadInfo downloadInfo, boolean z) {
        File file = new File(downloadInfo.getFile());
        this.downloadInfoUpdater.deleteDownload(downloadInfo);
        if (z && file.exists()) {
            file.delete();
            FetchUtils.deleteRequestTempFiles(this.fileTempDir, this.downloader, downloadInfo);
        }
    }
}
