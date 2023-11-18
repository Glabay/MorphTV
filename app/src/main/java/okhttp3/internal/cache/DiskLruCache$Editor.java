package okhttp3.internal.cache;

import java.io.IOException;

public final class DiskLruCache$Editor {
    private boolean done;
    final DiskLruCache$Entry entry;
    final /* synthetic */ DiskLruCache this$0;
    final boolean[] written;

    DiskLruCache$Editor(DiskLruCache diskLruCache, DiskLruCache$Entry diskLruCache$Entry) {
        this.this$0 = diskLruCache;
        this.entry = diskLruCache$Entry;
        this.written = diskLruCache$Entry.readable != null ? null : new boolean[diskLruCache.valueCount];
    }

    void detach() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r3 = this;
        r0 = r3.entry;
        r0 = r0.currentEditor;
        if (r0 != r3) goto L_0x0022;
    L_0x0006:
        r0 = 0;
    L_0x0007:
        r1 = r3.this$0;
        r1 = r1.valueCount;
        if (r0 >= r1) goto L_0x001d;
    L_0x000d:
        r1 = r3.this$0;	 Catch:{ IOException -> 0x001a }
        r1 = r1.fileSystem;	 Catch:{ IOException -> 0x001a }
        r2 = r3.entry;	 Catch:{ IOException -> 0x001a }
        r2 = r2.dirtyFiles;	 Catch:{ IOException -> 0x001a }
        r2 = r2[r0];	 Catch:{ IOException -> 0x001a }
        r1.delete(r2);	 Catch:{ IOException -> 0x001a }
    L_0x001a:
        r0 = r0 + 1;
        goto L_0x0007;
    L_0x001d:
        r0 = r3.entry;
        r1 = 0;
        r0.currentEditor = r1;
    L_0x0022:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache$Editor.detach():void");
    }

    public okio.Source newSource(int r5) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r4 = this;
        r0 = r4.this$0;
        monitor-enter(r0);
        r1 = r4.done;	 Catch:{ all -> 0x002f }
        if (r1 == 0) goto L_0x000d;	 Catch:{ all -> 0x002f }
    L_0x0007:
        r5 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x002f }
        r5.<init>();	 Catch:{ all -> 0x002f }
        throw r5;	 Catch:{ all -> 0x002f }
    L_0x000d:
        r1 = r4.entry;	 Catch:{ all -> 0x002f }
        r1 = r1.readable;	 Catch:{ all -> 0x002f }
        r2 = 0;	 Catch:{ all -> 0x002f }
        if (r1 == 0) goto L_0x002d;	 Catch:{ all -> 0x002f }
    L_0x0014:
        r1 = r4.entry;	 Catch:{ all -> 0x002f }
        r1 = r1.currentEditor;	 Catch:{ all -> 0x002f }
        if (r1 == r4) goto L_0x001b;
    L_0x001a:
        goto L_0x002d;
    L_0x001b:
        r1 = r4.this$0;	 Catch:{ FileNotFoundException -> 0x002b }
        r1 = r1.fileSystem;	 Catch:{ FileNotFoundException -> 0x002b }
        r3 = r4.entry;	 Catch:{ FileNotFoundException -> 0x002b }
        r3 = r3.cleanFiles;	 Catch:{ FileNotFoundException -> 0x002b }
        r5 = r3[r5];	 Catch:{ FileNotFoundException -> 0x002b }
        r5 = r1.source(r5);	 Catch:{ FileNotFoundException -> 0x002b }
        monitor-exit(r0);	 Catch:{ all -> 0x002f }
        return r5;	 Catch:{ all -> 0x002f }
    L_0x002b:
        monitor-exit(r0);	 Catch:{ all -> 0x002f }
        return r2;	 Catch:{ all -> 0x002f }
    L_0x002d:
        monitor-exit(r0);	 Catch:{ all -> 0x002f }
        return r2;	 Catch:{ all -> 0x002f }
    L_0x002f:
        r5 = move-exception;	 Catch:{ all -> 0x002f }
        monitor-exit(r0);	 Catch:{ all -> 0x002f }
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache$Editor.newSource(int):okio.Source");
    }

    public okio.Sink newSink(int r4) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r3 = this;
        r0 = r3.this$0;
        monitor-enter(r0);
        r1 = r3.done;	 Catch:{ all -> 0x003f }
        if (r1 == 0) goto L_0x000d;	 Catch:{ all -> 0x003f }
    L_0x0007:
        r4 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x003f }
        r4.<init>();	 Catch:{ all -> 0x003f }
        throw r4;	 Catch:{ all -> 0x003f }
    L_0x000d:
        r1 = r3.entry;	 Catch:{ all -> 0x003f }
        r1 = r1.currentEditor;	 Catch:{ all -> 0x003f }
        if (r1 == r3) goto L_0x0019;	 Catch:{ all -> 0x003f }
    L_0x0013:
        r4 = okio.Okio.blackhole();	 Catch:{ all -> 0x003f }
        monitor-exit(r0);	 Catch:{ all -> 0x003f }
        return r4;	 Catch:{ all -> 0x003f }
    L_0x0019:
        r1 = r3.entry;	 Catch:{ all -> 0x003f }
        r1 = r1.readable;	 Catch:{ all -> 0x003f }
        if (r1 != 0) goto L_0x0024;	 Catch:{ all -> 0x003f }
    L_0x001f:
        r1 = r3.written;	 Catch:{ all -> 0x003f }
        r2 = 1;	 Catch:{ all -> 0x003f }
        r1[r4] = r2;	 Catch:{ all -> 0x003f }
    L_0x0024:
        r1 = r3.entry;	 Catch:{ all -> 0x003f }
        r1 = r1.dirtyFiles;	 Catch:{ all -> 0x003f }
        r4 = r1[r4];	 Catch:{ all -> 0x003f }
        r1 = r3.this$0;	 Catch:{ FileNotFoundException -> 0x0039 }
        r1 = r1.fileSystem;	 Catch:{ FileNotFoundException -> 0x0039 }
        r4 = r1.sink(r4);	 Catch:{ FileNotFoundException -> 0x0039 }
        r1 = new okhttp3.internal.cache.DiskLruCache$Editor$1;	 Catch:{ all -> 0x003f }
        r1.<init>(r4);	 Catch:{ all -> 0x003f }
        monitor-exit(r0);	 Catch:{ all -> 0x003f }
        return r1;	 Catch:{ all -> 0x003f }
    L_0x0039:
        r4 = okio.Okio.blackhole();	 Catch:{ all -> 0x003f }
        monitor-exit(r0);	 Catch:{ all -> 0x003f }
        return r4;	 Catch:{ all -> 0x003f }
    L_0x003f:
        r4 = move-exception;	 Catch:{ all -> 0x003f }
        monitor-exit(r0);	 Catch:{ all -> 0x003f }
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache$Editor.newSink(int):okio.Sink");
    }

    public void commit() throws IOException {
        synchronized (this.this$0) {
            if (this.done) {
                throw new IllegalStateException();
            }
            if (this.entry.currentEditor == this) {
                this.this$0.completeEdit(this, true);
            }
            this.done = true;
        }
    }

    public void abort() throws IOException {
        synchronized (this.this$0) {
            if (this.done) {
                throw new IllegalStateException();
            }
            if (this.entry.currentEditor == this) {
                this.this$0.completeEdit(this, false);
            }
            this.done = true;
        }
    }

    public void abortUnlessCommitted() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r3 = this;
        r0 = r3.this$0;
        monitor-enter(r0);
        r1 = r3.done;	 Catch:{ all -> 0x0015 }
        if (r1 != 0) goto L_0x0013;	 Catch:{ all -> 0x0015 }
    L_0x0007:
        r1 = r3.entry;	 Catch:{ all -> 0x0015 }
        r1 = r1.currentEditor;	 Catch:{ all -> 0x0015 }
        if (r1 != r3) goto L_0x0013;
    L_0x000d:
        r1 = r3.this$0;	 Catch:{ IOException -> 0x0013 }
        r2 = 0;	 Catch:{ IOException -> 0x0013 }
        r1.completeEdit(r3, r2);	 Catch:{ IOException -> 0x0013 }
    L_0x0013:
        monitor-exit(r0);	 Catch:{ all -> 0x0015 }
        return;	 Catch:{ all -> 0x0015 }
    L_0x0015:
        r1 = move-exception;	 Catch:{ all -> 0x0015 }
        monitor-exit(r0);	 Catch:{ all -> 0x0015 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache$Editor.abortUnlessCommitted():void");
    }
}
