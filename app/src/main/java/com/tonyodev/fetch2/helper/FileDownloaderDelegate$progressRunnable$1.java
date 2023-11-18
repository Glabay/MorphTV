package com.tonyodev.fetch2.helper;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016¨\u0006\u0005"}, d2 = {"com/tonyodev/fetch2/helper/FileDownloaderDelegate$progressRunnable$1", "Lcom/tonyodev/fetch2/helper/DownloadReportingRunnable;", "(Lcom/tonyodev/fetch2/helper/FileDownloaderDelegate;)V", "run", "", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: FileDownloaderDelegate.kt */
public final class FileDownloaderDelegate$progressRunnable$1 extends DownloadReportingRunnable {
    final /* synthetic */ FileDownloaderDelegate this$0;

    FileDownloaderDelegate$progressRunnable$1(FileDownloaderDelegate fileDownloaderDelegate) {
        this.this$0 = fileDownloaderDelegate;
    }

    public void run() {
        this.this$0.fetchListener.onProgress(getDownload(), getEtaInMilliSeconds(), getDownloadedBytesPerSecond());
    }
}
