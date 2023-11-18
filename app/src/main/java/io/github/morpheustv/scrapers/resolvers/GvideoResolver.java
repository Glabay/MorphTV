package io.github.morpheustv.scrapers.resolvers;

import com.google.common.net.HttpHeaders;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.helper.Utils;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.BaseResolver;
import io.github.morpheustv.scrapers.model.Source;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GvideoResolver extends BaseResolver {
    public GvideoResolver(Scraper scraper, BaseProvider baseProvider) {
        super(scraper, baseProvider, false);
    }

    public void resolve(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        try {
            StringBuilder stringBuilder;
            str = redirectOnce(str, str);
            if (str.contains("google.com/file/d/")) {
                str = str.substring(str.indexOf("/d/") + 3, str.lastIndexOf("/"));
                stringBuilder = new StringBuilder();
                stringBuilder.append("https://drive.google.com/uc?export=download&id=");
                stringBuilder.append(str);
                str = stringBuilder.toString();
            }
            if (str.contains("youtube.com/embed")) {
                str = (String) Utils.splitQuery(new URL(str)).get("docid");
                stringBuilder = new StringBuilder();
                stringBuilder.append("https://drive.google.com/uc?export=download&id=");
                stringBuilder.append(str);
                str = stringBuilder.toString();
            }
            if (str.contains("drive.google.com")) {
                Connection referrer = Jsoup.connect(str).ignoreHttpErrors(true).validateTLSCertificates(false).ignoreContentType(true).followRedirects(false).userAgent(UserAgent).timeout(10000).header(HttpHeaders.ACCEPT_ENCODING, "identity;q=1, *;q=0").header(HttpHeaders.RANGE, "bytes=0-").referrer(str);
                Document document = referrer.get();
                Map cookies = referrer.response().cookies();
                String html = document.select("title").html();
                if (!html.toLowerCase().contains("quota exceeded")) {
                    if (html.toLowerCase().contains("virus")) {
                        str = new StringBuilder();
                        str.append("https://drive.google.com");
                        str.append(document.select("a#uc-download-link").attr("href"));
                        str = str.toString();
                        referrer = Jsoup.connect(str).ignoreHttpErrors(true).validateTLSCertificates(false).ignoreContentType(true).followRedirects(false).userAgent(UserAgent).cookies(cookies).timeout(10000).header(HttpHeaders.ACCEPT_ENCODING, "identity;q=1, *;q=0").header(HttpHeaders.RANGE, "bytes=0-").referrer(str);
                        referrer.get();
                    }
                    if (!((referrer.response().statusCode() != 302 && referrer.response().statusCode() != 301) || referrer.response().header(HttpHeaders.LOCATION) == null || referrer.response().header(HttpHeaders.LOCATION).equals("/"))) {
                        str = referrer.response().header(HttpHeaders.LOCATION);
                    }
                } else {
                    return;
                }
            }
            String str4 = str;
            str = googletag(str4);
            if (str.equals(BaseProvider.UNKNOWN_QUALITY)) {
                str = getLabelQuality(str2);
            }
            String str5 = str3;
            addSource(copyOnWriteArrayList, new Source(str5, "GVIDEO", str, this.PROVIDER_NAME, str4), true, true);
        } catch (String str6) {
            str6.printStackTrace();
        }
    }
}
