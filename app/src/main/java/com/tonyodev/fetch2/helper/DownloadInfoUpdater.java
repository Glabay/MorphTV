package com.tonyodev.fetch2.helper;

import com.tonyodev.fetch2.database.DatabaseManager;
import com.tonyodev.fetch2.database.DownloadInfo;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lcom/tonyodev/fetch2/helper/DownloadInfoUpdater;", "", "databaseManager", "Lcom/tonyodev/fetch2/database/DatabaseManager;", "(Lcom/tonyodev/fetch2/database/DatabaseManager;)V", "deleteDownload", "", "downloadInfo", "Lcom/tonyodev/fetch2/database/DownloadInfo;", "update", "updateFileBytesInfoAndStatusOnly", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: DownloadInfoUpdater.kt */
public final class DownloadInfoUpdater {
    private final DatabaseManager databaseManager;

    public DownloadInfoUpdater(@NotNull DatabaseManager databaseManager) {
        Intrinsics.checkParameterIsNotNull(databaseManager, "databaseManager");
        this.databaseManager = databaseManager;
    }

    public final void updateFileBytesInfoAndStatusOnly(@NotNull DownloadInfo downloadInfo) {
        Intrinsics.checkParameterIsNotNull(downloadInfo, "downloadInfo");
        this.databaseManager.updateFileBytesInfoAndStatusOnly(downloadInfo);
    }

    public final void update(@NotNull DownloadInfo downloadInfo) {
        Intrinsics.checkParameterIsNotNull(downloadInfo, "downloadInfo");
        this.databaseManager.update(downloadInfo);
    }

    public final void deleteDownload(@NotNull DownloadInfo downloadInfo) {
        Intrinsics.checkParameterIsNotNull(downloadInfo, "downloadInfo");
        this.databaseManager.delete(downloadInfo);
    }
}
