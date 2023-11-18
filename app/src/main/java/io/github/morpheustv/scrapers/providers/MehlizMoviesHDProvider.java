package io.github.morpheustv.scrapers.providers;

import android.webkit.CookieManager;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.common.net.HttpHeaders;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MehlizMoviesHDProvider extends BaseProvider {
    private static String cookie = "";
    String base_link = "https://www1.mehlizmovies.com";
    String[] domains = new String[]{"www1.mehlizmovies.com"};

    public MehlizMoviesHDProvider(Scraper scraper) {
        super(scraper, "MEHLIZMOVIES.COM", true);
        scraper = new String[12];
        scraper[0] = "(.*chk_jschl?.*)";
        scraper[1] = "(.*jquery.*)";
        scraper[2] = "(.*?s=.*)";
        scraper[3] = "(.*captcha.*)";
        scraper[4] = "(.*googleapis.*)";
        scraper[5] = "(.*token.*)";
        scraper[6] = "(.*heyhey.*)";
        scraper[7] = "(.*google.com/js/.*)";
        scraper[8] = "(.*challenge.js.*)";
        scraper[9] = "(.*dooplay.*.js.*)";
        scraper[10] = "(.*piguiqproxy.*)";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(.*");
        stringBuilder.append(this.base_link);
        stringBuilder.append(".*)");
        scraper[11] = stringBuilder.toString();
        this.resourceWhitelist = scraper;
    }

    public boolean isCaptchaPage(String str) {
        return str.toLowerCase().contains("validation required");
    }

    public String request(String str, String str2, boolean z) {
        CookieManager instance = CookieManager.getInstance();
        String wvgethtml = wvgethtml(str, str2, z);
        cookie = instance.getCookie(this.base_link);
        if (isCaptchaPage(wvgethtml)) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.base_link);
                stringBuilder.append("/wp-content/plugins/spam-detection/captcha-verification.php");
                Connection ignoreHttpErrors = Jsoup.connect(stringBuilder.toString()).userAgent(BaseProvider.UserAgent).validateTLSCertificates(false).timeout(10000).data("verification_box", "on").followRedirects(true).ignoreContentType(true).ignoreHttpErrors(true);
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append("/");
                Response execute = ignoreHttpErrors.referrer(stringBuilder2.toString()).header(TtmlNode.ATTR_TTS_ORIGIN, str).header(HttpHeaders.COOKIE, cookie != null ? cookie : "").method(Method.POST).execute();
                if (execute.cookies() != null && execute.cookies().size() > 0) {
                    for (Entry entry : execute.cookies().entrySet()) {
                        StringBuilder stringBuilder3;
                        if (!cookie.isEmpty()) {
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append(cookie);
                            stringBuilder3.append("; ");
                            cookie = stringBuilder3.toString();
                        }
                        stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(cookie);
                        stringBuilder3.append((String) entry.getKey());
                        stringBuilder3.append("=");
                        stringBuilder3.append((String) entry.getValue());
                        cookie = stringBuilder3.toString();
                    }
                    instance.setCookie(this.base_link, cookie);
                }
                return wvgethtml(str, str2, z);
            } catch (String str3) {
                str3.printStackTrace();
            }
        }
        return wvgethtml;
    }

    public String request(String str) {
        return request(str, null, false);
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            request(this.base_link);
            for (String str22 : list) {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(this.base_link);
                    stringBuilder.append("/movies/");
                    stringBuilder.append(cleantitleurl(str22));
                    String stringBuilder2 = stringBuilder.toString();
                    if (!request(stringBuilder2).contains("Page not found")) {
                        ProviderSearchResult providerSearchResult = new ProviderSearchResult();
                        providerSearchResult.setPageUrl(stringBuilder2);
                        providerSearchResult.setTitle(str22);
                        providerSearchResult.setYear(str);
                        return providerSearchResult;
                    }
                } catch (String str222) {
                    str222.printStackTrace();
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
        try {
            request(this.base_link);
            for (String str2 : list) {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(this.base_link);
                    stringBuilder.append("/tvshows/");
                    stringBuilder.append(cleantitleurl(str2));
                    String request = request(stringBuilder.toString());
                    if (request.contains("Page not found")) {
                        continue;
                    } else {
                        Iterator it = Jsoup.parse(request).select("ul.episodios").iterator();
                        while (it.hasNext()) {
                            Iterator it2 = ((Element) it.next()).select("li").iterator();
                            while (it2.hasNext()) {
                                Element element = (Element) it2.next();
                                String text = element.select("div.numerando").text();
                                String attr = element.select("a").attr("href");
                                if (text.equals(String.format(String.format("%d - %d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}), new Object[0]))) {
                                    ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                                    providerSearchResult2.setPageUrl(attr);
                                    providerSearchResult2.setTitle(str2);
                                    providerSearchResult2.setYear(str);
                                    providerSearchResult2.setSeason(i);
                                    providerSearchResult2.setEpisode(i2);
                                    return providerSearchResult2;
                                }
                            }
                        }
                        continue;
                    }
                } catch (List<String> list2) {
                    list2.printStackTrace();
                }
            }
        } catch (ProviderSearchResult providerSearchResult3) {
            providerSearchResult3.printStackTrace();
        }
        return null;
    }

    public void getSources(ProviderSearchResult providerSearchResult, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        if (providerSearchResult != null) {
            try {
                if (providerSearchResult.getPageUrl() != null) {
                    Document parse = Jsoup.parse(request(providerSearchResult.getPageUrl()));
                    if (parse != null) {
                        Iterator it = parse.select("div.playex").select("iframe").iterator();
                        while (it.hasNext()) {
                            try {
                                String attr = ((Element) it.next()).attr("src");
                                if (attr.contains("mehlizmovies") || attr.contains("embed.php")) {
                                    Matcher matcher = Pattern.compile("sources\\s*:\\s*(\\[.*\\{.+?\\])").matcher(Jsoup.parse(request(attr, providerSearchResult.getPageUrl(), false)).html());
                                    if (matcher.find() && matcher.groupCount() > 0) {
                                        String group = matcher.group(1);
                                        if (group != null) {
                                            group = StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeEcmaScript(group.replace(",]", "]").replace("﻿", "")));
                                            if (group.startsWith("\"")) {
                                                group = group.replaceAll("^\"|\"$", "");
                                            }
                                            StringBuilder stringBuilder = new StringBuilder();
                                            stringBuilder.append("{\"sources\":");
                                            stringBuilder.append(group);
                                            stringBuilder.append("}");
                                            JSONArray jSONArray = new JSONObject(stringBuilder.toString()).getJSONArray("sources");
                                            for (int i = 0; i < jSONArray.length(); i++) {
                                                JSONObject jSONObject = jSONArray.getJSONObject(i);
                                                String replace = jSONObject.optString("file").replace("\"", "");
                                                String replace2 = jSONObject.optString("label").replace("\"", "");
                                                if (!replace.isEmpty()) {
                                                    processLink(replace, attr, replace2, copyOnWriteArrayList, providerSearchResult.getTitle());
                                                    if (hasMaxSources(copyOnWriteArrayList)) {
                                                        return;
                                                    }
                                                }
                                            }
                                            continue;
                                        } else {
                                            continue;
                                        }
                                    }
                                } else {
                                    processLink(attr, providerSearchResult.getPageUrl(), null, copyOnWriteArrayList, providerSearchResult.getTitle());
                                    if (hasMaxSources(copyOnWriteArrayList)) {
                                        return;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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
