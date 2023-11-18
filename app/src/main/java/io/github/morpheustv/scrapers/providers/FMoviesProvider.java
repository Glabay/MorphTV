package io.github.morpheustv.scrapers.providers;

import android.webkit.CookieManager;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.common.net.HttpHeaders;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class FMoviesProvider extends BaseProvider {
    String base_link;
    String[] domains;

    public FMoviesProvider(Scraper scraper) {
        super(scraper, "fmovies.fyi", true);
        this.domains = new String[]{"ww1.fmovies.io"};
        this.base_link = "https://ww1.fmovies.io";
        this.resourceWhitelist = new String[]{"(.*chk_jschl?.*)", "(.*jquery.*)", "(.*public/all.*.js.*)", "(.*episode/info.*)", "(.*search.html?keyword=.*)", "(.*/ajax/.*)"};
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            wvgethtml(this.base_link);
            String cookie = CookieManager.getInstance().getCookie(this.base_link);
            for (String str3 : list) {
                try {
                    String stringBuilder;
                    URL url = new URL(this.base_link);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("/search.html?keyword=");
                    stringBuilder2.append(cleantitlequery(str3));
                    String url2 = new URL(url, stringBuilder2.toString()).toString();
                    Iterator it = Jsoup.parse(wvgethtml(url2)).select("div.movie-list").select("div.item").iterator();
                    while (it.hasNext()) {
                        Element element = (Element) it.next();
                        if (BaseProvider.cleantitle(((Element) element.select("a.name").get(0)).text()).equals(BaseProvider.cleantitle(str3))) {
                            String attr = element.attr("data-tip");
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append(this.base_link);
                            stringBuilder3.append("/");
                            stringBuilder3.append(attr);
                            Document document = Jsoup.connect(stringBuilder3.toString()).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).header(HttpHeaders.COOKIE, cookie).userAgent(UserAgent).timeout(10000).get();
                            if (document.select("div.title").select(TtmlNode.TAG_SPAN).text().equals(str)) {
                                StringBuilder stringBuilder4 = new StringBuilder();
                                stringBuilder4.append(this.base_link);
                                stringBuilder4.append(document.select("a.watch-now").attr("href"));
                                stringBuilder = stringBuilder4.toString();
                                break;
                            }
                        }
                    }
                    stringBuilder = null;
                    if (stringBuilder != null) {
                        ProviderSearchResult providerSearchResult = new ProviderSearchResult();
                        providerSearchResult.setTitle(str3);
                        providerSearchResult.setYear(str);
                        providerSearchResult.setPageUrl(stringBuilder);
                        providerSearchResult.setCookie(cookie);
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
                wvgethtml(this.base_link);
                providerSearchResult = CookieManager.getInstance().getCookie(this.base_link);
                for (String str2 : list) {
                    try {
                        String stringBuilder;
                        URL url = new URL(this.base_link);
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("/search.html?keyword=");
                        stringBuilder2.append(cleantitlequery(str2));
                        String url2 = new URL(url, stringBuilder2.toString()).toString();
                        if (i > 1) {
                            url = new URL(this.base_link);
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("/search.html?keyword=");
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append(str2);
                            stringBuilder3.append(StringUtils.SPACE);
                            stringBuilder3.append(String.valueOf(i));
                            stringBuilder2.append(cleantitlequery(stringBuilder3.toString()));
                            url2 = new URL(url, stringBuilder2.toString()).toString();
                        }
                        Iterator it = Jsoup.parse(wvgethtml(url2)).select("div.movie-list").select("div.item").iterator();
                        while (it.hasNext()) {
                            Element element = (Element) it.next();
                            String text = ((Element) element.select("a.name").get(0)).text();
                            String cleantitle = BaseProvider.cleantitle(text);
                            StringBuilder stringBuilder4 = new StringBuilder();
                            stringBuilder4.append(str2);
                            stringBuilder4.append(StringUtils.SPACE);
                            stringBuilder4.append(String.valueOf(i));
                            StringBuilder stringBuilder5;
                            if (!cleantitle.equals(BaseProvider.cleantitle(stringBuilder4.toString()))) {
                                if (BaseProvider.cleantitle(text).equals(BaseProvider.cleantitle(str2))) {
                                    stringBuilder5 = new StringBuilder();
                                    stringBuilder5.append(this.base_link);
                                    stringBuilder5.append(element.select("a.poster").attr("href"));
                                    stringBuilder = stringBuilder5.toString();
                                    break;
                                }
                            }
                            stringBuilder5 = new StringBuilder();
                            stringBuilder5.append(this.base_link);
                            stringBuilder5.append(element.select("a.poster").attr("href"));
                            stringBuilder = stringBuilder5.toString();
                            break;
                        }
                        stringBuilder = null;
                        if (stringBuilder != null) {
                            ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                            providerSearchResult2.setTitle(str2);
                            providerSearchResult2.setYear(str);
                            providerSearchResult2.setPageUrl(stringBuilder);
                            providerSearchResult2.setCookie(providerSearchResult);
                            providerSearchResult2.setEpisode(i2);
                            providerSearchResult2.setSeason(i);
                            providerSearchResult2.setReferer(url2);
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
            }
        } else {
            Iterator it = Jsoup.parse(new JSONObject(Jsoup.connect(wvmatchresource(providerSearchResult2.getPageUrl(), "(.*/servers/.*)", 10000)).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).header(HttpHeaders.COOKIE, providerSearchResult2.getCookie()).userAgent(UserAgent).timeout(10000).execute().body()).optString("html")).select("ul.episodes").iterator();
            while (it.hasNext()) {
                Element element = (Element) it.next();
                String str = null;
                try {
                    Iterator it2;
                    if (providerSearchResult2.getEpisode() > 0 && providerSearchResult2.getSeason() > 0) {
                        it2 = element.select("li").iterator();
                        while (it2.hasNext()) {
                            Element element2 = (Element) it2.next();
                            Element element3 = (Element) element2.select("a").get(0);
                            if (element2.text().toLowerCase().startsWith(String.format("%02d", new Object[]{Integer.valueOf(providerSearchResult2.getEpisode())}))) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(this.base_link);
                                stringBuilder.append(element3.attr("href"));
                                str = stringBuilder.toString();
                                break;
                            }
                        }
                    }
                    element = (Element) element.select("a").get(0);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(this.base_link);
                    stringBuilder2.append(element.attr("href"));
                    str = stringBuilder2.toString();
                    if (str != null) {
                        this.loadedResources.clear();
                        String wvgethtml = wvgethtml(str, providerSearchResult2.getPageUrl(), false);
                        Thread.sleep(2000);
                        try {
                            it2 = Jsoup.parse(wvgethtml).select("iframe").iterator();
                            while (it2.hasNext()) {
                                String attr = ((Element) it2.next()).attr("src");
                                if (!(attr == null || attr.isEmpty() || !attr.startsWith("http"))) {
                                    processLink(attr, providerSearchResult2.getPageUrl(), null, copyOnWriteArrayList, providerSearchResult2.getTitle());
                                }
                                if (hasMaxSources(copyOnWriteArrayList)) {
                                    return;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        wvgethtml = findLoadedResource("(.+grabber-api.+)");
                        if (!(wvgethtml == null || wvgethtml.isEmpty())) {
                            wvgethtml = wvgethtml(wvgethtml, str, true);
                            if (wvgethtml != null) {
                                try {
                                    wvgethtml = Jsoup.parse(wvgethtml).text();
                                    if (wvgethtml.startsWith("\"")) {
                                        wvgethtml = wvgethtml.replaceAll("^\"|\"$", "");
                                    }
                                    JSONArray jSONArray = new JSONObject(wvgethtml).getJSONArray("data");
                                    int i = 0;
                                    while (i < jSONArray.length()) {
                                        JSONObject jSONObject = jSONArray.getJSONObject(i);
                                        processLink(jSONObject.optString("file").replace("\"", ""), str, jSONObject.optString("label").replace("\"", ""), copyOnWriteArrayList, providerSearchResult2.getTitle());
                                        if (!hasMaxSources(copyOnWriteArrayList)) {
                                            i++;
                                        } else {
                                            return;
                                        }
                                    }
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            }
                        }
                        wvgethtml = findLoadedResource("(.*episode/info.*)");
                        if (!(wvgethtml == null || wvgethtml.isEmpty())) {
                            wvgethtml = wvgethtml(wvgethtml, str, true);
                            if (wvgethtml != null) {
                                try {
                                    it2 = extractLinks(Jsoup.parse(wvgethtml).text()).iterator();
                                    while (it2.hasNext()) {
                                        String str2 = (String) it2.next();
                                        if (!(str2.contains("grabber-api") || str2.contains("/subtitle/"))) {
                                            processLink(str2, providerSearchResult2.getPageUrl(), null, copyOnWriteArrayList, providerSearchResult2.getTitle());
                                        }
                                        if (hasMaxSources(copyOnWriteArrayList)) {
                                            return;
                                        }
                                    }
                                    continue;
                                } catch (Exception e22) {
                                    e22.printStackTrace();
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                } catch (Exception e222) {
                    e222.printStackTrace();
                }
            }
        }
    }
}
