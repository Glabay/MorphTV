package okhttp3.internal.cache;

import java.io.IOException;
import okio.Sink;

class DiskLruCache$2 extends FaultHidingSink {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    final /* synthetic */ DiskLruCache this$0;

    static {
        Class cls = DiskLruCache.class;
    }

    DiskLruCache$2(DiskLruCache diskLruCache, Sink sink) {
        this.this$0 = diskLruCache;
        super(sink);
    }

    protected void onException(IOException iOException) {
        this.this$0.hasJournalErrors = true;
    }
}
