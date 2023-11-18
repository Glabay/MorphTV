package io.github.morpheustv.scrapers.providers;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class HdGoProvider extends BaseProvider {
    String base_link;
    String[] domains;

    public HdGoProvider(Scraper scraper) {
        super(scraper, "HDGO.TO", false);
        this.domains = new String[]{"www1.hdgo.to"};
        this.base_link = "http://www1.hdgo.to";
        this.resourceWhitelist = new String[]{"(.*base64.*)", "(.*jquery.*.js.*)", "(.*piguiqproxy.*)", "(.*amgload.*)", "(.*smcheck.*)"};
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            for (String str3 : list) {
                try {
                    String attr;
                    URL url = new URL(this.base_link);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("/search-movies/");
                    stringBuilder.append(URLEncoder.encode(str3, "UTF-8"));
                    stringBuilder.append(".html");
                    Iterator it = Jsoup.connect(new URL(url, stringBuilder.toString()).toString()).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(5000).get().select("div.listItem").iterator();
                    while (it.hasNext()) {
                        Element element = (Element) it.next();
                        String text = element.select("div.title").select("a").text();
                        if (element.select("div.status-year").text().equals(str)) {
                            if (!BaseProvider.cleantitle(text).equals(BaseProvider.cleantitle(str3))) {
                                text = BaseProvider.cleantitle(text);
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(str3);
                                stringBuilder2.append(str);
                                if (text.equals(BaseProvider.cleantitle(stringBuilder2.toString()))) {
                                }
                            }
                            attr = element.select("div.title").select("a").attr("href");
                            break;
                        }
                    }
                    attr = null;
                    if (attr != null) {
                        ProviderSearchResult providerSearchResult = new ProviderSearchResult();
                        providerSearchResult.setTitle(str3);
                        providerSearchResult.setYear(str);
                        providerSearchResult.setPageUrl(attr);
                        return providerSearchResult;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (List<String> list2) {
            list2.printStackTrace();
        }
        return null;
    }

    public ProviderSearchResult getTvShow(List<String> list, String str, String str2) {
        try {
            String str3 = (String) list.get(null);
            str2 = new ProviderSearchResult();
            str2.setTitle(str3);
            str2.setYear(str);
            str2.setPageUrl("");
            return str2;
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
                        String attr;
                        StringBuilder stringBuilder = new StringBuilder();
                        URL url = new URL(this.base_link);
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("/search-movies/");
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(str2);
                        stringBuilder3.append(" season ");
                        stringBuilder3.append(String.valueOf(i));
                        stringBuilder2.append(URLEncoder.encode(stringBuilder3.toString(), "UTF-8"));
                        stringBuilder.append(new URL(url, stringBuilder2.toString()).toString());
                        stringBuilder.append(".html");
                        Iterator it = Jsoup.connect(stringBuilder.toString()).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(5000).get().select("div.listItem").iterator();
                        while (it.hasNext()) {
                            Element element = (Element) it.next();
                            String text = element.select("div.title").select("a").text();
                            if (BaseProvider.cleantitle(text).startsWith(BaseProvider.cleantitle(str2))) {
                                text = BaseProvider.cleantitle(text);
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("Season ");
                                stringBuilder2.append(String.valueOf(i));
                                if (text.contains(BaseProvider.cleantitle(stringBuilder2.toString()))) {
                                    attr = element.select("div.title").select("a").attr("href");
                                    break;
                                }
                            }
                        }
                        attr = null;
                        if (attr != null) {
                            ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                            providerSearchResult2.setTitle(str2);
                            providerSearchResult2.setYear(str);
                            providerSearchResult2.setPageUrl(attr);
                            providerSearchResult2.setSeason(i);
                            providerSearchResult2.setEpisode(i2);
                            return providerSearchResult2;
                        }
                    } catch (List<String> list2) {
                        list2.printStackTrace();
                    }
                }
            } catch (ProviderSearchResult providerSearchResult3) {
                providerSearchResult3.printStackTrace();
            }
        }
        return null;
    }

    public void getSources(io.github.morpheustv.scrapers.model.ProviderSearchResult r13, java.util.concurrent.CopyOnWriteArrayList<io.github.morpheustv.scrapers.model.Source> r14) {
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
        r12 = this;
        if (r13 != 0) goto L_0x000b;
    L_0x0002:
        r13 = new java.lang.Exception;	 Catch:{ Exception -> 0x0008 }
        r13.<init>();	 Catch:{ Exception -> 0x0008 }
        throw r13;	 Catch:{ Exception -> 0x0008 }
    L_0x0008:
        r13 = move-exception;	 Catch:{ Exception -> 0x0008 }
        goto L_0x01ba;	 Catch:{ Exception -> 0x0008 }
    L_0x000b:
        r0 = r13.getPageUrl();	 Catch:{ Exception -> 0x0008 }
        r0 = org.jsoup.Jsoup.connect(r0);	 Catch:{ Exception -> 0x0008 }
        r1 = 0;	 Catch:{ Exception -> 0x0008 }
        r0 = r0.validateTLSCertificates(r1);	 Catch:{ Exception -> 0x0008 }
        r2 = 1;	 Catch:{ Exception -> 0x0008 }
        r0 = r0.ignoreHttpErrors(r2);	 Catch:{ Exception -> 0x0008 }
        r0 = r0.ignoreContentType(r2);	 Catch:{ Exception -> 0x0008 }
        r3 = UserAgent;	 Catch:{ Exception -> 0x0008 }
        r0 = r0.userAgent(r3);	 Catch:{ Exception -> 0x0008 }
        r3 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ Exception -> 0x0008 }
        r0 = r0.timeout(r3);	 Catch:{ Exception -> 0x0008 }
        r0 = r0.get();	 Catch:{ Exception -> 0x0008 }
        r4 = r13.getSeason();	 Catch:{ Exception -> 0x0008 }
        if (r4 <= 0) goto L_0x0089;	 Catch:{ Exception -> 0x0008 }
    L_0x0037:
        r4 = r13.getEpisode();	 Catch:{ Exception -> 0x0008 }
        if (r4 <= 0) goto L_0x0089;	 Catch:{ Exception -> 0x0008 }
    L_0x003d:
        r4 = "a.episode_series_link";	 Catch:{ Exception -> 0x0008 }
        r4 = r0.select(r4);	 Catch:{ Exception -> 0x0008 }
        r4 = r4.iterator();	 Catch:{ Exception -> 0x0008 }
    L_0x0047:
        r5 = r4.hasNext();	 Catch:{ Exception -> 0x0008 }
        if (r5 == 0) goto L_0x0089;	 Catch:{ Exception -> 0x0008 }
    L_0x004d:
        r5 = r4.next();	 Catch:{ Exception -> 0x0008 }
        r5 = (org.jsoup.nodes.Element) r5;	 Catch:{ Exception -> 0x0008 }
        r6 = r5.text();	 Catch:{ Exception -> 0x0008 }
        r7 = "href";	 Catch:{ Exception -> 0x0008 }
        r5 = r5.attr(r7);	 Catch:{ Exception -> 0x0008 }
        r7 = r13.getEpisode();	 Catch:{ Exception -> 0x0008 }
        r7 = java.lang.String.valueOf(r7);	 Catch:{ Exception -> 0x0008 }
        r6 = r6.equals(r7);	 Catch:{ Exception -> 0x0008 }
        if (r6 == 0) goto L_0x0047;	 Catch:{ Exception -> 0x0008 }
    L_0x006b:
        r0 = org.jsoup.Jsoup.connect(r5);	 Catch:{ Exception -> 0x0008 }
        r0 = r0.validateTLSCertificates(r1);	 Catch:{ Exception -> 0x0008 }
        r0 = r0.ignoreHttpErrors(r2);	 Catch:{ Exception -> 0x0008 }
        r0 = r0.ignoreContentType(r2);	 Catch:{ Exception -> 0x0008 }
        r4 = UserAgent;	 Catch:{ Exception -> 0x0008 }
        r0 = r0.userAgent(r4);	 Catch:{ Exception -> 0x0008 }
        r0 = r0.timeout(r3);	 Catch:{ Exception -> 0x0008 }
        r0 = r0.get();	 Catch:{ Exception -> 0x0008 }
    L_0x0089:
        r4 = "div.movieBlock";	 Catch:{ Exception -> 0x0008 }
        r4 = r0.select(r4);	 Catch:{ Exception -> 0x0008 }
        r5 = "script";	 Catch:{ Exception -> 0x0008 }
        r4 = r4.select(r5);	 Catch:{ Exception -> 0x0008 }
        r4 = r4.first();	 Catch:{ Exception -> 0x0008 }
        r4 = r4.html();	 Catch:{ Exception -> 0x0008 }
        r5 = "(\"";	 Catch:{ Exception -> 0x0008 }
        r5 = r4.indexOf(r5);	 Catch:{ Exception -> 0x0008 }
        r5 = r5 + 2;	 Catch:{ Exception -> 0x0008 }
        r6 = "\")";	 Catch:{ Exception -> 0x0008 }
        r6 = r4.indexOf(r6);	 Catch:{ Exception -> 0x0008 }
        r4 = r4.substring(r5, r6);	 Catch:{ Exception -> 0x0008 }
        r5 = new java.lang.String;	 Catch:{ Exception -> 0x0008 }
        r4 = android.util.Base64.decode(r4, r1);	 Catch:{ Exception -> 0x0008 }
        r5.<init>(r4);	 Catch:{ Exception -> 0x0008 }
        r4 = org.jsoup.Jsoup.parse(r5);	 Catch:{ Exception -> 0x0008 }
        r5 = "iframe";	 Catch:{ Exception -> 0x0008 }
        r4 = r4.select(r5);	 Catch:{ Exception -> 0x0008 }
        r4 = r4.first();	 Catch:{ Exception -> 0x0008 }
        if (r4 == 0) goto L_0x00e3;	 Catch:{ Exception -> 0x0008 }
    L_0x00c8:
        r5 = "src";	 Catch:{ Exception -> 0x0008 }
        r7 = r4.attr(r5);	 Catch:{ Exception -> 0x0008 }
        r8 = r13.getPageUrl();	 Catch:{ Exception -> 0x0008 }
        r9 = 0;	 Catch:{ Exception -> 0x0008 }
        r11 = r13.getTitle();	 Catch:{ Exception -> 0x0008 }
        r6 = r12;	 Catch:{ Exception -> 0x0008 }
        r10 = r14;	 Catch:{ Exception -> 0x0008 }
        r6.processLink(r7, r8, r9, r10, r11);	 Catch:{ Exception -> 0x0008 }
        r4 = r12.hasMaxSources(r14);	 Catch:{ Exception -> 0x0008 }
        if (r4 == 0) goto L_0x00e3;	 Catch:{ Exception -> 0x0008 }
    L_0x00e2:
        return;	 Catch:{ Exception -> 0x0008 }
    L_0x00e3:
        r4 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0008 }
        r4.<init>();	 Catch:{ Exception -> 0x0008 }
        r5 = "div.server_line";	 Catch:{ Exception -> 0x0008 }
        r0 = r0.select(r5);	 Catch:{ Exception -> 0x0008 }
        r0 = r0.iterator();	 Catch:{ Exception -> 0x0008 }
    L_0x00f2:
        r5 = r0.hasNext();	 Catch:{ Exception -> 0x0008 }
        if (r5 == 0) goto L_0x012f;	 Catch:{ Exception -> 0x0008 }
    L_0x00f8:
        r5 = r0.next();	 Catch:{ Exception -> 0x0008 }
        r5 = (org.jsoup.nodes.Element) r5;	 Catch:{ Exception -> 0x0008 }
        r6 = "p.server_servername";	 Catch:{ Exception -> 0x012a }
        r6 = r5.select(r6);	 Catch:{ Exception -> 0x012a }
        r6 = r6.text();	 Catch:{ Exception -> 0x012a }
        r6 = r6.toLowerCase();	 Catch:{ Exception -> 0x012a }
        r7 = "p.server_play";	 Catch:{ Exception -> 0x012a }
        r5 = r5.select(r7);	 Catch:{ Exception -> 0x012a }
        r7 = "a";	 Catch:{ Exception -> 0x012a }
        r5 = r5.select(r7);	 Catch:{ Exception -> 0x012a }
        r7 = "href";	 Catch:{ Exception -> 0x012a }
        r5 = r5.attr(r7);	 Catch:{ Exception -> 0x012a }
        r7 = "openload";	 Catch:{ Exception -> 0x012a }
        r6 = r6.contains(r7);	 Catch:{ Exception -> 0x012a }
        if (r6 == 0) goto L_0x00f2;	 Catch:{ Exception -> 0x012a }
    L_0x0126:
        r4.add(r5);	 Catch:{ Exception -> 0x012a }
        goto L_0x00f2;
    L_0x012a:
        r5 = move-exception;
        r5.printStackTrace();	 Catch:{ Exception -> 0x0008 }
        goto L_0x00f2;	 Catch:{ Exception -> 0x0008 }
    L_0x012f:
        r0 = r4.iterator();	 Catch:{ Exception -> 0x0008 }
    L_0x0133:
        r4 = r0.hasNext();	 Catch:{ Exception -> 0x0008 }
        if (r4 == 0) goto L_0x01bd;	 Catch:{ Exception -> 0x0008 }
    L_0x0139:
        r4 = r0.next();	 Catch:{ Exception -> 0x0008 }
        r7 = r4;	 Catch:{ Exception -> 0x0008 }
        r7 = (java.lang.String) r7;	 Catch:{ Exception -> 0x0008 }
        r4 = org.jsoup.Jsoup.connect(r7);	 Catch:{ Exception -> 0x0133 }
        r4 = r4.validateTLSCertificates(r1);	 Catch:{ Exception -> 0x0133 }
        r4 = r4.ignoreHttpErrors(r2);	 Catch:{ Exception -> 0x0133 }
        r4 = r4.ignoreContentType(r2);	 Catch:{ Exception -> 0x0133 }
        r5 = UserAgent;	 Catch:{ Exception -> 0x0133 }
        r4 = r4.userAgent(r5);	 Catch:{ Exception -> 0x0133 }
        r4 = r4.timeout(r3);	 Catch:{ Exception -> 0x0133 }
        r4 = r4.get();	 Catch:{ Exception -> 0x0133 }
        r5 = "div.movieBlock";	 Catch:{ Exception -> 0x0133 }
        r4 = r4.select(r5);	 Catch:{ Exception -> 0x0133 }
        r5 = "script";	 Catch:{ Exception -> 0x0133 }
        r4 = r4.select(r5);	 Catch:{ Exception -> 0x0133 }
        r4 = r4.first();	 Catch:{ Exception -> 0x0133 }
        r4 = r4.html();	 Catch:{ Exception -> 0x0133 }
        r5 = "(\"";	 Catch:{ Exception -> 0x0133 }
        r5 = r4.indexOf(r5);	 Catch:{ Exception -> 0x0133 }
        r5 = r5 + 2;	 Catch:{ Exception -> 0x0133 }
        r6 = "\")";	 Catch:{ Exception -> 0x0133 }
        r6 = r4.indexOf(r6);	 Catch:{ Exception -> 0x0133 }
        r4 = r4.substring(r5, r6);	 Catch:{ Exception -> 0x0133 }
        r5 = new java.lang.String;	 Catch:{ Exception -> 0x0133 }
        r4 = android.util.Base64.decode(r4, r1);	 Catch:{ Exception -> 0x0133 }
        r5.<init>(r4);	 Catch:{ Exception -> 0x0133 }
        r4 = org.jsoup.Jsoup.parse(r5);	 Catch:{ Exception -> 0x0133 }
        r5 = "iframe";	 Catch:{ Exception -> 0x0133 }
        r4 = r4.select(r5);	 Catch:{ Exception -> 0x0133 }
        r4 = r4.first();	 Catch:{ Exception -> 0x0133 }
        r5 = "src";	 Catch:{ Exception -> 0x0133 }
        r6 = r4.attr(r5);	 Catch:{ Exception -> 0x0133 }
        r4 = "openload.co";	 Catch:{ Exception -> 0x0133 }
        r4 = r6.contains(r4);	 Catch:{ Exception -> 0x0133 }
        if (r4 == 0) goto L_0x0133;	 Catch:{ Exception -> 0x0133 }
    L_0x01a9:
        r8 = 0;	 Catch:{ Exception -> 0x0133 }
        r10 = r13.getTitle();	 Catch:{ Exception -> 0x0133 }
        r5 = r12;	 Catch:{ Exception -> 0x0133 }
        r9 = r14;	 Catch:{ Exception -> 0x0133 }
        r5.processLink(r6, r7, r8, r9, r10);	 Catch:{ Exception -> 0x0133 }
        r4 = r12.hasMaxSources(r14);	 Catch:{ Exception -> 0x0133 }
        if (r4 == 0) goto L_0x0133;
    L_0x01b9:
        return;
    L_0x01ba:
        r13.printStackTrace();
    L_0x01bd:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.github.morpheustv.scrapers.providers.HdGoProvider.getSources(io.github.morpheustv.scrapers.model.ProviderSearchResult, java.util.concurrent.CopyOnWriteArrayList):void");
    }
}
