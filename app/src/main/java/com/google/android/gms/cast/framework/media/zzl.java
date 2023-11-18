package com.google.android.gms.cast.framework.media;

import android.util.LruCache;
import com.google.android.gms.cast.MediaQueueItem;

final class zzl extends LruCache<Integer, MediaQueueItem> {
    private final /* synthetic */ MediaQueue zzls;

    zzl(MediaQueue mediaQueue, int i) {
        this.zzls = mediaQueue;
        super(i);
    }

    protected final /* synthetic */ void entryRemoved(boolean z, Object obj, Object obj2, Object obj3) {
        Integer num = (Integer) obj;
        if (z) {
            this.zzls.zzlh.add(num);
        }
    }
}
