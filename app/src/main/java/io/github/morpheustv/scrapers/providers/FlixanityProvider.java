package io.github.morpheustv.scrapers.providers;

import android.util.Base64;
import com.google.common.net.HttpHeaders;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class FlixanityProvider extends BaseProvider {
    String base_link = "https://flixanity.xyz";
    String[] domains = new String[]{"flixanity.xyz"};

    public FlixanityProvider(Scraper scraper) {
        super(scraper, "FLIXANITY.XYZ", false);
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            for (String str22 : list) {
                r2 = new Object[3];
                boolean z = true;
                r2[1] = cleantitleurl(str22);
                r2[2] = str;
                String format = String.format("%s/movie/%s-%s", r2);
                Connection timeout = Jsoup.connect(format).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(10000);
                timeout.cookies(timeout.execute().cookies());
                if (((Element) timeout.get().select("title").get(0)).text().toLowerCase().contains("oops")) {
                    format = String.format("%s/movie/%s", new Object[]{this.base_link, r0});
                    Connection timeout2 = Jsoup.connect(format).validateTLSCertificates(false).ignoreContentType(true).ignoreHttpErrors(true).userAgent(UserAgent).timeout(10000);
                    timeout2.cookies(timeout2.execute().cookies());
                    if (((Element) timeout2.get().select("title").get(0)).text().toLowerCase().contains("oops")) {
                        z = false;
                        continue;
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
                if (z) {
                    list = new ProviderSearchResult();
                    list.setTitle(str22);
                    list.setYear(str);
                    list.setPageUrl(format);
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
        FlixanityProvider flixanityProvider = this;
        String str2 = str;
        if (providerSearchResult != null) {
            try {
                for (String str3 : list) {
                    r8 = new Object[5];
                    boolean z = true;
                    r8[1] = cleantitleurl(str3.replace("-", StringUtils.SPACE));
                    r8[2] = str2;
                    r8[3] = Integer.valueOf(i);
                    r8[4] = Integer.valueOf(i2);
                    String format = String.format("%s/tv-show/%s-%s/season/%01d/episode/%01d", r8);
                    Connection timeout = Jsoup.connect(format).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(10000);
                    timeout.cookies(timeout.execute().cookies());
                    if (((Element) timeout.get().select("title").get(0)).text().toLowerCase().contains("oops")) {
                        String text;
                        format = String.format("%s/show/%s-%s/season/%01d/episode/%01d", new Object[]{flixanityProvider.base_link, text, String.valueOf(Integer.parseInt(str) + 1), Integer.valueOf(i), Integer.valueOf(i2)});
                        Connection timeout2 = Jsoup.connect(format).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(10000);
                        timeout2.cookies(timeout2.execute().cookies());
                        String text2 = ((Element) timeout2.get().select("title").get(0)).text();
                        if (!text2.toLowerCase().contains("watch")) {
                            if (!text2.toLowerCase().contains("episode")) {
                                format = String.format("%s/tv-show/%s/season/%01d/episode/%01d", new Object[]{flixanityProvider.base_link, text, Integer.valueOf(i), Integer.valueOf(i2)});
                                Connection timeout3 = Jsoup.connect(format).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(10000);
                                timeout3.cookies(timeout3.execute().cookies());
                                text = ((Element) timeout3.get().select("title").get(0)).text();
                                if (!text.toLowerCase().contains("watch")) {
                                    if (!text.toLowerCase().contains("episode")) {
                                        z = false;
                                    }
                                }
                            }
                        }
                    }
                    if (z) {
                        ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                        providerSearchResult2.setTitle(str3);
                        providerSearchResult2.setYear(str2);
                        providerSearchResult2.setPageUrl(format);
                        providerSearchResult2.setEpisode(i2);
                        providerSearchResult2.setSeason(i);
                        return providerSearchResult2;
                    }
                    int i3 = i;
                    int i4 = i2;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getEmbedLink() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.base_link);
        stringBuilder.append("/ajax/gonlflhyad.php");
        String stringBuilder2 = stringBuilder.toString();
        try {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(this.base_link);
            stringBuilder3.append("/templates/FliXanity/assets/scripts/videojs-flixanity.js");
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append("\"");
            stringBuilder4.append(this.base_link);
            stringBuilder4.append("/ajax/");
            stringBuilder4 = new StringBuilder();
            stringBuilder4.append("\"");
            stringBuilder4.append(this.base_link);
            stringBuilder4.append("/ajax/");
            Iterator it = extractLinks(Jsoup.connect(stringBuilder3.toString()).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).execute().body().replace("baseurl+\"/ajax/", stringBuilder4.toString()).replace("baseurl + \"/ajax/", stringBuilder4.toString())).iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                if (str.contains(this.base_link) && str.matches(".*/ajax/.*.php") && !str.contains("error.php") && !str.contains("sources.php") && !str.contains("view.php")) {
                    return str;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder2;
    }

    public void getSources(ProviderSearchResult providerSearchResult, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        if (providerSearchResult != null) {
            try {
                if (!(providerSearchResult.getPageUrl() == null || providerSearchResult.getPageUrl().isEmpty())) {
                    String pageUrl = providerSearchResult.getPageUrl();
                    Connection timeout = Jsoup.connect(pageUrl).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(10000);
                    Map cookies = timeout.execute().cookies();
                    timeout.cookies(cookies);
                    Document document = timeout.get();
                    Pattern compile = Pattern.compile("var\\s+tok\\s*=\\s*'([^']+)");
                    Pattern compile2 = Pattern.compile("elid\\s*=\\s*\"([^\"]+)");
                    Matcher matcher = compile.matcher(document.outerHtml());
                    if (matcher.find() && matcher.groupCount() > 0) {
                        String group = matcher.group(1);
                        Matcher matcher2 = compile2.matcher(document.outerHtml());
                        if (matcher2.find() && matcher2.groupCount() > 0) {
                            String group2 = matcher2.group(1);
                            String encode = URLEncoder.encode(Base64.encodeToString(String.valueOf(System.currentTimeMillis() / 1000).getBytes(), 4).trim(), "UTF-8");
                            String str = "Bearer false";
                            String str2 = "getMovieEmb";
                            if (pageUrl.contains("/episode/")) {
                                str2 = "getEpisodeEmb";
                            }
                            pageUrl = Jsoup.connect(getEmbedLink()).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(10000).header("Authorization", str).header(HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest").header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8").header(HttpHeaders.ACCEPT, "application/json, text/javascript, */*; q=0.01").referrer(pageUrl).cookies(cookies).data("action", str2).data("idEl", group2).data("token", group).data("nopop", "").data("elid", encode).method(Method.POST).execute().body();
                            HashSet hashSet = new HashSet();
                            JSONObject jSONObject = new JSONObject(pageUrl);
                            Iterator keys = jSONObject.keys();
                            while (keys.hasNext()) {
                                String str3 = (String) keys.next();
                                if (jSONObject.get(str3) instanceof JSONObject) {
                                    Iterator it = Jsoup.parse(jSONObject.getJSONObject(str3).getString("embed")).select("iframe").iterator();
                                    while (it.hasNext()) {
                                        hashSet.add(((Element) it.next()).attr("src"));
                                    }
                                }
                            }
                            keys = hashSet.iterator();
                            while (keys.hasNext()) {
                                processLink((String) keys.next(), providerSearchResult.getPageUrl(), null, copyOnWriteArrayList, providerSearchResult.getTitle());
                                if (hasMaxSources(copyOnWriteArrayList)) {
                                    return;
                                }
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
