package android.arch.persistence.room;

import android.arch.persistence.db.SupportSQLiteProgram;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.VisibleForTesting;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

@RestrictTo({Scope.LIBRARY_GROUP})
public class RoomSQLiteQuery implements SupportSQLiteQuery, SupportSQLiteProgram {
    private static final int BLOB = 5;
    @VisibleForTesting
    static final int DESIRED_POOL_SIZE = 10;
    private static final int DOUBLE = 3;
    private static final int LONG = 2;
    private static final int NULL = 1;
    @VisibleForTesting
    static final int POOL_LIMIT = 15;
    private static final int STRING = 4;
    @VisibleForTesting
    static final TreeMap<Integer, RoomSQLiteQuery> sQueryPool = new TreeMap();
    @VisibleForTesting
    int mArgCount;
    private final int[] mBindingTypes;
    @VisibleForTesting
    final byte[][] mBlobBindings;
    @VisibleForTesting
    final int mCapacity;
    @VisibleForTesting
    final double[] mDoubleBindings;
    @VisibleForTesting
    final long[] mLongBindings;
    private volatile String mQuery;
    @VisibleForTesting
    final String[] mStringBindings;

    public void close() throws Exception {
    }

    public static RoomSQLiteQuery acquire(String str, int i) {
        synchronized (sQueryPool) {
            Entry ceilingEntry = sQueryPool.ceilingEntry(Integer.valueOf(i));
            if (ceilingEntry != null) {
                sQueryPool.remove(ceilingEntry.getKey());
                RoomSQLiteQuery roomSQLiteQuery = (RoomSQLiteQuery) ceilingEntry.getValue();
                roomSQLiteQuery.init(str, i);
                return roomSQLiteQuery;
            }
            RoomSQLiteQuery roomSQLiteQuery2 = new RoomSQLiteQuery(i);
            roomSQLiteQuery2.init(str, i);
            return roomSQLiteQuery2;
        }
    }

    private RoomSQLiteQuery(int i) {
        this.mCapacity = i;
        i++;
        this.mBindingTypes = new int[i];
        this.mLongBindings = new long[i];
        this.mDoubleBindings = new double[i];
        this.mStringBindings = new String[i];
        this.mBlobBindings = new byte[i][];
    }

    void init(String str, int i) {
        this.mQuery = str;
        this.mArgCount = i;
    }

    public void release() {
        synchronized (sQueryPool) {
            sQueryPool.put(Integer.valueOf(this.mCapacity), this);
            prunePoolLocked();
        }
    }

    private static void prunePoolLocked() {
        if (sQueryPool.size() > 15) {
            int size = sQueryPool.size() - 10;
            Iterator it = sQueryPool.descendingKeySet().iterator();
            while (true) {
                int i = size - 1;
                if (size > 0) {
                    it.next();
                    it.remove();
                    size = i;
                } else {
                    return;
                }
            }
        }
    }

    public String getSql() {
        return this.mQuery;
    }

    public int getArgCount() {
        return this.mArgCount;
    }

    public void bindTo(SupportSQLiteProgram supportSQLiteProgram) {
        for (int i = 1; i <= this.mArgCount; i++) {
            switch (this.mBindingTypes[i]) {
                case 1:
                    supportSQLiteProgram.bindNull(i);
                    break;
                case 2:
                    supportSQLiteProgram.bindLong(i, this.mLongBindings[i]);
                    break;
                case 3:
                    supportSQLiteProgram.bindDouble(i, this.mDoubleBindings[i]);
                    break;
                case 4:
                    supportSQLiteProgram.bindString(i, this.mStringBindings[i]);
                    break;
                case 5:
                    supportSQLiteProgram.bindBlob(i, this.mBlobBindings[i]);
                    break;
                default:
                    break;
            }
        }
    }

    public void bindNull(int i) {
        this.mBindingTypes[i] = 1;
    }

    public void bindLong(int i, long j) {
        this.mBindingTypes[i] = 2;
        this.mLongBindings[i] = j;
    }

    public void bindDouble(int i, double d) {
        this.mBindingTypes[i] = 3;
        this.mDoubleBindings[i] = d;
    }

    public void bindString(int i, String str) {
        this.mBindingTypes[i] = 4;
        this.mStringBindings[i] = str;
    }

    public void bindBlob(int i, byte[] bArr) {
        this.mBindingTypes[i] = 5;
        this.mBlobBindings[i] = bArr;
    }

    public void copyArgumentsFrom(RoomSQLiteQuery roomSQLiteQuery) {
        int argCount = roomSQLiteQuery.getArgCount() + 1;
        System.arraycopy(roomSQLiteQuery.mBindingTypes, 0, this.mBindingTypes, 0, argCount);
        System.arraycopy(roomSQLiteQuery.mLongBindings, 0, this.mLongBindings, 0, argCount);
        System.arraycopy(roomSQLiteQuery.mStringBindings, 0, this.mStringBindings, 0, argCount);
        System.arraycopy(roomSQLiteQuery.mBlobBindings, 0, this.mBlobBindings, 0, argCount);
        System.arraycopy(roomSQLiteQuery.mDoubleBindings, 0, this.mDoubleBindings, 0, argCount);
    }

    public void clearBindings() {
        Arrays.fill(this.mBindingTypes, 1);
        Arrays.fill(this.mStringBindings, null);
        Arrays.fill(this.mBlobBindings, null);
        this.mQuery = null;
    }
}
