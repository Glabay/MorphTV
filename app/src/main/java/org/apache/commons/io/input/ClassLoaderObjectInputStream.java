package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

public class ClassLoaderObjectInputStream extends ObjectInputStream {
    private final ClassLoader classLoader;

    public ClassLoaderObjectInputStream(ClassLoader classLoader, InputStream inputStream) throws IOException, StreamCorruptedException {
        super(inputStream);
        this.classLoader = classLoader;
    }

    protected java.lang.Class<?> resolveClass(java.io.ObjectStreamClass r4) throws java.io.IOException, java.lang.ClassNotFoundException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r3 = this;
        r0 = r4.getName();	 Catch:{ ClassNotFoundException -> 0x000c }
        r1 = 0;	 Catch:{ ClassNotFoundException -> 0x000c }
        r2 = r3.classLoader;	 Catch:{ ClassNotFoundException -> 0x000c }
        r0 = java.lang.Class.forName(r0, r1, r2);	 Catch:{ ClassNotFoundException -> 0x000c }
        return r0;
    L_0x000c:
        r4 = super.resolveClass(r4);
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.input.ClassLoaderObjectInputStream.resolveClass(java.io.ObjectStreamClass):java.lang.Class<?>");
    }

    protected java.lang.Class<?> resolveProxyClass(java.lang.String[] r6) throws java.io.IOException, java.lang.ClassNotFoundException {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r5 = this;
        r0 = r6.length;
        r0 = new java.lang.Class[r0];
        r1 = 0;
        r2 = 0;
    L_0x0005:
        r3 = r6.length;
        if (r2 >= r3) goto L_0x0015;
    L_0x0008:
        r3 = r6[r2];
        r4 = r5.classLoader;
        r3 = java.lang.Class.forName(r3, r1, r4);
        r0[r2] = r3;
        r2 = r2 + 1;
        goto L_0x0005;
    L_0x0015:
        r1 = r5.classLoader;	 Catch:{ IllegalArgumentException -> 0x001c }
        r0 = java.lang.reflect.Proxy.getProxyClass(r1, r0);	 Catch:{ IllegalArgumentException -> 0x001c }
        return r0;
    L_0x001c:
        r6 = super.resolveProxyClass(r6);
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.io.input.ClassLoaderObjectInputStream.resolveProxyClass(java.lang.String[]):java.lang.Class<?>");
    }
}
