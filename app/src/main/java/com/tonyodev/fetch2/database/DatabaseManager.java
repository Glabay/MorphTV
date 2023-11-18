package com.tonyodev.fetch2.database;

import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.Status;
import java.io.Closeable;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH&J\u0016\u0010\n\u001a\u00020\u000b2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH&J\b\u0010\u0010\u001a\u00020\u000bH&J\u000e\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH&J\u0012\u0010\u0011\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0012\u001a\u00020\u0013H&J\u001e\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\u000f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00130\u000fH&J\u0012\u0010\u0015\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0016\u001a\u00020\u0017H&J\u0016\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\r0\u000f2\u0006\u0010\u0019\u001a\u00020\u0013H&J\u0016\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\r0\u000f2\u0006\u0010\u001b\u001a\u00020\u001cH&J\u001e\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u000f2\u0006\u0010\u001e\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u001cH&J\u000e\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH&J\u001c\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00030!2\u0006\u0010\f\u001a\u00020\rH&J(\u0010 \u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00030!0\u000f2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH&J\u0010\u0010\"\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH&J\u0016\u0010\"\u001a\u00020\u000b2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH&J\u0010\u0010#\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH&J\u0016\u0010$\u001a\u00020\u000b2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0004R\u0012\u0010\u0005\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0004R\u0012\u0010\u0006\u001a\u00020\u0007X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006%"}, d2 = {"Lcom/tonyodev/fetch2/database/DatabaseManager;", "Ljava/io/Closeable;", "isClosed", "", "()Z", "isMemoryDatabase", "logger", "Lcom/tonyodev/fetch2/Logger;", "getLogger", "()Lcom/tonyodev/fetch2/Logger;", "delete", "", "downloadInfo", "Lcom/tonyodev/fetch2/database/DownloadInfo;", "downloadInfoList", "", "deleteAll", "get", "id", "", "ids", "getByFile", "file", "", "getByGroup", "group", "getByStatus", "status", "Lcom/tonyodev/fetch2/Status;", "getDownloadsInGroupWithStatus", "groupId", "getPendingDownloadsSorted", "insert", "Lkotlin/Pair;", "update", "updateFileBytesInfoAndStatusOnly", "updateNoLock", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: DatabaseManager.kt */
public interface DatabaseManager extends Closeable {
    void delete(@NotNull DownloadInfo downloadInfo);

    void delete(@NotNull List<DownloadInfo> list);

    void deleteAll();

    @Nullable
    DownloadInfo get(int i);

    @NotNull
    List<DownloadInfo> get();

    @NotNull
    List<DownloadInfo> get(@NotNull List<Integer> list);

    @Nullable
    DownloadInfo getByFile(@NotNull String str);

    @NotNull
    List<DownloadInfo> getByGroup(int i);

    @NotNull
    List<DownloadInfo> getByStatus(@NotNull Status status);

    @NotNull
    List<DownloadInfo> getDownloadsInGroupWithStatus(int i, @NotNull Status status);

    @NotNull
    Logger getLogger();

    @NotNull
    List<DownloadInfo> getPendingDownloadsSorted();

    @NotNull
    List<Pair<DownloadInfo, Boolean>> insert(@NotNull List<DownloadInfo> list);

    @NotNull
    Pair<DownloadInfo, Boolean> insert(@NotNull DownloadInfo downloadInfo);

    boolean isClosed();

    boolean isMemoryDatabase();

    void update(@NotNull DownloadInfo downloadInfo);

    void update(@NotNull List<DownloadInfo> list);

    void updateFileBytesInfoAndStatusOnly(@NotNull DownloadInfo downloadInfo);

    void updateNoLock(@NotNull List<DownloadInfo> list);
}
