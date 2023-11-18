package com.tonyodev.fetch2.downloader;

import android.os.Handler;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Downloader;
import com.tonyodev.fetch2.Downloader.FileDownloaderType;
import com.tonyodev.fetch2.Downloader.Request;
import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.RequestOptions;
import com.tonyodev.fetch2.downloader.FileDownloader.Delegate;
import com.tonyodev.fetch2.exception.FetchException.Code;
import com.tonyodev.fetch2.exception.FetchImplementationException;
import com.tonyodev.fetch2.helper.DownloadInfoUpdater;
import com.tonyodev.fetch2.helper.FileDownloaderDelegate;
import com.tonyodev.fetch2.provider.ListenerProvider;
import com.tonyodev.fetch2.provider.NetworkInfoProvider;
import com.tonyodev.fetch2.util.FetchUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001Bk\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016\u0012\u0006\u0010\u0018\u001a\u00020\u0019¢\u0006\u0002\u0010\u001aJ\b\u0010(\u001a\u00020\u000eH\u0016J\u0010\u0010)\u001a\u00020\u000e2\u0006\u0010*\u001a\u00020\u0005H\u0016J\b\u0010+\u001a\u00020,H\u0016J\b\u0010-\u001a\u00020,H\u0002J\b\u0010.\u001a\u00020,H\u0016J\u0010\u0010/\u001a\u00020\u000e2\u0006\u0010*\u001a\u00020\u0005H\u0016J\b\u00100\u001a\u00020\u0005H\u0016J\u000e\u00101\u001a\b\u0012\u0004\u0012\u00020302H\u0016J\b\u00104\u001a\u000205H\u0016J\u0010\u00106\u001a\u00020\u001e2\u0006\u00107\u001a\u000203H\u0016J\u0010\u00108\u001a\u00020\u000e2\u0006\u00107\u001a\u000203H\u0016J\b\u00109\u001a\u00020,H\u0016J\b\u0010:\u001a\u00020,H\u0002R\u0012\u0010\u001b\u001a\u00020\u000e8\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R*\u0010\u001c\u001a\u001e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u001e0\u001dj\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u001e`\u001fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010 \u001a\u00020\u00058\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010!\u001a\n #*\u0004\u0018\u00010\"0\"X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010$\u001a\u00020\u000e8VX\u0004¢\u0006\u0006\u001a\u0004\b$\u0010%R\u000e\u0010&\u001a\u00020'X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000¨\u0006;"}, d2 = {"Lcom/tonyodev/fetch2/downloader/DownloadManagerImpl;", "Lcom/tonyodev/fetch2/downloader/DownloadManager;", "downloader", "Lcom/tonyodev/fetch2/Downloader;", "concurrentLimit", "", "progressReportingIntervalMillis", "", "downloadBufferSizeBytes", "logger", "Lcom/tonyodev/fetch2/Logger;", "networkInfoProvider", "Lcom/tonyodev/fetch2/provider/NetworkInfoProvider;", "retryOnNetworkGain", "", "fetchListenerProvider", "Lcom/tonyodev/fetch2/provider/ListenerProvider;", "uiHandler", "Landroid/os/Handler;", "downloadInfoUpdater", "Lcom/tonyodev/fetch2/helper/DownloadInfoUpdater;", "requestOptions", "", "Lcom/tonyodev/fetch2/RequestOptions;", "fileTempDir", "", "(Lcom/tonyodev/fetch2/Downloader;IJILcom/tonyodev/fetch2/Logger;Lcom/tonyodev/fetch2/provider/NetworkInfoProvider;ZLcom/tonyodev/fetch2/provider/ListenerProvider;Landroid/os/Handler;Lcom/tonyodev/fetch2/helper/DownloadInfoUpdater;Ljava/util/Set;Ljava/lang/String;)V", "closed", "currentDownloadsMap", "Ljava/util/HashMap;", "Lcom/tonyodev/fetch2/downloader/FileDownloader;", "Lkotlin/collections/HashMap;", "downloadCounter", "executor", "Ljava/util/concurrent/ExecutorService;", "kotlin.jvm.PlatformType", "isClosed", "()Z", "lock", "Ljava/lang/Object;", "canAccommodateNewDownload", "cancel", "id", "cancelAll", "", "cancelAllDownloads", "close", "contains", "getActiveDownloadCount", "getDownloads", "", "Lcom/tonyodev/fetch2/Download;", "getFileDownloaderDelegate", "Lcom/tonyodev/fetch2/downloader/FileDownloader$Delegate;", "getNewFileDownloaderForDownload", "download", "start", "terminateAllDownloads", "throwExceptionIfClosed", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: DownloadManagerImpl.kt */
public final class DownloadManagerImpl implements DownloadManager {
    private volatile boolean closed;
    private final int concurrentLimit;
    private final HashMap<Integer, FileDownloader> currentDownloadsMap = new HashMap();
    private final int downloadBufferSizeBytes;
    private volatile int downloadCounter;
    private final DownloadInfoUpdater downloadInfoUpdater;
    private final Downloader downloader;
    private final ExecutorService executor = Executors.newFixedThreadPool(this.concurrentLimit);
    private final ListenerProvider fetchListenerProvider;
    private final String fileTempDir;
    private final Object lock = new Object();
    private final Logger logger;
    private final NetworkInfoProvider networkInfoProvider;
    private final long progressReportingIntervalMillis;
    private final Set<RequestOptions> requestOptions;
    private final boolean retryOnNetworkGain;
    private final Handler uiHandler;

    public DownloadManagerImpl(@NotNull Downloader downloader, int i, long j, int i2, @NotNull Logger logger, @NotNull NetworkInfoProvider networkInfoProvider, boolean z, @NotNull ListenerProvider listenerProvider, @NotNull Handler handler, @NotNull DownloadInfoUpdater downloadInfoUpdater, @NotNull Set<? extends RequestOptions> set, @NotNull String str) {
        Intrinsics.checkParameterIsNotNull(downloader, "downloader");
        Intrinsics.checkParameterIsNotNull(logger, "logger");
        Intrinsics.checkParameterIsNotNull(networkInfoProvider, "networkInfoProvider");
        Intrinsics.checkParameterIsNotNull(listenerProvider, "fetchListenerProvider");
        Intrinsics.checkParameterIsNotNull(handler, "uiHandler");
        Intrinsics.checkParameterIsNotNull(downloadInfoUpdater, "downloadInfoUpdater");
        Intrinsics.checkParameterIsNotNull(set, "requestOptions");
        Intrinsics.checkParameterIsNotNull(str, "fileTempDir");
        this.downloader = downloader;
        this.concurrentLimit = i;
        this.progressReportingIntervalMillis = j;
        this.downloadBufferSizeBytes = i2;
        this.logger = logger;
        this.networkInfoProvider = networkInfoProvider;
        this.retryOnNetworkGain = z;
        this.fetchListenerProvider = listenerProvider;
        this.uiHandler = handler;
        this.downloadInfoUpdater = downloadInfoUpdater;
        this.requestOptions = set;
        this.fileTempDir = str;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public boolean start(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            boolean z = false;
            Logger logger;
            StringBuilder stringBuilder;
            if (this.currentDownloadsMap.containsKey(Integer.valueOf(download.getId()))) {
                logger = this.logger;
                stringBuilder = new StringBuilder();
                stringBuilder.append("DownloadManager already running download ");
                stringBuilder.append(download);
                logger.mo4160d(stringBuilder.toString());
                return false;
            } else if (this.downloadCounter >= this.concurrentLimit) {
                logger = this.logger;
                stringBuilder = new StringBuilder();
                stringBuilder.append("DownloadManager cannot init download ");
                stringBuilder.append(download);
                stringBuilder.append(" because ");
                stringBuilder.append("the download queue is full");
                logger.mo4160d(stringBuilder.toString());
                return false;
            } else {
                FileDownloader newFileDownloaderForDownload = getNewFileDownloaderForDownload(download);
                newFileDownloaderForDownload.setDelegate(getFileDownloaderDelegate());
                this.downloadCounter++;
                this.currentDownloadsMap.put(Integer.valueOf(download.getId()), newFileDownloaderForDownload);
                try {
                    this.executor.execute(new DownloadManagerImpl$start$$inlined$synchronized$lambda$1(newFileDownloaderForDownload, this, download));
                    z = true;
                } catch (Exception e) {
                    Logger logger2 = this.logger;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("DownloadManager failed to start download ");
                    stringBuilder2.append(download);
                    logger2.mo4163e(stringBuilder2.toString(), e);
                }
            }
        }
        return z;
    }

    public boolean cancel(int i) {
        boolean z;
        synchronized (this.lock) {
            throwExceptionIfClosed();
            z = true;
            if (this.currentDownloadsMap.containsKey(Integer.valueOf(i))) {
                Object obj = this.currentDownloadsMap.get(Integer.valueOf(i));
                if (obj == null) {
                    throw new TypeCastException("null cannot be cast to non-null type com.tonyodev.fetch2.downloader.FileDownloader");
                }
                FileDownloader fileDownloader = (FileDownloader) obj;
                fileDownloader.setInterrupted(true);
                while (!fileDownloader.getTerminated()) {
                }
                this.currentDownloadsMap.remove(Integer.valueOf(i));
                this.downloadCounter--;
                i = this.logger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("DownloadManager cancelled download ");
                stringBuilder.append(fileDownloader.getDownload());
                i.mo4160d(stringBuilder.toString());
            } else {
                z = false;
            }
        }
        return z;
    }

    public void cancelAll() {
        synchronized (this.lock) {
            throwExceptionIfClosed();
            cancelAllDownloads();
            Unit unit = Unit.INSTANCE;
        }
    }

    private final void cancelAllDownloads() {
        for (Entry entry : this.currentDownloadsMap.entrySet()) {
            ((FileDownloader) entry.getValue()).setInterrupted(true);
            while (!((FileDownloader) entry.getValue()).getTerminated()) {
            }
            Logger logger = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DownloadManager cancelled download ");
            stringBuilder.append(((FileDownloader) entry.getValue()).getDownload());
            logger.mo4160d(stringBuilder.toString());
        }
        this.currentDownloadsMap.clear();
        this.downloadCounter = 0;
    }

    public void terminateAllDownloads() {
        for (Entry entry : this.currentDownloadsMap.entrySet()) {
            ((FileDownloader) entry.getValue()).setTerminated(true);
            Logger logger = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DownloadManager terminated download ");
            stringBuilder.append(((FileDownloader) entry.getValue()).getDownload());
            logger.mo4160d(stringBuilder.toString());
        }
        this.currentDownloadsMap.clear();
        this.downloadCounter = 0;
        try {
            this.downloader.close();
        } catch (Exception e) {
            this.logger.mo4163e("DownloadManager closing downloader", e);
        }
    }

    public void close() {
        synchronized (this.lock) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            terminateAllDownloads();
            this.logger.mo4160d("DownloadManager closing download manager");
            this.executor.shutdown();
            Unit unit = Unit.INSTANCE;
        }
    }

    public boolean contains(int i) {
        synchronized (this.lock) {
            throwExceptionIfClosed();
            i = this.currentDownloadsMap.containsKey(Integer.valueOf(i));
        }
        return i;
    }

    public boolean canAccommodateNewDownload() {
        boolean z;
        synchronized (this.lock) {
            throwExceptionIfClosed();
            z = this.downloadCounter < this.concurrentLimit;
        }
        return z;
    }

    public int getActiveDownloadCount() {
        int i;
        synchronized (this.lock) {
            throwExceptionIfClosed();
            i = this.downloadCounter;
        }
        return i;
    }

    @NotNull
    public List<Download> getDownloads() {
        List<Download> list;
        synchronized (this.lock) {
            throwExceptionIfClosed();
            Collection values = this.currentDownloadsMap.values();
            Intrinsics.checkExpressionValueIsNotNull(values, "currentDownloadsMap.values");
            Iterable<FileDownloader> iterable = values;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            for (FileDownloader download : iterable) {
                arrayList.add(download.getDownload());
            }
            list = (List) arrayList;
        }
        return list;
    }

    private final void throwExceptionIfClosed() {
        if (this.closed) {
            throw new FetchImplementationException("DownloadManager is already shutdown.", Code.CLOSED);
        }
    }

    @NotNull
    public FileDownloader getNewFileDownloaderForDownload(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "download");
        Request requestForDownload$default = FetchUtils.getRequestForDownload$default(download, 0, 0, 6, null);
        if (this.downloader.getFileDownloaderType(requestForDownload$default) == FileDownloaderType.SEQUENTIAL) {
            return new SequentialFileDownloaderImpl(download, this.downloader, this.progressReportingIntervalMillis, this.downloadBufferSizeBytes, this.logger, this.networkInfoProvider, this.retryOnNetworkGain);
        }
        String directoryForFileDownloaderTypeParallel = this.downloader.getDirectoryForFileDownloaderTypeParallel(requestForDownload$default);
        if (directoryForFileDownloaderTypeParallel == null) {
            directoryForFileDownloaderTypeParallel = this.fileTempDir;
        }
        return new ParallelFileDownloaderImpl(download, this.downloader, this.progressReportingIntervalMillis, this.downloadBufferSizeBytes, this.logger, this.networkInfoProvider, this.retryOnNetworkGain, directoryForFileDownloaderTypeParallel);
    }

    @NotNull
    public Delegate getFileDownloaderDelegate() {
        return new FileDownloaderDelegate(this.downloadInfoUpdater, this.uiHandler, this.fetchListenerProvider.getMainListener(), this.logger, this.retryOnNetworkGain, this.requestOptions, this.downloader, this.fileTempDir);
    }
}
