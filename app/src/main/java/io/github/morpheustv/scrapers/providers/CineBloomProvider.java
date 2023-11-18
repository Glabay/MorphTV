package io.github.morpheustv.scrapers.providers;

import android.support.graphics.drawable.PathInterpolatorCompat;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

public class CineBloomProvider extends BaseProvider {
    String base_link = "https://www.cinebloom.com";
    String[] domains = new String[]{"www.cinebloom.com"};

    public CineBloomProvider(Scraper scraper) {
        super(scraper, "CINEBLOOM.COM", false);
    }

    private String request(String str) {
        try {
            return Jsoup.connect(str).validateTLSCertificates(false).ignoreHttpErrors(false).ignoreContentType(true).userAgent(UserAgent).timeout(PathInterpolatorCompat.MAX_NUM_POINTS).get().html();
        } catch (String str2) {
            str2.printStackTrace();
            return "page not found";
        }
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            for (String str3 : list) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.base_link);
                stringBuilder.append("/movies/");
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str3);
                stringBuilder2.append(StringUtils.SPACE);
                stringBuilder2.append(str);
                stringBuilder.append(cleantitleurl(stringBuilder2.toString()));
                String stringBuilder3 = stringBuilder.toString();
                String request = request(stringBuilder3);
                if (!request.toLowerCase().contains("page not found")) {
                    list = new ProviderSearchResult();
                    list.setTitle(str3);
                    list.setYear(str);
                    list.setPageUrl(stringBuilder3);
                    list.setImdb(str2);
                    list.setContent(request);
                    return list;
                }
            }
        } catch (List<String> list2) {
            list2.printStackTrace();
        }
        return null;
    }

    public ProviderSearchResult getTvShow(List<String> list, String str, String str2) {
        try {
            String str3 = (String) list.get(0);
            ProviderSearchResult providerSearchResult = new ProviderSearchResult();
            providerSearchResult.setTitle(str3);
            providerSearchResult.setYear(str);
            providerSearchResult.setPageUrl("");
            providerSearchResult.setImdb(str2);
            return providerSearchResult;
        } catch (List<String> list2) {
            list2.printStackTrace();
            return null;
        }
    }

    public ProviderSearchResult getEpisode(ProviderSearchResult providerSearchResult, List<String> list, String str, int i, int i2) {
        if (providerSearchResult != null) {
            try {
                for (String str2 : list) {
                    try {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(this.base_link);
                        stringBuilder.append("/tvshows/");
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(str2);
                        stringBuilder2.append(StringUtils.SPACE);
                        stringBuilder2.append(str);
                        stringBuilder.append(cleantitleurl(stringBuilder2.toString()));
                        String stringBuilder3 = stringBuilder.toString();
                        String request = request(stringBuilder3);
                        if (!request.toLowerCase().contains("page not found")) {
                            ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                            providerSearchResult2.setTitle(str2);
                            providerSearchResult2.setYear(str);
                            providerSearchResult2.setPageUrl(stringBuilder3);
                            providerSearchResult2.setImdb(providerSearchResult.getImdb());
                            providerSearchResult2.setContent(request);
                            providerSearchResult2.setSeason(i);
                            providerSearchResult2.setEpisode(i2);
                            return providerSearchResult2;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (ProviderSearchResult providerSearchResult3) {
                providerSearchResult3.printStackTrace();
            }
        }
        return null;
    }

    public String grablink(String str, String str2) {
        try {
            Connection method = Jsoup.connect(str).ignoreHttpErrors(false).validateTLSCertificates(false).ignoreContentType(true).followRedirects(true).userAgent(UserAgent).timeout(10000).method(Method.GET);
            if (!(str2 == null || str2.isEmpty())) {
                method.referrer(str2);
            }
            str2 = method.execute();
            String url = str2.url().toString();
            if (!(url == null || url.isEmpty())) {
                if (!url.equals(str)) {
                    str = url;
                    return str;
                }
            }
            str2 = Jsoup.parse(str2.body()).select("iframe").attr("src");
            if (str2 != null) {
                if (!str2.isEmpty()) {
                    str = str2;
                }
            }
            return str;
        } catch (String str22) {
            str22.printStackTrace();
            return str;
        }
    }

    public void getSources(io.github.morpheustv.scrapers.model.ProviderSearchResult r11, java.util.concurrent.CopyOnWriteArrayList<io.github.morpheustv.scrapers.model.Source> r12) {
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
        r10 = this;
        if (r11 != 0) goto L_0x000b;
    L_0x0002:
        r11 = new java.lang.Exception;	 Catch:{ Exception -> 0x0008 }
        r11.<init>();	 Catch:{ Exception -> 0x0008 }
        throw r11;	 Catch:{ Exception -> 0x0008 }
    L_0x0008:
        r11 = move-exception;	 Catch:{ Exception -> 0x0008 }
        goto L_0x010d;	 Catch:{ Exception -> 0x0008 }
    L_0x000b:
        r0 = r11.getContent();	 Catch:{ Exception -> 0x0008 }
        r0 = org.jsoup.Jsoup.parse(r0);	 Catch:{ Exception -> 0x0008 }
        r1 = r11.getSeason();	 Catch:{ Exception -> 0x0008 }
        if (r1 <= 0) goto L_0x00ce;	 Catch:{ Exception -> 0x0008 }
    L_0x0019:
        r1 = r11.getEpisode();	 Catch:{ Exception -> 0x0008 }
        if (r1 <= 0) goto L_0x00ce;	 Catch:{ Exception -> 0x0008 }
    L_0x001f:
        r1 = "tr.season";	 Catch:{ Exception -> 0x0008 }
        r0 = r0.select(r1);	 Catch:{ Exception -> 0x0008 }
        r0 = r0.iterator();	 Catch:{ Exception -> 0x0008 }
    L_0x0029:
        r1 = r0.hasNext();	 Catch:{ Exception -> 0x0008 }
        if (r1 == 0) goto L_0x0110;	 Catch:{ Exception -> 0x0008 }
    L_0x002f:
        r1 = r0.next();	 Catch:{ Exception -> 0x0008 }
        r1 = (org.jsoup.nodes.Element) r1;	 Catch:{ Exception -> 0x0008 }
        r2 = "span.title";	 Catch:{ Exception -> 0x0008 }
        r2 = r1.select(r2);	 Catch:{ Exception -> 0x0008 }
        r2 = r2.first();	 Catch:{ Exception -> 0x0008 }
        r2 = r2.text();	 Catch:{ Exception -> 0x0008 }
        r3 = "Season";	 Catch:{ Exception -> 0x0008 }
        r4 = "";	 Catch:{ Exception -> 0x0008 }
        r2 = r2.replace(r3, r4);	 Catch:{ Exception -> 0x0008 }
        r2 = r2.trim();	 Catch:{ Exception -> 0x0008 }
        r3 = r11.getSeason();	 Catch:{ Exception -> 0x0008 }
        r3 = java.lang.String.valueOf(r3);	 Catch:{ Exception -> 0x0008 }
        r2 = r2.equals(r3);	 Catch:{ Exception -> 0x0008 }
        if (r2 == 0) goto L_0x0029;	 Catch:{ Exception -> 0x0008 }
    L_0x005d:
        r2 = "ul.episodes";	 Catch:{ Exception -> 0x0008 }
        r1 = r1.select(r2);	 Catch:{ Exception -> 0x0008 }
        r2 = "li";	 Catch:{ Exception -> 0x0008 }
        r1 = r1.select(r2);	 Catch:{ Exception -> 0x0008 }
        r1 = r1.iterator();	 Catch:{ Exception -> 0x0008 }
    L_0x006d:
        r2 = r1.hasNext();	 Catch:{ Exception -> 0x0008 }
        if (r2 == 0) goto L_0x0029;	 Catch:{ Exception -> 0x0008 }
    L_0x0073:
        r2 = r1.next();	 Catch:{ Exception -> 0x0008 }
        r2 = (org.jsoup.nodes.Element) r2;	 Catch:{ Exception -> 0x0008 }
        r3 = "a";	 Catch:{ Exception -> 0x0008 }
        r3 = r2.select(r3);	 Catch:{ Exception -> 0x0008 }
        r4 = "href";	 Catch:{ Exception -> 0x0008 }
        r3 = r3.attr(r4);	 Catch:{ Exception -> 0x0008 }
        r4 = "h5";	 Catch:{ Exception -> 0x0008 }
        r2 = r2.select(r4);	 Catch:{ Exception -> 0x0008 }
        r2 = r2.text();	 Catch:{ Exception -> 0x0008 }
        r4 = "EP";	 Catch:{ Exception -> 0x0008 }
        r5 = "";	 Catch:{ Exception -> 0x0008 }
        r2 = r2.replace(r4, r5);	 Catch:{ Exception -> 0x0008 }
        r4 = ":";	 Catch:{ Exception -> 0x0008 }
        r5 = "";	 Catch:{ Exception -> 0x0008 }
        r2 = r2.replace(r4, r5);	 Catch:{ Exception -> 0x0008 }
        r2 = r2.trim();	 Catch:{ Exception -> 0x0008 }
        r4 = r11.getEpisode();	 Catch:{ Exception -> 0x0008 }
        r4 = java.lang.String.valueOf(r4);	 Catch:{ Exception -> 0x0008 }
        r2 = r2.equals(r4);	 Catch:{ Exception -> 0x0008 }
        if (r2 == 0) goto L_0x006d;	 Catch:{ Exception -> 0x0008 }
    L_0x00b1:
        r2 = r11.getPageUrl();	 Catch:{ Exception -> 0x0008 }
        r5 = r10.grablink(r3, r2);	 Catch:{ Exception -> 0x0008 }
        r6 = r11.getPageUrl();	 Catch:{ Exception -> 0x0008 }
        r7 = 0;	 Catch:{ Exception -> 0x0008 }
        r9 = r11.getTitle();	 Catch:{ Exception -> 0x0008 }
        r4 = r10;	 Catch:{ Exception -> 0x0008 }
        r8 = r12;	 Catch:{ Exception -> 0x0008 }
        r4.processLink(r5, r6, r7, r8, r9);	 Catch:{ Exception -> 0x0008 }
        r2 = r10.hasMaxSources(r12);	 Catch:{ Exception -> 0x0008 }
        if (r2 == 0) goto L_0x006d;	 Catch:{ Exception -> 0x0008 }
    L_0x00cd:
        return;	 Catch:{ Exception -> 0x0008 }
    L_0x00ce:
        r1 = "tbody#stream-list";	 Catch:{ Exception -> 0x0008 }
        r0 = r0.select(r1);	 Catch:{ Exception -> 0x0008 }
        r1 = "a";	 Catch:{ Exception -> 0x0008 }
        r0 = r0.select(r1);	 Catch:{ Exception -> 0x0008 }
        r0 = r0.iterator();	 Catch:{ Exception -> 0x0008 }
    L_0x00de:
        r1 = r0.hasNext();	 Catch:{ Exception -> 0x0008 }
        if (r1 == 0) goto L_0x0110;	 Catch:{ Exception -> 0x0008 }
    L_0x00e4:
        r1 = r0.next();	 Catch:{ Exception -> 0x0008 }
        r1 = (org.jsoup.nodes.Element) r1;	 Catch:{ Exception -> 0x0008 }
        r2 = "href";	 Catch:{ Exception -> 0x00de }
        r1 = r1.attr(r2);	 Catch:{ Exception -> 0x00de }
        r2 = r11.getPageUrl();	 Catch:{ Exception -> 0x00de }
        r4 = r10.grablink(r1, r2);	 Catch:{ Exception -> 0x00de }
        r5 = r11.getPageUrl();	 Catch:{ Exception -> 0x00de }
        r6 = 0;	 Catch:{ Exception -> 0x00de }
        r8 = r11.getTitle();	 Catch:{ Exception -> 0x00de }
        r3 = r10;	 Catch:{ Exception -> 0x00de }
        r7 = r12;	 Catch:{ Exception -> 0x00de }
        r3.processLink(r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x00de }
        r1 = r10.hasMaxSources(r12);	 Catch:{ Exception -> 0x00de }
        if (r1 == 0) goto L_0x00de;
    L_0x010c:
        return;
    L_0x010d:
        r11.printStackTrace();
    L_0x0110:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.github.morpheustv.scrapers.providers.CineBloomProvider.getSources(io.github.morpheustv.scrapers.model.ProviderSearchResult, java.util.concurrent.CopyOnWriteArrayList):void");
    }
}
