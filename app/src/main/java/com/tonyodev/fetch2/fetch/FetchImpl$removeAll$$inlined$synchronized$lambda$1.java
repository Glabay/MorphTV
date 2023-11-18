package com.tonyodev.fetch2.fetch;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.exception.FetchException;
import java.util.List;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"}, d2 = {"<anonymous>", "", "run", "com/tonyodev/fetch2/fetch/FetchImpl$removeAll$1$1"}, k = 3, mv = {1, 1, 10})
/* compiled from: FetchImpl.kt */
final class FetchImpl$removeAll$$inlined$synchronized$lambda$1 implements Runnable {
    final /* synthetic */ FetchImpl this$0;

    FetchImpl$removeAll$$inlined$synchronized$lambda$1(FetchImpl fetchImpl) {
        this.this$0 = fetchImpl;
    }

    public final void run() {
        try {
            final List removeAll = this.this$0.getFetchHandler().removeAll();
            this.this$0.getUiHandler().post(new Runnable() {
                public final void run() {
                    for (Download download : removeAll) {
                        Logger logger = this.this$0.getLogger();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Removed download ");
                        stringBuilder.append(download);
                        logger.mo4160d(stringBuilder.toString());
                        this.this$0.getFetchListenerProvider().getMainListener().onRemoved(download);
                    }
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
