package android.arch.persistence.room.util;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@RestrictTo({Scope.LIBRARY_GROUP})
public class TableInfo {
    public final Map<String, Column> columns;
    public final Set<ForeignKey> foreignKeys;
    @Nullable
    public final Set<Index> indices;
    public final String name;

    public static class Column {
        public final String name;
        public final boolean notNull;
        public final int primaryKeyPosition;
        public final String type;

        public Column(String str, String str2, boolean z, int i) {
            this.name = str;
            this.type = str2;
            this.notNull = z;
            this.primaryKeyPosition = i;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj != null) {
                if (getClass() == obj.getClass()) {
                    Column column = (Column) obj;
                    if (VERSION.SDK_INT >= 20) {
                        if (this.primaryKeyPosition != column.primaryKeyPosition) {
                            return false;
                        }
                    } else if (isPrimaryKey() != column.isPrimaryKey()) {
                        return false;
                    }
                    if (!this.name.equals(column.name) || this.notNull != column.notNull) {
                        return false;
                    }
                    if (this.type != null) {
                        z = this.type.equalsIgnoreCase(column.type);
                    } else if (column.type != null) {
                        z = false;
                    }
                    return z;
                }
            }
            return false;
        }

        public boolean isPrimaryKey() {
            return this.primaryKeyPosition > 0;
        }

        public int hashCode() {
            return (((((this.name.hashCode() * 31) + (this.type != null ? this.type.hashCode() : 0)) * 31) + (this.notNull ? 1231 : 1237)) * 31) + this.primaryKeyPosition;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Column{name='");
            stringBuilder.append(this.name);
            stringBuilder.append('\'');
            stringBuilder.append(", type='");
            stringBuilder.append(this.type);
            stringBuilder.append('\'');
            stringBuilder.append(", notNull=");
            stringBuilder.append(this.notNull);
            stringBuilder.append(", primaryKeyPosition=");
            stringBuilder.append(this.primaryKeyPosition);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public static class ForeignKey {
        @NonNull
        public final List<String> columnNames;
        @NonNull
        public final String onDelete;
        @NonNull
        public final String onUpdate;
        @NonNull
        public final List<String> referenceColumnNames;
        @NonNull
        public final String referenceTable;

        public ForeignKey(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull List<String> list, @NonNull List<String> list2) {
            this.referenceTable = str;
            this.onDelete = str2;
            this.onUpdate = str3;
            this.columnNames = Collections.unmodifiableList(list);
            this.referenceColumnNames = Collections.unmodifiableList(list2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null) {
                if (getClass() == obj.getClass()) {
                    ForeignKey foreignKey = (ForeignKey) obj;
                    if (this.referenceTable.equals(foreignKey.referenceTable) && this.onDelete.equals(foreignKey.onDelete) && this.onUpdate.equals(foreignKey.onUpdate) && this.columnNames.equals(foreignKey.columnNames)) {
                        return this.referenceColumnNames.equals(foreignKey.referenceColumnNames);
                    }
                    return false;
                }
            }
            return false;
        }

        public int hashCode() {
            return (((((((this.referenceTable.hashCode() * 31) + this.onDelete.hashCode()) * 31) + this.onUpdate.hashCode()) * 31) + this.columnNames.hashCode()) * 31) + this.referenceColumnNames.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ForeignKey{referenceTable='");
            stringBuilder.append(this.referenceTable);
            stringBuilder.append('\'');
            stringBuilder.append(", onDelete='");
            stringBuilder.append(this.onDelete);
            stringBuilder.append('\'');
            stringBuilder.append(", onUpdate='");
            stringBuilder.append(this.onUpdate);
            stringBuilder.append('\'');
            stringBuilder.append(", columnNames=");
            stringBuilder.append(this.columnNames);
            stringBuilder.append(", referenceColumnNames=");
            stringBuilder.append(this.referenceColumnNames);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    static class ForeignKeyWithSequence implements Comparable<ForeignKeyWithSequence> {
        final String mFrom;
        final int mId;
        final int mSequence;
        final String mTo;

        ForeignKeyWithSequence(int i, int i2, String str, String str2) {
            this.mId = i;
            this.mSequence = i2;
            this.mFrom = str;
            this.mTo = str2;
        }

        public int compareTo(ForeignKeyWithSequence foreignKeyWithSequence) {
            int i = this.mId - foreignKeyWithSequence.mId;
            return i == 0 ? this.mSequence - foreignKeyWithSequence.mSequence : i;
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public static class Index {
        public static final String DEFAULT_PREFIX = "index_";
        public final List<String> columns;
        public final String name;
        public final boolean unique;

        public Index(String str, boolean z, List<String> list) {
            this.name = str;
            this.unique = z;
            this.columns = list;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null) {
                if (getClass() == obj.getClass()) {
                    Index index = (Index) obj;
                    if (this.unique != index.unique || !this.columns.equals(index.columns)) {
                        return false;
                    }
                    if (this.name.startsWith(DEFAULT_PREFIX)) {
                        return index.name.startsWith(DEFAULT_PREFIX);
                    }
                    return this.name.equals(index.name);
                }
            }
            return false;
        }

        public int hashCode() {
            int hashCode;
            if (this.name.startsWith(DEFAULT_PREFIX)) {
                hashCode = DEFAULT_PREFIX.hashCode();
            } else {
                hashCode = this.name.hashCode();
            }
            return (((hashCode * 31) + this.unique) * 31) + this.columns.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Index{name='");
            stringBuilder.append(this.name);
            stringBuilder.append('\'');
            stringBuilder.append(", unique=");
            stringBuilder.append(this.unique);
            stringBuilder.append(", columns=");
            stringBuilder.append(this.columns);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }

    public TableInfo(String str, Map<String, Column> map, Set<ForeignKey> set, Set<Index> set2) {
        this.name = str;
        this.columns = Collections.unmodifiableMap(map);
        this.foreignKeys = Collections.unmodifiableSet(set);
        if (set2 == null) {
            str = null;
        } else {
            str = Collections.unmodifiableSet(set2);
        }
        this.indices = str;
    }

    public TableInfo(String str, Map<String, Column> map, Set<ForeignKey> set) {
        this(str, map, set, Collections.emptySet());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
        r4 = this;
        r0 = 1;
        if (r4 != r5) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = 0;
        if (r5 == 0) goto L_0x0063;
    L_0x0007:
        r2 = r4.getClass();
        r3 = r5.getClass();
        if (r2 == r3) goto L_0x0012;
    L_0x0011:
        goto L_0x0063;
    L_0x0012:
        r5 = (android.arch.persistence.room.util.TableInfo) r5;
        r2 = r4.name;
        if (r2 == 0) goto L_0x0023;
    L_0x0018:
        r2 = r4.name;
        r3 = r5.name;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x0028;
    L_0x0022:
        goto L_0x0027;
    L_0x0023:
        r2 = r5.name;
        if (r2 == 0) goto L_0x0028;
    L_0x0027:
        return r1;
    L_0x0028:
        r2 = r4.columns;
        if (r2 == 0) goto L_0x0037;
    L_0x002c:
        r2 = r4.columns;
        r3 = r5.columns;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x003c;
    L_0x0036:
        goto L_0x003b;
    L_0x0037:
        r2 = r5.columns;
        if (r2 == 0) goto L_0x003c;
    L_0x003b:
        return r1;
    L_0x003c:
        r2 = r4.foreignKeys;
        if (r2 == 0) goto L_0x004b;
    L_0x0040:
        r2 = r4.foreignKeys;
        r3 = r5.foreignKeys;
        r2 = r2.equals(r3);
        if (r2 != 0) goto L_0x0050;
    L_0x004a:
        goto L_0x004f;
    L_0x004b:
        r2 = r5.foreignKeys;
        if (r2 == 0) goto L_0x0050;
    L_0x004f:
        return r1;
    L_0x0050:
        r1 = r4.indices;
        if (r1 == 0) goto L_0x0062;
    L_0x0054:
        r1 = r5.indices;
        if (r1 != 0) goto L_0x0059;
    L_0x0058:
        goto L_0x0062;
    L_0x0059:
        r0 = r4.indices;
        r5 = r5.indices;
        r5 = r0.equals(r5);
        return r5;
    L_0x0062:
        return r0;
    L_0x0063:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.arch.persistence.room.util.TableInfo.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int i = 0;
        int hashCode = (((this.name != null ? this.name.hashCode() : 0) * 31) + (this.columns != null ? this.columns.hashCode() : 0)) * 31;
        if (this.foreignKeys != null) {
            i = this.foreignKeys.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TableInfo{name='");
        stringBuilder.append(this.name);
        stringBuilder.append('\'');
        stringBuilder.append(", columns=");
        stringBuilder.append(this.columns);
        stringBuilder.append(", foreignKeys=");
        stringBuilder.append(this.foreignKeys);
        stringBuilder.append(", indices=");
        stringBuilder.append(this.indices);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public static TableInfo read(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        return new TableInfo(str, readColumns(supportSQLiteDatabase, str), readForeignKeys(supportSQLiteDatabase, str), readIndices(supportSQLiteDatabase, str));
    }

    private static Set<ForeignKey> readForeignKeys(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        Set<ForeignKey> hashSet = new HashSet();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PRAGMA foreign_key_list(`");
        stringBuilder.append(str);
        stringBuilder.append("`)");
        Cursor query = supportSQLiteDatabase.query(stringBuilder.toString());
        try {
            int columnIndex = query.getColumnIndex(TtmlNode.ATTR_ID);
            int columnIndex2 = query.getColumnIndex("seq");
            int columnIndex3 = query.getColumnIndex("table");
            int columnIndex4 = query.getColumnIndex("on_delete");
            int columnIndex5 = query.getColumnIndex("on_update");
            List<ForeignKeyWithSequence> readForeignKeyFieldMappings = readForeignKeyFieldMappings(query);
            int count = query.getCount();
            int i = 0;
            while (i < count) {
                int i2;
                query.moveToPosition(i);
                if (query.getInt(columnIndex2) != 0) {
                    i2 = columnIndex;
                } else {
                    int i3 = query.getInt(columnIndex);
                    List arrayList = new ArrayList();
                    List arrayList2 = new ArrayList();
                    for (ForeignKeyWithSequence foreignKeyWithSequence : readForeignKeyFieldMappings) {
                        i2 = columnIndex;
                        if (foreignKeyWithSequence.mId == i3) {
                            arrayList.add(foreignKeyWithSequence.mFrom);
                            arrayList2.add(foreignKeyWithSequence.mTo);
                        }
                        columnIndex = i2;
                    }
                    i2 = columnIndex;
                    List list = arrayList2;
                    hashSet.add(new ForeignKey(query.getString(columnIndex3), query.getString(columnIndex4), query.getString(columnIndex5), arrayList, list));
                }
                i++;
                columnIndex = i2;
            }
            query.close();
            return hashSet;
        } catch (Throwable th) {
            Throwable th2 = th;
            query.close();
        }
    }

    private static List<ForeignKeyWithSequence> readForeignKeyFieldMappings(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(TtmlNode.ATTR_ID);
        int columnIndex2 = cursor.getColumnIndex("seq");
        int columnIndex3 = cursor.getColumnIndex("from");
        int columnIndex4 = cursor.getColumnIndex("to");
        int count = cursor.getCount();
        List<ForeignKeyWithSequence> arrayList = new ArrayList();
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            arrayList.add(new ForeignKeyWithSequence(cursor.getInt(columnIndex), cursor.getInt(columnIndex2), cursor.getString(columnIndex3), cursor.getString(columnIndex4)));
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    private static Map<String, Column> readColumns(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PRAGMA table_info(`");
        stringBuilder.append(str);
        stringBuilder.append("`)");
        supportSQLiteDatabase = supportSQLiteDatabase.query(stringBuilder.toString());
        str = new HashMap();
        try {
            if (supportSQLiteDatabase.getColumnCount() > 0) {
                int columnIndex = supportSQLiteDatabase.getColumnIndex("name");
                int columnIndex2 = supportSQLiteDatabase.getColumnIndex("type");
                int columnIndex3 = supportSQLiteDatabase.getColumnIndex("notnull");
                int columnIndex4 = supportSQLiteDatabase.getColumnIndex("pk");
                while (supportSQLiteDatabase.moveToNext()) {
                    String string = supportSQLiteDatabase.getString(columnIndex);
                    str.put(string, new Column(string, supportSQLiteDatabase.getString(columnIndex2), supportSQLiteDatabase.getInt(columnIndex3) != 0, supportSQLiteDatabase.getInt(columnIndex4)));
                }
            }
            supportSQLiteDatabase.close();
            return str;
        } catch (Throwable th) {
            supportSQLiteDatabase.close();
        }
    }

    @Nullable
    private static Set<Index> readIndices(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PRAGMA index_list(`");
        stringBuilder.append(str);
        stringBuilder.append("`)");
        str = supportSQLiteDatabase.query(stringBuilder.toString());
        try {
            int columnIndex = str.getColumnIndex("name");
            int columnIndex2 = str.getColumnIndex(TtmlNode.ATTR_TTS_ORIGIN);
            int columnIndex3 = str.getColumnIndex("unique");
            if (!(columnIndex == -1 || columnIndex2 == -1)) {
                if (columnIndex3 != -1) {
                    Set hashSet = new HashSet();
                    while (str.moveToNext()) {
                        if ("c".equals(str.getString(columnIndex2))) {
                            String string = str.getString(columnIndex);
                            boolean z = true;
                            if (str.getInt(columnIndex3) != 1) {
                                z = false;
                            }
                            Index readIndex = readIndex(supportSQLiteDatabase, string, z);
                            if (readIndex == null) {
                                str.close();
                                return null;
                            }
                            hashSet.add(readIndex);
                        }
                    }
                    str.close();
                    return hashSet;
                }
            }
            str.close();
            return null;
        } catch (Throwable th) {
            str.close();
        }
    }

    @Nullable
    private static Index readIndex(SupportSQLiteDatabase supportSQLiteDatabase, String str, boolean z) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PRAGMA index_xinfo(`");
        stringBuilder.append(str);
        stringBuilder.append("`)");
        supportSQLiteDatabase = supportSQLiteDatabase.query(stringBuilder.toString());
        try {
            int columnIndex = supportSQLiteDatabase.getColumnIndex("seqno");
            int columnIndex2 = supportSQLiteDatabase.getColumnIndex("cid");
            int columnIndex3 = supportSQLiteDatabase.getColumnIndex("name");
            if (!(columnIndex == -1 || columnIndex2 == -1)) {
                if (columnIndex3 != -1) {
                    TreeMap treeMap = new TreeMap();
                    while (supportSQLiteDatabase.moveToNext()) {
                        if (supportSQLiteDatabase.getInt(columnIndex2) >= 0) {
                            int i = supportSQLiteDatabase.getInt(columnIndex);
                            treeMap.put(Integer.valueOf(i), supportSQLiteDatabase.getString(columnIndex3));
                        }
                    }
                    List arrayList = new ArrayList(treeMap.size());
                    arrayList.addAll(treeMap.values());
                    Index index = new Index(str, z, arrayList);
                    supportSQLiteDatabase.close();
                    return index;
                }
            }
            supportSQLiteDatabase.close();
            return null;
        } catch (Throwable th) {
            supportSQLiteDatabase.close();
        }
    }
}
