package com.google.common.base.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Finalizer implements Runnable {
    private static final String FINALIZABLE_REFERENCE = "com.google.common.base.FinalizableReference";
    private static final Field inheritableThreadLocals = getInheritableThreadLocalsField();
    private static final Logger logger = Logger.getLogger(Finalizer.class.getName());
    private final WeakReference<Class<?>> finalizableReferenceClassReference;
    private final PhantomReference<Object> frqReference;
    private final ReferenceQueue<Object> queue;

    public static void startFinalizer(Class<?> cls, ReferenceQueue<Object> referenceQueue, PhantomReference<Object> phantomReference) {
        if (cls.getName().equals(FINALIZABLE_REFERENCE)) {
            cls = new Thread(new Finalizer(cls, referenceQueue, phantomReference));
            cls.setName(Finalizer.class.getName());
            cls.setDaemon(true);
            try {
                if (inheritableThreadLocals != null) {
                    inheritableThreadLocals.set(cls, null);
                }
            } catch (ReferenceQueue<Object> referenceQueue2) {
                logger.log(Level.INFO, "Failed to clear thread local values inherited by reference finalizer thread.", referenceQueue2);
            }
            cls.start();
            return;
        }
        throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
    }

    private Finalizer(Class<?> cls, ReferenceQueue<Object> referenceQueue, PhantomReference<Object> phantomReference) {
        this.queue = referenceQueue;
        this.finalizableReferenceClassReference = new WeakReference(cls);
        this.frqReference = phantomReference;
    }

    public void run() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r1 = this;
    L_0x0000:
        r0 = r1.queue;	 Catch:{ InterruptedException -> 0x0000 }
        r0 = r0.remove();	 Catch:{ InterruptedException -> 0x0000 }
        r0 = r1.cleanUp(r0);	 Catch:{ InterruptedException -> 0x0000 }
        if (r0 != 0) goto L_0x0000;
    L_0x000c:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.base.internal.Finalizer.run():void");
    }

    private boolean cleanUp(Reference<?> reference) {
        Method finalizeReferentMethod = getFinalizeReferentMethod();
        if (finalizeReferentMethod == null) {
            return false;
        }
        do {
            reference.clear();
            if (reference == this.frqReference) {
                return false;
            }
            try {
                finalizeReferentMethod.invoke(reference, new Object[0]);
            } catch (Reference<?> reference2) {
                logger.log(Level.SEVERE, "Error cleaning up after reference.", reference2);
            }
            reference2 = this.queue.poll();
        } while (reference2 != null);
        return true;
    }

    private Method getFinalizeReferentMethod() {
        Class cls = (Class) this.finalizableReferenceClassReference.get();
        if (cls == null) {
            return null;
        }
        try {
            return cls.getMethod("finalizeReferent", new Class[0]);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    public static java.lang.reflect.Field getInheritableThreadLocalsField() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = java.lang.Thread.class;	 Catch:{ Throwable -> 0x000d }
        r1 = "inheritableThreadLocals";	 Catch:{ Throwable -> 0x000d }
        r0 = r0.getDeclaredField(r1);	 Catch:{ Throwable -> 0x000d }
        r1 = 1;	 Catch:{ Throwable -> 0x000d }
        r0.setAccessible(r1);	 Catch:{ Throwable -> 0x000d }
        return r0;
    L_0x000d:
        r0 = logger;
        r1 = java.util.logging.Level.INFO;
        r2 = "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.";
        r0.log(r1, r2);
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.base.internal.Finalizer.getInheritableThreadLocalsField():java.lang.reflect.Field");
    }
}
