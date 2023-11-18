package com.tonyodev.fetch2.fetch;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2.RequestInfo;
import com.tonyodev.fetch2.Status;
import com.tonyodev.fetch2.provider.ListenerProvider;
import java.io.Closeable;
import java.util.List;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&J\u0016\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\r\u001a\u00020\u000eH&J\u000e\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH&J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H&J\u0016\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u0012\u001a\u00020\u0013H&J\u0016\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\r\u001a\u00020\u000eH&J\u000e\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH&J\u0016\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u0018\u001a\u00020\u0019H&J\u0016\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u0012\u001a\u00020\u0013H&J\u0010\u0010\u001b\u001a\u00020\u00072\u0006\u0010\u001c\u001a\u00020\u0011H&J\u0010\u0010\u001d\u001a\u00020\f2\u0006\u0010\u001e\u001a\u00020\u001fH&J\u001c\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0\u000bH&J\b\u0010!\u001a\u00020\u0007H&J\u0012\u0010\"\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0012\u001a\u00020\u0013H&J\u000e\u0010#\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH&J\u001c\u0010#\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00130\u000bH&J\u0016\u0010%\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u0012\u001a\u00020\u0013H&J\u001e\u0010&\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010'\u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u0019H&J\u0016\u0010(\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u0018\u001a\u00020\u0019H&J\b\u0010)\u001a\u00020\u0007H&J\u0010\u0010*\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H&J\u0016\u0010+\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\r\u001a\u00020\u000eH&J\u0016\u0010,\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u0012\u001a\u00020\u0013H&J\u0016\u0010-\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\r\u001a\u00020\u000eH&J\u000e\u0010.\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH&J\u0016\u0010/\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u0018\u001a\u00020\u0019H&J\u0016\u00100\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u0012\u001a\u00020\u0013H&J\u0010\u00101\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&J\u0016\u00102\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\r\u001a\u00020\u000eH&J\u0016\u00103\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\u0012\u001a\u00020\u0013H&J\u0016\u00104\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\r\u001a\u00020\u000eH&J\u0010\u00105\u001a\u00020\u00072\u0006\u00106\u001a\u000207H&J\b\u00108\u001a\u00020\u0007H&J\u001a\u00109\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010:\u001a\u00020;H&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006<"}, d2 = {"Lcom/tonyodev/fetch2/fetch/FetchHandler;", "Ljava/io/Closeable;", "fetchListenerProvider", "Lcom/tonyodev/fetch2/provider/ListenerProvider;", "getFetchListenerProvider", "()Lcom/tonyodev/fetch2/provider/ListenerProvider;", "addListener", "", "listener", "Lcom/tonyodev/fetch2/FetchListener;", "cancel", "", "Lcom/tonyodev/fetch2/Download;", "ids", "", "cancelAll", "cancelDownload", "", "id", "", "cancelGroup", "delete", "deleteAll", "deleteAllWithStatus", "status", "Lcom/tonyodev/fetch2/Status;", "deleteGroup", "enableLogging", "enabled", "enqueue", "request", "Lcom/tonyodev/fetch2/Request;", "requests", "freeze", "getDownload", "getDownloads", "idList", "getDownloadsInGroup", "getDownloadsInGroupWithStatus", "groupId", "getDownloadsWithStatus", "init", "isDownloading", "pause", "pausedGroup", "remove", "removeAll", "removeAllWithStatus", "removeGroup", "removeListener", "resume", "resumeGroup", "retry", "setGlobalNetworkType", "networkType", "Lcom/tonyodev/fetch2/NetworkType;", "unfreeze", "updateRequest", "requestInfo", "Lcom/tonyodev/fetch2/RequestInfo;", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: FetchHandler.kt */
public interface FetchHandler extends Closeable {
    void addListener(@NotNull FetchListener fetchListener);

    @NotNull
    List<Download> cancel(@NotNull int[] iArr);

    @NotNull
    List<Download> cancelAll();

    boolean cancelDownload(int i);

    @NotNull
    List<Download> cancelGroup(int i);

    @NotNull
    List<Download> delete(@NotNull int[] iArr);

    @NotNull
    List<Download> deleteAll();

    @NotNull
    List<Download> deleteAllWithStatus(@NotNull Status status);

    @NotNull
    List<Download> deleteGroup(int i);

    void enableLogging(boolean z);

    @NotNull
    Download enqueue(@NotNull Request request);

    @NotNull
    List<Download> enqueue(@NotNull List<? extends Request> list);

    void freeze();

    @Nullable
    Download getDownload(int i);

    @NotNull
    List<Download> getDownloads();

    @NotNull
    List<Download> getDownloads(@NotNull List<Integer> list);

    @NotNull
    List<Download> getDownloadsInGroup(int i);

    @NotNull
    List<Download> getDownloadsInGroupWithStatus(int i, @NotNull Status status);

    @NotNull
    List<Download> getDownloadsWithStatus(@NotNull Status status);

    @NotNull
    ListenerProvider getFetchListenerProvider();

    void init();

    boolean isDownloading(int i);

    @NotNull
    List<Download> pause(@NotNull int[] iArr);

    @NotNull
    List<Download> pausedGroup(int i);

    @NotNull
    List<Download> remove(@NotNull int[] iArr);

    @NotNull
    List<Download> removeAll();

    @NotNull
    List<Download> removeAllWithStatus(@NotNull Status status);

    @NotNull
    List<Download> removeGroup(int i);

    void removeListener(@NotNull FetchListener fetchListener);

    @NotNull
    List<Download> resume(@NotNull int[] iArr);

    @NotNull
    List<Download> resumeGroup(int i);

    @NotNull
    List<Download> retry(@NotNull int[] iArr);

    void setGlobalNetworkType(@NotNull NetworkType networkType);

    void unfreeze();

    @Nullable
    Download updateRequest(int i, @NotNull RequestInfo requestInfo);
}
