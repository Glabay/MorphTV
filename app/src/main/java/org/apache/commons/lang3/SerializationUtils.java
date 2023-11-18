package org.apache.commons.lang3;

import de.timroes.axmlrpc.serializer.SerializerHandler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtils {

    static class ClassLoaderAwareObjectInputStream extends ObjectInputStream {
        private static final Map<String, Class<?>> primitiveTypes = new HashMap();
        private final ClassLoader classLoader;

        static {
            primitiveTypes.put("byte", Byte.TYPE);
            primitiveTypes.put("short", Short.TYPE);
            primitiveTypes.put(SerializerHandler.TYPE_INT, Integer.TYPE);
            primitiveTypes.put("long", Long.TYPE);
            primitiveTypes.put("float", Float.TYPE);
            primitiveTypes.put(SerializerHandler.TYPE_DOUBLE, Double.TYPE);
            primitiveTypes.put(SerializerHandler.TYPE_BOOLEAN, Boolean.TYPE);
            primitiveTypes.put("char", Character.TYPE);
            primitiveTypes.put("void", Void.TYPE);
        }

        public ClassLoaderAwareObjectInputStream(InputStream inputStream, ClassLoader classLoader) throws IOException {
            super(inputStream);
            this.classLoader = classLoader;
        }

        protected java.lang.Class<?> resolveClass(java.io.ObjectStreamClass r3) throws java.io.IOException, java.lang.ClassNotFoundException {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
            /*
            r2 = this;
            r3 = r3.getName();
            r0 = 0;
            r1 = r2.classLoader;	 Catch:{ ClassNotFoundException -> 0x000c }
            r1 = java.lang.Class.forName(r3, r0, r1);	 Catch:{ ClassNotFoundException -> 0x000c }
            return r1;
        L_0x000c:
            r1 = java.lang.Thread.currentThread();	 Catch:{ ClassNotFoundException -> 0x0019 }
            r1 = r1.getContextClassLoader();	 Catch:{ ClassNotFoundException -> 0x0019 }
            r0 = java.lang.Class.forName(r3, r0, r1);	 Catch:{ ClassNotFoundException -> 0x0019 }
            return r0;
        L_0x0019:
            r0 = move-exception;
            r1 = primitiveTypes;
            r3 = r1.get(r3);
            r3 = (java.lang.Class) r3;
            if (r3 == 0) goto L_0x0025;
        L_0x0024:
            return r3;
        L_0x0025:
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.SerializationUtils.ClassLoaderAwareObjectInputStream.resolveClass(java.io.ObjectStreamClass):java.lang.Class<?>");
        }
    }

    public static <T extends Serializable> T clone(T t) {
        ClassLoaderAwareObjectInputStream classLoaderAwareObjectInputStream = null;
        if (t == null) {
            return null;
        }
        try {
            T classLoaderAwareObjectInputStream2 = new ClassLoaderAwareObjectInputStream(new ByteArrayInputStream(serialize(t)), t.getClass().getClassLoader());
            try {
                Serializable serializable = (Serializable) classLoaderAwareObjectInputStream2.readObject();
                if (classLoaderAwareObjectInputStream2 != null) {
                    try {
                        classLoaderAwareObjectInputStream2.close();
                    } catch (T t2) {
                        throw new SerializationException("IOException on closing cloned object data InputStream.", t2);
                    }
                }
                return serializable;
            } catch (ClassNotFoundException e) {
                t2 = e;
                T t3 = classLoaderAwareObjectInputStream2;
                throw new SerializationException("ClassNotFoundException while reading cloned object data", t2);
            } catch (IOException e2) {
                t2 = e2;
                classLoaderAwareObjectInputStream = classLoaderAwareObjectInputStream2;
                throw new SerializationException("IOException while reading cloned object data", t2);
            } catch (Throwable th) {
                t2 = th;
                classLoaderAwareObjectInputStream = classLoaderAwareObjectInputStream2;
                if (classLoaderAwareObjectInputStream != null) {
                    try {
                        classLoaderAwareObjectInputStream.close();
                    } catch (T t22) {
                        throw new SerializationException("IOException on closing cloned object data InputStream.", t22);
                    }
                }
                throw t22;
            }
        } catch (ClassNotFoundException e3) {
            t22 = e3;
            throw new SerializationException("ClassNotFoundException while reading cloned object data", t22);
        } catch (IOException e4) {
            t22 = e4;
            throw new SerializationException("IOException while reading cloned object data", t22);
        } catch (Throwable th2) {
            t22 = th2;
            if (classLoaderAwareObjectInputStream != null) {
                classLoaderAwareObjectInputStream.close();
            }
            throw t22;
        }
    }

    public static <T extends Serializable> T roundtrip(T t) {
        return (Serializable) deserialize(serialize(t));
    }

    public static void serialize(java.io.Serializable r2, java.io.OutputStream r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r3 != 0) goto L_0x000a;
    L_0x0002:
        r2 = new java.lang.IllegalArgumentException;
        r3 = "The OutputStream must not be null";
        r2.<init>(r3);
        throw r2;
    L_0x000a:
        r0 = 0;
        r1 = new java.io.ObjectOutputStream;	 Catch:{ IOException -> 0x0021 }
        r1.<init>(r3);	 Catch:{ IOException -> 0x0021 }
        r1.writeObject(r2);	 Catch:{ IOException -> 0x001c, all -> 0x0019 }
        if (r1 == 0) goto L_0x0018;
    L_0x0015:
        r1.close();	 Catch:{ IOException -> 0x0018 }
    L_0x0018:
        return;
    L_0x0019:
        r2 = move-exception;
        r0 = r1;
        goto L_0x0028;
    L_0x001c:
        r2 = move-exception;
        r0 = r1;
        goto L_0x0022;
    L_0x001f:
        r2 = move-exception;
        goto L_0x0028;
    L_0x0021:
        r2 = move-exception;
    L_0x0022:
        r3 = new org.apache.commons.lang3.SerializationException;	 Catch:{ all -> 0x001f }
        r3.<init>(r2);	 Catch:{ all -> 0x001f }
        throw r3;	 Catch:{ all -> 0x001f }
    L_0x0028:
        if (r0 == 0) goto L_0x002d;
    L_0x002a:
        r0.close();	 Catch:{ IOException -> 0x002d }
    L_0x002d:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.SerializationUtils.serialize(java.io.Serializable, java.io.OutputStream):void");
    }

    public static byte[] serialize(Serializable serializable) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
        serialize(serializable, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static <T> T deserialize(java.io.InputStream r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        if (r2 != 0) goto L_0x000a;
    L_0x0002:
        r2 = new java.lang.IllegalArgumentException;
        r0 = "The InputStream must not be null";
        r2.<init>(r0);
        throw r2;
    L_0x000a:
        r0 = 0;
        r1 = new java.io.ObjectInputStream;	 Catch:{ ClassNotFoundException -> 0x002c, IOException -> 0x0025 }
        r1.<init>(r2);	 Catch:{ ClassNotFoundException -> 0x002c, IOException -> 0x0025 }
        r2 = r1.readObject();	 Catch:{ ClassNotFoundException -> 0x0020, IOException -> 0x001d, all -> 0x001a }
        if (r1 == 0) goto L_0x0019;
    L_0x0016:
        r1.close();	 Catch:{ IOException -> 0x0019 }
    L_0x0019:
        return r2;
    L_0x001a:
        r2 = move-exception;
        r0 = r1;
        goto L_0x0033;
    L_0x001d:
        r2 = move-exception;
        r0 = r1;
        goto L_0x0026;
    L_0x0020:
        r2 = move-exception;
        r0 = r1;
        goto L_0x002d;
    L_0x0023:
        r2 = move-exception;
        goto L_0x0033;
    L_0x0025:
        r2 = move-exception;
    L_0x0026:
        r1 = new org.apache.commons.lang3.SerializationException;	 Catch:{ all -> 0x0023 }
        r1.<init>(r2);	 Catch:{ all -> 0x0023 }
        throw r1;	 Catch:{ all -> 0x0023 }
    L_0x002c:
        r2 = move-exception;	 Catch:{ all -> 0x0023 }
    L_0x002d:
        r1 = new org.apache.commons.lang3.SerializationException;	 Catch:{ all -> 0x0023 }
        r1.<init>(r2);	 Catch:{ all -> 0x0023 }
        throw r1;	 Catch:{ all -> 0x0023 }
    L_0x0033:
        if (r0 == 0) goto L_0x0038;
    L_0x0035:
        r0.close();	 Catch:{ IOException -> 0x0038 }
    L_0x0038:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.lang3.SerializationUtils.deserialize(java.io.InputStream):T");
    }

    public static <T> T deserialize(byte[] bArr) {
        if (bArr != null) {
            return deserialize(new ByteArrayInputStream(bArr));
        }
        throw new IllegalArgumentException("The byte[] must not be null");
    }
}
