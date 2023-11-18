package retrofit2;

import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

class Platform {
    private static final Platform PLATFORM = findPlatform();

    static class Android extends Platform {
        Android() {
        }

        public Executor defaultCallbackExecutor() {
            return new Platform$Android$MainThreadExecutor();
        }

        CallAdapter$Factory defaultCallAdapterFactory(@Nullable Executor executor) {
            if (executor != null) {
                return new ExecutorCallAdapterFactory(executor);
            }
            throw new AssertionError();
        }
    }

    @IgnoreJRERequirement
    static class Java8 extends Platform {
        Java8() {
        }

        boolean isDefaultMethod(Method method) {
            return method.isDefault();
        }

        Object invokeDefaultMethod(Method method, Class<?> cls, Object obj, @Nullable Object... objArr) throws Throwable {
            Constructor declaredConstructor = Lookup.class.getDeclaredConstructor(new Class[]{Class.class, Integer.TYPE});
            declaredConstructor.setAccessible(true);
            return ((Lookup) declaredConstructor.newInstance(new Object[]{cls, Integer.valueOf(-1)})).unreflectSpecial(method, cls).bindTo(obj).invokeWithArguments(objArr);
        }
    }

    @Nullable
    Executor defaultCallbackExecutor() {
        return null;
    }

    boolean isDefaultMethod(Method method) {
        return false;
    }

    Platform() {
    }

    static Platform get() {
        return PLATFORM;
    }

    private static retrofit2.Platform findPlatform() {
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
        r0 = "android.os.Build";	 Catch:{ ClassNotFoundException -> 0x000f }
        java.lang.Class.forName(r0);	 Catch:{ ClassNotFoundException -> 0x000f }
        r0 = android.os.Build.VERSION.SDK_INT;	 Catch:{ ClassNotFoundException -> 0x000f }
        if (r0 == 0) goto L_0x000f;	 Catch:{ ClassNotFoundException -> 0x000f }
    L_0x0009:
        r0 = new retrofit2.Platform$Android;	 Catch:{ ClassNotFoundException -> 0x000f }
        r0.<init>();	 Catch:{ ClassNotFoundException -> 0x000f }
        return r0;
    L_0x000f:
        r0 = "java.util.Optional";	 Catch:{ ClassNotFoundException -> 0x001a }
        java.lang.Class.forName(r0);	 Catch:{ ClassNotFoundException -> 0x001a }
        r0 = new retrofit2.Platform$Java8;	 Catch:{ ClassNotFoundException -> 0x001a }
        r0.<init>();	 Catch:{ ClassNotFoundException -> 0x001a }
        return r0;
    L_0x001a:
        r0 = new retrofit2.Platform;
        r0.<init>();
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: retrofit2.Platform.findPlatform():retrofit2.Platform");
    }

    CallAdapter$Factory defaultCallAdapterFactory(@Nullable Executor executor) {
        if (executor != null) {
            return new ExecutorCallAdapterFactory(executor);
        }
        return DefaultCallAdapterFactory.INSTANCE;
    }

    @Nullable
    Object invokeDefaultMethod(Method method, Class<?> cls, Object obj, @Nullable Object... objArr) throws Throwable {
        throw new UnsupportedOperationException();
    }
}
