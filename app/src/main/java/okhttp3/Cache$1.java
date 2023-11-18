package okhttp3;

import java.io.IOException;
import okhttp3.internal.cache.CacheRequest;
import okhttp3.internal.cache.CacheStrategy;
import okhttp3.internal.cache.InternalCache;

class Cache$1 implements InternalCache {
    final /* synthetic */ Cache this$0;

    Cache$1(Cache cache) {
        this.this$0 = cache;
    }

    public Response get(Request request) throws IOException {
        return this.this$0.get(request);
    }

    public CacheRequest put(Response response) throws IOException {
        return this.this$0.put(response);
    }

    public void remove(Request request) throws IOException {
        this.this$0.remove(request);
    }

    public void update(Response response, Response response2) {
        this.this$0.update(response, response2);
    }

    public void trackConditionalCacheHit() {
        this.this$0.trackConditionalCacheHit();
    }

    public void trackResponse(CacheStrategy cacheStrategy) {
        this.this$0.trackResponse(cacheStrategy);
    }
}
