package com.google.common.reflect;

import com.google.common.annotations.Beta;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import javax.annotation.Nullable;

@Beta
public abstract class AbstractInvocationHandler implements InvocationHandler {
    private static final Object[] NO_ARGS = new Object[0];

    protected abstract Object handleInvocation(Object obj, Method method, Object[] objArr) throws Throwable;

    public final Object invoke(Object obj, Method method, @Nullable Object[] objArr) throws Throwable {
        if (objArr == null) {
            objArr = NO_ARGS;
        }
        if (objArr.length == 0 && method.getName().equals("hashCode")) {
            return Integer.valueOf(hashCode());
        }
        boolean z = true;
        if (objArr.length == 1 && method.getName().equals("equals") && method.getParameterTypes()[0] == Object.class) {
            method = objArr[0];
            if (method == null) {
                return Boolean.valueOf(false);
            }
            if (obj == method) {
                return Boolean.valueOf(true);
            }
            if (isProxyOfSameInterfaces(method, obj.getClass()) == null || equals(Proxy.getInvocationHandler(method)) == null) {
                z = false;
            }
            return Boolean.valueOf(z);
        } else if (objArr.length == 0 && method.getName().equals("toString")) {
            return toString();
        } else {
            return handleInvocation(obj, method, objArr);
        }
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return super.toString();
    }

    private static boolean isProxyOfSameInterfaces(Object obj, Class<?> cls) {
        if (!cls.isInstance(obj)) {
            if (!Proxy.isProxyClass(obj.getClass()) || Arrays.equals(obj.getClass().getInterfaces(), cls.getInterfaces()) == null) {
                return null;
            }
        }
        return true;
    }
}
