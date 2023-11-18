package android.arch.persistence.room.paging;

import android.arch.paging.TiledDataSource;
import android.arch.persistence.room.InvalidationTracker.Observer;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import java.util.List;
import java.util.Set;

@RestrictTo({Scope.LIBRARY_GROUP})
public abstract class LimitOffsetDataSource<T> extends TiledDataSource<T> {
    private final String mCountQuery;
    private final RoomDatabase mDb;
    private final boolean mInTransaction;
    private final String mLimitOffsetQuery;
    private final Observer mObserver;
    private final RoomSQLiteQuery mSourceQuery;

    protected abstract List<T> convertRows(Cursor cursor);

    protected LimitOffsetDataSource(RoomDatabase roomDatabase, RoomSQLiteQuery roomSQLiteQuery, boolean z, String... strArr) {
        this.mDb = roomDatabase;
        this.mSourceQuery = roomSQLiteQuery;
        this.mInTransaction = z;
        roomSQLiteQuery = new StringBuilder();
        roomSQLiteQuery.append("SELECT COUNT(*) FROM ( ");
        roomSQLiteQuery.append(this.mSourceQuery.getSql());
        roomSQLiteQuery.append(" )");
        this.mCountQuery = roomSQLiteQuery.toString();
        roomSQLiteQuery = new StringBuilder();
        roomSQLiteQuery.append("SELECT * FROM ( ");
        roomSQLiteQuery.append(this.mSourceQuery.getSql());
        roomSQLiteQuery.append(" ) LIMIT ? OFFSET ?");
        this.mLimitOffsetQuery = roomSQLiteQuery.toString();
        this.mObserver = new Observer(strArr) {
            public void onInvalidated(@NonNull Set<String> set) {
                LimitOffsetDataSource.this.invalidate();
            }
        };
        roomDatabase.getInvalidationTracker().addWeakObserver(this.mObserver);
    }

    public int countItems() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire(this.mCountQuery, this.mSourceQuery.getArgCount());
        acquire.copyArgumentsFrom(this.mSourceQuery);
        Cursor query = this.mDb.query(acquire);
        try {
            if (query.moveToFirst()) {
                int i = query.getInt(0);
                return i;
            }
            query.close();
            acquire.release();
            return 0;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public boolean isInvalid() {
        this.mDb.getInvalidationTracker().refreshVersionsSync();
        return super.isInvalid();
    }

    @Nullable
    public List<T> loadRange(int i, int i2) {
        Object acquire = RoomSQLiteQuery.acquire(this.mLimitOffsetQuery, this.mSourceQuery.getArgCount() + 2);
        acquire.copyArgumentsFrom(this.mSourceQuery);
        acquire.bindLong(acquire.getArgCount() - 1, (long) i2);
        acquire.bindLong(acquire.getArgCount(), (long) i);
        if (this.mInTransaction != 0) {
            this.mDb.beginTransaction();
            try {
                i2 = this.mDb.query(acquire);
                try {
                    i = convertRows(i2);
                    this.mDb.setTransactionSuccessful();
                    if (i2 != 0) {
                        i2.close();
                    }
                    this.mDb.endTransaction();
                    acquire.release();
                    return i;
                } catch (Throwable th) {
                    i = th;
                    if (i2 != 0) {
                        i2.close();
                    }
                    this.mDb.endTransaction();
                    acquire.release();
                    throw i;
                }
            } catch (int i22) {
                Object obj = i22;
                i22 = 0;
                i = obj;
                if (i22 != 0) {
                    i22.close();
                }
                this.mDb.endTransaction();
                acquire.release();
                throw i;
            }
        }
        i = this.mDb.query(acquire);
        try {
            i22 = convertRows(i);
            return i22;
        } finally {
            i.close();
            acquire.release();
        }
    }
}
