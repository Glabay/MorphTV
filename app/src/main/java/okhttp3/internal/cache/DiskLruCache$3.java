package okhttp3.internal.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import okhttp3.internal.cache.DiskLruCache.Snapshot;

class DiskLruCache$3 implements Iterator<Snapshot> {
    final Iterator<DiskLruCache$Entry> delegate = new ArrayList(this.this$0.lruEntries.values()).iterator();
    Snapshot nextSnapshot;
    Snapshot removeSnapshot;
    final /* synthetic */ DiskLruCache this$0;

    DiskLruCache$3(DiskLruCache diskLruCache) {
        this.this$0 = diskLruCache;
    }

    public boolean hasNext() {
        if (this.nextSnapshot != null) {
            return true;
        }
        synchronized (this.this$0) {
            if (this.this$0.closed) {
                return false;
            }
            while (this.delegate.hasNext()) {
                Snapshot snapshot = ((DiskLruCache$Entry) this.delegate.next()).snapshot();
                if (snapshot != null) {
                    this.nextSnapshot = snapshot;
                    return true;
                }
            }
            return false;
        }
    }

    public Snapshot next() {
        if (hasNext()) {
            this.removeSnapshot = this.nextSnapshot;
            this.nextSnapshot = null;
            return this.removeSnapshot;
        }
        throw new NoSuchElementException();
    }

    public void remove() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r3 = this;
        r0 = r3.removeSnapshot;
        if (r0 != 0) goto L_0x000c;
    L_0x0004:
        r0 = new java.lang.IllegalStateException;
        r1 = "remove() before next()";
        r0.<init>(r1);
        throw r0;
    L_0x000c:
        r0 = 0;
        r1 = r3.this$0;	 Catch:{ IOException -> 0x001d, all -> 0x0019 }
        r2 = r3.removeSnapshot;	 Catch:{ IOException -> 0x001d, all -> 0x0019 }
        r2 = okhttp3.internal.cache.DiskLruCache.Snapshot.access$000(r2);	 Catch:{ IOException -> 0x001d, all -> 0x0019 }
        r1.remove(r2);	 Catch:{ IOException -> 0x001d, all -> 0x0019 }
        goto L_0x001d;
    L_0x0019:
        r1 = move-exception;
        r3.removeSnapshot = r0;
        throw r1;
    L_0x001d:
        r3.removeSnapshot = r0;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache$3.remove():void");
    }
}
