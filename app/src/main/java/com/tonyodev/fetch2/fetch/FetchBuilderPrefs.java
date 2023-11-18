package com.tonyodev.fetch2.fetch;

import android.content.Context;
import com.google.android.gms.common.server.FavaDiagnosticsEntity;
import com.tonyodev.fetch2.Downloader;
import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.RequestOptions;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\u0018\u0018\u00002\u00020\u0001Bs\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u0007\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\f\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\f\u0012\u0006\u0010\u0015\u001a\u00020\f\u0012\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017¢\u0006\u0002\u0010\u0019R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u0014\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\n\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u001fR\u0011\u0010\u000e\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0011\u0010\u0010\u001a\u00020\u0011¢\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010\r\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001dR\u0011\u0010\u0012\u001a\u00020\u0013¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u0011\u0010\u000b\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001dR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017¢\u0006\b\n\u0000\u001a\u0004\b-\u0010.R\u0011\u0010\u0015\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b/\u0010\u001d¨\u00060"}, d2 = {"Lcom/tonyodev/fetch2/fetch/FetchBuilderPrefs;", "", "appContext", "Landroid/content/Context;", "namespace", "", "concurrentLimit", "", "progressReportingIntervalMillis", "", "downloadBufferSizeBytes", "loggingEnabled", "", "inMemoryDatabaseEnabled", "downloader", "Lcom/tonyodev/fetch2/Downloader;", "globalNetworkType", "Lcom/tonyodev/fetch2/NetworkType;", "logger", "Lcom/tonyodev/fetch2/Logger;", "autoStart", "retryOnNetworkGain", "requestOptions", "", "Lcom/tonyodev/fetch2/RequestOptions;", "(Landroid/content/Context;Ljava/lang/String;IJIZZLcom/tonyodev/fetch2/Downloader;Lcom/tonyodev/fetch2/NetworkType;Lcom/tonyodev/fetch2/Logger;ZZLjava/util/Set;)V", "getAppContext", "()Landroid/content/Context;", "getAutoStart", "()Z", "getConcurrentLimit", "()I", "getDownloadBufferSizeBytes", "getDownloader", "()Lcom/tonyodev/fetch2/Downloader;", "getGlobalNetworkType", "()Lcom/tonyodev/fetch2/NetworkType;", "getInMemoryDatabaseEnabled", "getLogger", "()Lcom/tonyodev/fetch2/Logger;", "getLoggingEnabled", "getNamespace", "()Ljava/lang/String;", "getProgressReportingIntervalMillis", "()J", "getRequestOptions", "()Ljava/util/Set;", "getRetryOnNetworkGain", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: FetchBuilderPrefs.kt */
public final class FetchBuilderPrefs {
    @NotNull
    private final Context appContext;
    private final boolean autoStart;
    private final int concurrentLimit;
    private final int downloadBufferSizeBytes;
    @NotNull
    private final Downloader downloader;
    @NotNull
    private final NetworkType globalNetworkType;
    private final boolean inMemoryDatabaseEnabled;
    @NotNull
    private final Logger logger;
    private final boolean loggingEnabled;
    @NotNull
    private final String namespace;
    private final long progressReportingIntervalMillis;
    @NotNull
    private final Set<RequestOptions> requestOptions;
    private final boolean retryOnNetworkGain;

    public FetchBuilderPrefs(@NotNull Context context, @NotNull String str, int i, long j, int i2, boolean z, boolean z2, @NotNull Downloader downloader, @NotNull NetworkType networkType, @NotNull Logger logger, boolean z3, boolean z4, @NotNull Set<? extends RequestOptions> set) {
        Intrinsics.checkParameterIsNotNull(context, "appContext");
        Intrinsics.checkParameterIsNotNull(str, FavaDiagnosticsEntity.EXTRA_NAMESPACE);
        Intrinsics.checkParameterIsNotNull(downloader, "downloader");
        Intrinsics.checkParameterIsNotNull(networkType, "globalNetworkType");
        Intrinsics.checkParameterIsNotNull(logger, "logger");
        Intrinsics.checkParameterIsNotNull(set, "requestOptions");
        this.appContext = context;
        this.namespace = str;
        this.concurrentLimit = i;
        this.progressReportingIntervalMillis = j;
        this.downloadBufferSizeBytes = i2;
        this.loggingEnabled = z;
        this.inMemoryDatabaseEnabled = z2;
        this.downloader = downloader;
        this.globalNetworkType = networkType;
        this.logger = logger;
        this.autoStart = z3;
        this.retryOnNetworkGain = z4;
        this.requestOptions = set;
    }

    @NotNull
    public final Context getAppContext() {
        return this.appContext;
    }

    @NotNull
    public final String getNamespace() {
        return this.namespace;
    }

    public final int getConcurrentLimit() {
        return this.concurrentLimit;
    }

    public final long getProgressReportingIntervalMillis() {
        return this.progressReportingIntervalMillis;
    }

    public final int getDownloadBufferSizeBytes() {
        return this.downloadBufferSizeBytes;
    }

    public final boolean getLoggingEnabled() {
        return this.loggingEnabled;
    }

    public final boolean getInMemoryDatabaseEnabled() {
        return this.inMemoryDatabaseEnabled;
    }

    @NotNull
    public final Downloader getDownloader() {
        return this.downloader;
    }

    @NotNull
    public final NetworkType getGlobalNetworkType() {
        return this.globalNetworkType;
    }

    @NotNull
    public final Logger getLogger() {
        return this.logger;
    }

    public final boolean getAutoStart() {
        return this.autoStart;
    }

    public final boolean getRetryOnNetworkGain() {
        return this.retryOnNetworkGain;
    }

    @NotNull
    public final Set<RequestOptions> getRequestOptions() {
        return this.requestOptions;
    }
}
