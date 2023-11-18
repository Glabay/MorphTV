package android.arch.persistence.room;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import java.util.List;

@RestrictTo({Scope.LIBRARY_GROUP})
public class RoomOpenHelper extends Callback {
    @Nullable
    private DatabaseConfiguration mConfiguration;
    @NonNull
    private final Delegate mDelegate;
    @NonNull
    private final String mIdentityHash;

    @RestrictTo({Scope.LIBRARY_GROUP})
    public static abstract class Delegate {
        public final int version;

        protected abstract void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase);

        protected abstract void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase);

        protected abstract void onCreate(SupportSQLiteDatabase supportSQLiteDatabase);

        protected abstract void onOpen(SupportSQLiteDatabase supportSQLiteDatabase);

        protected abstract void validateMigration(SupportSQLiteDatabase supportSQLiteDatabase);

        public Delegate(int i) {
            this.version = i;
        }
    }

    public RoomOpenHelper(@NonNull DatabaseConfiguration databaseConfiguration, @NonNull Delegate delegate, @NonNull String str) {
        super(delegate.version);
        this.mConfiguration = databaseConfiguration;
        this.mDelegate = delegate;
        this.mIdentityHash = str;
    }

    public void onConfigure(SupportSQLiteDatabase supportSQLiteDatabase) {
        super.onConfigure(supportSQLiteDatabase);
    }

    public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
        updateIdentity(supportSQLiteDatabase);
        this.mDelegate.createAllTables(supportSQLiteDatabase);
        this.mDelegate.onCreate(supportSQLiteDatabase);
    }

    public void onUpgrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
        Object obj;
        if (this.mConfiguration != null) {
            List<Migration> findMigrationPath = this.mConfiguration.migrationContainer.findMigrationPath(i, i2);
            if (findMigrationPath != null) {
                for (Migration migrate : findMigrationPath) {
                    migrate.migrate(supportSQLiteDatabase);
                }
                this.mDelegate.validateMigration(supportSQLiteDatabase);
                updateIdentity(supportSQLiteDatabase);
                obj = 1;
                if (obj == null) {
                    if (this.mConfiguration != null) {
                        if (this.mConfiguration.requireMigration) {
                            this.mDelegate.dropAllTables(supportSQLiteDatabase);
                            this.mDelegate.createAllTables(supportSQLiteDatabase);
                            return;
                        }
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("A migration from ");
                    stringBuilder.append(i);
                    stringBuilder.append(" to ");
                    stringBuilder.append(i2);
                    stringBuilder.append(" is necessary. Please provide a Migration in the builder or call");
                    stringBuilder.append(" fallbackToDestructiveMigration in the builder in which case Room will");
                    stringBuilder.append(" re-create all of the tables.");
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
        }
        obj = null;
        if (obj == null) {
            if (this.mConfiguration != null) {
                if (this.mConfiguration.requireMigration) {
                    this.mDelegate.dropAllTables(supportSQLiteDatabase);
                    this.mDelegate.createAllTables(supportSQLiteDatabase);
                    return;
                }
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("A migration from ");
            stringBuilder2.append(i);
            stringBuilder2.append(" to ");
            stringBuilder2.append(i2);
            stringBuilder2.append(" is necessary. Please provide a Migration in the builder or call");
            stringBuilder2.append(" fallbackToDestructiveMigration in the builder in which case Room will");
            stringBuilder2.append(" re-create all of the tables.");
            throw new IllegalStateException(stringBuilder2.toString());
        }
    }

    public void onDowngrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
        onUpgrade(supportSQLiteDatabase, i, i2);
    }

    public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
        super.onOpen(supportSQLiteDatabase);
        checkIdentity(supportSQLiteDatabase);
        this.mDelegate.onOpen(supportSQLiteDatabase);
        this.mConfiguration = null;
    }

    private void checkIdentity(SupportSQLiteDatabase supportSQLiteDatabase) {
        createMasterTableIfNotExists(supportSQLiteDatabase);
        Object obj = "";
        supportSQLiteDatabase = supportSQLiteDatabase.query(new SimpleSQLiteQuery(RoomMasterTable.READ_QUERY));
        try {
            if (supportSQLiteDatabase.moveToFirst()) {
                obj = supportSQLiteDatabase.getString(0);
            }
            supportSQLiteDatabase.close();
            if (this.mIdentityHash.equals(obj) == null) {
                throw new IllegalStateException("Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number. You can simply fix this by increasing the version number.");
            }
        } catch (Throwable th) {
            supportSQLiteDatabase.close();
        }
    }

    private void updateIdentity(SupportSQLiteDatabase supportSQLiteDatabase) {
        createMasterTableIfNotExists(supportSQLiteDatabase);
        supportSQLiteDatabase.execSQL(RoomMasterTable.createInsertQuery(this.mIdentityHash));
    }

    private void createMasterTableIfNotExists(SupportSQLiteDatabase supportSQLiteDatabase) {
        supportSQLiteDatabase.execSQL(RoomMasterTable.CREATE_QUERY);
    }
}
