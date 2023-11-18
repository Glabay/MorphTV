package com.tonyodev.fetch2.util;

import com.android.morpheustv.service.BackgroundService;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Status;
import com.tonyodev.fetch2.database.DatabaseManager;
import com.tonyodev.fetch2.database.DownloadInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmName;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u001a\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\u001a \u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u0001H\u0007\u001a\u0016\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0005\u001a\u00020\u0001H\u0007\u001a$\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u00072\b\b\u0002\u0010\u0005\u001a\u00020\u0001H\u0007¨\u0006\b"}, d2 = {"sanitize", "", "Lcom/tonyodev/fetch2/database/DatabaseManager;", "downloadInfo", "Lcom/tonyodev/fetch2/database/DownloadInfo;", "initializing", "downloads", "", "fetch2_release"}, k = 2, mv = {1, 1, 10})
@JvmName(name = "FetchDatabaseExtensions")
/* compiled from: DatabaseExtensions.kt */
public final class FetchDatabaseExtensions {
    @JvmOverloads
    public static final boolean sanitize(@NotNull DatabaseManager databaseManager) {
        return sanitize$default(databaseManager, false, 1, null);
    }

    @JvmOverloads
    public static final boolean sanitize(@NotNull DatabaseManager databaseManager, @Nullable DownloadInfo downloadInfo) {
        return sanitize$default(databaseManager, downloadInfo, false, 2, null);
    }

    @JvmOverloads
    public static final boolean sanitize(@NotNull DatabaseManager databaseManager, @NotNull List<DownloadInfo> list) {
        return sanitize$default(databaseManager, (List) list, false, 2, null);
    }

    @JvmOverloads
    public static /* bridge */ /* synthetic */ boolean sanitize$default(DatabaseManager databaseManager, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        return sanitize(databaseManager, z);
    }

    @JvmOverloads
    public static final boolean sanitize(@NotNull DatabaseManager databaseManager, boolean z) {
        Intrinsics.checkParameterIsNotNull(databaseManager, "$receiver");
        return sanitize(databaseManager, databaseManager.get(), z);
    }

    @JvmOverloads
    public static final boolean sanitize(@NotNull DatabaseManager databaseManager, @NotNull List<DownloadInfo> list, boolean z) {
        DatabaseManager databaseManager2 = databaseManager;
        List<DownloadInfo> list2 = list;
        Intrinsics.checkParameterIsNotNull(databaseManager2, "$receiver");
        Intrinsics.checkParameterIsNotNull(list2, BackgroundService.DOWNLOADS_NOTIFICATION_CHANNEL);
        List arrayList = new ArrayList();
        int size = list.size();
        int i = 0;
        while (true) {
            Object obj = 1;
            if (i < size) {
                DownloadInfo downloadInfo = (DownloadInfo) list2.get(i);
                boolean exists = new File(downloadInfo.getFile()).exists();
                switch (downloadInfo.getStatus()) {
                    case PAUSED:
                    case COMPLETED:
                    case CANCELLED:
                    case REMOVED:
                    case FAILED:
                    case QUEUED:
                        if (!exists && downloadInfo.getStatus() == Status.COMPLETED) {
                            downloadInfo.setStatus(Status.FAILED);
                            downloadInfo.setError(Error.FILE_NOT_FOUND);
                            downloadInfo.setDownloaded(0);
                            downloadInfo.setTotal(-1);
                            arrayList.add(downloadInfo);
                            break;
                        }
                        if (downloadInfo.getStatus() != Status.COMPLETED || downloadInfo.getTotal() >= 1 || downloadInfo.getDownloaded() <= 0 || !exists) {
                            obj = null;
                        } else {
                            downloadInfo.setTotal(downloadInfo.getDownloaded());
                        }
                        if (obj == null) {
                            break;
                        }
                        arrayList.add(downloadInfo);
                        break;
                        break;
                    case DOWNLOADING:
                        if (!z) {
                            break;
                        }
                        downloadInfo.setStatus(Status.QUEUED);
                        arrayList.add(downloadInfo);
                        break;
                    default:
                        break;
                }
                i++;
            } else {
                if (arrayList.size() > 0) {
                    try {
                        databaseManager2.updateNoLock(arrayList);
                    } catch (Exception e) {
                        databaseManager.getLogger().mo4163e("Database sanitize update error", e);
                    }
                }
                if (arrayList.size() > 0) {
                    return true;
                }
                return false;
            }
        }
    }

    @JvmOverloads
    public static final boolean sanitize(@NotNull DatabaseManager databaseManager, @Nullable DownloadInfo downloadInfo, boolean z) {
        Intrinsics.checkParameterIsNotNull(databaseManager, "$receiver");
        return downloadInfo == null ? null : sanitize(databaseManager, CollectionsKt.listOf(downloadInfo), z);
    }
}
