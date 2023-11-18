package com.squareup.duktape;

import android.support.annotation.Keep;
import java.io.Closeable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public final class Duktape implements Closeable {
    private long context;

    private static native Object call(long j, long j2, Object obj, Object[] objArr);

    private static native long createContext();

    private static native void destroyContext(long j);

    private static native Object evaluate(long j, String str, String str2);

    private static native long get(long j, String str, Object[] objArr);

    private static native void set(long j, String str, Object obj, Object[] objArr);

    static {
        System.loadLibrary("duktape");
    }

    public static Duktape create() {
        long createContext = createContext();
        if (createContext != 0) {
            return new Duktape(createContext);
        }
        throw new OutOfMemoryError("Cannot create Duktape instance");
    }

    private Duktape(long j) {
        this.context = j;
    }

    public synchronized Object evaluate(String str, String str2) {
        return evaluate(this.context, str, str2);
    }

    public synchronized Object evaluate(String str) {
        return evaluate(this.context, str, "?");
    }

    public synchronized <T> void set(String str, Class<T> cls, T t) {
        if (!cls.isInterface()) {
            t = new StringBuilder();
            t.append("Only interfaces can be bound. Received: ");
            t.append(cls);
            throw new UnsupportedOperationException(t.toString());
        } else if (cls.getInterfaces().length > 0) {
            t = new StringBuilder();
            t.append(cls);
            t.append(" must not extend other interfaces");
            throw new UnsupportedOperationException(t.toString());
        } else if (cls.isInstance(t)) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            for (Method method : cls.getMethods()) {
                if (linkedHashMap.put(method.getName(), method) != null) {
                    t = new StringBuilder();
                    t.append(method.getName());
                    t.append(" is overloaded in ");
                    t.append(cls);
                    throw new UnsupportedOperationException(t.toString());
                }
            }
            set(this.context, str, t, linkedHashMap.values().toArray());
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(t.getClass());
            stringBuilder.append(" is not an instance of ");
            stringBuilder.append(cls);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public synchronized <T> T get(String str, Class<T> cls) {
        final long j;
        final Duktape duktape;
        final String str2;
        final Class<T> cls2;
        StringBuilder stringBuilder;
        if (!cls.isInterface()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Only interfaces can be proxied. Received: ");
            stringBuilder.append(cls);
            throw new UnsupportedOperationException(stringBuilder.toString());
        } else if (cls.getInterfaces().length > 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(cls);
            stringBuilder.append(" must not extend other interfaces");
            throw new UnsupportedOperationException(stringBuilder.toString());
        } else {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            for (Method method : cls.getMethods()) {
                if (linkedHashMap.put(method.getName(), method) != null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(method.getName());
                    stringBuilder.append(" is overloaded in ");
                    stringBuilder.append(cls);
                    throw new UnsupportedOperationException(stringBuilder.toString());
                }
            }
            j = get(this.context, str, linkedHashMap.values().toArray());
            duktape = this;
            str2 = str;
            cls2 = cls;
        }
        return Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new InvocationHandler() {
            public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, objArr);
                }
                synchronized (duktape) {
                    method = Duktape.call(duktape.context, j, method, objArr);
                }
                return method;
            }

            public String toString() {
                return String.format("DuktapeProxy{name=%s, type=%s}", new Object[]{str2, cls2.getName()});
            }
        });
    }

    public synchronized void close() {
        if (this.context != 0) {
            long j = this.context;
            this.context = 0;
            destroyContext(j);
        }
    }

    protected synchronized void finalize() throws Throwable {
        if (this.context != 0) {
            Logger.getLogger(getClass().getName()).warning("Duktape instance leaked!");
        }
    }

    @Keep
    private static int getLocalTimeZoneOffset(double d) {
        return (int) TimeUnit.MILLISECONDS.toSeconds((long) TimeZone.getDefault().getOffset((long) d));
    }
}
