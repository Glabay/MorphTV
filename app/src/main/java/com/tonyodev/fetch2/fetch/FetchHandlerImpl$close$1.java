package com.tonyodev.fetch2.fetch;

import android.os.Build.VERSION;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 10})
/* compiled from: FetchHandlerImpl.kt */
final class FetchHandlerImpl$close$1 implements Runnable {
    final /* synthetic */ FetchHandlerImpl this$0;

    FetchHandlerImpl$close$1(FetchHandlerImpl fetchHandlerImpl) {
        this.this$0 = fetchHandlerImpl;
    }

    public final void run() {
        try {
            this.this$0.downloadManager.close();
            this.this$0.databaseManager.close();
            if (VERSION.SDK_INT >= 18) {
                this.this$0.handler.getLooper().quitSafely();
            } else {
                this.this$0.handler.getLooper().quit();
            }
        } catch (Exception e) {
            this.this$0.logger.mo4163e("FetchHandler", e);
        }
    }
}
