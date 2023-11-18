package io.github.morpheustv.scrapers.providers;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class PlayMoviesProvider extends BaseProvider {
    String base_link = "http://playmovies.es";
    String[] domains = new String[]{"playmovies.es"};

    public PlayMoviesProvider(Scraper scraper) {
        super(scraper, "PLAYMOVIES.ES", true);
    }

    public String request(String str, String str2, boolean z) {
        String str3 = "";
        for (int i = 0; i < 3; i++) {
            str3 = wvgethtml(str, str2, z);
            if (str3 != null && !str3.isEmpty()) {
                break;
            }
        }
        return str3;
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            for (String str3 : list) {
                try {
                    String attr;
                    URL url = new URL(this.base_link);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("/?s=");
                    stringBuilder.append(cleantitlequery(str3));
                    String url2 = new URL(url, stringBuilder.toString()).toString();
                    Iterator it = Jsoup.parse(request(url2, this.base_link, false)).select("div.ml-item").iterator();
                    while (it.hasNext()) {
                        Element element = (Element) it.next();
                        if (BaseProvider.cleantitle(element.select("h2").text()).equals(BaseProvider.cleantitle(str3)) && ((Element) element.select("div.jt-info").get(1)).text().equals(str)) {
                            attr = element.select("a.btn-successful").attr("href");
                            break;
                        }
                    }
                    attr = null;
                    if (attr != null) {
                        ProviderSearchResult providerSearchResult = new ProviderSearchResult();
                        providerSearchResult.setTitle(str3);
                        providerSearchResult.setYear(str);
                        providerSearchResult.setPageUrl(attr);
                        providerSearchResult.setReferer(url2);
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
                        stringBuilder.append("/?s=");
                        stringBuilder.append(cleantitlequery(str2));
                        String url2 = new URL(url, stringBuilder.toString()).toString();
                        Iterator it = Jsoup.parse(request(url2, this.base_link, false)).select("div.ml-item").iterator();
                        while (it.hasNext()) {
                            Element element = (Element) it.next();
                            String text = element.select("h2").text();
                            if (BaseProvider.cleantitle(text).contains(BaseProvider.cleantitle(str2))) {
                                String cleantitle = BaseProvider.cleantitle(text);
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("Season ");
                                stringBuilder2.append(String.valueOf(i));
                                if (!cleantitle.contains(BaseProvider.cleantitle(stringBuilder2.toString()))) {
                                    text = BaseProvider.cleantitle(text);
                                    StringBuilder stringBuilder3 = new StringBuilder();
                                    stringBuilder3.append("Season ");
                                    stringBuilder3.append(String.format("%02d", new Object[]{Integer.valueOf(i)}));
                                    if (text.contains(BaseProvider.cleantitle(stringBuilder3.toString()))) {
                                    }
                                }
                                attr = element.select("a").attr("href");
                                break;
                            }
                        }
                        attr = null;
                        if (attr != null) {
                            ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                            providerSearchResult2.setTitle(str2);
                            providerSearchResult2.setYear(str);
                            providerSearchResult2.setPageUrl(attr);
                            providerSearchResult2.setEpisode(i2);
                            providerSearchResult2.setSeason(i);
                            providerSearchResult2.setReferer(url2);
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

    public void getSources(ProviderSearchResult providerSearchResult, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        if (providerSearchResult == null) {
            try {
                throw new Exception();
            } catch (ProviderSearchResult providerSearchResult2) {
                providerSearchResult2.printStackTrace();
            }
        } else {
            Document parse = Jsoup.parse(request(providerSearchResult2.getPageUrl(), providerSearchResult2.getReferer(), false));
            if (providerSearchResult2.getSeason() == 0 && providerSearchResult2.getEpisode() == 0) {
                Iterator it = parse.select("iframe").iterator();
                while (it.hasNext()) {
                    Element element = (Element) it.next();
                    if (element.id().isEmpty()) {
                        processLink(element.attr("src"), providerSearchResult2.getPageUrl(), null, copyOnWriteArrayList, providerSearchResult2.getTitle());
                        if (hasMaxSources(copyOnWriteArrayList)) {
                            return;
                        }
                    }
                }
            }
            Iterator it2 = parse.select("div.les-content").select("a").iterator();
            while (it2.hasNext()) {
                Element element2 = (Element) it2.next();
                if (matchesEpisode(element2.text(), providerSearchResult2.getEpisode())) {
                    String replace = element2.attr("href").replace("#", "");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("div#");
                    stringBuilder.append(replace);
                    element2 = parse.select(stringBuilder.toString()).select("iframe").first();
                    if (element2 != null) {
                        processLink(element2.attr("src"), providerSearchResult2.getPageUrl(), null, copyOnWriteArrayList, providerSearchResult2.getTitle());
                        if (hasMaxSources(copyOnWriteArrayList)) {
                            return;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
    }
}
