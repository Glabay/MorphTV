package com.tonyodev.fetch2;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0005\b&\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J \u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH\u0016J\u0010\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u0012"}, d2 = {"Lcom/tonyodev/fetch2/AbstractFetchListener;", "Lcom/tonyodev/fetch2/FetchListener;", "()V", "onCancelled", "", "download", "Lcom/tonyodev/fetch2/Download;", "onCompleted", "onDeleted", "onError", "onPaused", "onProgress", "etaInMilliSeconds", "", "downloadedBytesPerSecond", "onQueued", "onRemoved", "onResumed", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: AbstractFetchListener.kt */
public abstract class AbstractFetchListener implements FetchListener {
    public void onCancelled(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
    }

    public void onCompleted(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
    }

    public void onDeleted(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
    }

    public void onError(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
    }

    public void onPaused(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
    }

    public void onProgress(@NotNull Download download, long j, long j2) {
        Intrinsics.checkParameterIsNotNull(download, "download");
    }

    public void onQueued(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
    }

    public void onRemoved(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
    }

    public void onResumed(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
    }
}
