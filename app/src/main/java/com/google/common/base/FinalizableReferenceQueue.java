package com.google.common.base;

import com.google.common.annotations.VisibleForTesting;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;

public class FinalizableReferenceQueue implements Closeable {
    private static final String FINALIZER_CLASS_NAME = "com.google.common.base.internal.Finalizer";
    private static final Logger logger = Logger.getLogger(FinalizableReferenceQueue.class.getName());
    private static final Method startFinalizer = getStartFinalizer(loadFinalizer(new SystemLoader(), new DecoupledLoader(), new DirectLoader()));
    final PhantomReference<Object> frqRef = new PhantomReference(this, this.queue);
    final ReferenceQueue<Object> queue = new ReferenceQueue();
    final boolean threadStarted;

    interface FinalizerLoader {
        @Nullable
        Class<?> loadFinalizer();
    }

    static class DecoupledLoader implements FinalizerLoader {
        private static final String LOADING_ERROR = "Could not load Finalizer in its own class loader.Loading Finalizer in the current class loader instead. As a result, you will not be ableto garbage collect this class loader. To support reclaiming this class loader, eitherresolve the underlying issue, or move Google Collections to your system class path.";

        DecoupledLoader() {
        }

        public Class<?> loadFinalizer() {
            try {
                return newLoader(getBaseUrl()).loadClass(FinalizableReferenceQueue.FINALIZER_CLASS_NAME);
            } catch (Throwable e) {
                FinalizableReferenceQueue.logger.log(Level.WARNING, LOADING_ERROR, e);
                return null;
            }
        }

        URL getBaseUrl() throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(FinalizableReferenceQueue.FINALIZER_CLASS_NAME.replace('.', IOUtils.DIR_SEPARATOR_UNIX));
            stringBuilder.append(".class");
            String stringBuilder2 = stringBuilder.toString();
            URL resource = getClass().getClassLoader().getResource(stringBuilder2);
            if (resource == null) {
                throw new FileNotFoundException(stringBuilder2);
            }
            String url = resource.toString();
            if (url.endsWith(stringBuilder2)) {
                return new URL(resource, url.substring(0, url.length() - stringBuilder2.length()));
            }
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Unsupported path style: ");
            stringBuilder3.append(url);
            throw new IOException(stringBuilder3.toString());
        }

        URLClassLoader newLoader(URL url) {
            return new URLClassLoader(new URL[]{url}, null);
        }
    }

    static class DirectLoader implements FinalizerLoader {
        DirectLoader() {
        }

        public Class<?> loadFinalizer() {
            try {
                return Class.forName(FinalizableReferenceQueue.FINALIZER_CLASS_NAME);
            } catch (ClassNotFoundException e) {
                throw new AssertionError(e);
            }
        }
    }

    static class SystemLoader implements FinalizerLoader {
        @VisibleForTesting
        static boolean disabled;

        SystemLoader() {
        }

        public java.lang.Class<?> loadFinalizer() {
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
            r0 = disabled;
            r1 = 0;
            if (r0 == 0) goto L_0x0006;
        L_0x0005:
            return r1;
        L_0x0006:
            r0 = java.lang.ClassLoader.getSystemClassLoader();	 Catch:{ SecurityException -> 0x0015 }
            if (r0 == 0) goto L_0x0014;
        L_0x000c:
            r2 = "com.google.common.base.internal.Finalizer";	 Catch:{ ClassNotFoundException -> 0x0013 }
            r0 = r0.loadClass(r2);	 Catch:{ ClassNotFoundException -> 0x0013 }
            return r0;
        L_0x0013:
            return r1;
        L_0x0014:
            return r1;
        L_0x0015:
            r0 = com.google.common.base.FinalizableReferenceQueue.logger;
            r2 = "Not allowed to access system class loader.";
            r0.info(r2);
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.base.FinalizableReferenceQueue.SystemLoader.loadFinalizer():java.lang.Class<?>");
        }
    }

    public FinalizableReferenceQueue() {
        boolean z = true;
        try {
            startFinalizer.invoke(null, new Object[]{FinalizableReference.class, this.queue, this.frqRef});
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (Throwable th) {
            logger.log(Level.INFO, "Failed to start reference finalizer thread. Reference cleanup will only occur when new references are created.", th);
            z = false;
        }
        this.threadStarted = z;
    }

    public void close() {
        this.frqRef.enqueue();
        cleanUp();
    }

    void cleanUp() {
        if (!this.threadStarted) {
            while (true) {
                Reference poll = this.queue.poll();
                if (poll != null) {
                    poll.clear();
                    try {
                        ((FinalizableReference) poll).finalizeReferent();
                    } catch (Throwable th) {
                        logger.log(Level.SEVERE, "Error cleaning up after reference.", th);
                    }
                } else {
                    return;
                }
            }
        }
    }

    private static Class<?> loadFinalizer(FinalizerLoader... finalizerLoaderArr) {
        for (FinalizerLoader loadFinalizer : finalizerLoaderArr) {
            Class<?> loadFinalizer2 = loadFinalizer.loadFinalizer();
            if (loadFinalizer2 != null) {
                return loadFinalizer2;
            }
        }
        throw new AssertionError();
    }

    static Method getStartFinalizer(Class<?> cls) {
        try {
            return cls.getMethod("startFinalizer", new Class[]{Class.class, ReferenceQueue.class, PhantomReference.class});
        } catch (Class<?> cls2) {
            throw new AssertionError(cls2);
        }
    }
}
