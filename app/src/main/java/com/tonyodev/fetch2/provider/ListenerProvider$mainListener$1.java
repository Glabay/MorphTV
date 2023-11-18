package com.tonyodev.fetch2.provider;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.FetchListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J \u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH\u0016J\u0010\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u0012"}, d2 = {"com/tonyodev/fetch2/provider/ListenerProvider$mainListener$1", "Lcom/tonyodev/fetch2/FetchListener;", "(Lcom/tonyodev/fetch2/provider/ListenerProvider;)V", "onCancelled", "", "download", "Lcom/tonyodev/fetch2/Download;", "onCompleted", "onDeleted", "onError", "onPaused", "onProgress", "etaInMilliSeconds", "", "downloadedBytesPerSecond", "onQueued", "onRemoved", "onResumed", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: ListenerProvider.kt */
public final class ListenerProvider$mainListener$1 implements FetchListener {
    final /* synthetic */ ListenerProvider this$0;

    ListenerProvider$mainListener$1(ListenerProvider listenerProvider) {
        this.this$0 = listenerProvider;
    }

    public void onQueued(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        for (FetchListener onQueued : this.this$0.getListeners()) {
            onQueued.onQueued(download);
        }
    }

    public void onCompleted(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        for (FetchListener onCompleted : this.this$0.getListeners()) {
            onCompleted.onCompleted(download);
        }
    }

    public void onError(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        for (FetchListener onError : this.this$0.getListeners()) {
            onError.onError(download);
        }
    }

    public void onProgress(@NotNull Download download, long j, long j2) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        for (FetchListener onProgress : this.this$0.getListeners()) {
            onProgress.onProgress(download, j, j2);
        }
    }

    public void onPaused(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        for (FetchListener onPaused : this.this$0.getListeners()) {
            onPaused.onPaused(download);
        }
    }

    public void onResumed(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        for (FetchListener onResumed : this.this$0.getListeners()) {
            onResumed.onResumed(download);
        }
    }

    public void onCancelled(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        for (FetchListener onCancelled : this.this$0.getListeners()) {
            onCancelled.onCancelled(download);
        }
    }

    public void onRemoved(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        for (FetchListener onRemoved : this.this$0.getListeners()) {
            onRemoved.onRemoved(download);
        }
    }

    public void onDeleted(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        for (FetchListener onDeleted : this.this$0.getListeners()) {
            onDeleted.onDeleted(download);
        }
    }
}
