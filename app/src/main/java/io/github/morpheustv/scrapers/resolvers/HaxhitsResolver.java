package io.github.morpheustv.scrapers.resolvers;

import android.support.graphics.drawable.PathInterpolatorCompat;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.BaseResolver;
import io.github.morpheustv.scrapers.model.Source;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jsoup.Jsoup;

public class HaxhitsResolver extends BaseResolver {
    public HaxhitsResolver(Scraper scraper, BaseProvider baseProvider) {
        super(scraper, baseProvider, false);
        this.resourceWhitelist = new String[]{"(.*chk_jschl?.*)", "(.*jquery.*.js.*)"};
    }

    public void resolve(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        if (str != null) {
            try {
                if (!str.isEmpty()) {
                    str2 = Jsoup.connect(str).validateTLSCertificates(false).timeout(PathInterpolatorCompat.MAX_NUM_POINTS).followRedirects(true).ignoreContentType(true).ignoreHttpErrors(true).referrer(str2).get().html();
                    str2 = str2.substring(str2.indexOf("eval("));
                    str2 = extractLinks(jsunpack(str2.substring(0, str2.indexOf("</script>")))).iterator();
                    while (str2.hasNext()) {
                        String str4 = (String) str2.next();
                        if (str4.contains("googleapis.com")) {
                            processLink(str4, str, null, copyOnWriteArrayList, str3);
                        }
                        if (str4.contains("haxhits.com/gdata") && redirectOnce(str4, str).contains("googleapis.com")) {
                            processLink(str4, str, null, copyOnWriteArrayList, str3);
                        }
                    }
                }
            } catch (String str5) {
                str5.printStackTrace();
            }
        }
    }
}
