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
import org.jsoup.nodes.Element;

public class YesMoviesScProvider extends BaseProvider {
    String base_link;
    String[] domains;

    public YesMoviesScProvider(Scraper scraper) {
        super(scraper, "YESMOVIES.SC", true);
        this.domains = new String[]{"yesmovies.sc"};
        this.base_link = "https://yesmovies.sc";
        this.resourceWhitelist = new String[]{"(.*chk_jschl?.*)", "(.*hopecloud.*.js.*)", "(.*cdn.*.js.*)", "(.*player.*.js.*)", "(.*movie_.*)"};
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            for (String str3 : list) {
                try {
                    String attr;
                    StringBuilder stringBuilder = new StringBuilder();
                    URL url = new URL(this.base_link);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("/search-query/");
                    stringBuilder2.append(cleantitlequery(str3));
                    stringBuilder.append(new URL(url, stringBuilder2.toString()).toString());
                    stringBuilder.append("/");
                    Iterator it = Jsoup.parse(wvgethtml(stringBuilder.toString())).select("div.ml-item").iterator();
                    while (it.hasNext()) {
                        Element element = (Element) it.next();
                        if (BaseProvider.cleantitle(element.select("a").first().attr("title")).equals(BaseProvider.cleantitle(str3))) {
                            attr = element.select("a").first().attr("href");
                            if (attr.contains(str)) {
                                break;
                            }
                        }
                    }
                    attr = null;
                    if (attr != null) {
                        ProviderSearchResult providerSearchResult = new ProviderSearchResult();
                        providerSearchResult.setTitle(str3);
                        providerSearchResult.setYear(str);
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(attr);
                        stringBuilder3.append("watching/");
                        providerSearchResult.setPageUrl(stringBuilder3.toString());
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
                        stringBuilder.append("/search-query/");
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(str2);
                        stringBuilder2.append(" season ");
                        stringBuilder2.append(String.valueOf(i));
                        stringBuilder.append(cleantitlequery(stringBuilder2.toString()));
                        stringBuilder.append("/");
                        Iterator it = Jsoup.parse(wvgethtml(new URL(url, stringBuilder.toString()).toString())).select("div.ml-item").iterator();
                        while (it.hasNext()) {
                            Element element = (Element) it.next();
                            String attr2 = element.select("a").first().attr("title");
                            if (BaseProvider.cleantitle(attr2).contains(BaseProvider.cleantitle(str2))) {
                                attr2 = BaseProvider.cleantitle(attr2);
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("Season ");
                                stringBuilder2.append(String.valueOf(i));
                                if (attr2.contains(BaseProvider.cleantitle(stringBuilder2.toString()))) {
                                    attr = element.select("a").first().attr("href");
                                    break;
                                }
                            }
                        }
                        attr = null;
                        if (attr != null) {
                            ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                            providerSearchResult2.setTitle(str2);
                            providerSearchResult2.setYear(str);
                            list = new StringBuilder();
                            list.append(attr);
                            list.append("watching/");
                            providerSearchResult2.setPageUrl(list.toString());
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

    public void getSources(ProviderSearchResult providerSearchResult, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        if (providerSearchResult == null) {
            try {
                throw new Exception();
            } catch (ProviderSearchResult providerSearchResult2) {
                providerSearchResult2.printStackTrace();
            }
        } else {
            Iterator it = Jsoup.parse(wvgethtml(providerSearchResult2.getPageUrl())).select("a.btn-eps").iterator();
            while (it.hasNext()) {
                Element element = (Element) it.next();
                try {
                    if (providerSearchResult2.getEpisode() <= 0 || providerSearchResult2.getSeason() <= 0 || matchesEpisode(element.text(), providerSearchResult2.getEpisode())) {
                        StringBuilder stringBuilder;
                        String attr = element.attr("data-server");
                        String attr2 = element.attr("data-drive");
                        String attr3 = element.attr("data-photo");
                        String attr4 = element.attr("data-strgo");
                        String attr5 = element.attr("data-openload");
                        String attr6 = element.attr("data-svbackup");
                        String attr7 = element.attr("data-imdb");
                        String str = null;
                        if (!(attr4 == null || attr4.isEmpty())) {
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("https://vidload.co/player/");
                            stringBuilder2.append(attr4);
                            str = stringBuilder2.toString();
                        }
                        if (!(attr5 == null || attr5.isEmpty())) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("https://openload.co/embed/");
                            stringBuilder.append(attr5);
                            str = stringBuilder.toString();
                        }
                        if (!(attr2 == null || attr2.isEmpty())) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("https://play.gomovies.sc/");
                            stringBuilder.append(attr);
                            stringBuilder.append("/");
                            stringBuilder.append(attr2);
                            str = stringBuilder.toString();
                        }
                        if (!(attr3 == null || attr3.isEmpty())) {
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("https://play.gomovies.sc/");
                            stringBuilder3.append(attr);
                            stringBuilder3.append("/");
                            stringBuilder3.append(attr3);
                            str = stringBuilder3.toString();
                        }
                        if (attr7 == null || attr7.isEmpty()) {
                            attr7 = str;
                        } else {
                            StringBuilder stringBuilder4 = new StringBuilder();
                            stringBuilder4.append("https://videospider.in/getvideo?key=IfntUpFt05WyyQAJ&video_id=");
                            stringBuilder4.append(attr7);
                            attr7 = stringBuilder4.toString();
                        }
                        attr2 = (attr6 == null || attr6.isEmpty()) ? attr7 : attr6;
                        if (attr2 != null) {
                            processLink(attr2, providerSearchResult2.getPageUrl(), null, copyOnWriteArrayList, providerSearchResult2.getTitle());
                            if (hasMaxSources(copyOnWriteArrayList)) {
                                return;
                            }
                        } else {
                            continue;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
