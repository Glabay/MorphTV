package io.github.morpheustv.scrapers.providers;

import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.common.net.HttpHeaders;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TV21Provider extends BaseProvider {
    String base_link;
    String[] domains;

    public TV21Provider(Scraper scraper) {
        super(scraper, "TELEVISI21.TV", false);
        this.domains = new String[]{"televisi21.tv"};
        this.base_link = "https://televisi21.tv";
        this.resourceWhitelist = new String[]{"(.*chk_jschl?.*)", "(.*jquery.*)"};
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            for (String str22 : list) {
                String format = String.format("%s/%s-%s", new Object[]{this.base_link, cleantitleurl(str22), str});
                try {
                    Connection timeout = Jsoup.connect(format).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).userAgent(UserAgent).timeout(5000);
                    Response execute = timeout.execute();
                    timeout.cookies(execute.cookies());
                    Document document = timeout.get();
                    if (cleantitleurl(((Element) document.select("title").get(0)).text()).toLowerCase().startsWith(r0.toLowerCase())) {
                        ProviderSearchResult providerSearchResult = new ProviderSearchResult();
                        providerSearchResult.setTitle(str22);
                        providerSearchResult.setYear(str);
                        providerSearchResult.setPageUrl(format);
                        providerSearchResult.setCookie(execute.header(HttpHeaders.COOKIE));
                        providerSearchResult.setContent(document.html());
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

    public void getSources(ProviderSearchResult providerSearchResult, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        if (providerSearchResult != null) {
            try {
                if (!(providerSearchResult.getPageUrl() == null || providerSearchResult.getPageUrl().isEmpty())) {
                    Element first = Jsoup.parse(providerSearchResult.getContent()).select("div#muvipro_player_content_id").first();
                    String attr = first.attr("data-id");
                    Iterator it = first.select("ul.muvipro-player-tabs").first().select("li").iterator();
                    while (it.hasNext()) {
                        try {
                            String attr2 = ((Element) it.next()).select("a").first().attr(TtmlNode.ATTR_ID);
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(this.base_link);
                            stringBuilder.append("/wp-admin/admin-ajax.php");
                            attr2 = Jsoup.connect(stringBuilder.toString()).validateTLSCertificates(false).ignoreHttpErrors(true).ignoreContentType(true).data("action", "muvipro_player_content").data("tab", attr2).data("post_id", attr).header(HttpHeaders.COOKIE, providerSearchResult.getCookie() != null ? providerSearchResult.getCookie() : "").header("referer", this.base_link).header("x-requested-with", "XMLHttpRequest").header("accept", "application/json, text/javascript, */*; q=0.01").userAgent(UserAgent).method(Method.POST).timeout(5000).execute().parse().select("iframe").first().attr("src");
                            if (attr2.startsWith("//")) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("https:");
                                stringBuilder.append(attr2);
                                attr2 = stringBuilder.toString();
                            }
                            processLink(attr2, providerSearchResult.getPageUrl(), null, copyOnWriteArrayList, providerSearchResult.getTitle());
                            if (hasMaxSources(copyOnWriteArrayList)) {
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (ProviderSearchResult providerSearchResult2) {
                providerSearchResult2.printStackTrace();
            }
        }
    }
}
