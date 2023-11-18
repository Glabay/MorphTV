package io.github.morpheustv.scrapers.providers;

import android.net.Uri;
import android.support.graphics.drawable.PathInterpolatorCompat;
import com.google.android.gms.common.internal.ImagesContract;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.ProviderSearchResult;
import io.github.morpheustv.scrapers.model.Source;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class IceFilmsProvider extends BaseProvider {
    String base_link;
    String[] domains;

    public IceFilmsProvider(Scraper scraper) {
        super(scraper, "ICEFILMS.INFO", true);
        this.domains = new String[]{"icefilms.info"};
        this.base_link = "http://www.icefilms.info";
        this.resourceWhitelist = new String[]{"(.*com_iceplayer.*)", "(.*jquery.*.js.*)"};
    }

    public ProviderSearchResult getMovie(List<String> list, String str, String str2) {
        try {
            for (String str3 : list) {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    URL url = new URL(this.base_link);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("/search.php?q=");
                    stringBuilder2.append(cleantitlequery(str3));
                    stringBuilder.append(new URL(url, stringBuilder2.toString()).toString());
                    stringBuilder.append("&x=0&y=0");
                    Iterator it = Jsoup.parse(wvgethtml(stringBuilder.toString())).select("div.title").iterator();
                    String str4 = null;
                    while (it.hasNext()) {
                        Element element = (Element) it.next();
                        String cleantitle = BaseProvider.cleantitle(element.select("a").text());
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(str3);
                        stringBuilder3.append(str);
                        if (cleantitle.equals(BaseProvider.cleantitle(stringBuilder3.toString()))) {
                            str4 = element.select("a").attr("href");
                        }
                    }
                    ProviderSearchResult providerSearchResult;
                    StringBuilder stringBuilder4;
                    if (str4 != null) {
                        providerSearchResult = new ProviderSearchResult();
                        providerSearchResult.setTitle(str3);
                        providerSearchResult.setYear(str);
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append(this.base_link);
                        stringBuilder4.append(str4);
                        providerSearchResult.setPageUrl(stringBuilder4.toString());
                        return providerSearchResult;
                    }
                    String substring = str3.substring(0, 1);
                    if (!Character.isLetter(substring.charAt(0))) {
                        substring = "1";
                    }
                    url = new URL(this.base_link);
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("/movies/a-z/");
                    stringBuilder2.append(substring);
                    it = Jsoup.parse(wvgethtml(new URL(url, stringBuilder2.toString()).toString())).select("a").iterator();
                    while (it.hasNext()) {
                        Element element2 = (Element) it.next();
                        String cleantitle2 = BaseProvider.cleantitle(element2.select("a").text());
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(str3);
                        stringBuilder2.append(str);
                        if (cleantitle2.equals(BaseProvider.cleantitle(stringBuilder2.toString()))) {
                            str4 = element2.select("a").attr("href");
                            if (str4.contains("ip.php")) {
                                break;
                            }
                        }
                    }
                    str4 = null;
                    if (str4 != null) {
                        providerSearchResult = new ProviderSearchResult();
                        providerSearchResult.setTitle(str3);
                        providerSearchResult.setYear(str);
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append(this.base_link);
                        stringBuilder4.append(str4);
                        providerSearchResult.setPageUrl(stringBuilder4.toString());
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
        if (providerSearchResult != null) {
            try {
                if (providerSearchResult.getPageUrl() != null) {
                    for (String str2 : list) {
                        try {
                            StringBuilder stringBuilder = new StringBuilder();
                            URL url = new URL(this.base_link);
                            StringBuilder stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("/search.php?q=");
                            stringBuilder2.append(cleantitlequery(str2));
                            stringBuilder.append(new URL(url, stringBuilder2.toString()).toString());
                            stringBuilder.append("&x=0&y=0");
                            Iterator it = Jsoup.parse(wvgethtml(stringBuilder.toString())).select("div.title").iterator();
                            String str3 = null;
                            while (it.hasNext()) {
                                Element element = (Element) it.next();
                                String cleantitle = BaseProvider.cleantitle(element.select("a").text());
                                StringBuilder stringBuilder3 = new StringBuilder();
                                stringBuilder3.append(str2);
                                stringBuilder3.append(str);
                                if (cleantitle.equals(BaseProvider.cleantitle(stringBuilder3.toString()))) {
                                    str3 = element.select("a").attr("href");
                                }
                            }
                            ProviderSearchResult providerSearchResult2;
                            if (str3 != null) {
                                providerSearchResult2 = new ProviderSearchResult();
                                providerSearchResult2.setTitle(str2);
                                providerSearchResult2.setYear(str);
                                list = new StringBuilder();
                                list.append(this.base_link);
                                list.append(str3);
                                providerSearchResult2.setPageUrl(list.toString());
                                providerSearchResult2.setEpisode(i2);
                                providerSearchResult2.setSeason(i);
                                return providerSearchResult2;
                            }
                            String substring = str2.substring(0, 1);
                            if (!Character.isLetter(substring.charAt(0))) {
                                substring = "1";
                            }
                            url = new URL(this.base_link);
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("/tv/a-z/");
                            stringBuilder2.append(substring);
                            it = Jsoup.parse(wvgethtml(new URL(url, stringBuilder2.toString()).toString())).select("a").iterator();
                            while (it.hasNext()) {
                                Element element2 = (Element) it.next();
                                String cleantitle2 = BaseProvider.cleantitle(element2.select("a").text());
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(str2);
                                stringBuilder2.append(str);
                                if (cleantitle2.equals(BaseProvider.cleantitle(stringBuilder2.toString()))) {
                                    str3 = element2.select("a").attr("href");
                                    if (str3.contains("/tv/series/")) {
                                        break;
                                    }
                                }
                            }
                            str3 = null;
                            if (str3 != null) {
                                providerSearchResult2 = new ProviderSearchResult();
                                providerSearchResult2.setTitle(str2);
                                providerSearchResult2.setYear(str);
                                list = new StringBuilder();
                                list.append(this.base_link);
                                list.append(str3);
                                providerSearchResult2.setPageUrl(list.toString());
                                providerSearchResult2.setEpisode(i2);
                                providerSearchResult2.setSeason(i);
                                return providerSearchResult2;
                            }
                        } catch (List<String> list2) {
                            list2.printStackTrace();
                        }
                    }
                }
            } catch (ProviderSearchResult providerSearchResult3) {
                providerSearchResult3.printStackTrace();
            }
        }
        return null;
    }

    public void getSources(ProviderSearchResult providerSearchResult, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        IceFilmsProvider iceFilmsProvider = this;
        if (providerSearchResult != null) {
            try {
                String attr;
                StringBuilder stringBuilder;
                Document parse = Jsoup.parse(wvgethtml(providerSearchResult.getPageUrl()));
                int i = 0;
                if (providerSearchResult.getEpisode() > 0 && providerSearchResult.getSeason() > 0) {
                    Iterator it = parse.select("span.list a").iterator();
                    while (it.hasNext()) {
                        Element element = (Element) it.next();
                        String text = element.text();
                        attr = element.attr("href");
                        if (text.toLowerCase().startsWith(String.format("%dx%02d", new Object[]{Integer.valueOf(providerSearchResult.getSeason()), Integer.valueOf(providerSearchResult.getEpisode())}))) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(iceFilmsProvider.base_link);
                            stringBuilder.append(attr);
                            parse = Jsoup.parse(wvgethtml(stringBuilder.toString()));
                            break;
                        }
                    }
                }
                Iterator it2 = parse.select("iframe#videoframe").iterator();
                while (it2.hasNext()) {
                    Element element2 = (Element) it2.next();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(iceFilmsProvider.base_link);
                    stringBuilder.append(element2.attr("src"));
                    String stringBuilder2 = stringBuilder.toString();
                    String queryParameter = Uri.parse(stringBuilder2).getQueryParameter("vid");
                    String wvgethtmlbeforejs = wvgethtmlbeforejs(stringBuilder2);
                    parse = Jsoup.parse(wvgethtmlbeforejs);
                    String html = parse.html();
                    int indexOf = html.indexOf("f.secret.value=");
                    String substring = html.substring(indexOf + 16, html.indexOf(";", indexOf) - 1);
                    Iterator it3 = parse.select("div#srclist a").iterator();
                    while (it3.hasNext()) {
                        Iterator it4;
                        String str;
                        Element element3 = (Element) it3.next();
                        StringBuilder stringBuilder3 = new StringBuilder();
                        stringBuilder3.append(iceFilmsProvider.base_link);
                        stringBuilder3.append(element2.attr("src"));
                        wvsethtml(wvgethtmlbeforejs, stringBuilder3.toString());
                        html = element3.text();
                        stringBuilder2 = element3.attr("onclick").replace("go(", "").replace(")", "");
                        StringBuilder stringBuilder4 = new StringBuilder();
                        stringBuilder4.append(iceFilmsProvider.base_link);
                        stringBuilder4.append("/membersonly/components/com_iceplayer/video.php-link.php?s=%s&t=%s");
                        attr = String.format(stringBuilder4.toString(), new Object[]{stringBuilder2, queryParameter});
                        StringBuilder stringBuilder5 = new StringBuilder();
                        stringBuilder5.append("(function() { var id = ");
                        stringBuilder5.append(stringBuilder2);
                        stringBuilder5.append("; var url = '");
                        stringBuilder5.append(attr);
                        stringBuilder5.append("'; f.secret.value='");
                        stringBuilder5.append(substring);
                        stringBuilder5.append("'; f.id.value = id; f.s.value = s; f.m.value = m; $('form[name=frm]').attr('action',url).submit(); })();");
                        stringBuilder2 = stringBuilder5.toString();
                        if (html.toLowerCase().contains("openload")) {
                            attr = wvmatchresourceafterjs("(.+openload.co.+)", PathInterpolatorCompat.MAX_NUM_POINTS, stringBuilder2);
                            if (!(attr == null || attr.isEmpty())) {
                                html = Uri.parse(attr).getQueryParameter(ImagesContract.URL);
                                if (!(html == null || html.isEmpty())) {
                                    it4 = it3;
                                    str = substring;
                                    processLink(html, attr, null, copyOnWriteArrayList, providerSearchResult.getTitle());
                                }
                            }
                            it4 = it3;
                            str = substring;
                        } else {
                            it4 = it3;
                            str = substring;
                            if (html.toLowerCase().contains("vidlox")) {
                                attr = wvmatchresourceafterjs("(.+vidlox.me.+)", PathInterpolatorCompat.MAX_NUM_POINTS, stringBuilder2);
                                if (!(attr == null || attr.isEmpty())) {
                                    html = Uri.parse(attr).getQueryParameter(ImagesContract.URL);
                                    if (!(html == null || html.isEmpty())) {
                                        processLink(html, attr, null, copyOnWriteArrayList, providerSearchResult.getTitle());
                                    }
                                }
                            } else if (html.toLowerCase().contains("vidoza")) {
                                attr = wvmatchresourceafterjs("(.+vidoza.net.+)", PathInterpolatorCompat.MAX_NUM_POINTS, stringBuilder2);
                                if (!(attr == null || attr.isEmpty())) {
                                    html = Uri.parse(attr).getQueryParameter(ImagesContract.URL);
                                    if (!(html == null || html.isEmpty())) {
                                        processLink(html, attr, null, copyOnWriteArrayList, providerSearchResult.getTitle());
                                    }
                                }
                            }
                        }
                        if (!hasMaxSources(copyOnWriteArrayList)) {
                            it3 = it4;
                            substring = str;
                            i = 0;
                        } else {
                            return;
                        }
                    }
                    CopyOnWriteArrayList<Source> copyOnWriteArrayList2 = copyOnWriteArrayList;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
