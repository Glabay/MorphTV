package com.tonyodev.fetch2.fetch;

import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs;
import com.google.android.gms.common.server.FavaDiagnosticsEntity;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Downloader;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2.RequestInfo;
import com.tonyodev.fetch2.RequestOptions;
import com.tonyodev.fetch2.Status;
import com.tonyodev.fetch2.database.DatabaseManager;
import com.tonyodev.fetch2.database.DownloadDatabase;
import com.tonyodev.fetch2.database.DownloadInfo;
import com.tonyodev.fetch2.downloader.DownloadManager;
import com.tonyodev.fetch2.helper.PriorityListProcessor;
import com.tonyodev.fetch2.provider.ListenerProvider;
import com.tonyodev.fetch2.util.FetchDatabaseExtensions;
import com.tonyodev.fetch2.util.FetchDefaults;
import com.tonyodev.fetch2.util.FetchErrorStrings;
import com.tonyodev.fetch2.util.FetchTypeConverterExtensions;
import com.tonyodev.fetch2.util.FetchUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001Bi\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014\u0012\u0006\u0010\u0016\u001a\u00020\u0017\u0012\u0006\u0010\u0018\u001a\u00020\u0003¢\u0006\u0002\u0010\u0019J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0016J\u0016\u0010!\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010#\u001a\u00020$H\u0016J\u000e\u0010%\u001a\b\u0012\u0004\u0012\u00020\n0\"H\u0016J\u0010\u0010&\u001a\u00020\u00122\u0006\u0010'\u001a\u00020(H\u0016J\u0016\u0010)\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010'\u001a\u00020(H\u0016J\b\u0010*\u001a\u00020\u001eH\u0016J\u0016\u0010+\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010#\u001a\u00020$H\u0016J\u000e\u0010,\u001a\b\u0012\u0004\u0012\u00020\n0\"H\u0016J\u0016\u0010-\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010.\u001a\u00020/H\u0016J\u0016\u00100\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010'\u001a\u00020(H\u0016J\u0010\u00101\u001a\u00020\u001e2\u0006\u00102\u001a\u00020\u0012H\u0016J\u0010\u00103\u001a\u00020\n2\u0006\u00104\u001a\u000205H\u0016J\u001c\u00103\u001a\b\u0012\u0004\u0012\u00020\n0\"2\f\u00106\u001a\b\u0012\u0004\u0012\u0002050\"H\u0016J\b\u00107\u001a\u00020\u001eH\u0016J\u0012\u00108\u001a\u0004\u0018\u00010\n2\u0006\u0010'\u001a\u00020(H\u0016J\u000e\u00109\u001a\b\u0012\u0004\u0012\u00020\n0\"H\u0016J\u001c\u00109\u001a\b\u0012\u0004\u0012\u00020\n0\"2\f\u0010:\u001a\b\u0012\u0004\u0012\u00020(0\"H\u0016J\u0016\u0010;\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010'\u001a\u00020(H\u0016J\u001e\u0010<\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010=\u001a\u00020(2\u0006\u0010.\u001a\u00020/H\u0016J\u0016\u0010>\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010.\u001a\u00020/H\u0016J\b\u0010?\u001a\u00020\u001eH\u0016J\u0010\u0010@\u001a\u00020\u00122\u0006\u0010'\u001a\u00020(H\u0016J\u0016\u0010A\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010#\u001a\u00020$H\u0016J\u0016\u0010B\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010'\u001a\u00020(H\u0016J\u0010\u0010C\u001a\u00020\u001e2\u0006\u0010D\u001a\u00020EH\u0002J\u001c\u0010F\u001a\b\u0012\u0004\u0012\u0002050\"2\f\u00106\u001a\b\u0012\u0004\u0012\u0002050\"H\u0002J\u0016\u0010G\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010#\u001a\u00020$H\u0016J\u000e\u0010H\u001a\b\u0012\u0004\u0012\u00020\n0\"H\u0016J\u0016\u0010I\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010.\u001a\u00020/H\u0016J\u0016\u0010J\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010'\u001a\u00020(H\u0016J\u0010\u0010K\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0016J\u0016\u0010L\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010#\u001a\u00020$H\u0016J\u0016\u0010M\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010'\u001a\u00020(H\u0016J\u0016\u0010N\u001a\b\u0012\u0004\u0012\u00020\n0\"2\u0006\u0010#\u001a\u00020$H\u0016J\u0010\u0010O\u001a\u00020\u001e2\u0006\u0010P\u001a\u00020QH\u0016J\b\u0010R\u001a\u00020\u001eH\u0002J\b\u0010S\u001a\u00020\u001eH\u0016J\u0010\u0010T\u001a\u00020\u001e2\u0006\u0010D\u001a\u00020EH\u0002J\u0016\u0010T\u001a\u00020\u001e2\f\u0010U\u001a\b\u0012\u0004\u0012\u00020E0\"H\u0002J\u001a\u0010V\u001a\u0004\u0018\u00010\n2\u0006\u0010'\u001a\u00020(2\u0006\u0010W\u001a\u00020XH\u0016R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\u0018\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u001c\u001a\u00020\u00128\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014X\u0004¢\u0006\u0002\n\u0000¨\u0006Y"}, d2 = {"Lcom/tonyodev/fetch2/fetch/FetchHandlerImpl;", "Lcom/tonyodev/fetch2/fetch/FetchHandler;", "namespace", "", "databaseManager", "Lcom/tonyodev/fetch2/database/DatabaseManager;", "downloadManager", "Lcom/tonyodev/fetch2/downloader/DownloadManager;", "priorityListProcessor", "Lcom/tonyodev/fetch2/helper/PriorityListProcessor;", "Lcom/tonyodev/fetch2/Download;", "fetchListenerProvider", "Lcom/tonyodev/fetch2/provider/ListenerProvider;", "handler", "Landroid/os/Handler;", "logger", "Lcom/tonyodev/fetch2/Logger;", "autoStart", "", "requestOptions", "", "Lcom/tonyodev/fetch2/RequestOptions;", "downloader", "Lcom/tonyodev/fetch2/Downloader;", "fileTempDir", "(Ljava/lang/String;Lcom/tonyodev/fetch2/database/DatabaseManager;Lcom/tonyodev/fetch2/downloader/DownloadManager;Lcom/tonyodev/fetch2/helper/PriorityListProcessor;Lcom/tonyodev/fetch2/provider/ListenerProvider;Landroid/os/Handler;Lcom/tonyodev/fetch2/Logger;ZLjava/util/Set;Lcom/tonyodev/fetch2/Downloader;Ljava/lang/String;)V", "getFetchListenerProvider", "()Lcom/tonyodev/fetch2/provider/ListenerProvider;", "isTerminating", "addListener", "", "listener", "Lcom/tonyodev/fetch2/FetchListener;", "cancel", "", "ids", "", "cancelAll", "cancelDownload", "id", "", "cancelGroup", "close", "delete", "deleteAll", "deleteAllWithStatus", "status", "Lcom/tonyodev/fetch2/Status;", "deleteGroup", "enableLogging", "enabled", "enqueue", "request", "Lcom/tonyodev/fetch2/Request;", "requests", "freeze", "getDownload", "getDownloads", "idList", "getDownloadsInGroup", "getDownloadsInGroupWithStatus", "groupId", "getDownloadsWithStatus", "init", "isDownloading", "pause", "pausedGroup", "prepareDownloadInfoForEnqueue", "downloadInfo", "Lcom/tonyodev/fetch2/database/DownloadInfo;", "prepareRequestListForEnqueue", "remove", "removeAll", "removeAllWithStatus", "removeGroup", "removeListener", "resume", "resumeGroup", "retry", "setGlobalNetworkType", "networkType", "Lcom/tonyodev/fetch2/NetworkType;", "startPriorityQueueIfNotStarted", "unfreeze", "updateFileForDownloadInfoIfNeeded", "downloadInfoList", "updateRequest", "requestInfo", "Lcom/tonyodev/fetch2/RequestInfo;", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: FetchHandlerImpl.kt */
public final class FetchHandlerImpl implements FetchHandler {
    private final boolean autoStart;
    private final DatabaseManager databaseManager;
    private final DownloadManager downloadManager;
    private final Downloader downloader;
    @NotNull
    private final ListenerProvider fetchListenerProvider;
    private final String fileTempDir;
    private final Handler handler;
    private volatile boolean isTerminating;
    private final Logger logger;
    private final String namespace;
    private final PriorityListProcessor<Download> priorityListProcessor;
    private final Set<RequestOptions> requestOptions;

    public FetchHandlerImpl(@NotNull String str, @NotNull DatabaseManager databaseManager, @NotNull DownloadManager downloadManager, @NotNull PriorityListProcessor<? extends Download> priorityListProcessor, @NotNull ListenerProvider listenerProvider, @NotNull Handler handler, @NotNull Logger logger, boolean z, @NotNull Set<? extends RequestOptions> set, @NotNull Downloader downloader, @NotNull String str2) {
        Intrinsics.checkParameterIsNotNull(str, FavaDiagnosticsEntity.EXTRA_NAMESPACE);
        Intrinsics.checkParameterIsNotNull(databaseManager, "databaseManager");
        Intrinsics.checkParameterIsNotNull(downloadManager, "downloadManager");
        Intrinsics.checkParameterIsNotNull(priorityListProcessor, "priorityListProcessor");
        Intrinsics.checkParameterIsNotNull(listenerProvider, "fetchListenerProvider");
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        Intrinsics.checkParameterIsNotNull(logger, "logger");
        Intrinsics.checkParameterIsNotNull(set, "requestOptions");
        Intrinsics.checkParameterIsNotNull(downloader, "downloader");
        Intrinsics.checkParameterIsNotNull(str2, "fileTempDir");
        this.namespace = str;
        this.databaseManager = databaseManager;
        this.downloadManager = downloadManager;
        this.priorityListProcessor = priorityListProcessor;
        this.fetchListenerProvider = listenerProvider;
        this.handler = handler;
        this.logger = logger;
        this.autoStart = z;
        this.requestOptions = set;
        this.downloader = downloader;
        this.fileTempDir = str2;
    }

    @NotNull
    public ListenerProvider getFetchListenerProvider() {
        return this.fetchListenerProvider;
    }

    public void init() {
        FetchDatabaseExtensions.sanitize(this.databaseManager, true);
        if (this.autoStart) {
            this.priorityListProcessor.start();
        }
    }

    @NotNull
    public Download enqueue(@NotNull Request request) {
        Intrinsics.checkParameterIsNotNull(request, "request");
        DownloadInfo toDownloadInfo = FetchTypeConverterExtensions.toDownloadInfo(request);
        toDownloadInfo.setNamespace(this.namespace);
        toDownloadInfo.setStatus(Status.QUEUED);
        prepareDownloadInfoForEnqueue(toDownloadInfo);
        this.databaseManager.insert(toDownloadInfo);
        startPriorityQueueIfNotStarted();
        return toDownloadInfo;
    }

    private final void prepareDownloadInfoForEnqueue(DownloadInfo downloadInfo) {
        Object obj = 1;
        if (this.requestOptions.contains(RequestOptions.REPLACE_ALL_ON_ENQUEUE_WHERE_UNIQUE) || this.requestOptions.contains(RequestOptions.REPLACE_ALL_ON_ENQUEUE_WHERE_UNIQUE_FRESH) || this.requestOptions.contains(RequestOptions.REPLACE_ON_ENQUEUE_ID) || this.requestOptions.contains(RequestOptions.REPLACE_ON_ENQUEUE_FRESH_ID) || this.requestOptions.contains(RequestOptions.REPLACE_ON_ENQUEUE_FILE) || this.requestOptions.contains(RequestOptions.REPLACE_ON_ENQUEUE_FRESH_FILE)) {
            DownloadInfo downloadInfo2;
            DownloadInfo downloadInfo3;
            DownloadInfo downloadInfo4 = (DownloadInfo) null;
            if (!(this.requestOptions.contains(RequestOptions.REPLACE_ALL_ON_ENQUEUE_WHERE_UNIQUE) || this.requestOptions.contains(RequestOptions.REPLACE_ALL_ON_ENQUEUE_WHERE_UNIQUE_FRESH) || this.requestOptions.contains(RequestOptions.REPLACE_ON_ENQUEUE_ID))) {
                if (!this.requestOptions.contains(RequestOptions.REPLACE_ON_ENQUEUE_FRESH_ID)) {
                    downloadInfo2 = downloadInfo4;
                    if (this.requestOptions.contains(RequestOptions.REPLACE_ALL_ON_ENQUEUE_WHERE_UNIQUE) || this.requestOptions.contains(RequestOptions.REPLACE_ALL_ON_ENQUEUE_WHERE_UNIQUE_FRESH) || this.requestOptions.contains(RequestOptions.REPLACE_ON_ENQUEUE_FILE) || this.requestOptions.contains(RequestOptions.REPLACE_ON_ENQUEUE_FRESH_FILE)) {
                        downloadInfo4 = this.databaseManager.getByFile(downloadInfo.getFile());
                        if (downloadInfo4 != null) {
                            cancelDownload(downloadInfo4.getId());
                            downloadInfo4 = this.databaseManager.getByFile(downloadInfo.getFile());
                        }
                    }
                    if (this.requestOptions.contains(RequestOptions.REPLACE_ON_ENQUEUE_ID) || this.requestOptions.contains(RequestOptions.REPLACE_ALL_ON_ENQUEUE_WHERE_UNIQUE) || this.requestOptions.contains(RequestOptions.REPLACE_ON_ENQUEUE_FILE)) {
                        downloadInfo3 = downloadInfo4 == null ? downloadInfo4 : downloadInfo2;
                        if (downloadInfo3 != null) {
                            downloadInfo.setDownloaded(downloadInfo3.getDownloaded());
                            downloadInfo.setTotal(downloadInfo3.getTotal());
                            if (downloadInfo3.getStatus() == Status.COMPLETED) {
                                downloadInfo.setStatus(downloadInfo3.getStatus());
                            }
                            obj = null;
                        }
                    }
                    if (downloadInfo4 != null) {
                        this.databaseManager.delete(downloadInfo4);
                        FetchUtils.deleteRequestTempFiles(this.fileTempDir, this.downloader, downloadInfo4);
                    }
                    if (downloadInfo2 != null) {
                        this.databaseManager.delete(downloadInfo2);
                        FetchUtils.deleteRequestTempFiles(this.fileTempDir, this.downloader, downloadInfo2);
                    }
                }
            }
            downloadInfo2 = this.databaseManager.get(downloadInfo.getId());
            if (downloadInfo2 != null) {
                cancelDownload(downloadInfo2.getId());
                downloadInfo2 = this.databaseManager.get(downloadInfo.getId());
            }
            downloadInfo4 = this.databaseManager.getByFile(downloadInfo.getFile());
            if (downloadInfo4 != null) {
                cancelDownload(downloadInfo4.getId());
                downloadInfo4 = this.databaseManager.getByFile(downloadInfo.getFile());
            }
            if (downloadInfo4 == null) {
            }
            if (downloadInfo3 != null) {
                downloadInfo.setDownloaded(downloadInfo3.getDownloaded());
                downloadInfo.setTotal(downloadInfo3.getTotal());
                if (downloadInfo3.getStatus() == Status.COMPLETED) {
                    downloadInfo.setStatus(downloadInfo3.getStatus());
                }
                obj = null;
            }
            if (downloadInfo4 != null) {
                this.databaseManager.delete(downloadInfo4);
                FetchUtils.deleteRequestTempFiles(this.fileTempDir, this.downloader, downloadInfo4);
            }
            if (downloadInfo2 != null) {
                this.databaseManager.delete(downloadInfo2);
                FetchUtils.deleteRequestTempFiles(this.fileTempDir, this.downloader, downloadInfo2);
            }
        }
        if (obj != null) {
            updateFileForDownloadInfoIfNeeded(downloadInfo);
        }
    }

    private final void updateFileForDownloadInfoIfNeeded(DownloadInfo downloadInfo) {
        updateFileForDownloadInfoIfNeeded(CollectionsKt.listOf(downloadInfo));
    }

    private final void updateFileForDownloadInfoIfNeeded(List<DownloadInfo> list) {
        if (this.requestOptions.contains(RequestOptions.ADD_AUTO_INCREMENT_TO_FILE_ON_ENQUEUE) || this.requestOptions.contains(RequestOptions.REPLACE_ALL_ON_ENQUEUE_WHERE_UNIQUE)) {
            for (DownloadInfo downloadInfo : list) {
                File incrementedFileIfOriginalExists = FetchUtils.getIncrementedFileIfOriginalExists(downloadInfo.getFile());
                int uniqueId = FetchUtils.getUniqueId(downloadInfo.getUrl(), downloadInfo.getFile());
                String absolutePath = incrementedFileIfOriginalExists.getAbsolutePath();
                Intrinsics.checkExpressionValueIsNotNull(absolutePath, "file.absolutePath");
                downloadInfo.setFile(absolutePath);
                if (uniqueId == downloadInfo.getId()) {
                    downloadInfo.setId(FetchUtils.getUniqueId(downloadInfo.getUrl(), downloadInfo.getFile()));
                }
                FetchUtils.createFileIfPossible(incrementedFileIfOriginalExists);
            }
        }
    }

    @NotNull
    public List<Download> enqueue(@NotNull List<? extends Request> list) {
        Intrinsics.checkParameterIsNotNull(list, DownloadDatabase.TABLE_NAME);
        Iterable<Request> prepareRequestListForEnqueue = prepareRequestListForEnqueue(list);
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(prepareRequestListForEnqueue, 10));
        for (Request toDownloadInfo : prepareRequestListForEnqueue) {
            DownloadInfo toDownloadInfo2 = FetchTypeConverterExtensions.toDownloadInfo(toDownloadInfo);
            toDownloadInfo2.setNamespace(this.namespace);
            toDownloadInfo2.setStatus(Status.QUEUED);
            prepareDownloadInfoForEnqueue(toDownloadInfo2);
            arrayList.add(toDownloadInfo2);
        }
        Iterable insert = this.databaseManager.insert((List) arrayList);
        arrayList = new ArrayList();
        for (Object next : insert) {
            if (((Boolean) ((Pair) next).getSecond()).booleanValue()) {
                arrayList.add(next);
            }
        }
        Iterable<Pair> iterable = (List) arrayList;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (Pair pair : iterable) {
            Logger logger = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Enqueued download ");
            stringBuilder.append((DownloadInfo) pair.getFirst());
            logger.mo4160d(stringBuilder.toString());
            arrayList2.add((DownloadInfo) pair.getFirst());
        }
        list = (List) arrayList2;
        startPriorityQueueIfNotStarted();
        return list;
    }

    private final List<Request> prepareRequestListForEnqueue(List<? extends Request> list) {
        list = CollectionsKt___CollectionsKt.asSequence(list);
        if (this.requestOptions.contains(RequestOptions.REPLACE_ALL_ON_ENQUEUE_WHERE_UNIQUE) || this.requestOptions.contains(RequestOptions.REPLACE_ALL_ON_ENQUEUE_WHERE_UNIQUE_FRESH) || this.requestOptions.contains(RequestOptions.REPLACE_ON_ENQUEUE_ID) || this.requestOptions.contains(RequestOptions.REPLACE_ON_ENQUEUE_FRESH_ID)) {
            list = SequencesKt___SequencesKt.distinctBy(list, FetchHandlerImpl$prepareRequestListForEnqueue$1.INSTANCE);
        }
        if (this.requestOptions.contains(RequestOptions.REPLACE_ALL_ON_ENQUEUE_WHERE_UNIQUE) || this.requestOptions.contains(RequestOptions.REPLACE_ALL_ON_ENQUEUE_WHERE_UNIQUE_FRESH) || this.requestOptions.contains(RequestOptions.REPLACE_ON_ENQUEUE_FILE) || this.requestOptions.contains(RequestOptions.REPLACE_ON_ENQUEUE_FRESH_FILE)) {
            list = SequencesKt___SequencesKt.distinctBy(list, FetchHandlerImpl$prepareRequestListForEnqueue$2.INSTANCE);
        }
        return SequencesKt___SequencesKt.toList(list);
    }

    @NotNull
    public List<Download> pause(@NotNull int[] iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "ids");
        startPriorityQueueIfNotStarted();
        for (int i : iArr) {
            if (isDownloading(i)) {
                cancelDownload(i);
            }
        }
        List<DownloadInfo> filterNotNull = CollectionsKt___CollectionsKt.filterNotNull(this.databaseManager.get(ArraysKt___ArraysKt.toList(iArr)));
        for (DownloadInfo downloadInfo : filterNotNull) {
            if (FetchUtils.canPauseDownload(downloadInfo)) {
                downloadInfo.setStatus(Status.PAUSED);
            }
        }
        try {
            this.databaseManager.update((List) filterNotNull);
            return filterNotNull;
        } catch (int[] iArr2) {
            this.logger.mo4163e(FetchErrorStrings.FETCH_DATABASE_ERROR, (Throwable) iArr2);
            return CollectionsKt.emptyList();
        }
    }

    @NotNull
    public List<Download> pausedGroup(int i) {
        startPriorityQueueIfNotStarted();
        for (DownloadInfo downloadInfo : this.databaseManager.getByGroup(i)) {
            if (isDownloading(downloadInfo.getId())) {
                cancelDownload(downloadInfo.getId());
            }
        }
        List<DownloadInfo> byGroup = this.databaseManager.getByGroup(i);
        for (DownloadInfo downloadInfo2 : byGroup) {
            if (FetchUtils.canPauseDownload(downloadInfo2)) {
                downloadInfo2.setStatus(Status.PAUSED);
            }
        }
        try {
            this.databaseManager.update((List) byGroup);
            return byGroup;
        } catch (int i2) {
            this.logger.mo4163e(FetchErrorStrings.FETCH_DATABASE_ERROR, (Throwable) i2);
            return CollectionsKt.emptyList();
        }
    }

    public void freeze() {
        startPriorityQueueIfNotStarted();
        this.priorityListProcessor.pause();
        this.downloadManager.cancelAll();
        FetchDatabaseExtensions.sanitize(this.databaseManager, true);
    }

    public void unfreeze() {
        startPriorityQueueIfNotStarted();
        FetchDatabaseExtensions.sanitize(this.databaseManager, true);
        this.priorityListProcessor.resume();
    }

    @NotNull
    public List<Download> resume(@NotNull int[] iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "ids");
        startPriorityQueueIfNotStarted();
        for (int i : iArr) {
            if (isDownloading(i)) {
                cancelDownload(i);
            }
        }
        List<DownloadInfo> filterNotNull = CollectionsKt___CollectionsKt.filterNotNull(this.databaseManager.get(ArraysKt___ArraysKt.toList(iArr)));
        for (DownloadInfo downloadInfo : filterNotNull) {
            if (!isDownloading(downloadInfo.getId()) && FetchUtils.canResumeDownload(downloadInfo)) {
                downloadInfo.setStatus(Status.QUEUED);
            }
        }
        try {
            this.databaseManager.update((List) filterNotNull);
            return filterNotNull;
        } catch (int[] iArr2) {
            this.logger.mo4163e(FetchErrorStrings.FETCH_DATABASE_ERROR, (Throwable) iArr2);
            return CollectionsKt.emptyList();
        }
    }

    @NotNull
    public List<Download> resumeGroup(int i) {
        startPriorityQueueIfNotStarted();
        Collection arrayList = new ArrayList();
        for (Object next : this.databaseManager.getByGroup(i)) {
            DownloadInfo downloadInfo = (DownloadInfo) next;
            Object obj = (isDownloading(downloadInfo.getId()) || !FetchUtils.canResumeDownload(downloadInfo)) ? null : 1;
            if (obj != null) {
                arrayList.add(next);
            }
        }
        List<DownloadInfo> list = (List) arrayList;
        for (DownloadInfo status : list) {
            status.setStatus(Status.QUEUED);
        }
        try {
            this.databaseManager.update((List) list);
            return list;
        } catch (int i2) {
            this.logger.mo4163e(FetchErrorStrings.FETCH_DATABASE_ERROR, (Throwable) i2);
            return CollectionsKt.emptyList();
        }
    }

    @NotNull
    public List<Download> remove(@NotNull int[] iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "ids");
        startPriorityQueueIfNotStarted();
        for (int i : iArr) {
            if (isDownloading(i)) {
                cancelDownload(i);
            }
        }
        List<DownloadInfo> filterNotNull = CollectionsKt___CollectionsKt.filterNotNull(this.databaseManager.get(ArraysKt___ArraysKt.toList(iArr)));
        this.databaseManager.delete((List) filterNotNull);
        Status status = Status.REMOVED;
        for (DownloadInfo downloadInfo : filterNotNull) {
            downloadInfo.setStatus(status);
            FetchUtils.deleteRequestTempFiles(this.fileTempDir, this.downloader, downloadInfo);
        }
        return filterNotNull;
    }

    @NotNull
    public List<Download> removeGroup(int i) {
        startPriorityQueueIfNotStarted();
        List byGroup = this.databaseManager.getByGroup(i);
        Iterable<DownloadInfo> iterable = byGroup;
        for (DownloadInfo downloadInfo : iterable) {
            if (isDownloading(downloadInfo.getId())) {
                cancelDownload(downloadInfo.getId());
            }
        }
        this.databaseManager.delete(byGroup);
        Status status = Status.REMOVED;
        for (DownloadInfo downloadInfo2 : iterable) {
            downloadInfo2.setStatus(status);
            FetchUtils.deleteRequestTempFiles(this.fileTempDir, this.downloader, downloadInfo2);
        }
        return byGroup;
    }

    @NotNull
    public List<Download> removeAll() {
        startPriorityQueueIfNotStarted();
        this.downloadManager.cancelAll();
        List<Download> list = this.databaseManager.get();
        this.databaseManager.deleteAll();
        Status status = Status.REMOVED;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            DownloadInfo downloadInfo = (DownloadInfo) it.next();
            downloadInfo.setStatus(status);
            FetchUtils.deleteRequestTempFiles(this.fileTempDir, this.downloader, downloadInfo);
        }
        return list;
    }

    @NotNull
    public List<Download> removeAllWithStatus(@NotNull Status status) {
        Intrinsics.checkParameterIsNotNull(status, NotificationCompat.CATEGORY_STATUS);
        startPriorityQueueIfNotStarted();
        List byStatus = this.databaseManager.getByStatus(status);
        Iterable<DownloadInfo> iterable = byStatus;
        for (DownloadInfo downloadInfo : iterable) {
            if (isDownloading(downloadInfo.getId())) {
                cancelDownload(downloadInfo.getId());
            }
        }
        this.databaseManager.delete(byStatus);
        Status status2 = Status.REMOVED;
        for (DownloadInfo downloadInfo2 : iterable) {
            downloadInfo2.setStatus(status2);
            FetchUtils.deleteRequestTempFiles(this.fileTempDir, this.downloader, downloadInfo2);
        }
        return byStatus;
    }

    @NotNull
    public List<Download> delete(@NotNull int[] iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "ids");
        startPriorityQueueIfNotStarted();
        for (int i : iArr) {
            if (isDownloading(i)) {
                cancelDownload(i);
            }
        }
        List<DownloadInfo> filterNotNull = CollectionsKt___CollectionsKt.filterNotNull(this.databaseManager.get(ArraysKt___ArraysKt.toList(iArr)));
        this.databaseManager.delete((List) filterNotNull);
        Status status = Status.DELETED;
        for (DownloadInfo downloadInfo : filterNotNull) {
            downloadInfo.setStatus(status);
            try {
                File file = new File(downloadInfo.getFile());
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                Logger logger = this.logger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to delete file ");
                stringBuilder.append(downloadInfo.getFile());
                logger.mo4161d(stringBuilder.toString(), e);
            }
            FetchUtils.deleteRequestTempFiles(this.fileTempDir, this.downloader, downloadInfo);
        }
        return filterNotNull;
    }

    @NotNull
    public List<Download> deleteGroup(int i) {
        startPriorityQueueIfNotStarted();
        List byGroup = this.databaseManager.getByGroup(i);
        Iterable<DownloadInfo> iterable = byGroup;
        for (DownloadInfo downloadInfo : iterable) {
            if (isDownloading(downloadInfo.getId())) {
                cancelDownload(downloadInfo.getId());
            }
        }
        this.databaseManager.delete(byGroup);
        Status status = Status.DELETED;
        for (DownloadInfo downloadInfo2 : iterable) {
            downloadInfo2.setStatus(status);
            try {
                File file = new File(downloadInfo2.getFile());
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                Logger logger = this.logger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to delete file ");
                stringBuilder.append(downloadInfo2.getFile());
                logger.mo4161d(stringBuilder.toString(), e);
            }
            FetchUtils.deleteRequestTempFiles(this.fileTempDir, this.downloader, downloadInfo2);
        }
        return byGroup;
    }

    @NotNull
    public List<Download> deleteAll() {
        startPriorityQueueIfNotStarted();
        this.downloadManager.cancelAll();
        List<Download> list = this.databaseManager.get();
        this.databaseManager.deleteAll();
        Status status = Status.DELETED;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            DownloadInfo downloadInfo = (DownloadInfo) it.next();
            downloadInfo.setStatus(status);
            try {
                File file = new File(downloadInfo.getFile());
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                Logger logger = this.logger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to delete file ");
                stringBuilder.append(downloadInfo.getFile());
                logger.mo4161d(stringBuilder.toString(), e);
            }
            FetchUtils.deleteRequestTempFiles(this.fileTempDir, this.downloader, downloadInfo);
        }
        return list;
    }

    @NotNull
    public List<Download> deleteAllWithStatus(@NotNull Status status) {
        Intrinsics.checkParameterIsNotNull(status, NotificationCompat.CATEGORY_STATUS);
        startPriorityQueueIfNotStarted();
        List byStatus = this.databaseManager.getByStatus(status);
        Iterable<DownloadInfo> iterable = byStatus;
        for (DownloadInfo downloadInfo : iterable) {
            if (isDownloading(downloadInfo.getId())) {
                cancelDownload(downloadInfo.getId());
            }
        }
        this.databaseManager.delete(byStatus);
        Status status2 = Status.DELETED;
        for (DownloadInfo downloadInfo2 : iterable) {
            downloadInfo2.setStatus(status2);
            try {
                File file = new File(downloadInfo2.getFile());
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                Logger logger = this.logger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to delete file ");
                stringBuilder.append(downloadInfo2.getFile());
                logger.mo4161d(stringBuilder.toString(), e);
            }
            FetchUtils.deleteRequestTempFiles(this.fileTempDir, this.downloader, downloadInfo2);
        }
        return byStatus;
    }

    @NotNull
    public List<Download> cancel(@NotNull int[] iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "ids");
        startPriorityQueueIfNotStarted();
        for (int i : iArr) {
            if (isDownloading(i)) {
                cancelDownload(i);
            }
        }
        List<DownloadInfo> filterNotNull = CollectionsKt___CollectionsKt.filterNotNull(this.databaseManager.get(ArraysKt___ArraysKt.toList(iArr)));
        for (DownloadInfo downloadInfo : filterNotNull) {
            if (FetchUtils.canCancelDownload(downloadInfo)) {
                downloadInfo.setStatus(Status.CANCELLED);
                downloadInfo.setError(FetchDefaults.getDefaultNoError());
            }
        }
        try {
            this.databaseManager.update((List) filterNotNull);
            return filterNotNull;
        } catch (int[] iArr2) {
            this.logger.mo4163e(FetchErrorStrings.FETCH_DATABASE_ERROR, (Throwable) iArr2);
            return CollectionsKt.emptyList();
        }
    }

    @NotNull
    public List<Download> cancelGroup(int i) {
        startPriorityQueueIfNotStarted();
        for (DownloadInfo downloadInfo : this.databaseManager.getByGroup(i)) {
            if (isDownloading(downloadInfo.getId())) {
                this.downloadManager.cancel(downloadInfo.getId());
            }
        }
        List<DownloadInfo> byGroup = this.databaseManager.getByGroup(i);
        for (DownloadInfo downloadInfo2 : byGroup) {
            if (FetchUtils.canCancelDownload(downloadInfo2)) {
                downloadInfo2.setStatus(Status.CANCELLED);
                downloadInfo2.setError(FetchDefaults.getDefaultNoError());
            }
        }
        try {
            this.databaseManager.update((List) byGroup);
            return byGroup;
        } catch (int i2) {
            this.logger.mo4163e(FetchErrorStrings.FETCH_DATABASE_ERROR, (Throwable) i2);
            return CollectionsKt.emptyList();
        }
    }

    @NotNull
    public List<Download> cancelAll() {
        startPriorityQueueIfNotStarted();
        for (DownloadInfo downloadInfo : this.databaseManager.get()) {
            if (isDownloading(downloadInfo.getId())) {
                this.downloadManager.cancel(downloadInfo.getId());
            }
        }
        List<DownloadInfo> list = this.databaseManager.get();
        for (DownloadInfo downloadInfo2 : list) {
            if (FetchUtils.canCancelDownload(downloadInfo2)) {
                downloadInfo2.setStatus(Status.CANCELLED);
                downloadInfo2.setError(FetchDefaults.getDefaultNoError());
            }
        }
        try {
            this.databaseManager.update((List) list);
            return list;
        } catch (Exception e) {
            this.logger.mo4163e(FetchErrorStrings.FETCH_DATABASE_ERROR, e);
            return CollectionsKt.emptyList();
        }
    }

    @NotNull
    public List<Download> retry(@NotNull int[] iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "ids");
        startPriorityQueueIfNotStarted();
        List<DownloadInfo> filterNotNull = CollectionsKt___CollectionsKt.filterNotNull(this.databaseManager.get(ArraysKt___ArraysKt.toList(iArr)));
        for (DownloadInfo downloadInfo : filterNotNull) {
            if (FetchUtils.canRetryDownload(downloadInfo)) {
                downloadInfo.setStatus(Status.QUEUED);
                downloadInfo.setError(FetchDefaults.getDefaultNoError());
            }
        }
        try {
            this.databaseManager.update((List) filterNotNull);
            return filterNotNull;
        } catch (int[] iArr2) {
            this.logger.mo4163e(FetchErrorStrings.FETCH_DATABASE_ERROR, (Throwable) iArr2);
            return CollectionsKt.emptyList();
        }
    }

    @Nullable
    public Download updateRequest(int i, @NotNull RequestInfo requestInfo) {
        Intrinsics.checkParameterIsNotNull(requestInfo, "requestInfo");
        startPriorityQueueIfNotStarted();
        if (this.databaseManager.get(i) != null) {
            if (isDownloading(i)) {
                cancelDownload(i);
            }
            DownloadInfo downloadInfo = this.databaseManager.get(i);
            if (downloadInfo != 0) {
                downloadInfo.setGroup(requestInfo.getGroupId());
                downloadInfo.setHeaders(requestInfo.getHeaders());
                downloadInfo.setPriority(requestInfo.getPriority());
                downloadInfo.setNetworkType(requestInfo.getNetworkType());
                this.databaseManager.update(downloadInfo);
                return downloadInfo;
            }
        }
        return 0;
    }

    @NotNull
    public List<Download> getDownloads() {
        startPriorityQueueIfNotStarted();
        return this.databaseManager.get();
    }

    @Nullable
    public Download getDownload(int i) {
        startPriorityQueueIfNotStarted();
        return this.databaseManager.get(i);
    }

    @NotNull
    public List<Download> getDownloads(@NotNull List<Integer> list) {
        Intrinsics.checkParameterIsNotNull(list, "idList");
        startPriorityQueueIfNotStarted();
        List<Download> arrayList = new ArrayList();
        list = CollectionsKt___CollectionsKt.filterNotNull(this.databaseManager.get((List) list)).iterator();
        while (list.hasNext()) {
            arrayList.add((DownloadInfo) list.next());
        }
        return arrayList;
    }

    @NotNull
    public List<Download> getDownloadsInGroup(int i) {
        startPriorityQueueIfNotStarted();
        return this.databaseManager.getByGroup(i);
    }

    @NotNull
    public List<Download> getDownloadsWithStatus(@NotNull Status status) {
        Intrinsics.checkParameterIsNotNull(status, NotificationCompat.CATEGORY_STATUS);
        startPriorityQueueIfNotStarted();
        return this.databaseManager.getByStatus(status);
    }

    @NotNull
    public List<Download> getDownloadsInGroupWithStatus(int i, @NotNull Status status) {
        Intrinsics.checkParameterIsNotNull(status, NotificationCompat.CATEGORY_STATUS);
        startPriorityQueueIfNotStarted();
        return this.databaseManager.getDownloadsInGroupWithStatus(i, status);
    }

    public void close() {
        if (!this.isTerminating) {
            this.isTerminating = true;
            getFetchListenerProvider().getListeners().clear();
            this.priorityListProcessor.stop();
            this.downloadManager.terminateAllDownloads();
            this.handler.post(new FetchHandlerImpl$close$1(this));
            FetchModulesBuilder.INSTANCE.removeActiveFetchHandlerNamespaceInstance(this.namespace);
        }
    }

    public void setGlobalNetworkType(@NotNull NetworkType networkType) {
        Intrinsics.checkParameterIsNotNull(networkType, "networkType");
        startPriorityQueueIfNotStarted();
        this.priorityListProcessor.setGlobalNetworkType(networkType);
        this.downloadManager.cancelAll();
        FetchDatabaseExtensions.sanitize(this.databaseManager, true);
    }

    public void enableLogging(boolean z) {
        startPriorityQueueIfNotStarted();
        Logger logger = this.logger;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Enable logging - ");
        stringBuilder.append(z);
        logger.mo4160d(stringBuilder.toString());
        this.logger.setEnabled(z);
    }

    public void addListener(@NotNull FetchListener fetchListener) {
        Intrinsics.checkParameterIsNotNull(fetchListener, CastExtraArgs.LISTENER);
        startPriorityQueueIfNotStarted();
        getFetchListenerProvider().getListeners().add(fetchListener);
        Logger logger = this.logger;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Added listener ");
        stringBuilder.append(fetchListener);
        logger.mo4160d(stringBuilder.toString());
    }

    public void removeListener(@NotNull FetchListener fetchListener) {
        Intrinsics.checkParameterIsNotNull(fetchListener, CastExtraArgs.LISTENER);
        startPriorityQueueIfNotStarted();
        Iterator it = getFetchListenerProvider().getListeners().iterator();
        while (it.hasNext()) {
            if (Intrinsics.areEqual((FetchListener) it.next(), (Object) fetchListener)) {
                Logger logger = this.logger;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Removed listener ");
                stringBuilder.append(fetchListener);
                logger.mo4160d(stringBuilder.toString());
                it.remove();
                return;
            }
        }
    }

    public boolean isDownloading(int i) {
        startPriorityQueueIfNotStarted();
        return this.downloadManager.contains(i);
    }

    public boolean cancelDownload(int i) {
        startPriorityQueueIfNotStarted();
        return this.downloadManager.cancel(i);
    }

    private final void startPriorityQueueIfNotStarted() {
        if (this.priorityListProcessor.isStopped() && !this.isTerminating) {
            this.priorityListProcessor.start();
        }
        if (this.priorityListProcessor.isPaused() && !this.isTerminating) {
            this.priorityListProcessor.resume();
        }
    }
}
