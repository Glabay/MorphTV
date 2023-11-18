package io.github.morpheustv.scrapers.providers;

import com.google.common.net.HttpHeaders;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class YesMoviesProvider extends BaseProvider {
    String base_link;
    String[] domains;

    public YesMoviesProvider(Scraper scraper) {
        super(scraper, "YESMOVIES.TO", true);
        this.domains = new String[]{"yesmovies.to"};
        this.base_link = "https://yesmovies.to";
        this.resourceWhitelist = new String[]{"(.*chk_jschl?.*)", "(.*cdn.*.js.*)", "(.*mobile.*.js.*)", "(.*player.*.js.*)", "(.*movie_.*)"};
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            for (String str3 : list) {
                try {
                    String attr;
                    URL url = new URL(this.base_link);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("/search/");
                    stringBuilder.append(cleantitlequery(str3));
                    stringBuilder.append(".html");
                    Iterator it = Jsoup.connect(new URL(url, stringBuilder.toString()).toString()).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(10000).get().select("div.ml-item").iterator();
                    while (it.hasNext()) {
                        Element element = (Element) it.next();
                        if (BaseProvider.cleantitle(element.select("h2").text()).equals(BaseProvider.cleantitle(str3))) {
                            String attr2 = ((Element) element.select("a").get(0)).attr("data-url");
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append(this.base_link);
                            stringBuilder2.append("/");
                            stringBuilder2.append(attr2);
                            Document document = Jsoup.connect(stringBuilder2.toString()).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).header(HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest").userAgent(UserAgent).timeout(10000).get();
                            if (((Element) document.select("div.jt-info").get(1)).text().equals(str)) {
                                attr = document.select("a.btn-success").attr("href");
                                break;
                            }
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
                        URL url = new URL(this.base_link);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("/search/");
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(str2);
                        stringBuilder2.append(" season ");
                        stringBuilder2.append(String.valueOf(i));
                        stringBuilder.append(cleantitlequery(stringBuilder2.toString()));
                        stringBuilder.append(".html");
                        Iterator it = Jsoup.connect(new URL(url, stringBuilder.toString()).toString()).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(10000).get().select("div.ml-item").iterator();
                        while (it.hasNext()) {
                            Element element = (Element) it.next();
                            String text = element.select("h2").text();
                            if (BaseProvider.cleantitle(text).contains(BaseProvider.cleantitle(str2))) {
                                text = BaseProvider.cleantitle(text);
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("Season ");
                                stringBuilder2.append(String.valueOf(i));
                                if (text.contains(BaseProvider.cleantitle(stringBuilder2.toString()))) {
                                    attr = element.select("a").attr("href");
                                    break;
                                }
                            }
                        }
                        attr = null;
                        if (attr != null) {
                            ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                            providerSearchResult2.setTitle(str2);
                            providerSearchResult2.setYear(str);
                            providerSearchResult2.setPageUrl(attr.replace(".html", "/watching.html"));
                            providerSearchResult2.setEpisode(i2);
                            providerSearchResult2.setSeason(i);
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

    public void getSources(io.github.morpheustv.scrapers.model.ProviderSearchResult r15, java.util.concurrent.CopyOnWriteArrayList<io.github.morpheustv.scrapers.model.Source> r16) {
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
        r14 = this;
        r7 = r14;
        r8 = r16;
        if (r15 != 0) goto L_0x000f;
    L_0x0005:
        r1 = new java.lang.Exception;	 Catch:{ Exception -> 0x000b }
        r1.<init>();	 Catch:{ Exception -> 0x000b }
        throw r1;	 Catch:{ Exception -> 0x000b }
    L_0x000b:
        r0 = move-exception;	 Catch:{ Exception -> 0x000b }
        r1 = r0;	 Catch:{ Exception -> 0x000b }
        goto L_0x0210;	 Catch:{ Exception -> 0x000b }
    L_0x000f:
        r1 = r15.getPageUrl();	 Catch:{ Exception -> 0x000b }
        r1 = r7.wvgethtml(r1);	 Catch:{ Exception -> 0x000b }
        r1 = org.jsoup.Jsoup.parse(r1);	 Catch:{ Exception -> 0x000b }
        r2 = "li.ep-item";	 Catch:{ Exception -> 0x000b }
        r1 = r1.select(r2);	 Catch:{ Exception -> 0x000b }
        r9 = r1.iterator();	 Catch:{ Exception -> 0x000b }
    L_0x0025:
        r1 = r9.hasNext();	 Catch:{ Exception -> 0x000b }
        if (r1 == 0) goto L_0x0213;	 Catch:{ Exception -> 0x000b }
    L_0x002b:
        r1 = r9.next();	 Catch:{ Exception -> 0x000b }
        r1 = (org.jsoup.nodes.Element) r1;	 Catch:{ Exception -> 0x000b }
        r2 = "data-server";	 Catch:{ Exception -> 0x0209 }
        r2 = r1.attr(r2);	 Catch:{ Exception -> 0x0209 }
        r3 = "data-id";	 Catch:{ Exception -> 0x0209 }
        r3 = r1.attr(r3);	 Catch:{ Exception -> 0x0209 }
        r4 = "16";	 Catch:{ Exception -> 0x0209 }
        r4 = r2.equals(r4);	 Catch:{ Exception -> 0x0209 }
        if (r4 != 0) goto L_0x0075;	 Catch:{ Exception -> 0x0209 }
    L_0x0045:
        r4 = "15";	 Catch:{ Exception -> 0x0209 }
        r4 = r2.equals(r4);	 Catch:{ Exception -> 0x0209 }
        if (r4 != 0) goto L_0x0075;	 Catch:{ Exception -> 0x0209 }
    L_0x004d:
        r4 = "14";	 Catch:{ Exception -> 0x0209 }
        r4 = r2.equals(r4);	 Catch:{ Exception -> 0x0209 }
        if (r4 != 0) goto L_0x0075;	 Catch:{ Exception -> 0x0209 }
    L_0x0055:
        r4 = "6";	 Catch:{ Exception -> 0x0209 }
        r4 = r2.equals(r4);	 Catch:{ Exception -> 0x0209 }
        if (r4 != 0) goto L_0x0075;	 Catch:{ Exception -> 0x0209 }
    L_0x005d:
        r4 = "5";	 Catch:{ Exception -> 0x0209 }
        r4 = r2.equals(r4);	 Catch:{ Exception -> 0x0209 }
        if (r4 != 0) goto L_0x0075;	 Catch:{ Exception -> 0x0209 }
    L_0x0065:
        r4 = "7";	 Catch:{ Exception -> 0x0209 }
        r4 = r2.equals(r4);	 Catch:{ Exception -> 0x0209 }
        if (r4 != 0) goto L_0x0075;	 Catch:{ Exception -> 0x0209 }
    L_0x006d:
        r4 = "11";	 Catch:{ Exception -> 0x0209 }
        r4 = r2.equals(r4);	 Catch:{ Exception -> 0x0209 }
        if (r4 == 0) goto L_0x0025;	 Catch:{ Exception -> 0x0209 }
    L_0x0075:
        r4 = r15.getEpisode();	 Catch:{ Exception -> 0x0209 }
        if (r4 <= 0) goto L_0x0090;	 Catch:{ Exception -> 0x0209 }
    L_0x007b:
        r4 = r15.getSeason();	 Catch:{ Exception -> 0x0209 }
        if (r4 <= 0) goto L_0x0090;	 Catch:{ Exception -> 0x0209 }
    L_0x0081:
        r1 = r1.text();	 Catch:{ Exception -> 0x0209 }
        r4 = r15.getEpisode();	 Catch:{ Exception -> 0x0209 }
        r1 = r7.matchesEpisode(r1, r4);	 Catch:{ Exception -> 0x0209 }
        if (r1 != 0) goto L_0x0090;	 Catch:{ Exception -> 0x0209 }
    L_0x008f:
        goto L_0x0025;	 Catch:{ Exception -> 0x0209 }
    L_0x0090:
        r1 = r7.loadedResources;	 Catch:{ Exception -> 0x0209 }
        r1.clear();	 Catch:{ Exception -> 0x0209 }
        r1 = r15.getPageUrl();	 Catch:{ Exception -> 0x0209 }
        r4 = "watching.html";	 Catch:{ Exception -> 0x0209 }
        r5 = "";	 Catch:{ Exception -> 0x0209 }
        r1 = r1.replace(r4, r5);	 Catch:{ Exception -> 0x0209 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0209 }
        r4.<init>();	 Catch:{ Exception -> 0x0209 }
        r4.append(r1);	 Catch:{ Exception -> 0x0209 }
        r4.append(r3);	 Catch:{ Exception -> 0x0209 }
        r1 = "-";	 Catch:{ Exception -> 0x0209 }
        r4.append(r1);	 Catch:{ Exception -> 0x0209 }
        r4.append(r2);	 Catch:{ Exception -> 0x0209 }
        r1 = "/watching.html";	 Catch:{ Exception -> 0x0209 }
        r4.append(r1);	 Catch:{ Exception -> 0x0209 }
        r1 = r4.toString();	 Catch:{ Exception -> 0x0209 }
        r7.wvgethtml(r1);	 Catch:{ Exception -> 0x0209 }
        r1 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;	 Catch:{ Exception -> 0x0209 }
        java.lang.Thread.sleep(r1);	 Catch:{ Exception -> 0x0209 }
        r1 = "(.+ajax/movie_sources.+)";	 Catch:{ Exception -> 0x0209 }
        r1 = r7.findLoadedResource(r1);	 Catch:{ Exception -> 0x0209 }
        r2 = "(.+ajax/movie_embed.+)";	 Catch:{ Exception -> 0x0209 }
        r2 = r7.findLoadedResource(r2);	 Catch:{ Exception -> 0x0209 }
        r3 = r2.isEmpty();	 Catch:{ Exception -> 0x0209 }
        if (r3 != 0) goto L_0x00d8;	 Catch:{ Exception -> 0x0209 }
    L_0x00d7:
        r1 = r2;	 Catch:{ Exception -> 0x0209 }
    L_0x00d8:
        if (r1 == 0) goto L_0x0025;	 Catch:{ Exception -> 0x0209 }
    L_0x00da:
        r2 = r1.isEmpty();	 Catch:{ Exception -> 0x0209 }
        if (r2 != 0) goto L_0x0025;	 Catch:{ Exception -> 0x0209 }
    L_0x00e0:
        r2 = org.jsoup.Jsoup.connect(r1);	 Catch:{ Exception -> 0x0209 }
        r10 = 0;	 Catch:{ Exception -> 0x0209 }
        r2 = r2.validateTLSCertificates(r10);	 Catch:{ Exception -> 0x0209 }
        r3 = 1;	 Catch:{ Exception -> 0x0209 }
        r2 = r2.ignoreHttpErrors(r3);	 Catch:{ Exception -> 0x0209 }
        r2 = r2.ignoreContentType(r3);	 Catch:{ Exception -> 0x0209 }
        r3 = UserAgent;	 Catch:{ Exception -> 0x0209 }
        r2 = r2.userAgent(r3);	 Catch:{ Exception -> 0x0209 }
        r3 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;	 Catch:{ Exception -> 0x0209 }
        r2 = r2.timeout(r3);	 Catch:{ Exception -> 0x0209 }
        r2 = r2.get();	 Catch:{ Exception -> 0x0209 }
        r2 = r2.body();	 Catch:{ Exception -> 0x0209 }
        r2 = r2.html();	 Catch:{ Exception -> 0x0209 }
        r3 = "\"";	 Catch:{ Exception -> 0x0209 }
        r3 = r2.startsWith(r3);	 Catch:{ Exception -> 0x0209 }
        if (r3 == 0) goto L_0x011a;	 Catch:{ Exception -> 0x0209 }
    L_0x0112:
        r3 = "^\"|\"$";	 Catch:{ Exception -> 0x0209 }
        r4 = "";	 Catch:{ Exception -> 0x0209 }
        r2 = r2.replaceAll(r3, r4);	 Catch:{ Exception -> 0x0209 }
    L_0x011a:
        if (r2 == 0) goto L_0x0025;	 Catch:{ Exception -> 0x0209 }
    L_0x011c:
        r3 = r2.isEmpty();	 Catch:{ Exception -> 0x0209 }
        if (r3 != 0) goto L_0x0025;	 Catch:{ Exception -> 0x0209 }
    L_0x0122:
        r2 = org.apache.commons.lang3.StringEscapeUtils.unescapeEcmaScript(r2);	 Catch:{ Exception -> 0x0209 }
        r2 = org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4(r2);	 Catch:{ Exception -> 0x0209 }
        r3 = r7.PROVIDER_NAME;	 Catch:{ Exception -> 0x0209 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0209 }
        r4.<init>();	 Catch:{ Exception -> 0x0209 }
        r5 = "Got playlist: ";	 Catch:{ Exception -> 0x0209 }
        r4.append(r5);	 Catch:{ Exception -> 0x0209 }
        r4.append(r2);	 Catch:{ Exception -> 0x0209 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0209 }
        android.util.Log.d(r3, r4);	 Catch:{ Exception -> 0x0209 }
        r11 = new org.json.JSONObject;	 Catch:{ Exception -> 0x0209 }
        r11.<init>(r2);	 Catch:{ Exception -> 0x0209 }
        r2 = "movie_embed";	 Catch:{ Exception -> 0x0209 }
        r1 = r1.contains(r2);	 Catch:{ Exception -> 0x0209 }
        if (r1 == 0) goto L_0x0168;	 Catch:{ Exception -> 0x0209 }
    L_0x014d:
        r1 = "src";	 Catch:{ Exception -> 0x0209 }
        r2 = r11.getString(r1);	 Catch:{ Exception -> 0x0209 }
        r3 = r15.getPageUrl();	 Catch:{ Exception -> 0x0209 }
        r4 = 0;	 Catch:{ Exception -> 0x0209 }
        r6 = r15.getTitle();	 Catch:{ Exception -> 0x0209 }
        r1 = r7;	 Catch:{ Exception -> 0x0209 }
        r5 = r8;	 Catch:{ Exception -> 0x0209 }
        r1.processLink(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x0209 }
        r1 = r7.hasMaxSources(r8);	 Catch:{ Exception -> 0x0209 }
        if (r1 == 0) goto L_0x0025;
    L_0x0167:
        return;
    L_0x0168:
        r1 = "playlist";	 Catch:{ Exception -> 0x01bc }
        r1 = r11.getJSONArray(r1);	 Catch:{ Exception -> 0x01bc }
        r1 = r1.getJSONObject(r10);	 Catch:{ Exception -> 0x01bc }
        r2 = "sources";	 Catch:{ Exception -> 0x01bc }
        r12 = r1.getJSONArray(r2);	 Catch:{ Exception -> 0x01bc }
        r13 = 0;	 Catch:{ Exception -> 0x01bc }
    L_0x0179:
        r1 = r12.length();	 Catch:{ Exception -> 0x01bc }
        if (r13 >= r1) goto L_0x0025;	 Catch:{ Exception -> 0x01bc }
    L_0x017f:
        r1 = r12.getJSONObject(r13);	 Catch:{ Exception -> 0x01bc }
        r2 = "file";	 Catch:{ Exception -> 0x01bc }
        r2 = r1.optString(r2);	 Catch:{ Exception -> 0x01bc }
        r3 = "\"";	 Catch:{ Exception -> 0x01bc }
        r4 = "";	 Catch:{ Exception -> 0x01bc }
        r2 = r2.replace(r3, r4);	 Catch:{ Exception -> 0x01bc }
        r3 = "label";	 Catch:{ Exception -> 0x01bc }
        r1 = r1.optString(r3);	 Catch:{ Exception -> 0x01bc }
        r3 = "\"";	 Catch:{ Exception -> 0x01bc }
        r4 = "";	 Catch:{ Exception -> 0x01bc }
        r4 = r1.replace(r3, r4);	 Catch:{ Exception -> 0x01bc }
        r1 = r2.isEmpty();	 Catch:{ Exception -> 0x01bc }
        if (r1 != 0) goto L_0x01b9;	 Catch:{ Exception -> 0x01bc }
    L_0x01a5:
        r3 = r15.getPageUrl();	 Catch:{ Exception -> 0x01bc }
        r6 = r15.getTitle();	 Catch:{ Exception -> 0x01bc }
        r1 = r7;	 Catch:{ Exception -> 0x01bc }
        r5 = r8;	 Catch:{ Exception -> 0x01bc }
        r1.processLink(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x01bc }
        r1 = r7.hasMaxSources(r8);	 Catch:{ Exception -> 0x01bc }
        if (r1 == 0) goto L_0x01b9;
    L_0x01b8:
        return;
    L_0x01b9:
        r13 = r13 + 1;
        goto L_0x0179;
    L_0x01bc:
        r1 = "playlist";	 Catch:{ Exception -> 0x0202 }
        r1 = r11.getJSONArray(r1);	 Catch:{ Exception -> 0x0202 }
        r1 = r1.getJSONObject(r10);	 Catch:{ Exception -> 0x0202 }
        r2 = "sources";	 Catch:{ Exception -> 0x0202 }
        r1 = r1.getJSONObject(r2);	 Catch:{ Exception -> 0x0202 }
        r2 = "file";	 Catch:{ Exception -> 0x0202 }
        r2 = r1.optString(r2);	 Catch:{ Exception -> 0x0202 }
        r3 = "\"";	 Catch:{ Exception -> 0x0202 }
        r4 = "";	 Catch:{ Exception -> 0x0202 }
        r2 = r2.replace(r3, r4);	 Catch:{ Exception -> 0x0202 }
        r3 = "label";	 Catch:{ Exception -> 0x0202 }
        r1 = r1.optString(r3);	 Catch:{ Exception -> 0x0202 }
        r3 = "\"";	 Catch:{ Exception -> 0x0202 }
        r4 = "";	 Catch:{ Exception -> 0x0202 }
        r4 = r1.replace(r3, r4);	 Catch:{ Exception -> 0x0202 }
        r1 = r2.isEmpty();	 Catch:{ Exception -> 0x0202 }
        if (r1 != 0) goto L_0x0025;	 Catch:{ Exception -> 0x0202 }
    L_0x01ee:
        r3 = r15.getPageUrl();	 Catch:{ Exception -> 0x0202 }
        r6 = r15.getTitle();	 Catch:{ Exception -> 0x0202 }
        r1 = r7;	 Catch:{ Exception -> 0x0202 }
        r5 = r8;	 Catch:{ Exception -> 0x0202 }
        r1.processLink(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x0202 }
        r1 = r7.hasMaxSources(r8);	 Catch:{ Exception -> 0x0202 }
        if (r1 == 0) goto L_0x0025;
    L_0x0201:
        return;
    L_0x0202:
        r0 = move-exception;
        r1 = r0;
        r1.printStackTrace();	 Catch:{ Exception -> 0x0209 }
        goto L_0x0025;
    L_0x0209:
        r0 = move-exception;
        r1 = r0;
        r1.printStackTrace();	 Catch:{ Exception -> 0x000b }
        goto L_0x0025;
    L_0x0210:
        r1.printStackTrace();
    L_0x0213:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.github.morpheustv.scrapers.providers.YesMoviesProvider.getSources(io.github.morpheustv.scrapers.model.ProviderSearchResult, java.util.concurrent.CopyOnWriteArrayList):void");
    }
}
