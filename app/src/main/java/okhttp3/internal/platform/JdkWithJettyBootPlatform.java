package okhttp3.internal.platform;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import javax.net.ssl.SSLSocket;
import okhttp3.Protocol;
import okhttp3.internal.Util;

class JdkWithJettyBootPlatform extends Platform {
    private final Class<?> clientProviderClass;
    private final Method getMethod;
    private final Method putMethod;
    private final Method removeMethod;
    private final Class<?> serverProviderClass;

    private static class JettyNegoProvider implements InvocationHandler {
        private final List<String> protocols;
        String selected;
        boolean unsupported;

        JettyNegoProvider(List<String> list) {
            this.protocols = list;
        }

        public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
            obj = method.getName();
            Class returnType = method.getReturnType();
            if (objArr == null) {
                objArr = Util.EMPTY_STRING_ARRAY;
            }
            if (obj.equals("supports") && Boolean.TYPE == returnType) {
                return Boolean.valueOf(true);
            }
            if (obj.equals("unsupported") && Void.TYPE == returnType) {
                this.unsupported = true;
                return null;
            } else if (obj.equals("protocols") && objArr.length == 0) {
                return this.protocols;
            } else {
                if ((obj.equals("selectProtocol") || obj.equals("select")) && String.class == returnType && objArr.length == 1 && (objArr[0] instanceof List)) {
                    String str;
                    List list = (List) objArr[0];
                    method = list.size();
                    for (objArr = null; objArr < method; objArr++) {
                        if (this.protocols.contains(list.get(objArr))) {
                            str = (String) list.get(objArr);
                            this.selected = str;
                            return str;
                        }
                    }
                    str = (String) this.protocols.get(0);
                    this.selected = str;
                    return str;
                } else if ((!obj.equals("protocolSelected") && obj.equals("selected") == null) || objArr.length != 1) {
                    return method.invoke(this, objArr);
                } else {
                    this.selected = (String) objArr[0];
                    return null;
                }
            }
        }
    }

    JdkWithJettyBootPlatform(Method method, Method method2, Method method3, Class<?> cls, Class<?> cls2) {
        this.putMethod = method;
        this.getMethod = method2;
        this.removeMethod = method3;
        this.clientProviderClass = cls;
        this.serverProviderClass = cls2;
    }

    public void configureTlsExtensions(SSLSocket sSLSocket, String str, List<Protocol> list) {
        str = Platform.alpnProtocolNames(list);
        try {
            str = Proxy.newProxyInstance(Platform.class.getClassLoader(), new Class[]{this.clientProviderClass, this.serverProviderClass}, new JettyNegoProvider(str));
            this.putMethod.invoke(null, new Object[]{sSLSocket, str});
        } catch (SSLSocket sSLSocket2) {
            throw new AssertionError(sSLSocket2);
        }
    }

    public void afterHandshake(javax.net.ssl.SSLSocket r5) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r4 = this;
        r0 = r4.removeMethod;	 Catch:{ IllegalAccessException -> 0x000d, IllegalAccessException -> 0x000d }
        r1 = 0;	 Catch:{ IllegalAccessException -> 0x000d, IllegalAccessException -> 0x000d }
        r2 = 1;	 Catch:{ IllegalAccessException -> 0x000d, IllegalAccessException -> 0x000d }
        r2 = new java.lang.Object[r2];	 Catch:{ IllegalAccessException -> 0x000d, IllegalAccessException -> 0x000d }
        r3 = 0;	 Catch:{ IllegalAccessException -> 0x000d, IllegalAccessException -> 0x000d }
        r2[r3] = r5;	 Catch:{ IllegalAccessException -> 0x000d, IllegalAccessException -> 0x000d }
        r0.invoke(r1, r2);	 Catch:{ IllegalAccessException -> 0x000d, IllegalAccessException -> 0x000d }
        return;
    L_0x000d:
        r5 = new java.lang.AssertionError;
        r5.<init>();
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.JdkWithJettyBootPlatform.afterHandshake(javax.net.ssl.SSLSocket):void");
    }

    public java.lang.String getSelectedProtocol(javax.net.ssl.SSLSocket r4) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r3 = this;
        r0 = r3.getMethod;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        r1 = 1;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        r1 = new java.lang.Object[r1];	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        r2 = 0;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        r1[r2] = r4;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        r4 = 0;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        r0 = r0.invoke(r4, r1);	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        r0 = java.lang.reflect.Proxy.getInvocationHandler(r0);	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        r0 = (okhttp3.internal.platform.JdkWithJettyBootPlatform.JettyNegoProvider) r0;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        r1 = r0.unsupported;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        if (r1 != 0) goto L_0x0026;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
    L_0x0017:
        r1 = r0.selected;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        if (r1 != 0) goto L_0x0026;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
    L_0x001b:
        r0 = okhttp3.internal.platform.Platform.get();	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        r1 = 4;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        r2 = "ALPN callback dropped: HTTP/2 is disabled. Is alpn-boot on the boot class path?";	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        r0.log(r1, r2, r4);	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        return r4;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
    L_0x0026:
        r1 = r0.unsupported;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
        if (r1 == 0) goto L_0x002b;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
    L_0x002a:
        goto L_0x002d;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
    L_0x002b:
        r4 = r0.selected;	 Catch:{ InvocationTargetException -> 0x002e, InvocationTargetException -> 0x002e }
    L_0x002d:
        return r4;
    L_0x002e:
        r4 = new java.lang.AssertionError;
        r4.<init>();
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.JdkWithJettyBootPlatform.getSelectedProtocol(javax.net.ssl.SSLSocket):java.lang.String");
    }

    public static okhttp3.internal.platform.Platform buildIfSupported() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = "org.eclipse.jetty.alpn.ALPN";	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r1 = java.lang.Class.forName(r0);	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r2 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r2.<init>();	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r2.append(r0);	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3 = "$Provider";	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r2.append(r3);	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r2 = r2.toString();	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r2 = java.lang.Class.forName(r2);	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3.<init>();	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3.append(r0);	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r4 = "$ClientProvider";	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3.append(r4);	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3 = r3.toString();	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r8 = java.lang.Class.forName(r3);	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3 = new java.lang.StringBuilder;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3.<init>();	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3.append(r0);	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r0 = "$ServerProvider";	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3.append(r0);	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r0 = r3.toString();	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r9 = java.lang.Class.forName(r0);	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r0 = "put";	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3 = 2;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3 = new java.lang.Class[r3];	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r4 = javax.net.ssl.SSLSocket.class;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r5 = 0;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3[r5] = r4;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r4 = 1;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3[r4] = r2;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r0 = r1.getMethod(r0, r3);	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r2 = "get";	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3 = new java.lang.Class[r4];	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r6 = javax.net.ssl.SSLSocket.class;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3[r5] = r6;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r6 = r1.getMethod(r2, r3);	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r2 = "remove";	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3 = new java.lang.Class[r4];	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r4 = javax.net.ssl.SSLSocket.class;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r3[r5] = r4;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r7 = r1.getMethod(r2, r3);	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r1 = new okhttp3.internal.platform.JdkWithJettyBootPlatform;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r4 = r1;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r5 = r0;	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        r4.<init>(r5, r6, r7, r8, r9);	 Catch:{ ClassNotFoundException -> 0x0076, ClassNotFoundException -> 0x0076 }
        return r1;
    L_0x0076:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.JdkWithJettyBootPlatform.buildIfSupported():okhttp3.internal.platform.Platform");
    }
}
