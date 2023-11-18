package com.tonyodev.fetch2.database.migration;

import android.arch.persistence.db.SupportSQLiteDatabase;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u0007"}, d2 = {"Lcom/tonyodev/fetch2/database/migration/MigrationOneToTwo;", "Lcom/tonyodev/fetch2/database/migration/Migration;", "()V", "migrate", "", "database", "Landroid/arch/persistence/db/SupportSQLiteDatabase;", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: MigrationOneToTwo.kt */
public final class MigrationOneToTwo extends Migration {
    public MigrationOneToTwo() {
        super(1, 2);
    }

    public void migrate(@NotNull SupportSQLiteDatabase supportSQLiteDatabase) {
        Intrinsics.checkParameterIsNotNull(supportSQLiteDatabase, "database");
        supportSQLiteDatabase.execSQL("ALTER TABLE requests  ADD COLUMN _tag TEXT");
    }
}
