package okhttp3.internal.platform;

import java.lang.reflect.Method;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

final class Jdk9Platform extends Platform {
    final Method getProtocolMethod;
    final Method setProtocolMethod;

    Jdk9Platform(Method method, Method method2) {
        this.setProtocolMethod = method;
        this.getProtocolMethod = method2;
    }

    public void configureTlsExtensions(javax.net.ssl.SSLSocket r5, java.lang.String r6, java.util.List<okhttp3.Protocol> r7) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r4 = this;
        r6 = r5.getSSLParameters();	 Catch:{ IllegalAccessException -> 0x0021, IllegalAccessException -> 0x0021 }
        r7 = okhttp3.internal.platform.Platform.alpnProtocolNames(r7);	 Catch:{ IllegalAccessException -> 0x0021, IllegalAccessException -> 0x0021 }
        r0 = r4.setProtocolMethod;	 Catch:{ IllegalAccessException -> 0x0021, IllegalAccessException -> 0x0021 }
        r1 = 1;	 Catch:{ IllegalAccessException -> 0x0021, IllegalAccessException -> 0x0021 }
        r1 = new java.lang.Object[r1];	 Catch:{ IllegalAccessException -> 0x0021, IllegalAccessException -> 0x0021 }
        r2 = 0;	 Catch:{ IllegalAccessException -> 0x0021, IllegalAccessException -> 0x0021 }
        r3 = r7.size();	 Catch:{ IllegalAccessException -> 0x0021, IllegalAccessException -> 0x0021 }
        r3 = new java.lang.String[r3];	 Catch:{ IllegalAccessException -> 0x0021, IllegalAccessException -> 0x0021 }
        r7 = r7.toArray(r3);	 Catch:{ IllegalAccessException -> 0x0021, IllegalAccessException -> 0x0021 }
        r1[r2] = r7;	 Catch:{ IllegalAccessException -> 0x0021, IllegalAccessException -> 0x0021 }
        r0.invoke(r6, r1);	 Catch:{ IllegalAccessException -> 0x0021, IllegalAccessException -> 0x0021 }
        r5.setSSLParameters(r6);	 Catch:{ IllegalAccessException -> 0x0021, IllegalAccessException -> 0x0021 }
        return;
    L_0x0021:
        r5 = new java.lang.AssertionError;
        r5.<init>();
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.Jdk9Platform.configureTlsExtensions(javax.net.ssl.SSLSocket, java.lang.String, java.util.List):void");
    }

    public java.lang.String getSelectedProtocol(javax.net.ssl.SSLSocket r3) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r2 = this;
        r0 = r2.getProtocolMethod;	 Catch:{ IllegalAccessException -> 0x0019, IllegalAccessException -> 0x0019 }
        r1 = 0;	 Catch:{ IllegalAccessException -> 0x0019, IllegalAccessException -> 0x0019 }
        r1 = new java.lang.Object[r1];	 Catch:{ IllegalAccessException -> 0x0019, IllegalAccessException -> 0x0019 }
        r3 = r0.invoke(r3, r1);	 Catch:{ IllegalAccessException -> 0x0019, IllegalAccessException -> 0x0019 }
        r3 = (java.lang.String) r3;	 Catch:{ IllegalAccessException -> 0x0019, IllegalAccessException -> 0x0019 }
        if (r3 == 0) goto L_0x0017;	 Catch:{ IllegalAccessException -> 0x0019, IllegalAccessException -> 0x0019 }
    L_0x000d:
        r0 = "";	 Catch:{ IllegalAccessException -> 0x0019, IllegalAccessException -> 0x0019 }
        r0 = r3.equals(r0);	 Catch:{ IllegalAccessException -> 0x0019, IllegalAccessException -> 0x0019 }
        if (r0 == 0) goto L_0x0016;
    L_0x0015:
        goto L_0x0017;
    L_0x0016:
        return r3;
    L_0x0017:
        r3 = 0;
        return r3;
    L_0x0019:
        r3 = new java.lang.AssertionError;
        r3.<init>();
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.Jdk9Platform.getSelectedProtocol(javax.net.ssl.SSLSocket):java.lang.String");
    }

    public X509TrustManager trustManager(SSLSocketFactory sSLSocketFactory) {
        throw new UnsupportedOperationException("clientBuilder.sslSocketFactory(SSLSocketFactory) not supported on JDK 9+");
    }

    public static okhttp3.internal.platform.Jdk9Platform buildIfSupported() {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r0 = javax.net.ssl.SSLParameters.class;	 Catch:{ NoSuchMethodException -> 0x0020 }
        r1 = "setApplicationProtocols";	 Catch:{ NoSuchMethodException -> 0x0020 }
        r2 = 1;	 Catch:{ NoSuchMethodException -> 0x0020 }
        r2 = new java.lang.Class[r2];	 Catch:{ NoSuchMethodException -> 0x0020 }
        r3 = java.lang.String[].class;	 Catch:{ NoSuchMethodException -> 0x0020 }
        r4 = 0;	 Catch:{ NoSuchMethodException -> 0x0020 }
        r2[r4] = r3;	 Catch:{ NoSuchMethodException -> 0x0020 }
        r0 = r0.getMethod(r1, r2);	 Catch:{ NoSuchMethodException -> 0x0020 }
        r1 = javax.net.ssl.SSLSocket.class;	 Catch:{ NoSuchMethodException -> 0x0020 }
        r2 = "getApplicationProtocol";	 Catch:{ NoSuchMethodException -> 0x0020 }
        r3 = new java.lang.Class[r4];	 Catch:{ NoSuchMethodException -> 0x0020 }
        r1 = r1.getMethod(r2, r3);	 Catch:{ NoSuchMethodException -> 0x0020 }
        r2 = new okhttp3.internal.platform.Jdk9Platform;	 Catch:{ NoSuchMethodException -> 0x0020 }
        r2.<init>(r0, r1);	 Catch:{ NoSuchMethodException -> 0x0020 }
        return r2;
    L_0x0020:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.platform.Jdk9Platform.buildIfSupported():okhttp3.internal.platform.Jdk9Platform");
    }
}
