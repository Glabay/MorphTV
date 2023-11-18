package com.tonyodev.fetch2.helper;

import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.NetworkType;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 10})
/* compiled from: PriorityListProcessorImpl.kt */
final class PriorityListProcessorImpl$priorityIteratorRunnable$1 implements Runnable {
    final /* synthetic */ PriorityListProcessorImpl this$0;

    PriorityListProcessorImpl$priorityIteratorRunnable$1(PriorityListProcessorImpl priorityListProcessorImpl) {
        this.this$0 = priorityListProcessorImpl;
    }

    public final void run() {
        if (this.this$0.canContinueToProcess()) {
            if (this.this$0.networkInfoProvider.isNetworkAvailable() && this.this$0.downloadManager.canAccommodateNewDownload()) {
                List priorityList = this.this$0.getPriorityList();
                int i = 0;
                int lastIndex = CollectionsKt.getLastIndex(priorityList);
                if (lastIndex >= 0) {
                    while (this.this$0.canContinueToProcess()) {
                        NetworkType globalNetworkType;
                        Download download = (Download) priorityList.get(i);
                        if (this.this$0.getGlobalNetworkType() != NetworkType.GLOBAL_OFF) {
                            globalNetworkType = this.this$0.getGlobalNetworkType();
                        } else if (download.getNetworkType() == NetworkType.GLOBAL_OFF) {
                            globalNetworkType = NetworkType.ALL;
                        } else {
                            globalNetworkType = download.getNetworkType();
                        }
                        if (this.this$0.networkInfoProvider.isOnAllowedNetwork(globalNetworkType) && this.this$0.canContinueToProcess() && !this.this$0.downloadManager.contains(download.getId())) {
                            this.this$0.downloadManager.start(download);
                        }
                        if (i == lastIndex) {
                            break;
                        }
                        i++;
                    }
                }
            }
            if (this.this$0.canContinueToProcess()) {
                this.this$0.registerPriorityIterator();
            }
        }
    }
}
