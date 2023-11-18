package android.arch.persistence.db;

public final class SimpleSQLiteQuery implements SupportSQLiteQuery {
    private final Object[] mBindArgs;
    private final String mQuery;

    public SimpleSQLiteQuery(String str, Object[] objArr) {
        this.mQuery = str;
        this.mBindArgs = objArr;
    }

    public SimpleSQLiteQuery(String str) {
        this(str, null);
    }

    public String getSql() {
        return this.mQuery;
    }

    public void bindTo(SupportSQLiteProgram supportSQLiteProgram) {
        bind(supportSQLiteProgram, this.mBindArgs);
    }

    public static void bind(SupportSQLiteProgram supportSQLiteProgram, Object[] objArr) {
        if (objArr != null) {
            int length = objArr.length;
            int i = 0;
            while (i < length) {
                Object obj = objArr[i];
                i++;
                bind(supportSQLiteProgram, i, obj);
            }
        }
    }

    private static void bind(SupportSQLiteProgram supportSQLiteProgram, int i, Object obj) {
        if (obj == null) {
            supportSQLiteProgram.bindNull(i);
        } else if (obj instanceof byte[]) {
            supportSQLiteProgram.bindBlob(i, (byte[]) obj);
        } else if (obj instanceof Float) {
            supportSQLiteProgram.bindDouble(i, (double) ((Float) obj).floatValue());
        } else if (obj instanceof Double) {
            supportSQLiteProgram.bindDouble(i, ((Double) obj).doubleValue());
        } else if (obj instanceof Long) {
            supportSQLiteProgram.bindLong(i, ((Long) obj).longValue());
        } else if (obj instanceof Integer) {
            supportSQLiteProgram.bindLong(i, (long) ((Integer) obj).intValue());
        } else if (obj instanceof Short) {
            supportSQLiteProgram.bindLong(i, (long) ((Short) obj).shortValue());
        } else if (obj instanceof Byte) {
            supportSQLiteProgram.bindLong(i, (long) ((Byte) obj).byteValue());
        } else if (obj instanceof String) {
            supportSQLiteProgram.bindString(i, (String) obj);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot bind ");
            stringBuilder.append(obj);
            stringBuilder.append(" at index ");
            stringBuilder.append(i);
            stringBuilder.append(" Supported types: null, byte[], float, double, long, int, short, byte,");
            stringBuilder.append(" string");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }
}
