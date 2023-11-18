package okhttp3;

class ConnectionPool$1 implements Runnable {
    final /* synthetic */ ConnectionPool this$0;

    ConnectionPool$1(ConnectionPool connectionPool) {
        this.this$0 = connectionPool;
    }

    public void run() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r8 = this;
    L_0x0000:
        r0 = r8.this$0;
        r1 = java.lang.System.nanoTime();
        r0 = r0.cleanup(r1);
        r2 = -1;
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r4 != 0) goto L_0x0011;
    L_0x0010:
        return;
    L_0x0011:
        r2 = 0;
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r4 <= 0) goto L_0x0000;
    L_0x0017:
        r2 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r4 = r0 / r2;
        r2 = r2 * r4;
        r6 = r0 - r2;
        r0 = r8.this$0;
        monitor-enter(r0);
        r1 = r8.this$0;	 Catch:{ InterruptedException -> 0x002c }
        r2 = (int) r6;	 Catch:{ InterruptedException -> 0x002c }
        r1.wait(r4, r2);	 Catch:{ InterruptedException -> 0x002c }
        goto L_0x002c;
    L_0x002a:
        r1 = move-exception;
        goto L_0x002e;
    L_0x002c:
        monitor-exit(r0);	 Catch:{ all -> 0x002a }
        goto L_0x0000;	 Catch:{ all -> 0x002a }
    L_0x002e:
        monitor-exit(r0);	 Catch:{ all -> 0x002a }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.ConnectionPool$1.run():void");
    }
}
