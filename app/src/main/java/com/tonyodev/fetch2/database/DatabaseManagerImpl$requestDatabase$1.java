package com.tonyodev.fetch2.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase.Builder;
import android.content.Context;
import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.database.migration.Migration;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "Lcom/tonyodev/fetch2/database/DownloadDatabase;", "invoke"}, k = 3, mv = {1, 1, 10})
/* compiled from: DatabaseManagerImpl.kt */
final class DatabaseManagerImpl$requestDatabase$1 extends Lambda implements Function0<DownloadDatabase> {
    final /* synthetic */ Context $context;
    final /* synthetic */ Migration[] $migrations;
    final /* synthetic */ DatabaseManagerImpl this$0;

    DatabaseManagerImpl$requestDatabase$1(DatabaseManagerImpl databaseManagerImpl, Context context, Migration[] migrationArr) {
        this.this$0 = databaseManagerImpl;
        this.$context = context;
        this.$migrations = migrationArr;
        super(0);
    }

    @NotNull
    public final DownloadDatabase invoke() {
        Builder inMemoryDatabaseBuilder;
        Logger logger;
        StringBuilder stringBuilder;
        if (this.this$0.isMemoryDatabase()) {
            logger = this.this$0.getLogger();
            stringBuilder = new StringBuilder();
            stringBuilder.append("Init in memory database named ");
            stringBuilder.append(this.this$0.namespace);
            logger.mo4160d(stringBuilder.toString());
            inMemoryDatabaseBuilder = Room.inMemoryDatabaseBuilder(this.$context, DownloadDatabase.class);
        } else {
            logger = this.this$0.getLogger();
            stringBuilder = new StringBuilder();
            stringBuilder.append("Init file based database named ");
            stringBuilder.append(this.this$0.namespace);
            stringBuilder.append(".db");
            logger.mo4160d(stringBuilder.toString());
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("");
            stringBuilder2.append(this.this$0.namespace);
            stringBuilder2.append(".db");
            inMemoryDatabaseBuilder = Room.databaseBuilder(this.$context, DownloadDatabase.class, stringBuilder2.toString());
        }
        android.arch.persistence.room.migration.Migration[] migrationArr = (android.arch.persistence.room.migration.Migration[]) this.$migrations;
        inMemoryDatabaseBuilder.addMigrations((android.arch.persistence.room.migration.Migration[]) Arrays.copyOf(migrationArr, migrationArr.length));
        return (DownloadDatabase) inMemoryDatabaseBuilder.build();
    }
}
