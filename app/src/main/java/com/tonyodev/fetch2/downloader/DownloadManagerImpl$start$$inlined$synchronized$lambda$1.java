package com.tonyodev.fetch2.downloader;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Logger;
import kotlin.Metadata;
import kotlin.Unit;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"}, d2 = {"<anonymous>", "", "run", "com/tonyodev/fetch2/downloader/DownloadManagerImpl$start$1$1"}, k = 3, mv = {1, 1, 10})
/* compiled from: DownloadManagerImpl.kt */
final class DownloadManagerImpl$start$$inlined$synchronized$lambda$1 implements Runnable {
    final /* synthetic */ Download $download$inlined;
    final /* synthetic */ FileDownloader $fileDownloader;
    final /* synthetic */ DownloadManagerImpl this$0;

    DownloadManagerImpl$start$$inlined$synchronized$lambda$1(FileDownloader fileDownloader, DownloadManagerImpl downloadManagerImpl, Download download) {
        this.$fileDownloader = fileDownloader;
        this.this$0 = downloadManagerImpl;
        this.$download$inlined = download;
    }

    public final void run() {
        Logger access$getLogger$p = this.this$0.logger;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DownloadManager starting download ");
        stringBuilder.append(this.$download$inlined);
        access$getLogger$p.mo4160d(stringBuilder.toString());
        this.$fileDownloader.run();
        synchronized (this.this$0.lock) {
            if (this.this$0.currentDownloadsMap.containsKey(Integer.valueOf(this.$download$inlined.getId()))) {
                this.this$0.currentDownloadsMap.remove(Integer.valueOf(this.$download$inlined.getId()));
                DownloadManagerImpl downloadManagerImpl = this.this$0;
                downloadManagerImpl.downloadCounter = downloadManagerImpl.downloadCounter - 1;
            }
            Unit unit = Unit.INSTANCE;
        }
    }
}
