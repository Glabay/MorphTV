package com.tonyodev.fetch2.fetch;

import com.tonyodev.fetch2.Func;
import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.Status;
import com.tonyodev.fetch2.exception.FetchException;
import java.util.List;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"}, d2 = {"<anonymous>", "", "run", "com/tonyodev/fetch2/fetch/FetchImpl$getDownloadsWithStatus$1$1"}, k = 3, mv = {1, 1, 10})
/* compiled from: FetchImpl.kt */
final class FetchImpl$getDownloadsWithStatus$$inlined$synchronized$lambda$1 implements Runnable {
    final /* synthetic */ Func $func$inlined;
    final /* synthetic */ Status $status$inlined;
    final /* synthetic */ FetchImpl this$0;

    FetchImpl$getDownloadsWithStatus$$inlined$synchronized$lambda$1(FetchImpl fetchImpl, Status status, Func func) {
        this.this$0 = fetchImpl;
        this.$status$inlined = status;
        this.$func$inlined = func;
    }

    public final void run() {
        try {
            final List downloadsWithStatus = this.this$0.getFetchHandler().getDownloadsWithStatus(this.$status$inlined);
            this.this$0.getUiHandler().post(new Runnable() {
                public final void run() {
                    this.$func$inlined.call(downloadsWithStatus);
                }
            });
        } catch (FetchException e) {
            Logger logger = this.this$0.getLogger();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fetch with namespace ");
            stringBuilder.append(this.this$0.getNamespace());
            stringBuilder.append(" error");
            logger.mo4163e(stringBuilder.toString(), e);
        }
    }
}
