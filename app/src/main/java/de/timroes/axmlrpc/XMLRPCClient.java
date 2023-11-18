package de.timroes.axmlrpc;

import de.timroes.axmlrpc.serializer.SerializerHandler;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class XMLRPCClient {
    static final String CONTENT_LENGTH = "Content-Length";
    static final String CONTENT_TYPE = "Content-Type";
    private static final String DEFAULT_USER_AGENT = "aXMLRPC";
    static final String FAULT = "fault";
    public static final int FLAGS_8BYTE_INT = 2;
    public static final int FLAGS_APACHE_WS = 776;
    public static final int FLAGS_DEFAULT_TYPE_STRING = 256;
    public static final int FLAGS_ENABLE_COOKIES = 4;
    public static final int FLAGS_FORWARD = 32;
    public static final int FLAGS_IGNORE_NAMESPACES = 512;
    public static final int FLAGS_IGNORE_STATUSCODE = 16;
    public static final int FLAGS_NIL = 8;
    public static final int FLAGS_NONE = 0;
    public static final int FLAGS_NO_STRING_DECODE = 2048;
    public static final int FLAGS_NO_STRING_ENCODE = 4096;
    public static final int FLAGS_SSL_IGNORE_ERRORS = 192;
    public static final int FLAGS_SSL_IGNORE_INVALID_CERT = 128;
    public static final int FLAGS_SSL_IGNORE_INVALID_HOST = 64;
    public static final int FLAGS_STRICT = 1;
    public static final int FLAGS_USE_SYSTEM_PROXY = 1024;
    static final String HOST = "Host";
    static final String HTTP_POST = "POST";
    static final String METHOD_CALL = "methodCall";
    static final String METHOD_NAME = "methodName";
    static final String METHOD_RESPONSE = "methodResponse";
    static final String PARAM = "param";
    static final String PARAMS = "params";
    static final String STRUCT_MEMBER = "member";
    static final String TYPE_XML = "text/xml; charset=utf-8";
    static final String USER_AGENT = "User-Agent";
    public static final String VALUE = "value";
    private AuthenticationManager authManager;
    private Map<Long, Caller> backgroundCalls;
    private CookieManager cookieManager;
    private final int flags;
    private Map<String, String> httpParameters;
    private KeyManager[] keyManagers;
    private Proxy proxy;
    private ResponseParser responseParser;
    private int timeout;
    private TrustManager[] trustManagers;
    private URL url;

    /* renamed from: de.timroes.axmlrpc.XMLRPCClient$1 */
    class C13661 implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        C13661() {
        }
    }

    private class Caller extends Thread {
        private volatile boolean canceled;
        private HttpURLConnection http;
        private XMLRPCCallback listener;
        private String methodName;
        private Object[] params;
        private long threadId;

        /* renamed from: de.timroes.axmlrpc.XMLRPCClient$Caller$1 */
        class C13671 implements HostnameVerifier {
            public boolean verify(String str, SSLSession sSLSession) {
                return true;
            }

            C13671() {
            }
        }

        public Caller(XMLRPCCallback xMLRPCCallback, long j, String str, Object[] objArr) {
            this.listener = xMLRPCCallback;
            this.threadId = j;
            this.methodName = str;
            this.params = objArr;
        }

        public void run() {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r4 = this;
            r0 = r4.listener;
            if (r0 != 0) goto L_0x0005;
        L_0x0004:
            return;
        L_0x0005:
            r0 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ CancelException -> 0x0048, XMLRPCServerException -> 0x002f, XMLRPCException -> 0x0026 }
            r0 = r0.backgroundCalls;	 Catch:{ CancelException -> 0x0048, XMLRPCServerException -> 0x002f, XMLRPCException -> 0x0026 }
            r1 = r4.threadId;	 Catch:{ CancelException -> 0x0048, XMLRPCServerException -> 0x002f, XMLRPCException -> 0x0026 }
            r1 = java.lang.Long.valueOf(r1);	 Catch:{ CancelException -> 0x0048, XMLRPCServerException -> 0x002f, XMLRPCException -> 0x0026 }
            r0.put(r1, r4);	 Catch:{ CancelException -> 0x0048, XMLRPCServerException -> 0x002f, XMLRPCException -> 0x0026 }
            r0 = r4.methodName;	 Catch:{ CancelException -> 0x0048, XMLRPCServerException -> 0x002f, XMLRPCException -> 0x0026 }
            r1 = r4.params;	 Catch:{ CancelException -> 0x0048, XMLRPCServerException -> 0x002f, XMLRPCException -> 0x0026 }
            r0 = r4.call(r0, r1);	 Catch:{ CancelException -> 0x0048, XMLRPCServerException -> 0x002f, XMLRPCException -> 0x0026 }
            r1 = r4.listener;	 Catch:{ CancelException -> 0x0048, XMLRPCServerException -> 0x002f, XMLRPCException -> 0x0026 }
            r2 = r4.threadId;	 Catch:{ CancelException -> 0x0048, XMLRPCServerException -> 0x002f, XMLRPCException -> 0x0026 }
            r1.onResponse(r2, r0);	 Catch:{ CancelException -> 0x0048, XMLRPCServerException -> 0x002f, XMLRPCException -> 0x0026 }
            goto L_0x0048;
        L_0x0024:
            r0 = move-exception;
            goto L_0x0038;
        L_0x0026:
            r0 = move-exception;
            r1 = r4.listener;	 Catch:{ all -> 0x0024 }
            r2 = r4.threadId;	 Catch:{ all -> 0x0024 }
            r1.onError(r2, r0);	 Catch:{ all -> 0x0024 }
            goto L_0x0048;	 Catch:{ all -> 0x0024 }
        L_0x002f:
            r0 = move-exception;	 Catch:{ all -> 0x0024 }
            r1 = r4.listener;	 Catch:{ all -> 0x0024 }
            r2 = r4.threadId;	 Catch:{ all -> 0x0024 }
            r1.onServerError(r2, r0);	 Catch:{ all -> 0x0024 }
            goto L_0x0048;
        L_0x0038:
            r1 = de.timroes.axmlrpc.XMLRPCClient.this;
            r1 = r1.backgroundCalls;
            r2 = r4.threadId;
            r2 = java.lang.Long.valueOf(r2);
            r1.remove(r2);
            throw r0;
        L_0x0048:
            r0 = de.timroes.axmlrpc.XMLRPCClient.this;
            r0 = r0.backgroundCalls;
            r1 = r4.threadId;
            r1 = java.lang.Long.valueOf(r1);
            r0.remove(r1);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: de.timroes.axmlrpc.XMLRPCClient.Caller.run():void");
        }

        public void cancel() {
            this.canceled = true;
            this.http.disconnect();
        }

        public java.lang.Object call(java.lang.String r8, java.lang.Object[] r9) throws de.timroes.axmlrpc.XMLRPCException {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r7 = this;
            r0 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r0 = r0.createCall(r8, r9);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.proxy;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            if (r1 == 0) goto L_0x001f;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x000e:
            r1 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.url;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r2 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r2 = r2.proxy;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.openConnection(r2);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            goto L_0x0029;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x001f:
            r1 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.url;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.openConnection();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x0029:
            r1 = r7.verifyConnection(r1);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r7.http = r1;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r2 = 0;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1.setInstanceFollowRedirects(r2);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r3 = "POST";	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1.setRequestMethod(r3);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r3 = 1;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1.setDoOutput(r3);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1.setDoInput(r3);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.timeout;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            if (r1 <= 0) goto L_0x0069;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x004f:
            r1 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = r4.timeout;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = r4 * 1000;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1.setConnectTimeout(r4);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = r4.timeout;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = r4 * 1000;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1.setReadTimeout(r4);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x0069:
            r1 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.httpParameters;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.entrySet();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.iterator();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x0077:
            r4 = r1.hasNext();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            if (r4 == 0) goto L_0x0095;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x007d:
            r4 = r1.next();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = (java.util.Map.Entry) r4;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r5 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r6 = r4.getKey();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r6 = (java.lang.String) r6;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = r4.getValue();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = (java.lang.String) r4;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r5.setRequestProperty(r6, r4);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            goto L_0x0077;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x0095:
            r1 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.authManager;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1.setAuthentication(r4);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.cookieManager;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1.setCookies(r4);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = new java.io.OutputStreamWriter;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = r4.getOutputStream();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1.<init>(r4);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r0 = r0.getXML();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1.write(r0);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1.flush();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1.close();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r0 = r7.http;	 Catch:{ IOException -> 0x00ca, SocketTimeoutException -> 0x01c8 }
            r0 = r0.getResponseCode();	 Catch:{ IOException -> 0x00ca, SocketTimeoutException -> 0x01c8 }
            goto L_0x00d0;
        L_0x00ca:
            r0 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r0 = r0.getResponseCode();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x00d0:
            r1 = 403; // 0x193 float:5.65E-43 double:1.99E-321;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = 16;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            if (r0 == r1) goto L_0x00e2;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x00d6:
            r1 = 401; // 0x191 float:5.62E-43 double:1.98E-321;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            if (r0 != r1) goto L_0x00db;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x00da:
            goto L_0x00e2;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x00db:
            r1 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.getInputStream();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            goto L_0x00f0;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x00e2:
            r1 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.isFlagSet(r4);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            if (r1 == 0) goto L_0x018f;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x00ea:
            r1 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.getErrorStream();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x00f0:
            r5 = 301; // 0x12d float:4.22E-43 double:1.487E-321;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r6 = 302; // 0x12e float:4.23E-43 double:1.49E-321;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            if (r0 == r5) goto L_0x0141;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x00f6:
            if (r0 != r6) goto L_0x00f9;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x00f8:
            goto L_0x0141;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x00f9:
            r8 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r8 = r8.isFlagSet(r4);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            if (r8 != 0) goto L_0x010d;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x0101:
            r8 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            if (r0 == r8) goto L_0x010d;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x0105:
            r8 = new de.timroes.axmlrpc.XMLRPCException;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r9 = "The status code of the http response must be 200.";	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r8.<init>(r9);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            throw r8;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x010d:
            r8 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r8 = r8.isFlagSet(r3);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            if (r8 == 0) goto L_0x012b;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x0115:
            r8 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r8 = r8.getContentType();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r9 = "text/xml; charset=utf-8";	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r8 = r8.startsWith(r9);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            if (r8 != 0) goto L_0x012b;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x0123:
            r8 = new de.timroes.axmlrpc.XMLRPCException;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r9 = "The Content-Type of the response must be text/xml.";	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r8.<init>(r9);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            throw r8;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x012b:
            r8 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r8 = r8.cookieManager;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r9 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r8.readCookies(r9);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r8 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r8 = r8.responseParser;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r8 = r8.parse(r1);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            return r8;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x0141:
            r1 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = 32;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.isFlagSet(r4);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            if (r1 == 0) goto L_0x0187;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x014b:
            if (r0 != r6) goto L_0x014e;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x014d:
            r2 = 1;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x014e:
            r0 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = "Location";	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r0 = r0.getHeaderField(r1);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            if (r0 == 0) goto L_0x015e;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x0158:
            r1 = r0.length();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            if (r1 > 0) goto L_0x0166;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x015e:
            r0 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = "location";	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r0 = r0.getHeaderField(r1);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x0166:
            r1 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = r1.url;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r3 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4 = new java.net.URL;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r4.<init>(r0);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r3.url = r4;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r0 = r7.http;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r0.disconnect();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r8 = r7.call(r8, r9);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            if (r2 == 0) goto L_0x0186;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x0181:
            r9 = de.timroes.axmlrpc.XMLRPCClient.this;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r9.url = r1;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x0186:
            return r8;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x0187:
            r8 = new de.timroes.axmlrpc.XMLRPCException;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r9 = "The server responded with a http 301 or 302 status code, but forwarding has not been enabled (FLAGS_FORWARD).";	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r8.<init>(r9);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            throw r8;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x018f:
            r8 = new de.timroes.axmlrpc.XMLRPCException;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r9 = new java.lang.StringBuilder;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r9.<init>();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r1 = "Invalid status code '";	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r9.append(r1);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r9.append(r0);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r0 = "' returned from server.";	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r9.append(r0);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r9 = r9.toString();	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            r8.<init>(r9);	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
            throw r8;	 Catch:{ SocketTimeoutException -> 0x01c8, IOException -> 0x01ab }
        L_0x01ab:
            r8 = move-exception;
            r9 = r7.canceled;
            if (r9 == 0) goto L_0x01c2;
        L_0x01b0:
            r0 = r7.threadId;
            r2 = 0;
            r9 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
            if (r9 > 0) goto L_0x01b9;
        L_0x01b8:
            goto L_0x01c2;
        L_0x01b9:
            r8 = new de.timroes.axmlrpc.XMLRPCClient$CancelException;
            r9 = de.timroes.axmlrpc.XMLRPCClient.this;
            r0 = 0;
            r8.<init>();
            throw r8;
        L_0x01c2:
            r9 = new de.timroes.axmlrpc.XMLRPCException;
            r9.<init>(r8);
            throw r9;
        L_0x01c8:
            r8 = new de.timroes.axmlrpc.XMLRPCTimeoutException;
            r9 = "The XMLRPC call timed out.";
            r8.<init>(r9);
            throw r8;
            */
            throw new UnsupportedOperationException("Method not decompiled: de.timroes.axmlrpc.XMLRPCClient.Caller.call(java.lang.String, java.lang.Object[]):java.lang.Object");
        }

        private HttpURLConnection verifyConnection(URLConnection uRLConnection) throws XMLRPCException {
            if (!(uRLConnection instanceof HttpURLConnection)) {
                throw new IllegalArgumentException("The URL is not valid for a http connection.");
            } else if (!(uRLConnection instanceof HttpsURLConnection)) {
                return (HttpURLConnection) uRLConnection;
            } else {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) uRLConnection;
                if (XMLRPCClient.this.isFlagSet(64)) {
                    httpsURLConnection.setHostnameVerifier(new C13671());
                }
                if (XMLRPCClient.this.trustManagers != null) {
                    try {
                        for (String instance : new String[]{"TLS", "SSL"}) {
                            SSLContext instance2 = SSLContext.getInstance(instance);
                            instance2.init(XMLRPCClient.this.keyManagers, XMLRPCClient.this.trustManagers, new SecureRandom());
                            httpsURLConnection.setSSLSocketFactory(instance2.getSocketFactory());
                        }
                    } catch (Exception e) {
                        throw new XMLRPCException(e);
                    }
                }
                return httpsURLConnection;
            }
        }
    }

    private class CancelException extends RuntimeException {
        private CancelException() {
        }
    }

    public XMLRPCClient(URL url, String str, int i) {
        this.httpParameters = new ConcurrentHashMap();
        this.backgroundCalls = new ConcurrentHashMap();
        SerializerHandler.initialize(i);
        this.url = url;
        this.flags = i;
        this.responseParser = new ResponseParser();
        this.cookieManager = new CookieManager(i);
        this.authManager = new AuthenticationManager();
        this.httpParameters.put("Content-Type", TYPE_XML);
        this.httpParameters.put("User-Agent", str);
        if (isFlagSet(128) != null) {
            this.trustManagers = new TrustManager[]{new C13661()};
        }
        if (isFlagSet(1024) != null) {
            url = System.getProperties();
            str = url.getProperty("http.proxyHost");
            url = Integer.parseInt(url.getProperty("http.proxyPort", "0"));
            if (url > null && str.length() > 0 && str.equals("null") == 0) {
                this.proxy = new Proxy(Type.HTTP, new InetSocketAddress(str, url));
            }
        }
    }

    public XMLRPCClient(URL url, int i) {
        this(url, DEFAULT_USER_AGENT, i);
    }

    public XMLRPCClient(URL url, String str) {
        this(url, str, 0);
    }

    public XMLRPCClient(URL url) {
        this(url, DEFAULT_USER_AGENT, 0);
    }

    public URL getURL() {
        return this.url;
    }

    public void setTimeout(int i) {
        this.timeout = i;
    }

    public void setUserAgentString(String str) {
        this.httpParameters.put("User-Agent", str);
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public void setCustomHttpHeader(String str, String str2) {
        if (!("Content-Type".equals(str) || "Host".equals(str))) {
            if (!"Content-Length".equals(str)) {
                this.httpParameters.put(str, str2);
                return;
            }
        }
        throw new XMLRPCRuntimeException("You cannot modify the Host, Content-Type or Content-Length header.");
    }

    public void setLoginData(String str, String str2) {
        this.authManager.setAuthData(str, str2);
    }

    public void clearLoginData() {
        this.authManager.clearAuthData();
    }

    public Map<String, String> getCookies() {
        return this.cookieManager.getCookies();
    }

    public void clearCookies() {
        this.cookieManager.clearCookies();
    }

    public void installCustomTrustManager(TrustManager trustManager) {
        if (!isFlagSet(128)) {
            this.trustManagers = new TrustManager[]{trustManager};
        }
    }

    public void installCustomTrustManagers(TrustManager[] trustManagerArr) {
        if (!isFlagSet(128)) {
            this.trustManagers = (TrustManager[]) trustManagerArr.clone();
        }
    }

    public void installCustomKeyManager(KeyManager keyManager) {
        if (!isFlagSet(128)) {
            this.keyManagers = new KeyManager[]{keyManager};
        }
    }

    public void installCustomKeyManagers(KeyManager[] keyManagerArr) {
        if (!isFlagSet(128)) {
            this.keyManagers = (KeyManager[]) keyManagerArr.clone();
        }
    }

    public Object call(String str, Object... objArr) throws XMLRPCException {
        return new Caller().call(str, objArr);
    }

    public long callAsync(XMLRPCCallback xMLRPCCallback, String str, Object... objArr) {
        long currentTimeMillis = System.currentTimeMillis();
        new Caller(xMLRPCCallback, currentTimeMillis, str, objArr).start();
        return currentTimeMillis;
    }

    public void cancel(long r2) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r1 = this;
        r0 = r1.backgroundCalls;
        r2 = java.lang.Long.valueOf(r2);
        r2 = r0.get(r2);
        r2 = (de.timroes.axmlrpc.XMLRPCClient.Caller) r2;
        if (r2 != 0) goto L_0x000f;
    L_0x000e:
        return;
    L_0x000f:
        r2.cancel();
        r2.join();	 Catch:{ InterruptedException -> 0x0015 }
    L_0x0015:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: de.timroes.axmlrpc.XMLRPCClient.cancel(long):void");
    }

    private Call createCall(String str, Object[] objArr) {
        if (!isFlagSet(1) || str.matches("^[A-Za-z0-9\\._:/]*$")) {
            return new Call(str, objArr);
        }
        throw new XMLRPCRuntimeException("Method name must only contain A-Z a-z . : _ / ");
    }

    private boolean isFlagSet(int i) {
        return (i & this.flags) != 0;
    }
}
