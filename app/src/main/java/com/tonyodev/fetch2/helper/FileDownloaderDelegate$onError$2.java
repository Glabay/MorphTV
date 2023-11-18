package com.tonyodev.fetch2.helper;

import com.tonyodev.fetch2.database.DownloadInfo;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 10})
/* compiled from: FileDownloaderDelegate.kt */
final class FileDownloaderDelegate$onError$2 implements Runnable {
    final /* synthetic */ DownloadInfo $downloadInfo;
    final /* synthetic */ FileDownloaderDelegate this$0;

    FileDownloaderDelegate$onError$2(FileDownloaderDelegate fileDownloaderDelegate, DownloadInfo downloadInfo) {
        this.this$0 = fileDownloaderDelegate;
        this.$downloadInfo = downloadInfo;
    }

    public final void run() {
        this.this$0.fetchListener.onError(this.$downloadInfo);
    }
}
