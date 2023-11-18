package com.tonyodev.fetch2.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.tonyodev.fetch2.Status;
import java.util.List;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Dao
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H'J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007H'J\b\u0010\b\u001a\u00020\u0003H'J\u000e\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007H'J\u0012\u0010\t\u001a\u0004\u0018\u00010\u00052\u0006\u0010\n\u001a\u00020\u000bH'J\u001c\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\u00072\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0007H'J\u0012\u0010\r\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000e\u001a\u00020\u000fH'J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00050\u00072\u0006\u0010\u0011\u001a\u00020\u000bH'J\u001e\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00050\u00072\u0006\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u0014H'J\u0016\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00050\u00072\u0006\u0010\u0013\u001a\u00020\u0014H'J\u0016\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00050\u00072\u0006\u0010\u0013\u001a\u00020\u0014H'J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0004\u001a\u00020\u0005H'J\u001c\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u00072\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007H'J\u0010\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u0005H'J\u0016\u0010\u0019\u001a\u00020\u00032\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007H'¨\u0006\u001b"}, d2 = {"Lcom/tonyodev/fetch2/database/DownloadDao;", "", "delete", "", "downloadInfo", "Lcom/tonyodev/fetch2/database/DownloadInfo;", "downloadInfoList", "", "deleteAll", "get", "id", "", "ids", "getByFile", "file", "", "getByGroup", "group", "getByGroupWithStatus", "status", "Lcom/tonyodev/fetch2/Status;", "getByStatus", "getPendingDownloadsSorted", "insert", "", "update", "download", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: DownloadDao.kt */
public interface DownloadDao {
    @Delete
    void delete(@NotNull DownloadInfo downloadInfo);

    @Delete
    void delete(@NotNull List<DownloadInfo> list);

    @Query("DELETE FROM requests")
    void deleteAll();

    @Query("SELECT * FROM requests WHERE _id = :id")
    @Nullable
    DownloadInfo get(int i);

    @Query("SELECT * FROM requests")
    @NotNull
    List<DownloadInfo> get();

    @Query("SELECT * FROM requests WHERE _id IN (:ids)")
    @NotNull
    List<DownloadInfo> get(@NotNull List<Integer> list);

    @Query("SELECT * FROM requests WHERE _file = :file")
    @Nullable
    DownloadInfo getByFile(@NotNull String str);

    @Query("SELECT * FROM requests WHERE _group = :group")
    @NotNull
    List<DownloadInfo> getByGroup(int i);

    @Query("SELECT * FROM requests WHERE _group = :group AND _status = :status")
    @NotNull
    List<DownloadInfo> getByGroupWithStatus(int i, @NotNull Status status);

    @Query("SELECT * FROM requests WHERE _status = :status")
    @NotNull
    List<DownloadInfo> getByStatus(@NotNull Status status);

    @Query("SELECT * FROM requests WHERE _status = :status ORDER BY _priority DESC, _created ASC")
    @NotNull
    List<DownloadInfo> getPendingDownloadsSorted(@NotNull Status status);

    @Insert(onConflict = 3)
    long insert(@NotNull DownloadInfo downloadInfo);

    @Insert(onConflict = 3)
    @NotNull
    List<Long> insert(@NotNull List<DownloadInfo> list);

    @Update(onConflict = 1)
    void update(@NotNull DownloadInfo downloadInfo);

    @Update(onConflict = 1)
    void update(@NotNull List<DownloadInfo> list);
}
