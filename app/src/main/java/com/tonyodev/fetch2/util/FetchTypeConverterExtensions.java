package com.tonyodev.fetch2.util;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2.database.DownloadInfo;
import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0003¨\u0006\u0004"}, d2 = {"toDownloadInfo", "Lcom/tonyodev/fetch2/database/DownloadInfo;", "Lcom/tonyodev/fetch2/Download;", "Lcom/tonyodev/fetch2/Request;", "fetch2_release"}, k = 2, mv = {1, 1, 10})
@JvmName(name = "FetchTypeConverterExtensions")
/* compiled from: TypeConverterExtensions.kt */
public final class FetchTypeConverterExtensions {
    @NotNull
    public static final DownloadInfo toDownloadInfo(@NotNull Request request) {
        Intrinsics.checkParameterIsNotNull(request, "$receiver");
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setId(request.getId());
        downloadInfo.setUrl(request.getUrl());
        downloadInfo.setFile(request.getFile());
        downloadInfo.setPriority(request.getPriority());
        downloadInfo.setHeaders(MapsKt__MapsKt.toMap(request.getHeaders()));
        downloadInfo.setGroup(request.getGroupId());
        downloadInfo.setNetworkType(request.getNetworkType());
        downloadInfo.setStatus(FetchDefaults.getDefaultStatus());
        downloadInfo.setError(FetchDefaults.getDefaultNoError());
        downloadInfo.setDownloaded(0);
        downloadInfo.setTag(request.getTag());
        return downloadInfo;
    }

    @NotNull
    public static final DownloadInfo toDownloadInfo(@NotNull Download download) {
        Intrinsics.checkParameterIsNotNull(download, "$receiver");
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setId(download.getId());
        downloadInfo.setNamespace(download.getNamespace());
        downloadInfo.setUrl(download.getUrl());
        downloadInfo.setFile(download.getFile());
        downloadInfo.setGroup(download.getGroup());
        downloadInfo.setPriority(download.getPriority());
        downloadInfo.setHeaders(MapsKt__MapsKt.toMap(download.getHeaders()));
        downloadInfo.setDownloaded(download.getDownloaded());
        downloadInfo.setTotal(download.getTotal());
        downloadInfo.setStatus(download.getStatus());
        downloadInfo.setNetworkType(download.getNetworkType());
        downloadInfo.setError(download.getError());
        downloadInfo.setCreated(download.getCreated());
        downloadInfo.setTag(download.getTag());
        return downloadInfo;
    }
}
