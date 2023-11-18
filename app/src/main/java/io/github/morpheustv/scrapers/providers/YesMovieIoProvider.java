package io.github.morpheustv.scrapers.providers;

import android.support.graphics.drawable.PathInterpolatorCompat;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class YesMovieIoProvider extends BaseProvider {
    String base_link;
    String[] domains;
    String search_link;

    public YesMovieIoProvider(Scraper scraper) {
        super(scraper, "YESMOVIES.GG", true);
        this.domains = new String[]{"yesmovies.gg"};
        this.base_link = "https://yesmovies.gg";
        this.search_link = "https://api.yesmovie.io";
        this.resourceWhitelist = new String[]{"(.*chk_jschl?.*)", "(.*jquery.*.js.*)", "(.*player.*.js.*)", "(.*video.*.js.*)", "(.*mobile.*.js.*)", "(.*api.yesmovie.*)"};
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            for (String str3 : list) {
                int i = 0;
                while (i < 2) {
                    try {
                        String attr;
                        StringBuilder stringBuilder = new StringBuilder();
                        URL url = new URL(this.search_link);
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("/movie/search/");
                        stringBuilder2.append(cleantitleurl(str3));
                        stringBuilder.append(new URL(url, stringBuilder2.toString()).toString());
                        stringBuilder.append("?link_web=");
                        stringBuilder.append(this.base_link);
                        stringBuilder.append("/");
                        Iterator it = Jsoup.parse(wvgethtml(stringBuilder.toString())).select("div.ml-item").iterator();
                        while (it.hasNext()) {
                            Element element = (Element) it.next();
                            if (BaseProvider.cleantitle(element.select("h2").text()).equals(BaseProvider.cleantitle(str3))) {
                                String attr2 = ((Element) element.select("a").get(0)).attr("data-url");
                                StringBuilder stringBuilder3 = new StringBuilder();
                                stringBuilder3.append(attr2);
                                stringBuilder3.append("?link_web=");
                                stringBuilder3.append(this.base_link);
                                stringBuilder3.append("/");
                                Document parse = Jsoup.parse(StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeEcmaScript(Jsoup.connect(stringBuilder3.toString()).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(10000).execute().body())));
                                if (((Element) parse.select("div.jt-info").get(1)).text().equals(str)) {
                                    attr = parse.select("a.btn-success").attr("href");
                                    break;
                                }
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
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                        URL url = new URL(this.search_link);
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("/movie/search/");
                        stringBuilder2.append(cleantitleurl(str2));
                        stringBuilder.append(new URL(url, stringBuilder2.toString()).toString());
                        stringBuilder.append("?link_web=");
                        stringBuilder.append(this.base_link);
                        stringBuilder.append("/");
                        Iterator it = Jsoup.parse(wvgethtml(stringBuilder.toString())).select("div.ml-item").iterator();
                        while (it.hasNext()) {
                            Element element = (Element) it.next();
                            String text = element.select("h2").text();
                            if (BaseProvider.cleantitle(text).contains(BaseProvider.cleantitle(str2))) {
                                String cleantitle = BaseProvider.cleantitle(text);
                                StringBuilder stringBuilder3 = new StringBuilder();
                                stringBuilder3.append("Season ");
                                stringBuilder3.append(String.valueOf(i));
                                if (!cleantitle.contains(BaseProvider.cleantitle(stringBuilder3.toString()))) {
                                    text = BaseProvider.cleantitle(text);
                                    stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append("Season ");
                                    stringBuilder2.append(String.format("%02d", new Object[]{Integer.valueOf(i)}));
                                    if (text.contains(BaseProvider.cleantitle(stringBuilder2.toString()))) {
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
                            list = new StringBuilder();
                            list.append(attr);
                            list.append("/watching.html");
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
            throw new Exception();
        }
        Response response = null;
        for (int i = 0; i < 3; i++) {
            try {
                response = Jsoup.connect(providerSearchResult.getPageUrl()).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(PathInterpolatorCompat.MAX_NUM_POINTS).execute();
            } catch (Exception e) {
                try {
                    e.printStackTrace();
                } catch (ProviderSearchResult providerSearchResult2) {
                    providerSearchResult2.printStackTrace();
                }
            }
            if (response != null && response.statusCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                break;
            }
        }
        Iterator it = response.parse().select("a.btn-eps").iterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            try {
                if (providerSearchResult2.getEpisode() <= 0 || providerSearchResult2.getSeason() <= 0 || matchesEpisode(element.text(), providerSearchResult2.getEpisode())) {
                    processLink(element.attr("player-data"), providerSearchResult2.getPageUrl(), null, copyOnWriteArrayList, providerSearchResult2.getTitle());
                    if (hasMaxSources(copyOnWriteArrayList)) {
                        return;
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
