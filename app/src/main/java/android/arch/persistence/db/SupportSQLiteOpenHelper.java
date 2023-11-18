package android.arch.persistence.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.File;

public interface SupportSQLiteOpenHelper {

    public static abstract class Callback {
        private static final String TAG = "SupportSQLite";
        public final int version;

        public void onConfigure(SupportSQLiteDatabase supportSQLiteDatabase) {
        }

        public abstract void onCreate(SupportSQLiteDatabase supportSQLiteDatabase);

        public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
        }

        public abstract void onUpgrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2);

        public Callback(int i) {
            this.version = i;
        }

        public void onDowngrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can't downgrade database from version ");
            stringBuilder.append(i);
            stringBuilder.append(" to ");
            stringBuilder.append(i2);
            throw new SQLiteException(stringBuilder.toString());
        }

        public void onCorruption(android.arch.persistence.db.SupportSQLiteDatabase r4) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r3 = this;
            r0 = "SupportSQLite";
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "Corruption reported by sqlite on database: ";
            r1.append(r2);
            r2 = r4.getPath();
            r1.append(r2);
            r1 = r1.toString();
            android.util.Log.e(r0, r1);
            r0 = r4.isOpen();
            if (r0 != 0) goto L_0x0028;
        L_0x0020:
            r4 = r4.getPath();
            r3.deleteDatabaseFile(r4);
            return;
        L_0x0028:
            r0 = 0;
            r1 = r4.getAttachedDbs();	 Catch:{ SQLiteException -> 0x0031, all -> 0x002f }
            r0 = r1;
            goto L_0x0031;
        L_0x002f:
            r1 = move-exception;
            goto L_0x0035;
        L_0x0031:
            r4.close();	 Catch:{ IOException -> 0x0057, all -> 0x002f }
            goto L_0x0057;
        L_0x0035:
            if (r0 == 0) goto L_0x004f;
        L_0x0037:
            r4 = r0.iterator();
        L_0x003b:
            r0 = r4.hasNext();
            if (r0 == 0) goto L_0x0056;
        L_0x0041:
            r0 = r4.next();
            r0 = (android.util.Pair) r0;
            r0 = r0.second;
            r0 = (java.lang.String) r0;
            r3.deleteDatabaseFile(r0);
            goto L_0x003b;
        L_0x004f:
            r4 = r4.getPath();
            r3.deleteDatabaseFile(r4);
        L_0x0056:
            throw r1;
        L_0x0057:
            if (r0 == 0) goto L_0x0071;
        L_0x0059:
            r4 = r0.iterator();
        L_0x005d:
            r0 = r4.hasNext();
            if (r0 == 0) goto L_0x0078;
        L_0x0063:
            r0 = r4.next();
            r0 = (android.util.Pair) r0;
            r0 = r0.second;
            r0 = (java.lang.String) r0;
            r3.deleteDatabaseFile(r0);
            goto L_0x005d;
        L_0x0071:
            r4 = r4.getPath();
            r3.deleteDatabaseFile(r4);
        L_0x0078:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.arch.persistence.db.SupportSQLiteOpenHelper.Callback.onCorruption(android.arch.persistence.db.SupportSQLiteDatabase):void");
        }

        private void deleteDatabaseFile(String str) {
            if (!str.equalsIgnoreCase(":memory:")) {
                if (str.trim().length() != 0) {
                    String str2 = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("deleting the database file: ");
                    stringBuilder.append(str);
                    Log.w(str2, stringBuilder.toString());
                    try {
                        if (VERSION.SDK_INT >= 16) {
                            SQLiteDatabase.deleteDatabase(new File(str));
                        } else {
                            try {
                                if (!new File(str).delete()) {
                                    str2 = TAG;
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("Could not delete the database file ");
                                    stringBuilder.append(str);
                                    Log.e(str2, stringBuilder.toString());
                                }
                            } catch (String str3) {
                                Log.e(TAG, "error while deleting corrupted database file", str3);
                            }
                        }
                    } catch (String str32) {
                        Log.w(TAG, "delete failed: ", str32);
                    }
                }
            }
        }
    }

    public static class Configuration {
        @NonNull
        public final Callback callback;
        @NonNull
        public final Context context;
        @Nullable
        public final String name;

        public static class Builder {
            Callback mCallback;
            Context mContext;
            String mName;

            public Configuration build() {
                if (this.mCallback == null) {
                    throw new IllegalArgumentException("Must set a callback to create the configuration.");
                } else if (this.mContext != null) {
                    return new Configuration(this.mContext, this.mName, this.mCallback);
                } else {
                    throw new IllegalArgumentException("Must set a non-null context to create the configuration.");
                }
            }

            Builder(@NonNull Context context) {
                this.mContext = context;
            }

            public Builder name(@Nullable String str) {
                this.mName = str;
                return this;
            }

            public Builder callback(@NonNull Callback callback) {
                this.mCallback = callback;
                return this;
            }
        }

        Configuration(@NonNull Context context, @Nullable String str, @NonNull Callback callback) {
            this.context = context;
            this.name = str;
            this.callback = callback;
        }

        public static Builder builder(Context context) {
            return new Builder(context);
        }
    }

    public interface Factory {
        SupportSQLiteOpenHelper create(Configuration configuration);
    }

    void close();

    String getDatabaseName();

    SupportSQLiteDatabase getReadableDatabase();

    SupportSQLiteDatabase getWritableDatabase();

    @RequiresApi(api = 16)
    void setWriteAheadLoggingEnabled(boolean z);
}
