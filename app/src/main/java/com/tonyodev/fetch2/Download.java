package com.tonyodev.fetch2;

import java.util.Map;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\bf\u0018\u00002\u00020\u0001J\b\u00104\u001a\u00020\u0000H&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005R\u0012\u0010\b\u001a\u00020\tX¦\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0012\u0010\f\u001a\u00020\rX¦\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0012\u0010\u0010\u001a\u00020\u0011X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u001e\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\r0\u0015X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u0012\u0010\u0018\u001a\u00020\u0011X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u0013R\u0012\u0010\u001a\u001a\u00020\rX¦\u0004¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u000fR\u0012\u0010\u001c\u001a\u00020\u001dX¦\u0004¢\u0006\u0006\u001a\u0004\b\u001e\u0010\u001fR\u0012\u0010 \u001a\u00020!X¦\u0004¢\u0006\u0006\u001a\u0004\b\"\u0010#R\u0012\u0010$\u001a\u00020\u0011X¦\u0004¢\u0006\u0006\u001a\u0004\b%\u0010\u0013R\u0012\u0010&\u001a\u00020'X¦\u0004¢\u0006\u0006\u001a\u0004\b(\u0010)R\u0012\u0010*\u001a\u00020+X¦\u0004¢\u0006\u0006\u001a\u0004\b,\u0010-R\u0014\u0010.\u001a\u0004\u0018\u00010\rX¦\u0004¢\u0006\u0006\u001a\u0004\b/\u0010\u000fR\u0012\u00100\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b1\u0010\u0005R\u0012\u00102\u001a\u00020\rX¦\u0004¢\u0006\u0006\u001a\u0004\b3\u0010\u000f¨\u00065"}, d2 = {"Lcom/tonyodev/fetch2/Download;", "", "created", "", "getCreated", "()J", "downloaded", "getDownloaded", "error", "Lcom/tonyodev/fetch2/Error;", "getError", "()Lcom/tonyodev/fetch2/Error;", "file", "", "getFile", "()Ljava/lang/String;", "group", "", "getGroup", "()I", "headers", "", "getHeaders", "()Ljava/util/Map;", "id", "getId", "namespace", "getNamespace", "networkType", "Lcom/tonyodev/fetch2/NetworkType;", "getNetworkType", "()Lcom/tonyodev/fetch2/NetworkType;", "priority", "Lcom/tonyodev/fetch2/Priority;", "getPriority", "()Lcom/tonyodev/fetch2/Priority;", "progress", "getProgress", "request", "Lcom/tonyodev/fetch2/Request;", "getRequest", "()Lcom/tonyodev/fetch2/Request;", "status", "Lcom/tonyodev/fetch2/Status;", "getStatus", "()Lcom/tonyodev/fetch2/Status;", "tag", "getTag", "total", "getTotal", "url", "getUrl", "copy", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: Download.kt */
public interface Download {
    @NotNull
    Download copy();

    long getCreated();

    long getDownloaded();

    @NotNull
    Error getError();

    @NotNull
    String getFile();

    int getGroup();

    @NotNull
    Map<String, String> getHeaders();

    int getId();

    @NotNull
    String getNamespace();

    @NotNull
    NetworkType getNetworkType();

    @NotNull
    Priority getPriority();

    int getProgress();

    @NotNull
    Request getRequest();

    @NotNull
    Status getStatus();

    @Nullable
    String getTag();

    long getTotal();

    @NotNull
    String getUrl();
}
