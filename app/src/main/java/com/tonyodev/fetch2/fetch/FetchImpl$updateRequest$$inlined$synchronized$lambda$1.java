package com.tonyodev.fetch2.fetch;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.FetchErrorUtils;
import com.tonyodev.fetch2.Func;
import com.tonyodev.fetch2.Logger;
import com.tonyodev.fetch2.RequestInfo;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"}, d2 = {"<anonymous>", "", "run", "com/tonyodev/fetch2/fetch/FetchImpl$updateRequest$1$1"}, k = 3, mv = {1, 1, 10})
/* compiled from: FetchImpl.kt */
final class FetchImpl$updateRequest$$inlined$synchronized$lambda$1 implements Runnable {
    final /* synthetic */ Func $func$inlined;
    final /* synthetic */ Func $func2$inlined;
    final /* synthetic */ int $id$inlined;
    final /* synthetic */ RequestInfo $requestInfo$inlined;
    final /* synthetic */ FetchImpl this$0;

    FetchImpl$updateRequest$$inlined$synchronized$lambda$1(FetchImpl fetchImpl, int i, RequestInfo requestInfo, Func func, Func func2) {
        this.this$0 = fetchImpl;
        this.$id$inlined = i;
        this.$requestInfo$inlined = requestInfo;
        this.$func$inlined = func;
        this.$func2$inlined = func2;
    }

    public final void run() {
        try {
            final Download updateRequest = this.this$0.getFetchHandler().updateRequest(this.$id$inlined, this.$requestInfo$inlined);
            if (!(updateRequest == null || this.$func$inlined == null)) {
                this.this$0.getUiHandler().post(new Runnable() {
                    public final void run() {
                        this.$func$inlined.call(updateRequest);
                    }
                });
            }
            if (updateRequest == null && this.$func2$inlined != null) {
                this.this$0.getUiHandler().post(new Runnable() {
                    public final void run() {
                        this.$func2$inlined.call(Error.DOWNLOAD_NOT_FOUND);
                    }
                });
            }
        } catch (Exception e) {
            Logger logger = this.this$0.getLogger();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to update request with id ");
            stringBuilder.append(this.$id$inlined);
            logger.mo4163e(stringBuilder.toString(), e);
            final Error errorFromMessage = FetchErrorUtils.getErrorFromMessage(e.getMessage());
            if (this.$func2$inlined != null) {
                this.this$0.getUiHandler().post(new Runnable() {
                    public final void run() {
                        this.$func2$inlined.call(errorFromMessage);
                    }
                });
            }
        }
    }
}
