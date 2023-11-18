package okhttp3;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import okhttp3.Response.Builder;
import okhttp3.internal.cache.CacheStrategy;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.cache.DiskLruCache$Editor;
import okhttp3.internal.cache.DiskLruCache.Snapshot;
import okhttp3.internal.cache.InternalCache;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.StatusLine;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import okio.Source;

public final class Cache implements Closeable, Flushable {
    private static final int ENTRY_BODY = 1;
    private static final int ENTRY_COUNT = 2;
    private static final int ENTRY_METADATA = 0;
    private static final int VERSION = 201105;
    final DiskLruCache cache;
    private int hitCount;
    final InternalCache internalCache;
    private int networkCount;
    private int requestCount;
    int writeAbortCount;
    int writeSuccessCount;

    /* renamed from: okhttp3.Cache$2 */
    class C11662 implements Iterator<String> {
        boolean canRemove;
        final Iterator<Snapshot> delegate = Cache.this.cache.snapshots();
        @Nullable
        String nextUrl;

        C11662() throws IOException {
        }

        public boolean hasNext() {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r4 = this;
            r0 = r4.nextUrl;
            r1 = 1;
            if (r0 == 0) goto L_0x0006;
        L_0x0005:
            return r1;
        L_0x0006:
            r0 = 0;
            r4.canRemove = r0;
        L_0x0009:
            r2 = r4.delegate;
            r2 = r2.hasNext();
            if (r2 == 0) goto L_0x0034;
        L_0x0011:
            r2 = r4.delegate;
            r2 = r2.next();
            r2 = (okhttp3.internal.cache.DiskLruCache.Snapshot) r2;
            r3 = r2.getSource(r0);	 Catch:{ IOException -> 0x0030, all -> 0x002b }
            r3 = okio.Okio.buffer(r3);	 Catch:{ IOException -> 0x0030, all -> 0x002b }
            r3 = r3.readUtf8LineStrict();	 Catch:{ IOException -> 0x0030, all -> 0x002b }
            r4.nextUrl = r3;	 Catch:{ IOException -> 0x0030, all -> 0x002b }
            r2.close();
            return r1;
        L_0x002b:
            r0 = move-exception;
            r2.close();
            throw r0;
        L_0x0030:
            r2.close();
            goto L_0x0009;
        L_0x0034:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.Cache.2.hasNext():boolean");
        }

        public String next() {
            if (hasNext()) {
                String str = this.nextUrl;
                this.nextUrl = null;
                this.canRemove = true;
                return str;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            if (this.canRemove) {
                this.delegate.remove();
                return;
            }
            throw new IllegalStateException("remove() before next()");
        }
    }

    private static class CacheResponseBody extends ResponseBody {
        private final BufferedSource bodySource;
        @Nullable
        private final String contentLength;
        @Nullable
        private final String contentType;
        final Snapshot snapshot;

        CacheResponseBody(Snapshot snapshot, String str, String str2) {
            this.snapshot = snapshot;
            this.contentType = str;
            this.contentLength = str2;
            this.bodySource = Okio.buffer(new Cache$CacheResponseBody$1(this, snapshot.getSource(1), snapshot));
        }

        public MediaType contentType() {
            return this.contentType != null ? MediaType.parse(this.contentType) : null;
        }

        public long contentLength() {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r4 = this;
            r0 = -1;
            r2 = r4.contentLength;	 Catch:{ NumberFormatException -> 0x000e }
            if (r2 == 0) goto L_0x000d;	 Catch:{ NumberFormatException -> 0x000e }
        L_0x0006:
            r2 = r4.contentLength;	 Catch:{ NumberFormatException -> 0x000e }
            r2 = java.lang.Long.parseLong(r2);	 Catch:{ NumberFormatException -> 0x000e }
            r0 = r2;
        L_0x000d:
            return r0;
        L_0x000e:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.Cache.CacheResponseBody.contentLength():long");
        }

        public BufferedSource source() {
            return this.bodySource;
        }
    }

    private static final class Entry {
        private static final String RECEIVED_MILLIS;
        private static final String SENT_MILLIS;
        private final int code;
        @Nullable
        private final Handshake handshake;
        private final String message;
        private final Protocol protocol;
        private final long receivedResponseMillis;
        private final String requestMethod;
        private final Headers responseHeaders;
        private final long sentRequestMillis;
        private final String url;
        private final Headers varyHeaders;

        static {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Platform.get().getPrefix());
            stringBuilder.append("-Sent-Millis");
            SENT_MILLIS = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(Platform.get().getPrefix());
            stringBuilder.append("-Received-Millis");
            RECEIVED_MILLIS = stringBuilder.toString();
        }

        Entry(Source source) throws IOException {
            try {
                BufferedSource buffer = Okio.buffer(source);
                this.url = buffer.readUtf8LineStrict();
                this.requestMethod = buffer.readUtf8LineStrict();
                Headers$Builder headers$Builder = new Headers$Builder();
                int readInt = Cache.readInt(buffer);
                for (int i = 0; i < readInt; i++) {
                    headers$Builder.addLenient(buffer.readUtf8LineStrict());
                }
                this.varyHeaders = headers$Builder.build();
                StatusLine parse = StatusLine.parse(buffer.readUtf8LineStrict());
                this.protocol = parse.protocol;
                this.code = parse.code;
                this.message = parse.message;
                headers$Builder = new Headers$Builder();
                readInt = Cache.readInt(buffer);
                for (int i2 = 0; i2 < readInt; i2++) {
                    headers$Builder.addLenient(buffer.readUtf8LineStrict());
                }
                String str = headers$Builder.get(SENT_MILLIS);
                String str2 = headers$Builder.get(RECEIVED_MILLIS);
                headers$Builder.removeAll(SENT_MILLIS);
                headers$Builder.removeAll(RECEIVED_MILLIS);
                long j = 0;
                this.sentRequestMillis = str != null ? Long.parseLong(str) : 0;
                if (str2 != null) {
                    j = Long.parseLong(str2);
                }
                this.receivedResponseMillis = j;
                this.responseHeaders = headers$Builder.build();
                if (isHttps()) {
                    String readUtf8LineStrict = buffer.readUtf8LineStrict();
                    if (readUtf8LineStrict.length() > 0) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("expected \"\" but was \"");
                        stringBuilder.append(readUtf8LineStrict);
                        stringBuilder.append("\"");
                        throw new IOException(stringBuilder.toString());
                    }
                    TlsVersion tlsVersion;
                    CipherSuite forJavaName = CipherSuite.forJavaName(buffer.readUtf8LineStrict());
                    List readCertificateList = readCertificateList(buffer);
                    List readCertificateList2 = readCertificateList(buffer);
                    if (buffer.exhausted()) {
                        tlsVersion = TlsVersion.SSL_3_0;
                    } else {
                        tlsVersion = TlsVersion.forJavaName(buffer.readUtf8LineStrict());
                    }
                    this.handshake = Handshake.get(tlsVersion, forJavaName, readCertificateList, readCertificateList2);
                } else {
                    this.handshake = null;
                }
                source.close();
            } catch (Throwable th) {
                source.close();
            }
        }

        Entry(Response response) {
            this.url = response.request().url().toString();
            this.varyHeaders = HttpHeaders.varyHeaders(response);
            this.requestMethod = response.request().method();
            this.protocol = response.protocol();
            this.code = response.code();
            this.message = response.message();
            this.responseHeaders = response.headers();
            this.handshake = response.handshake();
            this.sentRequestMillis = response.sentRequestAtMillis();
            this.receivedResponseMillis = response.receivedResponseAtMillis();
        }

        public void writeTo(DiskLruCache$Editor diskLruCache$Editor) throws IOException {
            int i = 0;
            diskLruCache$Editor = Okio.buffer(diskLruCache$Editor.newSink(0));
            diskLruCache$Editor.writeUtf8(this.url).writeByte(10);
            diskLruCache$Editor.writeUtf8(this.requestMethod).writeByte(10);
            diskLruCache$Editor.writeDecimalLong((long) this.varyHeaders.size()).writeByte(10);
            int size = this.varyHeaders.size();
            for (int i2 = 0; i2 < size; i2++) {
                diskLruCache$Editor.writeUtf8(this.varyHeaders.name(i2)).writeUtf8(": ").writeUtf8(this.varyHeaders.value(i2)).writeByte(10);
            }
            diskLruCache$Editor.writeUtf8(new StatusLine(this.protocol, this.code, this.message).toString()).writeByte(10);
            diskLruCache$Editor.writeDecimalLong((long) (this.responseHeaders.size() + 2)).writeByte(10);
            size = this.responseHeaders.size();
            while (i < size) {
                diskLruCache$Editor.writeUtf8(this.responseHeaders.name(i)).writeUtf8(": ").writeUtf8(this.responseHeaders.value(i)).writeByte(10);
                i++;
            }
            diskLruCache$Editor.writeUtf8(SENT_MILLIS).writeUtf8(": ").writeDecimalLong(this.sentRequestMillis).writeByte(10);
            diskLruCache$Editor.writeUtf8(RECEIVED_MILLIS).writeUtf8(": ").writeDecimalLong(this.receivedResponseMillis).writeByte(10);
            if (isHttps()) {
                diskLruCache$Editor.writeByte(10);
                diskLruCache$Editor.writeUtf8(this.handshake.cipherSuite().javaName()).writeByte(10);
                writeCertList(diskLruCache$Editor, this.handshake.peerCertificates());
                writeCertList(diskLruCache$Editor, this.handshake.localCertificates());
                diskLruCache$Editor.writeUtf8(this.handshake.tlsVersion().javaName()).writeByte(10);
            }
            diskLruCache$Editor.close();
        }

        private boolean isHttps() {
            return this.url.startsWith("https://");
        }

        private List<Certificate> readCertificateList(BufferedSource bufferedSource) throws IOException {
            int readInt = Cache.readInt(bufferedSource);
            if (readInt == -1) {
                return Collections.emptyList();
            }
            try {
                CertificateFactory instance = CertificateFactory.getInstance("X.509");
                List<Certificate> arrayList = new ArrayList(readInt);
                for (int i = 0; i < readInt; i++) {
                    String readUtf8LineStrict = bufferedSource.readUtf8LineStrict();
                    Buffer buffer = new Buffer();
                    buffer.write(ByteString.decodeBase64(readUtf8LineStrict));
                    arrayList.add(instance.generateCertificate(buffer.inputStream()));
                }
                return arrayList;
            } catch (BufferedSource bufferedSource2) {
                throw new IOException(bufferedSource2.getMessage());
            }
        }

        private void writeCertList(BufferedSink bufferedSink, List<Certificate> list) throws IOException {
            try {
                bufferedSink.writeDecimalLong((long) list.size()).writeByte(10);
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    bufferedSink.writeUtf8(ByteString.of(((Certificate) list.get(i)).getEncoded()).base64()).writeByte(10);
                }
            } catch (BufferedSink bufferedSink2) {
                throw new IOException(bufferedSink2.getMessage());
            }
        }

        public boolean matches(Request request, Response response) {
            return (this.url.equals(request.url().toString()) && this.requestMethod.equals(request.method()) && HttpHeaders.varyMatches(response, this.varyHeaders, request) != null) ? true : null;
        }

        public Response response(Snapshot snapshot) {
            String str = this.responseHeaders.get("Content-Type");
            String str2 = this.responseHeaders.get(com.google.common.net.HttpHeaders.CONTENT_LENGTH);
            return new Builder().request(new Request.Builder().url(this.url).method(this.requestMethod, null).headers(this.varyHeaders).build()).protocol(this.protocol).code(this.code).message(this.message).headers(this.responseHeaders).body(new CacheResponseBody(snapshot, str, str2)).handshake(this.handshake).sentRequestAtMillis(this.sentRequestMillis).receivedResponseAtMillis(this.receivedResponseMillis).build();
        }
    }

    public Cache(File file, long j) {
        this(file, j, FileSystem.SYSTEM);
    }

    Cache(File file, long j, FileSystem fileSystem) {
        this.internalCache = new Cache$1(this);
        this.cache = DiskLruCache.create(fileSystem, file, VERSION, 2, j);
    }

    public static String key(HttpUrl httpUrl) {
        return ByteString.encodeUtf8(httpUrl.toString()).md5().hex();
    }

    @javax.annotation.Nullable
    okhttp3.Response get(okhttp3.Request r5) {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r4 = this;
        r0 = r5.url();
        r0 = key(r0);
        r1 = 0;
        r2 = r4.cache;	 Catch:{ IOException -> 0x0033 }
        r0 = r2.get(r0);	 Catch:{ IOException -> 0x0033 }
        if (r0 != 0) goto L_0x0012;
    L_0x0011:
        return r1;
    L_0x0012:
        r2 = new okhttp3.Cache$Entry;	 Catch:{ IOException -> 0x002f }
        r3 = 0;	 Catch:{ IOException -> 0x002f }
        r3 = r0.getSource(r3);	 Catch:{ IOException -> 0x002f }
        r2.<init>(r3);	 Catch:{ IOException -> 0x002f }
        r0 = r2.response(r0);
        r5 = r2.matches(r5, r0);
        if (r5 != 0) goto L_0x002e;
    L_0x0026:
        r5 = r0.body();
        okhttp3.internal.Util.closeQuietly(r5);
        return r1;
    L_0x002e:
        return r0;
    L_0x002f:
        okhttp3.internal.Util.closeQuietly(r0);
        return r1;
    L_0x0033:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.Cache.get(okhttp3.Request):okhttp3.Response");
    }

    @javax.annotation.Nullable
    okhttp3.internal.cache.CacheRequest put(okhttp3.Response r4) {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r3 = this;
        r0 = r4.request();
        r0 = r0.method();
        r1 = r4.request();
        r1 = r1.method();
        r1 = okhttp3.internal.http.HttpMethod.invalidatesCache(r1);
        r2 = 0;
        if (r1 == 0) goto L_0x001f;
    L_0x0017:
        r4 = r4.request();	 Catch:{ IOException -> 0x001e }
        r3.remove(r4);	 Catch:{ IOException -> 0x001e }
    L_0x001e:
        return r2;
    L_0x001f:
        r1 = "GET";
        r0 = r0.equals(r1);
        if (r0 != 0) goto L_0x0028;
    L_0x0027:
        return r2;
    L_0x0028:
        r0 = okhttp3.internal.http.HttpHeaders.hasVaryAll(r4);
        if (r0 == 0) goto L_0x002f;
    L_0x002e:
        return r2;
    L_0x002f:
        r0 = new okhttp3.Cache$Entry;
        r0.<init>(r4);
        r1 = r3.cache;	 Catch:{ IOException -> 0x0052 }
        r4 = r4.request();	 Catch:{ IOException -> 0x0052 }
        r4 = r4.url();	 Catch:{ IOException -> 0x0052 }
        r4 = key(r4);	 Catch:{ IOException -> 0x0052 }
        r4 = r1.edit(r4);	 Catch:{ IOException -> 0x0052 }
        if (r4 != 0) goto L_0x0049;
    L_0x0048:
        return r2;
    L_0x0049:
        r0.writeTo(r4);	 Catch:{ IOException -> 0x0053 }
        r0 = new okhttp3.Cache$CacheRequestImpl;	 Catch:{ IOException -> 0x0053 }
        r0.<init>(r3, r4);	 Catch:{ IOException -> 0x0053 }
        return r0;
    L_0x0052:
        r4 = r2;
    L_0x0053:
        r3.abortQuietly(r4);
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.Cache.put(okhttp3.Response):okhttp3.internal.cache.CacheRequest");
    }

    void remove(Request request) throws IOException {
        this.cache.remove(key(request.url()));
    }

    void update(okhttp3.Response r2, okhttp3.Response r3) {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r1 = this;
        r0 = new okhttp3.Cache$Entry;
        r0.<init>(r3);
        r2 = r2.body();
        r2 = (okhttp3.Cache.CacheResponseBody) r2;
        r2 = r2.snapshot;
        r2 = r2.edit();	 Catch:{ IOException -> 0x001a }
        if (r2 == 0) goto L_0x001e;
    L_0x0013:
        r0.writeTo(r2);	 Catch:{ IOException -> 0x001b }
        r2.commit();	 Catch:{ IOException -> 0x001b }
        goto L_0x001e;
    L_0x001a:
        r2 = 0;
    L_0x001b:
        r1.abortQuietly(r2);
    L_0x001e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.Cache.update(okhttp3.Response, okhttp3.Response):void");
    }

    private void abortQuietly(@javax.annotation.Nullable okhttp3.internal.cache.DiskLruCache$Editor r1) {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = this;
        if (r1 == 0) goto L_0x0005;
    L_0x0002:
        r1.abort();	 Catch:{ IOException -> 0x0005 }
    L_0x0005:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.Cache.abortQuietly(okhttp3.internal.cache.DiskLruCache$Editor):void");
    }

    public void initialize() throws IOException {
        this.cache.initialize();
    }

    public void delete() throws IOException {
        this.cache.delete();
    }

    public void evictAll() throws IOException {
        this.cache.evictAll();
    }

    public Iterator<String> urls() throws IOException {
        return new C11662();
    }

    public synchronized int writeAbortCount() {
        return this.writeAbortCount;
    }

    public synchronized int writeSuccessCount() {
        return this.writeSuccessCount;
    }

    public long size() throws IOException {
        return this.cache.size();
    }

    public long maxSize() {
        return this.cache.getMaxSize();
    }

    public void flush() throws IOException {
        this.cache.flush();
    }

    public void close() throws IOException {
        this.cache.close();
    }

    public File directory() {
        return this.cache.getDirectory();
    }

    public boolean isClosed() {
        return this.cache.isClosed();
    }

    synchronized void trackResponse(CacheStrategy cacheStrategy) {
        this.requestCount++;
        if (cacheStrategy.networkRequest != null) {
            this.networkCount++;
        } else if (cacheStrategy.cacheResponse != null) {
            this.hitCount++;
        }
    }

    synchronized void trackConditionalCacheHit() {
        this.hitCount++;
    }

    public synchronized int networkCount() {
        return this.networkCount;
    }

    public synchronized int hitCount() {
        return this.hitCount;
    }

    public synchronized int requestCount() {
        return this.requestCount;
    }

    static int readInt(BufferedSource bufferedSource) throws IOException {
        try {
            long readDecimalLong = bufferedSource.readDecimalLong();
            bufferedSource = bufferedSource.readUtf8LineStrict();
            if (readDecimalLong >= 0 && readDecimalLong <= 2147483647L) {
                if (bufferedSource.isEmpty()) {
                    return (int) readDecimalLong;
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("expected an int but was \"");
            stringBuilder.append(readDecimalLong);
            stringBuilder.append(bufferedSource);
            stringBuilder.append("\"");
            throw new IOException(stringBuilder.toString());
        } catch (BufferedSource bufferedSource2) {
            throw new IOException(bufferedSource2.getMessage());
        }
    }
}
