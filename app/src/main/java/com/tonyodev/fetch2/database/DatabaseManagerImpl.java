package com.tonyodev.fetch2.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.common.server.FavaDiagnosticsEntity;
import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.Status;
import com.tonyodev.fetch2.database.migration.Migration;
import com.tonyodev.fetch2.exception.FetchException.Code;
import com.tonyodev.fetch2.exception.FetchImplementationException;
import com.tonyodev.fetch2.util.FetchDatabaseExtensions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B3\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b¢\u0006\u0002\u0010\rJ\b\u0010\u001b\u001a\u00020\u001cH\u0016J\u0010\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0016\u0010\u001d\u001a\u00020\u001c2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0!H\u0016J\b\u0010\"\u001a\u00020\u001cH\u0016J\u000e\u0010#\u001a\b\u0012\u0004\u0012\u00020\u001f0!H\u0016J\u0012\u0010#\u001a\u0004\u0018\u00010\u001f2\u0006\u0010$\u001a\u00020%H\u0016J\u001e\u0010#\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001f0!2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020%0!H\u0016J\u0012\u0010'\u001a\u0004\u0018\u00010\u001f2\u0006\u0010(\u001a\u00020\u0005H\u0016J\u0016\u0010)\u001a\b\u0012\u0004\u0012\u00020\u001f0!2\u0006\u0010*\u001a\u00020%H\u0016J\u0016\u0010+\u001a\b\u0012\u0004\u0012\u00020\u001f0!2\u0006\u0010,\u001a\u00020-H\u0016J\u001e\u0010.\u001a\b\u0012\u0004\u0012\u00020\u001f0!2\u0006\u0010/\u001a\u00020%2\u0006\u0010,\u001a\u00020-H\u0016J\u000e\u00100\u001a\b\u0012\u0004\u0012\u00020\u001f0!H\u0016J\u001c\u00101\u001a\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u0007022\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J(\u00101\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u0007020!2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0!H\u0016J\b\u00103\u001a\u00020\u001cH\u0002J\u0010\u00104\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0016\u00104\u001a\u00020\u001c2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0!H\u0016J\u0010\u00105\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0016\u00106\u001a\u00020\u001c2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0!H\u0016R\u0012\u0010\u000e\u001a\u00020\u00078\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u000f\u001a\u00020\u0010¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0014\u0010\u0013\u001a\u00020\u00078VX\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\tX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0004¢\u0006\u0002\n\u0000¨\u00067"}, d2 = {"Lcom/tonyodev/fetch2/database/DatabaseManagerImpl;", "Lcom/tonyodev/fetch2/database/DatabaseManager;", "context", "Landroid/content/Context;", "namespace", "", "isMemoryDatabase", "", "logger", "Lcom/tonyodev/fetch2/Logger;", "migrations", "", "Lcom/tonyodev/fetch2/database/migration/Migration;", "(Landroid/content/Context;Ljava/lang/String;ZLcom/tonyodev/fetch2/Logger;[Lcom/tonyodev/fetch2/database/migration/Migration;)V", "closed", "database", "Landroid/arch/persistence/db/SupportSQLiteDatabase;", "getDatabase", "()Landroid/arch/persistence/db/SupportSQLiteDatabase;", "isClosed", "()Z", "lock", "Ljava/lang/Object;", "getLogger", "()Lcom/tonyodev/fetch2/Logger;", "requestDatabase", "Lcom/tonyodev/fetch2/database/DownloadDatabase;", "close", "", "delete", "downloadInfo", "Lcom/tonyodev/fetch2/database/DownloadInfo;", "downloadInfoList", "", "deleteAll", "get", "id", "", "ids", "getByFile", "file", "getByGroup", "group", "getByStatus", "status", "Lcom/tonyodev/fetch2/Status;", "getDownloadsInGroupWithStatus", "groupId", "getPendingDownloadsSorted", "insert", "Lkotlin/Pair;", "throwExceptionIfClosed", "update", "updateFileBytesInfoAndStatusOnly", "updateNoLock", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: DatabaseManagerImpl.kt */
public final class DatabaseManagerImpl implements DatabaseManager {
    private volatile boolean closed;
    @NotNull
    private final SupportSQLiteDatabase database;
    private final boolean isMemoryDatabase;
    private final Object lock = new Object();
    @NotNull
    private final Logger logger;
    private final String namespace;
    private final DownloadDatabase requestDatabase;

    public DatabaseManagerImpl(@NotNull Context context, @NotNull String str, boolean z, @NotNull Logger logger, @NotNull Migration[] migrationArr) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, FavaDiagnosticsEntity.EXTRA_NAMESPACE);
        Intrinsics.checkParameterIsNotNull(logger, "logger");
        Intrinsics.checkParameterIsNotNull(migrationArr, "migrations");
        this.namespace = str;
        this.isMemoryDatabase = z;
        this.logger = logger;
        this.requestDatabase = (DownloadDatabase) ((Function0) new DatabaseManagerImpl$requestDatabase$1(this, context, migrationArr)).invoke();
        context = this.requestDatabase;
        Intrinsics.checkExpressionValueIsNotNull(context, "requestDatabase");
        context = context.getOpenHelper();
        Intrinsics.checkExpressionValueIsNotNull(context, "requestDatabase.openHelper");
        context = context.getWritableDatabase();
        Intrinsics.checkExpressionValueIsNotNull(context, "requestDatabase.openHelper.writableDatabase");
        this.database = context;
    }

    public boolean isMemoryDatabase() {
        return this.isMemoryDatabase;
    }

    @NotNull
    public Logger getLogger() {
        return this.logger;
    }

    public boolean isClosed() {
        return this.closed;
    }

    @NotNull
    public final SupportSQLiteDatabase getDatabase() {
        return this.database;
    }

    @NotNull
    public Pair<DownloadInfo, Boolean> insert(@NotNull DownloadInfo downloadInfo) {
        Pair<DownloadInfo, Boolean> pair;
        Intrinsics.checkParameterIsNotNull(downloadInfo, "downloadInfo");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            pair = new Pair(downloadInfo, Boolean.valueOf(this.requestDatabase.wasRowInserted(this.requestDatabase.requestDao().insert(downloadInfo))));
        }
        return pair;
    }

    @NotNull
    public List<Pair<DownloadInfo, Boolean>> insert(@NotNull List<DownloadInfo> list) {
        List<Pair<DownloadInfo, Boolean>> list2;
        Intrinsics.checkParameterIsNotNull(list, "downloadInfoList");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            List insert = this.requestDatabase.requestDao().insert((List) list);
            Iterable indices = CollectionsKt.getIndices(insert);
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(indices, 10));
            Iterator it = indices.iterator();
            while (it.hasNext()) {
                int nextInt = ((IntIterator) it).nextInt();
                arrayList.add(new Pair(list.get(nextInt), Boolean.valueOf(this.requestDatabase.wasRowInserted(((Number) insert.get(nextInt)).longValue()))));
            }
            list2 = (List) arrayList;
        }
        return list2;
    }

    public void delete(@NotNull DownloadInfo downloadInfo) {
        Intrinsics.checkParameterIsNotNull(downloadInfo, "downloadInfo");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.requestDatabase.requestDao().delete(downloadInfo);
            downloadInfo = Unit.INSTANCE;
        }
    }

    public void delete(@NotNull List<DownloadInfo> list) {
        Intrinsics.checkParameterIsNotNull(list, "downloadInfoList");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.requestDatabase.requestDao().delete((List) list);
            list = Unit.INSTANCE;
        }
    }

    public void deleteAll() {
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.requestDatabase.requestDao().deleteAll();
            Logger logger = getLogger();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cleared Database ");
            stringBuilder.append(this.namespace);
            stringBuilder.append(".db");
            logger.mo4160d(stringBuilder.toString());
            Unit unit = Unit.INSTANCE;
        }
    }

    public void update(@NotNull DownloadInfo downloadInfo) {
        Intrinsics.checkParameterIsNotNull(downloadInfo, "downloadInfo");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            this.requestDatabase.requestDao().update(downloadInfo);
            downloadInfo = Unit.INSTANCE;
        }
    }

    public void update(@NotNull List<DownloadInfo> list) {
        Intrinsics.checkParameterIsNotNull(list, "downloadInfoList");
        synchronized (this.lock) {
            updateNoLock(list);
            list = Unit.INSTANCE;
        }
    }

    public void updateNoLock(@NotNull List<DownloadInfo> list) {
        Intrinsics.checkParameterIsNotNull(list, "downloadInfoList");
        throwExceptionIfClosed();
        this.requestDatabase.requestDao().update((List) list);
    }

    public void updateFileBytesInfoAndStatusOnly(@NotNull DownloadInfo downloadInfo) {
        Intrinsics.checkParameterIsNotNull(downloadInfo, "downloadInfo");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            try {
                this.database.beginTransaction();
                SupportSQLiteDatabase supportSQLiteDatabase = this.database;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("UPDATE requests SET ");
                stringBuilder.append("_written_bytes = ");
                stringBuilder.append(downloadInfo.getDownloaded());
                stringBuilder.append(", ");
                stringBuilder.append("_total_bytes = ");
                stringBuilder.append(downloadInfo.getTotal());
                stringBuilder.append(", ");
                stringBuilder.append("_status = ");
                stringBuilder.append(downloadInfo.getStatus().getValue());
                stringBuilder.append(' ');
                stringBuilder.append("WHERE _id = ");
                stringBuilder.append(downloadInfo.getId());
                supportSQLiteDatabase.execSQL(stringBuilder.toString());
                this.database.setTransactionSuccessful();
            } catch (DownloadInfo downloadInfo2) {
                getLogger().mo4163e("DatabaseManager exception", (Throwable) downloadInfo2);
            }
            try {
                this.database.endTransaction();
            } catch (DownloadInfo downloadInfo22) {
                getLogger().mo4163e("DatabaseManager exception", (Throwable) downloadInfo22);
            }
            downloadInfo22 = Unit.INSTANCE;
        }
        return;
    }

    @NotNull
    public List<DownloadInfo> get() {
        List list;
        synchronized (this.lock) {
            throwExceptionIfClosed();
            list = this.requestDatabase.requestDao().get();
            FetchDatabaseExtensions.sanitize$default((DatabaseManager) this, list, false, 2, null);
        }
        return list;
    }

    @Nullable
    public DownloadInfo get(int i) {
        DownloadInfo downloadInfo;
        synchronized (this.lock) {
            throwExceptionIfClosed();
            downloadInfo = this.requestDatabase.requestDao().get(i);
            FetchDatabaseExtensions.sanitize$default((DatabaseManager) this, downloadInfo, false, 2, null);
        }
        return downloadInfo;
    }

    @NotNull
    public List<DownloadInfo> get(@NotNull List<Integer> list) {
        List list2;
        Intrinsics.checkParameterIsNotNull(list, "ids");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            list2 = this.requestDatabase.requestDao().get((List) list);
            FetchDatabaseExtensions.sanitize$default((DatabaseManager) this, list2, false, 2, null);
        }
        return list2;
    }

    @Nullable
    public DownloadInfo getByFile(@NotNull String str) {
        DownloadInfo byFile;
        Intrinsics.checkParameterIsNotNull(str, "file");
        synchronized (this.lock) {
            throwExceptionIfClosed();
            byFile = this.requestDatabase.requestDao().getByFile(str);
            FetchDatabaseExtensions.sanitize$default((DatabaseManager) this, byFile, false, 2, null);
        }
        return byFile;
    }

    @NotNull
    public List<DownloadInfo> getByStatus(@NotNull Status status) {
        List<DownloadInfo> byStatus;
        Intrinsics.checkParameterIsNotNull(status, NotificationCompat.CATEGORY_STATUS);
        synchronized (this.lock) {
            throwExceptionIfClosed();
            byStatus = this.requestDatabase.requestDao().getByStatus(status);
            if (FetchDatabaseExtensions.sanitize$default((DatabaseManager) this, (List) byStatus, false, 2, null)) {
                Collection arrayList = new ArrayList();
                for (Object next : byStatus) {
                    if ((((DownloadInfo) next).getStatus() == status ? 1 : null) != null) {
                        arrayList.add(next);
                    }
                }
                byStatus = (List) arrayList;
            }
        }
        return byStatus;
    }

    @NotNull
    public List<DownloadInfo> getByGroup(int i) {
        List byGroup;
        synchronized (this.lock) {
            throwExceptionIfClosed();
            byGroup = this.requestDatabase.requestDao().getByGroup(i);
            FetchDatabaseExtensions.sanitize$default((DatabaseManager) this, byGroup, false, 2, null);
        }
        return byGroup;
    }

    @NotNull
    public List<DownloadInfo> getDownloadsInGroupWithStatus(int i, @NotNull Status status) {
        Intrinsics.checkParameterIsNotNull(status, NotificationCompat.CATEGORY_STATUS);
        synchronized (this.lock) {
            throwExceptionIfClosed();
            i = this.requestDatabase.requestDao().getByGroupWithStatus(i, status);
            if (FetchDatabaseExtensions.sanitize$default((DatabaseManager) this, (List) i, false, 2, null)) {
                Collection arrayList = new ArrayList();
                for (Object next : (Iterable) i) {
                    if ((((DownloadInfo) next).getStatus() == status ? 1 : null) != null) {
                        arrayList.add(next);
                    }
                }
                i = (List) arrayList;
            }
        }
        return i;
    }

    @NotNull
    public List<DownloadInfo> getPendingDownloadsSorted() {
        List<DownloadInfo> pendingDownloadsSorted;
        synchronized (this.lock) {
            throwExceptionIfClosed();
            pendingDownloadsSorted = this.requestDatabase.requestDao().getPendingDownloadsSorted(Status.QUEUED);
            if (FetchDatabaseExtensions.sanitize$default((DatabaseManager) this, (List) pendingDownloadsSorted, false, 2, null)) {
                Collection arrayList = new ArrayList();
                for (Object next : pendingDownloadsSorted) {
                    if ((((DownloadInfo) next).getStatus() == Status.QUEUED ? 1 : null) != null) {
                        arrayList.add(next);
                    }
                }
                pendingDownloadsSorted = (List) arrayList;
            }
        }
        return pendingDownloadsSorted;
    }

    public void close() {
        synchronized (this.lock) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            this.requestDatabase.close();
            getLogger().mo4160d("Database closed");
            Unit unit = Unit.INSTANCE;
        }
    }

    private final void throwExceptionIfClosed() {
        if (this.closed) {
            throw new FetchImplementationException("database is closed", Code.CLOSED);
        }
    }
}
