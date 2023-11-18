package com.tonyodev.fetch2;

import com.google.android.gms.common.internal.ImagesContract;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0016\u0018\u00002\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005B\u001d\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\bJ\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0002J\b\u0010\u0012\u001a\u00020\u0007H\u0016J\b\u0010\u0013\u001a\u00020\u0003H\u0016R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\n¨\u0006\u0014"}, d2 = {"Lcom/tonyodev/fetch2/Request;", "Lcom/tonyodev/fetch2/RequestInfo;", "url", "", "file", "(Ljava/lang/String;Ljava/lang/String;)V", "id", "", "(ILjava/lang/String;Ljava/lang/String;)V", "getFile", "()Ljava/lang/String;", "getId", "()I", "getUrl", "equals", "", "other", "", "hashCode", "toString", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: Request.kt */
public class Request extends RequestInfo {
    @NotNull
    private final String file;
    private final int id;
    @NotNull
    private final String url;

    public final int getId() {
        return this.id;
    }

    @NotNull
    public final String getUrl() {
        return this.url;
    }

    public Request(int i, @NotNull String str, @NotNull String str2) {
        Intrinsics.checkParameterIsNotNull(str, ImagesContract.URL);
        Intrinsics.checkParameterIsNotNull(str2, "file");
        this.id = i;
        this.url = str;
        this.file = str2;
    }

    @NotNull
    public final String getFile() {
        return this.file;
    }

    public Request(@NotNull String str, @NotNull String str2) {
        Intrinsics.checkParameterIsNotNull(str, ImagesContract.URL);
        Intrinsics.checkParameterIsNotNull(str2, "file");
        this((str.hashCode() * 31) + str2.hashCode(), str, str2);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if ((Intrinsics.areEqual(getClass(), obj != null ? obj.getClass() : null) ^ 1) != 0) {
            return false;
        }
        if (obj == null) {
            throw new TypeCastException("null cannot be cast to non-null type com.tonyodev.fetch2.Request");
        }
        Request request = (Request) obj;
        if ((Intrinsics.areEqual(this.url, request.url) ^ 1) == 0 && (Intrinsics.areEqual(this.file, request.file) ^ 1) == 0 && this.id == request.id && getGroupId() == request.getGroupId() && (Intrinsics.areEqual(getHeaders(), request.getHeaders()) ^ 1) == 0 && getPriority() == request.getPriority() && getNetworkType() == request.getNetworkType() && (Intrinsics.areEqual(getTag(), request.getTag()) ^ 1) == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hashCode = ((((((((((((this.url.hashCode() * 31) + this.file.hashCode()) * 31) + this.id) * 31) + getGroupId()) * 31) + getHeaders().hashCode()) * 31) + getPriority().hashCode()) * 31) + getNetworkType().hashCode()) * 31;
        String tag = getTag();
        return hashCode + (tag != null ? tag.hashCode() : 0);
    }

    @NotNull
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Request(url='");
        stringBuilder.append(this.url);
        stringBuilder.append("', file='");
        stringBuilder.append(this.file);
        stringBuilder.append("', id=");
        stringBuilder.append(this.id);
        stringBuilder.append(", groupId=");
        stringBuilder.append(getGroupId());
        stringBuilder.append(", ");
        stringBuilder.append("headers=");
        stringBuilder.append(getHeaders());
        stringBuilder.append(", priority=");
        stringBuilder.append(getPriority());
        stringBuilder.append(", networkType=");
        stringBuilder.append(getNetworkType());
        stringBuilder.append(", tag=");
        stringBuilder.append(getTag());
        stringBuilder.append(')');
        return stringBuilder.toString();
    }
}
