package io.github.morpheustv.scrapers.providers;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Hulu123Provider extends BaseProvider {
    String base_link;
    String[] domains;

    public Hulu123Provider(Scraper scraper) {
        super(scraper, "123HULU.COM", true);
        this.domains = new String[]{"123hulu.com"};
        this.base_link = "http://www0.123hulu.com";
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
                        if (!BaseProvider.cleantitle(text).equals(BaseProvider.cleantitle(str3))) {
                            text = BaseProvider.cleantitle(text);
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append(str3);
                            stringBuilder3.append(str);
                            if (!text.equals(BaseProvider.cleantitle(stringBuilder3.toString()))) {
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
                            if (BaseProvider.cleantitle(text).startsWith(BaseProvider.cleantitle(str2))) {
                                text = BaseProvider.cleantitle(text);
                                stringBuilder3 = new StringBuilder();
                                stringBuilder3.append("Season ");
                                stringBuilder3.append(String.valueOf(i));
                                if (text.contains(BaseProvider.cleantitle(stringBuilder3.toString()))) {
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

    public void getSources(io.github.morpheustv.scrapers.model.ProviderSearchResult r10, java.util.concurrent.CopyOnWriteArrayList<io.github.morpheustv.scrapers.model.Source> r11) {
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
        r9 = this;
        if (r10 != 0) goto L_0x000b;
    L_0x0002:
        r10 = new java.lang.Exception;	 Catch:{ Exception -> 0x0008 }
        r10.<init>();	 Catch:{ Exception -> 0x0008 }
        throw r10;	 Catch:{ Exception -> 0x0008 }
    L_0x0008:
        r10 = move-exception;	 Catch:{ Exception -> 0x0008 }
        goto L_0x0116;	 Catch:{ Exception -> 0x0008 }
    L_0x000b:
        r0 = r10.getPageUrl();	 Catch:{ Exception -> 0x0008 }
        r0 = r9.wvgethtml(r0);	 Catch:{ Exception -> 0x0008 }
        r0 = org.jsoup.Jsoup.parse(r0);	 Catch:{ Exception -> 0x0008 }
        r1 = r10.getSeason();	 Catch:{ Exception -> 0x0008 }
        if (r1 <= 0) goto L_0x0059;	 Catch:{ Exception -> 0x0008 }
    L_0x001d:
        r1 = r10.getEpisode();	 Catch:{ Exception -> 0x0008 }
        if (r1 <= 0) goto L_0x0059;	 Catch:{ Exception -> 0x0008 }
    L_0x0023:
        r1 = "a.episode_series_link";	 Catch:{ Exception -> 0x0008 }
        r1 = r0.select(r1);	 Catch:{ Exception -> 0x0008 }
        r1 = r1.iterator();	 Catch:{ Exception -> 0x0008 }
    L_0x002d:
        r2 = r1.hasNext();	 Catch:{ Exception -> 0x0008 }
        if (r2 == 0) goto L_0x0059;	 Catch:{ Exception -> 0x0008 }
    L_0x0033:
        r2 = r1.next();	 Catch:{ Exception -> 0x0008 }
        r2 = (org.jsoup.nodes.Element) r2;	 Catch:{ Exception -> 0x0008 }
        r3 = r2.text();	 Catch:{ Exception -> 0x0008 }
        r4 = "href";	 Catch:{ Exception -> 0x0008 }
        r2 = r2.attr(r4);	 Catch:{ Exception -> 0x0008 }
        r4 = r10.getEpisode();	 Catch:{ Exception -> 0x0008 }
        r4 = java.lang.String.valueOf(r4);	 Catch:{ Exception -> 0x0008 }
        r3 = r3.equals(r4);	 Catch:{ Exception -> 0x0008 }
        if (r3 == 0) goto L_0x002d;	 Catch:{ Exception -> 0x0008 }
    L_0x0051:
        r0 = r9.wvgethtml(r2);	 Catch:{ Exception -> 0x0008 }
        r0 = org.jsoup.Jsoup.parse(r0);	 Catch:{ Exception -> 0x0008 }
    L_0x0059:
        r1 = "div#media-player";	 Catch:{ Exception -> 0x0008 }
        r1 = r0.select(r1);	 Catch:{ Exception -> 0x0008 }
        r2 = "iframe";	 Catch:{ Exception -> 0x0008 }
        r1 = r1.select(r2);	 Catch:{ Exception -> 0x0008 }
        r1 = r1.first();	 Catch:{ Exception -> 0x0008 }
        if (r1 == 0) goto L_0x0086;	 Catch:{ Exception -> 0x0008 }
    L_0x006b:
        r2 = "src";	 Catch:{ Exception -> 0x0008 }
        r4 = r1.attr(r2);	 Catch:{ Exception -> 0x0008 }
        r5 = r10.getPageUrl();	 Catch:{ Exception -> 0x0008 }
        r6 = 0;	 Catch:{ Exception -> 0x0008 }
        r8 = r10.getTitle();	 Catch:{ Exception -> 0x0008 }
        r3 = r9;	 Catch:{ Exception -> 0x0008 }
        r7 = r11;	 Catch:{ Exception -> 0x0008 }
        r3.processLink(r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x0008 }
        r1 = r9.hasMaxSources(r11);	 Catch:{ Exception -> 0x0008 }
        if (r1 == 0) goto L_0x0086;	 Catch:{ Exception -> 0x0008 }
    L_0x0085:
        return;	 Catch:{ Exception -> 0x0008 }
    L_0x0086:
        r1 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0008 }
        r1.<init>();	 Catch:{ Exception -> 0x0008 }
        r2 = "div.server_line";	 Catch:{ Exception -> 0x0008 }
        r0 = r0.select(r2);	 Catch:{ Exception -> 0x0008 }
        r0 = r0.iterator();	 Catch:{ Exception -> 0x0008 }
    L_0x0095:
        r2 = r0.hasNext();	 Catch:{ Exception -> 0x0008 }
        if (r2 == 0) goto L_0x00d2;	 Catch:{ Exception -> 0x0008 }
    L_0x009b:
        r2 = r0.next();	 Catch:{ Exception -> 0x0008 }
        r2 = (org.jsoup.nodes.Element) r2;	 Catch:{ Exception -> 0x0008 }
        r3 = "p.server_servername";	 Catch:{ Exception -> 0x00cd }
        r3 = r2.select(r3);	 Catch:{ Exception -> 0x00cd }
        r3 = r3.text();	 Catch:{ Exception -> 0x00cd }
        r3 = r3.toLowerCase();	 Catch:{ Exception -> 0x00cd }
        r4 = "p.server_play";	 Catch:{ Exception -> 0x00cd }
        r2 = r2.select(r4);	 Catch:{ Exception -> 0x00cd }
        r4 = "a";	 Catch:{ Exception -> 0x00cd }
        r2 = r2.select(r4);	 Catch:{ Exception -> 0x00cd }
        r4 = "href";	 Catch:{ Exception -> 0x00cd }
        r2 = r2.attr(r4);	 Catch:{ Exception -> 0x00cd }
        r4 = "openload";	 Catch:{ Exception -> 0x00cd }
        r3 = r3.contains(r4);	 Catch:{ Exception -> 0x00cd }
        if (r3 == 0) goto L_0x0095;	 Catch:{ Exception -> 0x00cd }
    L_0x00c9:
        r1.add(r2);	 Catch:{ Exception -> 0x00cd }
        goto L_0x0095;
    L_0x00cd:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ Exception -> 0x0008 }
        goto L_0x0095;	 Catch:{ Exception -> 0x0008 }
    L_0x00d2:
        r0 = r1.iterator();	 Catch:{ Exception -> 0x0008 }
    L_0x00d6:
        r1 = r0.hasNext();	 Catch:{ Exception -> 0x0008 }
        if (r1 == 0) goto L_0x0119;	 Catch:{ Exception -> 0x0008 }
    L_0x00dc:
        r1 = r0.next();	 Catch:{ Exception -> 0x0008 }
        r4 = r1;	 Catch:{ Exception -> 0x0008 }
        r4 = (java.lang.String) r4;	 Catch:{ Exception -> 0x0008 }
        r1 = r9.wvgethtml(r4);	 Catch:{ Exception -> 0x00d6 }
        r1 = org.jsoup.Jsoup.parse(r1);	 Catch:{ Exception -> 0x00d6 }
        r2 = "div#media-player";	 Catch:{ Exception -> 0x00d6 }
        r1 = r1.select(r2);	 Catch:{ Exception -> 0x00d6 }
        r2 = "iframe";	 Catch:{ Exception -> 0x00d6 }
        r1 = r1.select(r2);	 Catch:{ Exception -> 0x00d6 }
        r2 = "src";	 Catch:{ Exception -> 0x00d6 }
        r3 = r1.attr(r2);	 Catch:{ Exception -> 0x00d6 }
        r1 = "openload.co";	 Catch:{ Exception -> 0x00d6 }
        r1 = r3.contains(r1);	 Catch:{ Exception -> 0x00d6 }
        if (r1 == 0) goto L_0x00d6;	 Catch:{ Exception -> 0x00d6 }
    L_0x0105:
        r5 = 0;	 Catch:{ Exception -> 0x00d6 }
        r7 = r10.getTitle();	 Catch:{ Exception -> 0x00d6 }
        r2 = r9;	 Catch:{ Exception -> 0x00d6 }
        r6 = r11;	 Catch:{ Exception -> 0x00d6 }
        r2.processLink(r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x00d6 }
        r1 = r9.hasMaxSources(r11);	 Catch:{ Exception -> 0x00d6 }
        if (r1 == 0) goto L_0x00d6;
    L_0x0115:
        return;
    L_0x0116:
        r10.printStackTrace();
    L_0x0119:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: io.github.morpheustv.scrapers.providers.Hulu123Provider.getSources(io.github.morpheustv.scrapers.model.ProviderSearchResult, java.util.concurrent.CopyOnWriteArrayList):void");
    }
}
