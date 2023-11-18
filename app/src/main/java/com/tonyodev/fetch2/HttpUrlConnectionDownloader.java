package com.tonyodev.fetch2;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.google.common.net.HttpHeaders;
import com.tonyodev.fetch2.Downloader.FileDownloaderType;
import com.tonyodev.fetch2.Downloader.Request;
import com.tonyodev.fetch2.Downloader.Response;
import ir.mahdi.mzip.rar.unpack.decode.Compress;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0016\u0018\u00002\u00020\u0001:\u0001)B\u001d\b\u0007\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u0010\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\fH\u0016J\u0012\u0010\u0014\u001a\u00020\u00112\b\u0010\u0015\u001a\u0004\u0018\u00010\rH\u0002J\u0012\u0010\u0016\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u0012\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u0010\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u001f\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u001e\u001a\u00020\u001fH\u0016¢\u0006\u0002\u0010 J\u001a\u0010!\u001a\u0004\u0018\u00010\"2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010#\u001a\u00020\u001fH\u0016J\u0010\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u001dH\u0004J \u0010'\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010(\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u001fH\u0016R\u0014\u0010\u0007\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR \u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006*"}, d2 = {"Lcom/tonyodev/fetch2/HttpUrlConnectionDownloader;", "Lcom/tonyodev/fetch2/Downloader;", "httpUrlConnectionPreferences", "Lcom/tonyodev/fetch2/HttpUrlConnectionDownloader$HttpUrlConnectionPreferences;", "fileDownloaderType", "Lcom/tonyodev/fetch2/Downloader$FileDownloaderType;", "(Lcom/tonyodev/fetch2/HttpUrlConnectionDownloader$HttpUrlConnectionPreferences;Lcom/tonyodev/fetch2/Downloader$FileDownloaderType;)V", "connectionPrefs", "getConnectionPrefs", "()Lcom/tonyodev/fetch2/HttpUrlConnectionDownloader$HttpUrlConnectionPreferences;", "connections", "", "Lcom/tonyodev/fetch2/Downloader$Response;", "Ljava/net/HttpURLConnection;", "getConnections", "()Ljava/util/Map;", "close", "", "disconnect", "response", "disconnectClient", "client", "execute", "request", "Lcom/tonyodev/fetch2/Downloader$Request;", "getDirectoryForFileDownloaderTypeParallel", "", "getFileDownloaderType", "getFileSlicingCount", "", "contentLength", "", "(Lcom/tonyodev/fetch2/Downloader$Request;J)Ljava/lang/Integer;", "getRequestOutputStream", "Ljava/io/OutputStream;", "filePointerOffset", "isResponseOk", "", "responseCode", "seekOutputStreamToPosition", "outputStream", "HttpUrlConnectionPreferences", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: HttpUrlConnectionDownloader.kt */
public class HttpUrlConnectionDownloader implements Downloader {
    @NotNull
    private final HttpUrlConnectionPreferences connectionPrefs;
    @NotNull
    private final Map<Response, HttpURLConnection> connections;
    private final FileDownloaderType fileDownloaderType;

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u000e\b\u0016\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\bR\u001a\u0010\u0012\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\f\"\u0004\b\u0014\u0010\u000eR\u001a\u0010\u0015\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\f\"\u0004\b\u0017\u0010\u000e¨\u0006\u0018"}, d2 = {"Lcom/tonyodev/fetch2/HttpUrlConnectionDownloader$HttpUrlConnectionPreferences;", "", "()V", "connectTimeout", "", "getConnectTimeout", "()I", "setConnectTimeout", "(I)V", "followsRedirect", "", "getFollowsRedirect", "()Z", "setFollowsRedirect", "(Z)V", "readTimeout", "getReadTimeout", "setReadTimeout", "usesCache", "getUsesCache", "setUsesCache", "usesDefaultCache", "getUsesDefaultCache", "setUsesDefaultCache", "fetch2_release"}, k = 1, mv = {1, 1, 10})
    /* compiled from: HttpUrlConnectionDownloader.kt */
    public static class HttpUrlConnectionPreferences {
        private int connectTimeout = 15000;
        private boolean followsRedirect = true;
        private int readTimeout = 20000;
        private boolean usesCache;
        private boolean usesDefaultCache;

        public final int getReadTimeout() {
            return this.readTimeout;
        }

        public final void setReadTimeout(int i) {
            this.readTimeout = i;
        }

        public final int getConnectTimeout() {
            return this.connectTimeout;
        }

        public final void setConnectTimeout(int i) {
            this.connectTimeout = i;
        }

        public final boolean getUsesCache() {
            return this.usesCache;
        }

        public final void setUsesCache(boolean z) {
            this.usesCache = z;
        }

        public final boolean getUsesDefaultCache() {
            return this.usesDefaultCache;
        }

        public final void setUsesDefaultCache(boolean z) {
            this.usesDefaultCache = z;
        }

        public final boolean getFollowsRedirect() {
            return this.followsRedirect;
        }

        public final void setFollowsRedirect(boolean z) {
            this.followsRedirect = z;
        }
    }

    @JvmOverloads
    public HttpUrlConnectionDownloader() {
        this(null, null, 3, null);
    }

    @JvmOverloads
    public HttpUrlConnectionDownloader(@Nullable HttpUrlConnectionPreferences httpUrlConnectionPreferences) {
        this(httpUrlConnectionPreferences, null, 2, null);
    }

    @Nullable
    public String getDirectoryForFileDownloaderTypeParallel(@NotNull Request request) {
        Intrinsics.checkParameterIsNotNull(request, "request");
        return null;
    }

    @Nullable
    public Integer getFileSlicingCount(@NotNull Request request, long j) {
        Intrinsics.checkParameterIsNotNull(request, "request");
        return null;
    }

    @Nullable
    public OutputStream getRequestOutputStream(@NotNull Request request, long j) {
        Intrinsics.checkParameterIsNotNull(request, "request");
        return null;
    }

    protected final boolean isResponseOk(int i) {
        if (Callback.DEFAULT_DRAG_ANIMATION_DURATION <= i) {
            if (Compress.NC >= i) {
                return true;
            }
        }
        return false;
    }

    public void seekOutputStreamToPosition(@NotNull Request request, @NotNull OutputStream outputStream, long j) {
        Intrinsics.checkParameterIsNotNull(request, "request");
        Intrinsics.checkParameterIsNotNull(outputStream, "outputStream");
    }

    @JvmOverloads
    public HttpUrlConnectionDownloader(@Nullable HttpUrlConnectionPreferences httpUrlConnectionPreferences, @NotNull FileDownloaderType fileDownloaderType) {
        Intrinsics.checkParameterIsNotNull(fileDownloaderType, "fileDownloaderType");
        this.fileDownloaderType = fileDownloaderType;
        if (httpUrlConnectionPreferences == null) {
            httpUrlConnectionPreferences = new HttpUrlConnectionPreferences();
        }
        this.connectionPrefs = httpUrlConnectionPreferences;
        httpUrlConnectionPreferences = Collections.synchronizedMap((Map) new HashMap());
        Intrinsics.checkExpressionValueIsNotNull(httpUrlConnectionPreferences, "Collections.synchronized…se, HttpURLConnection>())");
        this.connections = httpUrlConnectionPreferences;
    }

    @JvmOverloads
    public /* synthetic */ HttpUrlConnectionDownloader(HttpUrlConnectionPreferences httpUrlConnectionPreferences, FileDownloaderType fileDownloaderType, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != null) {
            httpUrlConnectionPreferences = null;
        }
        if ((i & 2) != 0) {
            fileDownloaderType = FileDownloaderType.SEQUENTIAL;
        }
        this(httpUrlConnectionPreferences, fileDownloaderType);
    }

    @NotNull
    protected final HttpUrlConnectionPreferences getConnectionPrefs() {
        return this.connectionPrefs;
    }

    @NotNull
    protected final Map<Response, HttpURLConnection> getConnections() {
        return this.connections;
    }

    @Nullable
    public Response execute(@NotNull Request request) {
        Intrinsics.checkParameterIsNotNull(request, "request");
        URLConnection openConnection = new URL(request.getUrl()).openConnection();
        if (openConnection == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.net.HttpURLConnection");
        }
        InputStream inputStream;
        long j;
        boolean z;
        HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setReadTimeout(this.connectionPrefs.getReadTimeout());
        httpURLConnection.setConnectTimeout(this.connectionPrefs.getConnectTimeout());
        httpURLConnection.setUseCaches(this.connectionPrefs.getUsesCache());
        httpURLConnection.setDefaultUseCaches(this.connectionPrefs.getUsesDefaultCache());
        httpURLConnection.setInstanceFollowRedirects(this.connectionPrefs.getFollowsRedirect());
        httpURLConnection.setDoInput(true);
        for (Entry entry : request.getHeaders().entrySet()) {
            httpURLConnection.addRequestProperty((String) entry.getKey(), (String) entry.getValue());
        }
        httpURLConnection.connect();
        int responseCode = httpURLConnection.getResponseCode();
        InputStream inputStream2 = (InputStream) null;
        long j2 = -1;
        if (isResponseOk(responseCode)) {
            String headerField = httpURLConnection.getHeaderField(HttpHeaders.CONTENT_LENGTH);
            if (headerField != null) {
                j2 = Long.parseLong(headerField);
            }
            inputStream = httpURLConnection.getInputStream();
            j = j2;
            z = true;
        } else {
            inputStream = inputStream2;
            j = -1;
            z = false;
        }
        Response response = new Response(responseCode, z, j, inputStream, request);
        this.connections.put(response, httpURLConnection);
        return response;
    }

    public void disconnect(@NotNull Response response) {
        Intrinsics.checkParameterIsNotNull(response, "response");
        if (this.connections.containsKey(response)) {
            Object obj = this.connections.get(response);
            if (obj == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.net.HttpURLConnection");
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) obj;
            this.connections.remove(response);
            disconnectClient(httpURLConnection);
        }
    }

    public void close() {
        for (Entry value : this.connections.entrySet()) {
            disconnectClient((HttpURLConnection) value.getValue());
        }
        this.connections.clear();
    }

    private final void disconnectClient(java.net.HttpURLConnection r1) {
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
        r1.disconnect();	 Catch:{ Exception -> 0x0005 }
    L_0x0005:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tonyodev.fetch2.HttpUrlConnectionDownloader.disconnectClient(java.net.HttpURLConnection):void");
    }

    @NotNull
    public FileDownloaderType getFileDownloaderType(@NotNull Request request) {
        Intrinsics.checkParameterIsNotNull(request, "request");
        return this.fileDownloaderType;
    }
}
