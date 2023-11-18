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

public class Movie4KProvider extends BaseProvider {
    String base_link;
    String[] domains;

    public Movie4KProvider(Scraper scraper) {
        super(scraper, "MOVIE4K.IS", false);
        this.domains = new String[]{"movie4k.is"};
        this.base_link = "https://movie4k.is";
        this.resourceWhitelist = new String[]{"(.*chk_jschl?.*)", "(.*jquery.*.js.*)"};
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            for (String str3 : list) {
                String attr;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.base_link);
                stringBuilder.append("/?s=%s");
                Iterator it = Jsoup.connect(String.format(stringBuilder.toString(), new Object[]{cleantitlequery(str3)})).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(15000).get().select("div.item").iterator();
                while (it.hasNext()) {
                    Element element = (Element) it.next();
                    Element first = element.select("div.fixyear").first();
                    String text = first.select("h2").first().text();
                    String text2 = first.select("span.year").first().text();
                    attr = element.select("a").first().attr("href");
                    if (text.equals(str3) && text2.equals(str)) {
                        continue;
                        break;
                    }
                }
                attr = null;
                continue;
                if (attr != null) {
                    ProviderSearchResult providerSearchResult = new ProviderSearchResult();
                    providerSearchResult.setTitle(str3);
                    providerSearchResult.setTitles(list);
                    providerSearchResult.setYear(str);
                    providerSearchResult.setPageUrl(attr);
                    providerSearchResult.setImdb(str2);
                    return providerSearchResult;
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
            try {
                Iterator it = Jsoup.connect(providerSearchResult2.getPageUrl()).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(10000).get().select("iframe").iterator();
                while (it.hasNext()) {
                    processLink(((Element) it.next()).attr("src"), providerSearchResult2.getPageUrl(), null, copyOnWriteArrayList, providerSearchResult2.getTitle());
                    if (hasMaxSources(copyOnWriteArrayList)) {
                        return;
                    }
                }
            } catch (ProviderSearchResult providerSearchResult22) {
                providerSearchResult22.printStackTrace();
            }
        }
    }
}
