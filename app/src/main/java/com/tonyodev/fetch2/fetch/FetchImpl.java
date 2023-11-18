package com.tonyodev.fetch2.fetch;

import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs;
import com.google.android.gms.common.server.FavaDiagnosticsEntity;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.Func;
import com.tonyodev.fetch2.Func2;
import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2.RequestInfo;
import com.tonyodev.fetch2.Status;
import com.tonyodev.fetch2.database.DownloadDatabase;
import com.tonyodev.fetch2.exception.FetchException;
import com.tonyodev.fetch2.exception.FetchException.Code;
import com.tonyodev.fetch2.fetch.FetchModulesBuilder.Modules;
import com.tonyodev.fetch2.provider.ListenerProvider;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0016\u0018\u0000 Z2\u00020\u0001:\u0001ZB5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f¢\u0006\u0002\u0010\rJ\u0010\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$H\u0016J\u0014\u0010%\u001a\u00020\"2\n\u0010&\u001a\u00020'\"\u00020(H\u0016J\b\u0010)\u001a\u00020\"H\u0016J\u0010\u0010*\u001a\u00020\"2\u0006\u0010+\u001a\u00020(H\u0016J\b\u0010,\u001a\u00020\"H\u0016J\u0014\u0010-\u001a\u00020\"2\n\u0010&\u001a\u00020'\"\u00020(H\u0016J\b\u0010.\u001a\u00020\"H\u0016J\u0010\u0010/\u001a\u00020\"2\u0006\u00100\u001a\u000201H\u0016J\u0010\u00102\u001a\u00020\"2\u0006\u0010+\u001a\u00020(H\u0016J\u0010\u00103\u001a\u00020\"2\u0006\u00104\u001a\u00020\u000fH\u0016J0\u00105\u001a\u00020\"2\u0006\u00106\u001a\u0002072\u000e\u00108\u001a\n\u0012\u0004\u0012\u00020:\u0018\u0001092\u000e\u0010;\u001a\n\u0012\u0004\u0012\u00020<\u0018\u000109H\u0016J<\u00105\u001a\u00020\"2\f\u0010=\u001a\b\u0012\u0004\u0012\u0002070>2\u0014\u00108\u001a\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020:0>\u0018\u0001092\u000e\u0010;\u001a\n\u0012\u0004\u0012\u00020<\u0018\u000109H\u0016J\b\u0010?\u001a\u00020\"H\u0016J \u0010@\u001a\u00020\"2\u0006\u0010+\u001a\u00020(2\u000e\u00108\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010:0AH\u0016J\u001c\u0010B\u001a\u00020\"2\u0012\u00108\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020:0>09H\u0016J*\u0010B\u001a\u00020\"2\f\u0010C\u001a\b\u0012\u0004\u0012\u00020(0>2\u0012\u00108\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020:0>09H\u0016J$\u0010D\u001a\u00020\"2\u0006\u0010E\u001a\u00020(2\u0012\u00108\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020:0>09H\u0016J,\u0010F\u001a\u00020\"2\u0006\u0010E\u001a\u00020(2\u0006\u00100\u001a\u0002012\u0012\u00108\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020:0>09H\u0016J$\u0010G\u001a\u00020\"2\u0006\u00100\u001a\u0002012\u0012\u00108\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020:0>09H\u0016J\u0014\u0010H\u001a\u00020\"2\n\u0010&\u001a\u00020'\"\u00020(H\u0016J\u0010\u0010I\u001a\u00020\"2\u0006\u0010+\u001a\u00020(H\u0016J\u0014\u0010J\u001a\u00020\"2\n\u0010&\u001a\u00020'\"\u00020(H\u0016J\b\u0010K\u001a\u00020\"H\u0016J\u0010\u0010L\u001a\u00020\"2\u0006\u00100\u001a\u000201H\u0016J\u0010\u0010M\u001a\u00020\"2\u0006\u0010+\u001a\u00020(H\u0016J\u0010\u0010N\u001a\u00020\"2\u0006\u0010#\u001a\u00020$H\u0016J\u0014\u0010O\u001a\u00020\"2\n\u0010&\u001a\u00020'\"\u00020(H\u0016J\u0010\u0010P\u001a\u00020\"2\u0006\u0010+\u001a\u00020(H\u0016J\u0014\u0010Q\u001a\u00020\"2\n\u0010&\u001a\u00020'\"\u00020(H\u0016J\u0010\u0010R\u001a\u00020\"2\u0006\u0010S\u001a\u00020TH\u0016J\b\u0010U\u001a\u00020\"H\u0004J\b\u0010V\u001a\u00020\"H\u0016J8\u0010W\u001a\u00020\"2\u0006\u0010+\u001a\u00020(2\u0006\u0010X\u001a\u00020Y2\u000e\u00108\u001a\n\u0012\u0004\u0012\u00020:\u0018\u0001092\u000e\u0010;\u001a\n\u0012\u0004\u0012\u00020<\u0018\u000109H\u0016R\u0012\u0010\u000e\u001a\u00020\u000f8\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\t\u001a\u00020\nX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0014\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0016\u001a\u00020\u000f8VX\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u0014\u0010\u0018\u001a\u00020\u0019X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0014\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0014\u0010\u0006\u001a\u00020\u0005X\u0004¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u0015¨\u0006["}, d2 = {"Lcom/tonyodev/fetch2/fetch/FetchImpl;", "Lcom/tonyodev/fetch2/Fetch;", "namespace", "", "handler", "Landroid/os/Handler;", "uiHandler", "fetchHandler", "Lcom/tonyodev/fetch2/fetch/FetchHandler;", "fetchListenerProvider", "Lcom/tonyodev/fetch2/provider/ListenerProvider;", "logger", "Lcom/tonyodev/fetch2/Logger;", "(Ljava/lang/String;Landroid/os/Handler;Landroid/os/Handler;Lcom/tonyodev/fetch2/fetch/FetchHandler;Lcom/tonyodev/fetch2/provider/ListenerProvider;Lcom/tonyodev/fetch2/Logger;)V", "closed", "", "getFetchHandler", "()Lcom/tonyodev/fetch2/fetch/FetchHandler;", "getFetchListenerProvider", "()Lcom/tonyodev/fetch2/provider/ListenerProvider;", "getHandler", "()Landroid/os/Handler;", "isClosed", "()Z", "lock", "Ljava/lang/Object;", "getLock", "()Ljava/lang/Object;", "getLogger", "()Lcom/tonyodev/fetch2/Logger;", "getNamespace", "()Ljava/lang/String;", "getUiHandler", "addListener", "", "listener", "Lcom/tonyodev/fetch2/FetchListener;", "cancel", "ids", "", "", "cancelAll", "cancelGroup", "id", "close", "delete", "deleteAll", "deleteAllWithStatus", "status", "Lcom/tonyodev/fetch2/Status;", "deleteGroup", "enableLogging", "enabled", "enqueue", "request", "Lcom/tonyodev/fetch2/Request;", "func", "Lcom/tonyodev/fetch2/Func;", "Lcom/tonyodev/fetch2/Download;", "func2", "Lcom/tonyodev/fetch2/Error;", "requests", "", "freeze", "getDownload", "Lcom/tonyodev/fetch2/Func2;", "getDownloads", "idList", "getDownloadsInGroup", "groupId", "getDownloadsInGroupWithStatus", "getDownloadsWithStatus", "pause", "pauseGroup", "remove", "removeAll", "removeAllWithStatus", "removeGroup", "removeListener", "resume", "resumeGroup", "retry", "setGlobalNetworkType", "networkType", "Lcom/tonyodev/fetch2/NetworkType;", "throwExceptionIfClosed", "unfreeze", "updateRequest", "requestInfo", "Lcom/tonyodev/fetch2/RequestInfo;", "Companion", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: FetchImpl.kt */
public class FetchImpl implements Fetch {
    public static final Companion Companion = new Companion();
    private volatile boolean closed;
    @NotNull
    private final FetchHandler fetchHandler;
    @NotNull
    private final ListenerProvider fetchListenerProvider;
    @NotNull
    private final Handler handler;
    @NotNull
    private final Object lock = new Object();
    @NotNull
    private final Logger logger;
    @NotNull
    private final String namespace;
    @NotNull
    private final Handler uiHandler;

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007¨\u0006\u0007"}, d2 = {"Lcom/tonyodev/fetch2/fetch/FetchImpl$Companion;", "", "()V", "newInstance", "Lcom/tonyodev/fetch2/fetch/FetchImpl;", "modules", "Lcom/tonyodev/fetch2/fetch/FetchModulesBuilder$Modules;", "fetch2_release"}, k = 1, mv = {1, 1, 10})
    /* compiled from: FetchImpl.kt */
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final FetchImpl newInstance(@NotNull Modules modules) {
            Intrinsics.checkParameterIsNotNull(modules, "modules");
            return new FetchImpl(modules.getPrefs().getNamespace(), modules.getHandler(), modules.getUiHandler(), modules.getFetchHandler(), modules.getFetchListenerProvider(), modules.getPrefs().getLogger());
        }
    }

    @JvmStatic
    @NotNull
    public static final FetchImpl newInstance(@NotNull Modules modules) {
        return Companion.newInstance(modules);
    }

    public FetchImpl(@NotNull String str, @NotNull Handler handler, @NotNull Handler handler2, @NotNull FetchHandler fetchHandler, @NotNull ListenerProvider listenerProvider, @NotNull Logger logger) {
        Intrinsics.checkParameterIsNotNull(str, FavaDiagnosticsEntity.EXTRA_NAMESPACE);
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        Intrinsics.checkParameterIsNotNull(handler2, "uiHandler");
        Intrinsics.checkParameterIsNotNull(fetchHandler, "fetchHandler");
        Intrinsics.checkParameterIsNotNull(listenerProvider, "fetchListenerProvider");
        Intrinsics.checkParameterIsNotNull(logger, "logger");
        this.namespace = str;
        this.handler = handler;
        this.uiHandler = handler2;
        this.fetchHandler = fetchHandler;
        this.fetchListenerProvider = listenerProvider;
        this.logger = logger;
        this.handler.post((Runnable) new Runnable() {
            public final void run() {
                this.getFetchHandler().init();
            }
        });
    }

    @NotNull
    public String getNamespace() {
        return this.namespace;
    }

    @NotNull
    protected final Handler getHandler() {
        return this.handler;
    }

    @NotNull
    protected final Handler getUiHandler() {
        return this.uiHandler;
    }

    @NotNull
    protected final FetchHandler getFetchHandler() {
        return this.fetchHandler;
    }

    @NotNull
    protected final ListenerProvider getFetchListenerProvider() {
        return this.fetchListenerProvider;
    }

    @NotNull
    protected final Logger getLogger() {
        return this.logger;
    }

    @NotNull
    protected final Object getLock() {
        return this.lock;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public void enqueue(@NotNull Request request, @Nullable Func<? super Download> func, @Nullable Func<? super Error> func2) {
        Intrinsics.checkParameterIsNotNull(request, "request");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$enqueue$$inlined$synchronized$lambda$1(this, request, func, func2));
        }
    }

    public void enqueue(@NotNull List<? extends Request> list, @Nullable Func<? super List<? extends Download>> func, @Nullable Func<? super Error> func2) {
        Intrinsics.checkParameterIsNotNull(list, DownloadDatabase.TABLE_NAME);
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$enqueue$$inlined$synchronized$lambda$2(this, list, func, func2));
        }
    }

    public void pause(@NotNull int... iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "ids");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$pause$$inlined$synchronized$lambda$1(this, iArr));
        }
    }

    public void pauseGroup(int i) {
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$pauseGroup$$inlined$synchronized$lambda$1(this, i));
        }
    }

    public void freeze() {
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$freeze$$inlined$synchronized$lambda$1(this));
        }
    }

    public void unfreeze() {
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$unfreeze$$inlined$synchronized$lambda$1(this));
        }
    }

    public void resume(@NotNull int... iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "ids");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$resume$$inlined$synchronized$lambda$1(this, iArr));
        }
    }

    public void resumeGroup(int i) {
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$resumeGroup$$inlined$synchronized$lambda$1(this, i));
        }
    }

    public void remove(@NotNull int... iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "ids");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$remove$$inlined$synchronized$lambda$1(this, iArr));
        }
    }

    public void removeGroup(int i) {
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$removeGroup$$inlined$synchronized$lambda$1(this, i));
        }
    }

    public void removeAll() {
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$removeAll$$inlined$synchronized$lambda$1(this));
        }
    }

    public void removeAllWithStatus(@NotNull Status status) {
        Intrinsics.checkParameterIsNotNull(status, NotificationCompat.CATEGORY_STATUS);
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$removeAllWithStatus$$inlined$synchronized$lambda$1(this, status));
        }
    }

    public void delete(@NotNull int... iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "ids");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$delete$$inlined$synchronized$lambda$1(this, iArr));
        }
    }

    public void deleteGroup(int i) {
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$deleteGroup$$inlined$synchronized$lambda$1(this, i));
        }
    }

    public void deleteAll() {
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$deleteAll$$inlined$synchronized$lambda$1(this));
        }
    }

    public void deleteAllWithStatus(@NotNull Status status) {
        Intrinsics.checkParameterIsNotNull(status, NotificationCompat.CATEGORY_STATUS);
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$deleteAllWithStatus$$inlined$synchronized$lambda$1(this, status));
        }
    }

    public void cancel(@NotNull int... iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "ids");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$cancel$$inlined$synchronized$lambda$1(this, iArr));
        }
    }

    public void cancelGroup(int i) {
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$cancelGroup$$inlined$synchronized$lambda$1(this, i));
        }
    }

    public void cancelAll() {
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$cancelAll$$inlined$synchronized$lambda$1(this));
        }
    }

    public void retry(@NotNull int... iArr) {
        Intrinsics.checkParameterIsNotNull(iArr, "ids");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$retry$$inlined$synchronized$lambda$1(this, iArr));
        }
    }

    public void updateRequest(int i, @NotNull RequestInfo requestInfo, @Nullable Func<? super Download> func, @Nullable Func<? super Error> func2) {
        Intrinsics.checkParameterIsNotNull(requestInfo, "requestInfo");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$updateRequest$$inlined$synchronized$lambda$1(this, i, requestInfo, func, func2));
        }
    }

    public void getDownloads(@NotNull Func<? super List<? extends Download>> func) {
        Intrinsics.checkParameterIsNotNull(func, "func");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$getDownloads$$inlined$synchronized$lambda$1(this, func));
        }
    }

    public void getDownload(int i, @NotNull Func2<? super Download> func2) {
        Intrinsics.checkParameterIsNotNull(func2, "func");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$getDownload$$inlined$synchronized$lambda$1(this, i, func2));
        }
    }

    public void getDownloads(@NotNull List<Integer> list, @NotNull Func<? super List<? extends Download>> func) {
        Intrinsics.checkParameterIsNotNull(list, "idList");
        Intrinsics.checkParameterIsNotNull(func, "func");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$getDownloads$$inlined$synchronized$lambda$2(this, list, func));
        }
    }

    public void getDownloadsInGroup(int i, @NotNull Func<? super List<? extends Download>> func) {
        Intrinsics.checkParameterIsNotNull(func, "func");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$getDownloadsInGroup$$inlined$synchronized$lambda$1(this, i, func));
        }
    }

    public void getDownloadsWithStatus(@NotNull Status status, @NotNull Func<? super List<? extends Download>> func) {
        Intrinsics.checkParameterIsNotNull(status, NotificationCompat.CATEGORY_STATUS);
        Intrinsics.checkParameterIsNotNull(func, "func");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$getDownloadsWithStatus$$inlined$synchronized$lambda$1(this, status, func));
        }
    }

    public void getDownloadsInGroupWithStatus(int i, @NotNull Status status, @NotNull Func<? super List<? extends Download>> func) {
        Intrinsics.checkParameterIsNotNull(status, NotificationCompat.CATEGORY_STATUS);
        Intrinsics.checkParameterIsNotNull(func, "func");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new C1345xd9dde2f8(this, i, status, func));
        }
    }

    public void addListener(@NotNull FetchListener fetchListener) {
        Intrinsics.checkParameterIsNotNull(fetchListener, CastExtraArgs.LISTENER);
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.fetchHandler.addListener(fetchListener);
            fetchListener = Unit.INSTANCE;
        }
    }

    public void removeListener(@NotNull FetchListener fetchListener) {
        Intrinsics.checkParameterIsNotNull(fetchListener, CastExtraArgs.LISTENER);
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.fetchHandler.removeListener(fetchListener);
            fetchListener = Unit.INSTANCE;
        }
    }

    public void setGlobalNetworkType(@NotNull NetworkType networkType) {
        Intrinsics.checkParameterIsNotNull(networkType, "networkType");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$setGlobalNetworkType$$inlined$synchronized$lambda$1(this, networkType));
        }
    }

    public void enableLogging(boolean z) {
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.handler.post(new FetchImpl$enableLogging$$inlined$synchronized$lambda$1(this, z));
        }
    }

    public void close() {
        synchronized (this.lock) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            Logger logger = this.logger;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(getNamespace());
            stringBuilder.append(" closing/shutting down");
            logger.mo4160d(stringBuilder.toString());
            try {
                this.fetchHandler.close();
            } catch (Exception e) {
                Logger logger2 = this.logger;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("exception occurred whiles shutting down Fetch with namespace:");
                stringBuilder2.append(getNamespace());
                logger2.mo4163e(stringBuilder2.toString(), e);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    protected final void throwExceptionIfClosed() {
        if (this.closed) {
            throw new FetchException("This fetch instance has been closed. Create a new instance using the builder.", Code.CLOSED);
        }
    }
}
