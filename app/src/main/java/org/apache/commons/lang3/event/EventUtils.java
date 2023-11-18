package org.apache.commons.lang3.event;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.reflect.MethodUtils;

public class EventUtils {

    private static class EventBindingInvocationHandler implements InvocationHandler {
        private final Set<String> eventTypes;
        private final String methodName;
        private final Object target;

        EventBindingInvocationHandler(Object obj, String str, String[] strArr) {
            this.target = obj;
            this.methodName = str;
            this.eventTypes = new HashSet(Arrays.asList(strArr));
        }

        public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
            if (this.eventTypes.isEmpty() == null) {
                if (this.eventTypes.contains(method.getName()) == null) {
                    return null;
                }
            }
            if (hasMatchingParametersMethod(method) != null) {
                return MethodUtils.invokeMethod(this.target, this.methodName, objArr);
            }
            return MethodUtils.invokeMethod(this.target, this.methodName);
        }

        private boolean hasMatchingParametersMethod(Method method) {
            return MethodUtils.getAccessibleMethod(this.target.getClass(), this.methodName, method.getParameterTypes()) != null ? true : null;
        }
    }

    public static <L> void addEventListener(java.lang.Object r3, java.lang.Class<L> r4, L r5) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = new java.lang.StringBuilder;	 Catch:{ NoSuchMethodException -> 0x0068, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x001f }
        r0.<init>();	 Catch:{ NoSuchMethodException -> 0x0068, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x001f }
        r1 = "add";	 Catch:{ NoSuchMethodException -> 0x0068, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x001f }
        r0.append(r1);	 Catch:{ NoSuchMethodException -> 0x0068, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x001f }
        r1 = r4.getSimpleName();	 Catch:{ NoSuchMethodException -> 0x0068, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x001f }
        r0.append(r1);	 Catch:{ NoSuchMethodException -> 0x0068, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x001f }
        r0 = r0.toString();	 Catch:{ NoSuchMethodException -> 0x0068, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x001f }
        r1 = 1;	 Catch:{ NoSuchMethodException -> 0x0068, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x001f }
        r1 = new java.lang.Object[r1];	 Catch:{ NoSuchMethodException -> 0x0068, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x001f }
        r2 = 0;	 Catch:{ NoSuchMethodException -> 0x0068, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x001f }
        r1[r2] = r5;	 Catch:{ NoSuchMethodException -> 0x0068, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x001f }
        org.apache.commons.lang3.reflect.MethodUtils.invokeMethod(r3, r0, r1);	 Catch:{ NoSuchMethodException -> 0x0068, IllegalAccessException -> 0x002c, InvocationTargetException -> 0x001f }
        return;
    L_0x001f:
        r3 = move-exception;
        r4 = new java.lang.RuntimeException;
        r5 = "Unable to add listener.";
        r3 = r3.getCause();
        r4.<init>(r5, r3);
        throw r4;
    L_0x002c:
        r5 = new java.lang.IllegalArgumentException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Class ";
        r0.append(r1);
        r3 = r3.getClass();
        r3 = r3.getName();
        r0.append(r3);
        r3 = " does not have an accessible add";
        r0.append(r3);
        r3 = r4.getSimpleName();
        r0.append(r3);
        r3 = " method which takes a parameter of type ";
        r0.append(r3);
        r3 = r4.getName();
        r0.append(r3);
        r3 = ".";
        r0.append(r3);
        r3 = r0.toString();
        r5.<init>(r3);
        throw r5;
    L_0x0068:
        r5 = new java.lang.IllegalArgumentException;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Class ";
        r0.append(r1);
        r3 = r3.getClass();
        r3 = r3.getName();
        r0.append(r3);
        r3 = " does not have a public add";
        r0.append(r3);
        r3 = r4.getSimpleName();
        r0.append(r3);
        r3 = " method which takes a parameter of type ";
        r0.append(r3);
        r3 = r4.getName();
        r0.append(r3);
        r3 = ".";
        r0.append(r3);
        r3 = r0.toString();
        r5.<init>(r3);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.event.EventUtils.addEventListener(java.lang.Object, java.lang.Class, java.lang.Object):void");
    }

    public static <L> void bindEventsToMethod(Object obj, String str, Object obj2, Class<L> cls, String... strArr) {
        addEventListener(obj2, cls, cls.cast(Proxy.newProxyInstance(obj.getClass().getClassLoader(), new Class[]{cls}, new EventBindingInvocationHandler(obj, str, strArr))));
    }
}
