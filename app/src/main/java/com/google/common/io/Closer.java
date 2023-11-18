package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import javax.annotation.Nullable;

@Beta
public final class Closer implements Closeable {
    private static final Suppressor SUPPRESSOR = (SuppressingSuppressor.isAvailable() ? SuppressingSuppressor.INSTANCE : LoggingSuppressor.INSTANCE);
    private final Deque<Closeable> stack = new ArrayDeque(4);
    @VisibleForTesting
    final Suppressor suppressor;
    private Throwable thrown;

    @VisibleForTesting
    interface Suppressor {
        void suppress(Closeable closeable, Throwable th, Throwable th2);
    }

    @VisibleForTesting
    static final class LoggingSuppressor implements Suppressor {
        static final LoggingSuppressor INSTANCE = new LoggingSuppressor();

        LoggingSuppressor() {
        }

        public void suppress(Closeable closeable, Throwable th, Throwable th2) {
            th = Closeables.logger;
            Level level = Level.WARNING;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Suppressing exception thrown when closing ");
            stringBuilder.append(closeable);
            th.log(level, stringBuilder.toString(), th2);
        }
    }

    @VisibleForTesting
    static final class SuppressingSuppressor implements Suppressor {
        static final SuppressingSuppressor INSTANCE = new SuppressingSuppressor();
        static final Method addSuppressed = getAddSuppressed();

        SuppressingSuppressor() {
        }

        static boolean isAvailable() {
            return addSuppressed != null;
        }

        private static java.lang.reflect.Method getAddSuppressed() {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r0 = java.lang.Throwable.class;	 Catch:{ Throwable -> 0x0011 }
            r1 = "addSuppressed";	 Catch:{ Throwable -> 0x0011 }
            r2 = 1;	 Catch:{ Throwable -> 0x0011 }
            r2 = new java.lang.Class[r2];	 Catch:{ Throwable -> 0x0011 }
            r3 = 0;	 Catch:{ Throwable -> 0x0011 }
            r4 = java.lang.Throwable.class;	 Catch:{ Throwable -> 0x0011 }
            r2[r3] = r4;	 Catch:{ Throwable -> 0x0011 }
            r0 = r0.getMethod(r1, r2);	 Catch:{ Throwable -> 0x0011 }
            return r0;
        L_0x0011:
            r0 = 0;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.io.Closer.SuppressingSuppressor.getAddSuppressed():java.lang.reflect.Method");
        }

        public void suppress(java.io.Closeable r4, java.lang.Throwable r5, java.lang.Throwable r6) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r3 = this;
            if (r5 != r6) goto L_0x0003;
        L_0x0002:
            return;
        L_0x0003:
            r0 = addSuppressed;	 Catch:{ Throwable -> 0x000f }
            r1 = 1;	 Catch:{ Throwable -> 0x000f }
            r1 = new java.lang.Object[r1];	 Catch:{ Throwable -> 0x000f }
            r2 = 0;	 Catch:{ Throwable -> 0x000f }
            r1[r2] = r6;	 Catch:{ Throwable -> 0x000f }
            r0.invoke(r5, r1);	 Catch:{ Throwable -> 0x000f }
            goto L_0x0014;
        L_0x000f:
            r0 = com.google.common.io.Closer.LoggingSuppressor.INSTANCE;
            r0.suppress(r4, r5, r6);
        L_0x0014:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.io.Closer.SuppressingSuppressor.suppress(java.io.Closeable, java.lang.Throwable, java.lang.Throwable):void");
        }
    }

    public static Closer create() {
        return new Closer(SUPPRESSOR);
    }

    @VisibleForTesting
    Closer(Suppressor suppressor) {
        this.suppressor = (Suppressor) Preconditions.checkNotNull(suppressor);
    }

    public <C extends Closeable> C register(@Nullable C c) {
        if (c != null) {
            this.stack.addFirst(c);
        }
        return c;
    }

    public RuntimeException rethrow(Throwable th) throws IOException {
        Preconditions.checkNotNull(th);
        this.thrown = th;
        Throwables.propagateIfPossible(th, IOException.class);
        throw new RuntimeException(th);
    }

    public <X extends Exception> RuntimeException rethrow(Throwable th, Class<X> cls) throws IOException, Exception {
        Preconditions.checkNotNull(th);
        this.thrown = th;
        Throwables.propagateIfPossible(th, IOException.class);
        Throwables.propagateIfPossible(th, cls);
        throw new RuntimeException(th);
    }

    public <X1 extends Exception, X2 extends Exception> RuntimeException rethrow(Throwable th, Class<X1> cls, Class<X2> cls2) throws IOException, Exception, Exception {
        Preconditions.checkNotNull(th);
        this.thrown = th;
        Throwables.propagateIfPossible(th, IOException.class);
        Throwables.propagateIfPossible(th, cls, cls2);
        throw new RuntimeException(th);
    }

    public void close() throws IOException {
        Throwable th = this.thrown;
        while (!this.stack.isEmpty()) {
            Closeable closeable = (Closeable) this.stack.removeFirst();
            try {
                closeable.close();
            } catch (Throwable th2) {
                if (th == null) {
                    th = th2;
                } else {
                    this.suppressor.suppress(closeable, th, th2);
                }
            }
        }
        if (this.thrown == null && th != null) {
            Throwables.propagateIfPossible(th, IOException.class);
            throw new AssertionError(th);
        }
    }
}
