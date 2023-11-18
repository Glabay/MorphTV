package io.github.morpheustv.scrapers.providers;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class AZMoviesProvider extends BaseProvider {
    String base_link;
    String cookie;
    String[] domains;

    public AZMoviesProvider(Scraper scraper) {
        super(scraper, "AZMOVIES.XYZ", true);
        this.domains = new String[]{"azmovies.xyz"};
        this.base_link = "https://azmovies.xyz";
        this.cookie = null;
        this.resourceWhitelist = new String[]{"(.*chk_jschl?.*)", "(.*jquery.*.js.*)", "(.*azmovies.*.js.*)", "(.*watch.php.*)"};
    }

    private String request(String str) {
        try {
            return wvgethtml(str);
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
                stringBuilder.append("/watch.php?title=");
                stringBuilder.append(cleantitlequery(str3));
                String stringBuilder2 = stringBuilder.toString();
                String request = request(stringBuilder2);
                if (!request.toLowerCase().contains("page not found")) {
                    list = new ProviderSearchResult();
                    list.setTitle(str3);
                    list.setYear(str);
                    list.setPageUrl(stringBuilder2);
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

    public void getSources(ProviderSearchResult providerSearchResult, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        if (providerSearchResult == null) {
            try {
                throw new Exception();
            } catch (ProviderSearchResult providerSearchResult2) {
                providerSearchResult2.printStackTrace();
            }
        } else {
            Iterator it = Jsoup.parse(providerSearchResult2.getContent()).select("a[target=iframe]").iterator();
            while (it.hasNext()) {
                String attr = ((Element) it.next()).attr("href");
                if (!(attr == null || attr.isEmpty())) {
                    processLink(attr, providerSearchResult2.getPageUrl(), null, copyOnWriteArrayList, providerSearchResult2.getTitle());
                }
                if (hasMaxSources(copyOnWriteArrayList)) {
                    return;
                }
            }
        }
    }
}
