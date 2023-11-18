package com.android.morpheustv.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.util.Log;
import com.android.morpheustv.casting.Srt2Vtt;
import com.android.morpheustv.helpers.TorrentHelper;
import com.android.morpheustv.helpers.Utils;
import com.android.morpheustv.settings.Settings;
import com.android.morpheustv.sources.SubtitleResult;
import com.android.morpheustv.sources.subtitles.LegendasDivx;
import com.android.morpheustv.sources.subtitles.OpenSubtitles;
import com.android.morpheustv.sources.subtitles.TVSubs;
import com.github.se_bastiaan.torrentstream.Torrent;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import de.timroes.base64.Base64;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.Source;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class HttpServer extends NanoHTTPD {
    public static final String ACCESS_CONTROL_ALLOW_HEADER_PROPERTY_NAME = "AccessControlAllowHeader";
    private static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS, HEAD";
    public static final String DEFAULT_ALLOWED_HEADERS = "origin,accept,content-type";
    private static final int MAX_AGE = 151200;
    private static String targetDownloadedFile;
    private static Source targetSource;
    private String indexHtml = "";
    private Context mContext;

    private static class ProxyStream {
        long content_length;
        String content_type;
        InputStream inputStream;
        Map<String, String> response_headers;
        Status result_status;

        private ProxyStream() {
            this.inputStream = null;
            this.content_type = MimeTypes.VIDEO_MP4;
            this.content_length = 0;
            this.result_status = Status.FORBIDDEN;
        }
    }

    private static Source getTargetSource() {
        return targetSource;
    }

    public static void setTargetSource(Source source) {
        targetSource = source;
        saveTargets();
    }

    private static String getTargetDownloadedFile() {
        return targetDownloadedFile;
    }

    public static void setTargetDownloadedFile(String str) {
        targetDownloadedFile = str;
        saveTargets();
    }

    private static void saveTargets() {
        File file = new File(Environment.getExternalStorageDirectory(), "/morpheustv/");
        if (!file.exists()) {
            file.mkdir();
        }
        if (file.exists()) {
            try {
                File file2 = new File(file, "streams.json");
                if (file2.exists()) {
                    file2.delete();
                }
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("targetDownloadedFile", getTargetDownloadedFile());
                jSONObject.put("targetSource", new Gson().toJson(getTargetSource()));
                FileUtils.write(file2, jSONObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadTargets() {
        File file = new File(Environment.getExternalStorageDirectory(), "/morpheustv/");
        if (!file.exists()) {
            file.mkdir();
        }
        if (file.exists()) {
            try {
                File file2 = new File(file, "streams.json");
                if (file2.exists()) {
                    JSONObject jSONObject = new JSONObject(FileUtils.readFileToString(file2));
                    targetDownloadedFile = jSONObject.optString("targetDownloadedFile", null);
                    try {
                        targetSource = (Source) new Gson().fromJson(jSONObject.optString("targetSource"), Source.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                        targetSource = null;
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                targetDownloadedFile = null;
                targetSource = null;
            }
        }
    }

    @SuppressLint({"NewApi"})
    public void sendMessageToService(String str) {
        if (this.mContext != null && BackgroundService.isRunning) {
            Intent intent = new Intent(this.mContext, BackgroundService.class);
            intent.setAction(BackgroundService.WEBSERVER_SERVICE_ACTION);
            intent.putExtra("Message", str);
            if (VERSION.SDK_INT < 26 || Settings.NOTIFICATION_MAIN == null) {
                this.mContext.startService(intent);
            } else {
                this.mContext.startForegroundService(intent);
            }
        }
    }

    public String LoadAssetAsString(String str) {
        String str2 = "";
        try {
            str = this.mContext.getAssets().open(str);
            byte[] bArr = new byte[str.available()];
            str.read(bArr);
            str.close();
            return new String(bArr);
        } catch (String str3) {
            str3.printStackTrace();
            return str2;
        }
    }

    public HttpServer(Context context, int i) {
        super(i);
        this.mContext = context;
        loadTargets();
        this.indexHtml = LoadAssetAsString("index.html");
        context = new StringBuilder();
        context.append("http://");
        context.append(Utils.getIP());
        context.append(":");
        context.append(String.valueOf(i));
        sendMessageToService(context.toString());
    }

    private Response getRootResponse() {
        return NanoHTTPD.newFixedLengthResponse(Status.OK, NanoHTTPD.MIME_HTML, this.indexHtml);
    }

    private Response getNotFound() {
        return NanoHTTPD.newFixedLengthResponse(Status.SERVICE_UNAVAILABLE, NanoHTTPD.MIME_HTML, "<h1>Morpheus TV Server</h1><p>The resource you requested is not available at this moment.</p>");
    }

    public Response serve(IHTTPSession iHTTPSession) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Proxy request for ");
        stringBuilder.append(iHTTPSession.getUri());
        Log.d("HttpServer", stringBuilder.toString());
        Response notFound = getNotFound();
        if (iHTTPSession.getUri().equals("/")) {
            notFound = getRootResponse();
        } else if (!iHTTPSession.getUri().contains("favicon")) {
            String remoteHostName = iHTTPSession.getRemoteHostName();
            if (getTargetDownloadedFile() != null && iHTTPSession.getUri().contains("download.mp4")) {
                notFound = serveDownloadedFile(iHTTPSession);
            } else if (TorrentHelper.getCurrentTorrent() != null && iHTTPSession.getUri().contains("torrent.mp4")) {
                notFound = serveTorrentStream(iHTTPSession);
            } else if (iHTTPSession.getUri().contains("subtitles.vtt")) {
                notFound = serveSubtitles(iHTTPSession);
            } else if (getTargetSource() != null) {
                notFound = serveMP4Stream(iHTTPSession);
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Serving '");
            stringBuilder2.append(iHTTPSession.getUri());
            stringBuilder2.append("' to ");
            stringBuilder2.append(remoteHostName);
            sendMessageToService(stringBuilder2.toString());
        }
        return addCORSHeaders(notFound);
    }

    public void fixSTRMGO(ProxyStream proxyStream, String str) {
        try {
            if (getTargetSource().getSource().contains("STRMGO") && str.contains("video.m3u8") != null && proxyStream.content_length > 0) {
                str = new byte[((int) proxyStream.content_length)];
                proxyStream.inputStream.read(str, 0, (int) proxyStream.content_length);
                str = new String(str).replace("400000000", "400000").replace("900000000", "900000").replace("2600000000", "2600000").replace("7200000000", "7200000").getBytes();
                proxyStream.inputStream = new ByteArrayInputStream(str);
                proxyStream.content_length = (long) str.length;
            }
        } catch (ProxyStream proxyStream2) {
            proxyStream2.printStackTrace();
        }
    }

    public ProxyStream getProxyStream(String str, Map<String, String> map) {
        try {
            String originalUrl = getTargetSource().getOriginalUrl();
            if (originalUrl.contains(".m3u8")) {
                Uri parse = Uri.parse(getTargetSource().getOriginalUrl());
                originalUrl = Uri.parse(str).buildUpon().scheme(parse.getScheme()).authority(parse.getAuthority()).build().toString();
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(originalUrl).openConnection();
            httpURLConnection.setInstanceFollowRedirects(true);
            map = map.entrySet().iterator();
            while (map.hasNext()) {
                Entry entry = (Entry) map.next();
                if (!(entry.getKey() == null || entry.getValue() == null || ((String) entry.getKey()).toLowerCase().equals("host") || ((String) entry.getKey()).toLowerCase().equals(TtmlNode.ATTR_TTS_ORIGIN) || ((String) entry.getKey()).toLowerCase().equals("chrome-proxy") || ((String) entry.getKey()).toLowerCase().equals("cast-device-capabilities") || ((String) entry.getKey()).toLowerCase().equals("user-agent") || ((String) entry.getKey()).toLowerCase().equals("http-client-ip") || ((String) entry.getKey()).toLowerCase().equals("remote-addr"))) {
                    httpURLConnection.addRequestProperty((String) entry.getKey(), (String) entry.getValue());
                }
            }
            httpURLConnection.addRequestProperty(HttpHeaders.REFERER, getTargetSource().getReferer() != null ? getTargetSource().getReferer() : getTargetSource().getOriginalUrl());
            httpURLConnection.addRequestProperty(HttpHeaders.USER_AGENT, BaseProvider.UserAgent);
            map = httpURLConnection.getResponseCode();
            ProxyStream proxyStream = new ProxyStream();
            proxyStream.content_length = (long) httpURLConnection.getContentLength();
            proxyStream.result_status = Status.lookup(map);
            proxyStream.inputStream = httpURLConnection.getInputStream();
            if (!(httpURLConnection.getContentEncoding() == null || httpURLConnection.getContentEncoding().contains("gzip") == null)) {
                proxyStream.inputStream = new GZIPInputStream(httpURLConnection.getInputStream());
            }
            fixSTRMGO(proxyStream, str);
            proxyStream.response_headers = new HashMap();
            for (Entry entry2 : httpURLConnection.getHeaderFields().entrySet()) {
                if (!(entry2.getKey() == null || ((String) entry2.getKey()).toLowerCase().startsWith("x-android-") || ((String) entry2.getKey()).toLowerCase().equals("content-length"))) {
                    if (((String) entry2.getKey()).toLowerCase().equals("content-type")) {
                        proxyStream.content_type = (String) ((List) entry2.getValue()).get(0);
                    } else {
                        proxyStream.response_headers.put(entry2.getKey(), ((List) entry2.getValue()).get(0));
                    }
                }
            }
            return proxyStream;
        } catch (String str2) {
            str2.printStackTrace();
            return new ProxyStream();
        }
    }

    public fi.iki.elonen.NanoHTTPD.Response serveMP4Stream(fi.iki.elonen.NanoHTTPD.IHTTPSession r6) {
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
        r5 = this;
        r0 = r6.getUri();	 Catch:{ Exception -> 0x004b }
        r6 = r6.getHeaders();	 Catch:{ Exception -> 0x004b }
        r6 = r5.getProxyStream(r0, r6);	 Catch:{ Exception -> 0x004b }
        r0 = r6.result_status;	 Catch:{ Exception -> 0x004b }
        r1 = r6.content_type;	 Catch:{ Exception -> 0x004b }
        r2 = r6.inputStream;	 Catch:{ Exception -> 0x004b }
        r3 = r6.content_length;	 Catch:{ Exception -> 0x004b }
        r0 = fi.iki.elonen.NanoHTTPD.newFixedLengthResponse(r0, r1, r2, r3);	 Catch:{ Exception -> 0x004b }
        r1 = r6.response_headers;	 Catch:{ Exception -> 0x004b }
        if (r1 == 0) goto L_0x004a;	 Catch:{ Exception -> 0x004b }
    L_0x001c:
        r1 = r6.response_headers;	 Catch:{ Exception -> 0x004b }
        r1 = r1.size();	 Catch:{ Exception -> 0x004b }
        if (r1 <= 0) goto L_0x004a;	 Catch:{ Exception -> 0x004b }
    L_0x0024:
        r6 = r6.response_headers;	 Catch:{ Exception -> 0x004b }
        r6 = r6.entrySet();	 Catch:{ Exception -> 0x004b }
        r6 = r6.iterator();	 Catch:{ Exception -> 0x004b }
    L_0x002e:
        r1 = r6.hasNext();	 Catch:{ Exception -> 0x004b }
        if (r1 == 0) goto L_0x004a;	 Catch:{ Exception -> 0x004b }
    L_0x0034:
        r1 = r6.next();	 Catch:{ Exception -> 0x004b }
        r1 = (java.util.Map.Entry) r1;	 Catch:{ Exception -> 0x004b }
        r2 = r1.getKey();	 Catch:{ Exception -> 0x004b }
        r2 = (java.lang.String) r2;	 Catch:{ Exception -> 0x004b }
        r1 = r1.getValue();	 Catch:{ Exception -> 0x004b }
        r1 = (java.lang.String) r1;	 Catch:{ Exception -> 0x004b }
        r0.addHeader(r2, r1);	 Catch:{ Exception -> 0x004b }
        goto L_0x002e;
    L_0x004a:
        return r0;
    L_0x004b:
        r6 = fi.iki.elonen.NanoHTTPD.Response.Status.FORBIDDEN;
        r0 = "text/plain";
        r1 = "Forbidden";
        r6 = fi.iki.elonen.NanoHTTPD.newFixedLengthResponse(r6, r0, r1);
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.morpheustv.service.HttpServer.serveMP4Stream(fi.iki.elonen.NanoHTTPD$IHTTPSession):fi.iki.elonen.NanoHTTPD$Response");
    }

    private InputStream getTorrentStream(long j) {
        try {
            Torrent currentTorrent = TorrentHelper.getCurrentTorrent();
            if (!(currentTorrent == null || currentTorrent.getVideoFile() == null)) {
                File videoFile = currentTorrent.getVideoFile();
                PrintStream printStream = System.out;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("InitDataSource at position ");
                stringBuilder.append(String.valueOf(j));
                stringBuilder.append(" of ");
                stringBuilder.append(String.valueOf(videoFile.length()));
                printStream.println(stringBuilder.toString());
                setInterestedBytes(currentTorrent, j, 10000);
                InputStream videoStream = currentTorrent.getVideoStream();
                videoStream.skip(j);
                return videoStream;
            }
        } catch (long j2) {
            j2.printStackTrace();
        }
        return new ByteArrayInputStream(new byte[0]);
    }

    private void setInterestedBytes(com.github.se_bastiaan.torrentstream.Torrent r5, long r6, int r8) {
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
        r4 = this;
        if (r5 == 0) goto L_0x0022;
    L_0x0002:
        r5.setInterestedBytes(r6);	 Catch:{ Exception -> 0x0022 }
        if (r8 <= 0) goto L_0x0022;	 Catch:{ Exception -> 0x0022 }
    L_0x0007:
        r6 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0022 }
        r0 = (long) r8;	 Catch:{ Exception -> 0x0022 }
        r2 = r6 + r0;	 Catch:{ Exception -> 0x0022 }
    L_0x000e:
        r6 = r5.hasInterestedBytes();	 Catch:{ Exception -> 0x0022 }
        if (r6 != 0) goto L_0x0022;	 Catch:{ Exception -> 0x0022 }
    L_0x0014:
        r6 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0022 }
        r8 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1));	 Catch:{ Exception -> 0x0022 }
        if (r8 >= 0) goto L_0x0022;	 Catch:{ Exception -> 0x0022 }
    L_0x001c:
        r6 = 100;	 Catch:{ Exception -> 0x0022 }
        java.lang.Thread.sleep(r6);	 Catch:{ Exception -> 0x0022 }
        goto L_0x000e;
    L_0x0022:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.morpheustv.service.HttpServer.setInterestedBytes(com.github.se_bastiaan.torrentstream.Torrent, long, int):void");
    }

    public fi.iki.elonen.NanoHTTPD.Response serveTorrentStream(fi.iki.elonen.NanoHTTPD.IHTTPSession r22) {
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
        r21 = this;
        r0 = r21;
        r1 = com.android.morpheustv.helpers.TorrentHelper.getCurrentTorrent();
        if (r1 == 0) goto L_0x0013;
    L_0x0008:
        r2 = r1.getVideoFile();
        if (r2 == 0) goto L_0x0013;
    L_0x000e:
        r1 = r1.getVideoFile();
        goto L_0x0014;
    L_0x0013:
        r1 = 0;
    L_0x0014:
        if (r1 != 0) goto L_0x0021;
    L_0x0016:
        r1 = fi.iki.elonen.NanoHTTPD.Response.Status.NOT_FOUND;
        r2 = "";
        r3 = "";
        r1 = fi.iki.elonen.NanoHTTPD.newFixedLengthResponse(r1, r2, r3);
        return r1;
    L_0x0021:
        r2 = "video/mp4";
        r3 = r22.getHeaders();
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01df }
        r4.<init>();	 Catch:{ Exception -> 0x01df }
        r5 = r1.getAbsolutePath();	 Catch:{ Exception -> 0x01df }
        r4.append(r5);	 Catch:{ Exception -> 0x01df }
        r5 = r1.length();	 Catch:{ Exception -> 0x01df }
        r4.append(r5);	 Catch:{ Exception -> 0x01df }
        r4 = r4.toString();	 Catch:{ Exception -> 0x01df }
        r4 = r4.hashCode();	 Catch:{ Exception -> 0x01df }
        r4 = java.lang.Integer.toHexString(r4);	 Catch:{ Exception -> 0x01df }
        r5 = -1;	 Catch:{ Exception -> 0x01df }
        r7 = "range";	 Catch:{ Exception -> 0x01df }
        r7 = r3.get(r7);	 Catch:{ Exception -> 0x01df }
        r7 = (java.lang.String) r7;	 Catch:{ Exception -> 0x01df }
        r8 = 1;	 Catch:{ Exception -> 0x01df }
        r9 = 0;	 Catch:{ Exception -> 0x01df }
        if (r7 == 0) goto L_0x0081;	 Catch:{ Exception -> 0x01df }
    L_0x0054:
        r12 = "bytes=";	 Catch:{ Exception -> 0x01df }
        r12 = r7.startsWith(r12);	 Catch:{ Exception -> 0x01df }
        if (r12 == 0) goto L_0x0081;	 Catch:{ Exception -> 0x01df }
    L_0x005c:
        r12 = "bytes=";	 Catch:{ Exception -> 0x01df }
        r12 = r12.length();	 Catch:{ Exception -> 0x01df }
        r7 = r7.substring(r12);	 Catch:{ Exception -> 0x01df }
        r12 = 45;	 Catch:{ Exception -> 0x01df }
        r12 = r7.indexOf(r12);	 Catch:{ Exception -> 0x01df }
        if (r12 <= 0) goto L_0x0081;
    L_0x006e:
        r13 = r7.substring(r9, r12);	 Catch:{ NumberFormatException -> 0x0081 }
        r13 = java.lang.Long.parseLong(r13);	 Catch:{ NumberFormatException -> 0x0081 }
        r12 = r12 + r8;
        r12 = r7.substring(r12);	 Catch:{ NumberFormatException -> 0x0083 }
        r15 = java.lang.Long.parseLong(r12);	 Catch:{ NumberFormatException -> 0x0083 }
        r5 = r15;
        goto L_0x0083;
    L_0x0081:
        r13 = 0;
    L_0x0083:
        r12 = "if-range";	 Catch:{ Exception -> 0x01df }
        r12 = r3.get(r12);	 Catch:{ Exception -> 0x01df }
        r12 = (java.lang.String) r12;	 Catch:{ Exception -> 0x01df }
        if (r12 == 0) goto L_0x0096;	 Catch:{ Exception -> 0x01df }
    L_0x008d:
        r12 = r4.equals(r12);	 Catch:{ Exception -> 0x01df }
        if (r12 == 0) goto L_0x0094;	 Catch:{ Exception -> 0x01df }
    L_0x0093:
        goto L_0x0096;	 Catch:{ Exception -> 0x01df }
    L_0x0094:
        r12 = 0;	 Catch:{ Exception -> 0x01df }
        goto L_0x0097;	 Catch:{ Exception -> 0x01df }
    L_0x0096:
        r12 = 1;	 Catch:{ Exception -> 0x01df }
    L_0x0097:
        r15 = "if-none-match";	 Catch:{ Exception -> 0x01df }
        r3 = r3.get(r15);	 Catch:{ Exception -> 0x01df }
        r3 = (java.lang.String) r3;	 Catch:{ Exception -> 0x01df }
        if (r3 == 0) goto L_0x00b0;	 Catch:{ Exception -> 0x01df }
    L_0x00a1:
        r15 = "*";	 Catch:{ Exception -> 0x01df }
        r15 = r15.equals(r3);	 Catch:{ Exception -> 0x01df }
        if (r15 != 0) goto L_0x00b1;	 Catch:{ Exception -> 0x01df }
    L_0x00a9:
        r3 = r3.equals(r4);	 Catch:{ Exception -> 0x01df }
        if (r3 == 0) goto L_0x00b0;	 Catch:{ Exception -> 0x01df }
    L_0x00af:
        goto L_0x00b1;	 Catch:{ Exception -> 0x01df }
    L_0x00b0:
        r8 = 0;	 Catch:{ Exception -> 0x01df }
    L_0x00b1:
        r10 = r1.length();	 Catch:{ Exception -> 0x01df }
        if (r12 == 0) goto L_0x0140;	 Catch:{ Exception -> 0x01df }
    L_0x00b7:
        if (r7 == 0) goto L_0x0140;	 Catch:{ Exception -> 0x01df }
    L_0x00b9:
        r15 = 0;	 Catch:{ Exception -> 0x01df }
        r1 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1));	 Catch:{ Exception -> 0x01df }
        if (r1 < 0) goto L_0x0140;	 Catch:{ Exception -> 0x01df }
    L_0x00bf:
        r1 = (r13 > r10 ? 1 : (r13 == r10 ? 0 : -1));	 Catch:{ Exception -> 0x01df }
        if (r1 >= 0) goto L_0x0140;	 Catch:{ Exception -> 0x01df }
    L_0x00c3:
        if (r8 == 0) goto L_0x00d4;	 Catch:{ Exception -> 0x01df }
    L_0x00c5:
        r1 = fi.iki.elonen.NanoHTTPD.Response.Status.NOT_MODIFIED;	 Catch:{ Exception -> 0x01df }
        r3 = "";	 Catch:{ Exception -> 0x01df }
        r1 = fi.iki.elonen.NanoHTTPD.newFixedLengthResponse(r1, r2, r3);	 Catch:{ Exception -> 0x01df }
        r2 = "ETag";	 Catch:{ Exception -> 0x01df }
        r1.addHeader(r2, r4);	 Catch:{ Exception -> 0x01df }
        goto L_0x01c0;	 Catch:{ Exception -> 0x01df }
    L_0x00d4:
        r7 = 0;	 Catch:{ Exception -> 0x01df }
        r1 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));	 Catch:{ Exception -> 0x01df }
        r15 = 1;	 Catch:{ Exception -> 0x01df }
        if (r1 >= 0) goto L_0x00de;	 Catch:{ Exception -> 0x01df }
    L_0x00dc:
        r5 = r10 - r15;	 Catch:{ Exception -> 0x01df }
    L_0x00de:
        r1 = 0;	 Catch:{ Exception -> 0x01df }
        r17 = r5 - r13;	 Catch:{ Exception -> 0x01df }
        r19 = r17 + r15;	 Catch:{ Exception -> 0x01df }
        r1 = (r19 > r7 ? 1 : (r19 == r7 ? 0 : -1));	 Catch:{ Exception -> 0x01df }
        if (r1 >= 0) goto L_0x00ea;	 Catch:{ Exception -> 0x01df }
    L_0x00e7:
        r7 = 0;	 Catch:{ Exception -> 0x01df }
        goto L_0x00ec;	 Catch:{ Exception -> 0x01df }
    L_0x00ea:
        r7 = r19;	 Catch:{ Exception -> 0x01df }
    L_0x00ec:
        r1 = r0.getTorrentStream(r13);	 Catch:{ Exception -> 0x01df }
        r3 = fi.iki.elonen.NanoHTTPD.Response.Status.PARTIAL_CONTENT;	 Catch:{ Exception -> 0x01df }
        r1 = fi.iki.elonen.NanoHTTPD.newFixedLengthResponse(r3, r2, r1, r7);	 Catch:{ Exception -> 0x01df }
        r2 = "Accept-Ranges";	 Catch:{ Exception -> 0x01df }
        r3 = "bytes";	 Catch:{ Exception -> 0x01df }
        r1.addHeader(r2, r3);	 Catch:{ Exception -> 0x01df }
        r2 = "Content-Length";	 Catch:{ Exception -> 0x01df }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01df }
        r3.<init>();	 Catch:{ Exception -> 0x01df }
        r9 = "";	 Catch:{ Exception -> 0x01df }
        r3.append(r9);	 Catch:{ Exception -> 0x01df }
        r3.append(r7);	 Catch:{ Exception -> 0x01df }
        r3 = r3.toString();	 Catch:{ Exception -> 0x01df }
        r1.addHeader(r2, r3);	 Catch:{ Exception -> 0x01df }
        r2 = "Content-Range";	 Catch:{ Exception -> 0x01df }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01df }
        r3.<init>();	 Catch:{ Exception -> 0x01df }
        r7 = "bytes ";	 Catch:{ Exception -> 0x01df }
        r3.append(r7);	 Catch:{ Exception -> 0x01df }
        r3.append(r13);	 Catch:{ Exception -> 0x01df }
        r7 = "-";	 Catch:{ Exception -> 0x01df }
        r3.append(r7);	 Catch:{ Exception -> 0x01df }
        r3.append(r5);	 Catch:{ Exception -> 0x01df }
        r5 = "/";	 Catch:{ Exception -> 0x01df }
        r3.append(r5);	 Catch:{ Exception -> 0x01df }
        r3.append(r10);	 Catch:{ Exception -> 0x01df }
        r3 = r3.toString();	 Catch:{ Exception -> 0x01df }
        r1.addHeader(r2, r3);	 Catch:{ Exception -> 0x01df }
        r2 = "ETag";	 Catch:{ Exception -> 0x01df }
        r1.addHeader(r2, r4);	 Catch:{ Exception -> 0x01df }
        goto L_0x01c0;	 Catch:{ Exception -> 0x01df }
    L_0x0140:
        if (r12 == 0) goto L_0x016e;	 Catch:{ Exception -> 0x01df }
    L_0x0142:
        if (r7 == 0) goto L_0x016e;	 Catch:{ Exception -> 0x01df }
    L_0x0144:
        r1 = (r13 > r10 ? 1 : (r13 == r10 ? 0 : -1));	 Catch:{ Exception -> 0x01df }
        if (r1 < 0) goto L_0x016e;	 Catch:{ Exception -> 0x01df }
    L_0x0148:
        r1 = fi.iki.elonen.NanoHTTPD.Response.Status.RANGE_NOT_SATISFIABLE;	 Catch:{ Exception -> 0x01df }
        r2 = "text/plain";	 Catch:{ Exception -> 0x01df }
        r3 = "";	 Catch:{ Exception -> 0x01df }
        r1 = fi.iki.elonen.NanoHTTPD.newFixedLengthResponse(r1, r2, r3);	 Catch:{ Exception -> 0x01df }
        r2 = "Content-Range";	 Catch:{ Exception -> 0x01df }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01df }
        r3.<init>();	 Catch:{ Exception -> 0x01df }
        r5 = "bytes */";	 Catch:{ Exception -> 0x01df }
        r3.append(r5);	 Catch:{ Exception -> 0x01df }
        r3.append(r10);	 Catch:{ Exception -> 0x01df }
        r3 = r3.toString();	 Catch:{ Exception -> 0x01df }
        r1.addHeader(r2, r3);	 Catch:{ Exception -> 0x01df }
        r2 = "ETag";	 Catch:{ Exception -> 0x01df }
        r1.addHeader(r2, r4);	 Catch:{ Exception -> 0x01df }
        goto L_0x01c0;	 Catch:{ Exception -> 0x01df }
    L_0x016e:
        if (r7 != 0) goto L_0x0180;	 Catch:{ Exception -> 0x01df }
    L_0x0170:
        if (r8 == 0) goto L_0x0180;	 Catch:{ Exception -> 0x01df }
    L_0x0172:
        r1 = fi.iki.elonen.NanoHTTPD.Response.Status.NOT_MODIFIED;	 Catch:{ Exception -> 0x01df }
        r3 = "";	 Catch:{ Exception -> 0x01df }
        r1 = fi.iki.elonen.NanoHTTPD.newFixedLengthResponse(r1, r2, r3);	 Catch:{ Exception -> 0x01df }
        r2 = "ETag";	 Catch:{ Exception -> 0x01df }
        r1.addHeader(r2, r4);	 Catch:{ Exception -> 0x01df }
        goto L_0x01c0;	 Catch:{ Exception -> 0x01df }
    L_0x0180:
        if (r12 != 0) goto L_0x0192;	 Catch:{ Exception -> 0x01df }
    L_0x0182:
        if (r8 == 0) goto L_0x0192;	 Catch:{ Exception -> 0x01df }
    L_0x0184:
        r1 = fi.iki.elonen.NanoHTTPD.Response.Status.NOT_MODIFIED;	 Catch:{ Exception -> 0x01df }
        r3 = "";	 Catch:{ Exception -> 0x01df }
        r1 = fi.iki.elonen.NanoHTTPD.newFixedLengthResponse(r1, r2, r3);	 Catch:{ Exception -> 0x01df }
        r2 = "ETag";	 Catch:{ Exception -> 0x01df }
        r1.addHeader(r2, r4);	 Catch:{ Exception -> 0x01df }
        goto L_0x01c0;	 Catch:{ Exception -> 0x01df }
    L_0x0192:
        r5 = 0;	 Catch:{ Exception -> 0x01df }
        r1 = r0.getTorrentStream(r5);	 Catch:{ Exception -> 0x01df }
        r3 = fi.iki.elonen.NanoHTTPD.Response.Status.OK;	 Catch:{ Exception -> 0x01df }
        r1 = fi.iki.elonen.NanoHTTPD.newFixedLengthResponse(r3, r2, r1, r10);	 Catch:{ Exception -> 0x01df }
        r2 = "Accept-Ranges";	 Catch:{ Exception -> 0x01df }
        r3 = "bytes";	 Catch:{ Exception -> 0x01df }
        r1.addHeader(r2, r3);	 Catch:{ Exception -> 0x01df }
        r2 = "Content-Length";	 Catch:{ Exception -> 0x01df }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01df }
        r3.<init>();	 Catch:{ Exception -> 0x01df }
        r5 = "";	 Catch:{ Exception -> 0x01df }
        r3.append(r5);	 Catch:{ Exception -> 0x01df }
        r3.append(r10);	 Catch:{ Exception -> 0x01df }
        r3 = r3.toString();	 Catch:{ Exception -> 0x01df }
        r1.addHeader(r2, r3);	 Catch:{ Exception -> 0x01df }
        r2 = "ETag";	 Catch:{ Exception -> 0x01df }
        r1.addHeader(r2, r4);	 Catch:{ Exception -> 0x01df }
    L_0x01c0:
        r2 = java.lang.System.out;	 Catch:{ Exception -> 0x01df }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x01df }
        r3.<init>();	 Catch:{ Exception -> 0x01df }
        r4 = "RESPONSE: ";	 Catch:{ Exception -> 0x01df }
        r3.append(r4);	 Catch:{ Exception -> 0x01df }
        r4 = r1.getStatus();	 Catch:{ Exception -> 0x01df }
        r4 = r4.getDescription();	 Catch:{ Exception -> 0x01df }
        r3.append(r4);	 Catch:{ Exception -> 0x01df }
        r3 = r3.toString();	 Catch:{ Exception -> 0x01df }
        r2.println(r3);	 Catch:{ Exception -> 0x01df }
        return r1;
    L_0x01df:
        r1 = java.lang.System.out;
        r2 = "RESPONSE: Forbidden";
        r1.println(r2);
        r1 = fi.iki.elonen.NanoHTTPD.Response.Status.FORBIDDEN;
        r2 = "text/plain";
        r3 = "Forbidden";
        r1 = fi.iki.elonen.NanoHTTPD.newFixedLengthResponse(r1, r2, r3);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.morpheustv.service.HttpServer.serveTorrentStream(fi.iki.elonen.NanoHTTPD$IHTTPSession):fi.iki.elonen.NanoHTTPD$Response");
    }

    private java.lang.String readSource(java.io.File r7) {
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
        r6 = this;
        r0 = 0;
        r1 = new java.io.FileReader;	 Catch:{ Exception -> 0x004a, all -> 0x003b }
        r1.<init>(r7);	 Catch:{ Exception -> 0x004a, all -> 0x003b }
        r7 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0039, all -> 0x0034 }
        r7.<init>(r1);	 Catch:{ Exception -> 0x0039, all -> 0x0034 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x004c, all -> 0x0032 }
        r2.<init>();	 Catch:{ Exception -> 0x004c, all -> 0x0032 }
    L_0x0010:
        r3 = r7.readLine();	 Catch:{ Exception -> 0x004c, all -> 0x0032 }
        if (r3 == 0) goto L_0x001e;	 Catch:{ Exception -> 0x004c, all -> 0x0032 }
    L_0x0016:
        r2.append(r3);	 Catch:{ Exception -> 0x004c, all -> 0x0032 }
        r4 = "\n";	 Catch:{ Exception -> 0x004c, all -> 0x0032 }
        r2.append(r4);	 Catch:{ Exception -> 0x004c, all -> 0x0032 }
    L_0x001e:
        if (r3 != 0) goto L_0x0010;	 Catch:{ Exception -> 0x004c, all -> 0x0032 }
    L_0x0020:
        r7.close();	 Catch:{ Exception -> 0x004c, all -> 0x0032 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x004c, all -> 0x0032 }
        if (r1 == 0) goto L_0x002c;
    L_0x0029:
        r1.close();	 Catch:{ IOException -> 0x0031 }
    L_0x002c:
        if (r7 == 0) goto L_0x0031;	 Catch:{ IOException -> 0x0031 }
    L_0x002e:
        r7.close();	 Catch:{ IOException -> 0x0031 }
    L_0x0031:
        return r2;
    L_0x0032:
        r0 = move-exception;
        goto L_0x003f;
    L_0x0034:
        r7 = move-exception;
        r5 = r0;
        r0 = r7;
        r7 = r5;
        goto L_0x003f;
    L_0x0039:
        r7 = r0;
        goto L_0x004c;
    L_0x003b:
        r7 = move-exception;
        r1 = r0;
        r0 = r7;
        r7 = r1;
    L_0x003f:
        if (r1 == 0) goto L_0x0044;
    L_0x0041:
        r1.close();	 Catch:{ IOException -> 0x0049 }
    L_0x0044:
        if (r7 == 0) goto L_0x0049;	 Catch:{ IOException -> 0x0049 }
    L_0x0046:
        r7.close();	 Catch:{ IOException -> 0x0049 }
    L_0x0049:
        throw r0;
    L_0x004a:
        r7 = r0;
        r1 = r7;
    L_0x004c:
        if (r1 == 0) goto L_0x0051;
    L_0x004e:
        r1.close();	 Catch:{ IOException -> 0x0056 }
    L_0x0051:
        if (r7 == 0) goto L_0x0056;	 Catch:{ IOException -> 0x0056 }
    L_0x0053:
        r7.close();	 Catch:{ IOException -> 0x0056 }
    L_0x0056:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.morpheustv.service.HttpServer.readSource(java.io.File):java.lang.String");
    }

    public Response serveSubtitles(IHTTPSession iHTTPSession) {
        if (!iHTTPSession.getParameters().containsKey(TtmlNode.ATTR_ID)) {
            return NanoHTTPD.newFixedLengthResponse(Status.BAD_REQUEST, NanoHTTPD.MIME_HTML, "Bad Request");
        }
        byte[] bArr = new byte[0];
        try {
            String str = (String) ((List) iHTTPSession.getParameters().get(TtmlNode.ATTR_ID)).get(0);
            SubtitleResult subtitleResult = (SubtitleResult) new Gson().fromJson(Base64.decodeAsString(str), SubtitleResult.class);
            iHTTPSession = Utils.md5(str);
            String str2 = null;
            if (subtitleResult.provider.equals("OPENSUBTITLES")) {
                OpenSubtitles openSubtitles = new OpenSubtitles();
                String subtitleID = subtitleResult.getSubtitleID();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(iHTTPSession);
                stringBuilder.append(".tmp");
                str2 = openSubtitles.downloadSubtitle(subtitleID, stringBuilder.toString(), false);
            }
            if (subtitleResult.provider.equals("LEGENDASDIVX")) {
                str2 = new LegendasDivx().download_subtitle(subtitleResult, iHTTPSession);
            }
            if (subtitleResult.provider.equals("TVSUBS")) {
                str2 = new TVSubs().download_subtitle(subtitleResult, iHTTPSession);
            }
            if (!(str2 == null || str2.isEmpty())) {
                File file = new File(Environment.getExternalStorageDirectory(), "/morpheustv/");
                File file2 = new File(str2);
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(iHTTPSession);
                stringBuilder2.append("_tmp.vtt");
                File file3 = new File(file, stringBuilder2.toString());
                new Srt2Vtt().convert(new FileReader(file2), new FileWriter(file3));
                iHTTPSession = readSource(file3);
                file3.delete();
                file2.delete();
                bArr = iHTTPSession.getBytes("UTF-8");
            }
        } catch (IHTTPSession iHTTPSession2) {
            iHTTPSession2.printStackTrace();
        }
        return NanoHTTPD.newFixedLengthResponse(Status.OK, MimeTypes.TEXT_VTT, new ByteArrayInputStream(bArr), (long) bArr.length);
    }

    private fi.iki.elonen.NanoHTTPD.Response serveDownloadedFile(fi.iki.elonen.NanoHTTPD.IHTTPSession r23) {
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
        r22 = this;
        r6 = r22;
        r2 = "video/mp4";	 Catch:{ IOException -> 0x0164 }
        r0 = new java.io.File;	 Catch:{ IOException -> 0x0164 }
        r1 = getTargetDownloadedFile();	 Catch:{ IOException -> 0x0164 }
        r0.<init>(r1);	 Catch:{ IOException -> 0x0164 }
        r1 = r23.getHeaders();	 Catch:{ IOException -> 0x0164 }
        r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0164 }
        r3.<init>();	 Catch:{ IOException -> 0x0164 }
        r4 = r0.getAbsolutePath();	 Catch:{ IOException -> 0x0164 }
        r3.append(r4);	 Catch:{ IOException -> 0x0164 }
        r4 = r0.lastModified();	 Catch:{ IOException -> 0x0164 }
        r3.append(r4);	 Catch:{ IOException -> 0x0164 }
        r4 = "";	 Catch:{ IOException -> 0x0164 }
        r3.append(r4);	 Catch:{ IOException -> 0x0164 }
        r4 = r0.length();	 Catch:{ IOException -> 0x0164 }
        r3.append(r4);	 Catch:{ IOException -> 0x0164 }
        r3 = r3.toString();	 Catch:{ IOException -> 0x0164 }
        r3 = r3.hashCode();	 Catch:{ IOException -> 0x0164 }
        r7 = java.lang.Integer.toHexString(r3);	 Catch:{ IOException -> 0x0164 }
        r3 = -1;	 Catch:{ IOException -> 0x0164 }
        r5 = "range";	 Catch:{ IOException -> 0x0164 }
        r5 = r1.get(r5);	 Catch:{ IOException -> 0x0164 }
        r5 = (java.lang.String) r5;	 Catch:{ IOException -> 0x0164 }
        r8 = 0;	 Catch:{ IOException -> 0x0164 }
        if (r5 == 0) goto L_0x0079;	 Catch:{ IOException -> 0x0164 }
    L_0x004a:
        r10 = "bytes=";	 Catch:{ IOException -> 0x0164 }
        r10 = r5.startsWith(r10);	 Catch:{ IOException -> 0x0164 }
        if (r10 == 0) goto L_0x0079;	 Catch:{ IOException -> 0x0164 }
    L_0x0052:
        r10 = "bytes=";	 Catch:{ IOException -> 0x0164 }
        r10 = r10.length();	 Catch:{ IOException -> 0x0164 }
        r5 = r5.substring(r10);	 Catch:{ IOException -> 0x0164 }
        r10 = 45;	 Catch:{ IOException -> 0x0164 }
        r10 = r5.indexOf(r10);	 Catch:{ IOException -> 0x0164 }
        if (r10 <= 0) goto L_0x0079;
    L_0x0064:
        r11 = 0;
        r11 = r5.substring(r11, r10);	 Catch:{ NumberFormatException -> 0x0079 }
        r11 = java.lang.Long.parseLong(r11);	 Catch:{ NumberFormatException -> 0x0079 }
        r10 = r10 + 1;
        r10 = r5.substring(r10);	 Catch:{ NumberFormatException -> 0x007a }
        r13 = java.lang.Long.parseLong(r10);	 Catch:{ NumberFormatException -> 0x007a }
        r3 = r13;
        goto L_0x007a;
    L_0x0079:
        r11 = r8;
    L_0x007a:
        r13 = r0.length();	 Catch:{ IOException -> 0x0164 }
        if (r5 == 0) goto L_0x011e;	 Catch:{ IOException -> 0x0164 }
    L_0x0080:
        r5 = (r11 > r8 ? 1 : (r11 == r8 ? 0 : -1));	 Catch:{ IOException -> 0x0164 }
        if (r5 < 0) goto L_0x011e;	 Catch:{ IOException -> 0x0164 }
    L_0x0084:
        r1 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1));	 Catch:{ IOException -> 0x0164 }
        if (r1 < 0) goto L_0x00b0;	 Catch:{ IOException -> 0x0164 }
    L_0x0088:
        r0 = fi.iki.elonen.NanoHTTPD.Response.Status.RANGE_NOT_SATISFIABLE;	 Catch:{ IOException -> 0x0164 }
        r1 = "text/plain";	 Catch:{ IOException -> 0x0164 }
        r2 = "";	 Catch:{ IOException -> 0x0164 }
        r0 = r6.createResponse(r0, r1, r2);	 Catch:{ IOException -> 0x0164 }
        r1 = "Content-Range";	 Catch:{ IOException -> 0x0164 }
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0164 }
        r2.<init>();	 Catch:{ IOException -> 0x0164 }
        r3 = "bytes 0-0/";	 Catch:{ IOException -> 0x0164 }
        r2.append(r3);	 Catch:{ IOException -> 0x0164 }
        r2.append(r13);	 Catch:{ IOException -> 0x0164 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x0164 }
        r0.addHeader(r1, r2);	 Catch:{ IOException -> 0x0164 }
        r1 = "ETag";	 Catch:{ IOException -> 0x0164 }
        r0.addHeader(r1, r7);	 Catch:{ IOException -> 0x0164 }
        r7 = r6;	 Catch:{ IOException -> 0x0164 }
        goto L_0x016b;	 Catch:{ IOException -> 0x0164 }
    L_0x00b0:
        r1 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1));	 Catch:{ IOException -> 0x0164 }
        r15 = 1;	 Catch:{ IOException -> 0x0164 }
        if (r1 >= 0) goto L_0x00b8;	 Catch:{ IOException -> 0x0164 }
    L_0x00b6:
        r3 = r13 - r15;	 Catch:{ IOException -> 0x0164 }
    L_0x00b8:
        r4 = r3;	 Catch:{ IOException -> 0x0164 }
        r1 = 0;	 Catch:{ IOException -> 0x0164 }
        r17 = r4 - r11;	 Catch:{ IOException -> 0x0164 }
        r19 = r17 + r15;	 Catch:{ IOException -> 0x0164 }
        r1 = (r19 > r8 ? 1 : (r19 == r8 ? 0 : -1));	 Catch:{ IOException -> 0x0164 }
        if (r1 >= 0) goto L_0x00c3;	 Catch:{ IOException -> 0x0164 }
    L_0x00c2:
        goto L_0x00c5;	 Catch:{ IOException -> 0x0164 }
    L_0x00c3:
        r8 = r19;	 Catch:{ IOException -> 0x0164 }
    L_0x00c5:
        r3 = new com.android.morpheustv.service.HttpServer$1;	 Catch:{ IOException -> 0x0164 }
        r3.<init>(r0, r8);	 Catch:{ IOException -> 0x0164 }
        r3.skip(r11);	 Catch:{ IOException -> 0x0164 }
        r1 = fi.iki.elonen.NanoHTTPD.Response.Status.PARTIAL_CONTENT;	 Catch:{ IOException -> 0x0164 }
        r0 = r6;
        r21 = r7;
        r6 = r4;
        r4 = r8;
        r0 = r0.createResponse(r1, r2, r3, r4);	 Catch:{ IOException -> 0x0161 }
        r1 = "Content-Length";	 Catch:{ IOException -> 0x0161 }
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0161 }
        r2.<init>();	 Catch:{ IOException -> 0x0161 }
        r3 = "";	 Catch:{ IOException -> 0x0161 }
        r2.append(r3);	 Catch:{ IOException -> 0x0161 }
        r2.append(r8);	 Catch:{ IOException -> 0x0161 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x0161 }
        r0.addHeader(r1, r2);	 Catch:{ IOException -> 0x0161 }
        r1 = "Content-Range";	 Catch:{ IOException -> 0x0161 }
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0161 }
        r2.<init>();	 Catch:{ IOException -> 0x0161 }
        r3 = "bytes ";	 Catch:{ IOException -> 0x0161 }
        r2.append(r3);	 Catch:{ IOException -> 0x0161 }
        r2.append(r11);	 Catch:{ IOException -> 0x0161 }
        r3 = "-";	 Catch:{ IOException -> 0x0161 }
        r2.append(r3);	 Catch:{ IOException -> 0x0161 }
        r2.append(r6);	 Catch:{ IOException -> 0x0161 }
        r3 = "/";	 Catch:{ IOException -> 0x0161 }
        r2.append(r3);	 Catch:{ IOException -> 0x0161 }
        r2.append(r13);	 Catch:{ IOException -> 0x0161 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x0161 }
        r0.addHeader(r1, r2);	 Catch:{ IOException -> 0x0161 }
        r1 = "ETag";	 Catch:{ IOException -> 0x0161 }
        r6 = r21;	 Catch:{ IOException -> 0x0161 }
        r0.addHeader(r1, r6);	 Catch:{ IOException -> 0x0161 }
        r7 = r22;	 Catch:{ IOException -> 0x0161 }
        goto L_0x016b;	 Catch:{ IOException -> 0x0161 }
    L_0x011e:
        r6 = r7;	 Catch:{ IOException -> 0x0161 }
        r3 = "if-none-match";	 Catch:{ IOException -> 0x0161 }
        r1 = r1.get(r3);	 Catch:{ IOException -> 0x0161 }
        r1 = r6.equals(r1);	 Catch:{ IOException -> 0x0161 }
        if (r1 == 0) goto L_0x0136;	 Catch:{ IOException -> 0x0161 }
    L_0x012b:
        r0 = fi.iki.elonen.NanoHTTPD.Response.Status.NOT_MODIFIED;	 Catch:{ IOException -> 0x0161 }
        r1 = "";	 Catch:{ IOException -> 0x0161 }
        r7 = r22;
        r0 = r7.createResponse(r0, r2, r1);	 Catch:{ IOException -> 0x0165 }
        goto L_0x016b;	 Catch:{ IOException -> 0x0165 }
    L_0x0136:
        r7 = r22;	 Catch:{ IOException -> 0x0165 }
        r1 = fi.iki.elonen.NanoHTTPD.Response.Status.OK;	 Catch:{ IOException -> 0x0165 }
        r3 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x0165 }
        r3.<init>(r0);	 Catch:{ IOException -> 0x0165 }
        r0 = r7;	 Catch:{ IOException -> 0x0165 }
        r4 = r13;	 Catch:{ IOException -> 0x0165 }
        r0 = r0.createResponse(r1, r2, r3, r4);	 Catch:{ IOException -> 0x0165 }
        r1 = "Content-Length";	 Catch:{ IOException -> 0x0165 }
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0165 }
        r2.<init>();	 Catch:{ IOException -> 0x0165 }
        r3 = "";	 Catch:{ IOException -> 0x0165 }
        r2.append(r3);	 Catch:{ IOException -> 0x0165 }
        r2.append(r13);	 Catch:{ IOException -> 0x0165 }
        r2 = r2.toString();	 Catch:{ IOException -> 0x0165 }
        r0.addHeader(r1, r2);	 Catch:{ IOException -> 0x0165 }
        r1 = "ETag";	 Catch:{ IOException -> 0x0165 }
        r0.addHeader(r1, r6);	 Catch:{ IOException -> 0x0165 }
        goto L_0x016b;
    L_0x0161:
        r7 = r22;
        goto L_0x0165;
    L_0x0164:
        r7 = r6;
    L_0x0165:
        r0 = "Forbidden: Reading file failed";
        r0 = r7.getResponse(r0);
    L_0x016b:
        if (r0 != 0) goto L_0x0173;
    L_0x016d:
        r0 = "Error 404: File not found";
        r0 = r7.getResponse(r0);
    L_0x0173:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.morpheustv.service.HttpServer.serveDownloadedFile(fi.iki.elonen.NanoHTTPD$IHTTPSession):fi.iki.elonen.NanoHTTPD$Response");
    }

    private Response createResponse(Status status, String str, InputStream inputStream, long j) {
        status = NanoHTTPD.newFixedLengthResponse(status, str, inputStream, j);
        status.addHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
        return status;
    }

    private Response createResponse(Status status, String str, String str2) {
        status = NanoHTTPD.newFixedLengthResponse(status, str, str2);
        status.addHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
        return status;
    }

    private Response getResponse(String str) {
        return createResponse(Status.OK, NanoHTTPD.MIME_PLAINTEXT, str);
    }

    private Response addCORSHeaders(Response response) {
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, System.getProperty(ACCESS_CONTROL_ALLOW_HEADER_PROPERTY_NAME, DEFAULT_ALLOWED_HEADERS));
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHODS);
        response.addHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "151200");
        return response;
    }
}
