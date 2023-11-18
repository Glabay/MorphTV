package com.tonyodev.fetch2;

import android.content.Context;
import com.google.android.gms.common.server.FavaDiagnosticsEntity;
import com.tonyodev.fetch2.fetch.FetchBuilder;
import com.tonyodev.fetch2.fetch.FetchImpl;
import com.tonyodev.fetch2.fetch.FetchModulesBuilder;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001:\u0001AJ\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH&J\u0014\u0010\r\u001a\u00020\n2\n\u0010\u000e\u001a\u00020\u000f\"\u00020\u0010H&J\b\u0010\u0011\u001a\u00020\nH&J\u0010\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u0010H&J\b\u0010\u0014\u001a\u00020\nH&J\u0014\u0010\u0015\u001a\u00020\n2\n\u0010\u000e\u001a\u00020\u000f\"\u00020\u0010H&J\b\u0010\u0016\u001a\u00020\nH&J\u0010\u0010\u0017\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u0019H&J\u0010\u0010\u001a\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u0010H&J\u0010\u0010\u001b\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\u0003H&J4\u0010\u001d\u001a\u00020\n2\u0006\u0010\u001e\u001a\u00020\u001f2\u0010\b\u0002\u0010 \u001a\n\u0012\u0004\u0012\u00020\"\u0018\u00010!2\u0010\b\u0002\u0010#\u001a\n\u0012\u0004\u0012\u00020$\u0018\u00010!H&J@\u0010\u001d\u001a\u00020\n2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u001f0&2\u0016\b\u0002\u0010 \u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0&\u0018\u00010!2\u0010\b\u0002\u0010#\u001a\n\u0012\u0004\u0012\u00020$\u0018\u00010!H&J\b\u0010'\u001a\u00020\nH&J \u0010(\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u00102\u000e\u0010 \u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\"0)H&J\u001c\u0010*\u001a\u00020\n2\u0012\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0&0!H&J*\u0010*\u001a\u00020\n2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00100&2\u0012\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0&0!H&J$\u0010,\u001a\u00020\n2\u0006\u0010-\u001a\u00020\u00102\u0012\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0&0!H&J,\u0010.\u001a\u00020\n2\u0006\u0010-\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u00192\u0012\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0&0!H&J$\u0010/\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u00192\u0012\u0010 \u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0&0!H&J\u0014\u00100\u001a\u00020\n2\n\u0010\u000e\u001a\u00020\u000f\"\u00020\u0010H&J\u0010\u00101\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u0010H&J\u0014\u00102\u001a\u00020\n2\n\u0010\u000e\u001a\u00020\u000f\"\u00020\u0010H&J\b\u00103\u001a\u00020\nH&J\u0010\u00104\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u0019H&J\u0010\u00105\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u0010H&J\u0010\u00106\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH&J\u0014\u00107\u001a\u00020\n2\n\u0010\u000e\u001a\u00020\u000f\"\u00020\u0010H&J\u0010\u00108\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u0010H&J\u0014\u00109\u001a\u00020\n2\n\u0010\u000e\u001a\u00020\u000f\"\u00020\u0010H&J\u0010\u0010:\u001a\u00020\n2\u0006\u0010;\u001a\u00020<H&J\b\u0010=\u001a\u00020\nH&J<\u0010>\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u00102\u0006\u0010?\u001a\u00020@2\u0010\b\u0002\u0010 \u001a\n\u0012\u0004\u0012\u00020\"\u0018\u00010!2\u0010\b\u0002\u0010#\u001a\n\u0012\u0004\u0012\u00020$\u0018\u00010!H&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0004R\u0012\u0010\u0005\u001a\u00020\u0006X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006B"}, d2 = {"Lcom/tonyodev/fetch2/Fetch;", "", "isClosed", "", "()Z", "namespace", "", "getNamespace", "()Ljava/lang/String;", "addListener", "", "listener", "Lcom/tonyodev/fetch2/FetchListener;", "cancel", "ids", "", "", "cancelAll", "cancelGroup", "id", "close", "delete", "deleteAll", "deleteAllWithStatus", "status", "Lcom/tonyodev/fetch2/Status;", "deleteGroup", "enableLogging", "enabled", "enqueue", "request", "Lcom/tonyodev/fetch2/Request;", "func", "Lcom/tonyodev/fetch2/Func;", "Lcom/tonyodev/fetch2/Download;", "func2", "Lcom/tonyodev/fetch2/Error;", "requests", "", "freeze", "getDownload", "Lcom/tonyodev/fetch2/Func2;", "getDownloads", "idList", "getDownloadsInGroup", "groupId", "getDownloadsInGroupWithStatus", "getDownloadsWithStatus", "pause", "pauseGroup", "remove", "removeAll", "removeAllWithStatus", "removeGroup", "removeListener", "resume", "resumeGroup", "retry", "setGlobalNetworkType", "networkType", "Lcom/tonyodev/fetch2/NetworkType;", "unfreeze", "updateRequest", "requestInfo", "Lcom/tonyodev/fetch2/RequestInfo;", "Builder", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: Fetch.kt */
public interface Fetch {

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0000\u0012\u0004\u0012\u00020\u00020\u0001B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\b\u001a\u00020\u0002H\u0016¨\u0006\t"}, d2 = {"Lcom/tonyodev/fetch2/Fetch$Builder;", "Lcom/tonyodev/fetch2/fetch/FetchBuilder;", "Lcom/tonyodev/fetch2/Fetch;", "context", "Landroid/content/Context;", "namespace", "", "(Landroid/content/Context;Ljava/lang/String;)V", "build", "fetch2_release"}, k = 1, mv = {1, 1, 10})
    /* compiled from: Fetch.kt */
    public static final class Builder extends FetchBuilder<Builder, Fetch> {
        public Builder(@NotNull Context context, @NotNull String str) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(str, FavaDiagnosticsEntity.EXTRA_NAMESPACE);
            super(context, str);
        }

        @NotNull
        public Fetch build() {
            return FetchImpl.Companion.newInstance(FetchModulesBuilder.INSTANCE.buildModulesFromPrefs(getBuilderPrefs()));
        }
    }

    @Metadata(bv = {1, 0, 2}, k = 3, mv = {1, 1, 10})
    /* compiled from: Fetch.kt */
    public static final class DefaultImpls {
        public static /* bridge */ /* synthetic */ void updateRequest$default(Fetch fetch, int i, RequestInfo requestInfo, Func func, Func func2, int i2, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: updateRequest");
            }
            if ((i2 & 4) != null) {
                func = null;
            }
            if ((i2 & 8) != 0) {
                func2 = null;
            }
            fetch.updateRequest(i, requestInfo, func, func2);
        }
    }

    void addListener(@NotNull FetchListener fetchListener);

    void cancel(@NotNull int... iArr);

    void cancelAll();

    void cancelGroup(int i);

    void close();

    void delete(@NotNull int... iArr);

    void deleteAll();

    void deleteAllWithStatus(@NotNull Status status);

    void deleteGroup(int i);

    void enableLogging(boolean z);

    void enqueue(@NotNull Request request, @Nullable Func<? super Download> func, @Nullable Func<? super Error> func2);

    void enqueue(@NotNull List<? extends Request> list, @Nullable Func<? super List<? extends Download>> func, @Nullable Func<? super Error> func2);

    void freeze();

    void getDownload(int i, @NotNull Func2<? super Download> func2);

    void getDownloads(@NotNull Func<? super List<? extends Download>> func);

    void getDownloads(@NotNull List<Integer> list, @NotNull Func<? super List<? extends Download>> func);

    void getDownloadsInGroup(int i, @NotNull Func<? super List<? extends Download>> func);

    void getDownloadsInGroupWithStatus(int i, @NotNull Status status, @NotNull Func<? super List<? extends Download>> func);

    void getDownloadsWithStatus(@NotNull Status status, @NotNull Func<? super List<? extends Download>> func);

    @NotNull
    String getNamespace();

    boolean isClosed();

    void pause(@NotNull int... iArr);

    void pauseGroup(int i);

    void remove(@NotNull int... iArr);

    void removeAll();

    void removeAllWithStatus(@NotNull Status status);

    void removeGroup(int i);

    void removeListener(@NotNull FetchListener fetchListener);

    void resume(@NotNull int... iArr);

    void resumeGroup(int i);

    void retry(@NotNull int... iArr);

    void setGlobalNetworkType(@NotNull NetworkType networkType);

    void unfreeze();

    void updateRequest(int i, @NotNull RequestInfo requestInfo, @Nullable Func<? super Download> func, @Nullable Func<? super Error> func2);
}
