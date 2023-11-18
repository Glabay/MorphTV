package com.tonyodev.fetch2.fetch;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.google.android.gms.common.server.FavaDiagnosticsEntity;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.database.DatabaseManager;
import com.tonyodev.fetch2.database.DatabaseManagerImpl;
import com.tonyodev.fetch2.database.DownloadDatabase;
import com.tonyodev.fetch2.database.migration.Migration;
import com.tonyodev.fetch2.downloader.DownloadManager;
import com.tonyodev.fetch2.downloader.DownloadManagerImpl;
import com.tonyodev.fetch2.exception.FetchException;
import com.tonyodev.fetch2.exception.FetchException.Code;
import com.tonyodev.fetch2.helper.DownloadInfoUpdater;
import com.tonyodev.fetch2.helper.PriorityListProcessor;
import com.tonyodev.fetch2.helper.PriorityListProcessorImpl;
import com.tonyodev.fetch2.provider.DownloadProvider;
import com.tonyodev.fetch2.provider.ListenerProvider;
import com.tonyodev.fetch2.provider.NetworkInfoProvider;
import com.tonyodev.fetch2.util.FetchUtils;
import java.util.HashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\u000fB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0005R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/tonyodev/fetch2/fetch/FetchModulesBuilder;", "", "()V", "activeFetchHandlerPool", "", "", "Lcom/tonyodev/fetch2/fetch/FetchModulesBuilder$Modules;", "lock", "Ljava/lang/Object;", "buildModulesFromPrefs", "prefs", "Lcom/tonyodev/fetch2/fetch/FetchBuilderPrefs;", "removeActiveFetchHandlerNamespaceInstance", "", "namespace", "Modules", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: FetchModulesBuilder.kt */
public final class FetchModulesBuilder {
    public static final FetchModulesBuilder INSTANCE = new FetchModulesBuilder();
    private static final Map<String, Modules> activeFetchHandlerPool = new HashMap();
    private static final Object lock = new Object();

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u0012¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0015\u001a\u00020\u0016¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0019\u001a\u00020\u001a¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u001d\u001a\u00020\u001e¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0019\u0010!\u001a\b\u0012\u0004\u0012\u00020#0\"¢\u0006\n\n\u0002\u0010&\u001a\u0004\b$\u0010%R\u0011\u0010'\u001a\u00020(¢\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0017\u0010-\u001a\b\u0012\u0004\u0012\u00020/0.¢\u0006\b\n\u0000\u001a\u0004\b0\u00101R\u0011\u00102\u001a\u00020\u001e¢\u0006\b\n\u0000\u001a\u0004\b3\u0010 ¨\u00064"}, d2 = {"Lcom/tonyodev/fetch2/fetch/FetchModulesBuilder$Modules;", "", "prefs", "Lcom/tonyodev/fetch2/fetch/FetchBuilderPrefs;", "(Lcom/tonyodev/fetch2/fetch/FetchBuilderPrefs;)V", "databaseManager", "Lcom/tonyodev/fetch2/database/DatabaseManager;", "getDatabaseManager", "()Lcom/tonyodev/fetch2/database/DatabaseManager;", "downloadInfoUpdater", "Lcom/tonyodev/fetch2/helper/DownloadInfoUpdater;", "getDownloadInfoUpdater", "()Lcom/tonyodev/fetch2/helper/DownloadInfoUpdater;", "downloadManager", "Lcom/tonyodev/fetch2/downloader/DownloadManager;", "getDownloadManager", "()Lcom/tonyodev/fetch2/downloader/DownloadManager;", "downloadProvider", "Lcom/tonyodev/fetch2/provider/DownloadProvider;", "getDownloadProvider", "()Lcom/tonyodev/fetch2/provider/DownloadProvider;", "fetchHandler", "Lcom/tonyodev/fetch2/fetch/FetchHandler;", "getFetchHandler", "()Lcom/tonyodev/fetch2/fetch/FetchHandler;", "fetchListenerProvider", "Lcom/tonyodev/fetch2/provider/ListenerProvider;", "getFetchListenerProvider", "()Lcom/tonyodev/fetch2/provider/ListenerProvider;", "handler", "Landroid/os/Handler;", "getHandler", "()Landroid/os/Handler;", "migrations", "", "Lcom/tonyodev/fetch2/database/migration/Migration;", "getMigrations", "()[Lcom/tonyodev/fetch2/database/migration/Migration;", "[Lcom/tonyodev/fetch2/database/migration/Migration;", "networkInfoProvider", "Lcom/tonyodev/fetch2/provider/NetworkInfoProvider;", "getNetworkInfoProvider", "()Lcom/tonyodev/fetch2/provider/NetworkInfoProvider;", "getPrefs", "()Lcom/tonyodev/fetch2/fetch/FetchBuilderPrefs;", "priorityListProcessor", "Lcom/tonyodev/fetch2/helper/PriorityListProcessor;", "Lcom/tonyodev/fetch2/Download;", "getPriorityListProcessor", "()Lcom/tonyodev/fetch2/helper/PriorityListProcessor;", "uiHandler", "getUiHandler", "fetch2_release"}, k = 1, mv = {1, 1, 10})
    /* compiled from: FetchModulesBuilder.kt */
    public static final class Modules {
        @NotNull
        private final DatabaseManager databaseManager;
        @NotNull
        private final DownloadInfoUpdater downloadInfoUpdater;
        @NotNull
        private final DownloadManager downloadManager;
        @NotNull
        private final DownloadProvider downloadProvider;
        @NotNull
        private final FetchHandler fetchHandler;
        @NotNull
        private final ListenerProvider fetchListenerProvider;
        @NotNull
        private final Handler handler;
        @NotNull
        private final Migration[] migrations = DownloadDatabase.Companion.getMigrations();
        @NotNull
        private final NetworkInfoProvider networkInfoProvider;
        @NotNull
        private final FetchBuilderPrefs prefs;
        @NotNull
        private final PriorityListProcessor<Download> priorityListProcessor;
        @NotNull
        private final Handler uiHandler = new Handler(Looper.getMainLooper());

        public Modules(@NotNull FetchBuilderPrefs fetchBuilderPrefs) {
            FetchBuilderPrefs fetchBuilderPrefs2 = fetchBuilderPrefs;
            Intrinsics.checkParameterIsNotNull(fetchBuilderPrefs2, "prefs");
            this.prefs = fetchBuilderPrefs2;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("fetch_");
            stringBuilder.append(this.prefs.getNamespace());
            HandlerThread handlerThread = new HandlerThread(stringBuilder.toString());
            handlerThread.start();
            this.handler = new Handler(handlerThread.getLooper());
            this.fetchListenerProvider = new ListenerProvider();
            this.networkInfoProvider = new NetworkInfoProvider(this.prefs.getAppContext());
            this.databaseManager = new DatabaseManagerImpl(this.prefs.getAppContext(), this.prefs.getNamespace(), this.prefs.getInMemoryDatabaseEnabled(), this.prefs.getLogger(), this.migrations);
            this.downloadProvider = new DownloadProvider(this.databaseManager);
            this.downloadInfoUpdater = new DownloadInfoUpdater(this.databaseManager);
            this.downloadManager = new DownloadManagerImpl(this.prefs.getDownloader(), this.prefs.getConcurrentLimit(), this.prefs.getProgressReportingIntervalMillis(), this.prefs.getDownloadBufferSizeBytes(), this.prefs.getLogger(), this.networkInfoProvider, this.prefs.getRetryOnNetworkGain(), this.fetchListenerProvider, this.uiHandler, this.downloadInfoUpdater, this.prefs.getRequestOptions(), FetchUtils.getFileTempDir(this.prefs.getAppContext()));
            this.priorityListProcessor = new PriorityListProcessorImpl(this.handler, this.downloadProvider, this.downloadManager, this.networkInfoProvider, this.prefs.getLogger());
            this.priorityListProcessor.setGlobalNetworkType(this.prefs.getGlobalNetworkType());
            this.fetchHandler = new FetchHandlerImpl(this.prefs.getNamespace(), this.databaseManager, this.downloadManager, this.priorityListProcessor, this.fetchListenerProvider, this.handler, this.prefs.getLogger(), this.prefs.getAutoStart(), this.prefs.getRequestOptions(), this.prefs.getDownloader(), FetchUtils.getFileTempDir(this.prefs.getAppContext()));
        }

        @NotNull
        public final FetchBuilderPrefs getPrefs() {
            return this.prefs;
        }

        @NotNull
        public final Handler getUiHandler() {
            return this.uiHandler;
        }

        @NotNull
        public final Handler getHandler() {
            return this.handler;
        }

        @NotNull
        public final ListenerProvider getFetchListenerProvider() {
            return this.fetchListenerProvider;
        }

        @NotNull
        public final DownloadManager getDownloadManager() {
            return this.downloadManager;
        }

        @NotNull
        public final DatabaseManager getDatabaseManager() {
            return this.databaseManager;
        }

        @NotNull
        public final PriorityListProcessor<Download> getPriorityListProcessor() {
            return this.priorityListProcessor;
        }

        @NotNull
        public final FetchHandler getFetchHandler() {
            return this.fetchHandler;
        }

        @NotNull
        public final NetworkInfoProvider getNetworkInfoProvider() {
            return this.networkInfoProvider;
        }

        @NotNull
        public final DownloadProvider getDownloadProvider() {
            return this.downloadProvider;
        }

        @NotNull
        public final DownloadInfoUpdater getDownloadInfoUpdater() {
            return this.downloadInfoUpdater;
        }

        @NotNull
        public final Migration[] getMigrations() {
            return this.migrations;
        }
    }

    private FetchModulesBuilder() {
    }

    @NotNull
    public final Modules buildModulesFromPrefs(@NotNull FetchBuilderPrefs fetchBuilderPrefs) {
        Modules modules;
        Intrinsics.checkParameterIsNotNull(fetchBuilderPrefs, "prefs");
        synchronized (lock) {
            if (((Modules) activeFetchHandlerPool.get(fetchBuilderPrefs.getNamespace())) != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Namespace:");
                stringBuilder.append(fetchBuilderPrefs.getNamespace());
                stringBuilder.append(" already exists. You cannot have more than one active instance of Fetch with the same namespace. Did your forget to call close the old instance?");
                throw new FetchException(stringBuilder.toString(), Code.FETCH_INSTANCE_WITH_NAMESPACE_ALREADY_EXIST);
            }
            modules = new Modules(fetchBuilderPrefs);
            activeFetchHandlerPool.put(fetchBuilderPrefs.getNamespace(), modules);
        }
        return modules;
    }

    public final void removeActiveFetchHandlerNamespaceInstance(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, FavaDiagnosticsEntity.EXTRA_NAMESPACE);
        synchronized (lock) {
            Modules modules = (Modules) activeFetchHandlerPool.remove(str);
        }
    }
}
