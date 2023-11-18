package io.github.morpheustv.scrapers.providers;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jsoup.Jsoup;

public class PutlockersMovieProvider extends BaseProvider {
    String base_link;
    String[] domains;

    public PutlockersMovieProvider(Scraper scraper) {
        super(scraper, "PUTLOCKERR.IS", false);
        this.domains = new String[]{"www5.putlockerr.is"};
        this.base_link = "https://www5.putlockerr.is";
        this.resourceWhitelist = new String[]{"(.*chk_jschl?.*)", "(.*jquery.*.js.*)", "(.*putlockerr.is.*)"};
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            ProviderSearchResult providerSearchResult = new ProviderSearchResult();
            providerSearchResult.setTitle((String) list.get(0));
            providerSearchResult.setYear(str);
            list = new StringBuilder();
            list.append(this.base_link);
            list.append("/embed/");
            list.append(str2);
            list.append("/");
            providerSearchResult.setPageUrl(list.toString());
            providerSearchResult.setImdb(str2);
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
                ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                providerSearchResult2.setTitle((String) list.get(0));
                providerSearchResult2.setYear(str);
                list = new StringBuilder();
                list.append(this.base_link);
                list.append("/embed/");
                list.append(providerSearchResult.getImdb());
                list.append("/");
                list.append(String.valueOf(i));
                list.append("-");
                list.append(String.valueOf(i2));
                list.append("/");
                providerSearchResult2.setPageUrl(list.toString());
                providerSearchResult2.setImdb(providerSearchResult.getImdb());
                return providerSearchResult2;
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
            HashSet hashSet = new HashSet();
            String attr = Jsoup.connect(providerSearchResult2.getPageUrl()).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(5000).get().select("iframe").attr("src");
            if (!attr.isEmpty()) {
                hashSet.add(attr);
            }
            if (hashSet.size() > 0) {
                Iterator it = hashSet.iterator();
                while (it.hasNext()) {
                    try {
                        processLink((String) it.next(), providerSearchResult2.getPageUrl(), null, copyOnWriteArrayList, providerSearchResult2.getTitle());
                        if (hasMaxSources(copyOnWriteArrayList)) {
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
