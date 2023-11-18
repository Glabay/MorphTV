package okhttp3.internal.cache;

class DiskLruCache$1 implements Runnable {
    final /* synthetic */ DiskLruCache this$0;

    DiskLruCache$1(DiskLruCache diskLruCache) {
        this.this$0 = diskLruCache;
    }

    public void run() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r4 = this;
        r0 = r4.this$0;
        monitor-enter(r0);
        r1 = r4.this$0;	 Catch:{ all -> 0x0041 }
        r1 = r1.initialized;	 Catch:{ all -> 0x0041 }
        r2 = 1;	 Catch:{ all -> 0x0041 }
        r1 = r1 ^ r2;	 Catch:{ all -> 0x0041 }
        r3 = r4.this$0;	 Catch:{ all -> 0x0041 }
        r3 = r3.closed;	 Catch:{ all -> 0x0041 }
        r1 = r1 | r3;	 Catch:{ all -> 0x0041 }
        if (r1 == 0) goto L_0x0012;	 Catch:{ all -> 0x0041 }
    L_0x0010:
        monitor-exit(r0);	 Catch:{ all -> 0x0041 }
        return;
    L_0x0012:
        r1 = r4.this$0;	 Catch:{ IOException -> 0x0018 }
        r1.trimToSize();	 Catch:{ IOException -> 0x0018 }
        goto L_0x001c;
    L_0x0018:
        r1 = r4.this$0;	 Catch:{ all -> 0x0041 }
        r1.mostRecentTrimFailed = r2;	 Catch:{ all -> 0x0041 }
    L_0x001c:
        r1 = r4.this$0;	 Catch:{ IOException -> 0x002f }
        r1 = r1.journalRebuildRequired();	 Catch:{ IOException -> 0x002f }
        if (r1 == 0) goto L_0x003f;	 Catch:{ IOException -> 0x002f }
    L_0x0024:
        r1 = r4.this$0;	 Catch:{ IOException -> 0x002f }
        r1.rebuildJournal();	 Catch:{ IOException -> 0x002f }
        r1 = r4.this$0;	 Catch:{ IOException -> 0x002f }
        r3 = 0;	 Catch:{ IOException -> 0x002f }
        r1.redundantOpCount = r3;	 Catch:{ IOException -> 0x002f }
        goto L_0x003f;
    L_0x002f:
        r1 = r4.this$0;	 Catch:{ all -> 0x0041 }
        r1.mostRecentRebuildFailed = r2;	 Catch:{ all -> 0x0041 }
        r1 = r4.this$0;	 Catch:{ all -> 0x0041 }
        r2 = okio.Okio.blackhole();	 Catch:{ all -> 0x0041 }
        r2 = okio.Okio.buffer(r2);	 Catch:{ all -> 0x0041 }
        r1.journalWriter = r2;	 Catch:{ all -> 0x0041 }
    L_0x003f:
        monitor-exit(r0);	 Catch:{ all -> 0x0041 }
        return;	 Catch:{ all -> 0x0041 }
    L_0x0041:
        r1 = move-exception;	 Catch:{ all -> 0x0041 }
        monitor-exit(r0);	 Catch:{ all -> 0x0041 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.cache.DiskLruCache$1.run():void");
    }
}
