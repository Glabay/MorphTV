package com.tonyodev.fetch2;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J \u0010\n\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH&J\u0010\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0011"}, d2 = {"Lcom/tonyodev/fetch2/FetchListener;", "", "onCancelled", "", "download", "Lcom/tonyodev/fetch2/Download;", "onCompleted", "onDeleted", "onError", "onPaused", "onProgress", "etaInMilliSeconds", "", "downloadedBytesPerSecond", "onQueued", "onRemoved", "onResumed", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: FetchListener.kt */
public interface FetchListener {
    void onCancelled(@NotNull Download download);

    void onCompleted(@NotNull Download download);

    void onDeleted(@NotNull Download download);

    void onError(@NotNull Download download);

    void onPaused(@NotNull Download download);

    void onProgress(@NotNull Download download, long j, long j2);

    void onQueued(@NotNull Download download);

    void onRemoved(@NotNull Download download);

    void onResumed(@NotNull Download download);
}
