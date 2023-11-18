package org.apache.commons.io;

import java.io.File;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class FileCleaningTracker {
    final List<String> deleteFailures = Collections.synchronizedList(new ArrayList());
    volatile boolean exitWhenFinished = false;
    /* renamed from: q */
    ReferenceQueue<Object> f65q = new ReferenceQueue();
    Thread reaper;
    final Collection<Tracker> trackers = Collections.synchronizedSet(new HashSet());

    private final class Reaper extends Thread {
        Reaper() {
            super("File Reaper");
            setPriority(10);
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
            r0 = org.apache.commons.io.FileCleaningTracker.this;
            r0 = r0.exitWhenFinished;
            if (r0 == 0) goto L_0x0012;
        L_0x0006:
            r0 = org.apache.commons.io.FileCleaningTracker.this;
            r0 = r0.trackers;
            r0 = r0.size();
            if (r0 <= 0) goto L_0x0011;
        L_0x0010:
            goto L_0x0012;
        L_0x0011:
            return;
        L_0x0012:
            r0 = org.apache.commons.io.FileCleaningTracker.this;	 Catch:{ InterruptedException -> 0x0000 }
            r0 = r0.f65q;	 Catch:{ InterruptedException -> 0x0000 }
            r0 = r0.remove();	 Catch:{ InterruptedException -> 0x0000 }
            r0 = (org.apache.commons.io.FileCleaningTracker.Tracker) r0;	 Catch:{ InterruptedException -> 0x0000 }
            r1 = org.apache.commons.io.FileCleaningTracker.this;	 Catch:{ InterruptedException -> 0x0000 }
            r1 = r1.trackers;	 Catch:{ InterruptedException -> 0x0000 }
            r1.remove(r0);	 Catch:{ InterruptedException -> 0x0000 }
            r1 = r0.delete();	 Catch:{ InterruptedException -> 0x0000 }
            if (r1 != 0) goto L_0x0034;	 Catch:{ InterruptedException -> 0x0000 }
        L_0x0029:
            r1 = org.apache.commons.io.FileCleaningTracker.this;	 Catch:{ InterruptedException -> 0x0000 }
            r1 = r1.deleteFailures;	 Catch:{ InterruptedException -> 0x0000 }
            r2 = r0.getPath();	 Catch:{ InterruptedException -> 0x0000 }
            r1.add(r2);	 Catch:{ InterruptedException -> 0x0000 }
        L_0x0034:
            r0.clear();	 Catch:{ InterruptedException -> 0x0000 }
            goto L_0x0000;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.FileCleaningTracker.Reaper.run():void");
        }
    }

    private static final class Tracker extends PhantomReference<Object> {
        private final FileDeleteStrategy deleteStrategy;
        private final String path;

        Tracker(String str, FileDeleteStrategy fileDeleteStrategy, Object obj, ReferenceQueue<? super Object> referenceQueue) {
            super(obj, referenceQueue);
            this.path = str;
            if (fileDeleteStrategy == null) {
                fileDeleteStrategy = FileDeleteStrategy.NORMAL;
            }
            this.deleteStrategy = fileDeleteStrategy;
        }

        public String getPath() {
            return this.path;
        }

        public boolean delete() {
            return this.deleteStrategy.deleteQuietly(new File(this.path));
        }
    }

    public void track(File file, Object obj) {
        track(file, obj, null);
    }

    public void track(File file, Object obj, FileDeleteStrategy fileDeleteStrategy) {
        if (file == null) {
            throw new NullPointerException("The file must not be null");
        }
        addTracker(file.getPath(), obj, fileDeleteStrategy);
    }

    public void track(String str, Object obj) {
        track(str, obj, null);
    }

    public void track(String str, Object obj, FileDeleteStrategy fileDeleteStrategy) {
        if (str == null) {
            throw new NullPointerException("The path must not be null");
        }
        addTracker(str, obj, fileDeleteStrategy);
    }

    private synchronized void addTracker(String str, Object obj, FileDeleteStrategy fileDeleteStrategy) {
        if (this.exitWhenFinished) {
            throw new IllegalStateException("No new trackers can be added once exitWhenFinished() is called");
        }
        if (this.reaper == null) {
            this.reaper = new Reaper();
            this.reaper.start();
        }
        this.trackers.add(new Tracker(str, fileDeleteStrategy, obj, this.f65q));
    }

    public int getTrackCount() {
        return this.trackers.size();
    }

    public List<String> getDeleteFailures() {
        return this.deleteFailures;
    }

    public synchronized void exitWhenFinished() {
        this.exitWhenFinished = true;
        if (this.reaper != null) {
            synchronized (this.reaper) {
                this.reaper.interrupt();
            }
        }
    }
}
