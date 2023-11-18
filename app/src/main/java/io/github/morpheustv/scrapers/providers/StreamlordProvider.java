package io.github.morpheustv.scrapers.providers;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.webkit.CookieManager;
import com.google.common.net.HttpHeaders;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class StreamlordProvider extends BaseProvider {
    private String COOKIE = "";
    private String PASSWORD;
    private String USERNAME;
    String base_link = "http://www.streamlord.com";
    String[] domains = new String[]{"streamlord.com"};

    public StreamlordProvider(Scraper scraper) {
        super(scraper, "STREAMLORD.COM", true);
        this.USERNAME = scraper.getConfig().getProviders().getStreamlordUsername();
        this.PASSWORD = scraper.getConfig().getProviders().getStreamlordPassword();
        this.resourceWhitelist = new String[]{"(.*cloudflare.*.js*)", "(.*jquery.*.js*)"};
    }

    public boolean login() {
        boolean z = false;
        try {
            if (!(this.USERNAME == null || this.PASSWORD == null || this.USERNAME.isEmpty())) {
                if (!this.PASSWORD.isEmpty()) {
                    String url = new URL(new URL(this.base_link), "/login.html").toString();
                    Response execute = Jsoup.connect(url).validateTLSCertificates(false).ignoreContentType(true).data("username", this.USERNAME).data("password", this.PASSWORD).data("submit", "Login").header(HttpHeaders.COOKIE, this.COOKIE != null ? this.COOKIE : "").header(HttpHeaders.REFERER, url).userAgent(UserAgent).method(Method.POST).timeout(15000).execute();
                    if (execute.cookies() != null && execute.cookies().size() > 0) {
                        for (Entry entry : execute.cookies().entrySet()) {
                            StringBuilder stringBuilder;
                            if (!this.COOKIE.isEmpty()) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(this.COOKIE);
                                stringBuilder.append("; ");
                                this.COOKIE = stringBuilder.toString();
                            }
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(this.COOKIE);
                            stringBuilder.append((String) entry.getKey());
                            stringBuilder.append("=");
                            stringBuilder.append((String) entry.getValue());
                            this.COOKIE = stringBuilder.toString();
                        }
                    }
                    if (execute.statusCode() == Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                        z = true;
                    }
                    return z;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            if (!login()) {
                return null;
            }
            for (String str3 : list) {
                String stringBuilder;
                Iterator it = Jsoup.connect(new URL(new URL(this.base_link), "/searchapi2.php").toString()).validateTLSCertificates(false).ignoreContentType(true).data("searchapi2", str3).header(HttpHeaders.COOKIE, this.COOKIE != null ? this.COOKIE : "").referrer(this.base_link).userAgent(UserAgent).method(Method.POST).timeout(10000).execute().parse().select("div#movieslist").first().select("div.item").iterator();
                while (it.hasNext()) {
                    String attr = ((Element) ((Element) it.next()).select("a").get(2)).attr("href");
                    if (BaseProvider.cleantitle(attr).replace("watchmovie", "").startsWith(BaseProvider.cleantitle(str3))) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(this.base_link);
                        stringBuilder2.append("/");
                        stringBuilder2.append(attr);
                        stringBuilder = stringBuilder2.toString();
                        continue;
                        break;
                    }
                }
                stringBuilder = null;
                continue;
                if (stringBuilder != null) {
                    list = new ProviderSearchResult();
                    list.setTitle(str3);
                    list.setYear(str);
                    list.setPageUrl(stringBuilder);
                    list.setCookie(this.COOKIE);
                    return list;
                }
            }
            return null;
        } catch (List<String> list2) {
            list2.printStackTrace();
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
        StreamlordProvider streamlordProvider = this;
        if (providerSearchResult != null) {
            try {
                if (providerSearchResult.getPageUrl() == null || !login()) {
                    return null;
                }
                for (String str2 : list) {
                    String str22;
                    String url = new URL(new URL(streamlordProvider.base_link), "/searchapi2.php").toString();
                    int i3 = 0;
                    Iterator it = ((Element) Jsoup.connect(url).validateTLSCertificates(false).ignoreContentType(true).data("searchapi2", str22).header(HttpHeaders.COOKIE, streamlordProvider.COOKIE != null ? streamlordProvider.COOKIE : "").referrer(streamlordProvider.base_link).userAgent(UserAgent).method(Method.POST).timeout(15000).execute().parse().select("div#movieslist").get(1)).select("div.item").iterator();
                    String str3 = null;
                    while (it.hasNext()) {
                        String attr = ((Element) ((Element) it.next()).select("a").get(2)).attr("href");
                        if (BaseProvider.cleantitle(attr).replace("watchtvshow", "").startsWith(BaseProvider.cleantitle(str22))) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(streamlordProvider.base_link);
                            stringBuilder.append("/");
                            stringBuilder.append(attr);
                            Iterator it2 = Jsoup.connect(stringBuilder.toString()).validateTLSCertificates(i3).ignoreContentType(true).header(HttpHeaders.COOKIE, streamlordProvider.COOKIE != null ? streamlordProvider.COOKIE : "").referrer(url).userAgent(UserAgent).timeout(15000).get().select("div.playpic").iterator();
                            while (it2.hasNext()) {
                                String attr2 = ((Element) it2.next()).select("a").first().attr("href");
                                String format = String.format("s%02de%02d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
                                String cleantitle = BaseProvider.cleantitle(attr2);
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(str22);
                                stringBuilder2.append(format);
                                if (cleantitle.startsWith(BaseProvider.cleantitle(stringBuilder2.toString()))) {
                                    stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append(streamlordProvider.base_link);
                                    stringBuilder2.append("/");
                                    stringBuilder2.append(attr2);
                                    str3 = stringBuilder2.toString();
                                    break;
                                }
                                i3 = 0;
                            }
                        }
                        i3 = 0;
                    }
                    if (str3 != null) {
                        ProviderSearchResult providerSearchResult2 = new ProviderSearchResult();
                        providerSearchResult2.setTitle(str22);
                        providerSearchResult2.setYear(str);
                        providerSearchResult2.setPageUrl(str3);
                        providerSearchResult2.setEpisode(i2);
                        providerSearchResult2.setSeason(i);
                        providerSearchResult2.setCookie(streamlordProvider.COOKIE);
                        return providerSearchResult2;
                    }
                    str22 = str;
                    int i4 = i;
                    int i5 = i2;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void getSources(ProviderSearchResult providerSearchResult, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        if (providerSearchResult != null) {
            try {
                CookieManager.getInstance().setCookie(this.base_link, this.COOKIE);
                Matcher matcher = Pattern.compile("\"sources\":\\s*(\\[.*?\\])").matcher(Jsoup.parse(wvgethtml(providerSearchResult.getPageUrl())).html());
                if (matcher.find() && matcher.groupCount() > 0) {
                    String unescapeHtml4 = StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeEcmaScript(matcher.group(1)));
                    if (unescapeHtml4 != null) {
                        if (unescapeHtml4.startsWith("\"")) {
                            unescapeHtml4 = unescapeHtml4.replaceAll("^\"|\"$", "");
                        }
                        int i = 0;
                        if (unescapeHtml4.contains("eval(")) {
                            unescapeHtml4 = unescapeHtml4.substring(unescapeHtml4.indexOf("eval("));
                            addCDNSource(jsunpack(unescapeHtml4.substring(0, unescapeHtml4.indexOf("{}))") + 4)), null, providerSearchResult.getPageUrl(), this.COOKIE, copyOnWriteArrayList, providerSearchResult.getTitle());
                            return;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("{\"sources\":");
                        stringBuilder.append(unescapeHtml4);
                        stringBuilder.append("}");
                        JSONArray jSONArray = new JSONObject(stringBuilder.toString()).getJSONArray("sources");
                        while (i < jSONArray.length()) {
                            String optString = jSONArray.getJSONObject(i).optString("file");
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("(function() { return (");
                            stringBuilder2.append(optString);
                            stringBuilder2.append("); })();");
                            addCDNSource(wveval(stringBuilder2.toString()).replace("\"", ""), null, providerSearchResult.getPageUrl(), this.COOKIE, copyOnWriteArrayList, providerSearchResult.getTitle());
                            i++;
                        }
                    }
                }
            } catch (ProviderSearchResult providerSearchResult2) {
                providerSearchResult2.printStackTrace();
            }
        }
    }
}
