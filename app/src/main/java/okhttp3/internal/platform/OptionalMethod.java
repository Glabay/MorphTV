package okhttp3.internal.platform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class OptionalMethod<T> {
    private final String methodName;
    private final Class[] methodParams;
    private final Class<?> returnType;

    OptionalMethod(Class<?> cls, String str, Class... clsArr) {
        this.returnType = cls;
        this.methodName = str;
        this.methodParams = clsArr;
    }

    public boolean isSupported(T t) {
        return getMethod(t.getClass()) != null ? true : null;
    }

    public java.lang.Object invokeOptional(T r3, java.lang.Object... r4) throws java.lang.reflect.InvocationTargetException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r2 = this;
        r0 = r3.getClass();
        r0 = r2.getMethod(r0);
        r1 = 0;
        if (r0 != 0) goto L_0x000c;
    L_0x000b:
        return r1;
    L_0x000c:
        r3 = r0.invoke(r3, r4);	 Catch:{ IllegalAccessException -> 0x0011 }
        return r3;
    L_0x0011:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.OptionalMethod.invokeOptional(java.lang.Object, java.lang.Object[]):java.lang.Object");
    }

    public Object invokeOptionalWithoutCheckedException(T t, Object... objArr) {
        try {
            return invokeOptional(t, objArr);
        } catch (T t2) {
            t2 = t2.getTargetException();
            if ((t2 instanceof RuntimeException) != null) {
                throw ((RuntimeException) t2);
            }
            objArr = new AssertionError("Unexpected exception");
            objArr.initCause(t2);
            throw objArr;
        }
    }

    public Object invoke(T t, Object... objArr) throws InvocationTargetException {
        Method method = getMethod(t.getClass());
        if (method == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Method ");
            stringBuilder.append(this.methodName);
            stringBuilder.append(" not supported for object ");
            stringBuilder.append(t);
            throw new AssertionError(stringBuilder.toString());
        }
        try {
            return method.invoke(t, objArr);
        } catch (T t2) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unexpectedly could not call: ");
            stringBuilder2.append(method);
            objArr = new AssertionError(stringBuilder2.toString());
            objArr.initCause(t2);
            throw objArr;
        }
    }

    public Object invokeWithoutCheckedException(T t, Object... objArr) {
        try {
            return invoke(t, objArr);
        } catch (T t2) {
            t2 = t2.getTargetException();
            if ((t2 instanceof RuntimeException) != null) {
                throw ((RuntimeException) t2);
            }
            objArr = new AssertionError("Unexpected exception");
            objArr.initCause(t2);
            throw objArr;
        }
    }

    private Method getMethod(Class<?> cls) {
        if (this.methodName == null) {
            return null;
        }
        cls = getPublicMethod(cls, this.methodName, this.methodParams);
        if (cls == null || this.returnType == null || this.returnType.isAssignableFrom(cls.getReturnType())) {
            return cls;
        }
        return null;
    }

    private static java.lang.reflect.Method getPublicMethod(java.lang.Class<?> r1, java.lang.String r2, java.lang.Class[] r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        r1 = r1.getMethod(r2, r3);	 Catch:{ NoSuchMethodException -> 0x000d }
        r2 = r1.getModifiers();	 Catch:{ NoSuchMethodException -> 0x000e }
        r2 = r2 & 1;
        if (r2 != 0) goto L_0x000e;
    L_0x000d:
        r1 = r0;
    L_0x000e:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.OptionalMethod.getPublicMethod(java.lang.Class, java.lang.String, java.lang.Class[]):java.lang.reflect.Method");
    }
}
