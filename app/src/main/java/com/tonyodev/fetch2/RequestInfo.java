package com.tonyodev.fetch2;

import com.tonyodev.fetch2.util.FetchDefaults;
import de.timroes.axmlrpc.XMLRPCClient;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0016\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0016\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u000b2\u0006\u0010\"\u001a\u00020\u000bJ\u0013\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010\u0001H\u0002J\b\u0010&\u001a\u00020\u0004H\u0016J\b\u0010'\u001a\u00020\u000bH\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001d\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0\n¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\u00020\u0015X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001c\u0010\u001a\u001a\u0004\u0018\u00010\u000bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001e¨\u0006("}, d2 = {"Lcom/tonyodev/fetch2/RequestInfo;", "", "()V", "groupId", "", "getGroupId", "()I", "setGroupId", "(I)V", "headers", "", "", "getHeaders", "()Ljava/util/Map;", "networkType", "Lcom/tonyodev/fetch2/NetworkType;", "getNetworkType", "()Lcom/tonyodev/fetch2/NetworkType;", "setNetworkType", "(Lcom/tonyodev/fetch2/NetworkType;)V", "priority", "Lcom/tonyodev/fetch2/Priority;", "getPriority", "()Lcom/tonyodev/fetch2/Priority;", "setPriority", "(Lcom/tonyodev/fetch2/Priority;)V", "tag", "getTag", "()Ljava/lang/String;", "setTag", "(Ljava/lang/String;)V", "addHeader", "", "key", "value", "equals", "", "other", "hashCode", "toString", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: RequestInfo.kt */
public class RequestInfo {
    private int groupId;
    @NotNull
    private final Map<String, String> headers = new LinkedHashMap();
    @NotNull
    private NetworkType networkType = FetchDefaults.getDefaultNetworkType();
    @NotNull
    private Priority priority = FetchDefaults.getDefaultPriority();
    @Nullable
    private String tag;

    public final int getGroupId() {
        return this.groupId;
    }

    public final void setGroupId(int i) {
        this.groupId = i;
    }

    @NotNull
    public final Map<String, String> getHeaders() {
        return this.headers;
    }

    @NotNull
    public final Priority getPriority() {
        return this.priority;
    }

    public final void setPriority(@NotNull Priority priority) {
        Intrinsics.checkParameterIsNotNull(priority, "<set-?>");
        this.priority = priority;
    }

    @NotNull
    public final NetworkType getNetworkType() {
        return this.networkType;
    }

    public final void setNetworkType(@NotNull NetworkType networkType) {
        Intrinsics.checkParameterIsNotNull(networkType, "<set-?>");
        this.networkType = networkType;
    }

    public final void addHeader(@NotNull String str, @NotNull String str2) {
        Intrinsics.checkParameterIsNotNull(str, "key");
        Intrinsics.checkParameterIsNotNull(str2, XMLRPCClient.VALUE);
        this.headers.put(str, str2);
    }

    @Nullable
    public final String getTag() {
        return this.tag;
    }

    public final void setTag(@Nullable String str) {
        this.tag = str;
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if ((Intrinsics.areEqual(getClass(), obj != null ? obj.getClass() : null) ^ 1) != 0) {
            return false;
        }
        if (obj == null) {
            throw new TypeCastException("null cannot be cast to non-null type com.tonyodev.fetch2.RequestInfo");
        }
        RequestInfo requestInfo = (RequestInfo) obj;
        if (this.groupId == requestInfo.groupId && (Intrinsics.areEqual(this.headers, requestInfo.headers) ^ 1) == 0 && this.priority == requestInfo.priority && this.networkType == requestInfo.networkType && (Intrinsics.areEqual(this.tag, requestInfo.tag) ^ 1) == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hashCode = ((((((this.groupId * 31) + this.headers.hashCode()) * 31) + this.priority.hashCode()) * 31) + this.networkType.hashCode()) * 31;
        String str = this.tag;
        return hashCode + (str != null ? str.hashCode() : 0);
    }

    @NotNull
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RequestInfo(groupId=");
        stringBuilder.append(this.groupId);
        stringBuilder.append(", headers=");
        stringBuilder.append(this.headers);
        stringBuilder.append(", priority=");
        stringBuilder.append(this.priority);
        stringBuilder.append(", ");
        stringBuilder.append("networkType=");
        stringBuilder.append(this.networkType);
        stringBuilder.append(", tag=");
        stringBuilder.append(this.tag);
        stringBuilder.append(')');
        return stringBuilder.toString();
    }
}
