package io.github.morpheustv.scrapers.providers;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.helper.Utils;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WatchSeriesMoviesProvider extends BaseProvider {
    String base_link;
    String[] domains;
    String search_link;

    public WatchSeriesMoviesProvider(Scraper scraper) {
        super(scraper, "GOWATCHSERIES.CO", true);
        this.domains = new String[]{"ww4.gowatchseries.co"};
        this.base_link = "https://ww4.gowatchseries.co";
        this.search_link = "/search.html?keyword=%s";
        this.resourceWhitelist = new String[]{"(.*chk_jschl?.*)", "(.*jquery.*)", "(.*keyword=.*)"};
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            for (String str22 : list) {
                try {
                    URL url = new URL(new URL(this.base_link), String.format(this.search_link, new Object[]{URLEncoder.encode(str22, "UTF-8")}));
                    String cleantitle = BaseProvider.cleantitle(str22);
                    String wvgethtml = wvgethtml(url.toString());
                    String wvcookie = wvcookie();
                    Document parse = Jsoup.parse(wvgethtml);
                    if (parse != null) {
                        Iterator it = ((Element) parse.select("div.movies_index").get(0)).select("li").iterator();
                        while (it.hasNext()) {
                            Element element = (Element) it.next();
                            String attr = element.select("a").attr("href");
                            String cleantitle2 = BaseProvider.cleantitle(element.select("div.name").html());
                            if (attr != null && attr.length() > 0 && cleantitle2.length() > 0 && Utils.stringSimilarity(cleantitle, cleantitle2) >= 0.8d) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(this.base_link);
                                stringBuilder.append(attr);
                                Document parse2 = Jsoup.parse(wvgethtml(stringBuilder.toString()));
                                attr = ((Element) ((Element) parse2.select("ul.three").get(0)).select("li").get(1)).ownText();
                                cleantitle2 = ((Element) parse2.select("div.bottom").get(0)).select("li").first().select("a").attr("href");
                                if (attr.equals(str)) {
                                    ProviderSearchResult providerSearchResult = new ProviderSearchResult();
                                    StringBuilder stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append(this.base_link);
                                    stringBuilder2.append(cleantitle2);
                                    providerSearchResult.setPageUrl(stringBuilder2.toString());
                                    providerSearchResult.setTitle(str22);
                                    providerSearchResult.setYear(attr);
                                    providerSearchResult.setCookie(wvcookie);
                                    return providerSearchResult;
                                }
                            }
                        }
                        continue;
                    } else {
                        continue;
                    }
                } catch (String str222) {
                    str222.printStackTrace();
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
        try {
            for (String str2 : list) {
                try {
                    String str3 = this.search_link;
                    Object[] objArr = new Object[1];
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(str2);
                    stringBuilder.append(" Season ");
                    stringBuilder.append(String.valueOf(i));
                    objArr[0] = URLEncoder.encode(stringBuilder.toString(), "UTF-8");
                    URL url = new URL(new URL(this.base_link), String.format(str3, objArr));
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str2);
                    stringBuilder2.append(" Season ");
                    stringBuilder2.append(String.valueOf(i));
                    str3 = BaseProvider.cleantitle(stringBuilder2.toString());
                    String wvgethtml = wvgethtml(url.toString());
                    String wvcookie = wvcookie();
                    Document parse = Jsoup.parse(wvgethtml);
                    if (parse != null) {
                        Iterator it = ((Element) parse.select("div.movies_index").get(0)).select("li").iterator();
                        while (it.hasNext()) {
                            Element element = (Element) it.next();
                            String attr = element.select("a").attr("href");
                            String html = element.select("div.name").html();
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("(");
                            stringBuilder3.append(str);
                            stringBuilder3.append(")");
                            html = BaseProvider.cleantitle(html.replace(stringBuilder3.toString(), ""));
                            if (attr != null && attr.length() > 0 && html.length() > 0 && Utils.stringSimilarity(str3, html) >= 0.8d) {
                                StringBuilder stringBuilder4 = new StringBuilder();
                                stringBuilder4.append(this.base_link);
                                stringBuilder4.append(attr);
                                Iterator it2 = Jsoup.parse(wvgethtml(stringBuilder4.toString())).select("div.bottom").first().select("li").iterator();
                                while (it2.hasNext()) {
                                    attr = ((Element) it2.next()).select("a").attr("href");
                                    String toLowerCase = attr.toLowerCase();
                                    StringBuilder stringBuilder5 = new StringBuilder();
                                    stringBuilder5.append("episode-");
                                    stringBuilder5.append(String.valueOf(i2));
                                    if (toLowerCase.endsWith(stringBuilder5.toString().toLowerCase())) {
                                        ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                                        StringBuilder stringBuilder6 = new StringBuilder();
                                        stringBuilder6.append(this.base_link);
                                        stringBuilder6.append(attr);
                                        providerSearchResult2.setPageUrl(stringBuilder6.toString());
                                        providerSearchResult2.setTitle(str2);
                                        providerSearchResult2.setYear(str);
                                        providerSearchResult2.setCookie(wvcookie);
                                        return providerSearchResult2;
                                    }
                                }
                                continue;
                            }
                        }
                        continue;
                    } else {
                        continue;
                    }
                } catch (List<String> list2) {
                    list2.printStackTrace();
                }
            }
        } catch (ProviderSearchResult providerSearchResult3) {
            providerSearchResult3.printStackTrace();
        }
        return null;
    }

    public void getSources(ProviderSearchResult providerSearchResult, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        if (providerSearchResult != null) {
            try {
                if (providerSearchResult.getPageUrl() != null) {
                    Document parse = Jsoup.parse(wvgethtml(providerSearchResult.getPageUrl()));
                    if (parse != null) {
                        Iterator it = parse.select("div.anime_muti_link").first().select("li").iterator();
                        while (it.hasNext()) {
                            processLink(((Element) it.next()).attr("data-video"), providerSearchResult.getPageUrl(), null, copyOnWriteArrayList, providerSearchResult.getTitle());
                            if (hasMaxSources(copyOnWriteArrayList)) {
                                return;
                            }
                        }
                    }
                }
            } catch (ProviderSearchResult providerSearchResult2) {
                providerSearchResult2.printStackTrace();
            }
        }
    }
}
