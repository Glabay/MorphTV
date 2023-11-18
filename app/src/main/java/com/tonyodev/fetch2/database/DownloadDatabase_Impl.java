package com.tonyodev.fetch2.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase.Callback;
import android.arch.persistence.room.RoomMasterTable;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.Index;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DownloadDatabase_Impl extends DownloadDatabase {
    private volatile DownloadDao _downloadDao;

    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, new Delegate(2) {
            public void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `requests` (`_id` INTEGER NOT NULL, `_namespace` TEXT NOT NULL, `_url` TEXT NOT NULL, `_file` TEXT NOT NULL, `_group` INTEGER NOT NULL, `_priority` INTEGER NOT NULL, `_headers` TEXT NOT NULL, `_written_bytes` INTEGER NOT NULL, `_total_bytes` INTEGER NOT NULL, `_status` INTEGER NOT NULL, `_error` INTEGER NOT NULL, `_network_type` INTEGER NOT NULL, `_created` INTEGER NOT NULL, `_tag` TEXT, PRIMARY KEY(`_id`))");
                supportSQLiteDatabase.execSQL("CREATE UNIQUE INDEX `index_requests__file` ON `requests` (`_file`)");
                supportSQLiteDatabase.execSQL("CREATE  INDEX `index_requests__group__status` ON `requests` (`_group`, `_status`)");
                supportSQLiteDatabase.execSQL(RoomMasterTable.CREATE_QUERY);
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"20c1a3a105883969ab31941cf131bb98\")");
            }

            public void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `requests`");
            }

            protected void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
                if (DownloadDatabase_Impl.this.mCallbacks != null) {
                    int size = DownloadDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) DownloadDatabase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
                DownloadDatabase_Impl.this.mDatabase = supportSQLiteDatabase;
                DownloadDatabase_Impl.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (DownloadDatabase_Impl.this.mCallbacks != null) {
                    int size = DownloadDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) DownloadDatabase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }

            protected void validateMigration(SupportSQLiteDatabase supportSQLiteDatabase) {
                Map hashMap = new HashMap(14);
                hashMap.put(DownloadDatabase.COLUMN_ID, new Column(DownloadDatabase.COLUMN_ID, "INTEGER", true, 1));
                hashMap.put(DownloadDatabase.COLUMN_NAMESPACE, new Column(DownloadDatabase.COLUMN_NAMESPACE, "TEXT", true, 0));
                hashMap.put(DownloadDatabase.COLUMN_URL, new Column(DownloadDatabase.COLUMN_URL, "TEXT", true, 0));
                hashMap.put(DownloadDatabase.COLUMN_FILE, new Column(DownloadDatabase.COLUMN_FILE, "TEXT", true, 0));
                hashMap.put(DownloadDatabase.COLUMN_GROUP, new Column(DownloadDatabase.COLUMN_GROUP, "INTEGER", true, 0));
                hashMap.put(DownloadDatabase.COLUMN_PRIORITY, new Column(DownloadDatabase.COLUMN_PRIORITY, "INTEGER", true, 0));
                hashMap.put(DownloadDatabase.COLUMN_HEADERS, new Column(DownloadDatabase.COLUMN_HEADERS, "TEXT", true, 0));
                hashMap.put(DownloadDatabase.COLUMN_DOWNLOADED, new Column(DownloadDatabase.COLUMN_DOWNLOADED, "INTEGER", true, 0));
                hashMap.put(DownloadDatabase.COLUMN_TOTAL, new Column(DownloadDatabase.COLUMN_TOTAL, "INTEGER", true, 0));
                hashMap.put(DownloadDatabase.COLUMN_STATUS, new Column(DownloadDatabase.COLUMN_STATUS, "INTEGER", true, 0));
                hashMap.put(DownloadDatabase.COLUMN_ERROR, new Column(DownloadDatabase.COLUMN_ERROR, "INTEGER", true, 0));
                hashMap.put(DownloadDatabase.COLUMN_NETWORK_TYPE, new Column(DownloadDatabase.COLUMN_NETWORK_TYPE, "INTEGER", true, 0));
                hashMap.put(DownloadDatabase.COLUMN_CREATED, new Column(DownloadDatabase.COLUMN_CREATED, "INTEGER", true, 0));
                hashMap.put(DownloadDatabase.COLUMN_TAG, new Column(DownloadDatabase.COLUMN_TAG, "TEXT", false, 0));
                Set hashSet = new HashSet(0);
                Set hashSet2 = new HashSet(2);
                hashSet2.add(new Index("index_requests__file", true, Arrays.asList(new String[]{DownloadDatabase.COLUMN_FILE})));
                hashSet2.add(new Index("index_requests__group__status", false, Arrays.asList(new String[]{DownloadDatabase.COLUMN_GROUP, DownloadDatabase.COLUMN_STATUS})));
                TableInfo tableInfo = new TableInfo(DownloadDatabase.TABLE_NAME, hashMap, hashSet, hashSet2);
                supportSQLiteDatabase = TableInfo.read(supportSQLiteDatabase, DownloadDatabase.TABLE_NAME);
                if (!tableInfo.equals(supportSQLiteDatabase)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Migration didn't properly handle requests(com.tonyodev.fetch2.database.DownloadInfo).\n Expected:\n");
                    stringBuilder.append(tableInfo);
                    stringBuilder.append("\n Found:\n");
                    stringBuilder.append(supportSQLiteDatabase);
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
        }, "20c1a3a105883969ab31941cf131bb98")).build());
    }

    protected InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, new String[]{DownloadDatabase.TABLE_NAME});
    }

    public DownloadDao requestDao() {
        if (this._downloadDao != null) {
            return this._downloadDao;
        }
        DownloadDao downloadDao;
        synchronized (this) {
            if (this._downloadDao == null) {
                this._downloadDao = new DownloadDao_Impl(this);
            }
            downloadDao = this._downloadDao;
        }
        return downloadDao;
    }
}
