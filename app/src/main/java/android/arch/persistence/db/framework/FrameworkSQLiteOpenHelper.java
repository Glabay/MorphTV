package android.arch.persistence.db.framework;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.RequiresApi;

class FrameworkSQLiteOpenHelper implements SupportSQLiteOpenHelper {
    private final OpenHelper mDelegate;

    static class OpenHelper extends SQLiteOpenHelper {
        final Callback mCallback;
        final FrameworkSQLiteDatabase[] mDbRef;

        /* renamed from: android.arch.persistence.db.framework.FrameworkSQLiteOpenHelper$OpenHelper$1 */
        class C00131 implements DatabaseErrorHandler {
            final /* synthetic */ Callback val$callback;
            final /* synthetic */ FrameworkSQLiteDatabase[] val$dbRef;

            C00131(FrameworkSQLiteDatabase[] frameworkSQLiteDatabaseArr, Callback callback) {
                this.val$dbRef = frameworkSQLiteDatabaseArr;
                this.val$callback = callback;
            }

            public void onCorruption(SQLiteDatabase sQLiteDatabase) {
                sQLiteDatabase = this.val$dbRef[0];
                if (sQLiteDatabase != null) {
                    this.val$callback.onCorruption(sQLiteDatabase);
                }
            }
        }

        OpenHelper(Context context, String str, FrameworkSQLiteDatabase[] frameworkSQLiteDatabaseArr, Callback callback) {
            super(context, str, null, callback.version, new C00131(frameworkSQLiteDatabaseArr, callback));
            this.mCallback = callback;
            this.mDbRef = frameworkSQLiteDatabaseArr;
        }

        SupportSQLiteDatabase getWritableSupportDatabase() {
            return getWrappedDb(super.getWritableDatabase());
        }

        SupportSQLiteDatabase getReadableSupportDatabase() {
            return getWrappedDb(super.getReadableDatabase());
        }

        FrameworkSQLiteDatabase getWrappedDb(SQLiteDatabase sQLiteDatabase) {
            if (this.mDbRef[0] == null) {
                this.mDbRef[0] = new FrameworkSQLiteDatabase(sQLiteDatabase);
            }
            return this.mDbRef[0];
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            this.mCallback.onCreate(getWrappedDb(sQLiteDatabase));
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            this.mCallback.onUpgrade(getWrappedDb(sQLiteDatabase), i, i2);
        }

        public void onConfigure(SQLiteDatabase sQLiteDatabase) {
            this.mCallback.onConfigure(getWrappedDb(sQLiteDatabase));
        }

        public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            this.mCallback.onDowngrade(getWrappedDb(sQLiteDatabase), i, i2);
        }

        public void onOpen(SQLiteDatabase sQLiteDatabase) {
            this.mCallback.onOpen(getWrappedDb(sQLiteDatabase));
        }

        public synchronized void close() {
            super.close();
            this.mDbRef[0] = null;
        }
    }

    FrameworkSQLiteOpenHelper(Context context, String str, Callback callback) {
        this.mDelegate = createDelegate(context, str, callback);
    }

    private OpenHelper createDelegate(Context context, String str, Callback callback) {
        return new OpenHelper(context, str, new FrameworkSQLiteDatabase[1], callback);
    }

    public String getDatabaseName() {
        return this.mDelegate.getDatabaseName();
    }

    @RequiresApi(api = 16)
    public void setWriteAheadLoggingEnabled(boolean z) {
        this.mDelegate.setWriteAheadLoggingEnabled(z);
    }

    public SupportSQLiteDatabase getWritableDatabase() {
        return this.mDelegate.getWritableSupportDatabase();
    }

    public SupportSQLiteDatabase getReadableDatabase() {
        return this.mDelegate.getReadableSupportDatabase();
    }

    public void close() {
        this.mDelegate.close();
    }
}
