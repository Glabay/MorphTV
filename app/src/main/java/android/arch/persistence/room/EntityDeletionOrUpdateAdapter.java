package android.arch.persistence.room;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public abstract class EntityDeletionOrUpdateAdapter<T> extends SharedSQLiteStatement {
    protected abstract void bind(SupportSQLiteStatement supportSQLiteStatement, T t);

    protected abstract String createQuery();

    public EntityDeletionOrUpdateAdapter(RoomDatabase roomDatabase) {
        super(roomDatabase);
    }

    public final int handle(T t) {
        SupportSQLiteStatement acquire = acquire();
        try {
            bind(acquire, t);
            t = acquire.executeUpdateDelete();
            return t;
        } finally {
            release(acquire);
        }
    }

    public final int handleMultiple(Iterable<T> iterable) {
        SupportSQLiteStatement acquire = acquire();
        int i = 0;
        try {
            for (T bind : iterable) {
                bind(acquire, bind);
                i += acquire.executeUpdateDelete();
            }
            return i;
        } finally {
            release(acquire);
        }
    }

    public final int handleMultiple(T[] tArr) {
        SupportSQLiteStatement acquire = acquire();
        try {
            int i = 0;
            for (Object bind : tArr) {
                bind(acquire, bind);
                i += acquire.executeUpdateDelete();
            }
            return i;
        } finally {
            release(acquire);
        }
    }
}
