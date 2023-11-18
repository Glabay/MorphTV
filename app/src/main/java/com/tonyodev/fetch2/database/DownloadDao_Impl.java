package com.tonyodev.fetch2.database;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.arch.persistence.room.util.StringUtil;
import android.database.Cursor;
import com.tonyodev.fetch2.Status;
import java.util.ArrayList;
import java.util.List;

public class DownloadDao_Impl implements DownloadDao {
    private final Converter __converter = new Converter();
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter __deletionAdapterOfDownloadInfo;
    private final EntityInsertionAdapter __insertionAdapterOfDownloadInfo;
    private final SharedSQLiteStatement __preparedStmtOfDeleteAll;
    private final EntityDeletionOrUpdateAdapter __updateAdapterOfDownloadInfo;

    public DownloadDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfDownloadInfo = new EntityInsertionAdapter<DownloadInfo>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR ABORT INTO `requests`(`_id`,`_namespace`,`_url`,`_file`,`_group`,`_priority`,`_headers`,`_written_bytes`,`_total_bytes`,`_status`,`_error`,`_network_type`,`_created`,`_tag`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, DownloadInfo downloadInfo) {
                supportSQLiteStatement.bindLong(1, (long) downloadInfo.getId());
                if (downloadInfo.getNamespace() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, downloadInfo.getNamespace());
                }
                if (downloadInfo.getUrl() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, downloadInfo.getUrl());
                }
                if (downloadInfo.getFile() == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, downloadInfo.getFile());
                }
                supportSQLiteStatement.bindLong(5, (long) downloadInfo.getGroup());
                supportSQLiteStatement.bindLong(6, (long) DownloadDao_Impl.this.__converter.toPriorityValue(downloadInfo.getPriority()));
                String toHeaderString = DownloadDao_Impl.this.__converter.toHeaderString(downloadInfo.getHeaders());
                if (toHeaderString == null) {
                    supportSQLiteStatement.bindNull(7);
                } else {
                    supportSQLiteStatement.bindString(7, toHeaderString);
                }
                supportSQLiteStatement.bindLong(8, downloadInfo.getDownloaded());
                supportSQLiteStatement.bindLong(9, downloadInfo.getTotal());
                supportSQLiteStatement.bindLong(10, (long) DownloadDao_Impl.this.__converter.toStatusValue(downloadInfo.getStatus()));
                supportSQLiteStatement.bindLong(11, (long) DownloadDao_Impl.this.__converter.toErrorValue(downloadInfo.getError()));
                supportSQLiteStatement.bindLong(12, (long) DownloadDao_Impl.this.__converter.toNetworkTypeValue(downloadInfo.getNetworkType()));
                supportSQLiteStatement.bindLong(13, downloadInfo.getCreated());
                if (downloadInfo.getTag() == null) {
                    supportSQLiteStatement.bindNull(14);
                } else {
                    supportSQLiteStatement.bindString(14, downloadInfo.getTag());
                }
            }
        };
        this.__deletionAdapterOfDownloadInfo = new EntityDeletionOrUpdateAdapter<DownloadInfo>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `requests` WHERE `_id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, DownloadInfo downloadInfo) {
                supportSQLiteStatement.bindLong(1, (long) downloadInfo.getId());
            }
        };
        this.__updateAdapterOfDownloadInfo = new EntityDeletionOrUpdateAdapter<DownloadInfo>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR REPLACE `requests` SET `_id` = ?,`_namespace` = ?,`_url` = ?,`_file` = ?,`_group` = ?,`_priority` = ?,`_headers` = ?,`_written_bytes` = ?,`_total_bytes` = ?,`_status` = ?,`_error` = ?,`_network_type` = ?,`_created` = ?,`_tag` = ? WHERE `_id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, DownloadInfo downloadInfo) {
                supportSQLiteStatement.bindLong(1, (long) downloadInfo.getId());
                if (downloadInfo.getNamespace() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, downloadInfo.getNamespace());
                }
                if (downloadInfo.getUrl() == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, downloadInfo.getUrl());
                }
                if (downloadInfo.getFile() == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, downloadInfo.getFile());
                }
                supportSQLiteStatement.bindLong(5, (long) downloadInfo.getGroup());
                supportSQLiteStatement.bindLong(6, (long) DownloadDao_Impl.this.__converter.toPriorityValue(downloadInfo.getPriority()));
                String toHeaderString = DownloadDao_Impl.this.__converter.toHeaderString(downloadInfo.getHeaders());
                if (toHeaderString == null) {
                    supportSQLiteStatement.bindNull(7);
                } else {
                    supportSQLiteStatement.bindString(7, toHeaderString);
                }
                supportSQLiteStatement.bindLong(8, downloadInfo.getDownloaded());
                supportSQLiteStatement.bindLong(9, downloadInfo.getTotal());
                supportSQLiteStatement.bindLong(10, (long) DownloadDao_Impl.this.__converter.toStatusValue(downloadInfo.getStatus()));
                supportSQLiteStatement.bindLong(11, (long) DownloadDao_Impl.this.__converter.toErrorValue(downloadInfo.getError()));
                supportSQLiteStatement.bindLong(12, (long) DownloadDao_Impl.this.__converter.toNetworkTypeValue(downloadInfo.getNetworkType()));
                supportSQLiteStatement.bindLong(13, downloadInfo.getCreated());
                if (downloadInfo.getTag() == null) {
                    supportSQLiteStatement.bindNull(14);
                } else {
                    supportSQLiteStatement.bindString(14, downloadInfo.getTag());
                }
                supportSQLiteStatement.bindLong(15, (long) downloadInfo.getId());
            }
        };
        this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM requests";
            }
        };
    }

    public long insert(DownloadInfo downloadInfo) {
        long insertAndReturnId;
        this.__db.beginTransaction();
        try {
            insertAndReturnId = this.__insertionAdapterOfDownloadInfo.insertAndReturnId(downloadInfo);
            this.__db.setTransactionSuccessful();
            return insertAndReturnId;
        } finally {
            insertAndReturnId = this.__db;
            insertAndReturnId.endTransaction();
        }
    }

    public List<Long> insert(List<DownloadInfo> list) {
        this.__db.beginTransaction();
        try {
            list = this.__insertionAdapterOfDownloadInfo.insertAndReturnIdsList(list);
            this.__db.setTransactionSuccessful();
            return list;
        } finally {
            this.__db.endTransaction();
        }
    }

    public void delete(DownloadInfo downloadInfo) {
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfDownloadInfo.handle(downloadInfo);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void delete(List<DownloadInfo> list) {
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfDownloadInfo.handleMultiple(list);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void update(DownloadInfo downloadInfo) {
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfDownloadInfo.handle(downloadInfo);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void update(List<DownloadInfo> list) {
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfDownloadInfo.handleMultiple(list);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void deleteAll() {
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteAll.acquire();
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteAll.release(acquire);
        }
    }

    public List<DownloadInfo> get() {
        Throwable th;
        Throwable th2;
        RoomSQLiteQuery roomSQLiteQuery;
        SupportSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM requests", 0);
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ID);
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NAMESPACE);
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_URL);
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_FILE);
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_GROUP);
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_PRIORITY);
            int columnIndexOrThrow7 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_HEADERS);
            int columnIndexOrThrow8 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_DOWNLOADED);
            int columnIndexOrThrow9 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TOTAL);
            int columnIndexOrThrow10 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_STATUS);
            int columnIndexOrThrow11 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ERROR);
            int columnIndexOrThrow12 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NETWORK_TYPE);
            SupportSQLiteQuery supportSQLiteQuery = acquire;
            try {
                int columnIndexOrThrow13 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_CREATED);
                int columnIndexOrThrow14 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TAG);
                int i = columnIndexOrThrow12;
                List<DownloadInfo> arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    try {
                        DownloadInfo downloadInfo = new DownloadInfo();
                        List<DownloadInfo> list = arrayList;
                        downloadInfo.setId(query.getInt(columnIndexOrThrow));
                        downloadInfo.setNamespace(query.getString(columnIndexOrThrow2));
                        downloadInfo.setUrl(query.getString(columnIndexOrThrow3));
                        downloadInfo.setFile(query.getString(columnIndexOrThrow4));
                        downloadInfo.setGroup(query.getInt(columnIndexOrThrow5));
                        int i2 = columnIndexOrThrow;
                        downloadInfo.setPriority(r1.__converter.fromPriorityValue(query.getInt(columnIndexOrThrow6)));
                        downloadInfo.setHeaders(r1.__converter.fromHeaderString(query.getString(columnIndexOrThrow7)));
                        int i3 = columnIndexOrThrow2;
                        downloadInfo.setDownloaded(query.getLong(columnIndexOrThrow8));
                        downloadInfo.setTotal(query.getLong(columnIndexOrThrow9));
                        downloadInfo.setStatus(r1.__converter.fromStatusValue(query.getInt(columnIndexOrThrow10)));
                        downloadInfo.setError(r1.__converter.fromErrorValue(query.getInt(columnIndexOrThrow11)));
                        int i4 = i;
                        downloadInfo.setNetworkType(r1.__converter.fromNetworkTypeValue(query.getInt(i4)));
                        int i5 = i4;
                        columnIndexOrThrow = columnIndexOrThrow13;
                        downloadInfo.setCreated(query.getLong(columnIndexOrThrow));
                        int i6 = columnIndexOrThrow14;
                        downloadInfo.setTag(query.getString(i6));
                        arrayList = list;
                        arrayList.add(downloadInfo);
                        columnIndexOrThrow14 = i6;
                        columnIndexOrThrow13 = columnIndexOrThrow;
                        columnIndexOrThrow = i2;
                        columnIndexOrThrow2 = i3;
                        i = i5;
                        DownloadDao_Impl downloadDao_Impl = this;
                    } catch (Throwable th22) {
                        th = th22;
                        roomSQLiteQuery = supportSQLiteQuery;
                    }
                }
                query.close();
                supportSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th3) {
                th22 = th3;
                roomSQLiteQuery = supportSQLiteQuery;
                th = th22;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th4) {
            th22 = th4;
            roomSQLiteQuery = acquire;
            th = th22;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public DownloadInfo get(int i) {
        Throwable th;
        Throwable th2;
        RoomSQLiteQuery roomSQLiteQuery;
        SupportSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM requests WHERE _id = ?", 1);
        acquire.bindLong(1, (long) i);
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ID);
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NAMESPACE);
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_URL);
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_FILE);
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_GROUP);
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_PRIORITY);
            int columnIndexOrThrow7 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_HEADERS);
            int columnIndexOrThrow8 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_DOWNLOADED);
            int columnIndexOrThrow9 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TOTAL);
            int columnIndexOrThrow10 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_STATUS);
            int columnIndexOrThrow11 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ERROR);
            int columnIndexOrThrow12 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NETWORK_TYPE);
            SupportSQLiteQuery supportSQLiteQuery = acquire;
            try {
                DownloadInfo downloadInfo;
                int columnIndexOrThrow13 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_CREATED);
                int columnIndexOrThrow14 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TAG);
                if (query.moveToFirst()) {
                    int i2 = columnIndexOrThrow14;
                    try {
                        downloadInfo = new DownloadInfo();
                        downloadInfo.setId(query.getInt(columnIndexOrThrow));
                        downloadInfo.setNamespace(query.getString(columnIndexOrThrow2));
                        downloadInfo.setUrl(query.getString(columnIndexOrThrow3));
                        downloadInfo.setFile(query.getString(columnIndexOrThrow4));
                        downloadInfo.setGroup(query.getInt(columnIndexOrThrow5));
                        downloadInfo.setPriority(r1.__converter.fromPriorityValue(query.getInt(columnIndexOrThrow6)));
                        downloadInfo.setHeaders(r1.__converter.fromHeaderString(query.getString(columnIndexOrThrow7)));
                        downloadInfo.setDownloaded(query.getLong(columnIndexOrThrow8));
                        downloadInfo.setTotal(query.getLong(columnIndexOrThrow9));
                        downloadInfo.setStatus(r1.__converter.fromStatusValue(query.getInt(columnIndexOrThrow10)));
                        downloadInfo.setError(r1.__converter.fromErrorValue(query.getInt(columnIndexOrThrow11)));
                        downloadInfo.setNetworkType(r1.__converter.fromNetworkTypeValue(query.getInt(columnIndexOrThrow12)));
                        downloadInfo.setCreated(query.getLong(columnIndexOrThrow13));
                        downloadInfo.setTag(query.getString(i2));
                    } catch (Throwable th22) {
                        th = th22;
                        roomSQLiteQuery = supportSQLiteQuery;
                        query.close();
                        roomSQLiteQuery.release();
                        throw th;
                    }
                }
                downloadInfo = null;
                query.close();
                supportSQLiteQuery.release();
                return downloadInfo;
            } catch (Throwable th3) {
                th22 = th3;
                roomSQLiteQuery = supportSQLiteQuery;
                th = th22;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th4) {
            th22 = th4;
            roomSQLiteQuery = acquire;
            th = th22;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public List<DownloadInfo> get(List<Integer> list) {
        Throwable th;
        Throwable th2;
        RoomSQLiteQuery roomSQLiteQuery;
        DownloadDao_Impl downloadDao_Impl = this;
        StringBuilder newStringBuilder = StringUtil.newStringBuilder();
        newStringBuilder.append("SELECT * FROM requests WHERE _id IN (");
        int size = list.size();
        StringUtil.appendPlaceholders(newStringBuilder, size);
        newStringBuilder.append(")");
        SupportSQLiteQuery acquire = RoomSQLiteQuery.acquire(newStringBuilder.toString(), size + 0);
        int i = 1;
        for (Integer num : list) {
            if (num == null) {
                acquire.bindNull(i);
            } else {
                acquire.bindLong(i, (long) num.intValue());
            }
            i++;
        }
        Cursor query = downloadDao_Impl.__db.query(acquire);
        try {
            i = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ID);
            int columnIndexOrThrow = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NAMESPACE);
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_URL);
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_FILE);
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_GROUP);
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_PRIORITY);
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_HEADERS);
            int columnIndexOrThrow7 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_DOWNLOADED);
            int columnIndexOrThrow8 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TOTAL);
            int columnIndexOrThrow9 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_STATUS);
            int columnIndexOrThrow10 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ERROR);
            int columnIndexOrThrow11 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NETWORK_TYPE);
            SupportSQLiteQuery supportSQLiteQuery = acquire;
            try {
                int columnIndexOrThrow12 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_CREATED);
                int columnIndexOrThrow13 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TAG);
                int i2 = columnIndexOrThrow11;
                List<DownloadInfo> arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    try {
                        DownloadInfo downloadInfo = new DownloadInfo();
                        List<DownloadInfo> list2 = arrayList;
                        downloadInfo.setId(query.getInt(i));
                        downloadInfo.setNamespace(query.getString(columnIndexOrThrow));
                        downloadInfo.setUrl(query.getString(columnIndexOrThrow2));
                        downloadInfo.setFile(query.getString(columnIndexOrThrow3));
                        downloadInfo.setGroup(query.getInt(columnIndexOrThrow4));
                        int i3 = i;
                        downloadInfo.setPriority(downloadDao_Impl.__converter.fromPriorityValue(query.getInt(columnIndexOrThrow5)));
                        downloadInfo.setHeaders(downloadDao_Impl.__converter.fromHeaderString(query.getString(columnIndexOrThrow6)));
                        int i4 = columnIndexOrThrow;
                        downloadInfo.setDownloaded(query.getLong(columnIndexOrThrow7));
                        downloadInfo.setTotal(query.getLong(columnIndexOrThrow8));
                        downloadInfo.setStatus(downloadDao_Impl.__converter.fromStatusValue(query.getInt(columnIndexOrThrow9)));
                        downloadInfo.setError(downloadDao_Impl.__converter.fromErrorValue(query.getInt(columnIndexOrThrow10)));
                        int i5 = i2;
                        downloadInfo.setNetworkType(downloadDao_Impl.__converter.fromNetworkTypeValue(query.getInt(i5)));
                        int i6 = i5;
                        i = columnIndexOrThrow12;
                        downloadInfo.setCreated(query.getLong(i));
                        int i7 = columnIndexOrThrow13;
                        downloadInfo.setTag(query.getString(i7));
                        arrayList = list2;
                        arrayList.add(downloadInfo);
                        columnIndexOrThrow13 = i7;
                        columnIndexOrThrow12 = i;
                        i = i3;
                        columnIndexOrThrow = i4;
                        i2 = i6;
                        downloadDao_Impl = this;
                    } catch (Throwable th22) {
                        th = th22;
                        roomSQLiteQuery = supportSQLiteQuery;
                    }
                }
                query.close();
                supportSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th3) {
                th22 = th3;
                roomSQLiteQuery = supportSQLiteQuery;
                th = th22;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th4) {
            th22 = th4;
            roomSQLiteQuery = acquire;
            th = th22;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public DownloadInfo getByFile(String str) {
        Throwable th;
        Throwable th2;
        RoomSQLiteQuery roomSQLiteQuery;
        DownloadDao_Impl downloadDao_Impl = this;
        String str2 = str;
        SupportSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM requests WHERE _file = ?", 1);
        if (str2 == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str2);
        }
        Cursor query = downloadDao_Impl.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ID);
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NAMESPACE);
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_URL);
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_FILE);
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_GROUP);
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_PRIORITY);
            int columnIndexOrThrow7 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_HEADERS);
            int columnIndexOrThrow8 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_DOWNLOADED);
            int columnIndexOrThrow9 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TOTAL);
            int columnIndexOrThrow10 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_STATUS);
            int columnIndexOrThrow11 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ERROR);
            int columnIndexOrThrow12 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NETWORK_TYPE);
            SupportSQLiteQuery supportSQLiteQuery = acquire;
            try {
                DownloadInfo downloadInfo;
                int columnIndexOrThrow13 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_CREATED);
                int columnIndexOrThrow14 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TAG);
                if (query.moveToFirst()) {
                    int i = columnIndexOrThrow14;
                    try {
                        downloadInfo = new DownloadInfo();
                        downloadInfo.setId(query.getInt(columnIndexOrThrow));
                        downloadInfo.setNamespace(query.getString(columnIndexOrThrow2));
                        downloadInfo.setUrl(query.getString(columnIndexOrThrow3));
                        downloadInfo.setFile(query.getString(columnIndexOrThrow4));
                        downloadInfo.setGroup(query.getInt(columnIndexOrThrow5));
                        downloadInfo.setPriority(downloadDao_Impl.__converter.fromPriorityValue(query.getInt(columnIndexOrThrow6)));
                        downloadInfo.setHeaders(downloadDao_Impl.__converter.fromHeaderString(query.getString(columnIndexOrThrow7)));
                        downloadInfo.setDownloaded(query.getLong(columnIndexOrThrow8));
                        downloadInfo.setTotal(query.getLong(columnIndexOrThrow9));
                        downloadInfo.setStatus(downloadDao_Impl.__converter.fromStatusValue(query.getInt(columnIndexOrThrow10)));
                        downloadInfo.setError(downloadDao_Impl.__converter.fromErrorValue(query.getInt(columnIndexOrThrow11)));
                        downloadInfo.setNetworkType(downloadDao_Impl.__converter.fromNetworkTypeValue(query.getInt(columnIndexOrThrow12)));
                        downloadInfo.setCreated(query.getLong(columnIndexOrThrow13));
                        downloadInfo.setTag(query.getString(i));
                    } catch (Throwable th22) {
                        th = th22;
                        roomSQLiteQuery = supportSQLiteQuery;
                        query.close();
                        roomSQLiteQuery.release();
                        throw th;
                    }
                }
                downloadInfo = null;
                query.close();
                supportSQLiteQuery.release();
                return downloadInfo;
            } catch (Throwable th3) {
                th22 = th3;
                roomSQLiteQuery = supportSQLiteQuery;
                th = th22;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th4) {
            th22 = th4;
            roomSQLiteQuery = acquire;
            th = th22;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public List<DownloadInfo> getByStatus(Status status) {
        Throwable th;
        Throwable th2;
        RoomSQLiteQuery roomSQLiteQuery;
        SupportSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM requests WHERE _status = ?", 1);
        acquire.bindLong(1, (long) this.__converter.toStatusValue(status));
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ID);
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NAMESPACE);
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_URL);
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_FILE);
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_GROUP);
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_PRIORITY);
            int columnIndexOrThrow7 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_HEADERS);
            int columnIndexOrThrow8 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_DOWNLOADED);
            int columnIndexOrThrow9 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TOTAL);
            int columnIndexOrThrow10 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_STATUS);
            int columnIndexOrThrow11 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ERROR);
            int columnIndexOrThrow12 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NETWORK_TYPE);
            SupportSQLiteQuery supportSQLiteQuery = acquire;
            try {
                int columnIndexOrThrow13 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_CREATED);
                int columnIndexOrThrow14 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TAG);
                int i = columnIndexOrThrow12;
                List<DownloadInfo> arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    try {
                        DownloadInfo downloadInfo = new DownloadInfo();
                        List<DownloadInfo> list = arrayList;
                        downloadInfo.setId(query.getInt(columnIndexOrThrow));
                        downloadInfo.setNamespace(query.getString(columnIndexOrThrow2));
                        downloadInfo.setUrl(query.getString(columnIndexOrThrow3));
                        downloadInfo.setFile(query.getString(columnIndexOrThrow4));
                        downloadInfo.setGroup(query.getInt(columnIndexOrThrow5));
                        int i2 = columnIndexOrThrow;
                        downloadInfo.setPriority(r1.__converter.fromPriorityValue(query.getInt(columnIndexOrThrow6)));
                        downloadInfo.setHeaders(r1.__converter.fromHeaderString(query.getString(columnIndexOrThrow7)));
                        int i3 = columnIndexOrThrow2;
                        downloadInfo.setDownloaded(query.getLong(columnIndexOrThrow8));
                        downloadInfo.setTotal(query.getLong(columnIndexOrThrow9));
                        downloadInfo.setStatus(r1.__converter.fromStatusValue(query.getInt(columnIndexOrThrow10)));
                        downloadInfo.setError(r1.__converter.fromErrorValue(query.getInt(columnIndexOrThrow11)));
                        int i4 = i;
                        downloadInfo.setNetworkType(r1.__converter.fromNetworkTypeValue(query.getInt(i4)));
                        int i5 = i4;
                        columnIndexOrThrow = columnIndexOrThrow13;
                        downloadInfo.setCreated(query.getLong(columnIndexOrThrow));
                        int i6 = columnIndexOrThrow14;
                        downloadInfo.setTag(query.getString(i6));
                        arrayList = list;
                        arrayList.add(downloadInfo);
                        columnIndexOrThrow14 = i6;
                        columnIndexOrThrow13 = columnIndexOrThrow;
                        columnIndexOrThrow = i2;
                        columnIndexOrThrow2 = i3;
                        i = i5;
                        DownloadDao_Impl downloadDao_Impl = this;
                    } catch (Throwable th22) {
                        th = th22;
                        roomSQLiteQuery = supportSQLiteQuery;
                    }
                }
                query.close();
                supportSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th3) {
                th22 = th3;
                roomSQLiteQuery = supportSQLiteQuery;
                th = th22;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th4) {
            th22 = th4;
            roomSQLiteQuery = acquire;
            th = th22;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public List<DownloadInfo> getByGroup(int i) {
        Throwable th;
        Throwable th2;
        RoomSQLiteQuery roomSQLiteQuery;
        SupportSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM requests WHERE _group = ?", 1);
        acquire.bindLong(1, (long) i);
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ID);
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NAMESPACE);
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_URL);
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_FILE);
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_GROUP);
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_PRIORITY);
            int columnIndexOrThrow7 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_HEADERS);
            int columnIndexOrThrow8 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_DOWNLOADED);
            int columnIndexOrThrow9 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TOTAL);
            int columnIndexOrThrow10 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_STATUS);
            int columnIndexOrThrow11 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ERROR);
            int columnIndexOrThrow12 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NETWORK_TYPE);
            SupportSQLiteQuery supportSQLiteQuery = acquire;
            try {
                int columnIndexOrThrow13 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_CREATED);
                int columnIndexOrThrow14 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TAG);
                int i2 = columnIndexOrThrow12;
                List<DownloadInfo> arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    try {
                        DownloadInfo downloadInfo = new DownloadInfo();
                        List<DownloadInfo> list = arrayList;
                        downloadInfo.setId(query.getInt(columnIndexOrThrow));
                        downloadInfo.setNamespace(query.getString(columnIndexOrThrow2));
                        downloadInfo.setUrl(query.getString(columnIndexOrThrow3));
                        downloadInfo.setFile(query.getString(columnIndexOrThrow4));
                        downloadInfo.setGroup(query.getInt(columnIndexOrThrow5));
                        int i3 = columnIndexOrThrow;
                        downloadInfo.setPriority(r1.__converter.fromPriorityValue(query.getInt(columnIndexOrThrow6)));
                        downloadInfo.setHeaders(r1.__converter.fromHeaderString(query.getString(columnIndexOrThrow7)));
                        int i4 = columnIndexOrThrow2;
                        downloadInfo.setDownloaded(query.getLong(columnIndexOrThrow8));
                        downloadInfo.setTotal(query.getLong(columnIndexOrThrow9));
                        downloadInfo.setStatus(r1.__converter.fromStatusValue(query.getInt(columnIndexOrThrow10)));
                        downloadInfo.setError(r1.__converter.fromErrorValue(query.getInt(columnIndexOrThrow11)));
                        int i5 = i2;
                        downloadInfo.setNetworkType(r1.__converter.fromNetworkTypeValue(query.getInt(i5)));
                        int i6 = i5;
                        columnIndexOrThrow = columnIndexOrThrow13;
                        downloadInfo.setCreated(query.getLong(columnIndexOrThrow));
                        int i7 = columnIndexOrThrow14;
                        downloadInfo.setTag(query.getString(i7));
                        arrayList = list;
                        arrayList.add(downloadInfo);
                        columnIndexOrThrow14 = i7;
                        columnIndexOrThrow13 = columnIndexOrThrow;
                        columnIndexOrThrow = i3;
                        columnIndexOrThrow2 = i4;
                        i2 = i6;
                        DownloadDao_Impl downloadDao_Impl = this;
                    } catch (Throwable th22) {
                        th = th22;
                        roomSQLiteQuery = supportSQLiteQuery;
                    }
                }
                query.close();
                supportSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th3) {
                th22 = th3;
                roomSQLiteQuery = supportSQLiteQuery;
                th = th22;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th4) {
            th22 = th4;
            roomSQLiteQuery = acquire;
            th = th22;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public List<DownloadInfo> getByGroupWithStatus(int i, Status status) {
        Throwable th;
        Throwable th2;
        RoomSQLiteQuery roomSQLiteQuery;
        SupportSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM requests WHERE _group = ? AND _status = ?", 2);
        acquire.bindLong(1, (long) i);
        acquire.bindLong(2, (long) this.__converter.toStatusValue(status));
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ID);
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NAMESPACE);
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_URL);
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_FILE);
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_GROUP);
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_PRIORITY);
            int columnIndexOrThrow7 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_HEADERS);
            int columnIndexOrThrow8 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_DOWNLOADED);
            int columnIndexOrThrow9 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TOTAL);
            int columnIndexOrThrow10 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_STATUS);
            int columnIndexOrThrow11 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ERROR);
            int columnIndexOrThrow12 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NETWORK_TYPE);
            SupportSQLiteQuery supportSQLiteQuery = acquire;
            try {
                int columnIndexOrThrow13 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_CREATED);
                int columnIndexOrThrow14 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TAG);
                int i2 = columnIndexOrThrow12;
                List<DownloadInfo> arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    try {
                        DownloadInfo downloadInfo = new DownloadInfo();
                        List<DownloadInfo> list = arrayList;
                        downloadInfo.setId(query.getInt(columnIndexOrThrow));
                        downloadInfo.setNamespace(query.getString(columnIndexOrThrow2));
                        downloadInfo.setUrl(query.getString(columnIndexOrThrow3));
                        downloadInfo.setFile(query.getString(columnIndexOrThrow4));
                        downloadInfo.setGroup(query.getInt(columnIndexOrThrow5));
                        int i3 = columnIndexOrThrow;
                        downloadInfo.setPriority(r1.__converter.fromPriorityValue(query.getInt(columnIndexOrThrow6)));
                        downloadInfo.setHeaders(r1.__converter.fromHeaderString(query.getString(columnIndexOrThrow7)));
                        int i4 = columnIndexOrThrow2;
                        downloadInfo.setDownloaded(query.getLong(columnIndexOrThrow8));
                        downloadInfo.setTotal(query.getLong(columnIndexOrThrow9));
                        downloadInfo.setStatus(r1.__converter.fromStatusValue(query.getInt(columnIndexOrThrow10)));
                        downloadInfo.setError(r1.__converter.fromErrorValue(query.getInt(columnIndexOrThrow11)));
                        int i5 = i2;
                        downloadInfo.setNetworkType(r1.__converter.fromNetworkTypeValue(query.getInt(i5)));
                        int i6 = i5;
                        columnIndexOrThrow = columnIndexOrThrow13;
                        downloadInfo.setCreated(query.getLong(columnIndexOrThrow));
                        int i7 = columnIndexOrThrow14;
                        downloadInfo.setTag(query.getString(i7));
                        arrayList = list;
                        arrayList.add(downloadInfo);
                        columnIndexOrThrow14 = i7;
                        columnIndexOrThrow13 = columnIndexOrThrow;
                        columnIndexOrThrow = i3;
                        columnIndexOrThrow2 = i4;
                        i2 = i6;
                        DownloadDao_Impl downloadDao_Impl = this;
                    } catch (Throwable th22) {
                        th = th22;
                        roomSQLiteQuery = supportSQLiteQuery;
                    }
                }
                query.close();
                supportSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th3) {
                th22 = th3;
                roomSQLiteQuery = supportSQLiteQuery;
                th = th22;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th4) {
            th22 = th4;
            roomSQLiteQuery = acquire;
            th = th22;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public List<DownloadInfo> getPendingDownloadsSorted(Status status) {
        Throwable th;
        Throwable th2;
        RoomSQLiteQuery roomSQLiteQuery;
        SupportSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM requests WHERE _status = ? ORDER BY _priority DESC, _created ASC", 1);
        acquire.bindLong(1, (long) this.__converter.toStatusValue(status));
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ID);
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NAMESPACE);
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_URL);
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_FILE);
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_GROUP);
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_PRIORITY);
            int columnIndexOrThrow7 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_HEADERS);
            int columnIndexOrThrow8 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_DOWNLOADED);
            int columnIndexOrThrow9 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TOTAL);
            int columnIndexOrThrow10 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_STATUS);
            int columnIndexOrThrow11 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_ERROR);
            int columnIndexOrThrow12 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_NETWORK_TYPE);
            SupportSQLiteQuery supportSQLiteQuery = acquire;
            try {
                int columnIndexOrThrow13 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_CREATED);
                int columnIndexOrThrow14 = query.getColumnIndexOrThrow(DownloadDatabase.COLUMN_TAG);
                int i = columnIndexOrThrow12;
                List<DownloadInfo> arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    try {
                        DownloadInfo downloadInfo = new DownloadInfo();
                        List<DownloadInfo> list = arrayList;
                        downloadInfo.setId(query.getInt(columnIndexOrThrow));
                        downloadInfo.setNamespace(query.getString(columnIndexOrThrow2));
                        downloadInfo.setUrl(query.getString(columnIndexOrThrow3));
                        downloadInfo.setFile(query.getString(columnIndexOrThrow4));
                        downloadInfo.setGroup(query.getInt(columnIndexOrThrow5));
                        int i2 = columnIndexOrThrow;
                        downloadInfo.setPriority(r1.__converter.fromPriorityValue(query.getInt(columnIndexOrThrow6)));
                        downloadInfo.setHeaders(r1.__converter.fromHeaderString(query.getString(columnIndexOrThrow7)));
                        int i3 = columnIndexOrThrow2;
                        downloadInfo.setDownloaded(query.getLong(columnIndexOrThrow8));
                        downloadInfo.setTotal(query.getLong(columnIndexOrThrow9));
                        downloadInfo.setStatus(r1.__converter.fromStatusValue(query.getInt(columnIndexOrThrow10)));
                        downloadInfo.setError(r1.__converter.fromErrorValue(query.getInt(columnIndexOrThrow11)));
                        int i4 = i;
                        downloadInfo.setNetworkType(r1.__converter.fromNetworkTypeValue(query.getInt(i4)));
                        int i5 = i4;
                        columnIndexOrThrow = columnIndexOrThrow13;
                        downloadInfo.setCreated(query.getLong(columnIndexOrThrow));
                        int i6 = columnIndexOrThrow14;
                        downloadInfo.setTag(query.getString(i6));
                        arrayList = list;
                        arrayList.add(downloadInfo);
                        columnIndexOrThrow14 = i6;
                        columnIndexOrThrow13 = columnIndexOrThrow;
                        columnIndexOrThrow = i2;
                        columnIndexOrThrow2 = i3;
                        i = i5;
                        DownloadDao_Impl downloadDao_Impl = this;
                    } catch (Throwable th22) {
                        th = th22;
                        roomSQLiteQuery = supportSQLiteQuery;
                    }
                }
                query.close();
                supportSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th3) {
                th22 = th3;
                roomSQLiteQuery = supportSQLiteQuery;
                th = th22;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th4) {
            th22 = th4;
            roomSQLiteQuery = acquire;
            th = th22;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }
}
