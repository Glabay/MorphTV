package org.apache.commons.io.monitor;

import com.google.android.gms.cast.framework.media.NotificationOptions;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;

public final class FileAlterationMonitor implements Runnable {
    private final long interval;
    private final List<FileAlterationObserver> observers;
    private volatile boolean running;
    private Thread thread;
    private ThreadFactory threadFactory;

    public FileAlterationMonitor() {
        this(NotificationOptions.SKIP_STEP_TEN_SECONDS_IN_MS);
    }

    public FileAlterationMonitor(long j) {
        this.observers = new CopyOnWriteArrayList();
        this.thread = null;
        this.running = false;
        this.interval = j;
    }

    public FileAlterationMonitor(long j, FileAlterationObserver... fileAlterationObserverArr) {
        this(j);
        if (fileAlterationObserverArr != null) {
            for (FileAlterationObserver addObserver : fileAlterationObserverArr) {
                addObserver(addObserver);
            }
        }
    }

    public long getInterval() {
        return this.interval;
    }

    public synchronized void setThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    public void addObserver(FileAlterationObserver fileAlterationObserver) {
        if (fileAlterationObserver != null) {
            this.observers.add(fileAlterationObserver);
        }
    }

    public void removeObserver(FileAlterationObserver fileAlterationObserver) {
        if (fileAlterationObserver != null) {
            while (this.observers.remove(fileAlterationObserver)) {
            }
        }
    }

    public Iterable<FileAlterationObserver> getObservers() {
        return this.observers;
    }

    public synchronized void start() throws Exception {
        if (this.running) {
            throw new IllegalStateException("Monitor is already running");
        }
        for (FileAlterationObserver initialize : this.observers) {
            initialize.initialize();
        }
        this.running = true;
        if (this.threadFactory != null) {
            this.thread = this.threadFactory.newThread(this);
        } else {
            this.thread = new Thread(this);
        }
        this.thread.start();
    }

    public synchronized void stop() throws Exception {
        stop(this.interval);
    }

    public synchronized void stop(long r2) throws java.lang.Exception {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r1 = this;
        monitor-enter(r1);
        r0 = r1.running;	 Catch:{ all -> 0x0035 }
        if (r0 != 0) goto L_0x000d;	 Catch:{ all -> 0x0035 }
    L_0x0005:
        r2 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0035 }
        r3 = "Monitor is not running";	 Catch:{ all -> 0x0035 }
        r2.<init>(r3);	 Catch:{ all -> 0x0035 }
        throw r2;	 Catch:{ all -> 0x0035 }
    L_0x000d:
        r0 = 0;	 Catch:{ all -> 0x0035 }
        r1.running = r0;	 Catch:{ all -> 0x0035 }
        r0 = r1.thread;	 Catch:{ InterruptedException -> 0x0016 }
        r0.join(r2);	 Catch:{ InterruptedException -> 0x0016 }
        goto L_0x001d;
    L_0x0016:
        r2 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0035 }
        r2.interrupt();	 Catch:{ all -> 0x0035 }
    L_0x001d:
        r2 = r1.observers;	 Catch:{ all -> 0x0035 }
        r2 = r2.iterator();	 Catch:{ all -> 0x0035 }
    L_0x0023:
        r3 = r2.hasNext();	 Catch:{ all -> 0x0035 }
        if (r3 == 0) goto L_0x0033;	 Catch:{ all -> 0x0035 }
    L_0x0029:
        r3 = r2.next();	 Catch:{ all -> 0x0035 }
        r3 = (org.apache.commons.io.monitor.FileAlterationObserver) r3;	 Catch:{ all -> 0x0035 }
        r3.destroy();	 Catch:{ all -> 0x0035 }
        goto L_0x0023;
    L_0x0033:
        monitor-exit(r1);
        return;
    L_0x0035:
        r2 = move-exception;
        monitor-exit(r1);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.monitor.FileAlterationMonitor.stop(long):void");
    }

    public void run() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r2 = this;
    L_0x0000:
        r0 = r2.running;
        if (r0 == 0) goto L_0x0025;
    L_0x0004:
        r0 = r2.observers;
        r0 = r0.iterator();
    L_0x000a:
        r1 = r0.hasNext();
        if (r1 == 0) goto L_0x001a;
    L_0x0010:
        r1 = r0.next();
        r1 = (org.apache.commons.io.monitor.FileAlterationObserver) r1;
        r1.checkAndNotify();
        goto L_0x000a;
    L_0x001a:
        r0 = r2.running;
        if (r0 != 0) goto L_0x001f;
    L_0x001e:
        goto L_0x0025;
    L_0x001f:
        r0 = r2.interval;	 Catch:{ InterruptedException -> 0x0000 }
        java.lang.Thread.sleep(r0);	 Catch:{ InterruptedException -> 0x0000 }
        goto L_0x0000;
    L_0x0025:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.monitor.FileAlterationMonitor.run():void");
    }
}
