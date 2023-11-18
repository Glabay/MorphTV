package org.apache.commons.lang3.event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.Validate;

public class EventListenerSupport<L> implements Serializable {
    private static final long serialVersionUID = 3593265990380473632L;
    private List<L> listeners;
    private transient L[] prototypeArray;
    private transient L proxy;

    protected class ProxyInvocationHandler implements InvocationHandler {
        protected ProxyInvocationHandler() {
        }

        public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
            for (Object invoke : EventListenerSupport.this.listeners) {
                method.invoke(invoke, objArr);
            }
            return null;
        }
    }

    public static <T> EventListenerSupport<T> create(Class<T> cls) {
        return new EventListenerSupport(cls);
    }

    public EventListenerSupport(Class<L> cls) {
        this(cls, Thread.currentThread().getContextClassLoader());
    }

    public EventListenerSupport(Class<L> cls, ClassLoader classLoader) {
        this();
        Validate.notNull(cls, "Listener interface cannot be null.", new Object[0]);
        Validate.notNull(classLoader, "ClassLoader cannot be null.", new Object[0]);
        Validate.isTrue(cls.isInterface(), "Class {0} is not an interface", cls.getName());
        initializeTransientFields(cls, classLoader);
    }

    private EventListenerSupport() {
        this.listeners = new CopyOnWriteArrayList();
    }

    public L fire() {
        return this.proxy;
    }

    public void addListener(L l) {
        addListener(l, true);
    }

    public void addListener(L l, boolean z) {
        Validate.notNull(l, "Listener object cannot be null.", new Object[0]);
        if (z) {
            this.listeners.add(l);
        } else if (!this.listeners.contains(l)) {
            this.listeners.add(l);
        }
    }

    int getListenerCount() {
        return this.listeners.size();
    }

    public void removeListener(L l) {
        Validate.notNull(l, "Listener object cannot be null.", new Object[0]);
        this.listeners.remove(l);
    }

    public L[] getListeners() {
        return this.listeners.toArray(this.prototypeArray);
    }

    private void writeObject(java.io.ObjectOutputStream r5) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r4 = this;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r1 = new java.io.ObjectOutputStream;
        r2 = new java.io.ByteArrayOutputStream;
        r2.<init>();
        r1.<init>(r2);
        r2 = r4.listeners;
        r2 = r2.iterator();
    L_0x0015:
        r3 = r2.hasNext();
        if (r3 == 0) goto L_0x0031;
    L_0x001b:
        r3 = r2.next();
        r1.writeObject(r3);	 Catch:{ IOException -> 0x0026 }
        r0.add(r3);	 Catch:{ IOException -> 0x0026 }
        goto L_0x0015;
    L_0x0026:
        r1 = new java.io.ObjectOutputStream;
        r3 = new java.io.ByteArrayOutputStream;
        r3.<init>();
        r1.<init>(r3);
        goto L_0x0015;
    L_0x0031:
        r1 = r4.prototypeArray;
        r0 = r0.toArray(r1);
        r5.writeObject(r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.event.EventListenerSupport.writeObject(java.io.ObjectOutputStream):void");
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        Object[] objArr = (Object[]) objectInputStream.readObject();
        this.listeners = new CopyOnWriteArrayList(objArr);
        initializeTransientFields(objArr.getClass().getComponentType(), Thread.currentThread().getContextClassLoader());
    }

    private void initializeTransientFields(Class<L> cls, ClassLoader classLoader) {
        this.prototypeArray = (Object[]) Array.newInstance(cls, 0);
        createProxy(cls, classLoader);
    }

    private void createProxy(Class<L> cls, ClassLoader classLoader) {
        this.proxy = cls.cast(Proxy.newProxyInstance(classLoader, new Class[]{cls}, createInvocationHandler()));
    }

    protected InvocationHandler createInvocationHandler() {
        return new ProxyInvocationHandler();
    }
}
