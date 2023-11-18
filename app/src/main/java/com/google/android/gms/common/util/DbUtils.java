package com.google.android.gms.common.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.stable.zzk;
import java.nio.charset.Charset;
import java.util.Set;
import javax.annotation.Nullable;

public final class DbUtils {
    private DbUtils() {
    }

    public static void clearDatabase(SQLiteDatabase sQLiteDatabase) {
        zza(sQLiteDatabase, "table", "sqlite_sequence", "android_metadata");
        zza(sQLiteDatabase, "trigger", new String[0]);
        zza(sQLiteDatabase, "view", new String[0]);
    }

    public static long countCurrentRowBytes(Cursor cursor) {
        return countCurrentRowBytes(cursor, Charset.forName("UTF-8"));
    }

    public static long countCurrentRowBytes(Cursor cursor, Charset charset) {
        long j = 0;
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            long j2;
            int length;
            switch (cursor.getType(i)) {
                case 0:
                case 1:
                case 2:
                    j2 = 4;
                    break;
                case 3:
                    length = cursor.getString(i).getBytes(charset).length;
                    break;
                case 4:
                    length = cursor.getBlob(i).length;
                    break;
                default:
                    continue;
            }
            j2 = (long) length;
            j += j2;
        }
        return j;
    }

    public static long getDatabaseSize(android.content.Context r2, java.lang.String r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r2 = r2.getDatabasePath(r3);	 Catch:{ SecurityException -> 0x000b }
        if (r2 == 0) goto L_0x0026;	 Catch:{ SecurityException -> 0x000b }
    L_0x0006:
        r0 = r2.length();	 Catch:{ SecurityException -> 0x000b }
        return r0;
    L_0x000b:
        r2 = "DbUtils";
        r0 = "Failed to get db size for ";
        r3 = java.lang.String.valueOf(r3);
        r1 = r3.length();
        if (r1 == 0) goto L_0x001e;
    L_0x0019:
        r3 = r0.concat(r3);
        goto L_0x0023;
    L_0x001e:
        r3 = new java.lang.String;
        r3.<init>(r0);
    L_0x0023:
        android.util.Log.w(r2, r3);
    L_0x0026:
        r2 = 0;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.DbUtils.getDatabaseSize(android.content.Context, java.lang.String):long");
    }

    @Nullable
    public static Integer getIntegerFromCursor(Cursor cursor, int i) {
        return getIntegerFromCursor(cursor, i, null);
    }

    @Nullable
    public static Integer getIntegerFromCursor(Cursor cursor, int i, @Nullable Integer num) {
        return (i < 0 || cursor.isNull(i)) ? num : Integer.valueOf(cursor.getInt(i));
    }

    @Nullable
    public static Long getLongFromCursor(Cursor cursor, int i) {
        return getLongFromCursor(cursor, i, null);
    }

    @Nullable
    public static Long getLongFromCursor(Cursor cursor, int i, @Nullable Long l) {
        return (i < 0 || cursor.isNull(i)) ? l : Long.valueOf(cursor.getLong(i));
    }

    @Nullable
    public static String getStringFromCursor(Cursor cursor, int i) {
        return getStringFromCursor(cursor, i, null);
    }

    @Nullable
    public static String getStringFromCursor(Cursor cursor, int i, @Nullable String str) {
        return (i < 0 || cursor.isNull(i)) ? str : cursor.getString(i);
    }

    public static void putIntegerIntoContentValues(ContentValues contentValues, String str, @Nullable Integer num) {
        if (num != null) {
            contentValues.put(str, num);
        } else {
            contentValues.putNull(str);
        }
    }

    public static void putLongIntoContentValues(ContentValues contentValues, String str, @Nullable Long l) {
        if (l != null) {
            contentValues.put(str, l);
        } else {
            contentValues.putNull(str);
        }
    }

    public static void putStringIntoContentValues(ContentValues contentValues, String str, @Nullable String str2) {
        if (str2 != null) {
            contentValues.put(str, str2);
        } else {
            contentValues.putNull(str);
        }
    }

    private static void zza(SQLiteDatabase sQLiteDatabase, String str, String... strArr) {
        boolean z;
        SQLiteDatabase sQLiteDatabase2;
        Cursor query;
        Set of;
        String string;
        if (!("table".equals(str) || "view".equals(str))) {
            if (!"trigger".equals(str)) {
                z = false;
                Preconditions.checkArgument(z);
                sQLiteDatabase2 = sQLiteDatabase;
                query = sQLiteDatabase2.query("SQLITE_MASTER", new String[]{"name"}, "type == ?", new String[]{str}, null, null, null);
                of = CollectionUtils.setOf((Object[]) strArr);
                while (query.moveToNext()) {
                    string = query.getString(0);
                    if (!of.contains(string)) {
                        StringBuilder stringBuilder = new StringBuilder((String.valueOf(str).length() + 8) + String.valueOf(string).length());
                        stringBuilder.append("DROP ");
                        stringBuilder.append(str);
                        stringBuilder.append(" '");
                        stringBuilder.append(string);
                        stringBuilder.append("'");
                        sQLiteDatabase.execSQL(stringBuilder.toString());
                    }
                }
                if (query != null) {
                    query.close();
                }
            }
        }
        z = true;
        Preconditions.checkArgument(z);
        sQLiteDatabase2 = sQLiteDatabase;
        query = sQLiteDatabase2.query("SQLITE_MASTER", new String[]{"name"}, "type == ?", new String[]{str}, null, null, null);
        try {
            of = CollectionUtils.setOf((Object[]) strArr);
            while (query.moveToNext()) {
                string = query.getString(0);
                if (!of.contains(string)) {
                    StringBuilder stringBuilder2 = new StringBuilder((String.valueOf(str).length() + 8) + String.valueOf(string).length());
                    stringBuilder2.append("DROP ");
                    stringBuilder2.append(str);
                    stringBuilder2.append(" '");
                    stringBuilder2.append(string);
                    stringBuilder2.append("'");
                    sQLiteDatabase.execSQL(stringBuilder2.toString());
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (Throwable th) {
            zzk.zza(r1, th);
        }
    }
}
