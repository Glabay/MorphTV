package com.tonyodev.fetch2.fetch;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Func2;
import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.exception.FetchException;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"}, d2 = {"<anonymous>", "", "run", "com/tonyodev/fetch2/fetch/FetchImpl$getDownload$1$1"}, k = 3, mv = {1, 1, 10})
/* compiled from: FetchImpl.kt */
final class FetchImpl$getDownload$$inlined$synchronized$lambda$1 implements Runnable {
    final /* synthetic */ Func2 $func$inlined;
    final /* synthetic */ int $id$inlined;
    final /* synthetic */ FetchImpl this$0;

    FetchImpl$getDownload$$inlined$synchronized$lambda$1(FetchImpl fetchImpl, int i, Func2 func2) {
        this.this$0 = fetchImpl;
        this.$id$inlined = i;
        this.$func$inlined = func2;
    }

    public final void run() {
        try {
            final Download download = this.this$0.getFetchHandler().getDownload(this.$id$inlined);
            this.this$0.getUiHandler().post(new Runnable() {
                public final void run() {
                    this.$func$inlined.call(download);
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
