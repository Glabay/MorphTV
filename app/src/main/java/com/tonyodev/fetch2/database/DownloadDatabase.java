package com.tonyodev.fetch2.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import com.tonyodev.fetch2.database.migration.Migration;
import com.tonyodev.fetch2.database.migration.MigrationOneToTwo;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import org.jetbrains.annotations.NotNull;

@Database(entities = {DownloadInfo.class}, exportSchema = false, version = 2)
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\b'\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b¨\u0006\n"}, d2 = {"Lcom/tonyodev/fetch2/database/DownloadDatabase;", "Landroid/arch/persistence/room/RoomDatabase;", "()V", "requestDao", "Lcom/tonyodev/fetch2/database/DownloadDao;", "wasRowInserted", "", "row", "", "Companion", "fetch2_release"}, k = 1, mv = {1, 1, 10})
@TypeConverters({Converter.class})
/* compiled from: DownloadDatabase.kt */
public abstract class DownloadDatabase extends RoomDatabase {
    @NotNull
    public static final String COLUMN_CREATED = "_created";
    @NotNull
    public static final String COLUMN_DOWNLOADED = "_written_bytes";
    @NotNull
    public static final String COLUMN_ERROR = "_error";
    @NotNull
    public static final String COLUMN_FILE = "_file";
    @NotNull
    public static final String COLUMN_GROUP = "_group";
    @NotNull
    public static final String COLUMN_HEADERS = "_headers";
    @NotNull
    public static final String COLUMN_ID = "_id";
    @NotNull
    public static final String COLUMN_NAMESPACE = "_namespace";
    @NotNull
    public static final String COLUMN_NETWORK_TYPE = "_network_type";
    @NotNull
    public static final String COLUMN_PRIORITY = "_priority";
    @NotNull
    public static final String COLUMN_STATUS = "_status";
    @NotNull
    public static final String COLUMN_TAG = "_tag";
    @NotNull
    public static final String COLUMN_TOTAL = "_total_bytes";
    @NotNull
    public static final String COLUMN_URL = "_url";
    public static final Companion Companion = new Companion();
    public static final int DATABASE_VERSION = 2;
    public static final int OLD_DATABASE_VERSION = 1;
    @NotNull
    public static final String TABLE_NAME = "requests";

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0013\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017H\u0007¢\u0006\u0002\u0010\u0019R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0013XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lcom/tonyodev/fetch2/database/DownloadDatabase$Companion;", "", "()V", "COLUMN_CREATED", "", "COLUMN_DOWNLOADED", "COLUMN_ERROR", "COLUMN_FILE", "COLUMN_GROUP", "COLUMN_HEADERS", "COLUMN_ID", "COLUMN_NAMESPACE", "COLUMN_NETWORK_TYPE", "COLUMN_PRIORITY", "COLUMN_STATUS", "COLUMN_TAG", "COLUMN_TOTAL", "COLUMN_URL", "DATABASE_VERSION", "", "OLD_DATABASE_VERSION", "TABLE_NAME", "getMigrations", "", "Lcom/tonyodev/fetch2/database/migration/Migration;", "()[Lcom/tonyodev/fetch2/database/migration/Migration;", "fetch2_release"}, k = 1, mv = {1, 1, 10})
    /* compiled from: DownloadDatabase.kt */
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final Migration[] getMigrations() {
            return new Migration[]{new MigrationOneToTwo()};
        }
    }

    @JvmStatic
    @NotNull
    public static final Migration[] getMigrations() {
        return Companion.getMigrations();
    }

    @NotNull
    public abstract DownloadDao requestDao();

    public final boolean wasRowInserted(long j) {
        return j != ((long) -1);
    }
}
