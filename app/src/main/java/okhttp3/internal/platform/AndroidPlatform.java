package okhttp3.internal.platform;

import android.util.Log;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import okhttp3.internal.tls.CertificateChainCleaner;

class AndroidPlatform extends Platform {
    private static final int MAX_LOG_LENGTH = 4000;
    private final CloseGuard closeGuard = CloseGuard.get();
    private final OptionalMethod<Socket> getAlpnSelectedProtocol;
    private final OptionalMethod<Socket> setAlpnProtocols;
    private final OptionalMethod<Socket> setHostname;
    private final OptionalMethod<Socket> setUseSessionTickets;
    private final Class<?> sslParametersClass;

    static final class AndroidCertificateChainCleaner extends CertificateChainCleaner {
        private final Method checkServerTrusted;
        private final Object x509TrustManagerExtensions;

        public int hashCode() {
            return 0;
        }

        AndroidCertificateChainCleaner(Object obj, Method method) {
            this.x509TrustManagerExtensions = obj;
            this.checkServerTrusted = method;
        }

        public List<Certificate> clean(List<Certificate> list, String str) throws SSLPeerUnverifiedException {
            try {
                X509Certificate[] x509CertificateArr = (X509Certificate[]) list.toArray(new X509Certificate[list.size()]);
                return (List) this.checkServerTrusted.invoke(this.x509TrustManagerExtensions, new Object[]{x509CertificateArr, "RSA", str});
            } catch (List<Certificate> list2) {
                str = new SSLPeerUnverifiedException(list2.getMessage());
                str.initCause(list2);
                throw str;
            } catch (List<Certificate> list22) {
                throw new AssertionError(list22);
            }
        }

        public boolean equals(Object obj) {
            return obj instanceof AndroidCertificateChainCleaner;
        }
    }

    static final class CloseGuard {
        private final Method getMethod;
        private final Method openMethod;
        private final Method warnIfOpenMethod;

        CloseGuard(Method method, Method method2, Method method3) {
            this.getMethod = method;
            this.openMethod = method2;
            this.warnIfOpenMethod = method3;
        }

        java.lang.Object createAndOpen(java.lang.String r6) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
            /*
            r5 = this;
            r0 = r5.getMethod;
            r1 = 0;
            if (r0 == 0) goto L_0x0019;
        L_0x0005:
            r0 = r5.getMethod;	 Catch:{ Exception -> 0x0019 }
            r2 = 0;	 Catch:{ Exception -> 0x0019 }
            r3 = new java.lang.Object[r2];	 Catch:{ Exception -> 0x0019 }
            r0 = r0.invoke(r1, r3);	 Catch:{ Exception -> 0x0019 }
            r3 = r5.openMethod;	 Catch:{ Exception -> 0x0019 }
            r4 = 1;	 Catch:{ Exception -> 0x0019 }
            r4 = new java.lang.Object[r4];	 Catch:{ Exception -> 0x0019 }
            r4[r2] = r6;	 Catch:{ Exception -> 0x0019 }
            r3.invoke(r0, r4);	 Catch:{ Exception -> 0x0019 }
            return r0;
        L_0x0019:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.AndroidPlatform.CloseGuard.createAndOpen(java.lang.String):java.lang.Object");
        }

        boolean warnIfOpen(java.lang.Object r4) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
            /*
            r3 = this;
            r0 = 0;
            if (r4 == 0) goto L_0x000b;
        L_0x0003:
            r1 = r3.warnIfOpenMethod;	 Catch:{ Exception -> 0x000b }
            r2 = new java.lang.Object[r0];	 Catch:{ Exception -> 0x000b }
            r1.invoke(r4, r2);	 Catch:{ Exception -> 0x000b }
            r0 = 1;
        L_0x000b:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.AndroidPlatform.CloseGuard.warnIfOpen(java.lang.Object):boolean");
        }

        static okhttp3.internal.platform.AndroidPlatform.CloseGuard get() {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
            /*
            r0 = 0;
            r1 = "dalvik.system.CloseGuard";	 Catch:{ Exception -> 0x0027 }
            r1 = java.lang.Class.forName(r1);	 Catch:{ Exception -> 0x0027 }
            r2 = "get";	 Catch:{ Exception -> 0x0027 }
            r3 = 0;	 Catch:{ Exception -> 0x0027 }
            r4 = new java.lang.Class[r3];	 Catch:{ Exception -> 0x0027 }
            r2 = r1.getMethod(r2, r4);	 Catch:{ Exception -> 0x0027 }
            r4 = "open";	 Catch:{ Exception -> 0x0027 }
            r5 = 1;	 Catch:{ Exception -> 0x0027 }
            r5 = new java.lang.Class[r5];	 Catch:{ Exception -> 0x0027 }
            r6 = java.lang.String.class;	 Catch:{ Exception -> 0x0027 }
            r5[r3] = r6;	 Catch:{ Exception -> 0x0027 }
            r4 = r1.getMethod(r4, r5);	 Catch:{ Exception -> 0x0027 }
            r5 = "warnIfOpen";	 Catch:{ Exception -> 0x0027 }
            r3 = new java.lang.Class[r3];	 Catch:{ Exception -> 0x0027 }
            r1 = r1.getMethod(r5, r3);	 Catch:{ Exception -> 0x0027 }
            r0 = r2;
            goto L_0x0029;
        L_0x0027:
            r1 = r0;
            r4 = r1;
        L_0x0029:
            r2 = new okhttp3.internal.platform.AndroidPlatform$CloseGuard;
            r2.<init>(r0, r4, r1);
            return r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.AndroidPlatform.CloseGuard.get():okhttp3.internal.platform.AndroidPlatform$CloseGuard");
        }
    }

    AndroidPlatform(Class<?> cls, OptionalMethod<Socket> optionalMethod, OptionalMethod<Socket> optionalMethod2, OptionalMethod<Socket> optionalMethod3, OptionalMethod<Socket> optionalMethod4) {
        this.sslParametersClass = cls;
        this.setUseSessionTickets = optionalMethod;
        this.setHostname = optionalMethod2;
        this.getAlpnSelectedProtocol = optionalMethod3;
        this.setAlpnProtocols = optionalMethod4;
    }

    public void connectSocket(Socket socket, InetSocketAddress inetSocketAddress, int i) throws IOException {
        try {
            socket.connect(inetSocketAddress, i);
        } catch (Socket socket2) {
            if (Util.isAndroidGetsocknameError(socket2) != null) {
                throw new IOException(socket2);
            }
            throw socket2;
        } catch (Socket socket22) {
            inetSocketAddress = new IOException("Exception in connect");
            inetSocketAddress.initCause(socket22);
            throw inetSocketAddress;
        }
    }

    public javax.net.ssl.X509TrustManager trustManager(javax.net.ssl.SSLSocketFactory r4) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r3 = this;
        r0 = r3.sslParametersClass;
        r1 = "sslParameters";
        r0 = okhttp3.internal.platform.Platform.readFieldOrNull(r4, r0, r1);
        if (r0 != 0) goto L_0x0025;
    L_0x000a:
        r0 = "com.google.android.gms.org.conscrypt.SSLParametersImpl";	 Catch:{ ClassNotFoundException -> 0x0020 }
        r1 = 0;	 Catch:{ ClassNotFoundException -> 0x0020 }
        r2 = r4.getClass();	 Catch:{ ClassNotFoundException -> 0x0020 }
        r2 = r2.getClassLoader();	 Catch:{ ClassNotFoundException -> 0x0020 }
        r0 = java.lang.Class.forName(r0, r1, r2);	 Catch:{ ClassNotFoundException -> 0x0020 }
        r1 = "sslParameters";	 Catch:{ ClassNotFoundException -> 0x0020 }
        r0 = okhttp3.internal.platform.Platform.readFieldOrNull(r4, r0, r1);	 Catch:{ ClassNotFoundException -> 0x0020 }
        goto L_0x0025;
    L_0x0020:
        r4 = super.trustManager(r4);
        return r4;
    L_0x0025:
        r4 = javax.net.ssl.X509TrustManager.class;
        r1 = "x509TrustManager";
        r4 = okhttp3.internal.platform.Platform.readFieldOrNull(r0, r4, r1);
        r4 = (javax.net.ssl.X509TrustManager) r4;
        if (r4 == 0) goto L_0x0032;
    L_0x0031:
        return r4;
    L_0x0032:
        r4 = javax.net.ssl.X509TrustManager.class;
        r1 = "trustManager";
        r4 = okhttp3.internal.platform.Platform.readFieldOrNull(r0, r4, r1);
        r4 = (javax.net.ssl.X509TrustManager) r4;
        return r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.AndroidPlatform.trustManager(javax.net.ssl.SSLSocketFactory):javax.net.ssl.X509TrustManager");
    }

    public void configureTlsExtensions(SSLSocket sSLSocket, String str, List<Protocol> list) {
        if (str != null) {
            this.setUseSessionTickets.invokeOptionalWithoutCheckedException(sSLSocket, Boolean.valueOf(true));
            this.setHostname.invokeOptionalWithoutCheckedException(sSLSocket, str);
        }
        if (this.setAlpnProtocols != null && this.setAlpnProtocols.isSupported(sSLSocket) != null) {
            this.setAlpnProtocols.invokeWithoutCheckedException(sSLSocket, new Object[]{Platform.concatLengthPrefixed(list)});
        }
    }

    public String getSelectedProtocol(SSLSocket sSLSocket) {
        String str = null;
        if (this.getAlpnSelectedProtocol == null || !this.getAlpnSelectedProtocol.isSupported(sSLSocket)) {
            return null;
        }
        byte[] bArr = (byte[]) this.getAlpnSelectedProtocol.invokeWithoutCheckedException(sSLSocket, new Object[0]);
        if (bArr != null) {
            str = new String(bArr, Util.UTF_8);
        }
        return str;
    }

    public void log(int i, String str, Throwable th) {
        int i2 = 5;
        if (i != 5) {
            i2 = 3;
        }
        if (th != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append('\n');
            stringBuilder.append(Log.getStackTraceString(th));
            str = stringBuilder.toString();
        }
        th = null;
        int length = str.length();
        while (th < length) {
            int min;
            int indexOf = str.indexOf(10, th);
            if (indexOf == -1) {
                indexOf = length;
            }
            while (true) {
                min = Math.min(indexOf, th + MAX_LOG_LENGTH);
                Log.println(i2, "OkHttp", str.substring(th, min));
                if (min >= indexOf) {
                    break;
                }
                th = min;
            }
            th = min + 1;
        }
    }

    public Object getStackTraceForCloseable(String str) {
        return this.closeGuard.createAndOpen(str);
    }

    public void logCloseableLeak(String str, Object obj) {
        if (this.closeGuard.warnIfOpen(obj) == null) {
            log(5, str, null);
        }
    }

    public boolean isCleartextTrafficPermitted(java.lang.String r8) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r7 = this;
        r0 = "android.security.NetworkSecurityPolicy";	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r0 = java.lang.Class.forName(r0);	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r1 = "getInstance";	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r2 = 0;	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r3 = new java.lang.Class[r2];	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r1 = r0.getMethod(r1, r3);	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r3 = 0;	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r4 = new java.lang.Object[r2];	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r1 = r1.invoke(r3, r4);	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r3 = "isCleartextTrafficPermitted";	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r4 = 1;	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r5 = new java.lang.Class[r4];	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r6 = java.lang.String.class;	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r5[r2] = r6;	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r0 = r0.getMethod(r3, r5);	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r3 = new java.lang.Object[r4];	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r3[r2] = r8;	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r0 = r0.invoke(r1, r3);	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r0 = (java.lang.Boolean) r0;	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        r0 = r0.booleanValue();	 Catch:{ ClassNotFoundException -> 0x0038, ClassNotFoundException -> 0x0038, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032, IllegalAccessException -> 0x0032 }
        return r0;
    L_0x0032:
        r8 = new java.lang.AssertionError;
        r8.<init>();
        throw r8;
    L_0x0038:
        r8 = super.isCleartextTrafficPermitted(r8);
        return r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.AndroidPlatform.isCleartextTrafficPermitted(java.lang.String):boolean");
    }

    public okhttp3.internal.tls.CertificateChainCleaner buildCertificateChainCleaner(javax.net.ssl.X509TrustManager r8) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r7 = this;
        r0 = "android.net.http.X509TrustManagerExtensions";	 Catch:{ Exception -> 0x0036 }
        r0 = java.lang.Class.forName(r0);	 Catch:{ Exception -> 0x0036 }
        r1 = 1;	 Catch:{ Exception -> 0x0036 }
        r2 = new java.lang.Class[r1];	 Catch:{ Exception -> 0x0036 }
        r3 = javax.net.ssl.X509TrustManager.class;	 Catch:{ Exception -> 0x0036 }
        r4 = 0;	 Catch:{ Exception -> 0x0036 }
        r2[r4] = r3;	 Catch:{ Exception -> 0x0036 }
        r2 = r0.getConstructor(r2);	 Catch:{ Exception -> 0x0036 }
        r3 = new java.lang.Object[r1];	 Catch:{ Exception -> 0x0036 }
        r3[r4] = r8;	 Catch:{ Exception -> 0x0036 }
        r2 = r2.newInstance(r3);	 Catch:{ Exception -> 0x0036 }
        r3 = "checkServerTrusted";	 Catch:{ Exception -> 0x0036 }
        r5 = 3;	 Catch:{ Exception -> 0x0036 }
        r5 = new java.lang.Class[r5];	 Catch:{ Exception -> 0x0036 }
        r6 = java.security.cert.X509Certificate[].class;	 Catch:{ Exception -> 0x0036 }
        r5[r4] = r6;	 Catch:{ Exception -> 0x0036 }
        r4 = java.lang.String.class;	 Catch:{ Exception -> 0x0036 }
        r5[r1] = r4;	 Catch:{ Exception -> 0x0036 }
        r1 = 2;	 Catch:{ Exception -> 0x0036 }
        r4 = java.lang.String.class;	 Catch:{ Exception -> 0x0036 }
        r5[r1] = r4;	 Catch:{ Exception -> 0x0036 }
        r0 = r0.getMethod(r3, r5);	 Catch:{ Exception -> 0x0036 }
        r1 = new okhttp3.internal.platform.AndroidPlatform$AndroidCertificateChainCleaner;	 Catch:{ Exception -> 0x0036 }
        r1.<init>(r2, r0);	 Catch:{ Exception -> 0x0036 }
        return r1;
    L_0x0036:
        r8 = super.buildCertificateChainCleaner(r8);
        return r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.AndroidPlatform.buildCertificateChainCleaner(javax.net.ssl.X509TrustManager):okhttp3.internal.tls.CertificateChainCleaner");
    }

    public static okhttp3.internal.platform.Platform buildIfSupported() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        r1 = "com.android.org.conscrypt.SSLParametersImpl";	 Catch:{ ClassNotFoundException -> 0x0009 }
        r1 = java.lang.Class.forName(r1);	 Catch:{ ClassNotFoundException -> 0x0009 }
    L_0x0007:
        r3 = r1;
        goto L_0x0010;
    L_0x0009:
        r1 = "org.apache.harmony.xnet.provider.jsse.SSLParametersImpl";	 Catch:{ ClassNotFoundException -> 0x0055 }
        r1 = java.lang.Class.forName(r1);	 Catch:{ ClassNotFoundException -> 0x0055 }
        goto L_0x0007;	 Catch:{ ClassNotFoundException -> 0x0055 }
    L_0x0010:
        r4 = new okhttp3.internal.platform.OptionalMethod;	 Catch:{ ClassNotFoundException -> 0x0055 }
        r1 = "setUseSessionTickets";	 Catch:{ ClassNotFoundException -> 0x0055 }
        r2 = 1;	 Catch:{ ClassNotFoundException -> 0x0055 }
        r5 = new java.lang.Class[r2];	 Catch:{ ClassNotFoundException -> 0x0055 }
        r6 = java.lang.Boolean.TYPE;	 Catch:{ ClassNotFoundException -> 0x0055 }
        r7 = 0;	 Catch:{ ClassNotFoundException -> 0x0055 }
        r5[r7] = r6;	 Catch:{ ClassNotFoundException -> 0x0055 }
        r4.<init>(r0, r1, r5);	 Catch:{ ClassNotFoundException -> 0x0055 }
        r5 = new okhttp3.internal.platform.OptionalMethod;	 Catch:{ ClassNotFoundException -> 0x0055 }
        r1 = "setHostname";	 Catch:{ ClassNotFoundException -> 0x0055 }
        r6 = new java.lang.Class[r2];	 Catch:{ ClassNotFoundException -> 0x0055 }
        r8 = java.lang.String.class;	 Catch:{ ClassNotFoundException -> 0x0055 }
        r6[r7] = r8;	 Catch:{ ClassNotFoundException -> 0x0055 }
        r5.<init>(r0, r1, r6);	 Catch:{ ClassNotFoundException -> 0x0055 }
        r1 = "android.net.Network";	 Catch:{ ClassNotFoundException -> 0x004b }
        java.lang.Class.forName(r1);	 Catch:{ ClassNotFoundException -> 0x004b }
        r1 = new okhttp3.internal.platform.OptionalMethod;	 Catch:{ ClassNotFoundException -> 0x004b }
        r6 = byte[].class;	 Catch:{ ClassNotFoundException -> 0x004b }
        r8 = "getAlpnSelectedProtocol";	 Catch:{ ClassNotFoundException -> 0x004b }
        r9 = new java.lang.Class[r7];	 Catch:{ ClassNotFoundException -> 0x004b }
        r1.<init>(r6, r8, r9);	 Catch:{ ClassNotFoundException -> 0x004b }
        r6 = new okhttp3.internal.platform.OptionalMethod;	 Catch:{ ClassNotFoundException -> 0x004c }
        r8 = "setAlpnProtocols";	 Catch:{ ClassNotFoundException -> 0x004c }
        r2 = new java.lang.Class[r2];	 Catch:{ ClassNotFoundException -> 0x004c }
        r9 = byte[].class;	 Catch:{ ClassNotFoundException -> 0x004c }
        r2[r7] = r9;	 Catch:{ ClassNotFoundException -> 0x004c }
        r6.<init>(r0, r8, r2);	 Catch:{ ClassNotFoundException -> 0x004c }
        r7 = r6;
        goto L_0x004d;
    L_0x004b:
        r1 = r0;
    L_0x004c:
        r7 = r0;
    L_0x004d:
        r6 = r1;
        r1 = new okhttp3.internal.platform.AndroidPlatform;	 Catch:{ ClassNotFoundException -> 0x0055 }
        r2 = r1;	 Catch:{ ClassNotFoundException -> 0x0055 }
        r2.<init>(r3, r4, r5, r6, r7);	 Catch:{ ClassNotFoundException -> 0x0055 }
        return r1;
    L_0x0055:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.AndroidPlatform.buildIfSupported():okhttp3.internal.platform.Platform");
    }
}
