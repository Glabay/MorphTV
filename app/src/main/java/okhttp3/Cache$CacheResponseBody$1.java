package okhttp3;

import java.io.IOException;
import okhttp3.Cache.CacheResponseBody;
import okhttp3.internal.cache.DiskLruCache.Snapshot;
import okio.ForwardingSource;
import okio.Source;

class Cache$CacheResponseBody$1 extends ForwardingSource {
    final /* synthetic */ CacheResponseBody this$0;
    final /* synthetic */ Snapshot val$snapshot;

    Cache$CacheResponseBody$1(CacheResponseBody cacheResponseBody, Source source, Snapshot snapshot) {
        this.this$0 = cacheResponseBody;
        this.val$snapshot = snapshot;
        super(source);
    }

    public void close() throws IOException {
        this.val$snapshot.close();
        super.close();
    }
}
