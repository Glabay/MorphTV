package android.arch.persistence.room;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import java.util.concurrent.atomic.AtomicBoolean;

@RestrictTo({Scope.LIBRARY_GROUP})
public abstract class SharedSQLiteStatement {
    private final RoomDatabase mDatabase;
    private final AtomicBoolean mLock = new AtomicBoolean(false);
    private volatile SupportSQLiteStatement mStmt;

    protected abstract String createQuery();

    public SharedSQLiteStatement(RoomDatabase roomDatabase) {
        this.mDatabase = roomDatabase;
    }

    protected void assertNotMainThread() {
        this.mDatabase.assertNotMainThread();
    }

    private SupportSQLiteStatement createNewStatement() {
        return this.mDatabase.compileStatement(createQuery());
    }

    private SupportSQLiteStatement getStmt(boolean z) {
        if (!z) {
            return createNewStatement();
        }
        if (!this.mStmt) {
            this.mStmt = createNewStatement();
        }
        return this.mStmt;
    }

    public SupportSQLiteStatement acquire() {
        assertNotMainThread();
        return getStmt(this.mLock.compareAndSet(false, true));
    }

    public void release(SupportSQLiteStatement supportSQLiteStatement) {
        if (supportSQLiteStatement == this.mStmt) {
            this.mLock.set(false);
        }
    }
}
