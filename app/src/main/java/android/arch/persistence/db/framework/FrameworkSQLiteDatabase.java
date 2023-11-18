package android.arch.persistence.db.framework;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteTransactionListener;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.util.Pair;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

class FrameworkSQLiteDatabase implements SupportSQLiteDatabase {
    private static final String[] CONFLICT_VALUES = new String[]{"", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private final SQLiteDatabase mDelegate;

    FrameworkSQLiteDatabase(SQLiteDatabase sQLiteDatabase) {
        this.mDelegate = sQLiteDatabase;
    }

    public SupportSQLiteStatement compileStatement(String str) {
        return new FrameworkSQLiteStatement(this.mDelegate.compileStatement(str));
    }

    public void beginTransaction() {
        this.mDelegate.beginTransaction();
    }

    public void beginTransactionNonExclusive() {
        this.mDelegate.beginTransactionNonExclusive();
    }

    public void beginTransactionWithListener(SQLiteTransactionListener sQLiteTransactionListener) {
        this.mDelegate.beginTransactionWithListener(sQLiteTransactionListener);
    }

    public void beginTransactionWithListenerNonExclusive(SQLiteTransactionListener sQLiteTransactionListener) {
        this.mDelegate.beginTransactionWithListenerNonExclusive(sQLiteTransactionListener);
    }

    public void endTransaction() {
        this.mDelegate.endTransaction();
    }

    public void setTransactionSuccessful() {
        this.mDelegate.setTransactionSuccessful();
    }

    public boolean inTransaction() {
        return this.mDelegate.inTransaction();
    }

    public boolean isDbLockedByCurrentThread() {
        return this.mDelegate.isDbLockedByCurrentThread();
    }

    public boolean yieldIfContendedSafely() {
        return this.mDelegate.yieldIfContendedSafely();
    }

    public boolean yieldIfContendedSafely(long j) {
        return this.mDelegate.yieldIfContendedSafely(j);
    }

    public int getVersion() {
        return this.mDelegate.getVersion();
    }

    public void setVersion(int i) {
        this.mDelegate.setVersion(i);
    }

    public long getMaximumSize() {
        return this.mDelegate.getMaximumSize();
    }

    public long setMaximumSize(long j) {
        return this.mDelegate.setMaximumSize(j);
    }

    public long getPageSize() {
        return this.mDelegate.getPageSize();
    }

    public void setPageSize(long j) {
        this.mDelegate.setPageSize(j);
    }

    public Cursor query(String str) {
        return query(new SimpleSQLiteQuery(str));
    }

    public Cursor query(String str, Object[] objArr) {
        return query(new SimpleSQLiteQuery(str, objArr));
    }

    public Cursor query(final SupportSQLiteQuery supportSQLiteQuery) {
        return this.mDelegate.rawQueryWithFactory(new CursorFactory() {
            public Cursor newCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery) {
                supportSQLiteQuery.bindTo(new FrameworkSQLiteProgram(sQLiteQuery));
                return new SQLiteCursor(sQLiteCursorDriver, str, sQLiteQuery);
            }
        }, supportSQLiteQuery.getSql(), EMPTY_STRING_ARRAY, null);
    }

    @RequiresApi(api = 16)
    public Cursor query(final SupportSQLiteQuery supportSQLiteQuery, CancellationSignal cancellationSignal) {
        return this.mDelegate.rawQueryWithFactory(new CursorFactory() {
            public Cursor newCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery) {
                supportSQLiteQuery.bindTo(new FrameworkSQLiteProgram(sQLiteQuery));
                return new SQLiteCursor(sQLiteCursorDriver, str, sQLiteQuery);
            }
        }, supportSQLiteQuery.getSql(), EMPTY_STRING_ARRAY, null, cancellationSignal);
    }

    public long insert(String str, int i, ContentValues contentValues) throws SQLException {
        return this.mDelegate.insertWithOnConflict(str, null, contentValues, i);
    }

    public int delete(String str, String str2, Object[] objArr) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DELETE FROM ");
        stringBuilder.append(str);
        if (isEmpty(str2) != null) {
            str = "";
        } else {
            str = new StringBuilder();
            str.append(" WHERE ");
            str.append(str2);
            str = str.toString();
        }
        stringBuilder.append(str);
        str = compileStatement(stringBuilder.toString());
        SimpleSQLiteQuery.bind(str, objArr);
        return str.executeUpdateDelete();
    }

    public int update(String str, int i, ContentValues contentValues, String str2, Object[] objArr) {
        if (contentValues != null) {
            if (contentValues.size() != 0) {
                StringBuilder stringBuilder = new StringBuilder(120);
                stringBuilder.append("UPDATE ");
                stringBuilder.append(CONFLICT_VALUES[i]);
                stringBuilder.append(str);
                stringBuilder.append(" SET ");
                str = contentValues.size();
                if (objArr == null) {
                    i = str;
                } else {
                    i = objArr.length + str;
                }
                Object[] objArr2 = new Object[i];
                int i2 = 0;
                for (String str3 : contentValues.keySet()) {
                    stringBuilder.append(i2 > 0 ? "," : "");
                    stringBuilder.append(str3);
                    int i3 = i2 + 1;
                    objArr2[i2] = contentValues.get(str3);
                    stringBuilder.append("=?");
                    i2 = i3;
                }
                if (objArr != null) {
                    for (contentValues = str; contentValues < i; contentValues++) {
                        objArr2[contentValues] = objArr[contentValues - str];
                    }
                }
                if (isEmpty(str2) == null) {
                    stringBuilder.append(" WHERE ");
                    stringBuilder.append(str2);
                }
                str = compileStatement(stringBuilder.toString());
                SimpleSQLiteQuery.bind(str, objArr2);
                return str.executeUpdateDelete();
            }
        }
        throw new IllegalArgumentException("Empty values");
    }

    public void execSQL(String str) throws SQLException {
        this.mDelegate.execSQL(str);
    }

    public void execSQL(String str, Object[] objArr) throws SQLException {
        this.mDelegate.execSQL(str, objArr);
    }

    public boolean isReadOnly() {
        return this.mDelegate.isReadOnly();
    }

    public boolean isOpen() {
        return this.mDelegate.isOpen();
    }

    public boolean needUpgrade(int i) {
        return this.mDelegate.needUpgrade(i);
    }

    public String getPath() {
        return this.mDelegate.getPath();
    }

    public void setLocale(Locale locale) {
        this.mDelegate.setLocale(locale);
    }

    public void setMaxSqlCacheSize(int i) {
        this.mDelegate.setMaxSqlCacheSize(i);
    }

    @RequiresApi(api = 16)
    public void setForeignKeyConstraintsEnabled(boolean z) {
        this.mDelegate.setForeignKeyConstraintsEnabled(z);
    }

    public boolean enableWriteAheadLogging() {
        return this.mDelegate.enableWriteAheadLogging();
    }

    @RequiresApi(api = 16)
    public void disableWriteAheadLogging() {
        this.mDelegate.disableWriteAheadLogging();
    }

    @RequiresApi(api = 16)
    public boolean isWriteAheadLoggingEnabled() {
        return this.mDelegate.isWriteAheadLoggingEnabled();
    }

    public List<Pair<String, String>> getAttachedDbs() {
        return this.mDelegate.getAttachedDbs();
    }

    public boolean isDatabaseIntegrityOk() {
        return this.mDelegate.isDatabaseIntegrityOk();
    }

    public void close() throws IOException {
        this.mDelegate.close();
    }

    private static boolean isEmpty(String str) {
        if (str != null) {
            if (str.length() != null) {
                return null;
            }
        }
        return true;
    }
}
