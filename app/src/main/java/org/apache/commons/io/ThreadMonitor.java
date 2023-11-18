package org.apache.commons.io;

class ThreadMonitor implements Runnable {
    private final Thread thread;
    private final long timeout;

    public static Thread start(long j) {
        return start(Thread.currentThread(), j);
    }

    public static Thread start(Thread thread, long j) {
        if (j <= 0) {
            return null;
        }
        thread = new Thread(new ThreadMonitor(thread, j), ThreadMonitor.class.getSimpleName());
        thread.setDaemon(1);
        thread.start();
        return thread;
    }

    public static void stop(Thread thread) {
        if (thread != null) {
            thread.interrupt();
        }
    }

    private ThreadMonitor(Thread thread, long j) {
        this.thread = thread;
        this.timeout = j;
    }

    public void run() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r2 = this;
        r0 = r2.timeout;	 Catch:{ InterruptedException -> 0x000a }
        sleep(r0);	 Catch:{ InterruptedException -> 0x000a }
        r0 = r2.thread;	 Catch:{ InterruptedException -> 0x000a }
        r0.interrupt();	 Catch:{ InterruptedException -> 0x000a }
    L_0x000a:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.ThreadMonitor.run():void");
    }

    private static void sleep(long j) throws InterruptedException {
        long currentTimeMillis = System.currentTimeMillis() + j;
        while (true) {
            Thread.sleep(j);
            long currentTimeMillis2 = currentTimeMillis - System.currentTimeMillis();
            if (currentTimeMillis2 > 0) {
                j = currentTimeMillis2;
            } else {
                return;
            }
        }
    }
}
