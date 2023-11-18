package com.tonyodev.fetch2.util;

import com.tonyodev.fetch2.Downloader;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.FetchLogger;
import com.tonyodev.fetch2.HttpUrlConnectionDownloader;
import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Status;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000X\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u000b\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000\"\u000e\u0010\f\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\r\u001a\u00020\u000eXT¢\u0006\u0002\n\u0000\"\u000e\u0010\u000f\u001a\u00020\u000eXT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0010\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000\"\u0011\u0010\u0011\u001a\u00020\u0012¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u001d\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000e0\u0016¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0011\u0010\u0019\u001a\u00020\u001a¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0011\u0010\u001d\u001a\u00020\u001e¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0011\u0010!\u001a\u00020\u001a¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001c\"\u0011\u0010#\u001a\u00020$¢\u0006\b\n\u0000\u001a\u0004\b%\u0010&\"\u0011\u0010'\u001a\u00020(¢\u0006\b\n\u0000\u001a\u0004\b)\u0010*\"\u0011\u0010+\u001a\u00020,¢\u0006\b\n\u0000\u001a\u0004\b-\u0010.¨\u0006/"}, d2 = {"DEFAULT_AUTO_START", "", "DEFAULT_CONCURRENT_LIMIT", "", "DEFAULT_DOWNLOAD_BUFFER_SIZE_BYTES", "DEFAULT_DOWNLOAD_SPEED_REPORTING_INTERVAL_IN_MILLISECONDS", "", "DEFAULT_FILE_SLICE_NO_LIMIT_SET", "DEFAULT_GROUP_ID", "DEFAULT_IN_MEMORY_DATABASE_ENABLED", "DEFAULT_LOGGING_ENABLED", "DEFAULT_PROGRESS_REPORTING_INTERVAL_IN_MILLISECONDS", "DEFAULT_RETRY_ON_NETWORK_GAIN", "DEFAULT_TAG", "", "EMPTY_JSON_OBJECT_STRING", "PRIORITY_QUEUE_INTERVAL_IN_MILLISECONDS", "defaultDownloader", "Lcom/tonyodev/fetch2/Downloader;", "getDefaultDownloader", "()Lcom/tonyodev/fetch2/Downloader;", "defaultEmptyHeaderMap", "", "getDefaultEmptyHeaderMap", "()Ljava/util/Map;", "defaultGlobalNetworkType", "Lcom/tonyodev/fetch2/NetworkType;", "getDefaultGlobalNetworkType", "()Lcom/tonyodev/fetch2/NetworkType;", "defaultLogger", "Lcom/tonyodev/fetch2/Logger;", "getDefaultLogger", "()Lcom/tonyodev/fetch2/Logger;", "defaultNetworkType", "getDefaultNetworkType", "defaultNoError", "Lcom/tonyodev/fetch2/Error;", "getDefaultNoError", "()Lcom/tonyodev/fetch2/Error;", "defaultPriority", "Lcom/tonyodev/fetch2/Priority;", "getDefaultPriority", "()Lcom/tonyodev/fetch2/Priority;", "defaultStatus", "Lcom/tonyodev/fetch2/Status;", "getDefaultStatus", "()Lcom/tonyodev/fetch2/Status;", "fetch2_release"}, k = 2, mv = {1, 1, 10})
@JvmName(name = "FetchDefaults")
/* compiled from: Defaults.kt */
public final class FetchDefaults {
    public static final boolean DEFAULT_AUTO_START = true;
    public static final int DEFAULT_CONCURRENT_LIMIT = 1;
    public static final int DEFAULT_DOWNLOAD_BUFFER_SIZE_BYTES = 8192;
    public static final long DEFAULT_DOWNLOAD_SPEED_REPORTING_INTERVAL_IN_MILLISECONDS = 1000;
    public static final int DEFAULT_FILE_SLICE_NO_LIMIT_SET = -1;
    public static final int DEFAULT_GROUP_ID = 0;
    public static final boolean DEFAULT_IN_MEMORY_DATABASE_ENABLED = false;
    public static final boolean DEFAULT_LOGGING_ENABLED = false;
    public static final long DEFAULT_PROGRESS_REPORTING_INTERVAL_IN_MILLISECONDS = 2000;
    public static final boolean DEFAULT_RETRY_ON_NETWORK_GAIN = false;
    @NotNull
    public static final String DEFAULT_TAG = "fetch2";
    @NotNull
    public static final String EMPTY_JSON_OBJECT_STRING = "{}";
    public static final long PRIORITY_QUEUE_INTERVAL_IN_MILLISECONDS = 500;
    @NotNull
    private static final Downloader defaultDownloader = new HttpUrlConnectionDownloader(null, null, 3, null);
    @NotNull
    private static final Map<String, String> defaultEmptyHeaderMap = MapsKt__MapsKt.emptyMap();
    @NotNull
    private static final NetworkType defaultGlobalNetworkType = NetworkType.GLOBAL_OFF;
    @NotNull
    private static final Logger defaultLogger = new FetchLogger(false, DEFAULT_TAG);
    @NotNull
    private static final NetworkType defaultNetworkType = NetworkType.ALL;
    @NotNull
    private static final Error defaultNoError = Error.NONE;
    @NotNull
    private static final Priority defaultPriority = Priority.NORMAL;
    @NotNull
    private static final Status defaultStatus = Status.NONE;

    @NotNull
    public static final Map<String, String> getDefaultEmptyHeaderMap() {
        return defaultEmptyHeaderMap;
    }

    @NotNull
    public static final NetworkType getDefaultNetworkType() {
        return defaultNetworkType;
    }

    @NotNull
    public static final NetworkType getDefaultGlobalNetworkType() {
        return defaultGlobalNetworkType;
    }

    @NotNull
    public static final Priority getDefaultPriority() {
        return defaultPriority;
    }

    @NotNull
    public static final Error getDefaultNoError() {
        return defaultNoError;
    }

    @NotNull
    public static final Status getDefaultStatus() {
        return defaultStatus;
    }

    @NotNull
    public static final Downloader getDefaultDownloader() {
        return defaultDownloader;
    }

    @NotNull
    public static final Logger getDefaultLogger() {
        return defaultLogger;
    }
}
