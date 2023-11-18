package org.jsoup.helper;

import android.support.graphics.drawable.PathInterpolatorCompat;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.google.common.net.HttpHeaders;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.text.Typography;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.HttpStatusException;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TokenQueue;

public class HttpConnection implements Connection {
    public static final String CONTENT_ENCODING = "Content-Encoding";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    private static final int HTTP_TEMP_REDIR = 307;
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private org.jsoup.Connection.Request req = new Request();
    private org.jsoup.Connection.Response res = new Response();

    private static abstract class Base<T extends org.jsoup.Connection.Base> implements org.jsoup.Connection.Base<T> {
        Map<String, String> cookies;
        Map<String, String> headers;
        Method method;
        URL url;

        private Base() {
            this.headers = new LinkedHashMap();
            this.cookies = new LinkedHashMap();
        }

        public URL url() {
            return this.url;
        }

        public T url(URL url) {
            Validate.notNull(url, "URL must not be null");
            this.url = url;
            return this;
        }

        public Method method() {
            return this.method;
        }

        public T method(Method method) {
            Validate.notNull(method, "Method must not be null");
            this.method = method;
            return this;
        }

        public String header(String str) {
            Validate.notNull(str, "Header name must not be null");
            return getHeaderCaseInsensitive(str);
        }

        public T header(String str, String str2) {
            Validate.notEmpty(str, "Header name must not be empty");
            Validate.notNull(str2, "Header value must not be null");
            removeHeader(str);
            this.headers.put(str, str2);
            return this;
        }

        public boolean hasHeader(String str) {
            Validate.notEmpty(str, "Header name must not be empty");
            return getHeaderCaseInsensitive(str) != null ? true : null;
        }

        public boolean hasHeaderWithValue(String str, String str2) {
            return (!hasHeader(str) || header(str).equalsIgnoreCase(str2) == null) ? null : true;
        }

        public T removeHeader(String str) {
            Validate.notEmpty(str, "Header name must not be empty");
            str = scanHeaders(str);
            if (str != null) {
                this.headers.remove(str.getKey());
            }
            return this;
        }

        public Map<String, String> headers() {
            return this.headers;
        }

        private String getHeaderCaseInsensitive(String str) {
            Validate.notNull(str, "Header name must not be null");
            String str2 = (String) this.headers.get(str);
            if (str2 == null) {
                str2 = (String) this.headers.get(str.toLowerCase());
            }
            if (str2 != null) {
                return str2;
            }
            str = scanHeaders(str);
            return str != null ? (String) str.getValue() : str2;
        }

        private Entry<String, String> scanHeaders(String str) {
            str = str.toLowerCase();
            for (Entry<String, String> entry : this.headers.entrySet()) {
                if (((String) entry.getKey()).toLowerCase().equals(str)) {
                    return entry;
                }
            }
            return null;
        }

        public String cookie(String str) {
            Validate.notEmpty(str, "Cookie name must not be empty");
            return (String) this.cookies.get(str);
        }

        public T cookie(String str, String str2) {
            Validate.notEmpty(str, "Cookie name must not be empty");
            Validate.notNull(str2, "Cookie value must not be null");
            this.cookies.put(str, str2);
            return this;
        }

        public boolean hasCookie(String str) {
            Validate.notEmpty(str, "Cookie name must not be empty");
            return this.cookies.containsKey(str);
        }

        public T removeCookie(String str) {
            Validate.notEmpty(str, "Cookie name must not be empty");
            this.cookies.remove(str);
            return this;
        }

        public Map<String, String> cookies() {
            return this.cookies;
        }
    }

    public static class KeyVal implements org.jsoup.Connection.KeyVal {
        private String key;
        private InputStream stream;
        private String value;

        public static KeyVal create(String str, String str2) {
            return new KeyVal().key(str).value(str2);
        }

        public static KeyVal create(String str, String str2, InputStream inputStream) {
            return new KeyVal().key(str).value(str2).inputStream(inputStream);
        }

        private KeyVal() {
        }

        public KeyVal key(String str) {
            Validate.notEmpty(str, "Data key must not be empty");
            this.key = str;
            return this;
        }

        public String key() {
            return this.key;
        }

        public KeyVal value(String str) {
            Validate.notNull(str, "Data value must not be null");
            this.value = str;
            return this;
        }

        public String value() {
            return this.value;
        }

        public KeyVal inputStream(InputStream inputStream) {
            Validate.notNull(this.value, "Data input stream must not be null");
            this.stream = inputStream;
            return this;
        }

        public InputStream inputStream() {
            return this.stream;
        }

        public boolean hasInputStream() {
            return this.stream != null;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.key);
            stringBuilder.append("=");
            stringBuilder.append(this.value);
            return stringBuilder.toString();
        }
    }

    public static class Request extends Base<org.jsoup.Connection.Request> implements org.jsoup.Connection.Request {
        private String body;
        private Collection<org.jsoup.Connection.KeyVal> data;
        private boolean followRedirects;
        private boolean ignoreContentType;
        private boolean ignoreHttpErrors;
        private int maxBodySizeBytes;
        private Parser parser;
        private boolean parserDefined;
        private String postDataCharset;
        private Proxy proxy;
        private int timeoutMilliseconds;
        private boolean validateTSLCertificates;

        public /* bridge */ /* synthetic */ String cookie(String str) {
            return super.cookie(str);
        }

        public /* bridge */ /* synthetic */ Map cookies() {
            return super.cookies();
        }

        public /* bridge */ /* synthetic */ boolean hasCookie(String str) {
            return super.hasCookie(str);
        }

        public /* bridge */ /* synthetic */ boolean hasHeader(String str) {
            return super.hasHeader(str);
        }

        public /* bridge */ /* synthetic */ boolean hasHeaderWithValue(String str, String str2) {
            return super.hasHeaderWithValue(str, str2);
        }

        public /* bridge */ /* synthetic */ String header(String str) {
            return super.header(str);
        }

        public /* bridge */ /* synthetic */ Map headers() {
            return super.headers();
        }

        public /* bridge */ /* synthetic */ Method method() {
            return super.method();
        }

        public /* bridge */ /* synthetic */ URL url() {
            return super.url();
        }

        private Request() {
            super();
            this.body = null;
            this.ignoreHttpErrors = false;
            this.ignoreContentType = false;
            this.parserDefined = false;
            this.validateTSLCertificates = true;
            this.postDataCharset = "UTF-8";
            this.timeoutMilliseconds = PathInterpolatorCompat.MAX_NUM_POINTS;
            this.maxBodySizeBytes = 1048576;
            this.followRedirects = true;
            this.data = new ArrayList();
            this.method = Method.GET;
            this.headers.put(HttpHeaders.ACCEPT_ENCODING, "gzip");
            this.parser = Parser.htmlParser();
        }

        public Proxy proxy() {
            return this.proxy;
        }

        public Request proxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public Request proxy(String str, int i) {
            this.proxy = new Proxy(Type.HTTP, InetSocketAddress.createUnresolved(str, i));
            return this;
        }

        public int timeout() {
            return this.timeoutMilliseconds;
        }

        public Request timeout(int i) {
            Validate.isTrue(i >= 0, "Timeout milliseconds must be 0 (infinite) or greater");
            this.timeoutMilliseconds = i;
            return this;
        }

        public int maxBodySize() {
            return this.maxBodySizeBytes;
        }

        public org.jsoup.Connection.Request maxBodySize(int i) {
            Validate.isTrue(i >= 0, "maxSize must be 0 (unlimited) or larger");
            this.maxBodySizeBytes = i;
            return this;
        }

        public boolean followRedirects() {
            return this.followRedirects;
        }

        public org.jsoup.Connection.Request followRedirects(boolean z) {
            this.followRedirects = z;
            return this;
        }

        public boolean ignoreHttpErrors() {
            return this.ignoreHttpErrors;
        }

        public boolean validateTLSCertificates() {
            return this.validateTSLCertificates;
        }

        public void validateTLSCertificates(boolean z) {
            this.validateTSLCertificates = z;
        }

        public org.jsoup.Connection.Request ignoreHttpErrors(boolean z) {
            this.ignoreHttpErrors = z;
            return this;
        }

        public boolean ignoreContentType() {
            return this.ignoreContentType;
        }

        public org.jsoup.Connection.Request ignoreContentType(boolean z) {
            this.ignoreContentType = z;
            return this;
        }

        public Request data(org.jsoup.Connection.KeyVal keyVal) {
            Validate.notNull(keyVal, "Key val must not be null");
            this.data.add(keyVal);
            return this;
        }

        public Collection<org.jsoup.Connection.KeyVal> data() {
            return this.data;
        }

        public org.jsoup.Connection.Request requestBody(String str) {
            this.body = str;
            return this;
        }

        public String requestBody() {
            return this.body;
        }

        public Request parser(Parser parser) {
            this.parser = parser;
            this.parserDefined = true;
            return this;
        }

        public Parser parser() {
            return this.parser;
        }

        public org.jsoup.Connection.Request postDataCharset(String str) {
            Validate.notNull(str, "Charset must not be null");
            if (Charset.isSupported(str)) {
                this.postDataCharset = str;
                return this;
            }
            throw new IllegalCharsetNameException(str);
        }

        public String postDataCharset() {
            return this.postDataCharset;
        }
    }

    public static class Response extends Base<org.jsoup.Connection.Response> implements org.jsoup.Connection.Response {
        private static final String LOCATION = "Location";
        private static final int MAX_REDIRECTS = 20;
        private static SSLSocketFactory sslSocketFactory;
        private static final Pattern xmlContentTypeRxp = Pattern.compile("(application|text)/\\w*\\+?xml.*");
        private ByteBuffer byteData;
        private String charset;
        private String contentType;
        private boolean executed = false;
        private int numRedirects = 0;
        private org.jsoup.Connection.Request req;
        private int statusCode;
        private String statusMessage;

        /* renamed from: org.jsoup.helper.HttpConnection$Response$1 */
        static class C14801 implements HostnameVerifier {
            public boolean verify(String str, SSLSession sSLSession) {
                return true;
            }

            C14801() {
            }
        }

        /* renamed from: org.jsoup.helper.HttpConnection$Response$2 */
        static class C14812 implements X509TrustManager {
            public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
            }

            public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            C14812() {
            }
        }

        public /* bridge */ /* synthetic */ String cookie(String str) {
            return super.cookie(str);
        }

        public /* bridge */ /* synthetic */ Map cookies() {
            return super.cookies();
        }

        public /* bridge */ /* synthetic */ boolean hasCookie(String str) {
            return super.hasCookie(str);
        }

        public /* bridge */ /* synthetic */ boolean hasHeader(String str) {
            return super.hasHeader(str);
        }

        public /* bridge */ /* synthetic */ boolean hasHeaderWithValue(String str, String str2) {
            return super.hasHeaderWithValue(str, str2);
        }

        public /* bridge */ /* synthetic */ String header(String str) {
            return super.header(str);
        }

        public /* bridge */ /* synthetic */ Map headers() {
            return super.headers();
        }

        public /* bridge */ /* synthetic */ Method method() {
            return super.method();
        }

        public /* bridge */ /* synthetic */ URL url() {
            return super.url();
        }

        Response() {
            super();
        }

        private Response(Response response) throws IOException {
            super();
            if (response != null) {
                this.numRedirects = response.numRedirects + 1;
                if (this.numRedirects >= 20) {
                    throw new IOException(String.format("Too many redirects occurred trying to load URL %s", new Object[]{response.url()}));
                }
            }
        }

        static Response execute(org.jsoup.Connection.Request request) throws IOException {
            return execute(request, null);
        }

        static Response execute(org.jsoup.Connection.Request request, Response response) throws IOException {
            Validate.notNull(request, "Request must not be null");
            String protocol = request.url().getProtocol();
            if (protocol.equals("http") || protocol.equals("https")) {
                HttpURLConnection createConnection;
                int responseCode;
                Response response2;
                boolean hasBody = request.method().hasBody();
                boolean z = request.requestBody() != null;
                if (!hasBody) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Cannot set a request body for HTTP method ");
                    stringBuilder.append(request.method());
                    Validate.isFalse(z, stringBuilder.toString());
                }
                InputStream inputStream = null;
                if (request.data().size() > 0 && (!hasBody || z)) {
                    serialiseRequestUrl(request);
                } else if (hasBody) {
                    protocol = setOutputContentType(request);
                    createConnection = createConnection(request);
                    createConnection.connect();
                    if (createConnection.getDoOutput()) {
                        writePost(request, createConnection.getOutputStream(), protocol);
                    }
                    responseCode = createConnection.getResponseCode();
                    response2 = new Response(response);
                    response2.setupFromConnection(createConnection, response);
                    response2.req = request;
                    if (response2.hasHeader("Location") != null || request.followRedirects() == null) {
                        if (responseCode < Callback.DEFAULT_DRAG_ANIMATION_DURATION || responseCode >= 400) {
                            if (request.ignoreHttpErrors() == null) {
                                throw new HttpStatusException("HTTP error fetching URL", responseCode, request.url().toString());
                            }
                        }
                        response = response2.contentType();
                        if (response != null || request.ignoreContentType() || response.startsWith("text/") || xmlContentTypeRxp.matcher(response).matches()) {
                            if (!(response == null || xmlContentTypeRxp.matcher(response).matches() == null || (request instanceof Request) == null || ((Request) request).parserDefined != null)) {
                                request.parser(Parser.xmlParser());
                            }
                            response2.charset = DataUtil.getCharsetFromContentType(response2.contentType);
                            if (createConnection.getContentLength() != null || request.method() == Method.HEAD) {
                                response2.byteData = DataUtil.emptyByteBuffer();
                            } else {
                                inputStream = createConnection.getErrorStream() != null ? createConnection.getErrorStream() : createConnection.getInputStream();
                                if (response2.hasHeaderWithValue("Content-Encoding", "gzip") != null) {
                                    inputStream = new GZIPInputStream(inputStream);
                                }
                                response2.byteData = DataUtil.readToByteBuffer(inputStream, request.maxBodySize());
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                            }
                            createConnection.disconnect();
                            response2.executed = true;
                            return response2;
                        }
                        throw new UnsupportedMimeTypeException("Unhandled content type. Must be text/*, application/xml, or application/xhtml+xml", response, request.url().toString());
                    }
                    if (responseCode != 307) {
                        request.method(Method.GET);
                        request.data().clear();
                    }
                    response = response2.header("Location");
                    if (!(response == null || !response.startsWith("http:/") || response.charAt(6) == IOUtils.DIR_SEPARATOR_UNIX)) {
                        response = response.substring(6);
                    }
                    request.url(StringUtil.resolve(request.url(), HttpConnection.encodeUrl(response)));
                    for (Entry entry : response2.cookies.entrySet()) {
                        request.cookie((String) entry.getKey(), (String) entry.getValue());
                    }
                    request = execute(request, response2);
                    createConnection.disconnect();
                    return request;
                }
                protocol = inputStream;
                createConnection = createConnection(request);
                try {
                    createConnection.connect();
                    if (createConnection.getDoOutput()) {
                        writePost(request, createConnection.getOutputStream(), protocol);
                    }
                    responseCode = createConnection.getResponseCode();
                    response2 = new Response(response);
                    response2.setupFromConnection(createConnection, response);
                    response2.req = request;
                    if (response2.hasHeader("Location") != null) {
                    }
                    if (request.ignoreHttpErrors() == null) {
                        throw new HttpStatusException("HTTP error fetching URL", responseCode, request.url().toString());
                    }
                    response = response2.contentType();
                    if (response != null) {
                    }
                    request.parser(Parser.xmlParser());
                    response2.charset = DataUtil.getCharsetFromContentType(response2.contentType);
                    if (createConnection.getContentLength() != null) {
                    }
                    response2.byteData = DataUtil.emptyByteBuffer();
                    createConnection.disconnect();
                    response2.executed = true;
                    return response2;
                } catch (Throwable th) {
                    createConnection.disconnect();
                }
            } else {
                throw new MalformedURLException("Only http & https protocols supported");
            }
        }

        public int statusCode() {
            return this.statusCode;
        }

        public String statusMessage() {
            return this.statusMessage;
        }

        public String charset() {
            return this.charset;
        }

        public Response charset(String str) {
            this.charset = str;
            return this;
        }

        public String contentType() {
            return this.contentType;
        }

        public Document parse() throws IOException {
            Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before parsing response");
            Document parseByteData = DataUtil.parseByteData(this.byteData, this.charset, this.url.toExternalForm(), this.req.parser());
            this.byteData.rewind();
            this.charset = parseByteData.outputSettings().charset().name();
            return parseByteData;
        }

        public String body() {
            String charBuffer;
            Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before getting response body");
            if (this.charset == null) {
                charBuffer = Charset.forName("UTF-8").decode(this.byteData).toString();
            } else {
                charBuffer = Charset.forName(this.charset).decode(this.byteData).toString();
            }
            this.byteData.rewind();
            return charBuffer;
        }

        public byte[] bodyAsBytes() {
            Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before getting response body");
            return this.byteData.array();
        }

        private static HttpURLConnection createConnection(org.jsoup.Connection.Request request) throws IOException {
            URLConnection openConnection;
            if (request.proxy() == null) {
                openConnection = request.url().openConnection();
            } else {
                openConnection = request.url().openConnection(request.proxy());
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
            httpURLConnection.setRequestMethod(request.method().name());
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setConnectTimeout(request.timeout());
            httpURLConnection.setReadTimeout(request.timeout());
            if ((httpURLConnection instanceof HttpsURLConnection) && !request.validateTLSCertificates()) {
                initUnSecureTSL();
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
                httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
                httpsURLConnection.setHostnameVerifier(getInsecureVerifier());
            }
            if (request.method().hasBody()) {
                httpURLConnection.setDoOutput(true);
            }
            if (request.cookies().size() > 0) {
                httpURLConnection.addRequestProperty(HttpHeaders.COOKIE, getRequestCookieString(request));
            }
            for (Entry entry : request.headers().entrySet()) {
                httpURLConnection.addRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
            return httpURLConnection;
        }

        private static HostnameVerifier getInsecureVerifier() {
            return new C14801();
        }

        private static synchronized void initUnSecureTSL() throws java.io.IOException {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
            /*
            r0 = org.jsoup.helper.HttpConnection.Response.class;
            monitor-enter(r0);
            r1 = sslSocketFactory;	 Catch:{ all -> 0x003a }
            if (r1 != 0) goto L_0x0038;	 Catch:{ all -> 0x003a }
        L_0x0007:
            r1 = 1;	 Catch:{ all -> 0x003a }
            r1 = new javax.net.ssl.TrustManager[r1];	 Catch:{ all -> 0x003a }
            r2 = 0;	 Catch:{ all -> 0x003a }
            r3 = new org.jsoup.helper.HttpConnection$Response$2;	 Catch:{ all -> 0x003a }
            r3.<init>();	 Catch:{ all -> 0x003a }
            r1[r2] = r3;	 Catch:{ all -> 0x003a }
            r2 = "SSL";	 Catch:{ NoSuchAlgorithmException -> 0x0030, KeyManagementException -> 0x0028 }
            r2 = javax.net.ssl.SSLContext.getInstance(r2);	 Catch:{ NoSuchAlgorithmException -> 0x0030, KeyManagementException -> 0x0028 }
            r3 = 0;	 Catch:{ NoSuchAlgorithmException -> 0x0030, KeyManagementException -> 0x0028 }
            r4 = new java.security.SecureRandom;	 Catch:{ NoSuchAlgorithmException -> 0x0030, KeyManagementException -> 0x0028 }
            r4.<init>();	 Catch:{ NoSuchAlgorithmException -> 0x0030, KeyManagementException -> 0x0028 }
            r2.init(r3, r1, r4);	 Catch:{ NoSuchAlgorithmException -> 0x0030, KeyManagementException -> 0x0028 }
            r1 = r2.getSocketFactory();	 Catch:{ NoSuchAlgorithmException -> 0x0030, KeyManagementException -> 0x0028 }
            sslSocketFactory = r1;	 Catch:{ NoSuchAlgorithmException -> 0x0030, KeyManagementException -> 0x0028 }
            goto L_0x0038;
        L_0x0028:
            r1 = new java.io.IOException;	 Catch:{ all -> 0x003a }
            r2 = "Can't create unsecure trust manager";	 Catch:{ all -> 0x003a }
            r1.<init>(r2);	 Catch:{ all -> 0x003a }
            throw r1;	 Catch:{ all -> 0x003a }
        L_0x0030:
            r1 = new java.io.IOException;	 Catch:{ all -> 0x003a }
            r2 = "Can't create unsecure trust manager";	 Catch:{ all -> 0x003a }
            r1.<init>(r2);	 Catch:{ all -> 0x003a }
            throw r1;	 Catch:{ all -> 0x003a }
        L_0x0038:
            monitor-exit(r0);
            return;
        L_0x003a:
            r1 = move-exception;
            monitor-exit(r0);
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.jsoup.helper.HttpConnection.Response.initUnSecureTSL():void");
        }

        private void setupFromConnection(HttpURLConnection httpURLConnection, org.jsoup.Connection.Response response) throws IOException {
            this.method = Method.valueOf(httpURLConnection.getRequestMethod());
            this.url = httpURLConnection.getURL();
            this.statusCode = httpURLConnection.getResponseCode();
            this.statusMessage = httpURLConnection.getResponseMessage();
            this.contentType = httpURLConnection.getContentType();
            processResponseHeaders(createHeaderMap(httpURLConnection));
            if (response != null) {
                for (Entry entry : response.cookies().entrySet()) {
                    if (!hasCookie((String) entry.getKey())) {
                        cookie((String) entry.getKey(), (String) entry.getValue());
                    }
                }
            }
        }

        private static LinkedHashMap<String, List<String>> createHeaderMap(HttpURLConnection httpURLConnection) {
            LinkedHashMap<String, List<String>> linkedHashMap = new LinkedHashMap();
            int i = 0;
            while (true) {
                String headerFieldKey = httpURLConnection.getHeaderFieldKey(i);
                String headerField = httpURLConnection.getHeaderField(i);
                if (headerFieldKey == null && headerField == null) {
                    return linkedHashMap;
                }
                i++;
                if (headerFieldKey != null) {
                    if (headerField != null) {
                        if (linkedHashMap.containsKey(headerFieldKey)) {
                            ((List) linkedHashMap.get(headerFieldKey)).add(headerField);
                        } else {
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(headerField);
                            linkedHashMap.put(headerFieldKey, arrayList);
                        }
                    }
                }
            }
        }

        void processResponseHeaders(Map<String, List<String>> map) {
            map = map.entrySet().iterator();
            while (map.hasNext()) {
                Entry entry = (Entry) map.next();
                String str = (String) entry.getKey();
                if (str != null) {
                    List<String> list = (List) entry.getValue();
                    if (str.equalsIgnoreCase(HttpHeaders.SET_COOKIE)) {
                        for (String str2 : list) {
                            if (str2 != null) {
                                TokenQueue tokenQueue = new TokenQueue(str2);
                                str2 = tokenQueue.chompTo("=").trim();
                                String trim = tokenQueue.consumeTo(";").trim();
                                if (str2.length() > 0) {
                                    cookie(str2, trim);
                                }
                            }
                        }
                    } else {
                        int i = 0;
                        if (list.size() == 1) {
                            header(str2, (String) list.get(0));
                        } else if (list.size() > 1) {
                            StringBuilder stringBuilder = new StringBuilder();
                            while (i < list.size()) {
                                String str3 = (String) list.get(i);
                                if (i != 0) {
                                    stringBuilder.append(", ");
                                }
                                stringBuilder.append(str3);
                                i++;
                            }
                            header(str2, stringBuilder.toString());
                        }
                    }
                }
            }
        }

        private static String setOutputContentType(org.jsoup.Connection.Request request) {
            if (!request.hasHeader("Content-Type")) {
                if (HttpConnection.needsMultipart(request)) {
                    String mimeBoundary = DataUtil.mimeBoundary();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("multipart/form-data; boundary=");
                    stringBuilder.append(mimeBoundary);
                    request.header("Content-Type", stringBuilder.toString());
                    return mimeBoundary;
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("application/x-www-form-urlencoded; charset=");
                stringBuilder2.append(request.postDataCharset());
                request.header("Content-Type", stringBuilder2.toString());
            }
            return null;
        }

        private static void writePost(org.jsoup.Connection.Request request, OutputStream outputStream, String str) throws IOException {
            Collection<org.jsoup.Connection.KeyVal> data = request.data();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, request.postDataCharset()));
            if (str != null) {
                for (org.jsoup.Connection.KeyVal keyVal : data) {
                    bufferedWriter.write("--");
                    bufferedWriter.write(str);
                    bufferedWriter.write(IOUtils.LINE_SEPARATOR_WINDOWS);
                    bufferedWriter.write("Content-Disposition: form-data; name=\"");
                    bufferedWriter.write(HttpConnection.encodeMimeName(keyVal.key()));
                    bufferedWriter.write("\"");
                    if (keyVal.hasInputStream()) {
                        bufferedWriter.write("; filename=\"");
                        bufferedWriter.write(HttpConnection.encodeMimeName(keyVal.value()));
                        bufferedWriter.write("\"\r\nContent-Type: application/octet-stream\r\n\r\n");
                        bufferedWriter.flush();
                        DataUtil.crossStreams(keyVal.inputStream(), outputStream);
                        outputStream.flush();
                    } else {
                        bufferedWriter.write("\r\n\r\n");
                        bufferedWriter.write(keyVal.value());
                    }
                    bufferedWriter.write(IOUtils.LINE_SEPARATOR_WINDOWS);
                }
                bufferedWriter.write("--");
                bufferedWriter.write(str);
                bufferedWriter.write("--");
            } else if (request.requestBody() != null) {
                bufferedWriter.write(request.requestBody());
            } else {
                outputStream = true;
                for (org.jsoup.Connection.KeyVal keyVal2 : data) {
                    if (outputStream == null) {
                        bufferedWriter.append(Typography.amp);
                    } else {
                        outputStream = null;
                    }
                    bufferedWriter.write(URLEncoder.encode(keyVal2.key(), request.postDataCharset()));
                    bufferedWriter.write(61);
                    bufferedWriter.write(URLEncoder.encode(keyVal2.value(), request.postDataCharset()));
                }
            }
            bufferedWriter.close();
        }

        private static String getRequestCookieString(org.jsoup.Connection.Request request) {
            StringBuilder stringBuilder = new StringBuilder();
            Object obj = 1;
            for (Entry entry : request.cookies().entrySet()) {
                if (obj == null) {
                    stringBuilder.append("; ");
                } else {
                    obj = null;
                }
                stringBuilder.append((String) entry.getKey());
                stringBuilder.append('=');
                stringBuilder.append((String) entry.getValue());
            }
            return stringBuilder.toString();
        }

        private static void serialiseRequestUrl(org.jsoup.Connection.Request request) throws IOException {
            Object obj;
            URL url = request.url();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(url.getProtocol());
            stringBuilder.append("://");
            stringBuilder.append(url.getAuthority());
            stringBuilder.append(url.getPath());
            stringBuilder.append("?");
            if (url.getQuery() != null) {
                stringBuilder.append(url.getQuery());
                obj = null;
            } else {
                obj = 1;
            }
            for (org.jsoup.Connection.KeyVal keyVal : request.data()) {
                Validate.isFalse(keyVal.hasInputStream(), "InputStream data not supported in URL query string.");
                if (obj == null) {
                    stringBuilder.append(Typography.amp);
                } else {
                    obj = null;
                }
                stringBuilder.append(URLEncoder.encode(keyVal.key(), "UTF-8"));
                stringBuilder.append('=');
                stringBuilder.append(URLEncoder.encode(keyVal.value(), "UTF-8"));
            }
            request.url(new URL(stringBuilder.toString()));
            request.data().clear();
        }
    }

    public static Connection connect(String str) {
        Connection httpConnection = new HttpConnection();
        httpConnection.url(str);
        return httpConnection;
    }

    public static Connection connect(URL url) {
        Connection httpConnection = new HttpConnection();
        httpConnection.url(url);
        return httpConnection;
    }

    private static String encodeUrl(String str) {
        return str == null ? null : str.replaceAll(StringUtils.SPACE, "%20");
    }

    private static String encodeMimeName(String str) {
        return str == null ? null : str.replaceAll("\"", "%22");
    }

    private HttpConnection() {
    }

    public Connection url(URL url) {
        this.req.url(url);
        return this;
    }

    public Connection url(String str) {
        Validate.notEmpty(str, "Must supply a valid URL");
        try {
            this.req.url(new URL(encodeUrl(str)));
            return this;
        } catch (Throwable e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Malformed URL: ");
            stringBuilder.append(str);
            throw new IllegalArgumentException(stringBuilder.toString(), e);
        }
    }

    public Connection proxy(Proxy proxy) {
        this.req.proxy(proxy);
        return this;
    }

    public Connection proxy(String str, int i) {
        this.req.proxy(str, i);
        return this;
    }

    public Connection userAgent(String str) {
        Validate.notNull(str, "User agent must not be null");
        this.req.header(HttpHeaders.USER_AGENT, str);
        return this;
    }

    public Connection timeout(int i) {
        this.req.timeout(i);
        return this;
    }

    public Connection maxBodySize(int i) {
        this.req.maxBodySize(i);
        return this;
    }

    public Connection followRedirects(boolean z) {
        this.req.followRedirects(z);
        return this;
    }

    public Connection referrer(String str) {
        Validate.notNull(str, "Referrer must not be null");
        this.req.header(HttpHeaders.REFERER, str);
        return this;
    }

    public Connection method(Method method) {
        this.req.method(method);
        return this;
    }

    public Connection ignoreHttpErrors(boolean z) {
        this.req.ignoreHttpErrors(z);
        return this;
    }

    public Connection ignoreContentType(boolean z) {
        this.req.ignoreContentType(z);
        return this;
    }

    public Connection validateTLSCertificates(boolean z) {
        this.req.validateTLSCertificates(z);
        return this;
    }

    public Connection data(String str, String str2) {
        this.req.data(KeyVal.create(str, str2));
        return this;
    }

    public Connection data(String str, String str2, InputStream inputStream) {
        this.req.data(KeyVal.create(str, str2, inputStream));
        return this;
    }

    public Connection data(Map<String, String> map) {
        Validate.notNull(map, "Data map must not be null");
        map = map.entrySet().iterator();
        while (map.hasNext()) {
            Entry entry = (Entry) map.next();
            this.req.data(KeyVal.create((String) entry.getKey(), (String) entry.getValue()));
        }
        return this;
    }

    public Connection data(String... strArr) {
        Validate.notNull(strArr, "Data key value pairs must not be null");
        Validate.isTrue(strArr.length % 2 == 0, "Must supply an even number of key value pairs");
        for (int i = 0; i < strArr.length; i += 2) {
            String str = strArr[i];
            String str2 = strArr[i + 1];
            Validate.notEmpty(str, "Data key must not be empty");
            Validate.notNull(str2, "Data value must not be null");
            this.req.data(KeyVal.create(str, str2));
        }
        return this;
    }

    public Connection data(Collection<org.jsoup.Connection.KeyVal> collection) {
        Validate.notNull(collection, "Data collection must not be null");
        for (org.jsoup.Connection.KeyVal data : collection) {
            this.req.data(data);
        }
        return this;
    }

    public org.jsoup.Connection.KeyVal data(String str) {
        Validate.notEmpty(str, "Data key must not be empty");
        for (org.jsoup.Connection.KeyVal keyVal : request().data()) {
            if (keyVal.key().equals(str)) {
                return keyVal;
            }
        }
        return null;
    }

    public Connection requestBody(String str) {
        this.req.requestBody(str);
        return this;
    }

    public Connection header(String str, String str2) {
        this.req.header(str, str2);
        return this;
    }

    public Connection headers(Map<String, String> map) {
        Validate.notNull(map, "Header map must not be null");
        map = map.entrySet().iterator();
        while (map.hasNext()) {
            Entry entry = (Entry) map.next();
            this.req.header((String) entry.getKey(), (String) entry.getValue());
        }
        return this;
    }

    public Connection cookie(String str, String str2) {
        this.req.cookie(str, str2);
        return this;
    }

    public Connection cookies(Map<String, String> map) {
        Validate.notNull(map, "Cookie map must not be null");
        map = map.entrySet().iterator();
        while (map.hasNext()) {
            Entry entry = (Entry) map.next();
            this.req.cookie((String) entry.getKey(), (String) entry.getValue());
        }
        return this;
    }

    public Connection parser(Parser parser) {
        this.req.parser(parser);
        return this;
    }

    public Document get() throws IOException {
        this.req.method(Method.GET);
        execute();
        return this.res.parse();
    }

    public Document post() throws IOException {
        this.req.method(Method.POST);
        execute();
        return this.res.parse();
    }

    public org.jsoup.Connection.Response execute() throws IOException {
        this.res = Response.execute(this.req);
        return this.res;
    }

    public org.jsoup.Connection.Request request() {
        return this.req;
    }

    public Connection request(org.jsoup.Connection.Request request) {
        this.req = request;
        return this;
    }

    public org.jsoup.Connection.Response response() {
        return this.res;
    }

    public Connection response(org.jsoup.Connection.Response response) {
        this.res = response;
        return this;
    }

    public Connection postDataCharset(String str) {
        this.req.postDataCharset(str);
        return this;
    }

    private static boolean needsMultipart(org.jsoup.Connection.Request request) {
        for (org.jsoup.Connection.KeyVal hasInputStream : request.data()) {
            if (hasInputStream.hasInputStream()) {
                return true;
            }
        }
        return null;
    }
}
