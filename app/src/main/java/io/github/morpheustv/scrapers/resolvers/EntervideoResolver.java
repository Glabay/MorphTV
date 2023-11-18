package io.github.morpheustv.scrapers.resolvers;

import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import io.github.morpheustv.scrapers.model.BaseResolver;
import io.github.morpheustv.scrapers.model.Source;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jsoup.Jsoup;

public class EntervideoResolver extends BaseResolver {
    public EntervideoResolver(Scraper scraper, BaseProvider baseProvider) {
        super(scraper, baseProvider, true);
    }

    public void resolve(String str, String str2, String str3, CopyOnWriteArrayList<Source> copyOnWriteArrayList) {
        if (str != null) {
            try {
                if (str.isEmpty() == null) {
                    str2 = Jsoup.parse(wvgethtml(str)).select("source").first();
                    if (str2 != null) {
                        String str4 = str3;
                        String source = new Source(str4, "ENTERVIDEO", BaseProvider.HD, this.PROVIDER_NAME, str2.attr("src"));
                        source.setReferer(str);
                        addSource(copyOnWriteArrayList, source, null, true);
                    }
                }
            } catch (String str5) {
                str5.printStackTrace();
            }
        }
    }
}
