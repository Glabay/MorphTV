package io.github.morpheustv.scrapers.providers;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class RedditProvider extends BaseProvider {
    String base_link;
    String[] domains;

    public RedditProvider(Scraper scraper) {
        super(scraper, "REDDIT.COM", false);
        this.domains = new String[]{"reddit.com"};
        this.base_link = "https://old.reddit.com";
        this.resourceWhitelist = new String[0];
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
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
                    String str2 = (String) list.get(0);
                    ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                    providerSearchResult2.setTitle(str2);
                    providerSearchResult2.setTitles(list);
                    providerSearchResult2.setYear(str);
                    providerSearchResult2.setPageUrl("");
                    providerSearchResult2.setSeason(i);
                    providerSearchResult2.setEpisode(i2);
                    providerSearchResult2.setImdb(providerSearchResult.getImdb());
                    return providerSearchResult2;
                }
            } catch (ProviderSearchResult providerSearchResult3) {
                providerSearchResult3.printStackTrace();
            }
        }
        return null;
    }

    public void getSources(ProviderSearchResult providerSearchResult, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        if (providerSearchResult != null) {
            try {
                for (String str : providerSearchResult.getTitles()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(StringUtils.SPACE);
                    stringBuilder.append(providerSearchResult.getYear());
                    String stringBuilder2 = stringBuilder.toString();
                    if (providerSearchResult.getSeason() > 0 && providerSearchResult.getEpisode() > 0) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(StringUtils.SPACE);
                        stringBuilder.append(String.format("S%02dE%02d", new Object[]{Integer.valueOf(providerSearchResult.getSeason()), Integer.valueOf(providerSearchResult.getEpisode())}));
                        stringBuilder2 = stringBuilder.toString();
                    }
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(this.base_link);
                    stringBuilder3.append("/r/fullmoviesonopenload/search?q=");
                    stringBuilder3.append(cleantitlequery(stringBuilder2));
                    stringBuilder3.append("&sort=new&restrict_sr=on&t=all");
                    providerSearchResult.setPageUrl(stringBuilder3.toString());
                    Iterator it = Jsoup.connect(providerSearchResult.getPageUrl()).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(5000).get().select("div.search-result").iterator();
                    while (it.hasNext()) {
                        Element element = (Element) it.next();
                        String html = element.select("a.search-title").html();
                        if (BaseProvider.cleantitle(html).contains(BaseProvider.cleantitle(stringBuilder2)) && !html.toLowerCase().contains("trailer")) {
                            String html2 = element.select("a.search-link").html();
                            if (html2.contains("openload.co")) {
                                processLink(html2, providerSearchResult.getPageUrl(), null, copyOnWriteArrayList, providerSearchResult.getTitle());
                                if (hasMaxSources(copyOnWriteArrayList)) {
                                    return;
                                }
                            } else {
                                continue;
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
