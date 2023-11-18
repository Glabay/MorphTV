package io.github.morpheustv.scrapers.providers;

import android.content.Context;
import com.android.morpheustv.sources.ProviderSearchResult;
import io.github.morpheustv.scrapers.controller.WebviewController;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Netflix123Provider extends WebviewController {
    String base_link;
    String[] domains;

    public Netflix123Provider(Context context) {
        super(context, "123NETFLIX");
        this.domains = new String[]{"123netflix.com"};
        this.base_link = "http://123netflix.unblockall.org";
        this.GROUP = 17;
        this.resourceWhitelist = new String[]{"(.*base64.*)", "(.*jquery.*.js.*)", "(.*piguiqproxy.*)", "(.*amgload.*)", "(.*smcheck.*)"};
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            for (String str3 : list) {
                try {
                    String attr;
                    StringBuilder stringBuilder = new StringBuilder();
                    URL url = new URL(this.base_link);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("/search-movies/");
                    stringBuilder2.append(URLEncoder.encode(str3, "UTF-8"));
                    stringBuilder.append(new URL(url, stringBuilder2.toString()).toString());
                    stringBuilder.append(".html");
                    Iterator it = Jsoup.parse(wvgethtml(stringBuilder.toString())).select("div.ml-item").iterator();
                    while (it.hasNext()) {
                        Element element = (Element) it.next();
                        Document parse = Jsoup.parse(((Element) element.select("a").get(0)).attr("onmouseover").replace("Tip('", "").replace("')", ""));
                        String text = ((Element) parse.select("i").get(0)).text();
                        if (!cleantitle(text).equals(cleantitle(str3))) {
                            text = cleantitle(text);
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append(str3);
                            stringBuilder3.append(str);
                            if (!text.equals(cleantitle(stringBuilder3.toString()))) {
                                continue;
                            }
                        }
                        Matcher matcher = Pattern.compile("<b>\\s*Release:\\s*(\\d{4})").matcher(parse.html());
                        if (matcher.find() && matcher.groupCount() > 0 && matcher.group(1).equals(str)) {
                            attr = ((Element) element.select("a").get(0)).attr("href");
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
                        Iterator it = Jsoup.parse(wvgethtml(stringBuilder.toString())).select("div.ml-item").iterator();
                        while (it.hasNext()) {
                            Element element = (Element) it.next();
                            String text = ((Element) Jsoup.parse(((Element) element.select("a").get(0)).attr("onmouseover").replace("Tip('", "").replace("')", "")).select("i").get(0)).text();
                            if (cleantitle(text).startsWith(cleantitle(str2))) {
                                text = cleantitle(text);
                                stringBuilder3 = new StringBuilder();
                                stringBuilder3.append("Season ");
                                stringBuilder3.append(String.valueOf(i));
                                if (text.contains(cleantitle(stringBuilder3.toString()))) {
                                    attr = ((Element) element.select("a").get(0)).attr("href");
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

    public void getSources(com.android.morpheustv.sources.ProviderSearchResult r14, java.util.concurrent.CopyOnWriteArrayList<com.android.morpheustv.sources.Source> r15) {
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
        if (r14 != 0) goto L_0x000b;
    L_0x0002:
        r14 = new java.lang.Exception;	 Catch:{ Exception -> 0x0008 }
        r14.<init>();	 Catch:{ Exception -> 0x0008 }
        throw r14;	 Catch:{ Exception -> 0x0008 }
    L_0x0008:
        r14 = move-exception;	 Catch:{ Exception -> 0x0008 }
        goto L_0x01d3;	 Catch:{ Exception -> 0x0008 }
    L_0x000b:
        r0 = r14.getPageUrl();	 Catch:{ Exception -> 0x0008 }
        r0 = r13.wvgethtml(r0);	 Catch:{ Exception -> 0x0008 }
        r0 = org.jsoup.Jsoup.parse(r0);	 Catch:{ Exception -> 0x0008 }
        r1 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0008 }
        r1.<init>();	 Catch:{ Exception -> 0x0008 }
        r2 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0008 }
        r2.<init>();	 Catch:{ Exception -> 0x0008 }
        r3 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0008 }
        r3.<init>();	 Catch:{ Exception -> 0x0008 }
        r4 = r14.getSeason();	 Catch:{ Exception -> 0x0008 }
        if (r4 <= 0) goto L_0x0068;	 Catch:{ Exception -> 0x0008 }
    L_0x002c:
        r4 = r14.getEpisode();	 Catch:{ Exception -> 0x0008 }
        if (r4 <= 0) goto L_0x0068;	 Catch:{ Exception -> 0x0008 }
    L_0x0032:
        r4 = "a.episode_series_link";	 Catch:{ Exception -> 0x0008 }
        r4 = r0.select(r4);	 Catch:{ Exception -> 0x0008 }
        r4 = r4.iterator();	 Catch:{ Exception -> 0x0008 }
    L_0x003c:
        r5 = r4.hasNext();	 Catch:{ Exception -> 0x0008 }
        if (r5 == 0) goto L_0x0068;	 Catch:{ Exception -> 0x0008 }
    L_0x0042:
        r5 = r4.next();	 Catch:{ Exception -> 0x0008 }
        r5 = (org.jsoup.nodes.Element) r5;	 Catch:{ Exception -> 0x0008 }
        r6 = r5.text();	 Catch:{ Exception -> 0x0008 }
        r7 = "href";	 Catch:{ Exception -> 0x0008 }
        r5 = r5.attr(r7);	 Catch:{ Exception -> 0x0008 }
        r7 = r14.getEpisode();	 Catch:{ Exception -> 0x0008 }
        r7 = java.lang.String.valueOf(r7);	 Catch:{ Exception -> 0x0008 }
        r6 = r6.equals(r7);	 Catch:{ Exception -> 0x0008 }
        if (r6 == 0) goto L_0x003c;	 Catch:{ Exception -> 0x0008 }
    L_0x0060:
        r0 = r13.wvgethtml(r5);	 Catch:{ Exception -> 0x0008 }
        r0 = org.jsoup.Jsoup.parse(r0);	 Catch:{ Exception -> 0x0008 }
    L_0x0068:
        r4 = "div#media-player";	 Catch:{ Exception -> 0x0008 }
        r4 = r0.select(r4);	 Catch:{ Exception -> 0x0008 }
        r5 = "iframe";	 Catch:{ Exception -> 0x0008 }
        r4 = r4.select(r5);	 Catch:{ Exception -> 0x0008 }
        r4 = r4.first();	 Catch:{ Exception -> 0x0008 }
        if (r4 == 0) goto L_0x0096;	 Catch:{ Exception -> 0x0008 }
    L_0x007a:
        r5 = "src";	 Catch:{ Exception -> 0x0008 }
        r7 = r4.attr(r5);	 Catch:{ Exception -> 0x0008 }
        r8 = r14.getPageUrl();	 Catch:{ Exception -> 0x0008 }
        r9 = 0;	 Catch:{ Exception -> 0x0008 }
        r11 = r14.getTitle();	 Catch:{ Exception -> 0x0008 }
        r6 = r13;	 Catch:{ Exception -> 0x0008 }
        r10 = r15;	 Catch:{ Exception -> 0x0008 }
        r4 = r6.processLink(r7, r8, r9, r10, r11);	 Catch:{ Exception -> 0x0008 }
        if (r4 == 0) goto L_0x0096;	 Catch:{ Exception -> 0x0008 }
    L_0x0091:
        r4 = com.android.morpheustv.settings.Settings.FAST_SCRAPING;	 Catch:{ Exception -> 0x0008 }
        if (r4 == 0) goto L_0x0096;	 Catch:{ Exception -> 0x0008 }
    L_0x0095:
        return;	 Catch:{ Exception -> 0x0008 }
    L_0x0096:
        r4 = "div.server_line";	 Catch:{ Exception -> 0x0008 }
        r0 = r0.select(r4);	 Catch:{ Exception -> 0x0008 }
        r0 = r0.iterator();	 Catch:{ Exception -> 0x0008 }
    L_0x00a0:
        r4 = r0.hasNext();	 Catch:{ Exception -> 0x0008 }
        if (r4 == 0) goto L_0x00f3;	 Catch:{ Exception -> 0x0008 }
    L_0x00a6:
        r4 = r0.next();	 Catch:{ Exception -> 0x0008 }
        r4 = (org.jsoup.nodes.Element) r4;	 Catch:{ Exception -> 0x0008 }
        r5 = "p.server_servername";	 Catch:{ Exception -> 0x00ee }
        r5 = r4.select(r5);	 Catch:{ Exception -> 0x00ee }
        r5 = r5.text();	 Catch:{ Exception -> 0x00ee }
        r5 = r5.toLowerCase();	 Catch:{ Exception -> 0x00ee }
        r6 = "p.server_play";	 Catch:{ Exception -> 0x00ee }
        r4 = r4.select(r6);	 Catch:{ Exception -> 0x00ee }
        r6 = "a";	 Catch:{ Exception -> 0x00ee }
        r4 = r4.select(r6);	 Catch:{ Exception -> 0x00ee }
        r6 = "href";	 Catch:{ Exception -> 0x00ee }
        r4 = r4.attr(r6);	 Catch:{ Exception -> 0x00ee }
        r6 = "openload";	 Catch:{ Exception -> 0x00ee }
        r6 = r5.contains(r6);	 Catch:{ Exception -> 0x00ee }
        if (r6 == 0) goto L_0x00d7;	 Catch:{ Exception -> 0x00ee }
    L_0x00d4:
        r2.add(r4);	 Catch:{ Exception -> 0x00ee }
    L_0x00d7:
        r6 = "thevideo";	 Catch:{ Exception -> 0x00ee }
        r6 = r5.contains(r6);	 Catch:{ Exception -> 0x00ee }
        if (r6 == 0) goto L_0x00e2;	 Catch:{ Exception -> 0x00ee }
    L_0x00df:
        r1.add(r4);	 Catch:{ Exception -> 0x00ee }
    L_0x00e2:
        r6 = "streamango";	 Catch:{ Exception -> 0x00ee }
        r5 = r5.contains(r6);	 Catch:{ Exception -> 0x00ee }
        if (r5 == 0) goto L_0x00a0;	 Catch:{ Exception -> 0x00ee }
    L_0x00ea:
        r3.add(r4);	 Catch:{ Exception -> 0x00ee }
        goto L_0x00a0;
    L_0x00ee:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ Exception -> 0x0008 }
        goto L_0x00a0;	 Catch:{ Exception -> 0x0008 }
    L_0x00f3:
        r0 = r1.iterator();	 Catch:{ Exception -> 0x0008 }
        r1 = 0;	 Catch:{ Exception -> 0x0008 }
        r4 = 0;	 Catch:{ Exception -> 0x0008 }
    L_0x00f9:
        r5 = r0.hasNext();	 Catch:{ Exception -> 0x0008 }
        r6 = 3;	 Catch:{ Exception -> 0x0008 }
        if (r5 == 0) goto L_0x013f;	 Catch:{ Exception -> 0x0008 }
    L_0x0100:
        r5 = r0.next();	 Catch:{ Exception -> 0x0008 }
        r9 = r5;	 Catch:{ Exception -> 0x0008 }
        r9 = (java.lang.String) r9;	 Catch:{ Exception -> 0x0008 }
        r5 = r13.wvgethtml(r9);	 Catch:{ Exception -> 0x00f9 }
        r5 = org.jsoup.Jsoup.parse(r5);	 Catch:{ Exception -> 0x00f9 }
        r7 = "div#media-player";	 Catch:{ Exception -> 0x00f9 }
        r5 = r5.select(r7);	 Catch:{ Exception -> 0x00f9 }
        r7 = "iframe";	 Catch:{ Exception -> 0x00f9 }
        r5 = r5.select(r7);	 Catch:{ Exception -> 0x00f9 }
        r7 = "src";	 Catch:{ Exception -> 0x00f9 }
        r8 = r5.attr(r7);	 Catch:{ Exception -> 0x00f9 }
        r5 = "thevideo";	 Catch:{ Exception -> 0x00f9 }
        r5 = r8.contains(r5);	 Catch:{ Exception -> 0x00f9 }
        if (r5 == 0) goto L_0x013d;	 Catch:{ Exception -> 0x00f9 }
    L_0x0129:
        r10 = 0;	 Catch:{ Exception -> 0x00f9 }
        r12 = r14.getTitle();	 Catch:{ Exception -> 0x00f9 }
        r7 = r13;	 Catch:{ Exception -> 0x00f9 }
        r11 = r15;	 Catch:{ Exception -> 0x00f9 }
        r5 = r7.processLink(r8, r9, r10, r11, r12);	 Catch:{ Exception -> 0x00f9 }
        if (r5 == 0) goto L_0x013d;	 Catch:{ Exception -> 0x00f9 }
    L_0x0136:
        r5 = com.android.morpheustv.settings.Settings.FAST_SCRAPING;	 Catch:{ Exception -> 0x00f9 }
        if (r5 == 0) goto L_0x013b;
    L_0x013a:
        return;
    L_0x013b:
        r4 = r4 + 1;
    L_0x013d:
        if (r4 < r6) goto L_0x00f9;
    L_0x013f:
        r0 = r2.iterator();	 Catch:{ Exception -> 0x0008 }
        r2 = 0;	 Catch:{ Exception -> 0x0008 }
    L_0x0144:
        r4 = r0.hasNext();	 Catch:{ Exception -> 0x0008 }
        if (r4 == 0) goto L_0x0189;	 Catch:{ Exception -> 0x0008 }
    L_0x014a:
        r4 = r0.next();	 Catch:{ Exception -> 0x0008 }
        r9 = r4;	 Catch:{ Exception -> 0x0008 }
        r9 = (java.lang.String) r9;	 Catch:{ Exception -> 0x0008 }
        r4 = r13.wvgethtml(r9);	 Catch:{ Exception -> 0x0144 }
        r4 = org.jsoup.Jsoup.parse(r4);	 Catch:{ Exception -> 0x0144 }
        r5 = "div#media-player";	 Catch:{ Exception -> 0x0144 }
        r4 = r4.select(r5);	 Catch:{ Exception -> 0x0144 }
        r5 = "iframe";	 Catch:{ Exception -> 0x0144 }
        r4 = r4.select(r5);	 Catch:{ Exception -> 0x0144 }
        r5 = "src";	 Catch:{ Exception -> 0x0144 }
        r8 = r4.attr(r5);	 Catch:{ Exception -> 0x0144 }
        r4 = "openload.co";	 Catch:{ Exception -> 0x0144 }
        r4 = r8.contains(r4);	 Catch:{ Exception -> 0x0144 }
        if (r4 == 0) goto L_0x0187;	 Catch:{ Exception -> 0x0144 }
    L_0x0173:
        r10 = 0;	 Catch:{ Exception -> 0x0144 }
        r12 = r14.getTitle();	 Catch:{ Exception -> 0x0144 }
        r7 = r13;	 Catch:{ Exception -> 0x0144 }
        r11 = r15;	 Catch:{ Exception -> 0x0144 }
        r4 = r7.processLink(r8, r9, r10, r11, r12);	 Catch:{ Exception -> 0x0144 }
        if (r4 == 0) goto L_0x0187;	 Catch:{ Exception -> 0x0144 }
    L_0x0180:
        r4 = com.android.morpheustv.settings.Settings.FAST_SCRAPING;	 Catch:{ Exception -> 0x0144 }
        if (r4 == 0) goto L_0x0185;
    L_0x0184:
        return;
    L_0x0185:
        r2 = r2 + 1;
    L_0x0187:
        if (r2 < r6) goto L_0x0144;
    L_0x0189:
        r0 = r3.iterator();	 Catch:{ Exception -> 0x0008 }
    L_0x018d:
        r2 = r0.hasNext();	 Catch:{ Exception -> 0x0008 }
        if (r2 == 0) goto L_0x01d6;	 Catch:{ Exception -> 0x0008 }
    L_0x0193:
        r2 = r0.next();	 Catch:{ Exception -> 0x0008 }
        r9 = r2;	 Catch:{ Exception -> 0x0008 }
        r9 = (java.lang.String) r9;	 Catch:{ Exception -> 0x0008 }
        r2 = r13.wvgethtml(r9);	 Catch:{ Exception -> 0x018d }
        r2 = org.jsoup.Jsoup.parse(r2);	 Catch:{ Exception -> 0x018d }
        r3 = "div#media-player";	 Catch:{ Exception -> 0x018d }
        r2 = r2.select(r3);	 Catch:{ Exception -> 0x018d }
        r3 = "iframe";	 Catch:{ Exception -> 0x018d }
        r2 = r2.select(r3);	 Catch:{ Exception -> 0x018d }
        r3 = "src";	 Catch:{ Exception -> 0x018d }
        r8 = r2.attr(r3);	 Catch:{ Exception -> 0x018d }
        r2 = "streamango.com";	 Catch:{ Exception -> 0x018d }
        r2 = r8.contains(r2);	 Catch:{ Exception -> 0x018d }
        if (r2 == 0) goto L_0x01d0;	 Catch:{ Exception -> 0x018d }
    L_0x01bc:
        r10 = 0;	 Catch:{ Exception -> 0x018d }
        r12 = r14.getTitle();	 Catch:{ Exception -> 0x018d }
        r7 = r13;	 Catch:{ Exception -> 0x018d }
        r11 = r15;	 Catch:{ Exception -> 0x018d }
        r2 = r7.processLink(r8, r9, r10, r11, r12);	 Catch:{ Exception -> 0x018d }
        if (r2 == 0) goto L_0x01d0;	 Catch:{ Exception -> 0x018d }
    L_0x01c9:
        r2 = com.android.morpheustv.settings.Settings.FAST_SCRAPING;	 Catch:{ Exception -> 0x018d }
        if (r2 == 0) goto L_0x01ce;
    L_0x01cd:
        return;
    L_0x01ce:
        r1 = r1 + 1;
    L_0x01d0:
        if (r1 < r6) goto L_0x018d;
    L_0x01d2:
        goto L_0x01d6;
    L_0x01d3:
        r14.printStackTrace();
    L_0x01d6:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.github.morpheustv.scrapers.providers.Netflix123Provider.getSources(com.android.morpheustv.sources.ProviderSearchResult, java.util.concurrent.CopyOnWriteArrayList):void");
    }
}
