package io.github.morpheustv.scrapers.providers;

import android.webkit.CookieManager;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.common.net.HttpHeaders;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class SeriesFreeProvider extends BaseProvider {
    String base_link;
    String[] domains;

    public SeriesFreeProvider(Scraper scraper) {
        super(scraper, "SERIESFREE.TO", true);
        this.domains = new String[]{"seriesfree.to"};
        this.base_link = "https://seriesfree.to";
        this.resourceWhitelist = new String[]{"(.*/search/.*)", "(.*jquery.*.js.*)"};
    }

    public ProviderSearchResult getTvShow(List<String> list, String str, String str2) {
        try {
            String str3 = (String) list.get(0);
            ProviderSearchResult providerSearchResult = new ProviderSearchResult();
            providerSearchResult.setTitle(str3);
            providerSearchResult.setTitles(list);
            providerSearchResult.setYear(str);
            providerSearchResult.setImdb(str2);
            providerSearchResult.setPageUrl("");
            return providerSearchResult;
        } catch (List<String> list2) {
            list2.printStackTrace();
            return null;
        }
    }

    public ProviderSearchResult getEpisode(ProviderSearchResult providerSearchResult, List<String> list, String str, int i, int i2) {
        if (providerSearchResult != null) {
            try {
                if (providerSearchResult.getPageUrl() != null) {
                    wvgethtml(this.base_link);
                    providerSearchResult = CookieManager.getInstance().getCookie(this.base_link);
                    for (String str2 : list) {
                        int i3 = 0;
                        while (i3 < 3) {
                            try {
                                String attr;
                                URL url = new URL(this.base_link);
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("/suggest.php?ajax=1&s=");
                                stringBuilder.append(URLEncoder.encode(cleantitleurl(str2).replace("-", StringUtils.SPACE), "UTF-8"));
                                stringBuilder.append("&type=TVShows");
                                Iterator it = Jsoup.parse(Jsoup.connect(new URL(url, stringBuilder.toString()).toString()).validateTLSCertificates(false).ignoreHttpErrors(false).ignoreContentType(true).header(HttpHeaders.COOKIE, providerSearchResult != null ? providerSearchResult : "").referrer(this.base_link).header(HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest").header(HttpHeaders.ACCEPT, "*/*").header(HttpHeaders.ACCEPT_LANGUAGE, "pt-PT,pt;q=0.9,en-US;q=0.8,en;q=0.7").header(HttpHeaders.CACHE_CONTROL, "no-cache").header(HttpHeaders.PRAGMA, "no-cache").header(HttpHeaders.HOST, "seriesfree.to").header(HttpHeaders.CONNECTION, "keep-alive").header("Upgrade-Insecure-Requests:", "1").userAgent(UserAgent).method(Method.GET).timeout(2000).execute().body()).select("li").iterator();
                                while (it.hasNext()) {
                                    Element element = (Element) ((Element) it.next()).select("a").get(0);
                                    String text = element.select(TtmlNode.TAG_SPAN).text();
                                    attr = element.attr("href");
                                    if (BaseProvider.cleantitle(text).equals(BaseProvider.cleantitle(str2))) {
                                        break;
                                    }
                                }
                                attr = null;
                                if (attr != null) {
                                    ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                                    providerSearchResult2.setTitle(str2);
                                    providerSearchResult2.setYear(str);
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(this.base_link);
                                    stringBuilder.append(attr.replace("/serie/", "/episode/"));
                                    stringBuilder.append("_s");
                                    stringBuilder.append(String.valueOf(i));
                                    stringBuilder.append("_e");
                                    stringBuilder.append(String.valueOf(i2));
                                    stringBuilder.append(".html");
                                    providerSearchResult2.setPageUrl(stringBuilder.toString());
                                    providerSearchResult2.setEpisode(i2);
                                    providerSearchResult2.setSeason(i);
                                    return providerSearchResult2;
                                }
                                i3++;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (ProviderSearchResult providerSearchResult3) {
                providerSearchResult3.printStackTrace();
            }
        }
        return null;
    }

    public void getSources(io.github.morpheustv.scrapers.model.ProviderSearchResult r14, java.util.concurrent.CopyOnWriteArrayList<io.github.morpheustv.scrapers.model.Source> r15) {
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
        r13 = this;
        r7 = r13;
        if (r14 == 0) goto L_0x012e;
    L_0x0003:
        r8 = 0;
        r9 = 0;
        r2 = r8;
        r1 = 0;
    L_0x0007:
        r3 = 3;
        r10 = 2000; // 0x7d0 float:2.803E-42 double:9.88E-321;
        r11 = 1;
        if (r1 >= r3) goto L_0x0048;
    L_0x000d:
        r3 = r14.getPageUrl();	 Catch:{ Exception -> 0x003b }
        r3 = org.jsoup.Jsoup.connect(r3);	 Catch:{ Exception -> 0x003b }
        r3 = r3.validateTLSCertificates(r9);	 Catch:{ Exception -> 0x003b }
        r3 = r3.ignoreContentType(r11);	 Catch:{ Exception -> 0x003b }
        r4 = UserAgent;	 Catch:{ Exception -> 0x003b }
        r3 = r3.userAgent(r4);	 Catch:{ Exception -> 0x003b }
        r3 = r3.timeout(r10);	 Catch:{ Exception -> 0x003b }
        r3 = r3.get();	 Catch:{ Exception -> 0x003b }
        r2 = r3.html();	 Catch:{ Exception -> 0x0039 }
        r4 = "404 Not Found";	 Catch:{ Exception -> 0x0039 }
        r2 = r2.contains(r4);	 Catch:{ Exception -> 0x0039 }
        if (r2 == 0) goto L_0x0041;
    L_0x0037:
        r3 = r8;
        goto L_0x0041;
    L_0x0039:
        r0 = move-exception;
        goto L_0x003d;
    L_0x003b:
        r0 = move-exception;
        r3 = r2;
    L_0x003d:
        r2 = r0;
        r2.printStackTrace();	 Catch:{ Exception -> 0x0129 }
    L_0x0041:
        r2 = r3;	 Catch:{ Exception -> 0x0129 }
        if (r2 == 0) goto L_0x0045;	 Catch:{ Exception -> 0x0129 }
    L_0x0044:
        goto L_0x0048;	 Catch:{ Exception -> 0x0129 }
    L_0x0045:
        r1 = r1 + 1;	 Catch:{ Exception -> 0x0129 }
        goto L_0x0007;	 Catch:{ Exception -> 0x0129 }
    L_0x0048:
        if (r2 == 0) goto L_0x012e;	 Catch:{ Exception -> 0x0129 }
    L_0x004a:
        r1 = "table.W";	 Catch:{ Exception -> 0x0129 }
        r1 = r2.select(r1);	 Catch:{ Exception -> 0x0129 }
        r1 = r1.get(r9);	 Catch:{ Exception -> 0x0129 }
        r1 = (org.jsoup.nodes.Element) r1;	 Catch:{ Exception -> 0x0129 }
        r2 = "tr";	 Catch:{ Exception -> 0x0129 }
        r1 = r1.select(r2);	 Catch:{ Exception -> 0x0129 }
        java.util.Collections.shuffle(r1);	 Catch:{ Exception -> 0x0129 }
        r12 = r1.iterator();	 Catch:{ Exception -> 0x0129 }
    L_0x0063:
        r1 = r12.hasNext();	 Catch:{ Exception -> 0x0129 }
        if (r1 == 0) goto L_0x012e;	 Catch:{ Exception -> 0x0129 }
    L_0x0069:
        r1 = r12.next();	 Catch:{ Exception -> 0x0129 }
        r1 = (org.jsoup.nodes.Element) r1;	 Catch:{ Exception -> 0x0129 }
        r2 = r7.isAlive();	 Catch:{ Exception -> 0x0121 }
        if (r2 != 0) goto L_0x0076;	 Catch:{ Exception -> 0x0121 }
    L_0x0075:
        return;	 Catch:{ Exception -> 0x0121 }
    L_0x0076:
        r2 = "td";	 Catch:{ Exception -> 0x0121 }
        r2 = r1.select(r2);	 Catch:{ Exception -> 0x0121 }
        r2 = r2.get(r9);	 Catch:{ Exception -> 0x0121 }
        r2 = (org.jsoup.nodes.Element) r2;	 Catch:{ Exception -> 0x0121 }
        r2 = r2.ownText();	 Catch:{ Exception -> 0x0121 }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0121 }
        r3.<init>();	 Catch:{ Exception -> 0x0121 }
        r4 = r7.base_link;	 Catch:{ Exception -> 0x0121 }
        r3.append(r4);	 Catch:{ Exception -> 0x0121 }
        r4 = "a.watch-btn";	 Catch:{ Exception -> 0x0121 }
        r1 = r1.select(r4);	 Catch:{ Exception -> 0x0121 }
        r4 = "href";	 Catch:{ Exception -> 0x0121 }
        r1 = r1.attr(r4);	 Catch:{ Exception -> 0x0121 }
        r3.append(r1);	 Catch:{ Exception -> 0x0121 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x0121 }
        r1 = "vidoza";	 Catch:{ Exception -> 0x0121 }
        r1 = r2.contains(r1);	 Catch:{ Exception -> 0x0121 }
        if (r1 != 0) goto L_0x00d3;	 Catch:{ Exception -> 0x0121 }
    L_0x00ab:
        r1 = "vidup";	 Catch:{ Exception -> 0x0121 }
        r1 = r2.contains(r1);	 Catch:{ Exception -> 0x0121 }
        if (r1 != 0) goto L_0x00d3;	 Catch:{ Exception -> 0x0121 }
    L_0x00b3:
        r1 = "streamango";	 Catch:{ Exception -> 0x0121 }
        r1 = r2.contains(r1);	 Catch:{ Exception -> 0x0121 }
        if (r1 != 0) goto L_0x00d3;	 Catch:{ Exception -> 0x0121 }
    L_0x00bb:
        r1 = "vidlox";	 Catch:{ Exception -> 0x0121 }
        r1 = r2.contains(r1);	 Catch:{ Exception -> 0x0121 }
        if (r1 != 0) goto L_0x00d3;	 Catch:{ Exception -> 0x0121 }
    L_0x00c3:
        r1 = "openload";	 Catch:{ Exception -> 0x0121 }
        r1 = r2.contains(r1);	 Catch:{ Exception -> 0x0121 }
        if (r1 != 0) goto L_0x00d3;	 Catch:{ Exception -> 0x0121 }
    L_0x00cb:
        r1 = "speedvid";	 Catch:{ Exception -> 0x0121 }
        r1 = r2.contains(r1);	 Catch:{ Exception -> 0x0121 }
        if (r1 == 0) goto L_0x011e;
    L_0x00d3:
        r1 = 0;
    L_0x00d4:
        r2 = 2;
        if (r1 >= r2) goto L_0x00f5;
    L_0x00d7:
        r2 = org.jsoup.Jsoup.connect(r3);	 Catch:{ Exception -> 0x00f2 }
        r2 = r2.validateTLSCertificates(r9);	 Catch:{ Exception -> 0x00f2 }
        r2 = r2.ignoreContentType(r11);	 Catch:{ Exception -> 0x00f2 }
        r4 = UserAgent;	 Catch:{ Exception -> 0x00f2 }
        r2 = r2.userAgent(r4);	 Catch:{ Exception -> 0x00f2 }
        r2 = r2.timeout(r10);	 Catch:{ Exception -> 0x00f2 }
        r2 = r2.get();	 Catch:{ Exception -> 0x00f2 }
        goto L_0x00f6;
    L_0x00f2:
        r1 = r1 + 1;
        goto L_0x00d4;
    L_0x00f5:
        r2 = r8;
    L_0x00f6:
        if (r2 == 0) goto L_0x011e;
    L_0x00f8:
        r1 = "a.action-btn";	 Catch:{ Exception -> 0x0121 }
        r1 = r2.select(r1);	 Catch:{ Exception -> 0x0121 }
        r1 = r1.get(r9);	 Catch:{ Exception -> 0x0121 }
        r1 = (org.jsoup.nodes.Element) r1;	 Catch:{ Exception -> 0x0121 }
        r2 = "href";	 Catch:{ Exception -> 0x0121 }
        r2 = r1.attr(r2);	 Catch:{ Exception -> 0x0121 }
        r4 = 0;	 Catch:{ Exception -> 0x0121 }
        r6 = r14.getTitle();	 Catch:{ Exception -> 0x0121 }
        r1 = r7;	 Catch:{ Exception -> 0x0121 }
        r5 = r15;	 Catch:{ Exception -> 0x0121 }
        r1.processLink(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x0121 }
        r1 = r15;
        r2 = r7.hasMaxSources(r1);	 Catch:{ Exception -> 0x011c }
        if (r2 == 0) goto L_0x0063;
    L_0x011b:
        return;
    L_0x011c:
        r0 = move-exception;
        goto L_0x0123;
    L_0x011e:
        r1 = r15;
        goto L_0x0063;
    L_0x0121:
        r0 = move-exception;
        r1 = r15;
    L_0x0123:
        r2 = r0;
        r2.printStackTrace();	 Catch:{ Exception -> 0x0129 }
        goto L_0x0063;
    L_0x0129:
        r0 = move-exception;
        r1 = r0;
        r1.printStackTrace();
    L_0x012e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.github.morpheustv.scrapers.providers.SeriesFreeProvider.getSources(io.github.morpheustv.scrapers.model.ProviderSearchResult, java.util.concurrent.CopyOnWriteArrayList):void");
    }
}
