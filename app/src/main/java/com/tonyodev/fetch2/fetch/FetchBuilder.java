package com.tonyodev.fetch2.fetch;

import android.content.Context;
import com.google.android.gms.common.server.FavaDiagnosticsEntity;
import com.tonyodev.fetch2.Downloader;
import com.tonyodev.fetch2.FetchLogger;
import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.RequestOptions;
import com.tonyodev.fetch2.exception.FetchException;
import com.tonyodev.fetch2.exception.FetchException.Code;
import com.tonyodev.fetch2.util.FetchDefaults;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\n\b&\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u0001*\u0006\b\u0001\u0010\u0002 \u00012\u00020\u0003B\u0015\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ+\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00002\u0012\u0010\u001a\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001c0\u001f\"\u00020\u001c¢\u0006\u0002\u0010 J\r\u0010!\u001a\u00028\u0001H&¢\u0006\u0002\u0010\"J\u001a\u0010#\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00002\u0006\u0010$\u001a\u00020\fJ\u001a\u0010%\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00002\u0006\u0010$\u001a\u00020\fJ\u001a\u0010&\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00002\u0006\u0010$\u001a\u00020\fJ\u001a\u0010'\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00002\u0006\u0010$\u001a\u00020\fJ\u0006\u0010(\u001a\u00020)J\u001a\u0010*\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00002\u0006\u0010+\u001a\u00020\u000eJ\u001a\u0010,\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00002\u0006\u0010-\u001a\u00020\u000eJ\u001a\u0010.\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00002\u0006\u0010\u0010\u001a\u00020\u0011J\u001a\u0010/\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00002\u0006\u00100\u001a\u00020\u0013J\u001a\u00101\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00002\u0006\u0010\u0015\u001a\u00020\u0016J\u001a\u00102\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00002\u0006\u0010\u0018\u001a\u00020\u0019R\u0016\u0010\t\u001a\n \n*\u0004\u0018\u00010\u00050\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000¨\u00063"}, d2 = {"Lcom/tonyodev/fetch2/fetch/FetchBuilder;", "B", "F", "", "context", "Landroid/content/Context;", "namespace", "", "(Landroid/content/Context;Ljava/lang/String;)V", "appContext", "kotlin.jvm.PlatformType", "autoStart", "", "concurrentLimit", "", "downloadBufferSizeBytes", "downloader", "Lcom/tonyodev/fetch2/Downloader;", "globalNetworkType", "Lcom/tonyodev/fetch2/NetworkType;", "inMemoryDatabaseEnabled", "logger", "Lcom/tonyodev/fetch2/Logger;", "loggingEnabled", "progressReportingIntervalMillis", "", "requestOptions", "", "Lcom/tonyodev/fetch2/RequestOptions;", "retryOnNetworkGain", "addRequestOptions", "", "([Lcom/tonyodev/fetch2/RequestOptions;)Lcom/tonyodev/fetch2/fetch/FetchBuilder;", "build", "()Ljava/lang/Object;", "enableAutoStart", "enabled", "enableLogging", "enableRetryOnNetworkGain", "enabledInMemoryDatabase", "getBuilderPrefs", "Lcom/tonyodev/fetch2/fetch/FetchBuilderPrefs;", "setDownloadBufferSize", "bytes", "setDownloadConcurrentLimit", "downloadConcurrentLimit", "setDownloader", "setGlobalNetworkType", "networkType", "setLogger", "setProgressReportingInterval", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: FetchBuilder.kt */
public abstract class FetchBuilder<B, F> {
    private final Context appContext;
    private boolean autoStart = true;
    private int concurrentLimit = 1;
    private int downloadBufferSizeBytes = 8192;
    private Downloader downloader = FetchDefaults.getDefaultDownloader();
    private NetworkType globalNetworkType = FetchDefaults.getDefaultGlobalNetworkType();
    private boolean inMemoryDatabaseEnabled;
    private Logger logger = FetchDefaults.getDefaultLogger();
    private boolean loggingEnabled;
    private final String namespace;
    private long progressReportingIntervalMillis = 2000;
    private Set<RequestOptions> requestOptions = ((Set) new LinkedHashSet());
    private boolean retryOnNetworkGain;

    public abstract F build();

    public FetchBuilder(@NotNull Context context, @NotNull String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, FavaDiagnosticsEntity.EXTRA_NAMESPACE);
        this.namespace = str;
        this.appContext = context.getApplicationContext();
    }

    @NotNull
    public final FetchBuilder<B, F> setDownloader(@NotNull Downloader downloader) {
        Intrinsics.checkParameterIsNotNull(downloader, "downloader");
        this.downloader = downloader;
        return this;
    }

    @NotNull
    public final FetchBuilder<B, F> setProgressReportingInterval(long j) {
        if (j < 0) {
            throw ((Throwable) new FetchException("progressReportingIntervalMillis cannot be less than 0", Code.ILLEGAL_ARGUMENT));
        }
        this.progressReportingIntervalMillis = j;
        return this;
    }

    @NotNull
    public final FetchBuilder<B, F> setDownloadConcurrentLimit(int i) {
        if (i < 1) {
            throw ((Throwable) new FetchException("Concurrent limit cannot be less than 1", Code.ILLEGAL_ARGUMENT));
        }
        this.concurrentLimit = i;
        return this;
    }

    @NotNull
    public final FetchBuilder<B, F> setGlobalNetworkType(@NotNull NetworkType networkType) {
        Intrinsics.checkParameterIsNotNull(networkType, "networkType");
        this.globalNetworkType = networkType;
        return this;
    }

    @NotNull
    public final FetchBuilder<B, F> enableLogging(boolean z) {
        this.loggingEnabled = z;
        return this;
    }

    @NotNull
    public final FetchBuilder<B, F> enabledInMemoryDatabase(boolean z) {
        this.inMemoryDatabaseEnabled = z;
        return this;
    }

    @NotNull
    public final FetchBuilder<B, F> setDownloadBufferSize(int i) {
        if (i < 1) {
            throw ((Throwable) new FetchException("Buffer size cannot be less than 1.", Code.ILLEGAL_ARGUMENT));
        }
        this.downloadBufferSizeBytes = i;
        return this;
    }

    @NotNull
    public final FetchBuilder<B, F> setLogger(@NotNull Logger logger) {
        Intrinsics.checkParameterIsNotNull(logger, "logger");
        this.logger = logger;
        return this;
    }

    @NotNull
    public final FetchBuilder<B, F> enableAutoStart(boolean z) {
        this.autoStart = z;
        return this;
    }

    @NotNull
    public final FetchBuilder<B, F> enableRetryOnNetworkGain(boolean z) {
        this.retryOnNetworkGain = z;
        return this;
    }

    @NotNull
    public final FetchBuilder<B, F> addRequestOptions(@NotNull RequestOptions... requestOptionsArr) {
        Intrinsics.checkParameterIsNotNull(requestOptionsArr, "requestOptions");
        CollectionsKt.addAll(this.requestOptions, requestOptionsArr);
        return this;
    }

    @NotNull
    public final FetchBuilderPrefs getBuilderPrefs() {
        Logger logger = this.logger;
        if (logger instanceof FetchLogger) {
            logger.setEnabled(r0.loggingEnabled);
            ((FetchLogger) logger).setTag(r0.namespace);
        } else {
            r0.logger.setEnabled(r0.loggingEnabled);
        }
        Context context = r0.appContext;
        Intrinsics.checkExpressionValueIsNotNull(context, "appContext");
        return new FetchBuilderPrefs(context, r0.namespace, r0.concurrentLimit, r0.progressReportingIntervalMillis, r0.downloadBufferSizeBytes, r0.loggingEnabled, r0.inMemoryDatabaseEnabled, r0.downloader, r0.globalNetworkType, logger, r0.autoStart, r0.retryOnNetworkGain, r0.requestOptions);
    }
}
