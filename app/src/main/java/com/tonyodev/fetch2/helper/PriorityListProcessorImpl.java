package com.tonyodev.fetch2.helper;

import android.os.Handler;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.downloader.DownloadManager;
import com.tonyodev.fetch2.provider.DownloadProvider;
import com.tonyodev.fetch2.provider.NetworkInfoProvider;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B-\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f¢\u0006\u0002\u0010\rJ\b\u0010\u001e\u001a\u00020\u0015H\u0002J\u000e\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00020 H\u0016J\b\u0010!\u001a\u00020\"H\u0016J\b\u0010#\u001a\u00020\"H\u0002J\b\u0010$\u001a\u00020\"H\u0016J\b\u0010%\u001a\u00020\"H\u0016J\b\u0010&\u001a\u00020\"H\u0016J\b\u0010'\u001a\u00020\"H\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u000e\u001a\u00020\u000f8\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\u00020\u00158VX\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0016R\u0014\u0010\u0017\u001a\u00020\u00158VX\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0016R\u000e\u0010\u0018\u001a\u00020\u0019X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u001a\u001a\u00020\u00158\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u001d\u001a\u00020\u00158\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000¨\u0006("}, d2 = {"Lcom/tonyodev/fetch2/helper/PriorityListProcessorImpl;", "Lcom/tonyodev/fetch2/helper/PriorityListProcessor;", "Lcom/tonyodev/fetch2/Download;", "handler", "Landroid/os/Handler;", "downloadProvider", "Lcom/tonyodev/fetch2/provider/DownloadProvider;", "downloadManager", "Lcom/tonyodev/fetch2/downloader/DownloadManager;", "networkInfoProvider", "Lcom/tonyodev/fetch2/provider/NetworkInfoProvider;", "logger", "Lcom/tonyodev/fetch2/Logger;", "(Landroid/os/Handler;Lcom/tonyodev/fetch2/provider/DownloadProvider;Lcom/tonyodev/fetch2/downloader/DownloadManager;Lcom/tonyodev/fetch2/provider/NetworkInfoProvider;Lcom/tonyodev/fetch2/Logger;)V", "globalNetworkType", "Lcom/tonyodev/fetch2/NetworkType;", "getGlobalNetworkType", "()Lcom/tonyodev/fetch2/NetworkType;", "setGlobalNetworkType", "(Lcom/tonyodev/fetch2/NetworkType;)V", "isPaused", "", "()Z", "isStopped", "lock", "Ljava/lang/Object;", "paused", "priorityIteratorRunnable", "Ljava/lang/Runnable;", "stopped", "canContinueToProcess", "getPriorityList", "", "pause", "", "registerPriorityIterator", "resume", "start", "stop", "unregisterPriorityIterator", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: PriorityListProcessorImpl.kt */
public final class PriorityListProcessorImpl implements PriorityListProcessor<Download> {
    private final DownloadManager downloadManager;
    private final DownloadProvider downloadProvider;
    @NotNull
    private volatile NetworkType globalNetworkType = NetworkType.GLOBAL_OFF;
    private final Handler handler;
    private final Object lock = new Object();
    private final Logger logger;
    private final NetworkInfoProvider networkInfoProvider;
    private volatile boolean paused;
    private final Runnable priorityIteratorRunnable = ((Runnable) new PriorityListProcessorImpl$priorityIteratorRunnable$1(this));
    private volatile boolean stopped;

    public PriorityListProcessorImpl(@NotNull Handler handler, @NotNull DownloadProvider downloadProvider, @NotNull DownloadManager downloadManager, @NotNull NetworkInfoProvider networkInfoProvider, @NotNull Logger logger) {
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        Intrinsics.checkParameterIsNotNull(downloadProvider, "downloadProvider");
        Intrinsics.checkParameterIsNotNull(downloadManager, "downloadManager");
        Intrinsics.checkParameterIsNotNull(networkInfoProvider, "networkInfoProvider");
        Intrinsics.checkParameterIsNotNull(logger, "logger");
        this.handler = handler;
        this.downloadProvider = downloadProvider;
        this.downloadManager = downloadManager;
        this.networkInfoProvider = networkInfoProvider;
        this.logger = logger;
    }

    @NotNull
    public NetworkType getGlobalNetworkType() {
        return this.globalNetworkType;
    }

    public void setGlobalNetworkType(@NotNull NetworkType networkType) {
        Intrinsics.checkParameterIsNotNull(networkType, "<set-?>");
        this.globalNetworkType = networkType;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public boolean isStopped() {
        return this.stopped;
    }

    private final boolean canContinueToProcess() {
        return (this.stopped || this.paused) ? false : true;
    }

    public void start() {
        synchronized (this.lock) {
            this.stopped = false;
            this.paused = false;
            registerPriorityIterator();
            Unit unit = Unit.INSTANCE;
        }
    }

    public void stop() {
        synchronized (this.lock) {
            unregisterPriorityIterator();
            this.paused = false;
            this.stopped = true;
            Unit unit = Unit.INSTANCE;
        }
    }

    public void pause() {
        synchronized (this.lock) {
            unregisterPriorityIterator();
            this.paused = true;
            this.stopped = false;
            this.downloadManager.cancelAll();
            this.logger.mo4160d("PriorityIterator paused");
            Unit unit = Unit.INSTANCE;
        }
    }

    public void resume() {
        synchronized (this.lock) {
            this.paused = false;
            this.stopped = false;
            registerPriorityIterator();
            this.logger.mo4160d("PriorityIterator resumed");
            Unit unit = Unit.INSTANCE;
        }
    }

    @NotNull
    public List<Download> getPriorityList() {
        List<Download> pendingDownloadsSorted;
        synchronized (this.lock) {
            try {
                pendingDownloadsSorted = this.downloadProvider.getPendingDownloadsSorted();
            } catch (Exception e) {
                this.logger.mo4161d("PriorityIterator failed access database", e);
                pendingDownloadsSorted = CollectionsKt.emptyList();
            }
        }
        return pendingDownloadsSorted;
    }

    private final void registerPriorityIterator() {
        this.handler.postDelayed(this.priorityIteratorRunnable, 500);
    }

    private final void unregisterPriorityIterator() {
        this.handler.removeCallbacks(this.priorityIteratorRunnable);
    }
}
