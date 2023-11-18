package com.tonyodev.fetch2.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2.Status;
import com.tonyodev.fetch2.util.FetchDefaults;
import com.tonyodev.fetch2.util.FetchTypeConverterExtensions;
import com.tonyodev.fetch2.util.FetchUtils;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010K\u001a\u00020\u0001H\u0016J\u0013\u0010L\u001a\u00020M2\b\u0010N\u001a\u0004\u0018\u00010OH\u0002J\b\u0010P\u001a\u00020\u0019H\u0016J\b\u0010Q\u001a\u00020\u0013H\u0016R\u001e\u0010\u0003\u001a\u00020\u00048\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\u00048\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001e\u0010\f\u001a\u00020\r8\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001e\u0010\u0012\u001a\u00020\u00138\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001e\u0010\u0018\u001a\u00020\u00198\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR*\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00130\u001f8\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u001e\u0010$\u001a\u00020\u00198\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u001b\"\u0004\b&\u0010\u001dR\u001e\u0010'\u001a\u00020\u00138\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\u0015\"\u0004\b)\u0010\u0017R\u001e\u0010*\u001a\u00020+8\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010-\"\u0004\b.\u0010/R\u001e\u00100\u001a\u0002018\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b2\u00103\"\u0004\b4\u00105R\u0014\u00106\u001a\u00020\u00198VX\u0004¢\u0006\u0006\u001a\u0004\b7\u0010\u001bR\u0014\u00108\u001a\u0002098VX\u0004¢\u0006\u0006\u001a\u0004\b:\u0010;R\u001e\u0010<\u001a\u00020=8\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b>\u0010?\"\u0004\b@\u0010AR \u0010B\u001a\u0004\u0018\u00010\u00138\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bC\u0010\u0015\"\u0004\bD\u0010\u0017R\u001e\u0010E\u001a\u00020\u00048\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bF\u0010\u0006\"\u0004\bG\u0010\bR\u001e\u0010H\u001a\u00020\u00138\u0016@\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bI\u0010\u0015\"\u0004\bJ\u0010\u0017¨\u0006R"}, d2 = {"Lcom/tonyodev/fetch2/database/DownloadInfo;", "Lcom/tonyodev/fetch2/Download;", "()V", "created", "", "getCreated", "()J", "setCreated", "(J)V", "downloaded", "getDownloaded", "setDownloaded", "error", "Lcom/tonyodev/fetch2/Error;", "getError", "()Lcom/tonyodev/fetch2/Error;", "setError", "(Lcom/tonyodev/fetch2/Error;)V", "file", "", "getFile", "()Ljava/lang/String;", "setFile", "(Ljava/lang/String;)V", "group", "", "getGroup", "()I", "setGroup", "(I)V", "headers", "", "getHeaders", "()Ljava/util/Map;", "setHeaders", "(Ljava/util/Map;)V", "id", "getId", "setId", "namespace", "getNamespace", "setNamespace", "networkType", "Lcom/tonyodev/fetch2/NetworkType;", "getNetworkType", "()Lcom/tonyodev/fetch2/NetworkType;", "setNetworkType", "(Lcom/tonyodev/fetch2/NetworkType;)V", "priority", "Lcom/tonyodev/fetch2/Priority;", "getPriority", "()Lcom/tonyodev/fetch2/Priority;", "setPriority", "(Lcom/tonyodev/fetch2/Priority;)V", "progress", "getProgress", "request", "Lcom/tonyodev/fetch2/Request;", "getRequest", "()Lcom/tonyodev/fetch2/Request;", "status", "Lcom/tonyodev/fetch2/Status;", "getStatus", "()Lcom/tonyodev/fetch2/Status;", "setStatus", "(Lcom/tonyodev/fetch2/Status;)V", "tag", "getTag", "setTag", "total", "getTotal", "setTotal", "url", "getUrl", "setUrl", "copy", "equals", "", "other", "", "hashCode", "toString", "fetch2_release"}, k = 1, mv = {1, 1, 10})
@Entity(indices = {@Index(unique = true, value = {"_file"}), @Index(unique = false, value = {"_group", "_status"})}, tableName = "requests")
/* compiled from: DownloadInfo.kt */
public final class DownloadInfo implements Download {
    @ColumnInfo(name = "_created")
    private long created = System.nanoTime();
    @ColumnInfo(name = "_written_bytes")
    private long downloaded;
    @NotNull
    @ColumnInfo(name = "_error")
    private Error error = FetchDefaults.getDefaultNoError();
    @NotNull
    @ColumnInfo(name = "_file")
    private String file = "";
    @ColumnInfo(name = "_group")
    private int group;
    @NotNull
    @ColumnInfo(name = "_headers")
    private Map<String, String> headers = FetchDefaults.getDefaultEmptyHeaderMap();
    @PrimaryKey
    @ColumnInfo(name = "_id")
    private int id;
    @NotNull
    @ColumnInfo(name = "_namespace")
    private String namespace = "";
    @NotNull
    @ColumnInfo(name = "_network_type")
    private NetworkType networkType = FetchDefaults.getDefaultNetworkType();
    @NotNull
    @ColumnInfo(name = "_priority")
    private Priority priority = FetchDefaults.getDefaultPriority();
    @NotNull
    @ColumnInfo(name = "_status")
    private Status status = FetchDefaults.getDefaultStatus();
    @ColumnInfo(name = "_tag")
    @Nullable
    private String tag;
    @ColumnInfo(name = "_total_bytes")
    private long total = -1;
    @NotNull
    @ColumnInfo(name = "_url")
    private String url = "";

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    @NotNull
    public String getNamespace() {
        return this.namespace;
    }

    public void setNamespace(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.namespace = str;
    }

    @NotNull
    public String getUrl() {
        return this.url;
    }

    public void setUrl(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.url = str;
    }

    @NotNull
    public String getFile() {
        return this.file;
    }

    public void setFile(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.file = str;
    }

    public int getGroup() {
        return this.group;
    }

    public void setGroup(int i) {
        this.group = i;
    }

    @NotNull
    public Priority getPriority() {
        return this.priority;
    }

    public void setPriority(@NotNull Priority priority) {
        Intrinsics.checkParameterIsNotNull(priority, "<set-?>");
        this.priority = priority;
    }

    @NotNull
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(@NotNull Map<String, String> map) {
        Intrinsics.checkParameterIsNotNull(map, "<set-?>");
        this.headers = map;
    }

    public long getDownloaded() {
        return this.downloaded;
    }

    public void setDownloaded(long j) {
        this.downloaded = j;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long j) {
        this.total = j;
    }

    @NotNull
    public Status getStatus() {
        return this.status;
    }

    public void setStatus(@NotNull Status status) {
        Intrinsics.checkParameterIsNotNull(status, "<set-?>");
        this.status = status;
    }

    @NotNull
    public Error getError() {
        return this.error;
    }

    public void setError(@NotNull Error error) {
        Intrinsics.checkParameterIsNotNull(error, "<set-?>");
        this.error = error;
    }

    @NotNull
    public NetworkType getNetworkType() {
        return this.networkType;
    }

    public void setNetworkType(@NotNull NetworkType networkType) {
        Intrinsics.checkParameterIsNotNull(networkType, "<set-?>");
        this.networkType = networkType;
    }

    public long getCreated() {
        return this.created;
    }

    public void setCreated(long j) {
        this.created = j;
    }

    @Nullable
    public String getTag() {
        return this.tag;
    }

    public void setTag(@Nullable String str) {
        this.tag = str;
    }

    public int getProgress() {
        return FetchUtils.calculateProgress(getDownloaded(), getTotal());
    }

    @NotNull
    public Request getRequest() {
        Request request = new Request(getId(), getUrl(), getFile());
        request.setGroupId(getGroup());
        request.getHeaders().putAll(getHeaders());
        request.setNetworkType(getNetworkType());
        request.setPriority(getPriority());
        return request;
    }

    @NotNull
    public Download copy() {
        return FetchTypeConverterExtensions.toDownloadInfo((Download) this);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if ((Intrinsics.areEqual(getClass(), obj != null ? obj.getClass() : null) ^ 1) != 0) {
            return false;
        }
        if (obj == null) {
            throw new TypeCastException("null cannot be cast to non-null type com.tonyodev.fetch2.database.DownloadInfo");
        }
        DownloadInfo downloadInfo = (DownloadInfo) obj;
        if (getId() == downloadInfo.getId() && (Intrinsics.areEqual(getNamespace(), downloadInfo.getNamespace()) ^ 1) == 0 && (Intrinsics.areEqual(getUrl(), downloadInfo.getUrl()) ^ 1) == 0 && (Intrinsics.areEqual(getFile(), downloadInfo.getFile()) ^ 1) == 0 && getGroup() == downloadInfo.getGroup() && getPriority() == downloadInfo.getPriority() && (Intrinsics.areEqual(getHeaders(), downloadInfo.getHeaders()) ^ 1) == 0 && getDownloaded() == downloadInfo.getDownloaded() && getTotal() == downloadInfo.getTotal() && getStatus() == downloadInfo.getStatus() && getError() == downloadInfo.getError() && getNetworkType() == downloadInfo.getNetworkType() && getCreated() == downloadInfo.getCreated() && (Intrinsics.areEqual(getTag(), downloadInfo.getTag()) ^ 1) == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int id = ((((((((((((((((((((((((getId() * 31) + getNamespace().hashCode()) * 31) + getUrl().hashCode()) * 31) + getFile().hashCode()) * 31) + getGroup()) * 31) + getPriority().hashCode()) * 31) + getHeaders().hashCode()) * 31) + Long.valueOf(getDownloaded()).hashCode()) * 31) + Long.valueOf(getTotal()).hashCode()) * 31) + getStatus().hashCode()) * 31) + getError().hashCode()) * 31) + getNetworkType().hashCode()) * 31) + Long.valueOf(getCreated()).hashCode()) * 31;
        String tag = getTag();
        return id + (tag != null ? tag.hashCode() : 0);
    }

    @NotNull
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Download(id=");
        stringBuilder.append(getId());
        stringBuilder.append(", namespace='");
        stringBuilder.append(getNamespace());
        stringBuilder.append("', url='");
        stringBuilder.append(getUrl());
        stringBuilder.append("', file='");
        stringBuilder.append(getFile());
        stringBuilder.append("', group=");
        stringBuilder.append(getGroup());
        stringBuilder.append(',');
        stringBuilder.append(" priority=");
        stringBuilder.append(getPriority());
        stringBuilder.append(", headers=");
        stringBuilder.append(getHeaders());
        stringBuilder.append(", downloaded=");
        stringBuilder.append(getDownloaded());
        stringBuilder.append(", total=");
        stringBuilder.append(getTotal());
        stringBuilder.append(", status=");
        stringBuilder.append(getStatus());
        stringBuilder.append(',');
        stringBuilder.append(" error=");
        stringBuilder.append(getError());
        stringBuilder.append(", networkType=");
        stringBuilder.append(getNetworkType());
        stringBuilder.append(", created=");
        stringBuilder.append(getCreated());
        stringBuilder.append(", tag=");
        stringBuilder.append(getTag());
        stringBuilder.append(')');
        return stringBuilder.toString();
    }
}
