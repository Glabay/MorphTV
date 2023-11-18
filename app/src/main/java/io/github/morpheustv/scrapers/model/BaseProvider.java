package io.github.morpheustv.scrapers.model;

import android.content.Context;
import android.support.graphics.drawable.PathInterpolatorCompat;
import android.webkit.CookieManager;
import android.webkit.WebView;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.common.net.HttpHeaders;
import com.google.common.net.InternetDomainName;
import com.squareup.duktape.Duktape;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.helper.TaskManager;
import io.github.morpheustv.scrapers.helper.Utils;
import io.github.morpheustv.scrapers.resolvers.EntervideoResolver;
import io.github.morpheustv.scrapers.resolvers.GvideoResolver;
import io.github.morpheustv.scrapers.resolvers.HaxhitsResolver;
import io.github.morpheustv.scrapers.resolvers.SpeedvidResolver;
import io.github.morpheustv.scrapers.resolvers.VidcloudResolver;
import io.github.morpheustv.scrapers.resolvers.VidloxResolver;
import io.github.morpheustv.scrapers.resolvers.VidnodeResolver;
import io.github.morpheustv.scrapers.resolvers.VidozaResolver;
import io.github.morpheustv.scrapers.resolvers.VidupResolver;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public abstract class BaseProvider {
    public static final String FULLHD = "1080p";
    public static final String HD = "HD";
    public static final String SD = "SD";
    public static final String UNKNOWN_QUALITY = "UNKNOWN";
    public static String UserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36";
    public static int runningResolvers;
    public static int runningVerifiers;
    public String PROVIDER_NAME = "BaseProvider";
    public String evalResult = "";
    public HashMap<String, String> extraHeaders = new HashMap();
    public boolean finished = false;
    public boolean handlingRecaptcha = false;
    public boolean isBusy = false;
    public boolean isTvShow = false;
    public boolean isWaitingFinish = false;
    public CopyOnWriteArrayList<String> loadedResources = new CopyOnWriteArrayList();
    public Context mContext;
    public String[] resourceWhitelist = new String[0];
    private Scraper scraper;
    public String[] staticResourceWhitelist = new String[]{"(.*chk_jschl.*)", "(.*openload.*.js.*)", "(.*checkInventory.*)", "(.*easylist.*)", "(.*streamango.*.js.*)", "(.*streamgo.*.js.*)", "(.*vidup.*)", "(.*player.*.js.*)", "(.*haxhits.*)"};
    public String urlLoading = "";
    public boolean usesWebview = false;
    public WebView webView;

    public interface OnStreamInfoResolved {
        void OnResolvedInfo(StreamInfo streamInfo);
    }

    private interface JSUnpack {
        String decode(String str);
    }

    public static class StreamInfo {
        public long content_length = 0;
        public String content_type = "";
        public Map<String, String> cookie;
        public boolean error = false;
        public long resolve_time = 0;
        public String resolved_url = "";
    }

    public ProviderSearchResult getEpisode(ProviderSearchResult providerSearchResult, List<String> list, String str, int i, int i2) {
        return null;
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        return null;
    }

    public void getSources(ProviderSearchResult providerSearchResult, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
    }

    public ProviderSearchResult getTvShow(List<String> list, String str, String str2) {
        return null;
    }

    public BaseProvider(Scraper scraper, String str, boolean z) {
        this.scraper = scraper;
        this.usesWebview = z;
        this.mContext = getConfig().getContext();
        this.PROVIDER_NAME = str;
    }

    public void log(String str) {
        this.scraper.Log(this.PROVIDER_NAME, str);
    }

    public Scraper getScraper() {
        return this.scraper;
    }

    public ScraperConfig getConfig() {
        return this.scraper.getConfig();
    }

    public Executor getExecutor() {
        if (this.usesWebview) {
            return TaskManager.WEBVIEW_EXECUTOR;
        }
        return TaskManager.EXECUTOR;
    }

    public static String cleantitle(String str) {
        if (str == null) {
            return str;
        }
        return Pattern.compile("\\n|(\\[\\[\\].+?\\[\\]\\])|(\\[(\\].+?\\[)\\])|\\s(vs|v[.])\\s|(:|;|-|\"|,|'|_|\\.|\\?)|\\s").matcher(Pattern.compile("(&#[0-9]+)([^;^0-9]+)").matcher(Pattern.compile("&#(\\d+);").matcher(str).replaceAll("")).replaceAll("\\1;\\2").replace("&quot;", "\"").replace("&amp;", "&").replace("(", "").replace(")", "").replace("'", "").replace("`", "").replace("´", "").replace("’", "")).replaceAll("").toLowerCase();
    }

    public String cleantitleurl(String str) {
        return str.replace("-", "").replace("/", "").replace(":", "").replace("*", "").replace(".", "").replace("?", "").replace("\"", "").replace("'", "").replace("<", "").replace(">", "").replace("!", "").replace(",", "").replace(StringUtils.SPACE, "-").replace("--", "-").toLowerCase();
    }

    public String cleantitlequery(String str) {
        return str.replace("/", "").replace(":", "").replace("*", "").replace(".", "").replace("?", "").replace("\"", "").replace("'", "").replace("<", "").replace(">", "").replace("!", "").replace(",", "").replace(StringUtils.SPACE, "+").replace("-", "").toLowerCase();
    }

    public String cleantitleuri(String str) {
        return str.replace("/", "").replace(":", "").replace("*", "").replace(".", "").replace("?", "").replace("\"", "").replace("'", "").replace("<", "").replace(">", "").replace("!", "").replace(",", "").replace("+", "%2B").replace("-", "").replace(StringUtils.SPACE, "%20").toLowerCase();
    }

    public java.lang.String getLabelQuality(java.lang.String r5) {
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
        r0 = "UNKNOWN";
        if (r5 == 0) goto L_0x004a;
    L_0x0004:
        r1 = r5.isEmpty();	 Catch:{ Exception -> 0x004a }
        if (r1 != 0) goto L_0x004a;	 Catch:{ Exception -> 0x004a }
    L_0x000a:
        r1 = r5.toLowerCase();	 Catch:{ Exception -> 0x004a }
        r2 = "p";	 Catch:{ Exception -> 0x004a }
        r1 = r1.endsWith(r2);	 Catch:{ Exception -> 0x004a }
        if (r1 == 0) goto L_0x0027;	 Catch:{ Exception -> 0x004a }
    L_0x0016:
        r1 = r5.toLowerCase();	 Catch:{ Exception -> 0x004a }
        r2 = "p";	 Catch:{ Exception -> 0x004a }
        r3 = "";	 Catch:{ Exception -> 0x004a }
        r1 = r1.replace(r2, r3);	 Catch:{ Exception -> 0x004a }
        r1 = r1.trim();	 Catch:{ Exception -> 0x004a }
        r5 = r1;	 Catch:{ Exception -> 0x004a }
    L_0x0027:
        r1 = r5.toLowerCase();	 Catch:{ Exception -> 0x004a }
        r1 = r1.trim();	 Catch:{ Exception -> 0x004a }
        r1 = java.lang.Integer.parseInt(r1);	 Catch:{ Exception -> 0x004a }
        r2 = 1080; // 0x438 float:1.513E-42 double:5.336E-321;	 Catch:{ Exception -> 0x004a }
        if (r1 < r2) goto L_0x003b;	 Catch:{ Exception -> 0x004a }
    L_0x0037:
        r1 = "1080p";	 Catch:{ Exception -> 0x004a }
    L_0x0039:
        r0 = r1;	 Catch:{ Exception -> 0x004a }
        goto L_0x004a;	 Catch:{ Exception -> 0x004a }
    L_0x003b:
        r2 = 480; // 0x1e0 float:6.73E-43 double:2.37E-321;	 Catch:{ Exception -> 0x004a }
        if (r1 < r2) goto L_0x0042;	 Catch:{ Exception -> 0x004a }
    L_0x003f:
        r1 = "HD";	 Catch:{ Exception -> 0x004a }
        goto L_0x0039;	 Catch:{ Exception -> 0x004a }
    L_0x0042:
        if (r1 <= 0) goto L_0x0047;	 Catch:{ Exception -> 0x004a }
    L_0x0044:
        r1 = "SD";	 Catch:{ Exception -> 0x004a }
        goto L_0x0039;	 Catch:{ Exception -> 0x004a }
    L_0x0047:
        r1 = "UNKNOWN";	 Catch:{ Exception -> 0x004a }
        goto L_0x0039;
    L_0x004a:
        r1 = "UNKNOWN";
        r1 = r0.equals(r1);
        if (r1 == 0) goto L_0x006e;
    L_0x0052:
        if (r5 == 0) goto L_0x006e;
    L_0x0054:
        r1 = r5.isEmpty();
        if (r1 != 0) goto L_0x006e;
    L_0x005a:
        r1 = "HD";
        r1 = r5.equals(r1);
        if (r1 == 0) goto L_0x0064;
    L_0x0062:
        r0 = "HD";
    L_0x0064:
        r1 = "SD";
        r5 = r5.equals(r1);
        if (r5 == 0) goto L_0x006e;
    L_0x006c:
        r0 = "SD";
    L_0x006e:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.github.morpheustv.scrapers.model.BaseProvider.getLabelQuality(java.lang.String):java.lang.String");
    }

    public String googletag(String str) {
        String str2 = str;
        Pattern compile = Pattern.compile("itag=(\\d*)");
        Pattern compile2 = Pattern.compile("=m(\\d*)");
        Object obj = UNKNOWN_QUALITY;
        try {
            Matcher matcher = compile.matcher(str2);
            if (matcher.find() && matcher.groupCount() > 0) {
                obj = matcher.group(1);
            }
            if (obj.equals(UNKNOWN_QUALITY)) {
                Matcher matcher2 = compile2.matcher(str2);
                if (matcher2.find() && matcher2.groupCount() > 0) {
                    obj = matcher2.group(1);
                }
            }
            if (Arrays.asList(new String[]{"37", "137", "299", "96", "248", "303", "46"}).contains(obj)) {
                return FULLHD;
            }
            if (Arrays.asList(new String[]{"22", "84", "136", "298", "120", "95", "247", "302", "45", "102"}).contains(obj)) {
                return HD;
            }
            return Arrays.asList(new String[]{"35", "44", "135", "244", "94", "18", "34", "43", "82", "100", "101", "134", "243", "93", "5", "6", "36", "83", "133", "242", "92", "132", "59"}).contains(obj) ? SD : UNKNOWN_QUALITY;
        } catch (Exception e) {
            e.printStackTrace();
            return UNKNOWN_QUALITY;
        }
    }

    public void verifySource(Source source, Executor executor, boolean z, OnStreamInfoResolved onStreamInfoResolved) {
        final String url = source.getUrl();
        final String referer = source.getReferer() != null ? source.getReferer() : source.getUrl();
        if ((getConfig().shouldVerifySources() || z) && !source.isTorrent()) {
            final Source source2 = source;
            final OnStreamInfoResolved onStreamInfoResolved2 = onStreamInfoResolved;
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        BaseProvider.runningVerifiers++;
                        long currentTimeMillis = System.currentTimeMillis();
                        String str = "";
                        if (!(source2.getCookieString() == null || source2.getCookieString().isEmpty())) {
                            str = source2.getCookieString();
                        }
                        if (source2.getCookies() != null && source2.getCookies().size() > 0) {
                            for (Entry entry : source2.getCookies().entrySet()) {
                                StringBuilder stringBuilder;
                                if (!str.isEmpty()) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(str);
                                    stringBuilder.append("; ");
                                    str = stringBuilder.toString();
                                }
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(str);
                                stringBuilder.append((String) entry.getKey());
                                stringBuilder.append("=");
                                stringBuilder.append((String) entry.getValue());
                                str = stringBuilder.toString();
                            }
                        }
                        Response execute;
                        StreamInfo streamInfo;
                        if (url.contains(".m3u8")) {
                            execute = Jsoup.connect(url).ignoreContentType(true).validateTLSCertificates(false).followRedirects(true).userAgent(BaseProvider.UserAgent).timeout(BaseProvider.this.getConfig().getVerifySourcesTimeout() / 2).header(HttpHeaders.ACCEPT_ENCODING, "identity;q=1, *;q=0").header(HttpHeaders.COOKIE, str).referrer(referer).method(Method.GET).execute();
                            String body = execute.body();
                            if (execute.hasHeader("content-type") && body.contains("#EXTM3U")) {
                                streamInfo = new StreamInfo();
                                streamInfo.content_type = execute.header("content-type");
                                streamInfo.resolved_url = execute.url().toString();
                                streamInfo.cookie = execute.cookies();
                                streamInfo.content_length = 0;
                                streamInfo.resolve_time = System.currentTimeMillis() - currentTimeMillis;
                                onStreamInfoResolved2.OnResolvedInfo(streamInfo);
                            } else {
                                onStreamInfoResolved2.OnResolvedInfo(null);
                            }
                        } else {
                            execute = Jsoup.connect(url).ignoreContentType(true).validateTLSCertificates(false).followRedirects(true).userAgent(BaseProvider.UserAgent).timeout(BaseProvider.this.getConfig().getVerifySourcesTimeout() / 2).header(HttpHeaders.ACCEPT_ENCODING, "identity;q=1, *;q=0").header(HttpHeaders.RANGE, "bytes=0-").header(HttpHeaders.COOKIE, str).referrer(referer).method(Method.GET).execute();
                            if (execute.hasHeader("content-type") && (execute.hasHeader("content-length") || execute.hasHeader("content-range"))) {
                                streamInfo = new StreamInfo();
                                streamInfo.content_type = execute.header("content-type");
                                if (execute.hasHeader("content-length")) {
                                    streamInfo.content_length = Long.parseLong(execute.header("content-length"));
                                } else {
                                    String header = execute.header("content-range");
                                    streamInfo.content_length = Long.parseLong(header.substring(header.indexOf("/") + 1));
                                }
                                streamInfo.resolved_url = execute.url().toString();
                                streamInfo.cookie = execute.cookies();
                                streamInfo.resolve_time = System.currentTimeMillis() - currentTimeMillis;
                                if (streamInfo.content_length > 0) {
                                    onStreamInfoResolved2.OnResolvedInfo(streamInfo);
                                } else {
                                    onStreamInfoResolved2.OnResolvedInfo(null);
                                }
                            } else {
                                onStreamInfoResolved2.OnResolvedInfo(null);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onStreamInfoResolved2.OnResolvedInfo(null);
                    }
                    BaseProvider.runningVerifiers--;
                }
            });
            return;
        }
        executor = new StreamInfo();
        executor.content_type = MimeTypes.VIDEO_MP4;
        executor.content_length = source.getSize();
        executor.resolved_url = url;
        executor.resolve_time = 0;
        executor.cookie = new HashMap();
        executor.error = null;
        onStreamInfoResolved.OnResolvedInfo(executor);
    }

    public String redirectOnce(String str, String str2) {
        try {
            Connection method = Jsoup.connect(str).ignoreHttpErrors(true).validateTLSCertificates(false).ignoreContentType(true).followRedirects(false).userAgent(UserAgent).timeout(10000).header(HttpHeaders.ACCEPT_ENCODING, "identity;q=1, *;q=0").header(HttpHeaders.RANGE, "bytes=0-").method(Method.GET);
            if (!(str2 == null || str2.isEmpty())) {
                method.referrer(str2);
            }
            method.execute();
            if (method.response().statusCode() == 429) {
                log("###### 429 - Too many requests, sleeping...");
                Thread.sleep(1000);
                method.get();
                if (method.response().statusCode() != 429) {
                    log("###### 429 - Recovered after sleep...");
                }
            }
            if ((method.response().statusCode() == 302 || method.response().statusCode() == 301 || method.response().statusCode() == 303 || method.response().statusCode() == 307 || method.response().statusCode() == 308) && method.response().header(HttpHeaders.LOCATION) != null && method.response().header(HttpHeaders.LOCATION).equals("/") == null) {
                return method.response().header(HttpHeaders.LOCATION);
            }
        } catch (String str22) {
            str22.printStackTrace();
        }
        return str;
    }

    public boolean isAntiBotPage(String str) {
        boolean contains = str.toLowerCase().contains("DDoS protection by Cloudflare".toLowerCase());
        if (!contains) {
            contains = str.toLowerCase().contains("sucuri_cloudproxy_js".toLowerCase());
        }
        if (!contains) {
            contains = str.toLowerCase().contains("blazingfast".toLowerCase());
        }
        return !contains ? str.toLowerCase().contains("checking your browser".toLowerCase()) : contains;
    }

    public boolean isPageNotFound(String str) {
        return str.toLowerCase().contains("We can't find the file you are looking for.".toLowerCase());
    }

    public String getCDNSource(String str) {
        String str2 = "CDN";
        try {
            InternetDomainName from = InternetDomainName.from(new URL(str).getHost());
            String str3 = "CDN";
            if (from.parts().size() > 1) {
                str3 = (String) from.parts().get(from.parts().size() - 2);
            }
            str2 = str3.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (str.contains(".m3u8") == null) {
            return str2;
        }
        str = new StringBuilder();
        str.append(str2);
        str.append(" HLS");
        return str.toString();
    }

    public void addSource(final CopyOnWriteArrayList<Source> copyOnWriteArrayList, final Source source, boolean z, final boolean z2) {
        try {
            if (this.scraper.isBusy()) {
                Executor executor = TaskManager.RESOLVER_EXECUTOR;
                String source2 = source.getSource();
                Object obj = -1;
                switch (source2.hashCode()) {
                    case -1763338715:
                        if (source2.equals("VIDOZA")) {
                            obj = 3;
                            break;
                        }
                        break;
                    case -709280427:
                        if (source2.equals("STREAMANGO")) {
                            obj = 1;
                            break;
                        }
                        break;
                    case 81665612:
                        if (source2.equals("VIDUP")) {
                            obj = 4;
                            break;
                        }
                        break;
                    case 278701616:
                        if (source2.equals("OPENLOAD")) {
                            obj = null;
                            break;
                        }
                        break;
                    case 1900012202:
                        if (source2.equals("THEVIDEO")) {
                            obj = 2;
                            break;
                        }
                        break;
                    default:
                        break;
                }
                switch (obj) {
                    case null:
                        executor = TaskManager.OPENLOAD_EXECUTOR;
                        break;
                    case 1:
                        executor = TaskManager.STREAMANGO_EXECUTOR;
                        break;
                    case 2:
                        executor = TaskManager.THEVIDEO_EXECUTOR;
                        break;
                    case 3:
                        executor = TaskManager.THEVIDEO_EXECUTOR;
                        break;
                    case 4:
                        executor = TaskManager.THEVIDEO_EXECUTOR;
                        break;
                    default:
                        break;
                }
                verifySource(source, executor, z, new OnStreamInfoResolved() {

                    /* renamed from: io.github.morpheustv.scrapers.model.BaseProvider$2$1 */
                    class C13911 implements Runnable {
                        C13911() {
                        }

                        public void run() {
                            BaseProvider.this.getScraper().getScraperListener().onSourceFound(source);
                        }
                    }

                    public void OnResolvedInfo(StreamInfo streamInfo) {
                        if (streamInfo != null && BaseProvider.this.scraper.isBusy()) {
                            if (streamInfo.resolve_time > 0) {
                                long j = streamInfo.resolve_time;
                                String formatSize = Utils.formatSize(BaseProvider.this.mContext, streamInfo.content_length);
                                if (streamInfo.resolved_url.contains(".m3u8")) {
                                    formatSize = "HLS";
                                }
                                source.setExtra_info(String.format("(%s / ~%dms)", new Object[]{formatSize, Long.valueOf(j)}));
                            }
                            source.setSize(streamInfo.content_length);
                            source.setResolve_time(streamInfo.resolve_time);
                            source.setCastable(z2);
                            source.setResolved_url(streamInfo.resolved_url);
                            source.setUrl(streamInfo.resolved_url);
                            streamInfo = BaseProvider.this;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("######## ADDING SOURCE ######## > ");
                            stringBuilder.append(source.getSource());
                            stringBuilder.append(" : ");
                            stringBuilder.append(source.getQuality());
                            stringBuilder.append(" : ");
                            stringBuilder.append(source.getUrl());
                            streamInfo.log(stringBuilder.toString());
                            copyOnWriteArrayList.add(source);
                            if (BaseProvider.this.getScraper().getScraperListener() != null) {
                                BaseProvider.this.getConfig().getContext().runOnUiThread(new C13911());
                            }
                        }
                    }
                });
            }
        } catch (CopyOnWriteArrayList<Source> copyOnWriteArrayList2) {
            copyOnWriteArrayList2.printStackTrace();
        }
    }

    public HashSet<String> extractLinks(String str) {
        HashSet<String> hashSet = new HashSet();
        try {
            str = StringEscapeUtils.unescapeEcmaScript(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            str = StringEscapeUtils.unescapeHtml4(str);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        str = Pattern.compile("(http|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?").matcher(str);
        while (str.find()) {
            hashSet.add(str.group());
        }
        return hashSet;
    }

    public HashSet<String> extractMagnets(String str) {
        HashSet<String> hashSet = new HashSet();
        try {
            str = StringEscapeUtils.unescapeEcmaScript(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            str = StringEscapeUtils.unescapeHtml4(str);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        str = Pattern.compile("magnet:([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?").matcher(str);
        while (str.find()) {
            hashSet.add(str.group());
        }
        return hashSet;
    }

    public static String getQualityFromUrl(String str) {
        String str2 = UNKNOWN_QUALITY;
        if (str.toLowerCase().contains("2160p".toLowerCase())) {
            return FULLHD;
        }
        if (str.toLowerCase().contains(FULLHD.toLowerCase())) {
            return FULLHD;
        }
        if (str.toLowerCase().contains("720p".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("480p".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("BRRip".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("BR-Rip".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("BR.Rip".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("BDRip".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("BD-Rip".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("BD.Rip".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("HDRip".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("HDTV".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("Webdl".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("Web-dl".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("Web.dl".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("Webrip".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("Web-rip".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("Web.rip".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("BluRay".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("1080.mp4".toLowerCase())) {
            return FULLHD;
        }
        if (str.toLowerCase().contains("720.mp4".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("480.mp4".toLowerCase())) {
            return HD;
        }
        if (str.toLowerCase().contains("360.mp4".toLowerCase())) {
            return SD;
        }
        return str.toLowerCase().contains("dvdrip".toLowerCase()) != null ? SD : str2;
    }

    public boolean matchesEpisode(java.lang.String r6, int r7) {
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
        r0 = 0;
        r6 = r6.toLowerCase();	 Catch:{ Exception -> 0x0033 }
        r1 = "pisode\\s*(\\d+)";	 Catch:{ Exception -> 0x0033 }
        r1 = java.util.regex.Pattern.compile(r1);	 Catch:{ Exception -> 0x0033 }
        r1 = r1.matcher(r6);	 Catch:{ Exception -> 0x0033 }
        r2 = 0;
    L_0x0010:
        r3 = r1.find();	 Catch:{ Exception -> 0x0034 }
        r4 = 1;	 Catch:{ Exception -> 0x0034 }
        if (r3 == 0) goto L_0x0029;	 Catch:{ Exception -> 0x0034 }
    L_0x0017:
        r3 = r1.groupCount();	 Catch:{ Exception -> 0x0034 }
        if (r3 <= 0) goto L_0x0029;	 Catch:{ Exception -> 0x0034 }
    L_0x001d:
        r3 = r1.group(r4);	 Catch:{ Exception -> 0x0034 }
        r3 = java.lang.Integer.parseInt(r3);	 Catch:{ Exception -> 0x0034 }
        if (r3 != r7) goto L_0x0010;	 Catch:{ Exception -> 0x0034 }
    L_0x0027:
        r2 = 1;	 Catch:{ Exception -> 0x0034 }
        goto L_0x0010;	 Catch:{ Exception -> 0x0034 }
    L_0x0029:
        if (r2 != 0) goto L_0x0034;	 Catch:{ Exception -> 0x0034 }
    L_0x002b:
        r6 = java.lang.Integer.parseInt(r6);	 Catch:{ Exception -> 0x0034 }
        if (r6 != r7) goto L_0x0033;
    L_0x0031:
        r2 = 1;
        goto L_0x0034;
    L_0x0033:
        r2 = 0;
    L_0x0034:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.github.morpheustv.scrapers.model.BaseProvider.matchesEpisode(java.lang.String, int):boolean");
    }

    public boolean isAlive() {
        return !this.finished && this.scraper.isBusy();
    }

    public void finish() {
        this.finished = true;
    }

    public boolean allowResource(String str) {
        Matcher matcher;
        if (this.resourceWhitelist != null) {
            for (String compile : this.resourceWhitelist) {
                matcher = Pattern.compile(compile).matcher(str);
                if (matcher.find() && matcher.groupCount() > 0) {
                    return true;
                }
            }
        }
        if (this.staticResourceWhitelist != null) {
            for (String compile2 : this.staticResourceWhitelist) {
                matcher = Pattern.compile(compile2).matcher(str);
                if (matcher.find() && matcher.groupCount() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public String findLoadedResource(String str) {
        if (this.loadedResources != null) {
            Iterator it = this.loadedResources.iterator();
            while (it.hasNext()) {
                Matcher matcher = Pattern.compile(str).matcher((String) it.next());
                if (matcher.find() && matcher.groupCount() > 0) {
                    return matcher.group(1);
                }
            }
        }
        return "";
    }

    public String wvgethtml(String str, int i) {
        if (!isAlive()) {
            return "";
        }
        String wveval;
        String str2 = "";
        try {
            wveval = wveval("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();", str, i);
            try {
                if (isAntiBotPage(wveval)) {
                    Thread.sleep(6000);
                    str = wveval("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();", str, i);
                    return str;
                }
            } catch (Exception e) {
                str = e;
                str.printStackTrace();
                str = wveval;
                return str;
            }
        } catch (Exception e2) {
            str = e2;
            wveval = str2;
            str.printStackTrace();
            str = wveval;
            return str;
        }
        str = wveval;
        return str;
    }

    public String wvgethtmlbeforejs(String str, int i) {
        if (isAlive() == 0) {
            return "";
        }
        String wvevalafterload;
        i = "";
        try {
            wvevalafterload = wvevalafterload("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();", str);
            try {
                if (isAntiBotPage(wvevalafterload) != 0) {
                    Thread.sleep(6000);
                    str = wvevalafterload("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();", str);
                    return str;
                }
            } catch (Exception e) {
                str = e;
                str.printStackTrace();
                str = wvevalafterload;
                return str;
            }
        } catch (Exception e2) {
            str = e2;
            wvevalafterload = i;
            str.printStackTrace();
            str = wvevalafterload;
            return str;
        }
        str = wvevalafterload;
        return str;
    }

    public String wvgethtml(String str) {
        return wvgethtml(str, 0);
    }

    public String wvgethtmlbeforejs(String str) {
        return wvgethtmlbeforejs(str, 0);
    }

    public String wvgethtml(String str, String str2, boolean z) {
        if (!isAlive()) {
            return "";
        }
        this.scraper.getController().clearHeaders(this);
        if (!(str2 == null || str2.isEmpty())) {
            this.scraper.getController().setHeader(this, HttpHeaders.REFERER, str2);
        }
        if (z) {
            this.scraper.getController().setHeader(this, HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest");
        }
        return wvgethtml(str);
    }

    public String wvgethtml(String str, Map<String, String> map) {
        if (!isAlive()) {
            return "";
        }
        this.scraper.getController().clearHeaders(this);
        if (!(map == null || map.isEmpty())) {
            map = map.entrySet().iterator();
            while (map.hasNext()) {
                Entry entry = (Entry) map.next();
                this.scraper.getController().setHeader(this, (String) entry.getKey(), (String) entry.getValue());
            }
        }
        return wvgethtml(str);
    }

    public String wvsethtml(String str, String str2) {
        if (!isAlive()) {
            return "";
        }
        String str3 = "";
        try {
            this.scraper.getController().loadHtml(this, str, str2);
            str = wvgethtml();
        } catch (String str4) {
            str4.printStackTrace();
            str4 = str3;
        }
        return str4;
    }

    public String wvgethtml() {
        return isAlive() ? wveval("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();") : "";
    }

    public String wveval(String str, String str2, int i) {
        if (isAlive() == 0) {
            return "";
        }
        i = "";
        try {
            this.scraper.getController().loadUrl(this, str2);
            str = wveval(str);
        } catch (String str3) {
            str3.printStackTrace();
            str3 = i;
        }
        return str3;
    }

    public String wvevalafterload(String str, String str2) {
        if (!isAlive()) {
            return "";
        }
        String str3 = "";
        try {
            this.scraper.getController().setJavascriptEnabled(this, false);
            this.scraper.getController().loadUrl(this, str2);
            this.scraper.getController().setJavascriptEnabled(this, true);
            str = wveval(str);
        } catch (String str4) {
            str4.printStackTrace();
            str4 = str3;
        }
        return str4;
    }

    public String wveval(String str) {
        if (!isAlive()) {
            return "";
        }
        String str2 = "";
        try {
            str = this.scraper.getController().eval(this, str);
        } catch (String str3) {
            str3.printStackTrace();
            str3 = str2;
        }
        return str3;
    }

    public String wvurl() {
        return isAlive() ? this.scraper.getController().getUrl(this) : "";
    }

    public String wvcookie(String str) {
        if (!isAlive()) {
            return "";
        }
        wvgethtml(str);
        return CookieManager.getInstance().getCookie(str);
    }

    public String wvcookie() {
        if (!isAlive()) {
            return "";
        }
        return CookieManager.getInstance().getCookie(wvurl());
    }

    public String wvmatchresource(String str, String str2, int i) {
        try {
            this.isBusy = false;
            this.isWaitingFinish = false;
            Thread.sleep(250);
            this.loadedResources.clear();
            wvgethtml(str, i);
            long currentTimeMillis = System.currentTimeMillis() + ((long) i);
            while (isAlive() != null && System.currentTimeMillis() < currentTimeMillis) {
                str = findLoadedResource(str2);
                if (str == null || str.isEmpty() != 0) {
                    Thread.sleep(10);
                } else {
                    str2 = new StringBuilder();
                    str2.append("Found Matching Resource : ");
                    str2.append(str);
                    log(str2.toString());
                    return str;
                }
            }
        } catch (String str3) {
            str3.printStackTrace();
        }
        return "";
    }

    public String wvmatchresourceafterjs(String str, String str2, int i, String str3, String str4) {
        try {
            this.isBusy = false;
            this.isWaitingFinish = false;
            Thread.sleep(250);
            if (isPageNotFound(wvgethtml(str, str3, false)) == null) {
                long currentTimeMillis = System.currentTimeMillis() + ((long) i);
                this.loadedResources.clear();
                wveval(str4);
                while (isAlive() != null && System.currentTimeMillis() < currentTimeMillis) {
                    str = findLoadedResource(str2);
                    if (str == null || str.isEmpty() != 0) {
                        Thread.sleep(10);
                    } else {
                        str2 = new StringBuilder();
                        str2.append("Found Matching Resource : ");
                        str2.append(str);
                        log(str2.toString());
                        return str;
                    }
                }
            }
        } catch (String str5) {
            str5.printStackTrace();
        }
        return "";
    }

    public String wvmatchresourceafterjs(String str, int i, String str2) {
        try {
            this.isBusy = false;
            this.isWaitingFinish = false;
            Thread.sleep(250);
            long currentTimeMillis = System.currentTimeMillis() + ((long) i);
            this.loadedResources.clear();
            wveval(str2);
            while (isAlive() != 0 && System.currentTimeMillis() < currentTimeMillis) {
                i = findLoadedResource(str);
                if (i == 0 || i.isEmpty() != null) {
                    Thread.sleep(10);
                } else {
                    str = new StringBuilder();
                    str.append("Found Matching Resource : ");
                    str.append(i);
                    log(str.toString());
                    return i;
                }
            }
        } catch (String str3) {
            str3.printStackTrace();
        }
        return "";
    }

    public String jsunpack(String str) {
        Duktape create = Duktape.create();
        create.evaluate(Utils.LoadAsset(getConfig().getContext(), "jsunpack.js"));
        return ((JSUnpack) create.get("JSUnpack", JSUnpack.class)).decode(str);
    }

    public boolean addAppspotSource(String str, String str2, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str3) {
        if (getConfig().getResolvers().isGvideoEnabled()) {
            if (isAlive()) {
                if (str == null || str.isEmpty()) {
                    return false;
                }
                Source source;
                if (str2 == null || str2.isEmpty()) {
                    Source source2 = new Source(str3, "APPSPOT", getQualityFromUrl(str), this.PROVIDER_NAME, str);
                } else {
                    source = new Source(str3, "APPSPOT", getLabelQuality(str2), this.PROVIDER_NAME, str);
                }
                addSource(copyOnWriteArrayList, source, true, true);
                return true;
            }
        }
        return false;
    }

    public boolean addCDNSource(String str, String str2, String str3, String str4, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str5) {
        String str6 = str2;
        if (getConfig().getResolvers().isOthersEnabled()) {
            if (isAlive()) {
                if (str == null || str.isEmpty()) {
                    return false;
                }
                Source source;
                if (str6 == null || str6.isEmpty()) {
                    source = new Source(str5, getCDNSource(str), getQualityFromUrl(str), r0.PROVIDER_NAME, str, str4);
                } else {
                    Source source2 = new Source(str5, getCDNSource(str), getLabelQuality(str6), r0.PROVIDER_NAME, str, str4);
                }
                source.setReferer(str3);
                addSource(copyOnWriteArrayList, source, false, true);
                return true;
            }
        }
        return false;
    }

    public boolean addTorrentSource(String str, String str2, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str3, String str4, String str5, long j, long j2, long j3) {
        long j4 = j;
        if (getConfig().isTorrentsEnabled()) {
            if (isAlive()) {
                if (str == null || str.isEmpty()) {
                    return false;
                }
                if (r0.isTvShow && getConfig().getTorrentMaxSizeShows() > 0 && j4 > getConfig().getTorrentMaxSizeShows()) {
                    return false;
                }
                if (!r0.isTvShow && getConfig().getTorrentMaxSizeMovies() > 0 && j4 > getConfig().getTorrentMaxSizeMovies()) {
                    return false;
                }
                Source source;
                if (str2 == null || str2.isEmpty()) {
                    source = new Source(str3, "TORRENT", getQualityFromUrl(str), r0.PROVIDER_NAME, str, str4, str5);
                } else {
                    source = new Source(str3, "TORRENT", str2, r0.PROVIDER_NAME, str, str4, str5);
                }
                r13.setSize(j4);
                r13.setTorrent_peers(j3);
                r13.setTorrent_seeds(j2);
                r13.setTorrent(true);
                addSource(copyOnWriteArrayList, r13, false, true);
                return true;
            }
        }
        return false;
    }

    public void addVidnodeSource(String str, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str2) {
        new VidnodeResolver(this.scraper, this).execute(str, str, str2, copyOnWriteArrayList);
    }

    public void addStreamangoSources(java.lang.String r3, java.util.concurrent.CopyOnWriteArrayList<io.github.morpheustv.scrapers.model.Source> r4, java.lang.String r5) {
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
        r2 = this;
        r0 = r2.getConfig();
        r0 = r0.getResolvers();
        r0 = r0.isStreamangoEnabled();
        if (r0 == 0) goto L_0x0063;
    L_0x000e:
        r0 = r2.isAlive();
        if (r0 != 0) goto L_0x0015;
    L_0x0014:
        goto L_0x0063;
    L_0x0015:
        r0 = "/f/";
        r1 = "/embed/";
        r3 = r3.replace(r0, r1);
        r0 = "streamcherry.com";
        r1 = "streamango.com";
        r3 = r3.replace(r0, r1);
        r0 = org.jsoup.Jsoup.connect(r3);	 Catch:{ Exception -> 0x0058 }
        r1 = 0;	 Catch:{ Exception -> 0x0058 }
        r0 = r0.validateTLSCertificates(r1);	 Catch:{ Exception -> 0x0058 }
        r1 = 1;	 Catch:{ Exception -> 0x0058 }
        r0 = r0.ignoreHttpErrors(r1);	 Catch:{ Exception -> 0x0058 }
        r0 = r0.ignoreContentType(r1);	 Catch:{ Exception -> 0x0058 }
        r1 = UserAgent;	 Catch:{ Exception -> 0x0058 }
        r0 = r0.userAgent(r1);	 Catch:{ Exception -> 0x0058 }
        r1 = 3000; // 0xbb8 float:4.204E-42 double:1.482E-320;	 Catch:{ Exception -> 0x0058 }
        r0 = r0.timeout(r1);	 Catch:{ Exception -> 0x0058 }
        r0 = r0.get();	 Catch:{ Exception -> 0x0058 }
        r0 = r0.html();	 Catch:{ Exception -> 0x0058 }
        r0 = r0.toLowerCase();	 Catch:{ Exception -> 0x0058 }
        r1 = "srces.push";	 Catch:{ Exception -> 0x0058 }
        r0 = r0.contains(r1);	 Catch:{ Exception -> 0x0058 }
        if (r0 != 0) goto L_0x0058;
    L_0x0057:
        return;
    L_0x0058:
        r0 = new io.github.morpheustv.scrapers.resolvers.StreamangoResolver;
        r1 = r2.scraper;
        r0.<init>(r1, r2);
        r0.execute(r3, r3, r5, r4);
        return;
    L_0x0063:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.github.morpheustv.scrapers.model.BaseProvider.addStreamangoSources(java.lang.String, java.util.concurrent.CopyOnWriteArrayList, java.lang.String):void");
    }

    public void addStreamGoSources(java.lang.String r17, java.lang.String r18, java.util.concurrent.CopyOnWriteArrayList<io.github.morpheustv.scrapers.model.Source> r19, java.lang.String r20) {
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
        r16 = this;
        r0 = r16;
        r1 = r16.getConfig();
        r1 = r1.getResolvers();
        r1 = r1.isStrmgoHLSEnabled();
        if (r1 == 0) goto L_0x0110;
    L_0x0010:
        r1 = r16.isAlive();
        if (r1 != 0) goto L_0x0018;
    L_0x0016:
        goto L_0x0110;
    L_0x0018:
        r1 = "/f/";	 Catch:{ Exception -> 0x010f }
        r2 = "/embed/";	 Catch:{ Exception -> 0x010f }
        r3 = r17;	 Catch:{ Exception -> 0x010f }
        r1 = r3.replace(r1, r2);	 Catch:{ Exception -> 0x010f }
        r2 = org.jsoup.Jsoup.connect(r1);	 Catch:{ Exception -> 0x010f }
        r3 = 0;	 Catch:{ Exception -> 0x010f }
        r2 = r2.validateTLSCertificates(r3);	 Catch:{ Exception -> 0x010f }
        r4 = 1;	 Catch:{ Exception -> 0x010f }
        r2 = r2.ignoreHttpErrors(r4);	 Catch:{ Exception -> 0x010f }
        r2 = r2.ignoreContentType(r4);	 Catch:{ Exception -> 0x010f }
        r5 = UserAgent;	 Catch:{ Exception -> 0x010f }
        r2 = r2.userAgent(r5);	 Catch:{ Exception -> 0x010f }
        r5 = r18;	 Catch:{ Exception -> 0x010f }
        r2 = r2.referrer(r5);	 Catch:{ Exception -> 0x010f }
        r5 = 3000; // 0xbb8 float:4.204E-42 double:1.482E-320;	 Catch:{ Exception -> 0x010f }
        r2 = r2.timeout(r5);	 Catch:{ Exception -> 0x010f }
        r2 = r2.get();	 Catch:{ Exception -> 0x010f }
        r2 = r2.html();	 Catch:{ Exception -> 0x010f }
        r5 = "sources\\s*:\\s*(\\[.*?\\])";	 Catch:{ Exception -> 0x010f }
        r5 = java.util.regex.Pattern.compile(r5);	 Catch:{ Exception -> 0x010f }
        r5 = r5.matcher(r2);	 Catch:{ Exception -> 0x010f }
        r6 = r5.find();	 Catch:{ Exception -> 0x010f }
        if (r6 == 0) goto L_0x010f;	 Catch:{ Exception -> 0x010f }
    L_0x005e:
        r6 = r5.groupCount();	 Catch:{ Exception -> 0x010f }
        if (r6 <= 0) goto L_0x010f;	 Catch:{ Exception -> 0x010f }
    L_0x0064:
        r5 = r5.group(r4);	 Catch:{ Exception -> 0x010f }
        r2 = org.jsoup.Jsoup.parse(r2);	 Catch:{ Exception -> 0x010f }
        r6 = "title";	 Catch:{ Exception -> 0x010f }
        r2 = r2.select(r6);	 Catch:{ Exception -> 0x010f }
        r2 = r2.first();	 Catch:{ Exception -> 0x010f }
        r2 = r2.text();	 Catch:{ Exception -> 0x010f }
        r5 = org.apache.commons.lang3.StringEscapeUtils.unescapeEcmaScript(r5);	 Catch:{ Exception -> 0x010f }
        r5 = org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4(r5);	 Catch:{ Exception -> 0x010f }
        if (r5 == 0) goto L_0x010f;	 Catch:{ Exception -> 0x010f }
    L_0x0084:
        r6 = "\"";	 Catch:{ Exception -> 0x010f }
        r6 = r5.startsWith(r6);	 Catch:{ Exception -> 0x010f }
        if (r6 == 0) goto L_0x0094;	 Catch:{ Exception -> 0x010f }
    L_0x008c:
        r6 = "^\"|\"$";	 Catch:{ Exception -> 0x010f }
        r7 = "";	 Catch:{ Exception -> 0x010f }
        r5 = r5.replaceAll(r6, r7);	 Catch:{ Exception -> 0x010f }
    L_0x0094:
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x010f }
        r6.<init>();	 Catch:{ Exception -> 0x010f }
        r7 = "{\"sources\":";	 Catch:{ Exception -> 0x010f }
        r6.append(r7);	 Catch:{ Exception -> 0x010f }
        r6.append(r5);	 Catch:{ Exception -> 0x010f }
        r5 = "}";	 Catch:{ Exception -> 0x010f }
        r6.append(r5);	 Catch:{ Exception -> 0x010f }
        r5 = r6.toString();	 Catch:{ Exception -> 0x010f }
        r6 = new org.json.JSONObject;	 Catch:{ Exception -> 0x010f }
        r6.<init>(r5);	 Catch:{ Exception -> 0x010f }
        r5 = "sources";	 Catch:{ Exception -> 0x010f }
        r5 = r6.getJSONArray(r5);	 Catch:{ Exception -> 0x010f }
        r14 = 0;	 Catch:{ Exception -> 0x010f }
    L_0x00b6:
        r6 = r5.length();	 Catch:{ Exception -> 0x010f }
        if (r14 >= r6) goto L_0x010f;	 Catch:{ Exception -> 0x010f }
    L_0x00bc:
        r6 = r5.getJSONObject(r14);	 Catch:{ Exception -> 0x010f }
        r7 = "file";	 Catch:{ Exception -> 0x010f }
        r6 = r6.optString(r7);	 Catch:{ Exception -> 0x010f }
        r7 = "\"";	 Catch:{ Exception -> 0x010f }
        r8 = "";	 Catch:{ Exception -> 0x010f }
        r6 = r6.replace(r7, r8);	 Catch:{ Exception -> 0x010f }
        r7 = "http";	 Catch:{ Exception -> 0x010f }
        r7 = r6.startsWith(r7);	 Catch:{ Exception -> 0x010f }
        if (r7 != 0) goto L_0x00e7;	 Catch:{ Exception -> 0x010f }
    L_0x00d6:
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x010f }
        r7.<init>();	 Catch:{ Exception -> 0x010f }
        r8 = "http:";	 Catch:{ Exception -> 0x010f }
        r7.append(r8);	 Catch:{ Exception -> 0x010f }
        r7.append(r6);	 Catch:{ Exception -> 0x010f }
        r6 = r7.toString();	 Catch:{ Exception -> 0x010f }
    L_0x00e7:
        r11 = r6;	 Catch:{ Exception -> 0x010f }
        r6 = r11.isEmpty();	 Catch:{ Exception -> 0x010f }
        if (r6 != 0) goto L_0x010a;	 Catch:{ Exception -> 0x010f }
    L_0x00ee:
        r15 = new io.github.morpheustv.scrapers.model.Source;	 Catch:{ Exception -> 0x010f }
        r8 = "STRMGO HLS";	 Catch:{ Exception -> 0x010f }
        r9 = getQualityFromUrl(r2);	 Catch:{ Exception -> 0x010f }
        r10 = r0.PROVIDER_NAME;	 Catch:{ Exception -> 0x010f }
        r13 = "";	 Catch:{ Exception -> 0x010f }
        r6 = r15;	 Catch:{ Exception -> 0x010f }
        r7 = r20;	 Catch:{ Exception -> 0x010f }
        r12 = r2;	 Catch:{ Exception -> 0x010f }
        r6.<init>(r7, r8, r9, r10, r11, r12, r13);	 Catch:{ Exception -> 0x010f }
        r15.setReferer(r1);	 Catch:{ Exception -> 0x010f }
        r6 = r19;	 Catch:{ Exception -> 0x010f }
        r0.addSource(r6, r15, r3, r4);	 Catch:{ Exception -> 0x010f }
        goto L_0x010c;
    L_0x010a:
        r6 = r19;
    L_0x010c:
        r14 = r14 + 1;
        goto L_0x00b6;
    L_0x010f:
        return;
    L_0x0110:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.github.morpheustv.scrapers.model.BaseProvider.addStreamGoSources(java.lang.String, java.lang.String, java.util.concurrent.CopyOnWriteArrayList, java.lang.String):void");
    }

    public boolean addGvideoSource(String str, String str2, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str3) {
        try {
            if (getConfig().getResolvers().isGvideoEnabled()) {
                if (isAlive()) {
                    if (str.contains("appspot.com")) {
                        return addAppspotSource(str, str2, copyOnWriteArrayList, str3);
                    }
                    new GvideoResolver(this.scraper, this).execute(str, str2, str3, copyOnWriteArrayList);
                    return true;
                }
            }
            return false;
        } catch (String str4) {
            str4.printStackTrace();
            return false;
        }
    }

    public void addOpenloadSource(java.lang.String r3, java.lang.String r4, java.util.concurrent.CopyOnWriteArrayList<io.github.morpheustv.scrapers.model.Source> r5, java.lang.String r6) {
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
        r2 = this;
        r0 = r2.getConfig();
        r0 = r0.getResolvers();
        r0 = r0.isOpenloadEnabled();
        if (r0 == 0) goto L_0x008c;
    L_0x000e:
        r0 = r2.isAlive();
        if (r0 != 0) goto L_0x0015;
    L_0x0014:
        goto L_0x008c;
    L_0x0015:
        r0 = "/f/";
        r1 = "/embed/";
        r3 = r3.replace(r0, r1);
        r0 = "http://";
        r1 = "https://";
        r3 = r3.replace(r0, r1);
        r0 = "oload.stream";
        r1 = "openload.co";
        r3 = r3.replace(r0, r1);
        r0 = "openloed.co";
        r1 = "openload.co";
        r3 = r3.replace(r0, r1);
        r0 = "/";
        r0 = r3.endsWith(r0);
        if (r0 != 0) goto L_0x004e;
    L_0x003d:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r0.append(r3);
        r3 = "/";
        r0.append(r3);
        r3 = r0.toString();
    L_0x004e:
        r0 = org.jsoup.Jsoup.connect(r3);	 Catch:{ Exception -> 0x0081 }
        r1 = 0;	 Catch:{ Exception -> 0x0081 }
        r0 = r0.validateTLSCertificates(r1);	 Catch:{ Exception -> 0x0081 }
        r1 = 1;	 Catch:{ Exception -> 0x0081 }
        r0 = r0.ignoreHttpErrors(r1);	 Catch:{ Exception -> 0x0081 }
        r0 = r0.ignoreContentType(r1);	 Catch:{ Exception -> 0x0081 }
        r1 = UserAgent;	 Catch:{ Exception -> 0x0081 }
        r0 = r0.userAgent(r1);	 Catch:{ Exception -> 0x0081 }
        r1 = 3000; // 0xbb8 float:4.204E-42 double:1.482E-320;	 Catch:{ Exception -> 0x0081 }
        r0 = r0.timeout(r1);	 Catch:{ Exception -> 0x0081 }
        r0 = r0.get();	 Catch:{ Exception -> 0x0081 }
        r0 = r0.html();	 Catch:{ Exception -> 0x0081 }
        r0 = r0.toLowerCase();	 Catch:{ Exception -> 0x0081 }
        r1 = "maybe got deleted";	 Catch:{ Exception -> 0x0081 }
        r0 = r0.contains(r1);	 Catch:{ Exception -> 0x0081 }
        if (r0 == 0) goto L_0x0081;
    L_0x0080:
        return;
    L_0x0081:
        r0 = new io.github.morpheustv.scrapers.resolvers.OpenloadResolver;
        r1 = r2.scraper;
        r0.<init>(r1, r2);
        r0.execute(r3, r4, r6, r5);
        return;
    L_0x008c:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.github.morpheustv.scrapers.model.BaseProvider.addOpenloadSource(java.lang.String, java.lang.String, java.util.concurrent.CopyOnWriteArrayList, java.lang.String):void");
    }

    public void addMCloudSources(String str, String str2, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str3) {
        BaseProvider baseProvider = this;
        String str4 = str;
        if (getConfig().getResolvers().isMcloudHLSEnabled()) {
            if (isAlive()) {
                if (!(str4 == null || str.isEmpty())) {
                    str4 = str4.replace("/f/", "/embed/");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Checking mcloud link : ");
                    stringBuilder.append(str4);
                    log(stringBuilder.toString());
                    try {
                        String html = Jsoup.connect(str4).validateTLSCertificates(false).timeout(4000).followRedirects(true).ignoreContentType(true).ignoreHttpErrors(true).referrer(str2).get().html();
                        if (!html.toLowerCase().contains("something went wrong") && html.contains("sources:")) {
                            String text = Jsoup.parse(html).select("title").first().text();
                            html = html.substring(html.indexOf("sources:") + 8);
                            JSONArray jSONArray = new JSONArray(html.substring(0, html.indexOf("}]") + 2).trim());
                            if (jSONArray.length() > 0) {
                                for (int i = 0; i < jSONArray.length(); i++) {
                                    String optString = jSONArray.getJSONObject(i).optString("file");
                                    String str5 = "MCLOUD";
                                    if (optString.contains(".m3u8")) {
                                        StringBuilder stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append(str5);
                                        stringBuilder2.append(" HLS");
                                        str5 = stringBuilder2.toString();
                                    }
                                    Source source = new Source(str3, str5, getQualityFromUrl(text), baseProvider.PROVIDER_NAME, optString);
                                    source.setReferer(str4);
                                    addSource(copyOnWriteArrayList, source, false, true);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean addLemonStreamSource(String str, String str2, String str3, String str4, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str5) {
        String str6 = str3;
        if (getConfig().getResolvers().isLemonHLSEnabled()) {
            if (isAlive()) {
                if (str == null || str.isEmpty()) {
                    return false;
                }
                Source source;
                if (str6 == null || str6.isEmpty()) {
                    source = new Source(str5, "LEMON HLS", getQualityFromUrl(str), r0.PROVIDER_NAME, str, str4);
                } else {
                    Source source2 = new Source(str5, "LEMON HLS", getLabelQuality(str6), r0.PROVIDER_NAME, str, str4);
                }
                source.setReferer(str2);
                addSource(copyOnWriteArrayList, source, false, true);
                return true;
            }
        }
        return false;
    }

    public void addVidozaSource(String str, String str2, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str3) {
        if (getConfig().getResolvers().isVidozaEnabled()) {
            if (isAlive()) {
                if (!(str == null || str.isEmpty())) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Checking vidoza link : ");
                    stringBuilder.append(str);
                    log(stringBuilder.toString());
                    try {
                        str = Jsoup.connect(str).validateTLSCertificates(false).timeout(4000).followRedirects(true).ignoreContentType(true).ignoreHttpErrors(true).referrer(str2).get().html();
                        if (!str.toLowerCase().contains("file not found")) {
                            if (!str.toLowerCase().contains("file was deleted")) {
                                new VidozaResolver(this.scraper, this).execute(str, str2, str3, copyOnWriteArrayList);
                            }
                        }
                    } catch (String str4) {
                        str4.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean addGdPlayer(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str4) {
        if (getConfig().getResolvers().isOthersEnabled()) {
            if (isAlive()) {
                if (str == null || str.isEmpty()) {
                    return false;
                }
                Source source = new Source(str4, "GDPLAYER", getLabelQuality(str3), this.PROVIDER_NAME, str);
                source.setReferer(str2);
                addSource(copyOnWriteArrayList, source, false, true);
                return true;
            }
        }
        return false;
    }

    public boolean addMehlizCDN(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str4) {
        if (getConfig().getResolvers().isOthersEnabled()) {
            if (isAlive()) {
                if (str == null || str.isEmpty()) {
                    return false;
                }
                Source source = new Source(str4, "MEHLIZ CDN", getLabelQuality(str3), this.PROVIDER_NAME, str);
                source.setReferer(str2);
                addSource(copyOnWriteArrayList, source, false, true);
                return true;
            }
        }
        return false;
    }

    public boolean addHTML5PlayerSource(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str4) {
        String str5 = str3;
        if (getConfig().getResolvers().isOthersEnabled()) {
            if (isAlive()) {
                if (str == null || str.isEmpty()) {
                    return false;
                }
                Source source;
                if (str5 == null || str5.isEmpty()) {
                    source = new Source(str4, getCDNSource(str), getQualityFromUrl(str), r0.PROVIDER_NAME, str);
                } else {
                    Source source2 = new Source(str4, getCDNSource(str), getLabelQuality(str5), r0.PROVIDER_NAME, str);
                }
                addSource(copyOnWriteArrayList, source, false, false);
                return true;
            }
        }
        return false;
    }

    public void addEnterVideoSource(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str4) {
        if (getConfig().getResolvers().isEntervideoEnabled() != null) {
            if (isAlive() != null) {
                new EntervideoResolver(this.scraper, this).execute(str, str2, str4, copyOnWriteArrayList);
            }
        }
    }

    public void addVidupSource(String str, String str2, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str3) {
        if (getConfig().getResolvers().isVidupEnabled()) {
            if (isAlive()) {
                if (str != null) {
                    try {
                        if (!str.isEmpty()) {
                            Matcher matcher = Pattern.compile("(?://|\\.)((?:thevideo|tvad|vidup)\\.(?:me|io|tv))/(?:embed-|download/)?([0-9a-zA-Z]+)").matcher(str);
                            if (matcher.find() && matcher.groupCount() > 1) {
                                String group = matcher.group(2);
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("https://vidup.io/embed-");
                                stringBuilder.append(group);
                                stringBuilder.append("-640x360.html");
                                group = Jsoup.connect(stringBuilder.toString()).validateTLSCertificates(false).timeout(PathInterpolatorCompat.MAX_NUM_POINTS).followRedirects(true).ignoreContentType(true).ignoreHttpErrors(true).referrer(str).get().html();
                                if (!group.toLowerCase().contains("file not found")) {
                                    if (!group.toLowerCase().contains("file was deleted")) {
                                        new VidupResolver(this.scraper, this).execute(str, str2, str3, copyOnWriteArrayList);
                                    }
                                }
                            }
                        }
                    } catch (String str4) {
                        str4.printStackTrace();
                    }
                }
            }
        }
    }

    public void addSpeedvidSource(String str, String str2, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str3) {
        if (getConfig().getResolvers().isOthersEnabled()) {
            if (isAlive()) {
                if (str != null) {
                    try {
                        if (!str.isEmpty()) {
                            new SpeedvidResolver(this.scraper, this).execute(str, str2, str3, copyOnWriteArrayList);
                        }
                    } catch (String str4) {
                        str4.printStackTrace();
                    }
                }
            }
        }
    }

    public void addVidloxSource(String str, String str2, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str3) {
        if (getConfig().getResolvers().isOthersEnabled()) {
            if (isAlive()) {
                if (str != null) {
                    try {
                        if (!str.isEmpty()) {
                            String html = Jsoup.connect(str).validateTLSCertificates(false).timeout(PathInterpolatorCompat.MAX_NUM_POINTS).followRedirects(true).ignoreContentType(true).ignoreHttpErrors(true).referrer(str2).get().html();
                            if (!html.toLowerCase().contains("file not found")) {
                                if (!html.toLowerCase().contains("file was deleted")) {
                                    new VidloxResolver(this.scraper, this).execute(str, str2, str3, copyOnWriteArrayList);
                                }
                            }
                        }
                    } catch (String str4) {
                        str4.printStackTrace();
                    }
                }
            }
        }
    }

    public void addHaxhitsSource(String str, String str2, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str3) {
        if (getConfig().getResolvers().isOthersEnabled()) {
            if (isAlive()) {
                new HaxhitsResolver(this.scraper, this).execute(str, str2, str3, copyOnWriteArrayList);
            }
        }
    }

    public void addIHLSStreamSource(String str, String str2, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str3) {
        if (getConfig().getResolvers().isOthersEnabled()) {
            if (isAlive()) {
                if (str != null) {
                    try {
                        if (!str.isEmpty()) {
                            str2 = Jsoup.connect(str).validateTLSCertificates(false).timeout(PathInterpolatorCompat.MAX_NUM_POINTS).followRedirects(true).ignoreContentType(true).ignoreHttpErrors(true).referrer(str2).get().html();
                            if (!str2.toLowerCase().contains("no records for this id")) {
                                str2 = str2.substring(str2.indexOf("file: \"") + 7);
                                String str4 = str3;
                                String source = new Source(str4, "IHLS", getQualityFromUrl(str), this.PROVIDER_NAME, str2.substring(0, str2.indexOf("\",")), str3, "");
                                source.setReferer(str);
                                addSource(copyOnWriteArrayList, source, false, true);
                            }
                        }
                    } catch (String str5) {
                        str5.printStackTrace();
                    }
                }
            }
        }
    }

    public void addVidCloudSource(String str, String str2, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str3) {
        if (getConfig().getResolvers().isOthersEnabled()) {
            if (isAlive()) {
                new VidcloudResolver(this.scraper, this).execute(str, str2, str3, copyOnWriteArrayList);
            }
        }
    }

    public void addAzmoviesSource(String str, String str2, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str3) {
        BaseProvider baseProvider = this;
        String str4 = str;
        if (getConfig().getResolvers().isOthersEnabled()) {
            if (isAlive()) {
                if (str4 != null) {
                    try {
                        if (!str.isEmpty()) {
                            String html = Jsoup.connect(str).validateTLSCertificates(false).timeout(PathInterpolatorCompat.MAX_NUM_POINTS).followRedirects(true).ignoreContentType(true).ignoreHttpErrors(true).referrer(str2).get().html();
                            if (!html.toLowerCase().contains("not found")) {
                                Iterator it = Jsoup.parse(html).select("source").iterator();
                                while (it.hasNext()) {
                                    Element element = (Element) it.next();
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("https://files.azmovies.xyz/");
                                    stringBuilder.append(element.attr("src"));
                                    String str5 = str3;
                                    Source source = new Source(str5, "AZMOVIES", getLabelQuality(element.attr("data-quality").toUpperCase()), baseProvider.PROVIDER_NAME, stringBuilder.toString(), str3, "");
                                    source.setReferer(str4);
                                    addSource(copyOnWriteArrayList, source, false, true);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void addRapidvideoSource(String str, String str2, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str3) {
        BaseProvider baseProvider = this;
        String str4 = str;
        String str5 = str2;
        CopyOnWriteArrayList<Source> copyOnWriteArrayList2 = copyOnWriteArrayList;
        if (getConfig().getResolvers().isOthersEnabled()) {
            if (isAlive()) {
                if (str4 != null) {
                    try {
                        if (!str.isEmpty()) {
                            Iterator it;
                            Element element;
                            String str6;
                            Source source;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(str4);
                            stringBuilder.append("?q=720p");
                            String html = Jsoup.connect(stringBuilder.toString()).validateTLSCertificates(false).timeout(PathInterpolatorCompat.MAX_NUM_POINTS).followRedirects(true).ignoreContentType(true).ignoreHttpErrors(true).referrer(str5).get().html();
                            if (!html.toLowerCase().contains("object not found")) {
                                it = Jsoup.parse(html).select("source").iterator();
                                while (it.hasNext()) {
                                    element = (Element) it.next();
                                    str6 = str3;
                                    source = new Source(str6, "RAPIDVIDEO", getLabelQuality(element.attr("data-res").toUpperCase()), baseProvider.PROVIDER_NAME, element.attr("src"), str3, "");
                                    source.setReferer(str4);
                                    addSource(copyOnWriteArrayList2, source, false, true);
                                }
                            }
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str4);
                            stringBuilder.append("?q=480p");
                            html = Jsoup.connect(stringBuilder.toString()).validateTLSCertificates(false).timeout(PathInterpolatorCompat.MAX_NUM_POINTS).followRedirects(true).ignoreContentType(true).ignoreHttpErrors(true).referrer(str5).get().html();
                            if (!html.toLowerCase().contains("object not found")) {
                                it = Jsoup.parse(html).select("source").iterator();
                                while (it.hasNext()) {
                                    element = (Element) it.next();
                                    str6 = str3;
                                    source = new Source(str6, "RAPIDVIDEO", getLabelQuality(element.attr("data-res").toUpperCase()), baseProvider.PROVIDER_NAME, element.attr("src"), str3, "");
                                    source.setReferer(str4);
                                    addSource(copyOnWriteArrayList2, source, false, true);
                                }
                            }
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str4);
                            stringBuilder.append("?q=1080p");
                            str5 = Jsoup.connect(stringBuilder.toString()).validateTLSCertificates(false).timeout(PathInterpolatorCompat.MAX_NUM_POINTS).followRedirects(true).ignoreContentType(true).ignoreHttpErrors(true).referrer(str5).get().html();
                            if (!str5.toLowerCase().contains("object not found")) {
                                Iterator it2 = Jsoup.parse(str5).select("source").iterator();
                                while (it2.hasNext()) {
                                    Element element2 = (Element) it2.next();
                                    String str7 = str3;
                                    Source source2 = new Source(str7, "RAPIDVIDEO", getLabelQuality(element2.attr("data-res").toUpperCase()), baseProvider.PROVIDER_NAME, element2.attr("src"), str3, "");
                                    source2.setReferer(str4);
                                    addSource(copyOnWriteArrayList2, source2, false, true);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void addMyStreamSource(String str, String str2, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str3) {
        BaseProvider baseProvider = this;
        String str4 = str;
        if (getConfig().getResolvers().isOthersEnabled()) {
            if (isAlive()) {
                if (str4 != null) {
                    try {
                        if (!str.isEmpty()) {
                            String html = Jsoup.connect(str).validateTLSCertificates(false).timeout(PathInterpolatorCompat.MAX_NUM_POINTS).followRedirects(true).ignoreContentType(true).ignoreHttpErrors(true).referrer(str2).get().html();
                            if (!html.toLowerCase().contains("has been blocked")) {
                                Iterator it = Jsoup.parse(html).select("source").iterator();
                                while (it.hasNext()) {
                                    Element element = (Element) it.next();
                                    String str5 = str3;
                                    Source source = new Source(str5, "MYSTREAM", getLabelQuality(element.attr("label").toUpperCase()), baseProvider.PROVIDER_NAME, element.attr("src"), str3, "");
                                    source.setReferer(str4);
                                    addSource(copyOnWriteArrayList, source, false, true);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void addVideoSpiderSource(String str, String str2, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str3) {
        if (getConfig().getResolvers().isOthersEnabled()) {
            if (isAlive()) {
                if (str != null) {
                    try {
                        if (!str.isEmpty()) {
                            str2 = Jsoup.connect(str).validateTLSCertificates(false).timeout(PathInterpolatorCompat.MAX_NUM_POINTS).followRedirects(true).ignoreContentType(true).ignoreHttpErrors(true).referrer(str2).get().html();
                            if (!str2.toLowerCase().contains("movie not found")) {
                                processLink(Jsoup.parse(str2).select("iframe").first().attr("src"), str, null, copyOnWriteArrayList, str3);
                            }
                        }
                    } catch (String str4) {
                        str4.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean hasMaxSources(CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        copyOnWriteArrayList = copyOnWriteArrayList.iterator();
        int i = 0;
        while (copyOnWriteArrayList.hasNext()) {
            if (((Source) copyOnWriteArrayList.next()).getProvider().equals(this.PROVIDER_NAME)) {
                i++;
            }
        }
        if (i >= getConfig().getMaxSourcesProvider() || runningResolvers >= getConfig().getMaxSourcesProvider()) {
            return true;
        }
        return false;
    }

    public void processLink(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList, String str4) {
        if (str != null) {
            if (!str.isEmpty()) {
                if (!(str.contains("google") || str.contains("blogspot"))) {
                    if (!str.contains("youtube.com")) {
                        if (!(str.contains("openload.co") || str.contains("oload.stream"))) {
                            if (!str.contains("openloed.co")) {
                                if (!str.contains("streamango.com")) {
                                    if (!str.contains("streamcherry.com")) {
                                        if (!str.contains("streamgo.me")) {
                                            if (!str.contains("vidload.co")) {
                                                if (str.contains("mcloud.to")) {
                                                    addMCloudSources(str, str2, copyOnWriteArrayList, str4);
                                                } else {
                                                    if (!str.contains("vidnode.net")) {
                                                        if (!str.contains("vidcloud.icu")) {
                                                            if (str.contains("lemonstream.me")) {
                                                                addLemonStreamSource(str, str2, str3, null, copyOnWriteArrayList, str4);
                                                            } else if (str.contains("vidoza.net")) {
                                                                addVidozaSource(str, str2, copyOnWriteArrayList, str4);
                                                            } else if (str.contains("gdplayer.site")) {
                                                                addGdPlayer(str, str2, str3, copyOnWriteArrayList, str4);
                                                            } else if (str.contains("ihls.stream")) {
                                                                addIHLSStreamSource(str, str2, copyOnWriteArrayList, str4);
                                                            } else if (str.contains("mehliz")) {
                                                                addMehlizCDN(str, str2, str3, copyOnWriteArrayList, str4);
                                                            } else {
                                                                if (!str.contains("afdah.co")) {
                                                                    if (!str.contains("html5player")) {
                                                                        if (str.contains("entervideo.net")) {
                                                                            addEnterVideoSource(str, str2, str3, copyOnWriteArrayList, str4);
                                                                        } else {
                                                                            if (!str.contains("vidup.me")) {
                                                                                if (!str.contains("vidup.tv")) {
                                                                                    if (str.contains("speedvid.net")) {
                                                                                        addSpeedvidSource(str, str2, copyOnWriteArrayList, str4);
                                                                                    } else {
                                                                                        if (!str.contains("vidlox.me")) {
                                                                                            if (!str.contains("vidlox.tv")) {
                                                                                                if (str.contains("haxhits.com")) {
                                                                                                    addHaxhitsSource(str, str2, copyOnWriteArrayList, str4);
                                                                                                } else {
                                                                                                    if (!str.contains("vcstream.to")) {
                                                                                                        if (!str.contains("loadvid.online")) {
                                                                                                            if (str.contains("azmovies.co")) {
                                                                                                                addAzmoviesSource(str, str2, copyOnWriteArrayList, str4);
                                                                                                            } else if (str.contains("rapidvideo.com")) {
                                                                                                                addRapidvideoSource(str, str2, copyOnWriteArrayList, str4);
                                                                                                            } else if (str.contains("embed.mystream.to")) {
                                                                                                                addMyStreamSource(str, str2, copyOnWriteArrayList, str4);
                                                                                                            } else if (str.contains("videospider.in")) {
                                                                                                                addVideoSpiderSource(str, str2, copyOnWriteArrayList, str4);
                                                                                                            } else {
                                                                                                                if (!(str.contains("llnw") || str.contains("vidcdn") || str.contains("megaup.net") || str.contains("m4ukido.com") || str.contains("fbcdn.net") || str.contains("cloudfront.net") || str.contains("dfcdn") || str.contains("ntcdn"))) {
                                                                                                                    if (!str.contains("mload.stream")) {
                                                                                                                        str2 = new StringBuilder();
                                                                                                                        str2.append("######### NO RESOLVER FOR LINK #########: ");
                                                                                                                        str2.append(str);
                                                                                                                        log(str2.toString());
                                                                                                                    }
                                                                                                                }
                                                                                                                addCDNSource(str, str3, str2, null, copyOnWriteArrayList, str4);
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                    addVidCloudSource(str, str2, copyOnWriteArrayList, str4);
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        addVidloxSource(str, str2, copyOnWriteArrayList, str4);
                                                                                    }
                                                                                }
                                                                            }
                                                                            addVidupSource(str, str2, copyOnWriteArrayList, str4);
                                                                        }
                                                                    }
                                                                }
                                                                addHTML5PlayerSource(str, str2, str3, copyOnWriteArrayList, str4);
                                                            }
                                                        }
                                                    }
                                                    addVidnodeSource(str, copyOnWriteArrayList, str4);
                                                }
                                            }
                                        }
                                        addStreamGoSources(str, str2, copyOnWriteArrayList, str4);
                                    }
                                }
                                addStreamangoSources(str, copyOnWriteArrayList, str4);
                            }
                        }
                        addOpenloadSource(str, str2, copyOnWriteArrayList, str4);
                    }
                }
                addGvideoSource(str, str3, copyOnWriteArrayList, str4);
            }
        }
    }
}
