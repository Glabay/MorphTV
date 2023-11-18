package io.github.morpheustv.scrapers.providers;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jsoup.Jsoup;

public class WatchonlineALProvider extends BaseProvider {
    String base_link;
    String[] domains;

    public WatchonlineALProvider(Scraper scraper) {
        super(scraper, "WATCHONLINE.AL", true);
        this.domains = new String[]{"watchonline.al"};
        this.base_link = "https://watchonline.al";
        this.resourceWhitelist = new String[]{"(.*chk_jschl?.*)"};
    }

    private String request(String str) {
        try {
            return Jsoup.parse(wvgethtml(str)).html();
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
                stringBuilder.append("/movie/");
                stringBuilder.append(cleantitleurl(str3));
                String stringBuilder2 = stringBuilder.toString();
                String request = request(stringBuilder2);
                if (!request.toLowerCase().contains("page you were looking for is not here")) {
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

    public ProviderSearchResult getTvShow(List<String> list, String str, String str2) {
        try {
            String str3 = (String) list.get(0);
            ProviderSearchResult providerSearchResult = new ProviderSearchResult();
            providerSearchResult.setTitle(str3);
            providerSearchResult.setYear(str);
            providerSearchResult.setPageUrl("");
            providerSearchResult.setImdb(str2);
            return providerSearchResult;
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
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(this.base_link);
                        stringBuilder.append("/episode/");
                        stringBuilder.append(cleantitleurl(str2));
                        stringBuilder.append("-season-");
                        stringBuilder.append(String.valueOf(i));
                        stringBuilder.append("-episode-");
                        stringBuilder.append(String.valueOf(i2));
                        String stringBuilder2 = stringBuilder.toString();
                        String request = request(stringBuilder2);
                        if (!request.toLowerCase().contains("page you were looking for is not here")) {
                            ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                            providerSearchResult2.setTitle(str2);
                            providerSearchResult2.setYear(str);
                            providerSearchResult2.setPageUrl(stringBuilder2);
                            providerSearchResult2.setImdb(providerSearchResult.getImdb());
                            providerSearchResult2.setContent(request);
                            return providerSearchResult2;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
                return;
            }
        }
        processLink(Jsoup.parse(providerSearchResult2.getContent()).select("div.movieplay").select("iframe").attr("src"), providerSearchResult2.getPageUrl(), null, copyOnWriteArrayList, providerSearchResult2.getTitle());
    }
}
