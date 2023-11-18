package android.arch.persistence.room;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.core.internal.SafeIterableMap;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.ArraySet;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

public class InvalidationTracker {
    @VisibleForTesting
    static final String CLEANUP_SQL = "DELETE FROM room_table_modification_log WHERE version NOT IN( SELECT MAX(version) FROM room_table_modification_log GROUP BY table_id)";
    private static final String CREATE_VERSION_TABLE_SQL = "CREATE TEMP TABLE room_table_modification_log(version INTEGER PRIMARY KEY AUTOINCREMENT, table_id INTEGER)";
    @VisibleForTesting
    static final String SELECT_UPDATED_TABLES_SQL = "SELECT * FROM room_table_modification_log WHERE version  > ? ORDER BY version ASC;";
    private static final String TABLE_ID_COLUMN_NAME = "table_id";
    private static final String[] TRIGGERS = new String[]{"UPDATE", "DELETE", "INSERT"};
    private static final String UPDATE_TABLE_NAME = "room_table_modification_log";
    private static final String VERSION_COLUMN_NAME = "version";
    private volatile SupportSQLiteStatement mCleanupStatement;
    private final RoomDatabase mDatabase;
    private volatile boolean mInitialized;
    private long mMaxVersion = 0;
    private ObservedTableTracker mObservedTableTracker;
    @VisibleForTesting
    final SafeIterableMap<Observer, ObserverWrapper> mObserverMap;
    AtomicBoolean mPendingRefresh;
    private Object[] mQueryArgs = new Object[1];
    @VisibleForTesting
    Runnable mRefreshRunnable;
    private Runnable mSyncTriggers;
    @VisibleForTesting
    @NonNull
    ArrayMap<String, Integer> mTableIdLookup;
    private String[] mTableNames;
    @VisibleForTesting
    @NonNull
    long[] mTableVersions;

    /* renamed from: android.arch.persistence.room.InvalidationTracker$1 */
    class C00151 implements Runnable {
        C00151() {
        }

        public void run() {
            if (!InvalidationTracker.this.mDatabase.inTransaction() && InvalidationTracker.this.ensureInitialization()) {
                while (true) {
                    int[] tablesToSync = InvalidationTracker.this.mObservedTableTracker.getTablesToSync();
                    if (tablesToSync != null) {
                        int length = tablesToSync.length;
                        SupportSQLiteDatabase writableDatabase = InvalidationTracker.this.mDatabase.getOpenHelper().getWritableDatabase();
                        try {
                            writableDatabase.beginTransaction();
                            for (int i = 0; i < length; i++) {
                                switch (tablesToSync[i]) {
                                    case 1:
                                        InvalidationTracker.this.startTrackingTable(writableDatabase, i);
                                        break;
                                    case 2:
                                        InvalidationTracker.this.stopTrackingTable(writableDatabase, i);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            writableDatabase.setTransactionSuccessful();
                            writableDatabase.endTransaction();
                            InvalidationTracker.this.mObservedTableTracker.onSyncCompleted();
                        } catch (Throwable e) {
                            Log.e("ROOM", "Cannot run invalidation tracker. Is the db closed?", e);
                            return;
                        } catch (Throwable th) {
                            writableDatabase.endTransaction();
                        }
                    }
                    return;
                }
            }
        }
    }

    /* renamed from: android.arch.persistence.room.InvalidationTracker$2 */
    class C00162 implements Runnable {
        C00162() {
        }

        public void run() {
            Throwable th;
            Throwable th2;
            Lock closeLock = InvalidationTracker.this.mDatabase.getCloseLock();
            Object obj;
            Iterator it;
            try {
                Cursor query;
                closeLock.lock();
                if (!InvalidationTracker.this.ensureInitialization()) {
                    closeLock.unlock();
                    return;
                } else if (!InvalidationTracker.this.mPendingRefresh.compareAndSet(true, false)) {
                    closeLock.unlock();
                    return;
                } else if (InvalidationTracker.this.mDatabase.inTransaction()) {
                    closeLock.unlock();
                    return;
                } else {
                    InvalidationTracker.this.mCleanupStatement.executeUpdateDelete();
                    InvalidationTracker.this.mQueryArgs[0] = Long.valueOf(InvalidationTracker.this.mMaxVersion);
                    query = InvalidationTracker.this.mDatabase.query(InvalidationTracker.SELECT_UPDATED_TABLES_SQL, InvalidationTracker.this.mQueryArgs);
                    obj = null;
                    while (query.moveToNext()) {
                        try {
                            long j = query.getLong(0);
                            InvalidationTracker.this.mTableVersions[query.getInt(1)] = j;
                            try {
                                InvalidationTracker.this.mMaxVersion = j;
                                obj = 1;
                            } catch (Throwable th3) {
                                th = th3;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                        }
                    }
                    try {
                        query.close();
                    } catch (Throwable th5) {
                        th2 = th5;
                    }
                    closeLock.unlock();
                    if (obj != null) {
                        synchronized (InvalidationTracker.this.mObserverMap) {
                            it = InvalidationTracker.this.mObserverMap.iterator();
                            while (it.hasNext()) {
                                ((ObserverWrapper) ((Entry) it.next()).getValue()).checkForInvalidation(InvalidationTracker.this.mTableVersions);
                            }
                        }
                    }
                }
                query.close();
                throw th5;
            } catch (IllegalStateException e) {
                th2 = e;
                obj = null;
                try {
                    Log.e("ROOM", "Cannot run invalidation tracker. Is the db closed?", th2);
                    closeLock.unlock();
                    if (obj != null) {
                        synchronized (InvalidationTracker.this.mObserverMap) {
                            it = InvalidationTracker.this.mObserverMap.iterator();
                            while (it.hasNext()) {
                                ((ObserverWrapper) ((Entry) it.next()).getValue()).checkForInvalidation(InvalidationTracker.this.mTableVersions);
                            }
                        }
                    }
                } catch (Throwable th6) {
                    closeLock.unlock();
                }
            }
        }
    }

    static class ObservedTableTracker {
        static final int ADD = 1;
        static final int NO_OP = 0;
        static final int REMOVE = 2;
        boolean mNeedsSync;
        boolean mPendingSync;
        final long[] mTableObservers;
        final int[] mTriggerStateChanges;
        final boolean[] mTriggerStates;

        ObservedTableTracker(int i) {
            this.mTableObservers = new long[i];
            this.mTriggerStates = new boolean[i];
            this.mTriggerStateChanges = new int[i];
            Arrays.fill(this.mTableObservers, 0);
            Arrays.fill(this.mTriggerStates, false);
        }

        boolean onAdded(int... iArr) {
            boolean z;
            synchronized (this) {
                z = false;
                for (int i : iArr) {
                    long j = this.mTableObservers[i];
                    this.mTableObservers[i] = j + 1;
                    if (j == 0) {
                        this.mNeedsSync = true;
                        z = true;
                    }
                }
            }
            return z;
        }

        boolean onRemoved(int... iArr) {
            boolean z;
            synchronized (this) {
                z = false;
                for (int i : iArr) {
                    long j = this.mTableObservers[i];
                    this.mTableObservers[i] = j - 1;
                    if (j == 1) {
                        this.mNeedsSync = true;
                        z = true;
                    }
                }
            }
            return z;
        }

        @Nullable
        int[] getTablesToSync() {
            synchronized (this) {
                if (this.mNeedsSync) {
                    if (!this.mPendingSync) {
                        int length = this.mTableObservers.length;
                        int i = 0;
                        while (true) {
                            boolean z = true;
                            if (i < length) {
                                boolean z2 = this.mTableObservers[i] > 0;
                                if (z2 != this.mTriggerStates[i]) {
                                    int[] iArr = this.mTriggerStateChanges;
                                    if (!z2) {
                                        z = true;
                                    }
                                    iArr[i] = z;
                                } else {
                                    this.mTriggerStateChanges[i] = 0;
                                }
                                this.mTriggerStates[i] = z2;
                                i++;
                            } else {
                                this.mPendingSync = true;
                                this.mNeedsSync = false;
                                int[] iArr2 = this.mTriggerStateChanges;
                                return iArr2;
                            }
                        }
                    }
                }
                return null;
            }
        }

        void onSyncCompleted() {
            synchronized (this) {
                this.mPendingSync = false;
            }
        }
    }

    public static abstract class Observer {
        final String[] mTables;

        public abstract void onInvalidated(@NonNull Set<String> set);

        protected Observer(@NonNull String str, String... strArr) {
            this.mTables = (String[]) Arrays.copyOf(strArr, strArr.length + 1);
            this.mTables[strArr.length] = str;
        }

        public Observer(@NonNull String[] strArr) {
            this.mTables = (String[]) Arrays.copyOf(strArr, strArr.length);
        }
    }

    static class ObserverWrapper {
        final Observer mObserver;
        private final Set<String> mSingleTableSet;
        final int[] mTableIds;
        private final String[] mTableNames;
        private final long[] mVersions;

        ObserverWrapper(Observer observer, int[] iArr, String[] strArr, long[] jArr) {
            this.mObserver = observer;
            this.mTableIds = iArr;
            this.mTableNames = strArr;
            this.mVersions = jArr;
            if (iArr.length == 1) {
                observer = new ArraySet();
                observer.add(this.mTableNames[null]);
                this.mSingleTableSet = Collections.unmodifiableSet(observer);
                return;
            }
            this.mSingleTableSet = null;
        }

        void checkForInvalidation(long[] jArr) {
            int length = this.mTableIds.length;
            Set set = null;
            for (int i = 0; i < length; i++) {
                long j = jArr[this.mTableIds[i]];
                if (this.mVersions[i] < j) {
                    this.mVersions[i] = j;
                    if (length == 1) {
                        set = this.mSingleTableSet;
                    } else {
                        if (set == null) {
                            set = new ArraySet(length);
                        }
                        set.add(this.mTableNames[i]);
                    }
                }
            }
            if (set != null) {
                this.mObserver.onInvalidated(set);
            }
        }
    }

    static class WeakObserver extends Observer {
        final WeakReference<Observer> mDelegateRef;
        final InvalidationTracker mTracker;

        WeakObserver(InvalidationTracker invalidationTracker, Observer observer) {
            super(observer.mTables);
            this.mTracker = invalidationTracker;
            this.mDelegateRef = new WeakReference(observer);
        }

        public void onInvalidated(@NonNull Set<String> set) {
            Observer observer = (Observer) this.mDelegateRef.get();
            if (observer == null) {
                this.mTracker.removeObserver(this);
            } else {
                observer.onInvalidated(set);
            }
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public InvalidationTracker(RoomDatabase roomDatabase, String... strArr) {
        int i = 0;
        this.mPendingRefresh = new AtomicBoolean(false);
        this.mInitialized = false;
        this.mObserverMap = new SafeIterableMap();
        this.mSyncTriggers = new C00151();
        this.mRefreshRunnable = new C00162();
        this.mDatabase = roomDatabase;
        this.mObservedTableTracker = new ObservedTableTracker(strArr.length);
        this.mTableIdLookup = new ArrayMap();
        roomDatabase = strArr.length;
        this.mTableNames = new String[roomDatabase];
        while (i < roomDatabase) {
            String toLowerCase = strArr[i].toLowerCase(Locale.US);
            this.mTableIdLookup.put(toLowerCase, Integer.valueOf(i));
            this.mTableNames[i] = toLowerCase;
            i++;
        }
        this.mTableVersions = new long[strArr.length];
        Arrays.fill(this.mTableVersions, 0);
    }

    void internalInit(SupportSQLiteDatabase supportSQLiteDatabase) {
        synchronized (this) {
            if (this.mInitialized) {
                Log.e("ROOM", "Invalidation tracker is initialized twice :/.");
                return;
            }
            supportSQLiteDatabase.beginTransaction();
            try {
                supportSQLiteDatabase.execSQL("PRAGMA temp_store = MEMORY;");
                supportSQLiteDatabase.execSQL("PRAGMA recursive_triggers='ON';");
                supportSQLiteDatabase.execSQL(CREATE_VERSION_TABLE_SQL);
                supportSQLiteDatabase.setTransactionSuccessful();
                this.mCleanupStatement = supportSQLiteDatabase.compileStatement(CLEANUP_SQL);
                this.mInitialized = true;
            } finally {
                supportSQLiteDatabase.endTransaction();
            }
        }
    }

    private static void appendTriggerName(StringBuilder stringBuilder, String str, String str2) {
        stringBuilder.append("`");
        stringBuilder.append("room_table_modification_trigger_");
        stringBuilder.append(str);
        stringBuilder.append("_");
        stringBuilder.append(str2);
        stringBuilder.append("`");
    }

    private void stopTrackingTable(SupportSQLiteDatabase supportSQLiteDatabase, int i) {
        i = this.mTableNames[i];
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : TRIGGERS) {
            stringBuilder.setLength(0);
            stringBuilder.append("DROP TRIGGER IF EXISTS ");
            appendTriggerName(stringBuilder, i, str);
            supportSQLiteDatabase.execSQL(stringBuilder.toString());
        }
    }

    private void startTrackingTable(SupportSQLiteDatabase supportSQLiteDatabase, int i) {
        String str = this.mTableNames[i];
        StringBuilder stringBuilder = new StringBuilder();
        for (String str2 : TRIGGERS) {
            stringBuilder.setLength(0);
            stringBuilder.append("CREATE TEMP TRIGGER IF NOT EXISTS ");
            appendTriggerName(stringBuilder, str, str2);
            stringBuilder.append(" AFTER ");
            stringBuilder.append(str2);
            stringBuilder.append(" ON `");
            stringBuilder.append(str);
            stringBuilder.append("` BEGIN INSERT OR REPLACE INTO ");
            stringBuilder.append(UPDATE_TABLE_NAME);
            stringBuilder.append(" VALUES(null, ");
            stringBuilder.append(i);
            stringBuilder.append("); END");
            supportSQLiteDatabase.execSQL(stringBuilder.toString());
        }
    }

    public void addObserver(@NonNull Observer observer) {
        String[] strArr = observer.mTables;
        int[] iArr = new int[strArr.length];
        int length = strArr.length;
        long[] jArr = new long[strArr.length];
        for (int i = 0; i < length; i++) {
            Integer num = (Integer) this.mTableIdLookup.get(strArr[i].toLowerCase(Locale.US));
            if (num == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("There is no table with name ");
                stringBuilder.append(strArr[i]);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            iArr[i] = num.intValue();
            jArr[i] = this.mMaxVersion;
        }
        ObserverWrapper observerWrapper = new ObserverWrapper(observer, iArr, strArr, jArr);
        synchronized (this.mObserverMap) {
            ObserverWrapper observerWrapper2 = (ObserverWrapper) this.mObserverMap.putIfAbsent(observer, observerWrapper);
        }
        if (observerWrapper2 == null && this.mObservedTableTracker.onAdded(iArr) != null) {
            ArchTaskExecutor.getInstance().executeOnDiskIO(this.mSyncTriggers);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void addWeakObserver(Observer observer) {
        addObserver(new WeakObserver(this, observer));
    }

    public void removeObserver(@NonNull Observer observer) {
        synchronized (this.mObserverMap) {
            ObserverWrapper observerWrapper = (ObserverWrapper) this.mObserverMap.remove(observer);
        }
        if (observerWrapper != null && this.mObservedTableTracker.onRemoved(observerWrapper.mTableIds) != null) {
            ArchTaskExecutor.getInstance().executeOnDiskIO(this.mSyncTriggers);
        }
    }

    private boolean ensureInitialization() {
        if (!this.mDatabase.isOpen()) {
            return false;
        }
        if (!this.mInitialized) {
            this.mDatabase.getOpenHelper().getWritableDatabase();
        }
        if (this.mInitialized) {
            return true;
        }
        Log.e("ROOM", "database is not initialized even though it is open");
        return false;
    }

    public void refreshVersionsAsync() {
        if (this.mPendingRefresh.compareAndSet(false, true)) {
            ArchTaskExecutor.getInstance().executeOnDiskIO(this.mRefreshRunnable);
        }
    }

    @WorkerThread
    @RestrictTo({Scope.LIBRARY_GROUP})
    public void refreshVersionsSync() {
        syncTriggers();
        this.mRefreshRunnable.run();
    }

    void syncTriggers() {
        this.mSyncTriggers.run();
    }
}
