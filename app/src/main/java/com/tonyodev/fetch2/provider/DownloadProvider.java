package com.tonyodev.fetch2.provider;

import android.support.v4.app.NotificationCompat;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Status;
import com.tonyodev.fetch2.database.DatabaseManager;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\tJ\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u000b\u001a\u00020\fJ\u0010\u0010\r\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u000e\u001a\u00020\tJ\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006J\u001c\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u00062\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\t0\u0006J\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lcom/tonyodev/fetch2/provider/DownloadProvider;", "", "databaseManager", "Lcom/tonyodev/fetch2/database/DatabaseManager;", "(Lcom/tonyodev/fetch2/database/DatabaseManager;)V", "getByGroup", "", "Lcom/tonyodev/fetch2/Download;", "group", "", "getByStatus", "status", "Lcom/tonyodev/fetch2/Status;", "getDownload", "id", "getDownloads", "ids", "getPendingDownloadsSorted", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: DownloadProvider.kt */
public final class DownloadProvider {
    private final DatabaseManager databaseManager;

    public DownloadProvider(@NotNull DatabaseManager databaseManager) {
        Intrinsics.checkParameterIsNotNull(databaseManager, "databaseManager");
        this.databaseManager = databaseManager;
    }

    @NotNull
    public final List<Download> getDownloads() {
        return this.databaseManager.get();
    }

    @Nullable
    public final Download getDownload(int i) {
        return this.databaseManager.get(i);
    }

    @NotNull
    public final List<Download> getDownloads(@NotNull List<Integer> list) {
        Intrinsics.checkParameterIsNotNull(list, "ids");
        return this.databaseManager.get((List) list);
    }

    @NotNull
    public final List<Download> getByGroup(int i) {
        return this.databaseManager.getByGroup(i);
    }

    @NotNull
    public final List<Download> getByStatus(@NotNull Status status) {
        Intrinsics.checkParameterIsNotNull(status, NotificationCompat.CATEGORY_STATUS);
        return this.databaseManager.getByStatus(status);
    }

    @NotNull
    public final List<Download> getPendingDownloadsSorted() {
        return this.databaseManager.getPendingDownloadsSorted();
    }
}
