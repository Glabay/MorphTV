package okio;

final class AsyncTimeout$Watchdog extends Thread {
    AsyncTimeout$Watchdog() {
        super("Okio Watchdog");
        setDaemon(true);
    }

    public void run() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r3 = this;
    L_0x0000:
        r0 = okio.AsyncTimeout.class;	 Catch:{ InterruptedException -> 0x0000 }
        monitor-enter(r0);	 Catch:{ InterruptedException -> 0x0000 }
        r1 = okio.AsyncTimeout.awaitTimeout();	 Catch:{ all -> 0x0019 }
        if (r1 != 0) goto L_0x000b;	 Catch:{ all -> 0x0019 }
    L_0x0009:
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        goto L_0x0000;	 Catch:{ all -> 0x0019 }
    L_0x000b:
        r2 = okio.AsyncTimeout.head;	 Catch:{ all -> 0x0019 }
        if (r1 != r2) goto L_0x0014;	 Catch:{ all -> 0x0019 }
    L_0x000f:
        r1 = 0;	 Catch:{ all -> 0x0019 }
        okio.AsyncTimeout.head = r1;	 Catch:{ all -> 0x0019 }
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        return;	 Catch:{ all -> 0x0019 }
    L_0x0014:
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        r1.timedOut();	 Catch:{ InterruptedException -> 0x0000 }
        goto L_0x0000;
    L_0x0019:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        throw r1;	 Catch:{ InterruptedException -> 0x0000 }
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.AsyncTimeout$Watchdog.run():void");
    }
}
